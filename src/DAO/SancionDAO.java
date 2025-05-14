package DAO;

import Model.Entities.LocalDateAdapter;
import Model.Entities.LocalTimeAdapter;
import Model.Entities.Sancion;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SancionDAO {

    private static SancionDAO instancia;
    private static final String JSON_FILE = "src/Resources/DATA/sanciones.json";
    private Gson gson;

    public SancionDAO() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                .create();
    }

    public static synchronized SancionDAO getInstancia() {
        if (instancia == null) {
            instancia = new SancionDAO();
        }
        return instancia;
    }

    public List<Sancion> cargarTodas() {
        File archivo = new File(JSON_FILE);

        if (!archivo.exists()) {
            try {
                archivo.createNewFile();
                guardarTodas(new ArrayList<>());
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
            Type tipoListaSancion = new TypeToken<ArrayList<Sancion>>() {
            }.getType();
            List<Sancion> sanciones = gson.fromJson(reader, tipoListaSancion);
            return sanciones != null ? sanciones : new ArrayList<>();
        } catch (JsonSyntaxException | IOException e) {
            System.err.println("Error al leer archivo JSON: " + e.getMessage());
            guardarTodas(new ArrayList<>());
            return new ArrayList<>();
        }
    }

    public boolean guardarSancion(Sancion sancion) {
        try {
            List<Sancion> sanciones = cargarTodas();

            if (sancion.getId() == 0) {
                int nuevoId = obtenerProximoId(sanciones);
                sancion.setId(nuevoId);
            }

            sanciones.add(sancion);
            guardarTodas(sanciones);
            return true;
        } catch (Exception e) {
            System.err.println("Error al guardar sanción: " + e.getMessage());
            return false;
        }
    }

    private int obtenerProximoId(List<Sancion> sanciones) {
        if (sanciones.isEmpty()) {
            return 1;
        }
        return sanciones.stream()
                .mapToInt(Sancion::getId)
                .max()
                .orElse(0) + 1;
    }

    public List<Sancion> cargarPorIdentificacionPreso(String identificacionPreso) {
        List<Sancion> todasSanciones = cargarTodas();
        List<Sancion> sancionesFiltradas = new ArrayList<>();

        for (Sancion sancion : todasSanciones) {
            if (sancion.getPreso() != null
                    && sancion.getPreso().getIdentificacion().equals(identificacionPreso)) {
                sancionesFiltradas.add(sancion);
            }
        }
        return sancionesFiltradas;
    }

    public void guardarTodas(List<Sancion> sanciones) {
        try (FileWriter writer = new FileWriter(JSON_FILE)) {
            gson.toJson(sanciones, writer);
        } catch (IOException e) {
            System.err.println("Error al guardar en archivo JSON: " + e.getMessage());
        }
    }

    public List<Sancion> cargarPorTipoYIdentificacionPreso(String tipoSancion, String identificacionPreso) {
        List<Sancion> todasSanciones = cargarTodas();
        List<Sancion> sancionesFiltradas = new ArrayList<>();

        for (Sancion sancion : todasSanciones) {
            boolean coincideTipo = tipoSancion == null || tipoSancion.equals(sancion.getTipoSancion());
            boolean coincideIdentificacion = sancion.getPreso() != null && sancion.getPreso().getIdentificacion().equals(identificacionPreso);

            if (coincideTipo && coincideIdentificacion) {
                sancionesFiltradas.add(sancion);
            }
        }

        return sancionesFiltradas;
    }

    public List<Sancion> obtenerSancionesPorPresoFechaYHora(String idPreso, LocalDate fecha, LocalTime hora) {
        return cargarTodas().stream()
                .filter(s -> s.getPreso() != null
                && s.getPreso().getIdentificacion().equals(idPreso)
                && s.getFechaSancion().equals(fecha)
                && s.getHora().equals(hora))
                .collect(Collectors.toList());
    }

    public boolean actualizarSancion(Sancion sancionActualizada) {
        List<Sancion> sanciones = cargarTodas();
        boolean encontrado = false;

        for (int i = 0; i < sanciones.size(); i++) {
            if (sanciones.get(i).getId() == sancionActualizada.getId()) {
                sanciones.set(i, sancionActualizada);
                encontrado = true;
                break;
            }
        }

        if (encontrado) {
            guardarTodas(sanciones);
            return true;
        }
        return false;
    }

    public Sancion buscarPorId(int id) {
        return cargarTodas().stream()
                .filter(s -> s.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Sancion> obtenerSancionesActivasPorPreso(String identificacionPreso) {
    List<Sancion> sanciones = cargarPorIdentificacionPreso(identificacionPreso);
    LocalDate hoy = LocalDate.now();

    return sanciones.stream()
            .filter(sancion -> {
                if (sancion.getTipoSancion().equalsIgnoreCase("Suspensión de visitas")) {
                    LocalDate fechaFin = sancion.getFechaSancion().plusDays(1);
                    return !hoy.isBefore(sancion.getFechaSancion()) && !hoy.isAfter(fechaFin);
                } else {
                    return hoy.isEqual(sancion.getFechaSancion());
                }
            })
            .collect(Collectors.toList());
}

    public List<Sancion> obtenerSancionesPorPresoYFecha(String identificacionPreso, LocalDate fecha) {
        List<Sancion> sanciones = cargarPorIdentificacionPreso(identificacionPreso);
        return sanciones.stream()
                .filter(sancion -> sancion.getFechaSancion().equals(fecha))
                .collect(Collectors.toList());
    }
}
