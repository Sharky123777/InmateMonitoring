package Controller;

import DAO.GuardiaDAO;
import Model.Entities.Guardia;
import View.FrmCamara;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import javax.swing.*;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;

public class GuardiaController {
    private static GuardiaController instancia;
    private final GuardiaDAO guardiaDAO;
    private FrmCamara ventanaCamara;

    private GuardiaController() {
        this.guardiaDAO = GuardiaDAO.getInstancia();
    }

    public static synchronized GuardiaController getInstancia() {
        if (instancia == null) {
            instancia = new GuardiaController();
        }
        return instancia;
    }
    
    public File capturarImagenGuardia() {
        FrmCamara ventanaCamara = new FrmCamara();
        ventanaCamara.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JDialog dialog = new JDialog();
        dialog.setModal(true);
        dialog.setContentPane(ventanaCamara.getContentPane());
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        
        while (dialog.isVisible()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }
        
        return ventanaCamara.getImagenCapturada();
    }

    
    public Guardia registrarGuardia(
            String primerNombre, String segundoNombre,
            String primerApellido, String segundoApellido,
            int edad, String cedula, String nacionalidad,
            String correo, String turno,
            LocalDate fechaInicioContrato, // <-- Recibir ambas fechas
            LocalDate fechaFinContrato,
            String cargo,
            File imagen
    ) throws IOException {
    
    validarCamposObligatorios(primerNombre, primerApellido, segundoApellido, 
            edad, cedula, nacionalidad, correo, turno, cargo);
    validarEdad(edad);
    
    // Fecha de inicio siempre es hoy
    LocalDate fechaInicio = LocalDate.now();
    validarFechasContrato(fechaInicio, fechaFinContrato); // Validar que fin > inicio
    validarImagen(imagen);

    Guardia nuevoGuardia = new Guardia(
            primerNombre, segundoNombre, primerApellido, segundoApellido,
            edad, cedula, nacionalidad, correo, turno, fechaFinContrato, cargo);

    boolean guardado = GuardiaDAO.getInstancia().guardarGuardia(nuevoGuardia, imagen);
    
    if (guardado) {
        return GuardiaDAO.getInstancia().obtenerGuardiaPorCedula(cedula);
    }
    throw new RuntimeException("No se pudo guardar el guardia");
}

    public boolean modificarGuardia(String cedulaOriginal, Map<String, Object> cambios, File nuevaImagen) {
    try {
        System.out.println("\n=== INICIO MODIFICACIÓN (CONTROLLER) ===");
        System.out.println("Validando datos...");
        
        Guardia original = obtenerGuardiaPorCedula(cedulaOriginal);
        if (original == null) {
            throw new IllegalArgumentException("Guardia no encontrado con cédula: " + cedulaOriginal);
        }

        validarCamposModificacion(cambios);
        validarEdad((int) cambios.get("edad"));
        validarFechasContrato(original.getFechaInicioContrato(), (LocalDate) cambios.get("fechaFin"));

        Guardia guardiaModificado = new Guardia(
            (String) cambios.get("primerNombre"),
            (String) cambios.get("segundoNombre"),
            (String) cambios.get("primerApellido"),
            (String) cambios.get("segundoApellido"),
            (int) cambios.get("edad"),
            cedulaOriginal,
            (String) cambios.get("nacionalidad"),
            (String) cambios.get("correo"),
            (String) cambios.get("turno"),
            (LocalDate) cambios.get("fechaFin"),
            (String) cambios.get("cargo")
        );

        System.out.println("Preparando imagen para modificación...");
        File imagenParaModificar = null;
        if (nuevaImagen != null) {
            System.out.println("Nueva imagen proporcionada: " + nuevaImagen.getAbsolutePath());
            if (nuevaImagen.exists()) {
                imagenParaModificar = nuevaImagen;
                System.out.println("Imagen válida, será procesada");
            } else {
                System.out.println("Advertencia: La imagen proporcionada no existe");
            }
        } else {
            System.out.println("No se proporcionó nueva imagen");
        }

        System.out.println("Invocando DAO para modificación...");
        boolean resultado = guardiaDAO.modificarGuardia(cedulaOriginal, guardiaModificado, imagenParaModificar);
        
        System.out.println("Resultado de modificación: " + resultado);
        return resultado;
    } catch (Exception e) {
        System.err.println("Error en controller al modificar guardia: " + e.getMessage());
        throw new RuntimeException("Error al modificar guardia: " + e.getMessage(), e);
    }
}
    
    
    private void validarImagen(File imagen) {
        if (imagen == null || !imagen.exists()) {
            throw new IllegalArgumentException("Debe proporcionar una imagen válida del guardia");
        }
        
        String nombre = imagen.getName().toLowerCase();
        if (!nombre.endsWith(".jpg") && !nombre.endsWith(".jpeg") && !nombre.endsWith(".png")) {
            throw new IllegalArgumentException("Formato de imagen no válido. Use JPG, JPEG o PNG");
        }
    }

    // Métodos de consulta
    public List<Guardia> obtenerTodosGuardias() {
        return guardiaDAO.obtenerGuardias();
    }

    public Guardia obtenerGuardiaPorCedula(String cedula) {
        return guardiaDAO.obtenerGuardiaPorCedula(cedula);
    }

    public boolean eliminarGuardia(String cedula) {
        return guardiaDAO.eliminarGuardia(cedula);
    }

    // Métodos de validación
    private void validarCamposModificacion(Map<String, Object> cambios) {
        StringBuilder errores = new StringBuilder();

        if (((String) cambios.get("primerNombre")).trim().isEmpty()) {
            errores.append("- Primer nombre es obligatorio\n");
        }
        if (((String) cambios.get("primerApellido")).trim().isEmpty()) {
            errores.append("- Primer apellido es obligatorio\n");
        }
        if (((String) cambios.get("segundoApellido")).trim().isEmpty()) {
            errores.append("- Segundo apellido es obligatorio\n");
        }
        if (((String) cambios.get("nacionalidad")).trim().isEmpty()) {
            errores.append("- Nacionalidad es obligatoria\n");
        }
        if (((String) cambios.get("correo")).trim().isEmpty()) {
            errores.append("- Correo es obligatorio\n");
        }
        if (((String) cambios.get("cargo")).trim().isEmpty()) {
            errores.append("- Cargo es obligatorio\n");
        }

        if (errores.length() > 0) {
            throw new IllegalArgumentException(errores.toString());
        }
    }

    private void validarCamposObligatorios(String primerNombre, String primerApellido,
            String segundoApellido, int edad, String cedula,
            String nacionalidad, String correo, String turno, String cargo) {
        
        StringBuilder camposFaltantes = new StringBuilder();

        if (primerNombre == null || primerNombre.trim().isEmpty()) {
            camposFaltantes.append("- Primer nombre\n");
        }
        if (primerApellido == null || primerApellido.trim().isEmpty()) {
            camposFaltantes.append("- Primer apellido\n");
        }
        if (segundoApellido == null || segundoApellido.trim().isEmpty()) {
            camposFaltantes.append("- Segundo apellido\n");
        }
        if (cedula == null || cedula.trim().isEmpty()) {
            camposFaltantes.append("- Cédula\n");
        }
        if (nacionalidad == null || nacionalidad.trim().isEmpty()) {
            camposFaltantes.append("- Nacionalidad\n");
        }
        if (correo == null || correo.trim().isEmpty()) {
            camposFaltantes.append("- Correo\n");
        }
        if (turno == null || turno.trim().isEmpty()) {
            camposFaltantes.append("- Turno\n");
        }
        if (cargo == null || cargo.trim().isEmpty()) {
            camposFaltantes.append("- Cargo\n");
        }

        if (camposFaltantes.length() > 0) {
            throw new IllegalArgumentException("Campos obligatorios faltantes:\n" + camposFaltantes);
        }
    }

    private void validarFechasContrato(LocalDate inicio, LocalDate fin) {
    // Validación de nulos
    if (inicio == null || fin == null) {
        throw new IllegalArgumentException("Ambas fechas deben estar especificadas");
    }

    // Debug detallado
    System.out.println("\n=== VALIDACIÓN DE FECHAS ===");
    System.out.println("Fecha inicio (recibida): " + inicio.format(DateTimeFormatter.ISO_DATE));
    System.out.println("Fecha fin (recibida): " + fin.format(DateTimeFormatter.ISO_DATE));
    System.out.println("Diferencia en días: " + ChronoUnit.DAYS.between(inicio, fin));
    System.out.println("Fin es igual a inicio? " + fin.isEqual(inicio));
    System.out.println("Fin es después de inicio? " + fin.isAfter(inicio));

    // Validación de fechas
    if (fin.isBefore(inicio)) {
        throw new IllegalArgumentException(String.format(
            "Error de fechas: La fecha de fin (%s) no puede ser anterior a la de inicio (%s)",
            fin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            inicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        ));
    }

    if (fin.isEqual(inicio)) {
        throw new IllegalArgumentException(String.format(
            "Error de fechas: La fecha de fin (%s) no puede ser igual a la de inicio (%s)",
            fin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            inicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        ));
    }
}
    
    private void validarEdad(int edad) {
        if (edad < 18 || edad > 70) {
            throw new IllegalArgumentException("La edad debe estar entre 18 y 70 años");
        }
    }
    
    public DefaultTableModel obtenerModeloTabla() {
        String[] columnas = {
            "Foto", "Nombres", "Apellidos", "Edad", "Cédula", 
            "Nacionalidad", "Correo", "Turno", "Cargo",
            "Fecha Inicio", "Fecha Fin"
        };

        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? ImageIcon.class : String.class;
            }
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Guardia guardia : obtenerTodosGuardias()) {
            modelo.addRow(new Object[]{
                obtenerImagenGuardia(guardia),
                guardia.getPrimerNombre() + " " + guardia.getSegundoNombre(),
                guardia.getPrimerApellido() + " " + guardia.getSegundoApellido(),
                guardia.getEdad(),
                guardia.getIdentificacion(),
                guardia.getNacionalidad(),
                guardia.getCorreo(),
                guardia.getTurno(),
                guardia.getCargo(),
                guardia.getFechaInicioContrato(),
                guardia.getFechaFinContrato()
            });
        }

        return modelo;
    }
    
    
    private ImageIcon obtenerImagenGuardia(Guardia guardia) {
    if (guardia.getRutaImagen() != null && !guardia.getRutaImagen().isEmpty()) {
        try {
            // Verificar si la ruta es absoluta o relativa
            File imagenFile = new File(guardia.getRutaImagen());
            if (!imagenFile.exists()) {
                // Intentar con ruta relativa si la absoluta falla
                imagenFile = new File("src/" + guardia.getRutaImagen());
            }
            
            if (imagenFile.exists()) {
                ImageIcon original = new ImageIcon(imagenFile.getAbsolutePath());
                Image imagen = original.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                return new ImageIcon(imagen);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar imagen: " + e.getMessage());
        }
    }
    // Devolver una imagen por defecto si no se puede cargar
    return crearIconoPorDefectoGuardia();
}
    
    private ImageIcon crearIconoPorDefectoGuardia() {
    // Crear una imagen de 80x80 píxeles
    BufferedImage img = new BufferedImage(80, 80, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = img.createGraphics();

    // Configurar calidad de renderizado
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    
    // Dibujar fondo azul claro
    g2d.setColor(new Color(200, 220, 255));
    g2d.fillRect(0, 0, 80, 80);
    
    // Dibujar borde
    g2d.setColor(new Color(0, 0, 139)); // Azul oscuro
    g2d.setStroke(new BasicStroke(2));
    g2d.drawRect(5, 5, 70, 70);
    
    // Dibujar icono de guardia (silueta)
    g2d.setColor(Color.BLACK);
    // Cabeza
    g2d.fillOval(25, 10, 30, 30);
    // Cuerpo
    g2d.fillRect(35, 40, 10, 25);
    // Brazos
    g2d.drawLine(35, 45, 15, 35);
    g2d.drawLine(45, 45, 65, 35);
    // Piernas
    g2d.drawLine(35, 65, 25, 75);
    g2d.drawLine(45, 65, 55, 75);
    
    // Dibujar insignia
    g2d.setColor(Color.YELLOW);
    g2d.fillOval(37, 45, 6, 6);
    
    g2d.dispose();
    
    return new ImageIcon(img);
}
}