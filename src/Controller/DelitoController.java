package Controller;

import DAO.DelitoDAO;
import DAO.PresoDAO;
import Model.Entities.Delito;
import Model.Entities.Preso;
import Model.Entities.Sentencia;
import Utilidades.Validador;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DelitoController {
    private static volatile DelitoController instancia;
    
    private final DelitoDAO delitoDAO;
    private final PresoDAO presoDAO;
    
    private DelitoController() {
        this.delitoDAO = DelitoDAO.getInstancia();
        this.presoDAO = PresoDAO.getInstancia();
    }
    
    public static DelitoController getInstancia() {
        DelitoController result = instancia;
        if (result == null) {
            synchronized (DelitoController.class) {
                result = instancia;
                if (result == null) {
                    instancia = result = new DelitoController();
                }
            }
        }
        return result;
    }
    
}
    
   
 