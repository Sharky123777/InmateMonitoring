package Controller;

import DAO.DelitoDAO;
import DAO.PresoDAO;
import Model.Delito;
import Model.Preso;
import Model.Sentencia;
import Utilidades.Validador;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DelitoController {
    private final DelitoDAO delitoDAO;
    private final PresoDAO presoDAO;
    
    public DelitoController(DelitoDAO delitoDAO, PresoDAO presoDAO) {
        if (delitoDAO == null || presoDAO == null) {
            throw new IllegalArgumentException("DAOs no pueden ser nulos");
        }
        this.delitoDAO = delitoDAO;
        this.presoDAO = presoDAO;
    }
    }
    
   
 