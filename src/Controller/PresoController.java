
package Controller;

import DAO.PresoDAO;
import Model.Preso;
import Model.Sentencia;
import View.OficialDeRegistro;
import java.io.File;
import java.time.LocalDate;
import javax.swing.JOptionPane;

public class PresoController {
  
    private PresoDAO presoDAO;
    private OficialDeRegistro view;

    public PresoController(OficialDeRegistro view) {
        this.view = view;
        this.presoDAO = new PresoDAO();
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
    
    
    
}
