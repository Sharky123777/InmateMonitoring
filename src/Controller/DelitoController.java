package Controller;

import DAO.DelitoDAO;
import DAO.PresoDAO;
import Model.Delito;
import Model.Preso;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JTable;

public class DelitoController {
    private final DelitoDAO delitoDAO;
    private final PresoDAO presoDAO;

    public DelitoController() {
        this.delitoDAO = new DelitoDAO();
        this.presoDAO = new PresoDAO(); 
    }

    
    public boolean agregarDelitoAPreso(Preso preso, String codigo, String articulo, 
                                     String nombreDelito, String gravedad, 
                                     String descripcion, Date fechaComision) {
        if (preso == null) {
            throw new IllegalArgumentException("Seleccione un preso primero");
        }
        
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código del delito es requerido");
        }
        
        if (articulo == null || articulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El artículo es requerido");
        }
        
        if (fechaComision == null) {
            throw new IllegalArgumentException("La fecha de comisión es requerida");
        }

        try {
            int codigoNumerico = Integer.parseInt(codigo.trim());
            
            Delito nuevoDelito = new Delito(
                0,
                    codigoNumerico,
                nombreDelito,
                articulo.trim(),
                gravedad,
                descripcion.trim(),
                fechaComision.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
            );

            
            int idGenerado = delitoDAO.guardarDelito(nuevoDelito);
            nuevoDelito.setId(idGenerado);
            
            
            List<Delito> delitosActuales = preso.getDelitos();
            if (delitosActuales == null) {
                delitosActuales = new ArrayList<>();
            }
            delitosActuales.add(nuevoDelito);
            
            
            return presoDAO.actualizarPreso(
                preso.getIdentificacion(),
                preso.getPrimerNombre(),
                preso.getSegundoNombre(),
                preso.getPrimerApellido(),
                preso.getSegundoApellido(),
                preso.getEdad(),
                preso.getSexo(),
                preso.getNacionalidad(),
                preso.getEstatura(),
                preso.getPeso(),
                preso.getSentencia(),
                preso.getGrupoSanguineo(),
                preso.getSeccionAsignada(),
                preso.getNivelDeSeguridad(),
                preso.isEnAislamiento(),
                preso.getNivelDeRiesgo(),
                null 
            );
            
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El código debe ser un número válido");
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el delito: " + e.getMessage(), e);
        }
    }
    
      
}