package Controller;

import DAO.CeldaDAO;
import DAO.DelitoDAO;
import DAO.PresoDAO;
import Model.Celda;
import Model.Delito;
import Model.ExpedienteJudicial;
import Model.Preso;
import Model.Sentencia;
import Utilidades.Validador;
import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class PresoController {

    private static PresoController instancia;

    private final PresoDAO presoDAO;
    private final CeldaDAO celdaDAO;
    private final DelitoDAO delitoDAO;
    private final Validador validador;

    private PresoController(PresoDAO presoDAO, CeldaDAO celdaDAO, DelitoDAO delitoDAO, Validador vañidador) {
        this.presoDAO = presoDAO;
        this.celdaDAO = celdaDAO;
        this.delitoDAO = delitoDAO;
        this.validador = vañidador;
    }

    public static synchronized PresoController getInstancia() {
        if (instancia == null) {
            instancia = new PresoController(
                    PresoDAO.getInstancia(),
                    CeldaDAO.getInstancia(),
                    DelitoDAO.getInstancia(),
                    Validador.getInstancia()
            );
        }
        return instancia;
    }

    public boolean hayCambios(Preso presoOriginal,
            String primerNombre,
            String segundoNombre,
            String primerApellido,
            String segundoApellido,
            String edad,
            String estatura,
            String peso,
            Object nacionalidad,
            Object grupoSanguineo,
            Object nivelSeguridad,
            Object aislamiento,
            Object nivelRiesgo,
            Object imagen) {

        return Stream.of(
                !primerNombre.trim().isEmpty() && !primerNombre.equals(presoOriginal.getPrimerNombre()),
                !segundoNombre.trim().isEmpty() && !segundoNombre.equals(presoOriginal.getSegundoNombre()),
                !primerApellido.trim().isEmpty() && !primerApellido.equals(presoOriginal.getPrimerApellido()),
                !segundoApellido.trim().isEmpty() && !segundoApellido.equals(presoOriginal.getSegundoApellido()),
                !edad.trim().isEmpty() && Integer.parseInt(edad.trim()) != presoOriginal.getEdad(),
                !estatura.trim().isEmpty() && Float.parseFloat(estatura.trim()) != presoOriginal.getEstatura(),
                !peso.trim().isEmpty() && Float.parseFloat(peso.trim()) != presoOriginal.getPeso(),
                nacionalidad != null && !nacionalidad.toString().equals("<Seleccione>"),
                grupoSanguineo != null && !grupoSanguineo.toString().equals("<Seleccione>")
                && !grupoSanguineo.toString().equals(presoOriginal.getGrupoSanguineo()),
                nivelSeguridad != null && !nivelSeguridad.toString().equals("<Seleccionar>")
                && !nivelSeguridad.toString().equals(presoOriginal.getNivelDeSeguridad()),
                nivelRiesgo != null && !nivelRiesgo.toString().equals("<Seleccionar>")
                && !nivelRiesgo.toString().equals(presoOriginal.getNivelDeRiesgo()),
                aislamiento != null && aislamiento.toString().equalsIgnoreCase("Sí") != presoOriginal.isEnAislamiento(),
                imagen != null
        ).anyMatch(Boolean::booleanValue);
    }

    public boolean actualizarPreso(Preso presoOriginal,
            String primerNombre,
            String segundoNombre,
            String primerApellido,
            String segundoApellido,
            String edad,
            Object nacionalidad,
            String estatura,
            String peso,
            Object grupoSanguineo,
            Object nivelSeguridad,
            Object aislamiento,
            Object nivelRiesgo,
            File selectedImageFile) {

        try {
            if (!hayCambios(presoOriginal, primerNombre, segundoNombre, primerApellido, segundoApellido,
                    edad, estatura, peso, nacionalidad, grupoSanguineo, nivelSeguridad, aislamiento, nivelRiesgo, selectedImageFile)) {
                Validador.mostrarAdvertencia("No hay cambios para guardar");
                return false;
            }

            if (!primerNombre.trim().isEmpty()) {
                Validador.validarNombre(primerNombre);
            }
            if (!segundoNombre.trim().isEmpty()) {
                Validador.validarNombre(segundoNombre);
            }
            if (!primerApellido.trim().isEmpty()) {
                Validador.validarNombre(primerApellido);
            }
            if (!segundoApellido.trim().isEmpty()) {
                Validador.validarNombre(segundoApellido);
            }
            if (!edad.trim().isEmpty()) {
                Validador.validarEdad(edad);
            }
            if (!estatura.trim().isEmpty()) {
                Validador.validarEstatura(estatura);
            }
            if (!peso.trim().isEmpty()) {
                Validador.validarPeso(peso);
            }

            boolean resultado = presoDAO.actualizarPreso(
                    presoOriginal.getIdentificacion(),
                    primerNombre.trim().isEmpty() ? presoOriginal.getPrimerNombre() : primerNombre.trim(),
                    segundoNombre.trim().isEmpty() ? presoOriginal.getSegundoNombre() : segundoNombre.trim(),
                    primerApellido.trim().isEmpty() ? presoOriginal.getPrimerApellido() : primerApellido.trim(),
                    segundoApellido.trim().isEmpty() ? presoOriginal.getSegundoApellido() : segundoApellido.trim(),
                    edad.trim().isEmpty() ? presoOriginal.getEdad() : Integer.parseInt(edad.trim()),
                    presoOriginal.getSexo(),
                    nacionalidad == null || nacionalidad.toString().equals("<Seleccione>")
                    ? presoOriginal.getNacionalidad() : nacionalidad.toString(),
                    estatura.trim().isEmpty() ? presoOriginal.getEstatura() : Float.parseFloat(estatura.trim()),
                    peso.trim().isEmpty() ? presoOriginal.getPeso() : Float.parseFloat(peso.trim()),
                    grupoSanguineo == null || grupoSanguineo.toString().equals("<Seleccionar>")
                    ? presoOriginal.getGrupoSanguineo() : grupoSanguineo.toString(),
                    presoOriginal.getSeccionAsignada(),
                    nivelSeguridad == null || nivelSeguridad.toString().equals("<Seleccionar>")
                    ? presoOriginal.getNivelDeSeguridad() : nivelSeguridad.toString(),
                    aislamiento != null && aislamiento.toString().equalsIgnoreCase("Sí"),
                    nivelRiesgo == null || nivelRiesgo.toString().equals("<Seleccionar>")
                    ? presoOriginal.getNivelDeRiesgo() : nivelRiesgo.toString(),
                    selectedImageFile != null ? selectedImageFile : new File(presoOriginal.getFotoPath())
            );

            if (resultado) {
                Validador.mostrarInfo("Preso actualizado correctamente");
            } else {
                Validador.mostrarError("No se pudo actualizar el preso");
            }

            return resultado;

        } catch (Exception e) {
            Validador.mostrarError("Error al actualizar: " + e.getMessage());
            return false;
        }
    }

    public boolean registrarPresoCompleto(
            String primerNombre,
            String segundoNombre,
            String primerApellido,
            String segundoApellido,
            String edadStr,
            String nacionalidad,
            String sexo,
            String estaturaStr,
            String pesoStr,
            Object grupoSanguineo,
            Object identificacion,
            Object seccion,
            Object nivelRiesgo,
            Object nivelSeguridad,
            List<Delito> delitos,
            File imagen) {

        try {
            Validador.validarCampoObligatorio("primer nombre", primerNombre);
            Validador.validarCampoObligatorio("primer apellido", primerApellido);
            Validador.validarCampoObligatorio("edad", edadStr);
            Validador.validarCampoObligatorio("nacionalidad", nacionalidad);
            Validador.validarCampoObligatorio("estatura", estaturaStr);
            Validador.validarCampoObligatorio("peso", pesoStr);

            Validador.validarNombre(primerNombre);
            Validador.validarNombre(primerApellido);
            validador.validarIdentificacionUnica((String) identificacion);

            Validador.validarEdad(edadStr);

            float estatura = Float.parseFloat(estaturaStr);
            Validador.validarEstatura(estaturaStr);

            float peso = Float.parseFloat(pesoStr);
            Validador.validarPeso(pesoStr);

            Validador.validarSeleccion("grupo sanguíneo", grupoSanguineo, "<Seleccionar>");
            Validador.validarSeleccion("sección", seccion, "<Seleccionar>");
            Validador.validarSeleccion("nivel de riesgo", nivelRiesgo, "<Seleccionar>");
            Validador.validarSeleccion("nivel de seguridad", nivelSeguridad, "<Seleccionar>");

            Validador.validarSeleccion("identificación", identificacion, "<Seleccionar>");
            Validador.validarListaNoVacia("delitos", delitos);

            Validador.validarImagen(imagen);

            String seccionStr = seccion.toString();
            int celdasDisponibles = celdaDAO.obtenerCeldasDisponibles(seccionStr);
            if (celdasDisponibles <= 0) {
                throw new IllegalArgumentException("No hay celdas disponibles en la sección " + seccionStr);
            }

            Celda celdaAsignada = celdaDAO.asignarCeldaDisponible(seccionStr);
            if (celdaAsignada == null) {
                throw new RuntimeException("No se pudo asignar celda automáticamente");
            }

            Preso nuevoPreso = new Preso(
                    primerNombre, segundoNombre, primerApellido, segundoApellido,
                    Integer.parseInt(edadStr), sexo, nacionalidad, (String) identificacion, estatura, peso,
                    new ArrayList<>(),
                    nivelSeguridad.toString(), seccionStr, "En espera",
                    celdaAsignada.getNombreFormateado(), false,
                    nivelRiesgo.toString(), 0, grupoSanguineo.toString(), null, 0
            );

            boolean presoGuardado = presoDAO.guardarPreso(nuevoPreso, imagen);

            if (presoGuardado) {
                for (Delito delito : delitos) {
                    delito.setPresoId((String) identificacion);
                    delitoDAO.guardarDelito(delito);
                }

                Validador.mostrarInfo("Preso registrado correctamente");
                return true;
            } else {
                Validador.mostrarError("No se pudo guardar el preso");
                return false;
            }

        } catch (IllegalArgumentException e) {
            Validador.mostrarError(e.getMessage());
            return false;
        } catch (Exception e) {
            Validador.mostrarError("Error inesperado al registrar preso: " + e.getMessage());
            return false;
        }
    }

    public boolean agregarDelitoAPreso(Preso preso, String codigoStr, String articulo,
            String nombreDelito, String gravedad,
            String descripcion, Date fechaComision,
            String añosStr, String mesesStr) {
        try {
            if (preso == null) {
                throw new IllegalArgumentException("Preso no puede ser nulo");
            }

            Validador.validarCampoObligatorio("código del delito", codigoStr);
            Validador.validarCampoObligatorio("artículo", articulo);
            Validador.validarCampoObligatorio("nombre del delito", nombreDelito);
            Validador.validarCampoObligatorio("gravedad", gravedad);
            Validador.validarCampoObligatorio("años de sentencia", añosStr);
            Validador.validarCampoObligatorio("meses de sentencia", mesesStr);

            int codigo = Integer.parseInt(codigoStr);
            int años = Integer.parseInt(añosStr);
            int meses = Integer.parseInt(mesesStr);

            Validador.validarSentencia(años, meses);

            LocalDate fechaComisionLocal = fechaComision.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            Validador.validarFechaNoFutura(fechaComisionLocal, "comisión del delito");

            LocalDate fechaIngreso = preso.getDelitos().isEmpty()
                    ? LocalDate.now()
                    : preso.getDelitos().get(0).getSentencia().getFechaIngreso();

            Sentencia sentenciaDelito = new Sentencia(años, meses, fechaIngreso);

            Delito nuevoDelito = new Delito(
                    0,
                    preso.getIdentificacion(),
                    codigo,
                    nombreDelito,
                    articulo.trim(),
                    gravedad,
                    descripcion.trim(),
                    fechaComisionLocal,
                    sentenciaDelito
            );

            int idGenerado = delitoDAO.guardarDelito(nuevoDelito);
            nuevoDelito.setId(idGenerado);

            List<Delito> delitosActualizados = delitoDAO.obtenerDelitosPorPreso(preso.getIdentificacion());
            preso.getDelitos().clear();
            preso.getDelitos().addAll(delitosActualizados);

            if (preso.getExpediente() == null) {
                ExpedienteJudicial expediente = new ExpedienteJudicial();
                expediente.setDelitos(delitosActualizados);
                preso.setExpediente(expediente);
            } else {
                preso.getExpediente().setDelitos(delitosActualizados);
            }

            Validador.mostrarInfo("Delito agregado correctamente");
            return true;

        } catch (IllegalArgumentException e) {
            Validador.mostrarError(e.getMessage());
            return false;
        } catch (Exception e) {
            Validador.mostrarError("Error inesperado al agregar delito: " + e.getMessage());
            return false;
        }
    }

    public Preso obtenerPresoDesdeTabla(int filaSeleccionada, JTable tablaPresos) {
        try {
            if (filaSeleccionada == -1) {
                throw new IllegalArgumentException("Debe seleccionar un preso de la tabla");
            }

            if (tablaPresos == null) {
                throw new IllegalArgumentException("La tabla de presos no puede ser nula");
            }

            String identificacion = tablaPresos.getValueAt(filaSeleccionada, 5).toString();
            Validador.validarFormatoIdentificacion(identificacion);

            Preso preso = presoDAO.buscarPresoPorIdentificacion(identificacion);

            if (preso == null) {
                throw new IllegalStateException("No se encontró el preso con identificación: " + identificacion);
            }

            if (preso != null) {
                List<Delito> delitos = delitoDAO.obtenerDelitosPorPreso(identificacion);
                preso.setDelitos(delitos);
            }

            List<Delito> delitos = delitoDAO.obtenerDelitosPorPreso(identificacion);

            if (preso.getExpediente() == null) {
                ExpedienteJudicial expediente = new ExpedienteJudicial();
                expediente.setDelitos(delitos);
                preso.setExpediente(expediente);
            } else {
                preso.getExpediente().setDelitos(delitos);
            }

            return preso;

        } catch (IllegalArgumentException | IllegalStateException e) {
            Validador.mostrarError(e.getMessage());
            return null;
        } catch (Exception e) {
            Validador.mostrarError("Error al obtener preso: " + e.getMessage());
            return null;
        }
    }

    public boolean validarLiberacionPreso(Preso preso, Date fechaValidacion) {
        try {
            if (preso == null) {
                throw new IllegalArgumentException("El preso no puede ser nulo");
            }

            if (fechaValidacion == null) {
                throw new IllegalArgumentException("La fecha de validación no puede ser nula");
            }

            LocalDate fechaActual = fechaValidacion.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            LocalDate fechaSalida;
            try {
                fechaSalida = calcularFechaSalidaPreso(preso.getDelitos());
            } catch (RuntimeException e) {
                throw new IllegalArgumentException("No se pudo calcular la fecha de salida: " + e.getMessage());
            }

            if (fechaActual.isBefore(fechaSalida)) {
                throw new IllegalArgumentException("No se puede liberar: El preso no ha completado su condena.\n"
                        + "Fecha de liberación: " + fechaSalida.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            }
            return true;

        } catch (IllegalArgumentException e) {
            Validador.mostrarError(e.getMessage());
            return false;
        }
    }

    public boolean eliminarPreso(String identificacion) {
        try {
            Validador.validarFormatoIdentificacion(identificacion);

            Preso preso = presoDAO.buscarPresoPorIdentificacion(identificacion);
            if (preso == null) {
                throw new IllegalArgumentException("No se encontró el preso con identificación: " + identificacion);
            }

            if (!validarLiberacionPreso(preso, new Date())) {
                return false;
            }

            boolean eliminado = presoDAO.eliminarPreso(identificacion);
            if (eliminado) {
                Validador.mostrarInfo("Preso LIBERADO correctamente");
                return true;
            } else {
                Validador.mostrarError("No se pudo Liberar el preso");
                return false;
            }

        } catch (IllegalArgumentException e) {
            Validador.mostrarError(e.getMessage());
            return false;
        } catch (Exception e) {
            Validador.mostrarError("Error inesperado al liberar preso: " + e.getMessage());
            return false;
        }
    }

    public LocalDate calcularFechaSalidaPreso(List<Delito> delitos) {
        try {
            Validador.validarListaNoVacia("delitos", delitos);

            LocalDate fechaIngreso = delitos.get(0).getSentencia().getFechaIngreso();
            if (fechaIngreso == null) {
                throw new IllegalArgumentException("La fecha de ingreso no puede ser nula");
            }

            Sentencia sentenciaAcumulada = new Sentencia(0, 0, fechaIngreso);

            for (Delito delito : delitos) {
                Sentencia s = delito.getSentencia();
                if (s == null) {
                    throw new IllegalArgumentException("Una sentencia no puede ser nula");
                }
                sentenciaAcumulada.sumarSentencia(s);
            }

            LocalDate fechaSalida = sentenciaAcumulada.getFechaSalidaCalculada();
            if (fechaSalida == null) {
                throw new IllegalStateException("La fecha de salida calculada no puede ser nula");
            }

            return fechaSalida;

        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new RuntimeException("Error al calcular fecha de salida: " + e.getMessage(), e);
        }
    }

    public void cargarTablaDelitos(Preso preso, JTable tabla) {
        try {
            if (preso == null) {
                throw new IllegalArgumentException("Preso no puede ser nulo");
            }

            if (tabla == null) {
                throw new IllegalArgumentException("Tabla no puede ser nula");
            }

            DefaultTableModel model = (DefaultTableModel) tabla.getModel();
            model.setRowCount(0);

            if (preso.getExpediente() == null || preso.getExpediente().getDelitos().isEmpty()) {
                model.addRow(new Object[]{"No hay delitos registrados"});
                return;
            }

            Sentencia sentenciaTotal = new Sentencia(0, 0,
                    preso.getExpediente().getDelitos().get(0).getSentencia().getFechaIngreso());

            for (Delito delito : preso.getExpediente().getDelitos()) {
                sentenciaTotal.sumarSentencia(delito.getSentencia());
            }

            LocalDate fechaSalidaComun = sentenciaTotal.getFechaSalidaCalculada();

            for (Delito delito : preso.getExpediente().getDelitos()) {
                model.addRow(new Object[]{
                    delito.getNombre(),
                    delito.getId(),
                    delito.getSentencia().getFechaIngreso().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    delito.getSentencia().getSentenciaFormateada(),
                    delito.getGravedad(),
                    delito.getFechaComision().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    fechaSalidaComun.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                });
            }

        } catch (IllegalArgumentException e) {
            Validador.mostrarError(e.getMessage());
        } catch (Exception e) {
            Validador.mostrarError("Error al cargar delitos: " + e.getMessage());
        }
    }

    public Preso obtenerPresoConDelitos(String identificacion) {
        try {
            Validador.validarFormatoIdentificacion(identificacion);

            Preso preso = presoDAO.buscarPresoPorIdentificacion(identificacion);
            if (preso == null) {
                throw new IllegalStateException("No se encontró el preso con identificación: " + identificacion);
            }

            List<Delito> delitos = delitoDAO.obtenerDelitosPorPreso(identificacion);

            if (preso.getExpediente() == null) {
                ExpedienteJudicial expediente = new ExpedienteJudicial();
                expediente.setDelitos(delitos);
                preso.setExpediente(expediente);
            } else {
                preso.getExpediente().setDelitos(delitos);
            }

            return preso;
        } catch (IllegalArgumentException | IllegalStateException e) {
            Validador.mostrarError(e.getMessage());
            return null;
        } catch (Exception e) {
            Validador.mostrarError("Error al obtener preso: " + e.getMessage());
            return null;
        }
    }

    public Sentencia calcularSentenciaTotal(List<Delito> delitos) {
        try {
            Validador.validarListaNoVacia("delitos", delitos);

            LocalDate fechaIngreso = delitos.get(0).getSentencia().getFechaIngreso();

            Sentencia sentenciaTotal = new Sentencia(0, 0, fechaIngreso);

            for (Delito delito : delitos) {
                sentenciaTotal.sumarSentencia(delito.getSentencia());
            }

            return sentenciaTotal;

        } catch (IllegalArgumentException e) {
            Validador.mostrarError(e.getMessage());
            return null;
        } catch (Exception e) {
            Validador.mostrarError("Error al calcular sentencia total: " + e.getMessage());
            return null;
        }
    }

    public Preso buscarPreso(String identificacion) throws Exception {
        return presoDAO.buscarPresoPorIdentificacion(identificacion);
    }

    private ImageIcon cargarImagenPreso(String path) {
        try {
            Image img = ImageIO.read(new File(path));
            return new ImageIcon(img.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        } catch (Exception e) {
            return new ImageIcon(getClass().getResource("/images/default_profile.png"));
        }
    }

    public ImageIcon obtenerFotoPreso(Preso preso) {
        if (preso.getFotoPath() != null && !preso.getFotoPath().isEmpty()) {
            return cargarImagenPreso(preso.getFotoPath());
        }
        return new ImageIcon(getClass().getResource("/images/default_profile.png"));
    }

    public List<Object[]> obtenerPresosPorSeccion(String seccion) {
        List<Preso> presos = presoDAO.buscarPorSeccion(seccion);
    
        List<Object[]> filas = new ArrayList<>();

        for (Preso preso : presos) {
            ImageIcon foto = obtenerFotoPreso(preso);

            filas.add(new Object[]{
                foto,
                preso.getPrimerNombre(),
                preso.getPrimerApellido(),
                preso.getEdad(),
                preso.getSexo(),
                preso.getNacionalidad(),
                preso.getIdentificacion(),
                preso.getCeldaAsignada(),
                preso.getSeccionAsignada()
            });
        }

        return filas;
    }

   public List<Object[]> obtenerTodosLosPresosParaTabla() {
        List<Preso> presos = presoDAO.buscarPorEstado("ACTIVO");
        List<Object[]> filas = new ArrayList<>();
        
        for (Preso preso : presos) {
            ImageIcon foto = obtenerFotoPreso(preso);
            
            List<Delito> delitos = delitoDAO.obtenerDelitosPorPreso(preso.getIdentificacion());
            preso.setDelitos(delitos);
            
            filas.add(new Object[]{
                foto,
                preso.getId(),
                preso.getNombresCompletos(),
                preso.getApellidosCompletos(),
                preso.getEdad(),
                preso.getIdentificacion(),
                preso.getNacionalidad(),
                preso.getSeccionAsignada(),
                preso.getCeldaAsignada()
            });
        }
        
        return filas;
    }
   
public List<Object[]> obtenerPresosInactivosParaTabla() {
     List<Preso> liberados = presoDAO.buscarPorEstado("LIBERADO");
    List<Preso> fallecidos = presoDAO.buscarPorEstado("DEFUNCION");
    
    List<Object[]> filas = new ArrayList<>();
    
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    for (Preso preso : liberados) {
 ImageIcon foto = (preso.getFotoPath() != null && !preso.getFotoPath().isEmpty()) 
            ? cargarImagenPreso(preso.getFotoPath())
            : new ImageIcon(getClass().getResource("/images/default_profile.png"));
        
        filas.add(new Object[]{
            foto,
            preso.getId(),
            preso.getNombresCompletos(),
            preso.getApellidosCompletos(),
            preso.getEdad(),
            preso.getIdentificacion(),
            preso.getNacionalidad(),
            "LIBERADO",
        });
    }
    
    
    for (Preso preso : fallecidos) {
        ImageIcon foto = obtenerFotoPreso(preso);
        filas.add(new Object[]{
            foto,
            preso.getId(),
            preso.getNombresCompletos(),
            preso.getApellidosCompletos(),
            preso.getEdad(),
            preso.getIdentificacion(),
            preso.getNacionalidad(),
            "DEFUNCION",
        });
    }
    
    return filas;
}

public boolean liberarPreso(String identificacion, Date fechaValidacion, String motivo) {
    try {
        Validador.validarFormatoIdentificacion(identificacion);

        Preso preso = presoDAO.buscarPresoPorIdentificacion(identificacion);
        if (preso == null) {
            throw new IllegalArgumentException("No se encontró el preso con identificación: " + identificacion);
        }

        if (motivo.equals("LIBERADO") && !validarLiberacionPreso(preso, fechaValidacion)) {
            return false;
        }

        if (motivo.equals("DEFUNCION")) {
            preso.setFechaDefuncion(fechaValidacion.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate());
        }

        boolean estadoCambiado = presoDAO.cambiarEstadoPreso(identificacion, motivo);
        
        if (estadoCambiado) {
            Validador.mostrarInfo("Preso marcado como " + motivo + " correctamente");
            return true;
        } else {
            Validador.mostrarError("No se pudo cambiar el estado del preso");
            return false;
        }

    } catch (IllegalArgumentException e) {
        Validador.mostrarError(e.getMessage());
        return false;
    } catch (Exception e) {
        Validador.mostrarError("Error inesperado al cambiar estado del preso: " + e.getMessage());
        return false;
    }
}

public void configurarTablaImagenes(JTable tabla) {
    tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value,
                    isSelected, hasFocus, row, column);

            if (column == 0 && value instanceof ImageIcon) {
                ImageIcon originalIcon = (ImageIcon) value;
                Image img = originalIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                ImageIcon roundedIcon = new ImageIcon(createRoundedImage(img));
                label.setIcon(roundedIcon);
                label.setText("");
            } else {
                label.setIcon(null);
            }
            label.setHorizontalAlignment(JLabel.CENTER);
            return label;
        }
    });

    tabla.setRowHeight(65);
    tabla.getColumnModel().getColumn(0).setPreferredWidth(70);
}


    private Image createRoundedImage(Image image) {
        int width = image.getWidth(null);
        int height = image.getHeight(null);

        BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = output.createGraphics();

        output = g2.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
        g2.dispose();
        g2 = output.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.fillRoundRect(0, 0, width, height, 20, 20);
        g2.setComposite(AlphaComposite.SrcIn);
        g2.drawImage(image, 0, 0, null);
        g2.dispose();

        return output;
    }



}
