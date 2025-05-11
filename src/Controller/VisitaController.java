package Controller;

import DAO.PersonalDeControlDAO;
import DAO.PresoDAO;
import DAO.VisitaDAO;
import DAO.VisitanteDAO;
import Model.Entities.Preso;
import Model.Entities.Visita;
import Model.Entities.Visitante;
import Model.Constants.EstadoVisitaEnum;
import Model.Constants.EstadoVisitanteEnum;
import View.PersonalDeControl;
import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class VisitaController {

    private VisitaDAO visitaDAO = VisitaDAO.getInstancia();
    private VisitanteDAO visitanteDAO = VisitanteDAO.getInstancia();
    private PersonalDeControlDAO personalDeControlDAO = PersonalDeControlDAO.getInstancia();
    private Visita visitaTemporal = null;

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
        view.getTipoVisita().setSelectedIndex(0);
        view.getLugarVisita().setSelectedIndex(0);
        view.getHoraVisita().setSelectedIndex(0);
    }

    public boolean existeVisitaTemporal() {
        return visitaTemporal != null;
    }

    public boolean validarCamposVisitante(PersonalDeControl view, File imagen, int cantidadTotal, List<Visitante> visitantesTemporales) {
        if (view.getCantidadDeVisitantesCombo().getSelectedIndex() == 0) {
            mostrarError("Primero seleccione la cantidad de visitantes");
            return false;
        }

        int cantidadSeleccionada = Integer.parseInt(view.getCantidadDeVisitantesCombo().getSelectedItem().toString());

        if (view.getPrimerNombreVisitante().getText().trim().isEmpty()
                || view.getPrimerApellidoVisitante().getText().trim().isEmpty()
                || view.getSegundoApellidoVisitante().getText().trim().isEmpty()
                || view.getEdadVisitante().getText().trim().isEmpty()
                || view.getIdentificacionVisitante().getText().trim().isEmpty()
                || view.getSexoVisitante().getSelectedIndex() == 0
                || view.getRelacionConPresoVisitante().getSelectedIndex() == 0
                || view.getNacionalidadVisitante().getSelectedIndex() == 0) {
            mostrarError("Por favor complete todos los campos del formulario");
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
        try {
            long identificacionNum = Long.parseLong(identificacion);
            if (identificacionNum < 0) {
                mostrarError("La identificación no puede ser un número negativo.");
                return false;
            }
            if (identificacion.length() < 6 || identificacion.length() > 10) {
                mostrarError("La identificación debe contener entre 6 y 10 dígitos.");
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarError("La identificación debe contener solo números.");
            return false;
        }

        int edadVisitante;
        try {
            edadVisitante = Integer.parseInt(view.getEdadVisitante().getText().trim());
            if (edadVisitante < 1 || edadVisitante > 120) {
                mostrarError("La edad debe estar entre 1 y 120 años");
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarError("La edad debe ser un número válido");
            return false;
        }

        boolean esMenorEnGrupoDe2 = (cantidadSeleccionada == 2 && edadVisitante < 18 && !visitantesTemporales.isEmpty());
        if (!esMenorEnGrupoDe2) {
            String email = view.getEmailVisitante().getText().trim();
            if (email.isEmpty()) {
                mostrarError("Por favor ingrese un correo electrónico");
                return false;
            }
            if (!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")) {
                mostrarError("Por favor ingrese un correo electrónico válido.");
                return false;
            }
        }

        for (Visitante v : visitantesTemporales) {
            if (v.getIdentificacion().equals(identificacion)) {
                mostrarError("Ya existe un visitante con esta identificación en el grupo actual");
                return false;
            }
        }

        if (!esMenorEnGrupoDe2) {
            String email = view.getEmailVisitante().getText().trim();
            for (Visitante v : visitantesTemporales) {
                if (v.getEmail() != null && v.getEmail().equalsIgnoreCase(email)) {
                    mostrarError("Ya existe un visitante con este email en el grupo actual");
                    return false;
                }
            }
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

        if (view.getTipoVisita().getSelectedIndex() == 0
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

        if (visitaTemporal == null) {
            mostrarError("Primero debe ingresar los datos de la visita antes de añadir visitantes.");
            return;
        }

        try {
            cantidadTotal = Integer.parseInt(view.getCantidadDeVisitantesCombo().getSelectedItem().toString());
        } catch (NumberFormatException e) {
            mostrarError("Cantidad de visitantes no válida");
            return;
        }

        if (visitantesTemporales.size() >= cantidadTotal) {
            mostrarError("Ya ha registrado el máximo de " + cantidadTotal + " visitantes para esta visita.");
            return;
        }

        String identificacion = view.getIdentificacionVisitante().getText().trim();
        Visitante visitanteExistente = visitanteDAO.buscarVisitantePorIdentificacion(identificacion);

        if (visitanteExistente != null) {
            if (visitanteExistente.getEstado() == EstadoVisitanteEnum.DESHABILITADO) {
                mostrarError("Este visitante está DESHABILITADO y no puede realizar visitas.");
                return;
            }

            if (!validarDatosCoincidentes(view, visitanteExistente)) {
                mostrarError("Los datos no coinciden con el visitante registrado previamente");
                return;
            }

            for (Visitante v : visitantesTemporales) {
                if (v.getIdentificacion().equals(identificacion)) {
                    mostrarError("Ya se ha ingresado un visitante con esta identificación");
                    return;
                }
            }

            if (visitaDAO.tieneVisitaEnFecha(identificacion, visitaTemporal.getFechaVisita())) {
                mostrarError("Este visitante ya tiene una visita programada para el "
                        + visitaTemporal.getFechaVisita() + ".\n"
                        + "Solo se permite UNA visita por día por visitante.");
                return;
            }

            Preso presoVisitante = PresoDAO.getInstancia().buscarPresoPorIdentificacion(identificacion);
            if (presoVisitante != null) {
                mostrarError("Los presos no pueden ser visitantes. \n"
                        + "La identificación " + identificacion + " pertenece al preso: \n"
                        + presoVisitante.getNombresCompletos() + " " + presoVisitante.getApellidosCompletos() + "\n"
                        + "Celda: " + presoVisitante.getCeldaAsignada());
                return;
            }

            visitantesTemporales.add(visitanteExistente);
            imagenesTemporales.add(new File(visitanteExistente.getFotoPath()));
        } else {
            if (!validarCamposVisitante(view, imagen, 0, visitantesTemporales)) {
                return;
            }

            String primerNombre = view.getPrimerNombreVisitante().getText().trim();
            String segundoNombre = view.getSegundoNombreVisitante().getText().trim();
            String primerApellido = view.getPrimerApellidoVisitante().getText().trim();
            String segundoApellido = view.getSegundoApellidoVisitante().getText().trim();
            int edad = Integer.parseInt(view.getEdadVisitante().getText().trim());
            String sexo = view.getSexoVisitante().getSelectedItem().toString();
            String nacionalidad = view.getNacionalidadVisitante().getSelectedItem().toString();
            String relacion = view.getRelacionConPresoVisitante().getSelectedItem().toString();
            String email = view.getEmailVisitante().getText().trim();

            Visitante nuevoVisitante = new Visitante(primerNombre, segundoNombre, primerApellido,
                    segundoApellido, edad, sexo, nacionalidad, identificacion,
                    relacion, imagen.getAbsolutePath(), email);

            visitantesTemporales.add(nuevoVisitante);
            imagenesTemporales.add(imagen);
        }

        limpiarCamposVisitante(view);
        view.setImagenVisitanteSeleccionada(null);

        if (visitantesTemporales.size() >= cantidadTotal) {
            JOptionPane.showMessageDialog(null,
                    "Ya se añadieron todos los visitantes. Ahora puede registrar la visita final.",
                    "Información", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null,
                    "Visitante añadido (" + visitantesTemporales.size() + " de " + cantidadTotal + ").",
                    "Información", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void cancelarProcesoVisita(PersonalDeControl view, List<Visitante> visitantesTemporales, List<File> imagenesTemporales) {
        limpiarCamposVisitante(view);
        limpiarCamposVisita(view);

        if (visitantesTemporales != null) {
            visitantesTemporales.clear();
        }

        if (imagenesTemporales != null) {
            imagenesTemporales.clear();
        }

        visitaTemporal = null;

        view.getCantidadDeVisitantesCombo().setSelectedIndex(0);
        view.setImagenVisitanteSeleccionada(null);

        JOptionPane.showMessageDialog(view,
                "Por favor, inicie el proceso de registro de visita nuevamente.",
                "Proceso cancelado",
                JOptionPane.WARNING_MESSAGE);
    }

    private boolean validarDatosCoincidentes(PersonalDeControl view, Visitante visitanteExistente) {
        if (!view.getPrimerNombreVisitante().getText().trim().equalsIgnoreCase(visitanteExistente.getPrimerNombre())) {
            return false;
        }
        if (!view.getSegundoNombreVisitante().getText().trim().equalsIgnoreCase(visitanteExistente.getSegundoNombre())) {
            return false;
        }
        if (!view.getPrimerApellidoVisitante().getText().trim().equalsIgnoreCase(visitanteExistente.getPrimerApellido())) {
            return false;
        }
        if (!view.getSegundoApellidoVisitante().getText().trim().equalsIgnoreCase(visitanteExistente.getSegundoApellido())) {
            return false;
        }

        try {
            int edadIngresada = Integer.parseInt(view.getEdadVisitante().getText().trim());
            if (edadIngresada != visitanteExistente.getEdad()) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }

        if (!view.getSexoVisitante().getSelectedItem().toString().equals(visitanteExistente.getSexo())) {
            return false;
        }
        if (!view.getNacionalidadVisitante().getSelectedItem().toString().equals(visitanteExistente.getNacionalidad())) {
            return false;
        }
        if (!view.getRelacionConPresoVisitante().getSelectedItem().toString().equals(visitanteExistente.getRelacionConPreso())) {
            return false;
        }

        if (visitanteExistente.getEdad() >= 18
                && !view.getEmailVisitante().getText().trim().equalsIgnoreCase(visitanteExistente.getEmail())) {
            return false;
        }

        return true;
    }

    public boolean guardarVisitaTemporal(PersonalDeControl view) {
        if (!validarCamposVisita(view)) {
            return false;
        }

        if (visitaTemporal != null && visitaTemporal.getEstado() == EstadoVisitaEnum.EN_PROCESO) {
            mostrarError("Ya tienes una visita en proceso. Debes completar esta visita (guardar visita final)");
            limpiarCamposVisita(view);
            return false;
        }

        try {
            String identificacionPreso = view.getIdentificacionPresoVisita().getText().trim();
            Preso preso = PresoDAO.getInstancia().buscarPresoPorIdentificacion(identificacionPreso);

            if (preso == null) {
                mostrarError("No se encontró ningún preso con la identificación: " + identificacionPreso);
                return false;
            }

            if (!preso.getEstado().equalsIgnoreCase("ACTIVO")) {
                mostrarError("El preso " + preso.getNombresCompletos() + " no puede recibir visitas.\n"
                        + "Motivo: Estado actual = " + preso.getEstado() + "\n"
                        + "Solo permitido: Presos en estado ACTIVO");
                return false;
            }

            if (preso.isEnAislamiento()) {
                mostrarError("El preso " + preso.getNombresCompletos() + " está en aislamiento.\n"
                        + "Celda: " + preso.getCeldaAsignada() + "\n"
                        + "No puede recibir visitas hasta que termine el período de aislamiento");
                return false;
            }

            String nivelSeguridad = preso.getNivelDeSeguridad();
            if (!nivelSeguridad.equalsIgnoreCase("Baja") && !nivelSeguridad.equalsIgnoreCase("Media")) {
                mostrarError("El preso " + preso.getNombresCompletos() + " tiene nivel de seguridad " + nivelSeguridad + ".\n"
                        + "Solo puede recibir visitas con nivel de seguridad Baja o Media");
                return false;
            }

            if (preso.getExpediente() != null && preso.getExpediente().getNivelRiesgo().equalsIgnoreCase("Riesgo alto")) {
                mostrarError("El preso " + preso.getNombresCompletos() + " tiene nivel de riesgo ALTO.\n"
                        + "No puede recibir visitas por motivos de seguridad");
                return false;
            }

            String tipo = view.getTipoVisita().getSelectedItem().toString();
            String lugar = view.getLugarVisita().getSelectedItem().toString();
            String horaSeleccionada = view.getHoraVisita().getSelectedItem().toString();

            Date fechaSeleccionada = view.getFechaVisita().getDate();
            LocalDate fecha = fechaSeleccionada.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalTime horaVisita = LocalTime.parse(horaSeleccionada);

            if (!fecha.isAfter(LocalDate.now())) {
                mostrarError("La fecha debe ser posterior al día actual.\n"
                        + "Fecha seleccionada: " + fecha + "\n"
                        + "Fecha actual: " + LocalDate.now());
                return false;
            }

            List<Visita> visitasExistentes = visitaDAO.cargarPorIdentificacionPresoFechaYHora(
                    identificacionPreso, fecha, horaVisita);

            if (!visitasExistentes.isEmpty()) {
                Visita visitaExistente = visitasExistentes.get(0);
                mostrarError("El preso ya tiene una visita programada para esta fecha y hora:\n\n"
                        + "Fecha: " + visitaExistente.getFechaVisita() + "\n"
                        + "Hora: " + visitaExistente.getHoraVisita() + "\n"
                        + "Tipo: " + visitaExistente.getTipoVisita() + "\n"
                        + "Estado: " + visitaExistente.getEstado());
                return false;
            }

            visitaTemporal = new Visita(
                    0,
                    fecha,
                    horaVisita,
                    tipo,
                    lugar,
                    preso,
                    new ArrayList<>()
            );

            visitaTemporal.setEstado(EstadoVisitaEnum.EN_PROCESO);

            JOptionPane.showMessageDialog(null,
                    "Visita guardada temporalmente. Ahora ingrese los visitantes.",
                    "Información", JOptionPane.INFORMATION_MESSAGE);

            limpiarCamposVisita(view);

            return true;

        } catch (Exception e) {
            mostrarError("Error al guardar visita temporal: " + e.getMessage());
            return false;
        }
    }

    public void guardarVisitaFinal(PersonalDeControl view, List<Visitante> visitantes, List<File> imagenes) {
        if (visitaTemporal == null) {
            mostrarError("Primero debe ingresar los datos de la visita.");
            return;
        }

        int cantidadEsperada;
        try {
            cantidadEsperada = Integer.parseInt(view.getCantidadDeVisitantesCombo().getSelectedItem().toString());
        } catch (NumberFormatException e) {
            mostrarError("Cantidad de visitantes no válida");
            return;
        }

        if (visitantes == null || visitantes.size() < cantidadEsperada) {
            mostrarError("Debe registrar todos los visitantes antes de guardar.\n"
                    + "Visitantes registrados: " + (visitantes != null ? visitantes.size() : 0) + "\n"
                    + "Visitantes requeridos: " + cantidadEsperada);
            return;
        }

        if (imagenes == null || imagenes.size() != visitantes.size()) {
            mostrarError("Error en las imágenes de los visitantes");
            return;
        }

        visitaTemporal.getVisitantes().addAll(visitantes);

        visitaDAO.guardarVisita(visitaTemporal);

        for (int i = 0; i < visitantes.size(); i++) {
            visitanteDAO.guardarVisitante(visitantes.get(i), imagenes.get(i));
        }

        visitaTemporal = null;
        visitantes.clear();
        imagenes.clear();
        view.getCantidadDeVisitantesCombo().setSelectedIndex(0);

        JOptionPane.showMessageDialog(null,
                "¡Visita y " + cantidadEsperada + " visitantes registrados exitosamente!",
                "Registro completado",
                JOptionPane.INFORMATION_MESSAGE);
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
                    identificacionPreso,
                    visita.getEstado()
                });
            }
        }
    }

    public void cargarHistorialVisitantes(String identificacionPreso, JTable tabla) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);

        List<Visita> visitas = visitaDAO.cargarPorIdentificacionPreso(identificacionPreso);

        Map<String, Object[]> visitantesMap = new LinkedHashMap<>();
        Map<String, Integer> contadorVisitas = new HashMap<>();

        for (Visita visita : visitas) {
            for (Visitante visitante : visita.getVisitantes()) {
                String idVisitante = visitante.getIdentificacion();

                contadorVisitas.put(idVisitante, contadorVisitas.getOrDefault(idVisitante, 0) + 1);

                if (!visitantesMap.containsKey(idVisitante)) {
                    ImageIcon foto = cargarImagen(visitante.getFotoPath());
                    visitantesMap.put(idVisitante, new Object[]{
                        foto,
                        visitante.getId(),
                        visitante.getNombresCompletos(),
                        visitante.getApellidosCompletos(),
                        visitante.getEmail(),
                        visitante.getEdad(),
                        visitante.getIdentificacion(),
                        visitante.getSexo(),
                        visitante.getNacionalidad(),
                        visitante.getRelacionConPreso(),
                        contadorVisitas.get(idVisitante),
                        identificacionPreso,
                        visitante.getEstado().toString()
                    });
                } else {
                    Object[] datos = visitantesMap.get(idVisitante);
                    datos[10] = contadorVisitas.get(idVisitante);
                    datos[12] = visitante.getEstado().toString();
                }
            }
        }

        for (Object[] datos : visitantesMap.values()) {
            modelo.addRow(datos);
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

        PresoDAO presoDAO = PresoDAO.getInstancia();
        List<Preso> presos = presoDAO.cargarTodos();

        for (Preso preso : presos) {
            ImageIcon foto = cargarImagen(preso.getFotoPath());
            modelo.addRow(new Object[]{
                foto,
                preso.getId(),
                preso.getNombresCompletos(),
                preso.getApellidosCompletos(),
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

        Preso preso = PresoDAO.getInstancia().buscarPresoPorIdentificacion(identificacion);
        if (preso != null) {
            ImageIcon foto = cargarImagen(preso.getFotoPath());
            modelo.addRow(new Object[]{
                foto,
                preso.getId(),
                preso.getNombresCompletos(),
                preso.getApellidosCompletos(),
                preso.getApellidosCompletos(),
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

        PresoDAO presoDAO = PresoDAO.getInstancia();
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
                    preso.getNombresCompletos(),
                    preso.getApellidosCompletos(),
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
        return PresoDAO.getInstancia().buscarPresoPorIdentificacion(identificacion);
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

        if (!primerNombre.equals(visitanteOriginal.getPrimerNombre())) {
            return true;
        }
        if (!segundoNombre.equals(visitanteOriginal.getSegundoNombre())) {
            return true;
        }
        if (!primerApellido.equals(visitanteOriginal.getPrimerApellido())) {
            return true;
        }
        if (!segundoApellido.equals(visitanteOriginal.getSegundoApellido())) {
            return true;
        }

        try {
            if (!edad.isEmpty() && Integer.parseInt(edad) != visitanteOriginal.getEdad()) {
                return true;
            }
        } catch (NumberFormatException e) {
            return true;
        }

        if (!sexo.equals("< Seleccionar >") && !sexo.equals(visitanteOriginal.getSexo())) {
            return true;
        }
        if (!relacion.equals("< Seleccionar >") && !relacion.equals(visitanteOriginal.getRelacionConPreso())) {
            return true;
        }

        if (imagen != null) {
            try {
                File originalFile = new File(visitanteOriginal.getFotoPath());
                return !imagen.getCanonicalPath().equals(originalFile.getCanonicalPath());
            } catch (IOException e) {
                return true;
            }
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

    private boolean hayCambiosVisita(Visita visitaOriginal, String tipo, String lugar) {
        return (!tipo.equals(visitaOriginal.getTipoVisita()))
                || (!lugar.equals(visitaOriginal.getLugarVisita()));
    }

    public Visita actualizarVisita(int idVisita,
            String nuevoTipo,
            String nuevoLugar) {

        Visita visitaOriginal = visitaDAO.buscarVisitaPorId(idVisita);

        if (visitaOriginal == null) {
            mostrarError("No se encontró la visita con el ID proporcionado.");
            return null;
        }

        String tipoFinal = nuevoTipo.equals("< Seleccionar >") ? visitaOriginal.getTipoVisita() : nuevoTipo;
        String lugarFinal = nuevoLugar.equals("< Seleccionar >") ? visitaOriginal.getLugarVisita() : nuevoLugar;

        if (!hayCambiosVisita(visitaOriginal, tipoFinal, lugarFinal)) {
            mostrarError("No hay cambios para guardar.");
            return null;
        }

        Visita visitaActualizada = visitaDAO.modificarDatosVisitaYDevolver(
                idVisita,
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

    public Visita cambiarEstadoVisita(int idVisita, EstadoVisitaEnum nuevoEstado) {
        if (nuevoEstado == null) {
            mostrarError("Debe seleccionar un estado válido");
            return null;
        }

        Visita visita = visitaDAO.buscarVisitaPorId(idVisita);
        if (visita == null) {
            mostrarError("No se encontró la visita con ID: " + idVisita);
            return null;
        }

        if (visita.getEstado() == nuevoEstado) {
            mostrarError("La visita ya tiene el estado: " + nuevoEstado.toString());
            return null;
        }

        if (nuevoEstado == EstadoVisitaEnum.FINALIZADA) {
            LocalDate fechaActual = LocalDate.now();
            LocalDate fechaVisita = visita.getFechaVisita();

            if (!fechaActual.isAfter(fechaVisita)) {
                mostrarError("No puede finalizar una visita el mismo día o antes.\n"
                        + "Fecha de la visita: " + fechaVisita + "\n"
                        + "Puede finalizar a partir de: " + fechaVisita.plusDays(1));
                return null;
            }
        }

        Visita visitaActualizada = visitaDAO.modificarEstadoVisitaYDevolver(idVisita, nuevoEstado);

        if (visitaActualizada != null) {
            JOptionPane.showMessageDialog(null,
                    "Estado de visita actualizado correctamente a: " + nuevoEstado.toString(),
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            return visitaActualizada;
        } else {
            mostrarError("Error al guardar el cambio de estado");
            return null;
        }
    }

    public Visitante cambiarEstadoVisitante(String identificacion, EstadoVisitanteEnum nuevoEstado, String identificacionPreso, JTable tablaVisitantes) {
        if (nuevoEstado == null) {
            mostrarError("Debe seleccionar un estado válido");
            return null;
        }

        Visitante visitante = visitanteDAO.buscarVisitantePorIdentificacion(identificacion);
        if (visitante == null) {
            mostrarError("No se encontró el visitante con identificación: " + identificacion);
            return null;
        }

        if (visitante.getEstado() == nuevoEstado) {
            mostrarError("El visitante ya tiene el estado: " + nuevoEstado.toString());
            return null;
        }

        Visitante visitanteActualizado = visitanteDAO.modificarEstadoVisitanteYDevolver(identificacion, nuevoEstado);

        if (visitanteActualizado != null) {
            if (identificacionPreso != null && tablaVisitantes != null) {
                cargarHistorialVisitantes(identificacionPreso, tablaVisitantes);
            }

            JOptionPane.showMessageDialog(null,
                    "Estado del visitante actualizado correctamente a: " + nuevoEstado.toString(),
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            return visitanteActualizado;
        } else {
            mostrarError("Error al cambiar el estado del visitante");
            return null;
        }
    }
}
