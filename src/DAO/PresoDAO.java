package DAO;

import Controller.ActividadController;
import Controller.SancionController;
import Model.Constants.EstadoActividadesPresoEnum;
import Model.Constants.EstadoExpedienteEnum;
import Model.Constants.EstadoPresoEnum;
import Model.Constants.EstadoVisitaEnum;
import Model.Constants.EstadoVisitanteEnum;
import Model.Entities.Actividad;
import Model.Entities.Celda;
import Model.Entities.ExpedienteJudicial;
import Model.Entities.Delito;
import Model.Entities.Preso;
import Model.Entities.Sancion;
import Model.Entities.Visita;
import Model.Entities.Visitante;
import Utilidades.EmailSender;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.reflect.TypeToken;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;

public class PresoDAO {

    private static IntentoFugaDAO intentoFugaDAO = IntentoFugaDAO.getInstancia();
    private static DelitoDAO delitoDAO = DelitoDAO.getInstancia();
    private static ExpedienteDAO expedienteDAO = ExpedienteDAO.getInstancia();
    private static ActividadDAO actividadDAO = ActividadDAO.getInstancia();
    private static PresoDAO instancia;

    private static final String JSON_FILE = "src/Resources/DATA/presos.json/";
    private static final String RUTA_IMAGENES = "src/Resources/imagenes_presos/";

    private Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    public static synchronized PresoDAO getInstancia() {
        if (instancia == null) {
            instancia = new PresoDAO();
        }
        return instancia;
    }

    private static class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

        @Override
        public JsonElement serialize(LocalDate date, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(date.toString());
        }

        @Override
        public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return LocalDate.parse(json.getAsString());
        }
    }

    private int obtenerProximoId() {
        List<Preso> presos = cargarTodos();
        if (presos.isEmpty()) {
            return 1;
        }

        int maxId = presos.stream()
                .mapToInt(Preso::getId)
                .max()
                .orElse(0);

        return maxId + 1;
    }

    public List<Preso> cargarTodos() {
        File archivo = new File(JSON_FILE);
        archivo.getParentFile().mkdirs();

        if (!archivo.exists()) {
            try {
                archivo.createNewFile();
                guardarTodos(new ArrayList<>());
                return new ArrayList<>();
            } catch (IOException e) {
                System.err.println("Error al crear archivo JSON: " + e.getMessage());
                return new ArrayList<>();
            }
        }

        if (archivo.length() == 0) {
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(JSON_FILE)) {
            Type tipoListaPreso = new TypeToken<ArrayList<Preso>>() {
            }.getType();
            List<Preso> presos = gson.fromJson(reader, tipoListaPreso);

            SancionController sc = new SancionController();
            for (Preso p : presos) {
            }

            return presos != null ? presos : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Error al leer archivo JSON: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public boolean guardarPreso(Preso preso, File imagenSeleccionada) {
        if (preso == null) {
            System.err.println("Error: El preso no puede ser nulo");
            return false;
        }
        List<Preso> presos = cargarTodos();

        if (preso.getId() == 0) {
            preso.setId(obtenerProximoId());
        }

        if (imagenSeleccionada != null) {
            try {
                String nombreArchivo = "preso_" + preso.getId() + getExtension(imagenSeleccionada.getName());
                String rutaDestino = RUTA_IMAGENES + nombreArchivo;

                new File(RUTA_IMAGENES).mkdirs();

                Files.copy(imagenSeleccionada.toPath(),
                        new File(rutaDestino).toPath(),
                        StandardCopyOption.REPLACE_EXISTING);

                preso.setFotoPath(rutaDestino);
            } catch (IOException e) {
                System.err.println("Error al guardar la imagen: " + e.getMessage());
                preso.setFotoPath(null);
            }
        }

        boolean existe = false;
        for (int i = 0; i < presos.size(); i++) {
            if (presos.get(i).getId() == preso.getId()) {
                presos.set(i, preso);
                existe = true;
                break;
            }
        }

        if (!existe) {
            presos.add(preso);
        }
        try {
            guardarTodos(presos);
            return true;
        } catch (Exception e) {
            System.err.println("Error al guardar la lista de presos: " + e.getMessage());
            return false;
        }
    }

    private String getExtension(String filename) {
        int lastDot = filename.lastIndexOf('.');
        return (lastDot == -1) ? "" : filename.substring(lastDot);
    }

    private boolean guardarTodos(List<Preso> presos) {
        try (Writer writer = new FileWriter(JSON_FILE)) {
            gson.toJson(presos, writer);
            return true;
        } catch (IOException e) {
            System.err.println("Error al guardar en archivo JSON: " + e.getMessage());
            return false;
        }
    }

    public Preso buscarPresoPorIdentificacion(String identificacion) {
        List<Preso> presos = cargarTodos();
        for (Preso preso : presos) {
            if (preso.getIdentificacion().equals(identificacion)) {
                List<Delito> delitos = delitoDAO.obtenerDelitosPorPreso(preso.getIdentificacion());
                preso.setDelitos(delitos);
                new SancionController().verificarFinAislamiento(preso);

                return preso;
            }
        }
        return null;

    }

    public boolean eliminarPreso(String numeroIdentificacion) {
        List<Preso> presos = cargarTodos();

        boolean eliminado = presos.removeIf(preso
                -> preso.getIdentificacion().equals(numeroIdentificacion));

        if (eliminado) {
            try (Writer writer = new FileWriter(JSON_FILE)) {
                gson.toJson(presos, writer);
                return true;
            } catch (IOException e) {
                System.err.println("Error al guardar los cambios después de eliminar: " + e.getMessage());
                return false;
            }
        }

        return eliminado;
    }

    public boolean actualizarPreso(
            String identificacionOriginal,
            String nuevoPrimerNombre,
            String nuevoSegundoNombre,
            String nuevoPrimerApellido,
            String nuevoSegundoApellido,
            Integer nuevaEdad,
            String nuevoSexo,
            String nuevaNacionalidad,
            Float nuevaEstatura,
            Float nuevoPeso,
            String nuevoGrupoSanguineo,
            String nuevaSeccionAsignada,
            String nuevoNivelSeguridad,
            Boolean nuevoEnAislamiento,
            String nuevoNivelRiesgo,
            File nuevaFoto) {

        List<Preso> presos = cargarTodos();
        boolean encontrado = false;

        for (Preso preso : presos) {
            if (preso.getIdentificacion().equals(identificacionOriginal)) {
                encontrado = true;

                if (nuevoPrimerNombre != null) {
                    preso.setPrimerNombre(nuevoPrimerNombre);
                }
                if (nuevoSegundoNombre != null) {
                    preso.setSegundoNombre(nuevoSegundoNombre);
                }
                if (nuevoPrimerApellido != null) {
                    preso.setPrimerApellido(nuevoPrimerApellido);
                }
                if (nuevoSegundoApellido != null) {
                    preso.setSegundoApellido(nuevoSegundoApellido);
                }
                if (nuevaEdad != null) {
                    preso.setEdad(nuevaEdad);
                }
                if (nuevoSexo != null) {
                    preso.setSexo(nuevoSexo);
                }
                if (nuevaNacionalidad != null) {
                    preso.setNacionalidad(nuevaNacionalidad);
                }
                if (nuevaEstatura != null) {
                    preso.setEstatura(nuevaEstatura);
                }
                if (nuevoPeso != null) {
                    preso.setPeso(nuevoPeso);
                }
                if (nuevoGrupoSanguineo != null) {
                    preso.setGrupoSanguineo(nuevoGrupoSanguineo);
                }

                if (nuevoEnAislamiento != null && nuevoEnAislamiento && !preso.isEnAislamiento()) {

                    if (preso.getEstado() == EstadoPresoEnum.FUGADO) {
                        JOptionPane.showMessageDialog(null,
                                "El preso no puede ser puesto en aislamiento mientras esté marcado como FUGADO.\nDebe registrarse su reingreso primero.",
                                "Acción no permitida", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }

                    List<String> actividadesAsignadas = new ArrayList<>(preso.getActividadesAsignadasIds());

                    for (String idActividad : actividadesAsignadas) {
                        actividadDAO.removerPresoDeActividad(idActividad, preso.getIdentificacion());
                    }

                    JOptionPane.showMessageDialog(null,
                            "El preso ha sido puesto en aislamiento. Todas sus actividades han sido canceladas.",
                            "Actividades canceladas", JOptionPane.WARNING_MESSAGE);
                }

                if (nuevaSeccionAsignada != null
                        || nuevoNivelSeguridad != null || nuevoEnAislamiento != null
                        || nuevoNivelRiesgo != null) {

                    if (!actualizarDatosJudiciales(preso, nuevaSeccionAsignada,
                            nuevoNivelSeguridad, nuevoEnAislamiento,
                            nuevoNivelRiesgo)) {
                        return false;
                    }
                }

                if (nuevaFoto != null) {
                    actualizarFotoPreso(preso, nuevaFoto);
                }

                break;
            }
        }

        if (!encontrado) {
            JOptionPane.showMessageDialog(null,
                    "Preso no encontrado: " + identificacionOriginal,
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return guardarCambios(presos);
    }

    private boolean actualizarDatosJudiciales(Preso preso, String nuevaSeccion,
            String nivelSeguridad, Boolean enAislamiento,
            String nivelRiesgo) {
        CeldaDAO celdaDAO = new CeldaDAO();

        if (nuevaSeccion != null && !nuevaSeccion.equals(preso.getSeccionAsignada())) {
            Celda nuevaCelda = celdaDAO.asignarCeldaDisponible(nuevaSeccion);
            if (nuevaCelda == null) {
                JOptionPane.showMessageDialog(null,
                        "No hay celdas disponibles en la sección " + nuevaSeccion,
                        "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            celdaDAO.liberarCelda(preso.getCeldaAsignada());
            preso.setCeldaAsignada(nuevaCelda.getNombreFormateado());
            preso.setSeccionAsignada(nuevaSeccion);
        }

        if (nivelSeguridad != null) {
            preso.setNivelDeSeguridad(nivelSeguridad);
        }
        if (enAislamiento != null) {
            preso.setEnAislamiento(enAislamiento);
        }
        if (nivelRiesgo != null) {
            preso.setNivelDeRiesgo(nivelRiesgo);
        }

        return true;
    }

    public void actualizarFotoPreso(Preso preso, File nuevaFoto) {
        try {
            String extension = nuevaFoto.getName().substring(nuevaFoto.getName().lastIndexOf("."));
            String nombreArchivo = "preso_" + preso.getId() + extension;
            String rutaDestino = RUTA_IMAGENES + nombreArchivo;

            new File(RUTA_IMAGENES).mkdirs();
            Files.copy(nuevaFoto.toPath(), new File(rutaDestino).toPath(), StandardCopyOption.REPLACE_EXISTING);
            preso.setFotoPath(rutaDestino);
        } catch (IOException e) {
            System.err.println("Error al guardar imagen: " + e.getMessage());
        }
    }

    private boolean guardarCambios(List<Preso> presos) {
        return guardarTodos(presos);
    }

    public boolean existePresoConIdentificacion(String identificacion) {
        List<Preso> presos = cargarTodos();
        for (Preso preso : presos) {
            if (preso.getIdentificacion().equals(identificacion)) {
                return true;
            }
        }
        return false;
    }

    public List<Preso> buscarPorSeccion(String seccion) {
        List<Preso> todos = cargarTodos();
        List<Preso> filtrados = new ArrayList<>();

        for (Preso preso : todos) {
            if (seccion != null && seccion.equalsIgnoreCase(preso.getSeccionAsignada())
                    && preso.getEstado() == EstadoPresoEnum.ACTIVO) {
                filtrados.add(preso);
            }
        }
        return filtrados;
    }

    public boolean agregarActividadAsignada(String idPreso, String idActividad) {
        List<Preso> presos = cargarTodos();

        try {
            for (Preso preso : presos) {
                if (preso.getIdentificacion().equals(idPreso)) {
                    preso.agregarActividadAsignada(idActividad);
                    guardarTodos(presos);
                    return true;
                }
            }
            return false;
        } catch (RuntimeException e) {
            System.err.println("Error al agregar actividad: " + e.getMessage());
            return false;
        }
    }

    public boolean marcarActividadCancelada(String idPreso, String idActividad) {
        List<Preso> presos = cargarTodos();

        try {
            for (Preso preso : presos) {
                if (preso.getIdentificacion().equals(idPreso)) {
                    preso.marcarActividadCancelada(idActividad);
                    guardarTodos(presos);
                    return true;
                }
            }
            return false;
        } catch (RuntimeException e) {
            System.err.println("Error al marcar actividad como cancelada: " + e.getMessage());
            return false;
        }
    }

public boolean cambiarEstadoPreso(String identificacion, EstadoPresoEnum nuevoEstado, LocalDate fechaCambio) {
    List<Preso> presos = cargarTodos();
    ActividadController actividadController = ActividadController.getInstancia();
    VisitaDAO visitaDAO = VisitaDAO.getInstancia();
    SancionDAO sancionDAO = SancionDAO.getInstancia(); 

    for (Preso preso : presos) {
        if (preso.getIdentificacion().equals(identificacion)) {
            EstadoPresoEnum estadoActual = preso.getEstado();

            List<Actividad> actividadesPreso = actividadController.buscarActividadesPorPreso(identificacion);

            preso.setEstado(nuevoEstado);

            switch (nuevoEstado) {
                case FUGADO:
                    preso.setFechaFuga(fechaCambio);
                    intentoFugaDAO.registrarFuga(identificacion, fechaCambio);

                    if (preso.isEnAislamiento()) {
                        preso.setEnAislamiento(false);
                    }

                    for (Actividad actividad : actividadesPreso) {
                        actividadDAO.removerPresoDeActividad(actividad.getIdActividad(), identificacion);
                    }

                    cancelarVisitasPreso(identificacion, "El preso ha sido marcado como FUGADO");

                    cancelarSancionesActivas(identificacion, sancionDAO);
                    break;

                case LIBERADO:
                    preso.setFechaLiberacion(fechaCambio);

                    for (Actividad actividad : actividadesPreso) {
                        actividadDAO.removerPresoDeActividad(actividad.getIdActividad(), identificacion);
                    }

                    ExpedienteJudicial expedienteAbierto = expedienteDAO.obtenerExpedienteAbierto(identificacion);
                    if (expedienteAbierto != null) {
                        expedienteAbierto.setEstado(EstadoExpedienteEnum.CERRADO);
                        expedienteDAO.actualizarExpediente(expedienteAbierto);
                    }

                    cancelarVisitasPreso(identificacion, "El preso ha sido LIBERADO");

                    cancelarSancionesActivas(identificacion, sancionDAO);
                    break;

                case FALLECIDO:
                    preso.setFechaDefuncion(fechaCambio);

                    for (Actividad actividad : actividadesPreso) {
                        actividadDAO.removerPresoDeActividad(actividad.getIdActividad(), identificacion);
                    }

                    ExpedienteJudicial expedienteAbiertoFallecido = expedienteDAO.obtenerExpedienteAbierto(identificacion);
                    if (expedienteAbiertoFallecido != null) {
                        expedienteAbiertoFallecido.setEstado(EstadoExpedienteEnum.CERRADO);
                        expedienteDAO.actualizarExpediente(expedienteAbiertoFallecido);
                    }

                    cancelarVisitasPreso(identificacion, "El preso ha FALLECIDO");

                    cancelarSancionesActivas(identificacion, sancionDAO);
                    break;

                case ACTIVO:
                    if (estadoActual == EstadoPresoEnum.FUGADO) {
                        intentoFugaDAO.registrarReingreso(identificacion, fechaCambio);
                    }
                    preso.setFechaFuga(null);
                    preso.setFechaLiberacion(null);
                    preso.setFechaDefuncion(null);
                    break;
            }

            return guardarCambios(presos);
        }
    }
    return false;
}


 private void cancelarSancionesActivas(String identificacionPreso, SancionDAO sancionDAO) {
        List<Sancion> sancionesActivas = sancionDAO.obtenerSancionesActivasPorPreso(identificacionPreso);

        for (Sancion sancion : sancionesActivas) {
            sancion.cancelar();
            sancionDAO.actualizarSancion(sancion);
        }
    }

private void cancelarVisitasPreso(String identificacionPreso, String razon) {
    int cantidadVisitasCanceladas = VisitaDAO.getInstancia()
                                .cancelarVisitasPreso(identificacionPreso, razon);
    
    if (cantidadVisitasCanceladas > 0) {
        List<Visita> visitasRecientesCanceladas = VisitaDAO.getInstancia()
            .cargarPorIdentificacionPreso(identificacionPreso)
            .stream()
            .filter(v -> v.getEstado() == EstadoVisitaEnum.CANCELADA && 
                        v.getRazonCancelacion().equals(razon) &&
                        v.getFechaVisita().isAfter(LocalDate.now().minusDays(7))) 
            .collect(Collectors.toList());
            
        for (Visita visita : visitasRecientesCanceladas) {
            notificarCancelacionVisita(visita);
        }
    }
}
private void notificarCancelacionVisita(Visita visita) {
    for (Visitante visitante : visita.getVisitantesConRelacion().keySet()) {
        visitante.setEstado(EstadoVisitanteEnum.HABILITADO);
        VisitanteDAO.getInstancia().guardarVisitante(visitante, null);
        
        if (visitante.getEdad() >= 18 && visitante.getEmail() != null) {
            EmailSender.getInstancia().enviarNotificacionCancelacion(
                visitante, 
                visita, 
                visita.getRazonCancelacion()
            );
        }
    }
}

   

    public boolean marcarComoLiberado(String numeroIdentificacion) {
        return cambiarEstadoPreso(numeroIdentificacion, EstadoPresoEnum.LIBERADO, LocalDate.now());
    }

    public boolean marcarComoFallecido(String numeroIdentificacion, LocalDate fechaDefuncion) {
        return cambiarEstadoPreso(numeroIdentificacion, EstadoPresoEnum.FALLECIDO, fechaDefuncion);
    }

    public boolean marcarComoFugado(String numeroIdentificacion, LocalDate fechaFuga) {
        return cambiarEstadoPreso(numeroIdentificacion, EstadoPresoEnum.FUGADO, fechaFuga);
    }

    public List<Preso> buscarPorEstado(EstadoPresoEnum estado) {
        List<Preso> todos = cargarTodos();
        List<Preso> filtrados = new ArrayList<>();

        for (Preso preso : todos) {
            if (preso.getEstado() == estado) {
                filtrados.add(preso);
            }
        }
        return filtrados;
    }

    public boolean actualizarPreso(Preso preso) {
        List<Preso> presos = cargarTodos();

        try {
            boolean encontrado = false;
            for (int i = 0; i < presos.size(); i++) {
                if (presos.get(i).getIdentificacion().equals(preso.getIdentificacion())) {
                    presos.set(i, preso);
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado) {
                return false;
            }

            guardarTodos(presos);
            return true;

        } catch (RuntimeException e) {
            System.err.println("Error al actualizar preso: " + e.getMessage());
            return false;
        }

    }

    public int contarPresosFallecidos() {
        int contador = 0;
        for (Preso preso : cargarTodos()) {
            if (preso.getEstado() == EstadoPresoEnum.FALLECIDO) {
                contador++;
            }
        }
        return contador;
    }

    public int contarPresosLiberados() {
        int contador = 0;
        for (Preso preso : cargarTodos()) {
            if (preso.getEstado() == EstadoPresoEnum.LIBERADO) {
                contador++;
            }
        }
        return contador;
    }
}
