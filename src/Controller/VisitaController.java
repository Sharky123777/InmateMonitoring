package Controller;

import DAO.PersonalDeControlDAO;
import DAO.PresoDAO;
import DAO.VisitaDAO;
import DAO.VisitanteDAO;
import Model.Preso;
import Model.Visita;
import Model.Visitante;
import View.PersonalDeControl;
import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class VisitaController {

    private VisitaDAO visitaDAO = new VisitaDAO();
    private VisitanteDAO visitanteDAO = new VisitanteDAO();
    private PersonalDeControlDAO personalDeControlDAO = new PersonalDeControlDAO();

    public void limpiarCamposVisitante(PersonalDeControl view) {
        view.getPrimerNombreVisitante().setText("");
        view.getSegundoNombreVisitante().setText("");
        view.getPrimerApellidoVisitante().setText("");
        view.getSegundoApellidoVisitante().setText("");
        view.getIdentificacionVisitante().setText("");
        view.getEmailVisitante().setText("");
        view.getEdadVisitante().setText("");
        view.getSexoVisitante().setSelectedIndex(0);
        view.getNacionalidadVisitante().setSelectedIndex(0);
        view.getRelacionConPresoVisitante().setSelectedIndex(0);
        view.getVistaPreviaVisitante().setIcon(null);
    }

    public void limpiarCamposVisita(PersonalDeControl view) {
        view.getIdentificacionPresoVisita().setText("");
        view.getFechaVisita().setDate(null);
        view.getDuracionVisita().setSelectedIndex(0);
        view.getTipoVisita().setSelectedIndex(0);
        view.getLugarVisita().setSelectedIndex(0);
        view.getHoraVisita().setSelectedIndex(0);
    }

    public boolean validarCamposVisitante(PersonalDeControl view, File imagen, int cantidadTotal, List<Visitante> visitantesTemporales) {
        if (view.getCantidadDeVisitantesCombo().getSelectedIndex() == 0) {
            mostrarError("Primero seleccione la cantidad de visitantes");
            return false;
        }

        int cantidadSeleccionada = Integer.parseInt(view.getCantidadDeVisitantesCombo().getSelectedItem().toString());
        int edadVisitante;

        try {
            edadVisitante = Integer.parseInt(view.getEdadVisitante().getText().trim());
        } catch (NumberFormatException e) {
            mostrarError("La edad debe ser un número válido");
            return false;
        }

        boolean esMenorEnGrupoDe2 = (cantidadSeleccionada == 2 && edadVisitante < 18 && !visitantesTemporales.isEmpty());

        if (view.getPrimerNombreVisitante().getText().trim().isEmpty()
                || view.getPrimerApellidoVisitante().getText().trim().isEmpty()
                || view.getSegundoApellidoVisitante().getText().trim().isEmpty()
                || view.getEdadVisitante().getText().trim().isEmpty()
                || view.getIdentificacionVisitante().getText().trim().isEmpty()
                || (view.getEmailVisitante().getText().trim().isEmpty() && !esMenorEnGrupoDe2)) {
            mostrarError("Por favor complete todos los campos del formulario");
            return false;
        }

        if (view.getSexoVisitante().getSelectedIndex() == 0
                || view.getRelacionConPresoVisitante().getSelectedIndex() == 0
                || view.getNacionalidadVisitante().getSelectedIndex() == 0) {
            mostrarError("Seleccione una opción válida en todos los desplegables");
            return false;
        }

        if (!view.getPrimerNombreVisitante().getText().trim().matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]{2,}$")) {
            mostrarError("El primer nombre solo debe contener letras y tener al menos 2 caracteres.");
            return false;
        }
        if (!view.getPrimerApellidoVisitante().getText().trim().matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]{2,}$")) {
            mostrarError("El primer apellido solo debe contener letras y tener al menos 2 caracteres.");
            return false;
        }
        if (!view.getSegundoApellidoVisitante().getText().trim().matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]{2,}$")) {
            mostrarError("El segundo apellido solo debe contener letras y tener al menos 2 caracteres.");
            return false;
        }

        String identificacion = view.getIdentificacionVisitante().getText().trim();
        if (!identificacion.matches("\\d{6,10}")) {
            mostrarError("La identificación debe contener entre 6 y 10 dígitos numéricos.");
            return false;
        }

        if (!esMenorEnGrupoDe2) {
            String email = view.getEmailVisitante().getText().trim();
            if (!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")) {
                mostrarError("Por favor ingrese un correo electrónico válido.");
                return false;
            }
        }

        if (edadVisitante < 1 || edadVisitante > 120) {
            mostrarError("La edad debe estar entre 1 y 120 años");
            return false;
        }

        if (cantidadSeleccionada == 1) {
            if (edadVisitante < 18) {
                mostrarError("Un solo visitante debe tener al menos 18 años");
                return false;
            }
        } else if (cantidadSeleccionada == 2) {
            if (visitantesTemporales.isEmpty()) {
                if (edadVisitante < 18) {
                    mostrarError("Debe ingresar primero al visitante mayor de edad (mínimo 18 años)");
                    return false;
                }
            } else {
                int edadPrimeraPersona = visitantesTemporales.get(0).getEdad();
                boolean unoMayorDeEdad = edadPrimeraPersona >= 18 || edadVisitante >= 18;
                boolean ningunoMenorDeUnAno = edadPrimeraPersona >= 1 && edadVisitante >= 1;

                if (!unoMayorDeEdad) {
                    mostrarError("Debe haber al menos un visitante mayor de 18 años");
                    return false;
                }
                if (!ningunoMenorDeUnAno) {
                    mostrarError("Ningún visitante puede tener menos de 1 año");
                    return false;
                }
            }
        }

        if (imagen == null) {
            mostrarError("Debe seleccionar una imagen del visitante");
            return false;
        }

        return true;
    }

    public boolean validarCamposVisita(PersonalDeControl view) {
        if (view.getIdentificacionPresoVisita().getText().trim().isEmpty()) {
            mostrarError("Digite la identificación del preso");
            return false;
        }

        if (view.getDuracionVisita().getSelectedIndex() == 0
                || view.getTipoVisita().getSelectedIndex() == 0
                || view.getLugarVisita().getSelectedIndex() == 0) {
            mostrarError("Complete todos los campos requeridos para la visita");
            return false;
        }

        if (view.getFechaVisita().getDate() == null) {
            mostrarError("Seleccione una fecha para la visita");
            return false;
        }

        return true;
    }

    public void registrarVisitante(PersonalDeControl view, List<Visitante> visitantesTemporales, List<File> imagenesTemporales) {
        int cantidadTotal;
        File imagen = view.getImagenVisitanteSeleccionada();

        if (!validarCamposVisitante(view, imagen, 0, visitantesTemporales)) {
            return;
        }

        try {
            cantidadTotal = Integer.parseInt(view.getCantidadDeVisitantesCombo().getSelectedItem().toString());
        } catch (NumberFormatException e) {
            mostrarError("Cantidad de visitantes no válida");
            return;
        }

        String primerNombre = view.getPrimerNombreVisitante().getText().trim();
        String segundoNombre = view.getSegundoNombreVisitante().getText().trim();
        String primerApellido = view.getPrimerApellidoVisitante().getText().trim();
        String segundoApellido = view.getSegundoApellidoVisitante().getText().trim();
        String identificacion = view.getIdentificacionVisitante().getText().trim();
        int edad = Integer.parseInt(view.getEdadVisitante().getText().trim());
        String sexo = view.getSexoVisitante().getSelectedItem().toString();
        String nacionalidad = view.getNacionalidadVisitante().getSelectedItem().toString();
        String relacion = view.getRelacionConPresoVisitante().getSelectedItem().toString();
        String email = view.getEmailVisitante().getText().trim();

        for (Visitante v : visitantesTemporales) {
            if (v.getIdentificacion().equals(identificacion)) {
                mostrarError("Ya se ha ingresado un visitante con esta identificación");
                return;
            }
            if (v.getEmail().equals(email)) {
                mostrarError("Ya se ha registrado un visitante con este correo electrónico");
                return;
            }
        }

        if (identificacion.length() < 6 || identificacion.length() > 10) {
            mostrarError("La identificación debe tener entre 6 y 10 caracteres.");
            return;
        }

        Visitante visitante = new Visitante(primerNombre, segundoNombre, primerApellido, segundoApellido, edad, sexo,
                nacionalidad, identificacion, relacion, imagen.getAbsolutePath(), email);
        visitantesTemporales.add(visitante);
        imagenesTemporales.add(imagen);

        limpiarCamposVisitante(view);
        view.setImagenVisitanteSeleccionada(null);

        if (visitantesTemporales.size() >= cantidadTotal) {
            JOptionPane.showMessageDialog(null,
                    "Ya se añadieron todos los visitantes. Ahora complete los datos de la visita.",
                    "Información", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null,
                    "Visitante añadido (" + visitantesTemporales.size() + " de " + cantidadTotal + ").\nPor favor ingrese el siguiente visitante.",
                    "Información", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void registrarVisita(PersonalDeControl view, List<Visitante> visitantes, List<File> imagenes) {
        int cantidadTotal;

        try {
            cantidadTotal = Integer.parseInt(view.getCantidadDeVisitantesCombo().getSelectedItem().toString());
        } catch (NumberFormatException e) {
            mostrarError("Cantidad de visitantes no válida");
            return;
        }

        if (cantidadTotal <= 0) {
            mostrarError("Debe seleccionar y añadir al menos un visitante.");
            return;
        }
        if (!validarCamposVisita(view)) {
            return;
        }

        String identificacionPreso = view.getIdentificacionPresoVisita().getText().trim();
        String duracion = view.getDuracionVisita().getSelectedItem().toString();
        String tipo = view.getTipoVisita().getSelectedItem().toString();
        String lugar = view.getLugarVisita().getSelectedItem().toString();
        String horaSeleccionada = view.getHoraVisita().getSelectedItem().toString();
        LocalTime horaVisita = LocalTime.parse(horaSeleccionada);
        Date fechaSeleccionada = view.getFechaVisita().getDate();

        LocalDate fecha = fechaSeleccionada.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (fecha.isAfter(LocalDate.now())) {
            mostrarError("La fecha de la visita no puede ser en el futuro.\nEste formulario es para guardar visitas ya realizadas");
            return;
        }

        Preso preso = new PresoDAO().buscarPresoPorIdentificacion(identificacionPreso);
        if (preso == null) {
            mostrarError("No se encontró ningún preso con esa identificación");
            return;
        }

        Visita nuevaVisita = new Visita(0, fecha, horaVisita, duracion, tipo, lugar, preso, null);
        nuevaVisita.getVisitantes().addAll(visitantes);
        visitaDAO.guardarVisita(nuevaVisita);

        for (int i = 0; i < nuevaVisita.getVisitantes().size(); i++) {
            Visitante visitante = nuevaVisita.getVisitantes().get(i);
            File imagen = imagenes.get(i);
            visitanteDAO.guardarVisitante(visitante, imagen);
        }

        visitantes.clear();
        imagenes.clear();
        limpiarCamposVisita(view);

        JOptionPane.showMessageDialog(null,
                "\u00a1Visita registrada exitosamente!",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }

    public void cargarHistorialVisitas(String identificacionPreso, JTable tabla) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);

        List<Visita> visitas = visitaDAO.cargarPorIdentificacionPreso(identificacionPreso);
        for (Visita visita : visitas) {
            for (Visitante visitante : visita.getVisitantes()) {
                modelo.addRow(new Object[]{
                    visita.getId(),
                    visitante.getIdentificacion(),
                    visita.getFechaVisita(),
                    visita.getHoraVisita(),
                    visita.getDuracionVisitaEnHoras(),
                    visita.getTipoVisita(),
                    visita.getLugarVisita(),
                    identificacionPreso
                });
            }
        }
    }

    public void cargarHistorialVisitantes(String identificacionPreso, JTable tabla) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);

        List<Visita> visitas = visitaDAO.cargarPorIdentificacionPreso(identificacionPreso);

        Map<String, Integer> contadorVisitas = new HashMap<>();
        Map<String, Visitante> infoVisitantes = new LinkedHashMap<>();

        for (Visita visita : visitas) {
            for (Visitante visitante : visita.getVisitantes()) {
                String idVisitante = visitante.getIdentificacion();
                contadorVisitas.put(idVisitante, contadorVisitas.getOrDefault(idVisitante, 0) + 1);
                infoVisitantes.putIfAbsent(idVisitante, visitante);
            }
        }

        for (Map.Entry<String, Visitante> entry : infoVisitantes.entrySet()) {
            Visitante visitante = entry.getValue();

            visitante = visitanteDAO.buscarVisitantePorIdentificacion(visitante.getIdentificacion());

            int totalVisitas = contadorVisitas.get(visitante.getIdentificacion());
            ImageIcon foto = cargarImagen(visitante.getFotoPath());

            modelo.addRow(new Object[]{
                foto,
                visitante.getId(),
                visitante.getNombreCompleto(),
                visitante.getEmail(),
                visitante.getEdad(),
                visitante.getIdentificacion(),
                visitante.getSexo(),
                visitante.getNacionalidad(),
                visitante.getRelacionConPreso(),
                totalVisitas,
                identificacionPreso
            });
        }
    }

    private ImageIcon cargarImagen(String path) {
        File imgFile = new File(path);
        if (!imgFile.exists()) {
            return null;
        }
        try {
            Image img = ImageIO.read(imgFile);
            return new ImageIcon(img.getScaledInstance(60, 60, Image.SCALE_SMOOTH));
        } catch (IOException e) {
            return null;
        }
    }

    public void cargarTodosLosPresos(JTable tabla) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);

        PresoDAO presoDAO = new PresoDAO();
        List<Preso> presos = presoDAO.cargarTodos();

        for (Preso preso : presos) {
            ImageIcon foto = cargarImagen(preso.getFotoPath());
            modelo.addRow(new Object[]{
                foto,
                preso.getId(),
                preso.getNombreCompleto(),
                preso.getEdad(),
                preso.getIdentificacion(),
                preso.getNacionalidad(),
                preso.getCeldaAsignada(),
                preso.getSeccionAsignada()
            });
        }
    }

    public void buscarPresoPorIdentificacion(String identificacion, JTable tabla) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);

        if (identificacion.isEmpty()) {
            mostrarError("Ingrese una identificación para buscar");
            return;
        }

        Preso preso = new PresoDAO().buscarPresoPorIdentificacion(identificacion);
        if (preso != null) {
            ImageIcon foto = cargarImagen(preso.getFotoPath());
            modelo.addRow(new Object[]{
                foto,
                preso.getId(),
                preso.getNombreCompleto(),
                preso.getEdad(),
                preso.getIdentificacion(),
                preso.getNacionalidad(),
                preso.getCeldaAsignada(),
                preso.getSeccionAsignada()
            });
        } else {
            mostrarError("No se encontró ningún preso con esa identificación");
        }
    }

    public File seleccionarImagen(Component parent, JLabel vistaPrevia) {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Imágenes (JPG, PNG, GIF)", "jpg", "jpeg", "png", "gif");
        fileChooser.setFileFilter(filter);

        int resultado = fileChooser.showOpenDialog(parent);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File imagenSeleccionada = fileChooser.getSelectedFile();
            try {
                BufferedImage originalImage = ImageIO.read(imagenSeleccionada);
                Image imgEscalada = originalImage.getScaledInstance(
                        vistaPrevia.getWidth(),
                        vistaPrevia.getHeight(),
                        Image.SCALE_SMOOTH);
                vistaPrevia.setIcon(new ImageIcon(imgEscalada));
                return imagenSeleccionada;
            } catch (IOException ex) {
                mostrarError("Error al cargar la imagen: " + ex.getMessage());
                return null;
            }
        }
        return null;
    }

    public void configurarTablaImagenes(JTable tabla) {
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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

    public void cargarDatosPresoEnTablaPorSeccion(String seccionFiltrada, JTable tablaPresos) {
        DefaultTableModel modelo = (DefaultTableModel) tablaPresos.getModel();
        modelo.setRowCount(0);

        PresoDAO presoDAO = new PresoDAO();
        List<Preso> presos = presoDAO.cargarTodos();

        for (Preso preso : presos) {
            if (preso.getSeccionAsignada() != null && preso.getSeccionAsignada().equalsIgnoreCase(seccionFiltrada)) {
                ImageIcon foto = null;
                if (preso.getFotoPath() != null && !preso.getFotoPath().isEmpty()) {
                    foto = cargarImagen(preso.getFotoPath());
                }

                modelo.addRow(new Object[]{
                    foto,
                    preso.getId(),
                    preso.getNombreCompleto(),
                    preso.getEdad(),
                    preso.getIdentificacion(),
                    preso.getNacionalidad(),
                    preso.getCeldaAsignada(),
                    preso.getSeccionAsignada()
                });
            }
        }

        tablaPresos.revalidate();
        tablaPresos.repaint();
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public Preso buscarPresoPorIdentificacion(String identificacion) {
        return new PresoDAO().buscarPresoPorIdentificacion(identificacion);
    }

    private boolean hayCambiosVisitante(Visitante visitanteOriginal,
            String primerNombre,
            String segundoNombre,
            String primerApellido,
            String segundoApellido,
            String edad,
            String sexo,
            String relacion,
            File imagen) {

        if (!primerNombre.trim().isEmpty() && !primerNombre.equals(visitanteOriginal.getPrimerNombre())) {
            return true;
        }
        if (!segundoNombre.trim().isEmpty() && !segundoNombre.equals(visitanteOriginal.getSegundoNombre())) {
            return true;
        }

        if (!primerApellido.trim().isEmpty() && !primerApellido.equals(visitanteOriginal.getPrimerApellido())) {
            return true;
        }

        if (!segundoApellido.trim().isEmpty() && !segundoApellido.equals(visitanteOriginal.getSegundoApellido())) {
            return true;
        }

        if (!edad.trim().isEmpty() && Integer.parseInt(edad.trim()) != visitanteOriginal.getEdad()) {
            return true;
        }

        if (!sexo.equals("< Seleccionar >") && !sexo.equals(visitanteOriginal.getSexo())) {
            return true;
        }

        if (!relacion.equals("< Seleccionar >") && !relacion.equals(visitanteOriginal.getRelacionConPreso())) {
            return true;
        }

        if (imagen != null && !imagen.equals(new File(visitanteOriginal.getFotoPath()))) {
            return true;
        }

        return false;
    }

    public Visitante actualizarVisitante(Visitante visitanteOriginal,
            String primerNombre,
            String segundoNombre,
            String primerApellido,
            String segundoApellido,
            String edad,
            String sexo,
            String relacion,
            File imagen) {

        try {
            if (!hayCambiosVisitante(visitanteOriginal, primerNombre, segundoNombre, primerApellido,
                    segundoApellido, edad, sexo, relacion, imagen)) {
                mostrarError("No hay cambios para guardar");
                return null;
            }

            if (!primerNombre.trim().isEmpty() && !primerNombre.equals(visitanteOriginal.getPrimerNombre())) {
                if (!primerNombre.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]{2,}$")) {
                    mostrarError("El primer nombre solo debe contener letras y tener al menos 2 caracteres.");
                    return null;
                }
            }

            if (!segundoNombre.trim().isEmpty() && !segundoNombre.equals(visitanteOriginal.getSegundoNombre())) {
                if (!segundoNombre.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]{2,}$")) {
                    mostrarError("El segundo nombre solo debe contener letras y tener al menos 2 caracteres.");
                    return null;
                }
            }

            if (!primerApellido.trim().isEmpty() && !primerApellido.equals(visitanteOriginal.getPrimerApellido())) {
                if (!primerApellido.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]{2,}$")) {
                    mostrarError("El primer apellido solo debe contener letras y tener al menos 2 caracteres.");
                    return null;
                }
            }

            if (!segundoApellido.trim().isEmpty() && !segundoApellido.equals(visitanteOriginal.getSegundoApellido())) {
                if (!segundoApellido.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]{2,}$")) {
                    mostrarError("El segundo apellido solo debe contener letras y tener al menos 2 caracteres.");
                    return null;
                }
            }

            if (!edad.trim().isEmpty()) {
                int edadVisitanteInt;

                try {
                    edadVisitanteInt = Integer.parseInt(edad.trim());
                } catch (NumberFormatException ex) {
                    mostrarError("La edad debe ser un número válido.");
                    return null;
                }

                if (edadVisitanteInt < 1 || edadVisitanteInt > 120) {
                    mostrarError("La edad debe estar entre 1 y 120 años.");
                    return null;
                }

                boolean edadModificada = edadVisitanteInt != visitanteOriginal.getEdad();
                boolean mismoGrupoEdad = (edadVisitanteInt < 18 && visitanteOriginal.getEdad() < 18)
                        || (edadVisitanteInt >= 18 && visitanteOriginal.getEdad() >= 18);

                if (edadModificada && !mismoGrupoEdad) {
                    mostrarError("No se puede cambiar de menor a mayor de edad o viceversa.");
                    return null;
                }
            }

            String relacionFinal = relacion.equals("< Seleccionar >") ? visitanteOriginal.getRelacionConPreso() : relacion;

            String sexoFinal = sexo.equals("< Seleccionar >") ? visitanteOriginal.getSexo() : sexo;

            Visitante visitanteActualizado = visitanteDAO.modificarDatosVisitanteYDevolver(
                    visitanteOriginal.getIdentificacion(),
                    primerNombre.trim().isEmpty() ? visitanteOriginal.getPrimerNombre() : primerNombre.trim(),
                    segundoNombre.trim().isEmpty() ? visitanteOriginal.getSegundoNombre() : segundoNombre.trim(),
                    primerApellido.trim().isEmpty() ? visitanteOriginal.getPrimerApellido() : primerApellido.trim(),
                    segundoApellido.trim().isEmpty() ? visitanteOriginal.getSegundoApellido() : segundoApellido.trim(),
                    edad.trim().isEmpty() ? visitanteOriginal.getEdad() : Integer.parseInt(edad.trim()),
                    sexoFinal,
                    relacionFinal,
                    imagen != null ? imagen : new File(visitanteOriginal.getFotoPath())
            );

            if (visitanteActualizado != null) {
                JOptionPane.showMessageDialog(null, "Visitante actualizado correctamente. Si desea continuar modificando vuelva a seleccionar un visitante en la tabla.");

                return visitanteActualizado;
            } else {
                mostrarError("No se pudo actualizar el visitante");
                return null;
            }

        } catch (Exception e) {
            mostrarError("Error al actualizar visitante: " + e.getMessage());
            return null;
        }
    }

    private boolean hayCambiosVisita(Visita visitaOriginal,
            String duracion,
            String tipo,
            String lugar) {

        if (duracion != null && duracion.equals(visitaOriginal.getDuracionVisitaEnHoras())) {
            return false;
        }
        if (tipo != null && tipo.equals(visitaOriginal.getTipoVisita())) {
            return false;
        }
        if (lugar != null && lugar.equals(visitaOriginal.getLugarVisita())) {
            return false;
        }

        return true;
    }

    public Visita actualizarVisita(int idVisita,
            String nuevaDuracion,
            String nuevoTipo,
            String nuevoLugar) {

        Visita visitaOriginal = visitaDAO.buscarVisitaPorId(idVisita);

        if (visitaOriginal == null) {
            mostrarError("No se encontró la visita con el ID proporcionado.");
            return null;
        }

        String duracionFinal = nuevaDuracion.equals("< Seleccionar >") ? visitaOriginal.getDuracionVisitaEnHoras() : nuevaDuracion;
        String tipoFinal = nuevoTipo.equals("< Seleccionar >") ? visitaOriginal.getTipoVisita() : nuevoTipo;
        String lugarFinal = nuevoLugar.equals("< Seleccionar >") ? visitaOriginal.getLugarVisita() : nuevoLugar;
        
        if (!hayCambiosVisita(visitaOriginal, nuevaDuracion, nuevoTipo, nuevoLugar)) {
            mostrarError("No hay cambios para guardar.");
            return null;
        }
        Visita visitaActualizada = visitaDAO.modificarDatosVisitaYDevolver(
                idVisita,
                duracionFinal,
                tipoFinal,
                lugarFinal
        );

        if (visitaActualizada != null) {
            JOptionPane.showMessageDialog(null, "Visita actualizada correctamente.  Si desea continuar modificando vuelva a seleccionar una visita en la tabla");
            return visitaActualizada;
        } else {
            mostrarError("Error al guardar los cambios de la visita.");
            return null;
        }
    }

    public Visitante obtenerVisitantePorIdentificacion(String identificacion) {
        try {
            if (identificacion == null || identificacion.trim().isEmpty()) {
                throw new IllegalArgumentException("La identificación no puede estar vacía");
            }
            return visitanteDAO.buscarVisitantePorIdentificacion(identificacion);
        } catch (Exception e) {
            mostrarError("Error al obtener visitante: " + e.getMessage());
            return null;
        }
    }
}
