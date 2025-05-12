package DAO;

import Model.Constants.EstadoExpedienteEnum;
import Model.Entities.Delito;
import Model.Entities.ExpedienteJudicial;
import Model.Entities.Preso;
import Model.Entities.Sentencia;
import Model.Entities.LocalDateAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class ExpedienteDAO {

    private static ExpedienteDAO instancia;
    private static final String JSON_FILE = "C:\\Users\\ASUS\\Desktop\\InmateMonitoring\\src\\Resources\\DATA\\expedientes.json";

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    public static synchronized ExpedienteDAO getInstancia() {
        if (instancia == null) {
            instancia = new ExpedienteDAO();
        }
        return instancia;
    }

    public boolean guardarExpediente(ExpedienteJudicial expediente) {
        List<ExpedienteJudicial> expedientes = cargarTodos();

        boolean existe = expedientes.stream()
                .anyMatch(e -> e.getCodigoExpediente().equals(expediente.getCodigoExpediente()));

        if (!existe) {
            expedientes.add(expediente);
        } else {
            expedientes = expedientes.stream()
                    .map(e -> e.getCodigoExpediente().equals(expediente.getCodigoExpediente()) ? expediente : e)
                    .collect(Collectors.toList());
        }

        return guardarTodos(expedientes);
    }

    public boolean actualizarExpediente(ExpedienteJudicial expediente) {
        return guardarExpediente(expediente);
    }

    public ExpedienteJudicial buscarPorCodigo(String codigoExpediente) {
        return cargarTodos().stream()
                .filter(e -> e.getCodigoExpediente().equals(codigoExpediente))
                .findFirst()
                .orElse(null);
    }

    // NUEVO MÉTODO: Buscar todos los expedientes asociados a un preso
    public List<ExpedienteJudicial> buscarExpedientesPorPreso(String identificacionPreso) {
        return cargarTodos().stream()
                .filter(e -> e.getPreso() != null && e.getPreso().getIdentificacion().equals(identificacionPreso))
                .collect(Collectors.toList());
    }

    public List<ExpedienteJudicial> listarTodos() {
        return cargarTodos();
    }

    public boolean eliminarExpediente(String codigoExpediente) {
        List<ExpedienteJudicial> expedientes = cargarTodos();
        boolean eliminado = expedientes.removeIf(e -> e.getCodigoExpediente().equals(codigoExpediente));

        if (eliminado) {
            return guardarTodos(expedientes);
        }
        return false;
    }

    private List<ExpedienteJudicial> cargarTodos() {
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
            Type tipoListaExpedientes = new TypeToken<ArrayList<ExpedienteJudicial>>() {
            }.getType();
            List<ExpedienteJudicial> expedientes = gson.fromJson(reader, tipoListaExpedientes);
            return expedientes != null ? expedientes : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Error al leer archivo JSON: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private boolean guardarTodos(List<ExpedienteJudicial> expedientes) {
        try (Writer writer = new FileWriter(JSON_FILE)) {
            gson.toJson(expedientes, writer);
            return true;
        } catch (IOException e) {
            System.err.println("Error al guardar en archivo JSON: " + e.getMessage());
            return false;
        }
    }

    public boolean agregarDelitosAExpediente(String codigoExpediente, List<Delito> nuevosDelitos) {
        List<ExpedienteJudicial> expedientes = cargarTodos();

        for (ExpedienteJudicial expediente : expedientes) {
            if (expediente.getCodigoExpediente().equals(codigoExpediente)) {
                expediente.getDelitos().addAll(nuevosDelitos);

                DelitoDAO delitoDAO = DelitoDAO.getInstancia();
                for (Delito delito : nuevosDelitos) {
                    delitoDAO.guardarDelito(delito);
                }

                return guardarTodos(expedientes);
            }
        }

        return false;
    }

    public ExpedienteJudicial actualizarExpedienteConDelitos(Preso preso, List<Delito> delitos) {
        List<ExpedienteJudicial> expedientes = buscarExpedientesPorPreso(preso.getIdentificacion());

        ExpedienteJudicial expediente = expedientes.stream()
                .filter(e -> e.getEstado() == EstadoExpedienteEnum.ABIERTO)
                .findFirst()
                .orElse(null);

        if (expediente == null) {
            expediente = new ExpedienteJudicial(preso);
            expediente.setCodigoExpediente(generarCodigoUnico());
            expediente.setFechaApertura(LocalDate.now());
            expediente.setEstado(EstadoExpedienteEnum.ABIERTO);
            expediente.setDelitos(new ArrayList<>());
        }

        List<Delito> delitosActualizados = new ArrayList<>(expediente.getDelitos());
        for (Delito nuevoDelito : delitos) {
            if (delitosActualizados.stream().noneMatch(d -> d.getId() == nuevoDelito.getId())) {
                delitosActualizados.add(nuevoDelito);
            }
        }

        expediente.setDelitos(delitosActualizados);
        guardarExpediente(expediente);
        return expediente;
    }

    private String generarCodigoUnico() {
        return "EXP-" + System.currentTimeMillis();
    }

    public Sentencia calcularSentenciaTotal(List<Delito> delitos) {
        if (delitos == null || delitos.isEmpty()) {
            return new Sentencia(0, 0, LocalDate.now());
        }

        LocalDate fechaIngreso = delitos.get(0).getSentencia().getFechaIngreso();
        Sentencia total = new Sentencia(0, 0, fechaIngreso);

        for (Delito delito : delitos) {
            total.sumarSentencia(delito.getSentencia());
        }

        return total;
    }
    
    public ExpedienteJudicial obtenerExpedienteAbierto(String identificacionPreso) {
    return buscarExpedientesPorPreso(identificacionPreso).stream()
            .filter(e -> e.getEstado() == EstadoExpedienteEnum.ABIERTO)
            .findFirst()
            .orElse(null);
}

    public ExpedienteJudicial crearExpedienteNuevoParaReincidencia(Preso preso, List<Delito> nuevosDelitos) {
    ExpedienteJudicial expediente = new ExpedienteJudicial(preso);
    expediente.setCodigoExpediente(generarCodigoUnico());
    expediente.setFechaApertura(LocalDate.now());
    expediente.setEstado(EstadoExpedienteEnum.ABIERTO);
    expediente.setDelitos(new ArrayList<>(nuevosDelitos));

    guardarExpediente(expediente);
    return expediente;
}
    
   

}
