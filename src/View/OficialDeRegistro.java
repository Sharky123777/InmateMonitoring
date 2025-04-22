package View;

import Controller.PresoController;
import DAO.CeldaDAO;
import DAO.DelitoDAO;
import DAO.PresoDAO;
import Model.Celda;
import Model.Delito;
import Model.Preso;
import Model.Sentencia;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;

public class OficialDeRegistro extends javax.swing.JFrame {

    private HashMap<String, String[]> datosDelitos;

    CeldaDAO celda = new CeldaDAO();

    private Preso preso;
    private int delitoActual = 1;
    private int totalDelitos = 1;
    private BufferedImage originalImage;
    private File selectedImageFile;
    private Preso presoOriginal;

    private List<Delito> delitosTemporales = new ArrayList<>();

    public OficialDeRegistro() {
        initComponents();
        inicializarMenu();
        cargarDelitosConCodigoYArticulo();
        this.setLocationRelativeTo(null);
        OficialDeRegistroView.setSelectedIndex(1);

        cargarDatosEnTabla();
        configurarTablaImagenes();
        actualizarProgreso();

        celda.generarCeldas("Sección A", 10, 2);
        celda.generarCeldas("Sección B", 10, 2);
        celda.generarCeldas("Sección C", 10, 2);

        cantidadDelitos.addActionListener(e -> {
            actualizarProgreso();
            actualizarEstadoBotonDelito();
        });

        TablaPresos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = TablaPresos.getSelectedRow();
                if (fila >= 0) {
                    String identificacion = (String) TablaPresos.getValueAt(fila, 5);
                    Preso preso = new PresoDAO().buscarPresoPorIdentificacion(identificacion);
                    if (preso != null) {
                        DescripDelito.setText(preso.getDatosExpedienteBasico());
                    }
                }
            }
        });
        
        guardarDelito.addActionListener(e -> agregarDelitoTemporalAñadir());
                     
        ActualizarFotoBoton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                selectedImageFile = fileChooser.getSelectedFile();
                ImageIcon icon = new ImageIcon(selectedImageFile.getPath());
                Image img = icon.getImage().getScaledInstance(nuevaFoto.getWidth(), nuevaFoto.getHeight(), Image.SCALE_SMOOTH);
                nuevaFoto.setIcon(new ImageIcon(img));
            }
        });

        agregarValidacionInstantanea();
        Siguiente1.setEnabled(false);
        Siguiente2.setEnabled(false);
        AñadirPreso.setEnabled(false);

        actualizarEstadoBotonDatosPersonales();
        actualizarEstadoBotonInformacionJudicial();
        actualizarEstadoBotonDelito();

        DescripcionDelito.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                actualizarEstadoBoton();
            }
        });
        identificacionB.setText("Buscar preso por identificación");
        identificacionB.setForeground(Color.GRAY);
        identificacionB.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (identificacionB.getText().equals("Buscar preso por identificación")) {
                    identificacionB.setText("");
                    identificacionB.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent e) {
                if (identificacionB.getText().isEmpty()) {
                    identificacionB.setText("Buscar preso por identificación");
                    identificacionB.setForeground(Color.GRAY);
                }
            }
        });

    }

    public void inicializarMenu() {
        JMenuItem Expediente = new JMenuItem("Expediente");
        JMenuItem Eliminar = new JMenuItem("Eliminar");
        JMenuItem Sanciones = new JMenuItem("Sanciones");
        JMenuItem historialMedico = new JMenuItem("Historial Medico");
        JMenuItem historialVisita = new JMenuItem("Historial Visitas");
        JMenuItem Informacion = new JMenuItem("Informacion General");
        JMenuItem Actualizar = new JMenuItem("Actualizar Información");
        JMenuItem AñadirDelito = new JMenuItem("Añadir Delito");

        ppMenuTablaPresos.add(Expediente);
        ppMenuTablaPresos.add(Informacion);
        ppMenuTablaPresos.add(AñadirDelito);

        ppMenuTablaPresos.add(Eliminar);
        ppMenuTablaPresos.add(Actualizar);
        ppMenuTablaPresos.add(Sanciones);
        ppMenuTablaPresos.add(historialVisita);

        ppMenuTablaPresos.add(historialMedico);

        TablaPresos.setComponentPopupMenu(ppMenuTablaPresos);
        
        
        AñadirDelito.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int filaSeleccionada = TablaPresos.getSelectedRow();
                if (filaSeleccionada == -1) {
                    JOptionPane.showMessageDialog(null,
                            "Seleccione un preso primero",
                            "Advertencia",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String identificacion = (String) TablaPresos.getValueAt(filaSeleccionada, 5);
                PresoDAO presoDAO = new PresoDAO();
                Preso presoEncontrado = presoDAO.buscarPresoPorIdentificacion(identificacion);

                if (presoEncontrado == null) {
                    JOptionPane.showMessageDialog(null,
                            "Preso no encontrado",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                preso = presoEncontrado;

                OficialDeRegistroView.setSelectedIndex(7);
            }
        });
    
        Sanciones.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int filaSeleccionada = TablaPresos.getSelectedRow();
                if (filaSeleccionada == -1) {
                    JOptionPane.showMessageDialog(null,
                            "Seleccione un preso primero",
                            "Advertencia",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String identificacion = (String) TablaPresos.getValueAt(filaSeleccionada, 5);
                PresoDAO presoDAO = new PresoDAO();
                Preso preso = presoDAO.buscarPresoPorIdentificacion(identificacion);

                if (preso == null) {
                    JOptionPane.showMessageDialog(null,
                            "Preso no encontrado",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                nombreS.setText(preso.getPrimerNombre());
                apellidoS.setText(preso.getPrimerApellido());

                String aislamiento = "";

                if (preso.isEnAislamiento() == false) {
                    aislamiento = "No";
                }

                if (preso.isEnAislamiento() == true) {
                    aislamiento = "Sí";

                }

                aislamientoS.setText(aislamiento);
                nivelseguridadS.setText(preso.getNivelDeSeguridad());
                OficialDeRegistroView.setSelectedIndex(6);

            }
        }
        );
        Actualizar.addActionListener(e -> {
            int fila = TablaPresos.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un preso", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String id = TablaPresos.getValueAt(fila, 5).toString();
            Preso preso = new PresoDAO().buscarPresoPorIdentificacion(id);

            if (preso != null) {
                cargarDatosPresoEnFormularioActualizacion(preso);

                OficialDeRegistroView.setSelectedIndex(0);
                TabbedAñadirInformacionGeneral.setSelectedIndex(3);
            } else {
                JOptionPane.showMessageDialog(this, "Preso no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        Eliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int filaSeleccionada = TablaPresos.getSelectedRow();
                if (filaSeleccionada == -1) {
                    JOptionPane.showMessageDialog(null,
                            "Seleccione un preso primero",
                            "Advertencia",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                String identificacion = (String) TablaPresos.getValueAt(filaSeleccionada, 5);
                PresoDAO presoDAO = new PresoDAO();
                Preso preso = presoDAO.buscarPresoPorIdentificacion(identificacion);

                if (preso == null) {
                    JOptionPane.showMessageDialog(null,
                            "Preso no encontrado",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                ValidarFechaDialog dialogo = new ValidarFechaDialog(null, true);
                dialogo.setVisible(true);

                if (!dialogo.isAceptado()) {
                    return;
                }

                Date fechaIngresada = dialogo.getFechaSeleccionada();
                if (fechaIngresada == null) {
                    JOptionPane.showMessageDialog(null,
                            "¡Debe seleccionar una fecha válida!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                LocalDate fechaActual = fechaIngresada.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();

                LocalDate fechaSalida = preso.getSentencia().getFechaSalidaCalculada();

                if (fechaActual.isBefore(fechaSalida)) {
                    JOptionPane.showMessageDialog(null,
                            "No se puede eliminar: El preso no ha completado su condena.\n"
                            + "Fecha de liberación: " + fechaSalida.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int confirmacion = JOptionPane.showConfirmDialog(null,
                        "¿Está seguro de eliminar al preso con identificación " + identificacion + "?",
                        "Confirmar Eliminación",
                        JOptionPane.YES_NO_OPTION);

                if (confirmacion == JOptionPane.YES_OPTION) {
                    boolean eliminado = presoDAO.eliminarPreso(identificacion);
                    if (eliminado) {
                        JOptionPane.showMessageDialog(null, "Preso eliminado correctamente");
                        cargarDatosEnTabla();
                    } else {
                        JOptionPane.showMessageDialog(null,
                                "Error al eliminar el preso",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        Expediente.addActionListener(e -> {
            int filaSeleccionada = TablaPresos.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "¡Selecciona un preso primero!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String identificacion = TablaPresos.getValueAt(filaSeleccionada, 5).toString();
            Preso preso = new PresoDAO().buscarPresoPorIdentificacion(identificacion);

            if (preso != null) {
                cargarExpedienteCompleto(preso);
                OficialDeRegistroView.setSelectedIndex(2);
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el preso", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        tablaExpediente.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tablaExpediente.getSelectedRow();
                if (selectedRow != -1) {
                    try {
                        Object idValue = tablaExpediente.getValueAt(selectedRow, 1);
                        if (idValue == null) {
                            DescripDelito.setText("Sin ID de delito");
                            return;
                        }

                        int idDelito;
                        try {
                            idDelito = Integer.parseInt(idValue.toString());
                        } catch (NumberFormatException ex) {
                            DescripDelito.setText("ID inválido: " + idValue);
                            return;
                        }

                        Delito delitoSeleccionado = new DelitoDAO().buscarDelitoPorId(idDelito);

                        if (delitoSeleccionado != null) {
                            DescripDelito.setText(delitoSeleccionado.getDescripcion());
                        } else {
                            DescripDelito.setText("Delito no encontrado (ID: " + idDelito + ")");
                            new DelitoDAO().debugMostrarDelitos();
                        }
                    } catch (Exception ex) {
                        DescripDelito.setText("Error al cargar");
                        ex.printStackTrace();
                    }
                }
            }
        });

        Informacion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int filaSeleccionada = TablaPresos.getSelectedRow();
                if (filaSeleccionada == -1) {
                    JOptionPane.showMessageDialog(OficialDeRegistro.this,
                            "¡Selecciona un preso primero!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    if (TablaPresos.getColumnCount() <= 5) {
                        throw new Exception("La tabla no tiene la estructura esperada");
                    }

                    Object idValue = TablaPresos.getValueAt(filaSeleccionada, 5);
                    if (idValue == null || idValue.toString().trim().isEmpty()) {
                        throw new Exception("La identificación está vacía o no es válida");
                    }

                    String identificacion = idValue.toString();
                    Preso preso = new PresoDAO().buscarPresoPorIdentificacion(identificacion);

                    if (preso == null) {
                        JOptionPane.showMessageDialog(OficialDeRegistro.this,
                                "No se encontró el preso con identificación: " + identificacion,
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    nombre.setText(preso.getNombresCompletos());
                    apellido.setText(preso.getApellidosCompletos());
                    edad1.setText(String.valueOf(preso.getEdad()));
                    nacionali.setText(preso.getNacionalidad());
                    sexo.setText(preso.getSexo());
                    identi.setText(preso.getIdentificacion());
                    estatura.setText(String.valueOf(preso.getEstatura()));
                    peso.setText(String.valueOf(preso.getPeso()));
                    sangre.setText(preso.getGrupoSanguineo());

                    ImageIcon icon = new ImageIcon(preso.getFotoPath());
                    Image img = icon.getImage().getScaledInstance(
                            ImagenPresoInformacion.getWidth(),
                            ImagenPresoInformacion.getHeight(),
                            Image.SCALE_SMOOTH
                    );
                    ImagenPresoInformacion.setIcon(new ImageIcon(img));

                    OficialDeRegistroView.setSelectedIndex(3);

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(OficialDeRegistro.this,
                            "Error al cargar información general: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

    }

    private void cargarExpedienteCompleto(Preso preso) {
        RegistroNum.setText(preso.getExpediente().getNumeroRegistro());
        CodExpe.setText(preso.getExpediente().getCodigoExpediente());
        FechaAper.setText(preso.getExpediente().getFechaApertura().toString());
        Estado.setText(preso.getExpediente().getEstado());
        Juzgado.setText(preso.getExpediente().getJuzgado());
        nivelRiesgExp.setText(preso.getNivelDeRiesgo());

        nom.setText(preso.getNombresCompletos());
        ape.setText(preso.getApellidosCompletos());
        edad.setText(String.valueOf(preso.getEdad()));
        identi.setText(preso.getIdentificacion());
        naciona.setText(preso.getNacionalidad());

        ImageIcon icon = new ImageIcon(preso.getFotoPath());
        Image img = icon.getImage().getScaledInstance(
                fotoPresoExpediente.getWidth(),
                fotoPresoExpediente.getHeight(),
                Image.SCALE_SMOOTH
        );
        fotoPresoExpediente.setIcon(new ImageIcon(img));

        DefaultTableModel model = (DefaultTableModel) tablaExpediente.getModel();
        model.setRowCount(0);

        for (Delito delito : preso.getExpediente().getDelitos()) {
            model.addRow(new Object[]{
                delito.getNombre(),
                delito.getId(),
                preso.getSentencia().getFechaIngreso(),
                preso.getSentenciaFormateada(),
                delito.getGravedad(),
                delito.getFechaComision(),
                preso.getSentencia().getFechaSalidaCalculada()
            });
        }
    }

    private void calcularFechaSalida() {
        try {
            Object valorAños = spinnerAñosSentencia.getValue();
            Object valorMeses = spinnerMesesSentencia.getValue();

            Integer años = valorAños != null ? (Integer) valorAños : 0;
            Integer meses = valorMeses != null ? (Integer) valorMeses : 0;

            Sentencia sentencia = null;

            if (datePickerFechaIngreso.getDate() != null && (años > 0 || meses > 0)) {
                LocalDate fechaIngreso = datePickerFechaIngreso.getDate().toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDate();

                sentencia = new Sentencia(años, meses, fechaIngreso);
                lblFechaSalidaCalculada.setText(
                        sentencia.getFechaSalidaCalculada().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                );
                Siguiente2.setEnabled(true);
            } else {
                lblFechaSalidaCalculada.setText("Seleccione una fecha y duración válida.");
            }
        } catch (Exception ex) {
            lblFechaSalidaCalculada.setText("Fecha salida: Error");
            ex.printStackTrace();
        }
    }

    private void cargarDatosPresoEnFormularioActualizacion(Preso preso) {
        this.presoOriginal = preso;
        this.delitosTemporales = new ArrayList<>();

        nuevoPrimerNombreField.setText("");
        nuevoPrimerApellidoField.setText("");
        nuevaEdadField.setText("");
        nuevoSegundoApellidoField.setText("");
        nuevaEstaturaField.setText("");
        nuevoPesoField.setText("");
        nuevaNacionalidadField.setText("");
        nuevoGrupoSanguineoCombo.setSelectedIndex(0);

        nuevoAño.setValue(0);
        nuevoMes.setValue(0);
        nuevaSeccionCombo.setSelectedIndex(0);
        nuevoNivelSeguridadCombo.setSelectedIndex(0);
        nuevoAislamientoCombo.setSelectedIndex(0);
        nuevoNivelRiesgoCombo.setSelectedIndex(0);

        cod.setText("");
        art.setText("");
        nuevoGrupoSanguineoCombo.setSelectedIndex(0);
        textAreaDescripcion1.setText("");

        spinnerAñosSentencia.setValue(preso.getSentencia().getAños());
        spinnerMesesSentencia.setValue(preso.getSentencia().getMeses());

        LocalDate fechaIngreso = preso.getSentencia().getFechaIngreso();

        Date fechaConvertida = Date.from(fechaIngreso.atStartOfDay(ZoneId.systemDefault()).toInstant());

        datePickerFechaIngreso.setDate(fechaConvertida);
        calcularFechaSalida();

        nuevaFoto.setIcon(null);
        selectedImageFile = null;
    }

    private void cargarDelitosConCodigoYArticulo() {
        datosDelitos = new HashMap<>();

        datosDelitos.put("Injuria", new String[]{"220", "Artículo 220"});
        datosDelitos.put("Calumnia", new String[]{"221", "Artículo 221"});
        datosDelitos.put("Daño en bien ajeno", new String[]{"265", "Artículo 265"});
        datosDelitos.put("Violación de habitación ajena", new String[]{"189", "Artículo 189"});
        datosDelitos.put("Inasistencia alimentaria", new String[]{"233", "Artículo 233"});
        datosDelitos.put("Omisión de socorro", new String[]{"131", "Artículo 131"});
        datosDelitos.put("Lesiones personales leves", new String[]{"111", "Artículo 111"});
        datosDelitos.put("Falsedad en documento privado", new String[]{"289", "Artículo 289"});
        datosDelitos.put("Usurpación de derechos", new String[]{"296", "Artículo 296"});
        datosDelitos.put("Uso de documento falso", new String[]{"291", "Artículo 291"});
        datosDelitos.put("Abuso de confianza", new String[]{"249", "Artículo 249"});
        datosDelitos.put("Hurto simple", new String[]{"239", "Artículo 239"});
        datosDelitos.put("Receptación", new String[]{"447", "Artículo 447"});
        datosDelitos.put("Estafa", new String[]{"490", "Artículo 490"});
        datosDelitos.put("Violación de cerraduras o sellos", new String[]{"189", "Artículo 189"});
        datosDelitos.put("Fraude", new String[]{"246", "Artículo 246 - Fraude"});
        datosDelitos.put("Violación de medidas sanitarias", new String[]{"368", "Artículo 368"});
        datosDelitos.put("Invasión de tierras o edificaciones", new String[]{"263", "Artículo 263"});
        datosDelitos.put("Suplantación de identidad", new String[]{"296", "Artículo 296"});
        datosDelitos.put("Contrabando", new String[]{"319", "Artículo 319 - Contrabando"});
        datosDelitos.put("Hurto calificado", new String[]{"240", "Artículo 240"});
        datosDelitos.put("Lesiones personales graves", new String[]{"111", "Artículo 111"});
        datosDelitos.put("Extorsión", new String[]{"244", "Artículo 244 - Extorsión"});
        datosDelitos.put("Falsedad en documento público", new String[]{"287", "Artículo 287"});
        datosDelitos.put("Lavado de activos", new String[]{"323", "Artículo 323"});
        datosDelitos.put("Peculado por uso", new String[]{"399", "Artículo 399"});
        datosDelitos.put("Violencia intrafamiliar", new String[]{"229", "Artículo 229"});
        datosDelitos.put("Acoso sexual", new String[]{"210", "Artículo 210"});
        datosDelitos.put("Acceso abusivo a sistema informático", new String[]{"269", "Artículo 269"});
        datosDelitos.put("Suplantación en medios electrónicos", new String[]{"296", "Artículo 296"});
        datosDelitos.put("Daño informático", new String[]{"269", "Artículo 269 - Daño informático"});
        datosDelitos.put("Tráfico de influencias", new String[]{"411", "Artículo 411"});
        datosDelitos.put("Porte ilegal de armas", new String[]{"365", "Artículo 365"});
        datosDelitos.put("Cohecho", new String[]{"405", "Artículo 405"});
        datosDelitos.put("Concusión", new String[]{"404", "Artículo 404"});
        datosDelitos.put("Prevaricato", new String[]{"413", "Artículo 413 - Penal"});
        datosDelitos.put("Abuso de autoridad", new String[]{"416", "Artículo 416 - Penal"});
        datosDelitos.put("Perturbación del orden público", new String[]{"353", "Artículo 353"});
        datosDelitos.put("Enriquecimiento ilícito", new String[]{"412", "Artículo 412"});
        datosDelitos.put("Tráfico de fauna o flora silvestre", new String[]{"328", "Artículo 328"});
        datosDelitos.put("Minería ilegal", new String[]{"338", "Artículo 338"});
        datosDelitos.put("Hurto agravado", new String[]{"240", "Artículo 240"});
        datosDelitos.put("Homicidio culposo", new String[]{"109", "Artículo 109"});
        datosDelitos.put("Acceso carnal abusivo con menor de 14 años", new String[]{"208", "Artículo 208"});
        datosDelitos.put("Actos sexuales con menor de 14 años", new String[]{"209", "Artículo 209"});
        datosDelitos.put("Acceso carnal violento", new String[]{"205", "Artículo 205"});
        datosDelitos.put("Acto sexual violento", new String[]{"206", "Artículo 206"});
        datosDelitos.put("Violación", new String[]{"205", "Artículo 205 - Violación"});
        datosDelitos.put("Secuestro simple", new String[]{"168", "Artículo 168"});
        datosDelitos.put("Tráfico de estupefacientes", new String[]{"376", "Artículo 376"});
        datosDelitos.put("Fabricación o porte de estupefacientes", new String[]{"376", "Artículo 376"});
        datosDelitos.put("Concierto para delinquir", new String[]{"340", "Artículo 340"});
        datosDelitos.put("Homicidio", new String[]{"103", "Artículo 103"});
        datosDelitos.put("Homicidio agravado", new String[]{"104", "Artículo 104"});
        datosDelitos.put("Tortura", new String[]{"178", "Artículo 178"});
        datosDelitos.put("Desaparición forzada", new String[]{"165", "Artículo 165"});
        datosDelitos.put("Terrorismo", new String[]{"343", "Artículo 343"});
        datosDelitos.put("Rebelión", new String[]{"467", "Artículo 467"});
        datosDelitos.put("Genocidio", new String[]{"101", "Artículo 101"});
        datosDelitos.put("Crímenes de lesa humanidad", new String[]{"7", "Artículo 7"});

        for (String delito : datosDelitos.keySet()) {
            NuevosDelitosNombre1.addItem(delito);
            this.delito.addItem(delito);
        }

        NuevosDelitosNombre1.addActionListener(e -> {
            String delitoSeleccionado = NuevosDelitosNombre1.getSelectedItem().toString();
            String[] datos = datosDelitos.get(delitoSeleccionado);
            if (datos != null) {
                cod.setText(datos[0]); 
                art.setText(datos[1]); 
            } else {
                cod.setText("");
                art.setText("");
            }
        });

        delito.addActionListener(e -> {
            String delitoSeleccionado = delito.getSelectedItem().toString();
            String[] datos = datosDelitos.get(delitoSeleccionado);
            if (datos != null) {
                Codigo.setText(datos[0]); 
                ArticuloLey.setText(datos[1]); 
            } else {
                Codigo.setText("");
                ArticuloLey.setText("");
            }
        });
    }
    
    private void agregarDelitoTemporalAñadir() {
        try {
            if (Codigo.getText().trim().isEmpty()
                    || ArticuloLey.getText().trim().isEmpty()
                    || FechaComision.getDate() == null) {
                JOptionPane.showMessageDialog(this,
                        "Código, Artículo de Ley y Fecha son obligatorios",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int codigo = Integer.parseInt(Codigo.getText().trim());
            LocalDate fecha = FechaComision.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            String articulo = ArticuloLey.getText().trim();
            String gravedad = Gravedad.getSelectedItem().toString();
            String descripcion = DescripcionDelito.getText().trim();
            String nombreDelito = delito.getSelectedItem().toString();

            // Crear delito con ID temporal (negativo)
            Delito nuevoDelito = new Delito(
                    0,
                    codigo,
                    nombreDelito,
                    articulo,
                    gravedad,
                    descripcion,
                    fecha
            );

            // Guardar delito en JSON para obtener un ID real
            DelitoDAO dao = new DelitoDAO();
            int idAsignado = dao.guardarDelito(nuevoDelito);
            nuevoDelito.setId(idAsignado);

            // Agregar a la lista temporal con ID real
            delitosTemporales.add(nuevoDelito);

            // Actualizar interfaz
            SwingUtilities.invokeLater(() -> {
                actualizarProgreso();
                actualizarEstadoBotonDelito();
                actualizarEstadoBoton();
            });

            // Limpiar campos
            Codigo.setText("");
            ArticuloLey.setText("");
            FechaComision.setDate(null);
            Gravedad.setSelectedIndex(0);
            DescripcionDelito.setText("");

            JOptionPane.showMessageDialog(this,
                    "Delito agregado (Total: " + delitosTemporales.size() + ")",
                    "Información", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El código debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error inesperado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void actualizarEstadoBoton() {
        boolean camposPersonalesLlenos = !InputNombrePreso.getText().trim().isEmpty()
                && !InputApellidoPreso.getText().trim().isEmpty()
                && !InputEdadPreso.getText().trim().isEmpty()
                && !InputIdentificacionPreso.getText().trim().isEmpty()
                && !InputSexoPreso.getSelectedItem().toString().equals("Seleccione")
                && !InputNacionalidadPreso.getText().trim().isEmpty()
                && !InputEstaturaPreso.getText().trim().isEmpty()
                && !InputPesoPreso.getText().trim().isEmpty()
                && !TipoSangreCombobox.getSelectedItem().toString().equals("Seleccione")
                && lblFoto.getIcon() != null;

        boolean camposJudicialesLlenos = !seccion.getSelectedItem().toString().equals("<Seleccionar>")
                && !seguridad.getSelectedItem().toString().equals("<Seleccionar>")
                && !riesgo.getSelectedItem().toString().equals("<Seleccionar>")
                && !condicionComb.getSelectedItem().toString().equals("<Seleccione>")
                && datePickerFechaIngreso.getDate() != null
                && ((Integer) spinnerAñosSentencia.getValue() > 0 || (Integer) spinnerMesesSentencia.getValue() > 0);

        boolean delitosCompletos = !delitosTemporales.isEmpty()
                && delitosTemporales.size() >= (cantidadDelitos.getSelectedIndex() > 0
                ? Integer.parseInt(cantidadDelitos.getSelectedItem().toString()) : 1);

        AñadirPreso.setEnabled(camposPersonalesLlenos && camposJudicialesLlenos && delitosCompletos);
    }

    private void agregarValidacionInstantanea() {
        Runnable actualizarEstado = () -> {
            SwingUtilities.invokeLater(() -> {
                switch (TabbedAñadirInformacionGeneral.getSelectedIndex()) {
                    case 0 ->
                        actualizarEstadoBotonDatosPersonales();
                    case 1 ->
                        actualizarEstadoBotonInformacionJudicial();
                    case 2 ->
                        actualizarEstadoBotonDelito();
                }
            });
        };

        KeyListener keyListener = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                actualizarEstado.run();
            }
        };

        Component[] camposTexto = {
            InputNombrePreso, InputApellidoPreso, InputEdadPreso,
            InputIdentificacionPreso, InputNacionalidadPreso,
            InputEstaturaPreso, InputPesoPreso, Codigo,
            ArticuloLey, DescripcionDelito
        };

        for (Component campo : camposTexto) {
            if (campo instanceof JTextComponent) {
                ((JTextComponent) campo).addKeyListener(keyListener);
            }
        }

        PropertyChangeListener propertyListener = evt -> actualizarEstado.run();

        spinnerAñosSentencia.addPropertyChangeListener("value", propertyListener);
        spinnerMesesSentencia.addPropertyChangeListener("value", propertyListener);
        FechaComision.getDateEditor().addPropertyChangeListener("date", propertyListener);
        datePickerFechaIngreso.addPropertyChangeListener("date", e -> {
            calcularFechaSalida();
            actualizarEstado.run();
        });

        ActionListener comboListener = e -> actualizarEstado.run();

        Component[] comboboxes = {
            TipoSangreCombobox, InputSexoPreso, riesgo,
            delito, seguridad, seccion, condicionComb, Gravedad,
            cantidadDelitos
        };

        for (Component combo : comboboxes) {
            if (combo instanceof JComboBox) {
                ((JComboBox<?>) combo).addActionListener(comboListener);
            }
        }

        spinnerAñosSentencia.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                calcularFechaSalida();
                actualizarEstado.run();
            }
        });

        spinnerAñosSentencia.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                calcularFechaSalida();
                actualizarEstado.run();
            }
        });
    }

    private void actualizarEstadoBotonDatosPersonales() {
        Siguiente1.setEnabled(validarDatosPersonales());
    }

    private void actualizarEstadoBotonInformacionJudicial() {
        Siguiente2.setEnabled(validarDatosJudiciales());
    }

    private void actualizarEstadoBotonDelito() {

        boolean camposValidos = validarDatosDelito();
        boolean delitosCompletos = validarCantidadDelitos();

        AñadirPreso.setEnabled(camposValidos && delitosCompletos);

        if (AñadirPreso.isEnabled()) {
            AñadirPreso.setBackground(new Color(46, 125, 50));
            AñadirPreso.setForeground(Color.WHITE);
        } else {
            AñadirPreso.setBackground(null);
            AñadirPreso.setForeground(null);
        }
    }

    private boolean validarCantidadDelitos() {
        try {
            if (cantidadDelitos.getSelectedIndex() <= 0) {
                return false;
            }

            int requeridos = Integer.parseInt(cantidadDelitos.getSelectedItem().toString());
            return delitosTemporales.size() >= requeridos;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean validarDatosPersonales() {
        if (InputNombrePreso.getText().trim().isEmpty()
                || InputSegundoNombre.getText().trim().isEmpty()
                || InputApellidoPreso.getText().trim().isEmpty()
                || InputSegundoApellido.getText().trim().isEmpty()
                || InputEdadPreso.getText().trim().isEmpty()
                || InputIdentificacionPreso.getText().trim().isEmpty()
                || InputSexoPreso.getSelectedItem().toString().equals("Seleccione")
                || InputNacionalidadPreso.getText().trim().isEmpty()
                || InputEstaturaPreso.getText().trim().isEmpty()
                || InputPesoPreso.getText().trim().isEmpty()
                || TipoSangreCombobox.getSelectedItem().toString().equals("Seleccione")
                || lblFoto.getIcon() == null) {
            return false;
        }

        try {
            Integer.parseInt(InputEdadPreso.getText().trim());
            Float.parseFloat(InputEstaturaPreso.getText().trim());
            Float.parseFloat(InputPesoPreso.getText().trim());
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private boolean validarDatosJudiciales() {
        if (seccion.getSelectedIndex() == 0
                || seguridad.getSelectedIndex() == 0
                || riesgo.getSelectedIndex() == 0
                || condicionComb.getSelectedIndex() == 0) {
            return false;
        }

        if (datePickerFechaIngreso.getDate() == null) {
            return false;
        }

        Object valorAños = spinnerAñosSentencia.getValue();
        Object valorMeses = spinnerMesesSentencia.getValue();

        Integer años = valorAños != null ? (Integer) valorAños : 0;
        Integer meses = valorMeses != null ? (Integer) valorMeses : 0;

        if (años < 0 || meses < 0 || meses > 11) {
            return false;
        }

        if (años == 0 && meses == 0) {
            return false;
        }

        return true;
    }

    private boolean validarDatosDelito() {
        if (!delitosTemporales.isEmpty()) {
            return true;
        }

        return !delito.getSelectedItem().toString().equals("<Seleccionar>")
                && !Codigo.getText().trim().isEmpty()
                && !ArticuloLey.getText().trim().isEmpty()
                && !Gravedad.getSelectedItem().toString().equals("<Seleccionar>")
                && FechaComision.getDate() != null
                && !DescripcionDelito.getText().trim().isEmpty();
    }

    private boolean validarFecha(String fechaStr) {
        try {
            LocalDate fecha = LocalDate.parse(fechaStr);

            if (fecha.getMonthValue() > 12 || fecha.getDayOfMonth() > 31) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean validarNumero(String numeroStr, boolean esEntero) {
        try {
            if (esEntero) {
                Integer.parseInt(numeroStr);
            } else {
                Float.parseFloat(numeroStr);
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void configurarTablaImagenes() {
        TablaPresos.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
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

        TablaPresos.setRowHeight(65);
        TablaPresos.getColumnModel().getColumn(0).setPreferredWidth(70);
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

    private ImageIcon cargarImagenPreso(String path) {
        try {
            Image img = ImageIO.read(new File(path));
            return new ImageIcon(img.getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        } catch (Exception e) {
            return new ImageIcon(getClass().getResource("/images/default_profile.png"));
        }
    }

    private void actualizarProgreso() {
        int totalDelitos = 1;

        try {
            if (cantidadDelitos.getSelectedIndex() > 0) {
                totalDelitos = Integer.parseInt(cantidadDelitos.getSelectedItem().toString());
            }
        } catch (NumberFormatException e) {
            System.err.println("Error al parsear cantidad de delitos");
        }

        int siguienteDelito = delitosTemporales.size() + 1;

        lblProgreso.setText("Delito " + siguienteDelito + " de " + totalDelitos);

        if (siguienteDelito > totalDelitos) {
            lblProgreso.setText("¡Todos los delitos completados! (" + totalDelitos + ")");
        }
    }

    private void finalizarPresoActionPerformed() {
        try {
            if (delitosTemporales.size() < totalDelitos) {
                JOptionPane.showMessageDialog(this,
                        "Faltan delitos por ingresar. Debe ingresar " + totalDelitos + " delitos.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!validarDatosPersonales() || !validarDatosJudiciales()) {
                JOptionPane.showMessageDialog(this,
                        "Debe completar correctamente todos los datos personales y judiciales.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (lblFoto.getIcon() == null) {
                JOptionPane.showMessageDialog(this,
                        "Debe seleccionar una foto del preso",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Preso preso = crearPresoDesdeFormulario();
            boolean guardado = new PresoDAO().guardarPreso(preso, selectedImageFile);

            if (guardado) {
                JOptionPane.showMessageDialog(this, "Preso añadido correctamente con " + delitosTemporales.size() + " delitos.");
                limpiarFormularioCompleto();

                cargarDatosEnTabla();

                OficialDeRegistroView.setSelectedIndex(0);

                revalidate();
                repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar el preso.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            manejarError(e);
        }
    }

    private Preso crearPresoDesdeFormulario() {
        try {
            String primerNombre = InputNombrePreso.getText().trim();
            String segundoNombre = InputSegundoNombre.getText().trim();
            String primerApellido = InputApellidoPreso.getText().trim();
            String segundoApellido = InputSegundoApellido.getText().trim();
            int edad = Integer.parseInt(InputEdadPreso.getText().trim());
            String nacionalidad = InputNacionalidadPreso.getText().trim();
            String sexo = InputSexoPreso.getSelectedItem().toString();
            float estatura = Float.parseFloat(InputEstaturaPreso.getText().trim());
            float peso = Float.parseFloat(InputPesoPreso.getText().trim());
            String tipoSangre = TipoSangreCombobox.getSelectedItem().toString();
            String identificacion = InputIdentificacionPreso.getText().trim();
            String condicion = condicionComb.getSelectedItem().toString();
            String seccionAsignada = seccion.getSelectedItem().toString();
            String nivelDeRiesgo = riesgo.getSelectedItem().toString();
            String nivelDeSeguridad = seguridad.getSelectedItem().toString();

            int años = (Integer) spinnerAñosSentencia.getValue();
            int meses = (Integer) spinnerMesesSentencia.getValue();

            Date fechaSeleccionada = datePickerFechaIngreso.getDate();
            if (fechaSeleccionada == null) {
                JOptionPane.showMessageDialog(null, "Debe seleccionar una fecha de ingreso");
                return null;
            }

            LocalDate fechaIngreso = fechaSeleccionada.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            Sentencia sentencia = new Sentencia(años, meses, fechaIngreso);

            Celda celdaAsignada = new CeldaDAO().asignarCeldaDisponible(seccionAsignada);
            if (celdaAsignada == null) {
                JOptionPane.showMessageDialog(null, "No hay celdas disponibles en la sección " + seccionAsignada);
                return null;
            }

            Preso preso = new Preso(
                    primerNombre,
                    segundoNombre,
                    primerApellido,
                    segundoApellido,
                    edad,
                    sexo,
                    nacionalidad,
                    identificacion,
                    estatura,
                    peso,
                    new ArrayList<>(delitosTemporales),
                    sentencia,
                    nivelDeSeguridad,
                    seccionAsignada,
                    condicion,
                    celdaAsignada.getNombreFormateado(),
                    false,
                    nivelDeRiesgo,
                    0,
                    tipoSangre,
                    null,
                    0
            );

            return preso;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error en formato numérico: " + e.getMessage());
            return null;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al crear preso: " + e.getMessage());
            return null;
        }
    }

    private void limpiarCamposDelito() {
        delito.setSelectedIndex(0);
        Codigo.setText("");
        ArticuloLey.setText("");
        Gravedad.setSelectedIndex(0);
        FechaComision.setDate(null);
        DescripcionDelito.setText("");
    }

    private void limpiarCamposPersonales() {
        InputNombrePreso.setText("");
        InputSegundoNombre.setText("");
        InputSegundoApellido.setText("");
        InputApellidoPreso.setText("");
        InputEdadPreso.setText("");
        InputIdentificacionPreso.setText("");
        InputSexoPreso.setSelectedItem(0);
        InputNacionalidadPreso.setText("");
        InputEstaturaPreso.setText("");
        InputPesoPreso.setText("");
        TipoSangreCombobox.setSelectedItem(0);
        lblFoto.setIcon(null);
    }

    private void limpiarCamposActualizacion() {

        nuevoPrimerNombreField.setText("");
        nuevoSegundoNombreField.setText("");
        nuevoPrimerApellidoField.setText("");
        nuevoSegundoApellidoField.setText("");
        nuevaEdadField.setText("");
        nuevaEstaturaField.setText("");
        nuevoPesoField.setText("");
        nuevaNacionalidadField.setText("");
        nuevoGrupoSanguineoCombo.setSelectedItem(0);
        nuevoAño.setValue(0);
        nuevoMes.setValue(0);
        nuevaSeccionCombo.setSelectedItem(0);
        nuevoNivelSeguridadCombo.setSelectedItem(0);
        nuevoAislamientoCombo.setSelectedItem(0);
        nuevoNivelRiesgoCombo.setSelectedItem(0);
        nuevaFoto = null;

    }

    private void limpiarCamposJudiciales() {
        seccion.setSelectedIndex(0);
        seguridad.setSelectedIndex(0);
        riesgo.setSelectedIndex(0);
        condicionComb.setSelectedIndex(0);

        datePickerFechaIngreso.setDate(null);

        lblFechaSalidaCalculada.setText("");

        spinnerAñosSentencia.setValue(0);
        spinnerMesesSentencia.setValue(0);
    }

    private void limpiarFormularioCompleto() {
        limpiarCamposDelito();
        limpiarCamposPersonales();
        limpiarCamposJudiciales();
        delitosTemporales.clear();
        delitoActual = 1;
        totalDelitos = 1;
        lblProgreso.setText("Delito 1 de 1");
    }

    private void manejarError(Exception e) {
        JOptionPane.showMessageDialog(this,
                "Error inesperado: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }

    private void cargarDatosEnTabla() {
        DefaultTableModel modelo = (DefaultTableModel) TablaPresos.getModel();
        modelo.setRowCount(0);

        PresoDAO presoDAO = new PresoDAO();
        List<Preso> presos = presoDAO.cargarTodos();

        for (Preso preso : presos) {
            ImageIcon foto = null;
            if (preso.getFotoPath() != null && !preso.getFotoPath().isEmpty()) {
                foto = cargarImagenPreso(preso.getFotoPath());
            } else {
                foto = new ImageIcon(getClass().getResource("/images/default_profile.png"));
            }

            modelo.addRow(new Object[]{
                foto,
                preso.getId(),
                preso.getNombresCompletos(),
                preso.getApellidosCompletos(),
                preso.getEdad(),
                preso.getIdentificacion(),
                preso.getNacionalidad(),
                preso.getSeccionAsignada(),
                preso.getCeldaAsignada()

            });
        }

        TablaPresos.revalidate();
        TablaPresos.repaint();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel22 = new javax.swing.JPanel();
        ppMenuTablaPresos = new javax.swing.JPopupMenu();
        PRINCIPAL = new javax.swing.JPanel();
        PanelFondoTextoPrincipal = new javax.swing.JPanel();
        PanelPerfilTitulo = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        PanelPresosTitulo = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        PanelAñadirPresoTitulo = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        OficialDeRegistroView = new javax.swing.JTabbedPane();
        PanelAñadirPresoBase = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        TabbedAñadirInformacionGeneral = new javax.swing.JTabbedPane();
        InformacionGeneralPanel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        InputSexoPreso = new javax.swing.JComboBox<>();
        TipoSangreCombobox = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();
        jSeparator7 = new javax.swing.JSeparator();
        jSeparator8 = new javax.swing.JSeparator();
        Siguiente1 = new javax.swing.JButton();
        InputApellidoPreso = new javax.swing.JTextField();
        InputEdadPreso = new javax.swing.JTextField();
        InputIdentificacionPreso = new javax.swing.JTextField();
        InputNacionalidadPreso = new javax.swing.JTextField();
        InputEstaturaPreso = new javax.swing.JTextField();
        InputPesoPreso = new javax.swing.JTextField();
        InputNombrePreso = new javax.swing.JTextField();
        btnIngresarFoto = new javax.swing.JButton();
        jSeparator27 = new javax.swing.JSeparator();
        jSeparator84 = new javax.swing.JSeparator();
        jLabel110 = new javax.swing.JLabel();
        jLabel111 = new javax.swing.JLabel();
        InputSegundoApellido = new javax.swing.JTextField();
        InputSegundoNombre = new javax.swing.JTextField();
        lblFoto = new javax.swing.JLabel();
        CancelarD3 = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        InformacionJudicialPanel = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jSeparator9 = new javax.swing.JSeparator();
        jSeparator10 = new javax.swing.JSeparator();
        jSeparator11 = new javax.swing.JSeparator();
        jSeparator12 = new javax.swing.JSeparator();
        riesgo = new javax.swing.JComboBox<>();
        seguridad = new javax.swing.JComboBox<>();
        condicionComb = new javax.swing.JComboBox<>();
        seccion = new javax.swing.JComboBox<>();
        jLabel19 = new javax.swing.JLabel();
        jSeparator13 = new javax.swing.JSeparator();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        spinnerAñosSentencia = new com.toedter.components.JSpinField();
        spinnerMesesSentencia = new com.toedter.components.JSpinField();
        jLabel22 = new javax.swing.JLabel();
        jSeparator14 = new javax.swing.JSeparator();
        datePickerFechaIngreso = new com.toedter.calendar.JDateChooser();
        jLabel23 = new javax.swing.JLabel();
        jSeparator15 = new javax.swing.JSeparator();
        lblFechaSalidaCalculada = new javax.swing.JLabel();
        Siguiente2 = new javax.swing.JButton();
        cancelarD2 = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        PanelIngresarDelito = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jSeparator16 = new javax.swing.JSeparator();
        jSeparator17 = new javax.swing.JSeparator();
        jSeparator18 = new javax.swing.JSeparator();
        delito = new javax.swing.JComboBox<>();
        Gravedad = new javax.swing.JComboBox<>();
        cantidadDelitos = new javax.swing.JComboBox<>();
        jLabel28 = new javax.swing.JLabel();
        jSeparator20 = new javax.swing.JSeparator();
        jSeparator21 = new javax.swing.JSeparator();
        FechaComision = new com.toedter.calendar.JDateChooser();
        jLabel32 = new javax.swing.JLabel();
        Codigo = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        DescripcionDelito = new javax.swing.JTextArea();
        jLabel33 = new javax.swing.JLabel();
        guardarDelito = new javax.swing.JButton();
        jLabel105 = new javax.swing.JLabel();
        jSeparator66 = new javax.swing.JSeparator();
        ArticuloLey = new javax.swing.JLabel();
        lblProgreso = new javax.swing.JLabel();
        cancelarD = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        AñadirPreso = new javax.swing.JButton();
        ActualizarInformacionPreso = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jSeparator19 = new javax.swing.JSeparator();
        jSeparator24 = new javax.swing.JSeparator();
        jSeparator25 = new javax.swing.JSeparator();
        jSeparator26 = new javax.swing.JSeparator();
        jSeparator28 = new javax.swing.JSeparator();
        nuevoGrupoSanguineoCombo = new javax.swing.JComboBox<>();
        jLabel91 = new javax.swing.JLabel();
        jSeparator61 = new javax.swing.JSeparator();
        jLabel92 = new javax.swing.JLabel();
        jLabel81 = new javax.swing.JLabel();
        jLabel72 = new javax.swing.JLabel();
        jSeparator56 = new javax.swing.JSeparator();
        jLabel88 = new javax.swing.JLabel();
        jSeparator67 = new javax.swing.JSeparator();
        jSeparator59 = new javax.swing.JSeparator();
        jSeparator68 = new javax.swing.JSeparator();
        nuevoNivelSeguridadCombo = new javax.swing.JComboBox<>();
        nuevoAislamientoCombo = new javax.swing.JComboBox<>();
        nuevoNivelRiesgoCombo = new javax.swing.JComboBox<>();
        nuevaSeccionCombo = new javax.swing.JComboBox<>();
        nuevoAño = new com.toedter.components.JSpinField();
        nuevoMes = new com.toedter.components.JSpinField();
        ActualizarFotoBoton = new javax.swing.JButton();
        nuevaFoto = new javax.swing.JLabel();
        jLabel74 = new javax.swing.JLabel();
        jLabel79 = new javax.swing.JLabel();
        jSeparator29 = new javax.swing.JSeparator();
        jPanel17 = new javax.swing.JPanel();
        actualizarPreso = new javax.swing.JButton();
        nuevoPrimerNombreField = new javax.swing.JTextField();
        nuevoPrimerApellidoField = new javax.swing.JTextField();
        nuevaEdadField = new javax.swing.JTextField();
        nuevoSegundoApellidoField = new javax.swing.JTextField();
        nuevaEstaturaField = new javax.swing.JTextField();
        nuevoPesoField = new javax.swing.JTextField();
        nuevaNacionalidadField = new javax.swing.JTextField();
        jSeparator81 = new javax.swing.JSeparator();
        jLabel73 = new javax.swing.JLabel();
        jSeparator82 = new javax.swing.JSeparator();
        nuevoSegundoNombreField = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        Perfil = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jLabel45 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jSeparator34 = new javax.swing.JSeparator();
        jSeparator36 = new javax.swing.JSeparator();
        jSeparator37 = new javax.swing.JSeparator();
        jSeparator38 = new javax.swing.JSeparator();
        jSeparator39 = new javax.swing.JSeparator();
        jSeparator40 = new javax.swing.JSeparator();
        ApellidoODR = new javax.swing.JLabel();
        IdentificacionODR = new javax.swing.JLabel();
        EdadODR = new javax.swing.JLabel();
        SexoODR = new javax.swing.JLabel();
        NacionalidadODR = new javax.swing.JLabel();
        nombreODR = new javax.swing.JLabel();
        jPanel18 = new RoundedPanel(30);
        jPanel20 = new RoundedPanel(26);
        LabelFotoOficialDeRegistro = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        LabelRango = new javax.swing.JLabel();
        jSeparator33 = new javax.swing.JSeparator();
        jLabel46 = new javax.swing.JLabel();
        LabelNumeroPlaca = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        LabelTurno = new javax.swing.JLabel();
        jSeparator35 = new javax.swing.JSeparator();
        jLabel49 = new javax.swing.JLabel();
        jSeparator41 = new javax.swing.JSeparator();
        cerrarSesionODR = new javax.swing.JButton();
        jLabel48 = new javax.swing.JLabel();
        btnActualizarInfoODR = new javax.swing.JButton();
        Expediente = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablaExpediente = new javax.swing.JTable();
        jPanel23 = new RoundedPanel(32)
        ;
        jLabel56 = new javax.swing.JLabel();
        jSeparator43 = new javax.swing.JSeparator();
        jLabel57 = new javax.swing.JLabel();
        FechaAper = new javax.swing.JLabel();
        jSeparator44 = new javax.swing.JSeparator();
        jLabel58 = new javax.swing.JLabel();
        Estado = new javax.swing.JLabel();
        jSeparator45 = new javax.swing.JSeparator();
        jLabel59 = new javax.swing.JLabel();
        Juzgado = new javax.swing.JLabel();
        jSeparator46 = new javax.swing.JSeparator();
        RegistroNum = new javax.swing.JLabel();
        CodExpe = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        jSeparator47 = new javax.swing.JSeparator();
        nivelRiesgExp = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        jSeparator48 = new javax.swing.JSeparator();
        jLabel62 = new javax.swing.JLabel();
        jSeparator53 = new javax.swing.JSeparator();
        jPanel35 = new RoundedPanel(32)
        ;
        jLabel78 = new javax.swing.JLabel();
        fotoPresoExpediente = new javax.swing.JLabel();
        DescripDelito = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        jSeparator42 = new javax.swing.JSeparator();
        jSeparator49 = new javax.swing.JSeparator();
        jSeparator50 = new javax.swing.JSeparator();
        jSeparator51 = new javax.swing.JSeparator();
        jSeparator52 = new javax.swing.JSeparator();
        ape = new javax.swing.JLabel();
        edad = new javax.swing.JLabel();
        identi = new javax.swing.JLabel();
        naciona = new javax.swing.JLabel();
        nom = new javax.swing.JLabel();
        DatosPersonalesPreso = new javax.swing.JPanel();
        jLabel68 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        jSeparator54 = new javax.swing.JSeparator();
        jLabel70 = new javax.swing.JLabel();
        jSeparator55 = new javax.swing.JSeparator();
        jLabel71 = new javax.swing.JLabel();
        jSeparator57 = new javax.swing.JSeparator();
        jLabel75 = new javax.swing.JLabel();
        jSeparator58 = new javax.swing.JSeparator();
        jLabel76 = new javax.swing.JLabel();
        jSeparator60 = new javax.swing.JSeparator();
        jLabel77 = new javax.swing.JLabel();
        jSeparator62 = new javax.swing.JSeparator();
        jLabel87 = new javax.swing.JLabel();
        jSeparator63 = new javax.swing.JSeparator();
        jLabel89 = new javax.swing.JLabel();
        jSeparator64 = new javax.swing.JSeparator();
        jLabel90 = new javax.swing.JLabel();
        jLabel93 = new javax.swing.JLabel();
        jSeparator65 = new javax.swing.JSeparator();
        nombre = new javax.swing.JLabel();
        apellido = new javax.swing.JLabel();
        edad1 = new javax.swing.JLabel();
        sexo = new javax.swing.JLabel();
        nacionali = new javax.swing.JLabel();
        identi1 = new javax.swing.JLabel();
        estatura = new javax.swing.JLabel();
        peso = new javax.swing.JLabel();
        sangre = new javax.swing.JLabel();
        jPanel24 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        ImagenPresoInformacion = new javax.swing.JLabel();
        regresar = new javax.swing.JButton();
        PanelTablaPresoBase = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TablaPresos = new javax.swing.JTable();
        jPanel1 = new RoundedPanel(30);
        jPanel2 = new RoundedPanel(30);
        ;
        jLabel2 = new javax.swing.JLabel();
        SelectorSeccion = new javax.swing.JComboBox<>();
        btnRestaurarTabla = new javax.swing.JButton();
        identificacionB = new javax.swing.JTextField();
        btnBuscarIdentificacion = new javax.swing.JButton();
        ActualizarODR = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        jPanel28 = new javax.swing.JPanel();
        jLabel98 = new javax.swing.JLabel();
        jLabel99 = new javax.swing.JLabel();
        jSeparator69 = new javax.swing.JSeparator();
        nuevoPrimerNombre = new javax.swing.JTextField();
        jLabel100 = new javax.swing.JLabel();
        jSeparator70 = new javax.swing.JSeparator();
        primerNuevoApellido = new javax.swing.JTextField();
        nuevaEdad = new javax.swing.JTextField();
        jLabel101 = new javax.swing.JLabel();
        jSeparator71 = new javax.swing.JSeparator();
        nuevaIdenti = new javax.swing.JTextField();
        jLabel96 = new javax.swing.JLabel();
        jSeparator72 = new javax.swing.JSeparator();
        nuevaNacio = new javax.swing.JTextField();
        jLabel95 = new javax.swing.JLabel();
        jSeparator73 = new javax.swing.JSeparator();
        jLabel97 = new javax.swing.JLabel();
        jSeparator74 = new javax.swing.JSeparator();
        nuevoSexoOdr = new javax.swing.JComboBox<>();
        jLabel104 = new javax.swing.JLabel();
        jLabel102 = new javax.swing.JLabel();
        nuevoCorreo = new javax.swing.JTextField();
        jSeparator75 = new javax.swing.JSeparator();
        jLabel103 = new javax.swing.JLabel();
        jSeparator76 = new javax.swing.JSeparator();
        nuevaContra = new javax.swing.JTextField();
        LabelFOTO = new javax.swing.JLabel();
        Actualizarimagenodr = new javax.swing.JButton();
        jPanel29 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        jPanel30 = new javax.swing.JPanel();
        jPanel31 = new javax.swing.JPanel();
        jPanel32 = new javax.swing.JPanel();
        jLabel112 = new javax.swing.JLabel();
        jSeparator83 = new javax.swing.JSeparator();
        jLabel113 = new javax.swing.JLabel();
        jSeparator85 = new javax.swing.JSeparator();
        segundoNuevoApellido1 = new javax.swing.JTextField();
        nuevoSegundoNombre = new javax.swing.JTextField();
        jLabel94 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        sanciones = new javax.swing.JPanel();
        jPanel34 = new javax.swing.JPanel();
        jPanel33 = new javax.swing.JPanel();
        jLabel106 = new javax.swing.JLabel();
        jLabel107 = new javax.swing.JLabel();
        jLabel108 = new javax.swing.JLabel();
        jLabel109 = new javax.swing.JLabel();
        jSeparator77 = new javax.swing.JSeparator();
        jSeparator78 = new javax.swing.JSeparator();
        jSeparator79 = new javax.swing.JSeparator();
        jSeparator80 = new javax.swing.JSeparator();
        nivelseguridadS = new javax.swing.JLabel();
        nombreS = new javax.swing.JLabel();
        apellidoS = new javax.swing.JLabel();
        aislamientoS = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tablaSancion = new javax.swing.JTable();
        AñadirDelito = new javax.swing.JPanel();
        jPanel36 = new javax.swing.JPanel();
        jPanel37 = new javax.swing.JPanel();
        jLabel80 = new javax.swing.JLabel();
        jPanel38 = new javax.swing.JPanel();
        jLabel121 = new javax.swing.JLabel();
        jLabel122 = new javax.swing.JLabel();
        jLabel123 = new javax.swing.JLabel();
        jSeparator89 = new javax.swing.JSeparator();
        NuevosDelitosNombre1 = new javax.swing.JComboBox<>();
        grav = new javax.swing.JComboBox<>();
        jSeparator92 = new javax.swing.JSeparator();
        fechaComisionActualizar = new com.toedter.calendar.JDateChooser();
        jLabel125 = new javax.swing.JLabel();
        cod = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        textAreaDescripcion1 = new javax.swing.JTextArea();
        jLabel126 = new javax.swing.JLabel();
        jLabel127 = new javax.swing.JLabel();
        jSeparator93 = new javax.swing.JSeparator();
        art = new javax.swing.JLabel();
        lblProgreso1 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jSpinField1 = new com.toedter.components.JSpinField();
        jSpinField2 = new com.toedter.components.JSpinField();
        jLabel124 = new javax.swing.JLabel();
        finalizarDelito = new javax.swing.JButton();

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        PRINCIPAL.setBackground(new java.awt.Color(255, 255, 255));
        PRINCIPAL.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        PanelFondoTextoPrincipal.setBackground(new java.awt.Color(29, 35, 51));
        PanelFondoTextoPrincipal.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        PanelPerfilTitulo.setBackground(new java.awt.Color(29, 35, 51));
        PanelPerfilTitulo.setForeground(new java.awt.Color(255, 255, 255));
        PanelPerfilTitulo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PanelPerfilTituloMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                PanelPerfilTituloMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                PanelPerfilTituloMouseExited(evt);
            }
        });
        PanelPerfilTitulo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("PERFIL");
        PanelPerfilTitulo.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 20, 100, -1));

        PanelFondoTextoPrincipal.add(PanelPerfilTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 370, 60));

        PanelPresosTitulo.setBackground(new java.awt.Color(29, 35, 51));
        PanelPresosTitulo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PanelPresosTituloMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                PanelPresosTituloMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                PanelPresosTituloMouseExited(evt);
            }
        });
        PanelPresosTitulo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("PRESOS");
        PanelPresosTitulo.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 20, 100, -1));

        PanelFondoTextoPrincipal.add(PanelPresosTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 0, 380, 60));

        PanelAñadirPresoTitulo.setBackground(new java.awt.Color(29, 35, 51));
        PanelAñadirPresoTitulo.setForeground(new java.awt.Color(255, 255, 255));
        PanelAñadirPresoTitulo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PanelAñadirPresoTituloMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                PanelAñadirPresoTituloMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                PanelAñadirPresoTituloMouseExited(evt);
            }
        });
        PanelAñadirPresoTitulo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("AÑADIR PRESOS");
        PanelAñadirPresoTitulo.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, 130, -1));

        PanelFondoTextoPrincipal.add(PanelAñadirPresoTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 0, 350, 60));

        PRINCIPAL.add(PanelFondoTextoPrincipal, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1100, 60));

        PanelAñadirPresoBase.setBackground(new java.awt.Color(255, 255, 255));
        PanelAñadirPresoBase.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        PanelAñadirPresoBase.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -30, 1100, 70));

        InformacionGeneralPanel.setBackground(new java.awt.Color(255, 255, 255));
        InformacionGeneralPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(180, 180, 195));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(29, 35, 51));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel3.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 890, 20));

        jLabel5.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Segundo apellido:");
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 180, 120, 20));

        jLabel6.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Edad:");
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 210, 60, 30));

        jLabel7.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Identificación:");
        jPanel3.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 260, 130, 20));

        jLabel8.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("Nacionalidad");
        jPanel3.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 290, 90, 30));

        jLabel9.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Sexo");
        jPanel3.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 430, 50, 30));

        jLabel10.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setText("Segundo nombre:");
        jPanel3.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 90, 120, 30));

        jLabel11.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setText("Estatura (cm):");
        jPanel3.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 330, 130, 30));

        InputSexoPreso.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Seleccione>", "F", "M" }));
        jPanel3.add(InputSexoPreso, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 430, 120, -1));

        TipoSangreCombobox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Seleccione>", "A+", "A-", "O+", "O-", "B+", "B-", "AB+", "AB-" }));
        jPanel3.add(TipoSangreCombobox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 430, 120, -1));

        jLabel12.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 0, 0));
        jLabel12.setText("Peso (kg):");
        jPanel3.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 380, 90, 20));

        jLabel13.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 0, 0));
        jLabel13.setText("Tipo sangre: ");
        jPanel3.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 430, 100, 30));

        jSeparator2.setForeground(new java.awt.Color(0, 0, 0));
        jPanel3.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 200, 410, 20));

        jSeparator3.setForeground(new java.awt.Color(0, 0, 0));
        jPanel3.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 240, 410, 10));

        jSeparator4.setForeground(new java.awt.Color(0, 0, 0));
        jPanel3.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 280, 410, 10));

        jSeparator5.setForeground(new java.awt.Color(0, 0, 0));
        jPanel3.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 320, 410, 20));

        jSeparator6.setForeground(new java.awt.Color(0, 0, 0));
        jPanel3.add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 360, 410, 10));

        jSeparator7.setForeground(new java.awt.Color(0, 0, 0));
        jPanel3.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 400, 410, 10));

        jSeparator8.setForeground(new java.awt.Color(0, 0, 0));
        jPanel3.add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 120, 410, 10));

        Siguiente1.setBackground(new java.awt.Color(19, 65, 19));
        Siguiente1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Siguiente1.setForeground(new java.awt.Color(255, 255, 255));
        Siguiente1.setText("Siguiente");
        Siguiente1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Siguiente1ActionPerformed(evt);
            }
        });
        jPanel3.add(Siguiente1, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 400, 120, 40));

        InputApellidoPreso.setBackground(new java.awt.Color(180, 180, 195));
        InputApellidoPreso.setBorder(null);
        jPanel3.add(InputApellidoPreso, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 140, 310, 20));

        InputEdadPreso.setBackground(new java.awt.Color(180, 180, 195));
        InputEdadPreso.setBorder(null);
        jPanel3.add(InputEdadPreso, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 220, 340, 20));

        InputIdentificacionPreso.setBackground(new java.awt.Color(180, 180, 195));
        InputIdentificacionPreso.setBorder(null);
        InputIdentificacionPreso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InputIdentificacionPresoActionPerformed(evt);
            }
        });
        jPanel3.add(InputIdentificacionPreso, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 260, 320, 20));

        InputNacionalidadPreso.setBackground(new java.awt.Color(180, 180, 195));
        InputNacionalidadPreso.setBorder(null);
        InputNacionalidadPreso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InputNacionalidadPresoActionPerformed(evt);
            }
        });
        jPanel3.add(InputNacionalidadPreso, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 290, 320, 30));

        InputEstaturaPreso.setBackground(new java.awt.Color(180, 180, 195));
        InputEstaturaPreso.setBorder(null);
        jPanel3.add(InputEstaturaPreso, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 340, 320, 20));

        InputPesoPreso.setBackground(new java.awt.Color(180, 180, 195));
        InputPesoPreso.setBorder(null);
        jPanel3.add(InputPesoPreso, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 380, 330, 20));

        InputNombrePreso.setBackground(new java.awt.Color(180, 180, 195));
        InputNombrePreso.setBorder(null);
        InputNombrePreso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InputNombrePresoActionPerformed(evt);
            }
        });
        jPanel3.add(InputNombrePreso, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 50, 310, 30));

        btnIngresarFoto.setBackground(new java.awt.Color(0, 0, 51));
        btnIngresarFoto.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnIngresarFoto.setForeground(new java.awt.Color(255, 255, 255));
        btnIngresarFoto.setText("Ingresar foto");
        btnIngresarFoto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIngresarFotoActionPerformed(evt);
            }
        });
        jPanel3.add(btnIngresarFoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 80, 130, -1));

        jSeparator27.setForeground(new java.awt.Color(0, 0, 0));
        jPanel3.add(jSeparator27, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 160, 410, 10));

        jSeparator84.setForeground(new java.awt.Color(0, 0, 0));
        jPanel3.add(jSeparator84, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, 410, 10));

        jLabel110.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel110.setForeground(new java.awt.Color(0, 0, 0));
        jLabel110.setText("Primer nombre:");
        jPanel3.add(jLabel110, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 50, 100, 30));

        jLabel111.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel111.setForeground(new java.awt.Color(0, 0, 0));
        jLabel111.setText("Primer apellido:");
        jPanel3.add(jLabel111, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 140, 100, 20));

        InputSegundoApellido.setBackground(new java.awt.Color(180, 180, 195));
        InputSegundoApellido.setBorder(null);
        InputSegundoApellido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InputSegundoApellidoActionPerformed(evt);
            }
        });
        jPanel3.add(InputSegundoApellido, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 170, 260, 30));

        InputSegundoNombre.setBackground(new java.awt.Color(180, 180, 195));
        InputSegundoNombre.setBorder(null);
        jPanel3.add(InputSegundoNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 90, 260, 30));

        lblFoto.setBackground(new java.awt.Color(129, 129, 164));
        lblFoto.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));
        jPanel3.add(lblFoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 120, 190, 230));

        InformacionGeneralPanel.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 30, 890, 480));

        CancelarD3.setBackground(new java.awt.Color(66, 11, 11));
        CancelarD3.setForeground(new java.awt.Color(255, 255, 255));
        CancelarD3.setText("Cancelar");
        CancelarD3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelarD3ActionPerformed(evt);
            }
        });
        InformacionGeneralPanel.add(CancelarD3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 10, -1, -1));

        jLabel14.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(102, 0, 0));
        jLabel14.setText("Todos los campos son obligarorios*");
        InformacionGeneralPanel.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 10, 200, -1));

        TabbedAñadirInformacionGeneral.addTab("Añadir Informacion General", InformacionGeneralPanel);

        InformacionJudicialPanel.setBackground(new java.awt.Color(255, 255, 255));
        InformacionJudicialPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel7.setBackground(new java.awt.Color(180, 180, 195));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel8.setBackground(new java.awt.Color(29, 35, 51));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel7.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 30));

        jPanel9.setBackground(new java.awt.Color(29, 35, 51));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel7.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 430, 780, 10));

        jLabel15.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 0, 0));
        jLabel15.setText("Nivel de riesgo:");
        jPanel7.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 160, 130, 30));

        jLabel16.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 0, 0));
        jLabel16.setText("Nivel de seguridad:");
        jPanel7.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 230, 130, 30));

        jLabel17.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 0, 0));
        jLabel17.setText("Condición:");
        jPanel7.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 300, 130, 30));

        jLabel18.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(0, 0, 0));
        jLabel18.setText("Meses");
        jPanel7.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 60, 70, 30));

        jSeparator9.setForeground(new java.awt.Color(0, 0, 0));
        jPanel7.add(jSeparator9, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 190, 110, 20));

        jSeparator10.setForeground(new java.awt.Color(0, 0, 0));
        jPanel7.add(jSeparator10, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 260, 110, 20));

        jSeparator11.setForeground(new java.awt.Color(0, 0, 0));
        jPanel7.add(jSeparator11, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 330, 110, 20));

        jSeparator12.setForeground(new java.awt.Color(0, 0, 0));
        jPanel7.add(jSeparator12, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 270, 270, 10));

        riesgo.setBackground(new java.awt.Color(51, 51, 51));
        riesgo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Seleccionar>", "Riesgo bajo", "Riesgo medio", "Riesgo alto" }));
        jPanel7.add(riesgo, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 150, 140, 40));

        seguridad.setBackground(new java.awt.Color(51, 51, 51));
        seguridad.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Seleccionar>", "Bajo", "Medio", "Alto" }));
        jPanel7.add(seguridad, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 220, 140, 40));

        condicionComb.setBackground(new java.awt.Color(51, 51, 51));
        condicionComb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Seleccione>", "Condenado", "Condenado en traslado" }));
        jPanel7.add(condicionComb, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 290, 140, 40));

        seccion.setBackground(new java.awt.Color(51, 51, 51));
        seccion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Seleccionar>", "Sección A", "Sección B", "Sección C" }));
        jPanel7.add(seccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 80, 140, 40));

        jLabel19.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(0, 0, 0));
        jLabel19.setText("Asignar sección:");
        jPanel7.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 90, 130, 30));

        jSeparator13.setForeground(new java.awt.Color(0, 0, 0));
        jPanel7.add(jSeparator13, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 120, 110, 10));

        jLabel20.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(0, 0, 0));
        jLabel20.setText("Fecha de salida:");
        jPanel7.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 240, 110, 30));

        jLabel21.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(0, 0, 0));
        jLabel21.setText("Años");
        jPanel7.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 60, 70, 30));
        jPanel7.add(spinnerAñosSentencia, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 90, 80, -1));
        jPanel7.add(spinnerMesesSentencia, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 90, 80, -1));

        jLabel22.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(0, 0, 0));
        jLabel22.setText("Condena:");
        jPanel7.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 90, 70, 30));

        jSeparator14.setForeground(new java.awt.Color(0, 0, 0));
        jPanel7.add(jSeparator14, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 120, 270, 10));
        jPanel7.add(datePickerFechaIngreso, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 160, 150, 30));

        jLabel23.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(0, 0, 0));
        jLabel23.setText("Fecha de ingreso:");
        jPanel7.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 160, 120, 30));

        jSeparator15.setForeground(new java.awt.Color(0, 0, 0));
        jPanel7.add(jSeparator15, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 190, 270, 10));

        lblFechaSalidaCalculada.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblFechaSalidaCalculada.setForeground(new java.awt.Color(0, 0, 0));
        jPanel7.add(lblFechaSalidaCalculada, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 240, 150, 30));

        Siguiente2.setBackground(new java.awt.Color(7, 56, 7));
        Siguiente2.setText("Siguiente");
        Siguiente2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Siguiente2ActionPerformed(evt);
            }
        });
        jPanel7.add(Siguiente2, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 380, 150, 40));

        InformacionJudicialPanel.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 60, 780, 440));

        cancelarD2.setText("Cancelar");
        cancelarD2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarD2ActionPerformed(evt);
            }
        });
        InformacionJudicialPanel.add(cancelarD2, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 7, -1, 30));

        jLabel27.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(102, 0, 0));
        jLabel27.setText("Todos los campos son obligarorios*");
        InformacionJudicialPanel.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 20, 200, -1));

        TabbedAñadirInformacionGeneral.addTab("Añadir  Informacion Judicial", InformacionJudicialPanel);

        PanelIngresarDelito.setBackground(new java.awt.Color(255, 255, 255));
        PanelIngresarDelito.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel10.setBackground(new java.awt.Color(180, 180, 195));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel11.setBackground(new java.awt.Color(29, 35, 51));
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel10.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 30));

        jPanel12.setBackground(new java.awt.Color(29, 35, 51));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel10.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 430, 780, 10));

        jLabel24.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(0, 0, 0));
        jLabel24.setText("Delito:");
        jPanel10.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 160, 90, 30));

        jLabel25.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(0, 0, 0));
        jLabel25.setText("Articulo:");
        jPanel10.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 290, 100, 30));

        jLabel26.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(0, 0, 0));
        jLabel26.setText("Gravedad:");
        jPanel10.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 340, 110, 30));

        jSeparator16.setForeground(new java.awt.Color(0, 0, 0));
        jPanel10.add(jSeparator16, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 190, 110, 20));

        jSeparator17.setForeground(new java.awt.Color(0, 0, 0));
        jPanel10.add(jSeparator17, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 310, 280, 10));

        jSeparator18.setForeground(new java.awt.Color(0, 0, 0));
        jPanel10.add(jSeparator18, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 370, 110, 20));

        delito.setBackground(new java.awt.Color(51, 51, 51));
        delito.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Seleccionar>", "Injuria", "Calumnia", "Daño en bien ajeno", "Violación de habitación ajena", "Inasistencia alimentaria", "Omisión de socorro", "Lesiones personales leves", "Falsedad en documento privado", "Usurpación de derechos", "Uso de documento falso", "Abuso de confianza", "Hurto simple", "Receptación", "Estafa", "Violación de cerraduras o sellos", "Fraude", "Violación de medidas sanitarias", "Invasión de tierras o edificaciones", "Suplantación de identidad", "Contrabando", "Hurto calificado", "Lesiones personales graves", "Extorsión", "Falsedad en documento público", "Lavado de activos", "Peculado por uso", "Violencia intrafamiliar", "Acoso sexual", "Acceso abusivo a sistema informático", "Suplantación en medios electrónicos", "Daño informático", "Tráfico de influencias", "Porte ilegal de armas", "Cohecho", "Concusión", "Prevaricato", "Abuso de autoridad", "Perturbación del orden público", "Enriquecimiento ilícito", "Tráfico de fauna o flora silvestre", "Minería ilegal", "Hurto agravado", "Homicidio culposo", "Acceso carnal abusivo con menor de 14 años", "Actos sexuales con menor de 14 años", "Acceso carnal violento", "Acto sexual violento", "Violación", "Secuestro simple", "Tráfico de estupefacientes", "Fabricación o porte de estupefacientes", "Concierto para delinquir", "Homicidio", "Homicidio agravado", "Tortura", "Desaparición forzada", "Terrorismo", "Rebelión", "Genocidio", "Crímenes de lesa humanidad" }));
        jPanel10.add(delito, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 150, 150, 40));

        Gravedad.setBackground(new java.awt.Color(51, 51, 51));
        Gravedad.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Seleccionar>", "Baja", "Media", "Alta" }));
        jPanel10.add(Gravedad, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 340, 140, 40));

        cantidadDelitos.setBackground(new java.awt.Color(51, 51, 51));
        cantidadDelitos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Seleccionar>", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30" }));
        jPanel10.add(cantidadDelitos, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 80, 140, 40));

        jLabel28.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(0, 0, 0));
        jLabel28.setText("Cantidad de delitos");
        jPanel10.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 90, 130, 30));

        jSeparator20.setForeground(new java.awt.Color(0, 0, 0));
        jPanel10.add(jSeparator20, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 120, 110, 10));

        jSeparator21.setForeground(new java.awt.Color(0, 0, 0));
        jPanel10.add(jSeparator21, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 110, 270, 10));
        jPanel10.add(FechaComision, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 80, 150, 30));

        jLabel32.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(0, 0, 0));
        jLabel32.setText("Descripcion delito:");
        jPanel10.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 150, 120, 30));

        Codigo.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        Codigo.setForeground(new java.awt.Color(0, 0, 0));
        jPanel10.add(Codigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 230, 210, 30));

        DescripcionDelito.setColumns(20);
        DescripcionDelito.setRows(5);
        jScrollPane2.setViewportView(DescripcionDelito);

        jPanel10.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 180, 270, 130));

        jLabel33.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(0, 0, 0));
        jLabel33.setText("Fecha comisión:");
        jPanel10.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 90, 120, 30));

        guardarDelito.setBackground(new java.awt.Color(0, 0, 51));
        guardarDelito.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        guardarDelito.setText("Guardar delito");
        guardarDelito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarDelitoActionPerformed(evt);
            }
        });
        jPanel10.add(guardarDelito, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 330, 140, -1));

        jLabel105.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel105.setForeground(new java.awt.Color(0, 0, 0));
        jLabel105.setText("Codigo:");
        jPanel10.add(jLabel105, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 230, 100, 30));

        jSeparator66.setForeground(new java.awt.Color(0, 0, 0));
        jPanel10.add(jSeparator66, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 260, 280, 10));

        ArticuloLey.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        ArticuloLey.setForeground(new java.awt.Color(0, 0, 0));
        jPanel10.add(ArticuloLey, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 280, 200, 30));

        lblProgreso.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        lblProgreso.setForeground(new java.awt.Color(0, 0, 0));
        jPanel10.add(lblProgreso, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 40, 490, 20));

        PanelIngresarDelito.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 50, 780, 440));

        cancelarD.setText("Cancelar");
        cancelarD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarDActionPerformed(evt);
            }
        });
        PanelIngresarDelito.add(cancelarD, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 10, -1, -1));

        jLabel29.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(102, 0, 0));
        jLabel29.setText("Todos los campos son obligarorios*");
        PanelIngresarDelito.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 10, 200, -1));

        AñadirPreso.setBackground(new java.awt.Color(7, 56, 7));
        AñadirPreso.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        AñadirPreso.setForeground(new java.awt.Color(255, 255, 255));
        AñadirPreso.setText("Finalizar y añadir preso");
        AñadirPreso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AñadirPresoActionPerformed(evt);
            }
        });
        PanelIngresarDelito.add(AñadirPreso, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 500, 170, 40));

        TabbedAñadirInformacionGeneral.addTab("TabbedAñadirInformacionGeneral", PanelIngresarDelito);

        ActualizarInformacionPreso.setBackground(new java.awt.Color(255, 255, 255));
        ActualizarInformacionPreso.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel13.setBackground(new java.awt.Color(180, 180, 195));
        jPanel13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel14.setBackground(new java.awt.Color(29, 35, 51));

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel13.add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1060, 20));

        jLabel34.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(0, 0, 0));
        jLabel34.setText("Grupo sanguineo:");
        jPanel13.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 410, 130, 20));

        jLabel35.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(0, 0, 0));
        jLabel35.setText("Actualizar datos judiciales");
        jPanel13.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 40, 190, -1));

        jLabel36.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(0, 0, 0));
        jLabel36.setText("Actualizar datos generales");
        jPanel13.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 40, 200, -1));

        jLabel37.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(0, 0, 0));
        jLabel37.setText("Segundo nombre:");
        jPanel13.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 120, 130, -1));

        jLabel38.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(0, 0, 0));
        jLabel38.setText("Primer apellido:");
        jPanel13.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 160, 110, -1));

        jLabel39.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(0, 0, 0));
        jLabel39.setText("Edad:");
        jPanel13.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 240, 90, -1));

        jLabel40.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(0, 0, 0));
        jLabel40.setText("Segundo apellido:");
        jPanel13.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 200, 120, -1));

        jLabel41.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(0, 0, 0));
        jLabel41.setText("Estatura:");
        jPanel13.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 280, 90, -1));

        jLabel42.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(0, 0, 0));
        jLabel42.setText("Peso:");
        jPanel13.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 320, 90, 20));

        jLabel43.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(0, 0, 0));
        jLabel43.setText("Nacionalidad:");
        jPanel13.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 360, 90, 20));

        jSeparator19.setForeground(new java.awt.Color(0, 0, 0));
        jPanel13.add(jSeparator19, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 220, 310, 10));

        jSeparator24.setForeground(new java.awt.Color(0, 0, 0));
        jPanel13.add(jSeparator24, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 300, 310, 10));

        jSeparator25.setForeground(new java.awt.Color(0, 0, 0));
        jPanel13.add(jSeparator25, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 340, 310, 10));

        jSeparator26.setForeground(new java.awt.Color(0, 0, 0));
        jPanel13.add(jSeparator26, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 380, 310, 10));

        jSeparator28.setForeground(new java.awt.Color(0, 0, 0));
        jPanel13.add(jSeparator28, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 180, 310, 10));

        nuevoGrupoSanguineoCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Seleccione>", "A+", "A-", "O+", "O-", "B+", "B-", "AB+", "AB-" }));
        jPanel13.add(nuevoGrupoSanguineoCombo, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 410, -1, 30));

        jLabel91.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel91.setForeground(new java.awt.Color(0, 0, 0));
        jLabel91.setText("Sentencia:");
        jPanel13.add(jLabel91, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 90, 90, -1));

        jSeparator61.setForeground(new java.awt.Color(0, 0, 0));
        jPanel13.add(jSeparator61, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 110, 70, 10));

        jLabel92.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel92.setForeground(new java.awt.Color(0, 0, 0));
        jLabel92.setText("Año");
        jPanel13.add(jLabel92, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 90, -1, -1));

        jLabel81.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel81.setForeground(new java.awt.Color(0, 0, 0));
        jLabel81.setText("Mes");
        jPanel13.add(jLabel81, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 90, -1, -1));

        jLabel72.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel72.setForeground(new java.awt.Color(0, 0, 0));
        jLabel72.setText("Nivel de seguridad:");
        jPanel13.add(jLabel72, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 190, 130, -1));

        jSeparator56.setForeground(new java.awt.Color(0, 0, 0));
        jPanel13.add(jSeparator56, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 210, 130, 10));

        jLabel88.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel88.setForeground(new java.awt.Color(0, 0, 0));
        jLabel88.setText("En aislamiento:");
        jPanel13.add(jLabel88, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 240, -1, 20));

        jSeparator67.setForeground(new java.awt.Color(0, 0, 0));
        jPanel13.add(jSeparator67, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 160, 120, 10));

        jSeparator59.setForeground(new java.awt.Color(0, 0, 0));
        jPanel13.add(jSeparator59, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 300, 110, 10));

        jSeparator68.setForeground(new java.awt.Color(0, 0, 0));
        jPanel13.add(jSeparator68, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 260, 110, 10));

        nuevoNivelSeguridadCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Seleccionar>", "Baja", "Media", "Alta" }));
        nuevoNivelSeguridadCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevoNivelSeguridadComboActionPerformed(evt);
            }
        });
        jPanel13.add(nuevoNivelSeguridadCombo, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 180, 190, 30));

        nuevoAislamientoCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Seleccionar>", "Sí", "No" }));
        jPanel13.add(nuevoAislamientoCombo, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 230, 200, 30));

        nuevoNivelRiesgoCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Seleccionar>", "Bajo", "Medio", "Alto" }));
        nuevoNivelRiesgoCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevoNivelRiesgoComboActionPerformed(evt);
            }
        });
        jPanel13.add(nuevoNivelRiesgoCombo, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 280, 210, 30));

        nuevaSeccionCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Seleccionar>", "Sección A", "Sección B", "Sección C" }));
        nuevaSeccionCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevaSeccionComboActionPerformed(evt);
            }
        });
        jPanel13.add(nuevaSeccionCombo, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 130, 190, 30));
        jPanel13.add(nuevoAño, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 80, -1, -1));
        jPanel13.add(nuevoMes, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 80, -1, -1));

        ActualizarFotoBoton.setText("Actualizar foto");
        ActualizarFotoBoton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ActualizarFotoBotonActionPerformed(evt);
            }
        });
        jPanel13.add(ActualizarFotoBoton, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 380, -1, -1));

        nuevaFoto.setBackground(new java.awt.Color(255, 255, 255));
        nuevaFoto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        jPanel13.add(nuevaFoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 350, 100, 90));

        jLabel74.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel74.setForeground(new java.awt.Color(0, 0, 0));
        jLabel74.setText("Nivel de riesgo");
        jPanel13.add(jLabel74, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 280, 110, 20));

        jLabel79.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel79.setForeground(new java.awt.Color(0, 0, 0));
        jLabel79.setText("Sección Asiganada:");
        jPanel13.add(jLabel79, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 140, -1, -1));

        jSeparator29.setForeground(new java.awt.Color(0, 0, 0));
        jPanel13.add(jSeparator29, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 260, 310, 10));

        jPanel17.setBackground(new java.awt.Color(29, 35, 51));

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel13.add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 500, 1060, 10));

        actualizarPreso.setBackground(new java.awt.Color(29, 35, 51));
        actualizarPreso.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        actualizarPreso.setForeground(new java.awt.Color(255, 255, 255));
        actualizarPreso.setText("Actualizar Preso");
        actualizarPreso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actualizarPresoActionPerformed(evt);
            }
        });
        jPanel13.add(actualizarPreso, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 450, 170, 40));

        nuevoPrimerNombreField.setBackground(new java.awt.Color(204, 204, 204));
        nuevoPrimerNombreField.setForeground(new java.awt.Color(0, 0, 0));
        nuevoPrimerNombreField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        nuevoPrimerNombreField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevoPrimerNombreFieldActionPerformed(evt);
            }
        });
        jPanel13.add(nuevoPrimerNombreField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 70, 200, 30));

        nuevoPrimerApellidoField.setBackground(new java.awt.Color(204, 204, 204));
        nuevoPrimerApellidoField.setForeground(new java.awt.Color(0, 0, 0));
        nuevoPrimerApellidoField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel13.add(nuevoPrimerApellidoField, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 150, 200, 30));

        nuevaEdadField.setBackground(new java.awt.Color(204, 204, 204));
        nuevaEdadField.setForeground(new java.awt.Color(0, 0, 0));
        nuevaEdadField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        nuevaEdadField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevaEdadFieldActionPerformed(evt);
            }
        });
        jPanel13.add(nuevaEdadField, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 230, 250, 30));

        nuevoSegundoApellidoField.setBackground(new java.awt.Color(204, 204, 204));
        nuevoSegundoApellidoField.setForeground(new java.awt.Color(0, 0, 0));
        nuevoSegundoApellidoField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel13.add(nuevoSegundoApellidoField, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 190, 190, 30));

        nuevaEstaturaField.setBackground(new java.awt.Color(204, 204, 204));
        nuevaEstaturaField.setForeground(new java.awt.Color(0, 0, 0));
        nuevaEstaturaField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel13.add(nuevaEstaturaField, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 270, 240, 30));

        nuevoPesoField.setBackground(new java.awt.Color(204, 204, 204));
        nuevoPesoField.setForeground(new java.awt.Color(0, 0, 0));
        nuevoPesoField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        nuevoPesoField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevoPesoFieldActionPerformed(evt);
            }
        });
        jPanel13.add(nuevoPesoField, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 310, 250, 30));

        nuevaNacionalidadField.setBackground(new java.awt.Color(204, 204, 204));
        nuevaNacionalidadField.setForeground(new java.awt.Color(0, 0, 0));
        nuevaNacionalidadField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel13.add(nuevaNacionalidadField, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 350, 210, 30));

        jSeparator81.setForeground(new java.awt.Color(0, 0, 0));
        jPanel13.add(jSeparator81, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 140, 310, 10));

        jLabel73.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel73.setForeground(new java.awt.Color(0, 0, 0));
        jLabel73.setText("Primer nombre:");
        jPanel13.add(jLabel73, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 80, 110, -1));

        jSeparator82.setForeground(new java.awt.Color(0, 0, 0));
        jPanel13.add(jSeparator82, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 100, 310, 10));

        nuevoSegundoNombreField.setBackground(new java.awt.Color(204, 204, 204));
        nuevoSegundoNombreField.setForeground(new java.awt.Color(0, 0, 0));
        nuevoSegundoNombreField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        nuevoSegundoNombreField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevoSegundoNombreFieldActionPerformed(evt);
            }
        });
        jPanel13.add(nuevoSegundoNombreField, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 110, 180, 30));

        ActualizarInformacionPreso.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 1060, 510));

        jLabel30.setFont(new java.awt.Font("Arial", 2, 14)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(102, 0, 0));
        jLabel30.setText("Los campos que no desee actualizar déjelos en blanco");
        ActualizarInformacionPreso.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 7, 370, 20));

        TabbedAñadirInformacionGeneral.addTab("Actualizar preso", ActualizarInformacionPreso);

        PanelAñadirPresoBase.add(TabbedAñadirInformacionGeneral, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1100, 590));

        OficialDeRegistroView.addTab("AñadirPreso", PanelAñadirPresoBase);

        Perfil.setBackground(new java.awt.Color(255, 255, 255));
        Perfil.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel19.setBackground(new java.awt.Color(180, 180, 195));
        jPanel19.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel21.setBackground(new java.awt.Color(29, 35, 51));
        jPanel21.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel19.add(jPanel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 490, 20));

        jLabel45.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(0, 0, 0));
        jLabel45.setText("Apellido completo:");
        jPanel19.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 90, 140, -1));

        jLabel50.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(0, 0, 0));
        jLabel50.setText("Identificacion:");
        jPanel19.add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 140, 110, -1));

        jLabel51.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel51.setForeground(new java.awt.Color(0, 0, 0));
        jLabel51.setText("Edad:");
        jPanel19.add(jLabel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 180, 50, -1));

        jLabel52.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel52.setForeground(new java.awt.Color(0, 0, 0));
        jLabel52.setText("Sexo:");
        jPanel19.add(jLabel52, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 220, 50, -1));

        jLabel53.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel53.setForeground(new java.awt.Color(0, 0, 0));
        jLabel53.setText("Nacionalidad:");
        jPanel19.add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 260, 100, -1));

        jLabel54.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel54.setForeground(new java.awt.Color(0, 0, 0));
        jLabel54.setText("Nombre completo:");
        jPanel19.add(jLabel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, 150, -1));

        jSeparator34.setForeground(new java.awt.Color(0, 0, 0));
        jPanel19.add(jSeparator34, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 410, 10));

        jSeparator36.setForeground(new java.awt.Color(0, 0, 0));
        jPanel19.add(jSeparator36, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 110, 410, 10));

        jSeparator37.setForeground(new java.awt.Color(0, 0, 0));
        jPanel19.add(jSeparator37, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 160, 410, 10));

        jSeparator38.setForeground(new java.awt.Color(0, 0, 0));
        jPanel19.add(jSeparator38, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 200, 410, 10));

        jSeparator39.setForeground(new java.awt.Color(0, 0, 0));
        jPanel19.add(jSeparator39, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 240, 410, 10));

        jSeparator40.setForeground(new java.awt.Color(0, 0, 0));
        jPanel19.add(jSeparator40, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 280, 410, 10));

        ApellidoODR.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        ApellidoODR.setForeground(new java.awt.Color(0, 0, 0));
        jPanel19.add(ApellidoODR, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 80, 260, 30));

        IdentificacionODR.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        IdentificacionODR.setForeground(new java.awt.Color(0, 0, 0));
        jPanel19.add(IdentificacionODR, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 130, 330, 30));

        EdadODR.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        EdadODR.setForeground(new java.awt.Color(0, 0, 0));
        jPanel19.add(EdadODR, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 170, 330, 30));

        SexoODR.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        SexoODR.setForeground(new java.awt.Color(0, 0, 0));
        jPanel19.add(SexoODR, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 210, 330, 30));

        NacionalidadODR.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        NacionalidadODR.setForeground(new java.awt.Color(0, 0, 0));
        jPanel19.add(NacionalidadODR, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 250, 330, 30));

        nombreODR.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        nombreODR.setForeground(new java.awt.Color(0, 0, 0));
        jPanel19.add(nombreODR, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 40, 260, 30));

        Perfil.add(jPanel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 110, 490, 330));

        jPanel18.setBackground(new java.awt.Color(180, 180, 195));
        jPanel18.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel20.setBackground(new java.awt.Color(29, 35, 51));
        jPanel20.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel18.add(jPanel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 310, 20));

        LabelFotoOficialDeRegistro.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));
        jPanel18.add(LabelFotoOficialDeRegistro, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 50, 190, 160));

        jLabel44.setFont(new java.awt.Font("Arial", 2, 15)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(102, 102, 102));
        jLabel44.setText("Oficial De Registro");
        jPanel18.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 230, -1, 20));
        jPanel18.add(LabelRango, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 280, 140, 20));

        jSeparator33.setForeground(new java.awt.Color(0, 0, 0));
        jPanel18.add(jSeparator33, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 300, 220, 10));

        jLabel46.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(0, 0, 0));
        jLabel46.setText("Numero de placa");
        jPanel18.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 340, 130, -1));
        jPanel18.add(LabelNumeroPlaca, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 340, 80, 20));

        jLabel47.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(0, 0, 0));
        jLabel47.setText("Turno");
        jPanel18.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 390, -1, -1));
        jPanel18.add(LabelTurno, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 390, 130, 20));

        jSeparator35.setForeground(new java.awt.Color(0, 0, 0));
        jPanel18.add(jSeparator35, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 410, 220, 10));

        jLabel49.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        jLabel49.setForeground(new java.awt.Color(0, 0, 0));
        jLabel49.setText("Rango");
        jPanel18.add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 280, 50, -1));

        jSeparator41.setForeground(new java.awt.Color(0, 0, 0));
        jPanel18.add(jSeparator41, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 360, 220, 10));

        cerrarSesionODR.setText("Cerrar sesión");
        cerrarSesionODR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cerrarSesionODRActionPerformed(evt);
            }
        });
        jPanel18.add(cerrarSesionODR, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 440, 110, -1));

        Perfil.add(jPanel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 60, 310, 490));

        jLabel48.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel48.setForeground(new java.awt.Color(0, 0, 0));
        jLabel48.setText("INFORMACIÓN PERSONAL");
        Perfil.add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 70, 220, -1));

        btnActualizarInfoODR.setText("Actualizar información");
        btnActualizarInfoODR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarInfoODRActionPerformed(evt);
            }
        });
        Perfil.add(btnActualizarInfoODR, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 480, 160, 40));

        OficialDeRegistroView.addTab("Perfil", Perfil);

        Expediente.setBackground(new java.awt.Color(255, 255, 255));
        Expediente.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tablaExpediente.setBackground(new java.awt.Color(255, 255, 255));
        tablaExpediente.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        tablaExpediente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Delito", "Id", "Fecha Sentencia", "Tiempo de condena", "Gravedad", "Fecha comisión", "Fecha de Salida"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaExpediente.setRowHeight(45);
        jScrollPane4.setViewportView(tablaExpediente);

        Expediente.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 250, 760, 320));

        jPanel23.setBackground(new java.awt.Color(215, 215, 215));
        jPanel23.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel56.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel56.setForeground(new java.awt.Color(0, 0, 0));
        jLabel56.setText("Codigo Expediente:");
        jPanel23.add(jLabel56, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, -1, -1));

        jSeparator43.setForeground(new java.awt.Color(0, 0, 0));
        jPanel23.add(jSeparator43, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 260, 10));

        jLabel57.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel57.setForeground(new java.awt.Color(0, 0, 0));
        jLabel57.setText("Fecha de Apertura:");
        jPanel23.add(jLabel57, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 210, -1, -1));
        jPanel23.add(FechaAper, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 210, 100, 20));

        jSeparator44.setForeground(new java.awt.Color(0, 0, 0));
        jPanel23.add(jSeparator44, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 260, 10));

        jLabel58.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel58.setForeground(new java.awt.Color(0, 0, 0));
        jLabel58.setText("Estado:");
        jPanel23.add(jLabel58, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 280, -1, 20));
        jPanel23.add(Estado, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 280, 140, 20));

        jSeparator45.setForeground(new java.awt.Color(0, 0, 0));
        jPanel23.add(jSeparator45, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 300, 260, 10));

        jLabel59.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel59.setForeground(new java.awt.Color(0, 0, 0));
        jLabel59.setText("Juzgado:");
        jPanel23.add(jLabel59, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 350, -1, -1));
        jPanel23.add(Juzgado, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 350, 110, 20));

        jSeparator46.setForeground(new java.awt.Color(0, 0, 0));
        jPanel23.add(jSeparator46, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 370, 260, 10));
        jPanel23.add(RegistroNum, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 70, 100, 20));
        jPanel23.add(CodExpe, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 140, 110, 20));

        jLabel60.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel60.setForeground(new java.awt.Color(0, 0, 0));
        jLabel60.setText("Nivel de riesgo:");
        jPanel23.add(jLabel60, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 420, 110, -1));

        jSeparator47.setForeground(new java.awt.Color(0, 0, 0));
        jPanel23.add(jSeparator47, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 440, 260, 10));

        nivelRiesgExp.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        nivelRiesgExp.setForeground(new java.awt.Color(0, 0, 0));
        jPanel23.add(nivelRiesgExp, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 420, 110, 20));

        jLabel61.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel61.setForeground(new java.awt.Color(0, 0, 0));
        jLabel61.setText("Nivel de adaptación: ");
        jPanel23.add(jLabel61, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 490, 150, -1));

        jSeparator48.setForeground(new java.awt.Color(0, 0, 0));
        jPanel23.add(jSeparator48, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 510, 260, 10));

        jLabel62.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel62.setForeground(new java.awt.Color(0, 0, 0));
        jLabel62.setText("Numero de registro:");
        jPanel23.add(jLabel62, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, -1, 20));

        jSeparator53.setForeground(new java.awt.Color(0, 0, 0));
        jPanel23.add(jSeparator53, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 260, 10));

        jPanel35.setBackground(new java.awt.Color(29, 35, 51));
        jPanel35.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel78.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel78.setForeground(new java.awt.Color(255, 255, 255));
        jLabel78.setText("EXPEDIENTE");
        jPanel35.add(jLabel78, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 20, 110, -1));

        jPanel23.add(jPanel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 300, 40));

        Expediente.add(jPanel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 300, 540));

        fotoPresoExpediente.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));
        Expediente.add(fotoPresoExpediente, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 30, 160, 200));

        DescripDelito.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Expediente.add(DescripDelito, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 110, 140, 110));

        jLabel55.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel55.setForeground(new java.awt.Color(0, 0, 0));
        jLabel55.setText("Apellidos completos:");
        Expediente.add(jLabel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 80, -1, 20));

        jLabel63.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel63.setForeground(new java.awt.Color(0, 0, 0));
        jLabel63.setText("Descripcion delito");
        Expediente.add(jLabel63, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 70, -1, 20));

        jLabel64.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel64.setForeground(new java.awt.Color(0, 0, 0));
        jLabel64.setText("Identificación:");
        Expediente.add(jLabel64, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 160, -1, 20));

        jLabel65.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel65.setForeground(new java.awt.Color(0, 0, 0));
        jLabel65.setText("Nacionalidad:");
        Expediente.add(jLabel65, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 200, -1, 20));

        jLabel66.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel66.setForeground(new java.awt.Color(0, 0, 0));
        jLabel66.setText("Edad:");
        Expediente.add(jLabel66, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 120, -1, 20));

        jLabel67.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel67.setForeground(new java.awt.Color(0, 0, 0));
        jLabel67.setText("Nombre  completo:");
        Expediente.add(jLabel67, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 40, -1, 20));

        jSeparator42.setForeground(new java.awt.Color(0, 0, 0));
        Expediente.add(jSeparator42, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 60, 380, 10));

        jSeparator49.setForeground(new java.awt.Color(0, 0, 0));
        Expediente.add(jSeparator49, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 100, 370, 10));

        jSeparator50.setForeground(new java.awt.Color(0, 0, 0));
        Expediente.add(jSeparator50, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 140, 380, 10));

        jSeparator51.setForeground(new java.awt.Color(0, 0, 0));
        Expediente.add(jSeparator51, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 180, 380, 10));

        jSeparator52.setForeground(new java.awt.Color(0, 0, 0));
        Expediente.add(jSeparator52, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 220, 380, 10));

        ape.setForeground(new java.awt.Color(0, 0, 0));
        Expediente.add(ape, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 70, 200, 30));

        edad.setForeground(new java.awt.Color(0, 0, 0));
        Expediente.add(edad, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 110, 240, 30));

        identi.setForeground(new java.awt.Color(0, 0, 0));
        Expediente.add(identi, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 150, 210, 30));

        naciona.setForeground(new java.awt.Color(0, 0, 0));
        Expediente.add(naciona, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 190, 220, 30));

        nom.setForeground(new java.awt.Color(0, 0, 0));
        Expediente.add(nom, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 40, 230, 20));

        OficialDeRegistroView.addTab("Expediente", Expediente);

        DatosPersonalesPreso.setBackground(new java.awt.Color(255, 255, 255));
        DatosPersonalesPreso.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel68.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        jLabel68.setForeground(new java.awt.Color(0, 0, 0));
        jLabel68.setText("DATOS PERSONALES");
        DatosPersonalesPreso.add(jLabel68, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 80, -1, -1));

        jLabel69.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel69.setForeground(new java.awt.Color(0, 0, 0));
        jLabel69.setText("Nombre completo:");
        DatosPersonalesPreso.add(jLabel69, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 150, 140, 20));

        jSeparator54.setForeground(new java.awt.Color(0, 0, 0));
        DatosPersonalesPreso.add(jSeparator54, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 170, 400, 10));

        jLabel70.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel70.setForeground(new java.awt.Color(0, 0, 0));
        jLabel70.setText("Apellido completo:");
        DatosPersonalesPreso.add(jLabel70, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 190, 140, 20));

        jSeparator55.setForeground(new java.awt.Color(0, 0, 0));
        DatosPersonalesPreso.add(jSeparator55, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 210, 400, 10));

        jLabel71.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel71.setForeground(new java.awt.Color(0, 0, 0));
        jLabel71.setText("Edad:");
        DatosPersonalesPreso.add(jLabel71, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 230, 110, 20));

        jSeparator57.setForeground(new java.awt.Color(0, 0, 0));
        DatosPersonalesPreso.add(jSeparator57, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 250, 400, 10));

        jLabel75.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel75.setForeground(new java.awt.Color(0, 0, 0));
        jLabel75.setText("Sexo:");
        DatosPersonalesPreso.add(jLabel75, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 270, 110, 20));

        jSeparator58.setForeground(new java.awt.Color(0, 0, 0));
        DatosPersonalesPreso.add(jSeparator58, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 293, 400, 10));

        jLabel76.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel76.setForeground(new java.awt.Color(0, 0, 0));
        jLabel76.setText("Nacionalidad:");
        DatosPersonalesPreso.add(jLabel76, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 310, 110, 20));

        jSeparator60.setForeground(new java.awt.Color(0, 0, 0));
        DatosPersonalesPreso.add(jSeparator60, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 330, 400, 10));

        jLabel77.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel77.setForeground(new java.awt.Color(0, 0, 0));
        jLabel77.setText("Identificación:");
        DatosPersonalesPreso.add(jLabel77, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 350, 110, 20));

        jSeparator62.setForeground(new java.awt.Color(0, 0, 0));
        DatosPersonalesPreso.add(jSeparator62, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 370, 400, 20));

        jLabel87.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel87.setForeground(new java.awt.Color(0, 0, 0));
        jLabel87.setText("Estatura:");
        DatosPersonalesPreso.add(jLabel87, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 390, 110, 20));

        jSeparator63.setForeground(new java.awt.Color(0, 0, 0));
        DatosPersonalesPreso.add(jSeparator63, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 410, 400, 10));

        jLabel89.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel89.setForeground(new java.awt.Color(0, 0, 0));
        jLabel89.setText("Peso:");
        DatosPersonalesPreso.add(jLabel89, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 430, 110, 20));

        jSeparator64.setForeground(new java.awt.Color(0, 0, 0));
        DatosPersonalesPreso.add(jSeparator64, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 490, 400, 10));

        jLabel90.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel90.setForeground(new java.awt.Color(0, 0, 0));
        jLabel90.setText("Grupo Sanguineo:");
        DatosPersonalesPreso.add(jLabel90, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 470, 140, 20));

        jLabel93.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        DatosPersonalesPreso.add(jLabel93, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 60, 480, 460));

        jSeparator65.setForeground(new java.awt.Color(0, 0, 0));
        DatosPersonalesPreso.add(jSeparator65, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 450, 400, 10));
        DatosPersonalesPreso.add(nombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 150, 260, 20));
        DatosPersonalesPreso.add(apellido, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 190, 270, 20));
        DatosPersonalesPreso.add(edad1, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 230, 270, 20));
        DatosPersonalesPreso.add(sexo, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 270, 270, 20));
        DatosPersonalesPreso.add(nacionali, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 310, 270, 20));
        DatosPersonalesPreso.add(identi1, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 350, 260, 20));
        DatosPersonalesPreso.add(estatura, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 390, 270, 20));
        DatosPersonalesPreso.add(peso, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 430, 270, 20));
        DatosPersonalesPreso.add(sangre, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 470, 210, 20));

        jPanel24.setBackground(new java.awt.Color(29, 35, 51));
        jPanel24.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel25.setBackground(new java.awt.Color(180, 180, 195));
        jPanel25.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ImagenPresoInformacion.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        jPanel25.add(ImagenPresoInformacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 160, 210));

        jPanel24.add(jPanel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 60, 180, 230));

        DatosPersonalesPreso.add(jPanel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 100, 280, 370));

        regresar.setText("Regresar");
        regresar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                regresarActionPerformed(evt);
            }
        });
        DatosPersonalesPreso.add(regresar, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 20, 100, -1));

        OficialDeRegistroView.addTab("InformacionPreso", DatosPersonalesPreso);

        PanelTablaPresoBase.setBackground(new java.awt.Color(255, 255, 255));
        PanelTablaPresoBase.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TablaPresos.setBackground(new java.awt.Color(204, 204, 204));
        TablaPresos.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 5, true));
        TablaPresos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Foto", "Id", "Nombre completo", "Apellido completo", "Edad", "Identificación", "Nacionalidad", "Seccion", "Celda"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TablaPresos.setGridColor(new java.awt.Color(0, 0, 0));
        TablaPresos.setShowGrid(false);
        TablaPresos.setShowHorizontalLines(true);
        TablaPresos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                TablaPresosMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(TablaPresos);
        if (TablaPresos.getColumnModel().getColumnCount() > 0) {
            TablaPresos.getColumnModel().getColumn(0).setPreferredWidth(20);
            TablaPresos.getColumnModel().getColumn(1).setPreferredWidth(3);
            TablaPresos.getColumnModel().getColumn(2).setPreferredWidth(60);
            TablaPresos.getColumnModel().getColumn(3).setPreferredWidth(60);
            TablaPresos.getColumnModel().getColumn(4).setPreferredWidth(3);
            TablaPresos.getColumnModel().getColumn(7).setPreferredWidth(20);
            TablaPresos.getColumnModel().getColumn(8).setPreferredWidth(20);
        }

        PanelTablaPresoBase.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 190, 1020, 360));

        jPanel1.setBackground(new java.awt.Color(180, 180, 195));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(29, 35, 51));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Sección");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 20, 90, 30));

        SelectorSeccion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sección A", "Sección B", "Sección C" }));
        SelectorSeccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectorSeccionActionPerformed(evt);
            }
        });
        jPanel2.add(SelectorSeccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 20, 90, 30));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 330, 70));

        btnRestaurarTabla.setBackground(new java.awt.Color(29, 35, 51));
        btnRestaurarTabla.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnRestaurarTabla.setForeground(new java.awt.Color(255, 255, 255));
        btnRestaurarTabla.setText("Restaurar tabla");
        btnRestaurarTabla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRestaurarTablaActionPerformed(evt);
            }
        });
        jPanel1.add(btnRestaurarTabla, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 20, 130, 30));

        PanelTablaPresoBase.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 90, 1010, 70));
        PanelTablaPresoBase.add(identificacionB, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 20, 590, 40));

        btnBuscarIdentificacion.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnBuscarIdentificacion.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscarIdentificacion.setText("Buscar");
        btnBuscarIdentificacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarIdentificacionActionPerformed(evt);
            }
        });
        PanelTablaPresoBase.add(btnBuscarIdentificacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 20, 90, 40));

        OficialDeRegistroView.addTab("Presos", PanelTablaPresoBase);

        ActualizarODR.setBackground(new java.awt.Color(255, 255, 255));
        ActualizarODR.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel27.setBackground(new java.awt.Color(180, 180, 195));
        jPanel27.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel28.setBackground(new java.awt.Color(29, 35, 51));
        jPanel28.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel27.add(jPanel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 430, 960, 20));

        jLabel98.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel98.setForeground(new java.awt.Color(0, 0, 0));
        jLabel98.setText("DATOS PERSONALES");
        jPanel27.add(jLabel98, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 50, -1, -1));

        jLabel99.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel99.setForeground(new java.awt.Color(0, 0, 0));
        jLabel99.setText("Segundo nombre:");
        jPanel27.add(jLabel99, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 130, -1, -1));

        jSeparator69.setForeground(new java.awt.Color(0, 0, 0));
        jPanel27.add(jSeparator69, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 150, 350, 10));

        nuevoPrimerNombre.setBackground(new java.awt.Color(204, 204, 204));
        nuevoPrimerNombre.setForeground(new java.awt.Color(0, 0, 0));
        nuevoPrimerNombre.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        nuevoPrimerNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevoPrimerNombreActionPerformed(evt);
            }
        });
        jPanel27.add(nuevoPrimerNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 80, 230, 30));

        jLabel100.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel100.setForeground(new java.awt.Color(0, 0, 0));
        jLabel100.setText("Segundo apellido:");
        jPanel27.add(jLabel100, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 210, -1, -1));

        jSeparator70.setForeground(new java.awt.Color(0, 0, 0));
        jPanel27.add(jSeparator70, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 230, 350, 10));

        primerNuevoApellido.setBackground(new java.awt.Color(204, 204, 204));
        primerNuevoApellido.setForeground(new java.awt.Color(0, 0, 0));
        primerNuevoApellido.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        primerNuevoApellido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                primerNuevoApellidoActionPerformed(evt);
            }
        });
        jPanel27.add(primerNuevoApellido, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 160, 230, 30));

        nuevaEdad.setBackground(new java.awt.Color(204, 204, 204));
        nuevaEdad.setForeground(new java.awt.Color(0, 0, 0));
        nuevaEdad.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel27.add(nuevaEdad, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 240, 280, 30));

        jLabel101.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel101.setForeground(new java.awt.Color(0, 0, 0));
        jLabel101.setText("Edad: ");
        jPanel27.add(jLabel101, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 250, -1, -1));

        jSeparator71.setForeground(new java.awt.Color(0, 0, 0));
        jPanel27.add(jSeparator71, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 270, 350, 10));

        nuevaIdenti.setBackground(new java.awt.Color(204, 204, 204));
        nuevaIdenti.setForeground(new java.awt.Color(0, 0, 0));
        nuevaIdenti.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel27.add(nuevaIdenti, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 280, 240, 30));

        jLabel96.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel96.setForeground(new java.awt.Color(0, 0, 0));
        jLabel96.setText("Identificación:");
        jPanel27.add(jLabel96, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 290, -1, 20));

        jSeparator72.setForeground(new java.awt.Color(0, 0, 0));
        jPanel27.add(jSeparator72, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 310, 350, 10));

        nuevaNacio.setBackground(new java.awt.Color(204, 204, 204));
        nuevaNacio.setForeground(new java.awt.Color(0, 0, 0));
        nuevaNacio.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel27.add(nuevaNacio, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 320, 230, 30));

        jLabel95.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel95.setForeground(new java.awt.Color(0, 0, 0));
        jLabel95.setText("Nacionalidad: ");
        jPanel27.add(jLabel95, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 330, -1, -1));

        jSeparator73.setForeground(new java.awt.Color(0, 0, 0));
        jPanel27.add(jSeparator73, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 350, 350, 10));

        jLabel97.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel97.setForeground(new java.awt.Color(0, 0, 0));
        jLabel97.setText("Sexo:");
        jPanel27.add(jLabel97, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 370, -1, -1));

        jSeparator74.setForeground(new java.awt.Color(0, 0, 0));
        jPanel27.add(jSeparator74, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 390, 40, 10));

        nuevoSexoOdr.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Seleccionar>", "F", "M" }));
        nuevoSexoOdr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevoSexoOdrActionPerformed(evt);
            }
        });
        jPanel27.add(nuevoSexoOdr, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 360, 130, 30));

        jLabel104.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel104.setForeground(new java.awt.Color(0, 0, 0));
        jLabel104.setText("CUENTA");
        jPanel27.add(jLabel104, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 70, -1, -1));

        jLabel102.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel102.setForeground(new java.awt.Color(0, 0, 0));
        jLabel102.setText("Correo Electronico:");
        jPanel27.add(jLabel102, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 110, -1, -1));

        nuevoCorreo.setBackground(new java.awt.Color(180, 180, 195));
        nuevoCorreo.setForeground(new java.awt.Color(0, 0, 0));
        nuevoCorreo.setBorder(null);
        nuevoCorreo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevoCorreoActionPerformed(evt);
            }
        });
        jPanel27.add(nuevoCorreo, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 100, 250, 30));

        jSeparator75.setForeground(new java.awt.Color(0, 0, 0));
        jPanel27.add(jSeparator75, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 130, 380, 10));

        jLabel103.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel103.setForeground(new java.awt.Color(0, 0, 0));
        jLabel103.setText("Contraseña:");
        jPanel27.add(jLabel103, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 160, -1, 20));

        jSeparator76.setForeground(new java.awt.Color(0, 0, 0));
        jPanel27.add(jSeparator76, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 180, 380, 10));

        nuevaContra.setBackground(new java.awt.Color(180, 180, 195));
        nuevaContra.setForeground(new java.awt.Color(0, 0, 0));
        nuevaContra.setBorder(null);
        nuevaContra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevaContraActionPerformed(evt);
            }
        });
        jPanel27.add(nuevaContra, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 150, 260, 30));

        LabelFOTO.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));
        jPanel27.add(LabelFOTO, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 280, 100, 90));

        Actualizarimagenodr.setText("Actualizar foto");
        Actualizarimagenodr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ActualizarimagenodrActionPerformed(evt);
            }
        });
        jPanel27.add(Actualizarimagenodr, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 240, -1, -1));

        jPanel29.setBackground(new java.awt.Color(29, 35, 51));
        jPanel29.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel27.add(jPanel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 960, 20));

        jPanel26.setBackground(new java.awt.Color(29, 35, 51));

        jPanel30.setBackground(new java.awt.Color(29, 35, 51));

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 430, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
            .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel26Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 430, Short.MAX_VALUE)
            .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel26Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jPanel27.add(jPanel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, 10, 430));

        jPanel31.setBackground(new java.awt.Color(29, 35, 51));

        javax.swing.GroupLayout jPanel31Layout = new javax.swing.GroupLayout(jPanel31);
        jPanel31.setLayout(jPanel31Layout);
        jPanel31Layout.setHorizontalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 430, Short.MAX_VALUE)
        );

        jPanel27.add(jPanel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 10, 20, 430));

        javax.swing.GroupLayout jPanel32Layout = new javax.swing.GroupLayout(jPanel32);
        jPanel32.setLayout(jPanel32Layout);
        jPanel32Layout.setHorizontalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel32Layout.setVerticalGroup(
            jPanel32Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 440, Short.MAX_VALUE)
        );

        jPanel27.add(jPanel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 0, 10, 440));

        jLabel112.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel112.setForeground(new java.awt.Color(0, 0, 0));
        jLabel112.setText("Primer nombre:");
        jPanel27.add(jLabel112, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 90, -1, -1));

        jSeparator83.setForeground(new java.awt.Color(0, 0, 0));
        jPanel27.add(jSeparator83, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 110, 350, 10));

        jLabel113.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel113.setForeground(new java.awt.Color(0, 0, 0));
        jLabel113.setText("Primer apellido:");
        jPanel27.add(jLabel113, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 170, -1, -1));

        jSeparator85.setForeground(new java.awt.Color(0, 0, 0));
        jPanel27.add(jSeparator85, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 190, 350, 10));

        segundoNuevoApellido1.setBackground(new java.awt.Color(204, 204, 204));
        segundoNuevoApellido1.setForeground(new java.awt.Color(0, 0, 0));
        segundoNuevoApellido1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        segundoNuevoApellido1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                segundoNuevoApellido1ActionPerformed(evt);
            }
        });
        jPanel27.add(segundoNuevoApellido1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 200, 230, 30));

        nuevoSegundoNombre.setBackground(new java.awt.Color(204, 204, 204));
        nuevoSegundoNombre.setForeground(new java.awt.Color(0, 0, 0));
        nuevoSegundoNombre.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        nuevoSegundoNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevoSegundoNombreActionPerformed(evt);
            }
        });
        jPanel27.add(nuevoSegundoNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 120, 230, 30));

        ActualizarODR.add(jPanel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 50, 960, 450));

        jLabel94.setBackground(new java.awt.Color(0, 0, 0));
        jLabel94.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        jLabel94.setForeground(new java.awt.Color(204, 0, 0));
        jLabel94.setText("Los campos que no desee modificar déjelos en blanco*");
        ActualizarODR.add(jLabel94, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 20, 290, -1));

        jButton2.setBackground(new java.awt.Color(29, 35, 51));
        jButton2.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Actualizar");
        ActualizarODR.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 520, 150, 40));

        OficialDeRegistroView.addTab("ActualizarInfoODR", ActualizarODR);

        sanciones.setBackground(new java.awt.Color(255, 255, 255));
        sanciones.setForeground(new java.awt.Color(0, 0, 0));
        sanciones.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel34.setBackground(new java.awt.Color(180, 180, 195));
        jPanel34.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel33.setBackground(new java.awt.Color(29, 35, 51));
        jPanel33.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel34.add(jPanel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 870, 20));

        jLabel106.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel106.setForeground(new java.awt.Color(0, 0, 0));
        jLabel106.setText("Apellido:");
        jPanel34.add(jLabel106, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, -1, 20));

        jLabel107.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel107.setForeground(new java.awt.Color(0, 0, 0));
        jLabel107.setText("Nivel de seguridad:");
        jPanel34.add(jLabel107, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 60, -1, 20));

        jLabel108.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel108.setForeground(new java.awt.Color(0, 0, 0));
        jLabel108.setText("Nombre:");
        jPanel34.add(jLabel108, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, -1, 20));

        jLabel109.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel109.setForeground(new java.awt.Color(0, 0, 0));
        jLabel109.setText("En aislamiento:");
        jPanel34.add(jLabel109, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 30, -1, 20));

        jSeparator77.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator77.setToolTipText("HOLA");
        jPanel34.add(jSeparator77, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 80, 360, 10));

        jSeparator78.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator78.setToolTipText("HOLA");
        jPanel34.add(jSeparator78, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 360, 10));

        jSeparator79.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator79.setToolTipText("HOLA");
        jPanel34.add(jSeparator79, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 360, 10));

        jSeparator80.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator80.setToolTipText("HOLA");
        jPanel34.add(jSeparator80, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 50, 360, 10));

        nivelseguridadS.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        nivelseguridadS.setForeground(new java.awt.Color(0, 0, 0));
        jPanel34.add(nivelseguridadS, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 60, 210, 20));

        nombreS.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        nombreS.setForeground(new java.awt.Color(0, 0, 0));
        jPanel34.add(nombreS, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 30, 290, 20));

        apellidoS.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        apellidoS.setForeground(new java.awt.Color(0, 0, 0));
        jPanel34.add(apellidoS, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 60, 290, 20));

        aislamientoS.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        aislamientoS.setForeground(new java.awt.Color(0, 0, 0));
        jPanel34.add(aislamientoS, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 30, 240, 20));

        sanciones.add(jPanel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 60, 870, 100));

        tablaSancion.setBackground(new java.awt.Color(255, 255, 255));
        tablaSancion.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Id", "Motivo", "Tipo de sanción", "Fecha sanción"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(tablaSancion);
        if (tablaSancion.getColumnModel().getColumnCount() > 0) {
            tablaSancion.getColumnModel().getColumn(0).setResizable(false);
            tablaSancion.getColumnModel().getColumn(1).setResizable(false);
            tablaSancion.getColumnModel().getColumn(2).setResizable(false);
            tablaSancion.getColumnModel().getColumn(3).setResizable(false);
        }

        sanciones.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 180, 870, 350));

        OficialDeRegistroView.addTab("Sanciones", sanciones);

        AñadirDelito.setBackground(new java.awt.Color(255, 255, 255));
        AñadirDelito.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel36.setBackground(new java.awt.Color(180, 180, 195));
        jPanel36.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel37.setBackground(new java.awt.Color(29, 35, 51));
        jPanel37.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel80.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel80.setForeground(new java.awt.Color(255, 255, 255));
        jLabel80.setText("AÑADIR DELITO");
        jPanel37.add(jLabel80, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 10, 110, -1));

        jPanel36.add(jPanel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 780, 30));

        jPanel38.setBackground(new java.awt.Color(29, 35, 51));
        jPanel38.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel36.add(jPanel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 450, 780, 10));

        jLabel121.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel121.setForeground(new java.awt.Color(0, 0, 0));
        jLabel121.setText("Delito:");
        jPanel36.add(jLabel121, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 80, 90, 30));

        jLabel122.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel122.setForeground(new java.awt.Color(0, 0, 0));
        jLabel122.setText("Articulo:");
        jPanel36.add(jLabel122, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 220, 60, -1));

        jLabel123.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel123.setForeground(new java.awt.Color(0, 0, 0));
        jLabel123.setText("Gravedad:");
        jPanel36.add(jLabel123, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 280, 110, 30));

        jSeparator89.setForeground(new java.awt.Color(0, 0, 0));
        jPanel36.add(jSeparator89, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 240, 280, 10));

        NuevosDelitosNombre1.setBackground(new java.awt.Color(51, 51, 51));
        NuevosDelitosNombre1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Seleccionar>", "Injuria", "Calumnia", "Daño en bien ajeno", "Violación de habitación ajena", "Inasistencia alimentaria", "Omisión de socorro", "Lesiones personales leves", "Falsedad en documento privado", "Usurpación de derechos", "Uso de documento falso", "Abuso de confianza", "Hurto simple", "Receptación", "Estafa", "Violación de cerraduras o sellos", "Fraude", "Violación de medidas sanitarias", "Invasión de tierras o edificaciones", "Suplantación de identidad", "Contrabando", "Hurto calificado", "Lesiones personales graves", "Extorsión", "Falsedad en documento público", "Lavado de activos", "Peculado por uso", "Violencia intrafamiliar", "Acoso sexual", "Acceso abusivo a sistema informático", "Suplantación en medios electrónicos", "Daño informático", "Tráfico de influencias", "Porte ilegal de armas", "Cohecho", "Concusión", "Prevaricato", "Abuso de autoridad", "Perturbación del orden público", "Enriquecimiento ilícito", "Tráfico de fauna o flora silvestre", "Minería ilegal", "Hurto agravado", "Homicidio culposo", "Acceso carnal abusivo con menor de 14 años", "Actos sexuales con menor de 14 años", "Acceso carnal violento", "Acto sexual violento", "Violación", "Secuestro simple", "Tráfico de estupefacientes", "Fabricación o porte de estupefacientes", "Concierto para delinquir", "Homicidio", "Homicidio agravado", "Tortura", "Desaparición forzada", "Terrorismo", "Rebelión", "Genocidio", "Crímenes de lesa humanidad" }));
        NuevosDelitosNombre1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NuevosDelitosNombre1ActionPerformed(evt);
            }
        });
        jPanel36.add(NuevosDelitosNombre1, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 70, 220, 40));

        grav.setBackground(new java.awt.Color(51, 51, 51));
        grav.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Seleccionar>", "Baja", "Media", "Alta" }));
        jPanel36.add(grav, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 270, 210, 40));

        jSeparator92.setForeground(new java.awt.Color(0, 0, 0));
        jPanel36.add(jSeparator92, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 110, 270, 10));
        jPanel36.add(fechaComisionActualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 80, 150, 30));

        jLabel125.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel125.setForeground(new java.awt.Color(0, 0, 0));
        jLabel125.setText("Descripcion delito:");
        jPanel36.add(jLabel125, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 150, 120, 30));

        cod.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        cod.setForeground(new java.awt.Color(0, 0, 0));
        jPanel36.add(cod, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 140, 210, 30));

        textAreaDescripcion1.setColumns(20);
        textAreaDescripcion1.setRows(5);
        jScrollPane7.setViewportView(textAreaDescripcion1);

        jPanel36.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 180, 270, 130));

        jLabel126.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel126.setForeground(new java.awt.Color(0, 0, 0));
        jLabel126.setText("Fecha comisión:");
        jPanel36.add(jLabel126, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 90, 120, 30));

        jLabel127.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel127.setForeground(new java.awt.Color(0, 0, 0));
        jLabel127.setText("Codigo:");
        jPanel36.add(jLabel127, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 150, 80, 20));

        jSeparator93.setForeground(new java.awt.Color(0, 0, 0));
        jPanel36.add(jSeparator93, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 170, 280, 10));

        art.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        art.setForeground(new java.awt.Color(0, 0, 0));
        jPanel36.add(art, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 210, 200, 30));

        lblProgreso1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        lblProgreso1.setForeground(new java.awt.Color(0, 0, 0));
        jPanel36.add(lblProgreso1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 40, 490, 20));

        jLabel31.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(0, 0, 0));
        jLabel31.setText("Añadir sentencia extra: ");
        jPanel36.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 370, 200, 30));
        jPanel36.add(jSpinField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 370, -1, -1));
        jPanel36.add(jSpinField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 370, -1, -1));

        jLabel124.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel124.setForeground(new java.awt.Color(0, 0, 0));
        jLabel124.setText("Gravedad:");
        jPanel36.add(jLabel124, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 280, 110, 30));

        AñadirDelito.add(jPanel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 30, 780, 460));

        finalizarDelito.setBackground(new java.awt.Color(9, 82, 9));
        finalizarDelito.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        finalizarDelito.setForeground(new java.awt.Color(255, 255, 255));
        finalizarDelito.setText("Finalizar y guardar");
        finalizarDelito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                finalizarDelitoActionPerformed(evt);
            }
        });
        AñadirDelito.add(finalizarDelito, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 520, 140, 40));

        OficialDeRegistroView.addTab("Añadir Delito", AñadirDelito);

        PRINCIPAL.add(OficialDeRegistroView, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 20, 1100, 620));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(PRINCIPAL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(PRINCIPAL, javax.swing.GroupLayout.PREFERRED_SIZE, 644, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TablaPresosMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TablaPresosMousePressed

    }//GEN-LAST:event_TablaPresosMousePressed

    private void btnRestaurarTablaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRestaurarTablaActionPerformed
        DefaultTableModel modelo = (DefaultTableModel) TablaPresos.getModel();
        modelo.setRowCount(0);

        PresoDAO presoDAO = new PresoDAO();
        List<Preso> presos = presoDAO.cargarTodos();

        for (Preso preso : presos) {
            ImageIcon foto = null;
            if (preso.getFotoPath() != null && !preso.getFotoPath().isEmpty()) {
                foto = cargarImagenPreso(preso.getFotoPath());
            } else {
                foto = new ImageIcon(getClass().getResource("/images/default_profile.png"));
            }

            modelo.addRow(new Object[]{
                foto,
                preso.getId(),
                preso.getPrimerNombre(),
                preso.getPrimerApellido(),
                preso.getEdad(),
                preso.getIdentificacion(),
                preso.getNacionalidad(),
                preso.getSeccionAsignada(),
                preso.getCeldaAsignada()

            });
        }

        TablaPresos.revalidate();
        TablaPresos.repaint();
    }//GEN-LAST:event_btnRestaurarTablaActionPerformed

    private void InputNacionalidadPresoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InputNacionalidadPresoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_InputNacionalidadPresoActionPerformed

    private void nuevoNivelRiesgoComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevoNivelRiesgoComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nuevoNivelRiesgoComboActionPerformed

    private void nuevoNivelSeguridadComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevoNivelSeguridadComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nuevoNivelSeguridadComboActionPerformed

    private void actualizarPresoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actualizarPresoActionPerformed
    PresoController controller = new PresoController(this);
    
    boolean actualizado = controller.actualizarPreso(
        presoOriginal,
        nuevoPrimerNombreField.getText(),
        nuevoSegundoNombreField.getText(),
        nuevoPrimerApellidoField.getText(),
        nuevoSegundoApellidoField.getText(),
        nuevaEdadField.getText(),
        nuevaNacionalidadField.getText(),
        nuevaEstaturaField.getText(),
        nuevoPesoField.getText(),
        (Integer) nuevoAño.getValue(),
        (Integer) nuevoMes.getValue(),
        nuevoGrupoSanguineoCombo.getSelectedItem(),
        nuevaSeccionCombo.getSelectedItem(),
        nuevoNivelSeguridadCombo.getSelectedItem(),
        nuevoAislamientoCombo.getSelectedItem(),
        nuevoNivelRiesgoCombo.getSelectedItem(),
        selectedImageFile
    );
    
    if (actualizado) {
        JOptionPane.showMessageDialog(this, "Preso actualizado correctamente", 
                                   "Éxito", JOptionPane.INFORMATION_MESSAGE);
        cargarDatosEnTabla();
        limpiarCamposActualizacion();

        OficialDeRegistroView.setSelectedIndex(4);
    
}
        

    }//GEN-LAST:event_actualizarPresoActionPerformed

    private void InputNombrePresoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InputNombrePresoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_InputNombrePresoActionPerformed

    private void nuevaEdadFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevaEdadFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nuevaEdadFieldActionPerformed

    private void nuevoPesoFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevoPesoFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nuevoPesoFieldActionPerformed

    private void nuevoPrimerNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevoPrimerNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nuevoPrimerNombreActionPerformed

    private void primerNuevoApellidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_primerNuevoApellidoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_primerNuevoApellidoActionPerformed

    private void nuevoSexoOdrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevoSexoOdrActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nuevoSexoOdrActionPerformed

    private void nuevaContraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevaContraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nuevaContraActionPerformed

    private void nuevoCorreoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevoCorreoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nuevoCorreoActionPerformed

    private void ActualizarimagenodrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ActualizarimagenodrActionPerformed

// TODO add your handling code here:
    }//GEN-LAST:event_ActualizarimagenodrActionPerformed

    private void PanelPerfilTituloMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PanelPerfilTituloMouseClicked
        OficialDeRegistroView.setSelectedIndex(1);
    }//GEN-LAST:event_PanelPerfilTituloMouseClicked

    private void PanelPerfilTituloMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PanelPerfilTituloMouseEntered
        PanelPerfilTitulo.setBackground(new Color(43, 54, 84));
    }//GEN-LAST:event_PanelPerfilTituloMouseEntered

    private void PanelPerfilTituloMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PanelPerfilTituloMouseExited
        PanelPerfilTitulo.setBackground(new Color(29, 35, 51));
    }//GEN-LAST:event_PanelPerfilTituloMouseExited

    private void PanelPresosTituloMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PanelPresosTituloMouseClicked
        OficialDeRegistroView.setSelectedIndex(4);
    }//GEN-LAST:event_PanelPresosTituloMouseClicked

    private void PanelPresosTituloMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PanelPresosTituloMouseEntered
        PanelPresosTitulo.setBackground(new Color(43, 54, 84));
    }//GEN-LAST:event_PanelPresosTituloMouseEntered

    private void PanelPresosTituloMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PanelPresosTituloMouseExited
        PanelPresosTitulo.setBackground(new Color(29, 35, 51));
    }//GEN-LAST:event_PanelPresosTituloMouseExited

    private void PanelAñadirPresoTituloMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PanelAñadirPresoTituloMouseClicked

        OficialDeRegistroView.setSelectedIndex(0);

        TabbedAñadirInformacionGeneral.setSelectedIndex(0);


    }//GEN-LAST:event_PanelAñadirPresoTituloMouseClicked

    private void PanelAñadirPresoTituloMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PanelAñadirPresoTituloMouseEntered
        PanelAñadirPresoTitulo.setBackground(new Color(43, 54, 84));
    }//GEN-LAST:event_PanelAñadirPresoTituloMouseEntered

    private void PanelAñadirPresoTituloMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PanelAñadirPresoTituloMouseExited
        PanelAñadirPresoTitulo.setBackground(new Color(29, 35, 51));
    }//GEN-LAST:event_PanelAñadirPresoTituloMouseExited

    private void btnBuscarIdentificacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarIdentificacionActionPerformed

        btnBuscarIdentificacion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idBuscado = identificacionB.getText().trim();
                if (idBuscado.isEmpty()) {
                    return;
                }

                String identificacion = identificacionB.getText();
                List<Preso> todos = new PresoDAO().cargarTodos();
                DefaultTableModel model = (DefaultTableModel) TablaPresos.getModel();
                model.setRowCount(0);

                for (Preso preso : todos) {
                    if (preso.getIdentificacion().equalsIgnoreCase(identificacion)) {

                        ImageIcon foto = null;
                        if (preso.getFotoPath() != null && !preso.getFotoPath().isEmpty()) {
                            foto = cargarImagenPreso(preso.getFotoPath());
                        } else {
                            foto = new ImageIcon(getClass().getResource("/images/default_profile.png"));
                        }
                        model.addRow(new Object[]{
                            foto,
                            preso.getId(),
                            preso.getPrimerNombre(),
                            preso.getPrimerApellido(),
                            preso.getEdad(),
                            preso.getIdentificacion(),
                            preso.getNacionalidad(),
                            preso.getSeccionAsignada(),
                            preso.getCeldaAsignada()

                        });
                        break;
                    } else {
                        JOptionPane.showMessageDialog(null, "NO se encontro ningun preso con la identificación " + identificacion);
                    }
                }
            }
        });


    }//GEN-LAST:event_btnBuscarIdentificacionActionPerformed

    private void SelectorSeccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectorSeccionActionPerformed
        String seccionSeleccionada = SelectorSeccion.getSelectedItem().toString();
        List<Preso> todosLosPresos = new PresoDAO().cargarTodos();
        DefaultTableModel model = (DefaultTableModel) TablaPresos.getModel();
        model.setRowCount(0);

        for (Preso preso : todosLosPresos) {

            if (preso.getSeccionAsignada().equalsIgnoreCase(seccionSeleccionada)) {

                ImageIcon foto = null;
                if (preso.getFotoPath() != null && !preso.getFotoPath().isEmpty()) {
                    foto = cargarImagenPreso(preso.getFotoPath());
                } else {
                    foto = new ImageIcon(getClass().getResource("/images/default_profile.png"));
                }

                model.addRow(new Object[]{
                    foto,
                    preso.getPrimerNombre(),
                    preso.getPrimerApellido(),
                    preso.getEdad(),
                    preso.getSexo(),
                    preso.getNacionalidad(),
                    preso.getIdentificacion(),
                    preso.getCeldaAsignada(),
                    preso.getSeccionAsignada()

                });
            }
        }    }//GEN-LAST:event_SelectorSeccionActionPerformed

    private void AñadirPresoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AñadirPresoActionPerformed
        finalizarPresoActionPerformed();
        OficialDeRegistroView.setSelectedIndex(4);
    }//GEN-LAST:event_AñadirPresoActionPerformed

    private void cancelarDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelarDActionPerformed
        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Estás seguro de que deseas cancelar y borrar los datos?",
                "Confirmación",
                JOptionPane.YES_NO_OPTION
        );

        if (opcion == JOptionPane.YES_OPTION) {

            limpiarFormularioCompleto();
            limpiarCamposDelito();
            OficialDeRegistroView.setSelectedIndex(4);

        }
    }//GEN-LAST:event_cancelarDActionPerformed

    private void Siguiente1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Siguiente1ActionPerformed
        TabbedAñadirInformacionGeneral.setSelectedIndex(1);
    }//GEN-LAST:event_Siguiente1ActionPerformed

    private void CancelarD3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelarD3ActionPerformed
        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Estás seguro de que deseas cancelar y borrar los datos?",
                "Confirmación",
                JOptionPane.YES_NO_OPTION
        );

        if (opcion == JOptionPane.YES_OPTION) {

            limpiarCamposPersonales();
            OficialDeRegistroView.setSelectedIndex(4);
        }
    }//GEN-LAST:event_CancelarD3ActionPerformed

    private void Siguiente2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Siguiente2ActionPerformed
        TabbedAñadirInformacionGeneral.setSelectedIndex(2);
    }//GEN-LAST:event_Siguiente2ActionPerformed

    private void cancelarD2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelarD2ActionPerformed
        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Estás seguro de que deseas cancelar y borrar los datos?",
                "Confirmación",
                JOptionPane.YES_NO_OPTION
        );

        if (opcion == JOptionPane.YES_OPTION) {

            limpiarCamposJudiciales();
            OficialDeRegistroView.setSelectedIndex(4);

        }
    }//GEN-LAST:event_cancelarD2ActionPerformed

    private void btnIngresarFotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIngresarFotoActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Imágenes (JPG, PNG, GIF)", "jpg", "jpeg", "png", "gif");
        fileChooser.setFileFilter(filter);

        int resultado = fileChooser.showOpenDialog(this);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            selectedImageFile = fileChooser.getSelectedFile();

            try {
                originalImage = ImageIO.read(selectedImageFile);
                ImageIcon icon = new ImageIcon(originalImage);
                Image img = icon.getImage();
                Image imgEscalada = img.getScaledInstance(
                        lblFoto.getWidth(),
                        lblFoto.getHeight(),
                        Image.SCALE_SMOOTH);

                lblFoto.setIcon(new ImageIcon(imgEscalada));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al cargar la imagen: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        }    }//GEN-LAST:event_btnIngresarFotoActionPerformed

    private void InputIdentificacionPresoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InputIdentificacionPresoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_InputIdentificacionPresoActionPerformed

    private void ActualizarFotoBotonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ActualizarFotoBotonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ActualizarFotoBotonActionPerformed

    private void nuevoSegundoNombreFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevoSegundoNombreFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nuevoSegundoNombreFieldActionPerformed

    private void InputSegundoApellidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InputSegundoApellidoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_InputSegundoApellidoActionPerformed

    private void guardarDelitoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarDelitoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_guardarDelitoActionPerformed

private void agregarDelitoYFinalizar() {
    try {
        if (preso == null) {
            JOptionPane.showMessageDialog(this, 
                "Seleccione un preso primero", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (cod.getText().trim().isEmpty() || 
            art.getText().trim().isEmpty() || 
            fechaComisionActualizar.getDate() == null) {
            JOptionPane.showMessageDialog(this,
                "Complete todos los campos obligatorios",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Delito nuevoDelito = new Delito(
            0, 
            Integer.parseInt(cod.getText().trim()),
            NuevosDelitosNombre1.getSelectedItem().toString(),
            art.getText().trim(),
            grav.getSelectedItem().toString(),
            textAreaDescripcion1.getText().trim(),
            fechaComisionActualizar.getDate().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate()
        );

        DelitoDAO delitoDAO = new DelitoDAO();
        PresoDAO presoDAO = new PresoDAO();
        
        int idDelito = delitoDAO.guardarDelito(nuevoDelito);
        nuevoDelito.setId(idDelito);
        
        List<Delito> delitos = new ArrayList<>();
        delitos.add(nuevoDelito);
        
        boolean exito = presoDAO.agregarDelitosAExpediente(preso, delitos);

        if (exito) {
            JOptionPane.showMessageDialog(this,
                "Delito añadido exitosamente al expediente",
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
            cod.setText("");
            art.setText("");
            fechaComisionActualizar.setDate(null);
            grav.setSelectedIndex(0);
            textAreaDescripcion1.setText("");
            
            OficialDeRegistroView.setSelectedIndex(4);
        } else {
            JOptionPane.showMessageDialog(this,
                "Error al asociar el delito al preso",
                "Error", JOptionPane.ERROR_MESSAGE);
        }

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this,
            "El código debe ser un número válido",
            "Error", JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Error inesperado: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}


    private void limpiarCamposDelitoNuevo() {
        cod.setText("");
        art.setText("");
        grav.setSelectedIndex(0);
        NuevosDelitosNombre1.setSelectedIndex(0);
        fechaComisionActualizar.setDate(null);
        textAreaDescripcion1.setText("");
    }

    


    private void finalizarDelitoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_finalizarDelitoActionPerformed

    agregarDelitoYFinalizar();
    limpiarCamposDelitoNuevo();
    OficialDeRegistroView.setSelectedIndex(4);
    }//GEN-LAST:event_finalizarDelitoActionPerformed

    private void NuevosDelitosNombre1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NuevosDelitosNombre1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NuevosDelitosNombre1ActionPerformed

    private void segundoNuevoApellido1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_segundoNuevoApellido1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_segundoNuevoApellido1ActionPerformed

    private void nuevoSegundoNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevoSegundoNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nuevoSegundoNombreActionPerformed

    private void regresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_regresarActionPerformed
        OficialDeRegistroView.setSelectedIndex(4);


    }//GEN-LAST:event_regresarActionPerformed

    private void btnActualizarInfoODRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarInfoODRActionPerformed
        OficialDeRegistroView.setSelectedIndex(5);
    }//GEN-LAST:event_btnActualizarInfoODRActionPerformed

    private void cerrarSesionODRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cerrarSesionODRActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cerrarSesionODRActionPerformed

    private void nuevoPrimerNombreFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevoPrimerNombreFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nuevoPrimerNombreFieldActionPerformed

    private void nuevaSeccionComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevaSeccionComboActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nuevaSeccionComboActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(OficialDeRegistro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(OficialDeRegistro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(OficialDeRegistro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(OficialDeRegistro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new OficialDeRegistro().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ActualizarFotoBoton;
    private javax.swing.JPanel ActualizarInformacionPreso;
    private javax.swing.JPanel ActualizarODR;
    private javax.swing.JButton Actualizarimagenodr;
    private javax.swing.JLabel ApellidoODR;
    private javax.swing.JLabel ArticuloLey;
    private javax.swing.JPanel AñadirDelito;
    private javax.swing.JButton AñadirPreso;
    private javax.swing.JButton CancelarD3;
    private javax.swing.JLabel CodExpe;
    private javax.swing.JLabel Codigo;
    private javax.swing.JPanel DatosPersonalesPreso;
    private javax.swing.JLabel DescripDelito;
    private javax.swing.JTextArea DescripcionDelito;
    private javax.swing.JLabel EdadODR;
    private javax.swing.JLabel Estado;
    private javax.swing.JPanel Expediente;
    private javax.swing.JLabel FechaAper;
    private com.toedter.calendar.JDateChooser FechaComision;
    private javax.swing.JComboBox<String> Gravedad;
    private javax.swing.JLabel IdentificacionODR;
    private javax.swing.JLabel ImagenPresoInformacion;
    private javax.swing.JPanel InformacionGeneralPanel;
    private javax.swing.JPanel InformacionJudicialPanel;
    private javax.swing.JTextField InputApellidoPreso;
    private javax.swing.JTextField InputEdadPreso;
    private javax.swing.JTextField InputEstaturaPreso;
    private javax.swing.JTextField InputIdentificacionPreso;
    private javax.swing.JTextField InputNacionalidadPreso;
    private javax.swing.JTextField InputNombrePreso;
    private javax.swing.JTextField InputPesoPreso;
    private javax.swing.JTextField InputSegundoApellido;
    private javax.swing.JTextField InputSegundoNombre;
    private javax.swing.JComboBox<String> InputSexoPreso;
    private javax.swing.JLabel Juzgado;
    private javax.swing.JLabel LabelFOTO;
    private javax.swing.JLabel LabelFotoOficialDeRegistro;
    private javax.swing.JLabel LabelNumeroPlaca;
    private javax.swing.JLabel LabelRango;
    private javax.swing.JLabel LabelTurno;
    private javax.swing.JLabel NacionalidadODR;
    private javax.swing.JComboBox<String> NuevosDelitosNombre1;
    private javax.swing.JTabbedPane OficialDeRegistroView;
    private javax.swing.JPanel PRINCIPAL;
    private javax.swing.JPanel PanelAñadirPresoBase;
    private javax.swing.JPanel PanelAñadirPresoTitulo;
    private javax.swing.JPanel PanelFondoTextoPrincipal;
    private javax.swing.JPanel PanelIngresarDelito;
    private javax.swing.JPanel PanelPerfilTitulo;
    private javax.swing.JPanel PanelPresosTitulo;
    private javax.swing.JPanel PanelTablaPresoBase;
    private javax.swing.JPanel Perfil;
    private javax.swing.JLabel RegistroNum;
    private javax.swing.JComboBox<String> SelectorSeccion;
    private javax.swing.JLabel SexoODR;
    private javax.swing.JButton Siguiente1;
    private javax.swing.JButton Siguiente2;
    private javax.swing.JTabbedPane TabbedAñadirInformacionGeneral;
    private javax.swing.JTable TablaPresos;
    private javax.swing.JComboBox<String> TipoSangreCombobox;
    private javax.swing.JButton actualizarPreso;
    private javax.swing.JLabel aislamientoS;
    private javax.swing.JLabel ape;
    private javax.swing.JLabel apellido;
    private javax.swing.JLabel apellidoS;
    private javax.swing.JLabel art;
    private javax.swing.JButton btnActualizarInfoODR;
    private javax.swing.JButton btnBuscarIdentificacion;
    private javax.swing.JButton btnIngresarFoto;
    private javax.swing.JButton btnRestaurarTabla;
    private javax.swing.JButton cancelarD;
    private javax.swing.JButton cancelarD2;
    private javax.swing.JComboBox<String> cantidadDelitos;
    private javax.swing.JButton cerrarSesionODR;
    private javax.swing.JLabel cod;
    private javax.swing.JComboBox<String> condicionComb;
    private com.toedter.calendar.JDateChooser datePickerFechaIngreso;
    private javax.swing.JComboBox<String> delito;
    private javax.swing.JLabel edad;
    private javax.swing.JLabel edad1;
    private javax.swing.JLabel estatura;
    private com.toedter.calendar.JDateChooser fechaComisionActualizar;
    private javax.swing.JButton finalizarDelito;
    private javax.swing.JLabel fotoPresoExpediente;
    private javax.swing.JComboBox<String> grav;
    private javax.swing.JButton guardarDelito;
    private javax.swing.JLabel identi;
    private javax.swing.JLabel identi1;
    private javax.swing.JTextField identificacionB;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel102;
    private javax.swing.JLabel jLabel103;
    private javax.swing.JLabel jLabel104;
    private javax.swing.JLabel jLabel105;
    private javax.swing.JLabel jLabel106;
    private javax.swing.JLabel jLabel107;
    private javax.swing.JLabel jLabel108;
    private javax.swing.JLabel jLabel109;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel110;
    private javax.swing.JLabel jLabel111;
    private javax.swing.JLabel jLabel112;
    private javax.swing.JLabel jLabel113;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel121;
    private javax.swing.JLabel jLabel122;
    private javax.swing.JLabel jLabel123;
    private javax.swing.JLabel jLabel124;
    private javax.swing.JLabel jLabel125;
    private javax.swing.JLabel jLabel126;
    private javax.swing.JLabel jLabel127;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JSeparator jSeparator15;
    private javax.swing.JSeparator jSeparator16;
    private javax.swing.JSeparator jSeparator17;
    private javax.swing.JSeparator jSeparator18;
    private javax.swing.JSeparator jSeparator19;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator20;
    private javax.swing.JSeparator jSeparator21;
    private javax.swing.JSeparator jSeparator24;
    private javax.swing.JSeparator jSeparator25;
    private javax.swing.JSeparator jSeparator26;
    private javax.swing.JSeparator jSeparator27;
    private javax.swing.JSeparator jSeparator28;
    private javax.swing.JSeparator jSeparator29;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator33;
    private javax.swing.JSeparator jSeparator34;
    private javax.swing.JSeparator jSeparator35;
    private javax.swing.JSeparator jSeparator36;
    private javax.swing.JSeparator jSeparator37;
    private javax.swing.JSeparator jSeparator38;
    private javax.swing.JSeparator jSeparator39;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator40;
    private javax.swing.JSeparator jSeparator41;
    private javax.swing.JSeparator jSeparator42;
    private javax.swing.JSeparator jSeparator43;
    private javax.swing.JSeparator jSeparator44;
    private javax.swing.JSeparator jSeparator45;
    private javax.swing.JSeparator jSeparator46;
    private javax.swing.JSeparator jSeparator47;
    private javax.swing.JSeparator jSeparator48;
    private javax.swing.JSeparator jSeparator49;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator50;
    private javax.swing.JSeparator jSeparator51;
    private javax.swing.JSeparator jSeparator52;
    private javax.swing.JSeparator jSeparator53;
    private javax.swing.JSeparator jSeparator54;
    private javax.swing.JSeparator jSeparator55;
    private javax.swing.JSeparator jSeparator56;
    private javax.swing.JSeparator jSeparator57;
    private javax.swing.JSeparator jSeparator58;
    private javax.swing.JSeparator jSeparator59;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator60;
    private javax.swing.JSeparator jSeparator61;
    private javax.swing.JSeparator jSeparator62;
    private javax.swing.JSeparator jSeparator63;
    private javax.swing.JSeparator jSeparator64;
    private javax.swing.JSeparator jSeparator65;
    private javax.swing.JSeparator jSeparator66;
    private javax.swing.JSeparator jSeparator67;
    private javax.swing.JSeparator jSeparator68;
    private javax.swing.JSeparator jSeparator69;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator70;
    private javax.swing.JSeparator jSeparator71;
    private javax.swing.JSeparator jSeparator72;
    private javax.swing.JSeparator jSeparator73;
    private javax.swing.JSeparator jSeparator74;
    private javax.swing.JSeparator jSeparator75;
    private javax.swing.JSeparator jSeparator76;
    private javax.swing.JSeparator jSeparator77;
    private javax.swing.JSeparator jSeparator78;
    private javax.swing.JSeparator jSeparator79;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator80;
    private javax.swing.JSeparator jSeparator81;
    private javax.swing.JSeparator jSeparator82;
    private javax.swing.JSeparator jSeparator83;
    private javax.swing.JSeparator jSeparator84;
    private javax.swing.JSeparator jSeparator85;
    private javax.swing.JSeparator jSeparator89;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JSeparator jSeparator92;
    private javax.swing.JSeparator jSeparator93;
    private com.toedter.components.JSpinField jSpinField1;
    private com.toedter.components.JSpinField jSpinField2;
    private javax.swing.JLabel lblFechaSalidaCalculada;
    private javax.swing.JLabel lblFoto;
    private javax.swing.JLabel lblProgreso;
    private javax.swing.JLabel lblProgreso1;
    private javax.swing.JLabel naciona;
    private javax.swing.JLabel nacionali;
    private javax.swing.JLabel nivelRiesgExp;
    private javax.swing.JLabel nivelseguridadS;
    private javax.swing.JLabel nom;
    private javax.swing.JLabel nombre;
    private javax.swing.JLabel nombreODR;
    private javax.swing.JLabel nombreS;
    private javax.swing.JTextField nuevaContra;
    private javax.swing.JTextField nuevaEdad;
    private javax.swing.JTextField nuevaEdadField;
    private javax.swing.JTextField nuevaEstaturaField;
    private javax.swing.JLabel nuevaFoto;
    private javax.swing.JTextField nuevaIdenti;
    private javax.swing.JTextField nuevaNacio;
    private javax.swing.JTextField nuevaNacionalidadField;
    private javax.swing.JComboBox<String> nuevaSeccionCombo;
    private javax.swing.JComboBox<String> nuevoAislamientoCombo;
    private com.toedter.components.JSpinField nuevoAño;
    private javax.swing.JTextField nuevoCorreo;
    private javax.swing.JComboBox<String> nuevoGrupoSanguineoCombo;
    private com.toedter.components.JSpinField nuevoMes;
    private javax.swing.JComboBox<String> nuevoNivelRiesgoCombo;
    private javax.swing.JComboBox<String> nuevoNivelSeguridadCombo;
    private javax.swing.JTextField nuevoPesoField;
    private javax.swing.JTextField nuevoPrimerApellidoField;
    private javax.swing.JTextField nuevoPrimerNombre;
    private javax.swing.JTextField nuevoPrimerNombreField;
    private javax.swing.JTextField nuevoSegundoApellidoField;
    private javax.swing.JTextField nuevoSegundoNombre;
    private javax.swing.JTextField nuevoSegundoNombreField;
    private javax.swing.JComboBox<String> nuevoSexoOdr;
    private javax.swing.JLabel peso;
    private javax.swing.JPopupMenu ppMenuTablaPresos;
    private javax.swing.JTextField primerNuevoApellido;
    private javax.swing.JButton regresar;
    private javax.swing.JComboBox<String> riesgo;
    private javax.swing.JPanel sanciones;
    private javax.swing.JLabel sangre;
    private javax.swing.JComboBox<String> seccion;
    private javax.swing.JTextField segundoNuevoApellido1;
    private javax.swing.JComboBox<String> seguridad;
    private javax.swing.JLabel sexo;
    private com.toedter.components.JSpinField spinnerAñosSentencia;
    private com.toedter.components.JSpinField spinnerMesesSentencia;
    private javax.swing.JTable tablaExpediente;
    private javax.swing.JTable tablaSancion;
    private javax.swing.JTextArea textAreaDescripcion1;
    // End of variables declaration//GEN-END:variables
}
