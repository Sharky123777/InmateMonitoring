package Controller;

import DAO.CeldaDAO;
import DAO.PresoDAO;
import Model.Celda;
import Model.Delito;
import Model.Preso;
import Model.Sentencia;
import View.OficialDeRegistro;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.swing.JOptionPane;
import javax.swing.JTable;

public class PresoController {
  
   private final PresoDAO presoDAO;
    private final CeldaDAO celdaDAO;

    public PresoController(PresoDAO presoDAO, CeldaDAO celdaDAO) {
        this.presoDAO = presoDAO;
        this.celdaDAO = celdaDAO;
    }
    
    public boolean hayCambios(String primerNombre, String primerApellido, String edad, 
                            String estatura, String peso, String nacionalidad,
                            Object grupoSanguineo, Object año, Object mes, 
                            Object seccion, Object nivelSeguridad, 
                            Object aislamiento, Object nivelRiesgo, 
                            Object imagen) {
        return !(primerNombre.trim().isEmpty()
                && primerApellido.trim().isEmpty()
                && edad.trim().isEmpty()
                && estatura.trim().isEmpty()
                && peso.trim().isEmpty()
                && nacionalidad.trim().isEmpty()
                && (grupoSanguineo == null || grupoSanguineo.toString().equals("Seleccionar"))
                && (año == null || (Integer)año == 0)
                && (mes == null || (Integer)mes == 0)
                && (seccion == null || seccion.toString().equals("Seleccionar"))
                && (nivelSeguridad == null || nivelSeguridad.toString().equals("Seleccionar"))
                && (aislamiento == null || aislamiento.toString().equals("Seleccionar"))
                && (nivelRiesgo == null || nivelRiesgo.toString().equals("Seleccionar"))
                && imagen == null);
    }

    public boolean actualizarPreso(Preso presoOriginal, 
                                 String primerNombre, 
                                 String segundoNombre, 
                                 String primerApellido, 
                                 String segundoApellido, 
                                 String edad, 
                                 String nacionalidad, 
                                 String estatura, 
                                 String peso, 
                                 int añosSentencia, 
                                 int mesesSentencia, 
                                 Object grupoSanguineo, 
                                 Object seccion, 
                                 Object nivelSeguridad, 
                                 Object aislamiento, 
                                 Object nivelRiesgo, 
                                 File selectedImageFile) {
        
        try {
            if (!hayCambios(primerNombre, primerApellido, edad, estatura, peso, nacionalidad,
                          grupoSanguineo, añosSentencia, mesesSentencia, seccion, 
                          nivelSeguridad, aislamiento, nivelRiesgo, selectedImageFile)) {
                JOptionPane.showMessageDialog(null, "No hay cambios para guardar", 
                                           "Advertencia", JOptionPane.WARNING_MESSAGE);
                return false;
            }

            Sentencia sentencia = null;
            if (añosSentencia > 0 || mesesSentencia > 0) {
                LocalDate fechaIngreso = presoOriginal.getSentencia().getFechaIngreso();
                sentencia = new Sentencia(añosSentencia, mesesSentencia, fechaIngreso);
            }

            boolean resultado = presoDAO.actualizarPreso(
                    presoOriginal.getIdentificacion(),
                    primerNombre.trim().isEmpty() ? null : primerNombre.trim(),
                    segundoNombre.trim().isEmpty() ? null : segundoNombre.trim(),
                    primerApellido.trim().isEmpty() ? null : primerApellido.trim(),
                    segundoApellido.trim().isEmpty() ? null : segundoApellido.trim(),
                    edad.trim().isEmpty() ? null : Integer.parseInt(edad.trim()),
                    presoOriginal.getSexo(),
                    nacionalidad.trim().isEmpty() ? null : nacionalidad.trim(),
                    estatura.trim().isEmpty() ? null : Float.parseFloat(estatura.trim()),
                    peso.trim().isEmpty() ? null : Float.parseFloat(peso.trim()),
                    sentencia,
                    grupoSanguineo == null || grupoSanguineo.toString().equals("Seleccionar") ? null : grupoSanguineo.toString(),
                    seccion == null || seccion.toString().equals("Seleccionar") ? null : seccion.toString(),
                    nivelSeguridad == null || nivelSeguridad.toString().equals("Seleccionar") ? null : nivelSeguridad.toString(),
                    aislamiento == null ? null : aislamiento.toString().equalsIgnoreCase("Sí"),
                    nivelRiesgo == null || nivelRiesgo.toString().equals("Seleccionar") ? null : nivelRiesgo.toString(),
                    selectedImageFile
            );

            return resultado;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error en formato numérico", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public boolean registrarPreso(String primerNombre, String segundoNombre, String primerApellido, 
                                String segundoApellido, String edad, String nacionalidad, 
                                String sexo, String estatura, String peso, String tipoSangre,
                                String identificacion, String condicion, String seccionAsignada,
                                String nivelDeRiesgo, String nivelDeSeguridad, String añosSentencia,
                                String mesesSentencia, Date fechaIngreso, List<Delito> delitos,
                               File selectedImageFile) {
        try {
            validarDatosPersonales(primerNombre, segundoNombre, primerApellido, segundoApellido,
                                  edad, nacionalidad, estatura, peso, identificacion);
            
            int edadNum = Integer.parseInt(edad);
            float estaturaNum = Float.parseFloat(estatura);
            float pesoNum = Float.parseFloat(peso);
            int años = Integer.parseInt(añosSentencia);
            int meses = Integer.parseInt(mesesSentencia);
            
            if (años < 0 || meses < 0 || meses > 11 || (años == 0 && meses == 0)) {
                throw new IllegalArgumentException("Sentencia inválida");
            }
            
            if (delitos == null || delitos.isEmpty()) {
                throw new IllegalArgumentException("Debe registrar al menos un delito");
            }
            
            Preso preso = crearPreso(primerNombre, segundoNombre, primerApellido, segundoApellido,
                                   edadNum, sexo, nacionalidad, identificacion, estaturaNum,
                                   pesoNum, delitos, años, meses, fechaIngreso, seccionAsignada,
                                   nivelDeSeguridad, condicion, nivelDeRiesgo, tipoSangre);
            
        return presoDAO.guardarPreso(preso, selectedImageFile);
            
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Datos numéricos inválidos: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error al registrar preso: " + e.getMessage(), e);
        }
    }
    
private Preso crearPreso(String primerNombre, String segundoNombre, String primerApellido,
                       String segundoApellido, int edad, String sexo, String nacionalidad,
                       String identificacion, float estatura, float peso, List<Delito> delitos,
                       int añosSentencia, int mesesSentencia, Date fechaIngreso,
                       String seccionAsignada, String nivelDeSeguridad, String condicion,
                       String nivelDeRiesgo, String tipoSangre) {
    LocalDate fechaIngresoLocal = fechaIngreso.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate();
    
    Sentencia sentencia = new Sentencia(añosSentencia, mesesSentencia, fechaIngresoLocal);
    
    Celda celdaAsignada = celdaDAO.asignarCeldaDisponible(seccionAsignada);
    if (celdaAsignada == null) {
        throw new RuntimeException("No hay celdas disponibles en la sección " + seccionAsignada);
    }
    
    return new Preso(
            primerNombre, segundoNombre, primerApellido, segundoApellido,
            edad, sexo, nacionalidad, identificacion, estatura, peso,
            new ArrayList<>(delitos), sentencia, nivelDeSeguridad,
            seccionAsignada, condicion, celdaAsignada.getNombreFormateado(), false,
            nivelDeRiesgo, 0, tipoSangre, null, 0
    );
}
    
    private void validarDatosPersonales(String... datos) {
        List<String> nombresCampos = List.of(
            "primer nombre", "segundo nombre", "primer apellido", "segundo apellido",
            "edad", "nacionalidad", "estatura", "peso", "identificación"
        );
        
        if (datos.length != nombresCampos.size()) {
            throw new IllegalArgumentException("Número incorrecto de parámetros");
        }
        
        List<String> camposFaltantes = new ArrayList<>();
        for (int i = 0; i < datos.length; i++) {
            if (datos[i] == null || datos[i].trim().isEmpty()) {
                camposFaltantes.add(nombresCampos.get(i));
            }
        }
        
        if (!camposFaltantes.isEmpty()) {
            throw new IllegalArgumentException(
                "Campos requeridos faltantes: " + String.join(", ", camposFaltantes));
        }
    }

    public boolean agregarDelitosAPreso(Preso preso, List<Delito> nuevosDelitos) {
        try {
            return presoDAO.agregarDelitosAExpediente(preso, nuevosDelitos);
        } catch (Exception e) {
            throw new RuntimeException("Error al agregar delitos: " + e.getMessage(), e);
        }
    }
    
    
     public Preso obtenerPresoDesdeTabla(int filaSeleccionada, JTable tablaPresos) {
        if (filaSeleccionada == -1) {
            throw new IllegalArgumentException("Seleccione un preso primero");
        }

        String identificacion = tablaPresos.getValueAt(filaSeleccionada, 5).toString();
        Preso preso = presoDAO.buscarPresoPorIdentificacion(identificacion);

        if (preso == null) {
            throw new IllegalStateException("Preso no encontrado");
        }

        return preso;
    }

    public boolean validarEliminacionPreso(Preso preso, Date fechaValidacion) {
        if (preso == null || preso.getSentencia() == null) {
            throw new IllegalArgumentException("Datos del preso incompletos");
        }

        LocalDate fechaActual = fechaValidacion.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        LocalDate fechaSalida = preso.getSentencia().getFechaSalidaCalculada();

        return !fechaActual.isBefore(fechaSalida);
    }

    public boolean eliminarPreso(String identificacion) {
        return presoDAO.eliminarPreso(identificacion);
    }
  
}