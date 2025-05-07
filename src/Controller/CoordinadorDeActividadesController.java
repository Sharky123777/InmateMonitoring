package Controller;

import DAO.CoordinadorDeActividadesDAO;
import Model.Entities.CoordinadorDeActividades;
import View.FrmCamara;
import com.toedter.calendar.JDateChooser;
import java.awt.Image;
import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CoordinadorDeActividadesController {
    private static CoordinadorDeActividadesController instancia;
    private final CoordinadorDeActividadesDAO coordinadorDAO;
    private FrmCamara ventanaCamara;
    
    private CoordinadorDeActividadesController(){
        this.CoordinadorDeActividadesDAO = CoordinadorDeActividadesDAO.getInstancia();
    }
   
}