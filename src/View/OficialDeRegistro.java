package View;

import Controller.PresoController;
import Controller.ExpedienteController;
import DAO.CeldaDAO;
import DAO.DelitoDAO;
import DAO.ExpedienteDAO;
import DAO.PresoDAO;
import Model.Constants.EstadoPresoEnum;
import Model.Entities.Delito;
import Model.Entities.ExpedienteJudicial;
import Model.Entities.Preso;
import Model.Entities.Sentencia;
import Utilidades.Validador;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
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
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;

public class OficialDeRegistro extends javax.swing.JFrame {

    private JLabel registroNum = new JLabel();
    private JLabel codExpe = new JLabel();
    private JLabel fechaAper = new JLabel();
    private JLabel estado = new JLabel();
    private JLabel juzgado = new JLabel();
    private JLabel nivelRiesgo = new JLabel();
    private JLabel apellidos = new JLabel();
    private JLabel identificacion = new JLabel();
    private JLabel nacionalidad = new JLabel();
    private JLabel fotoLabel = new JLabel();
    private JLabel SentenciaTotal = new JLabel();
    private JTextField txtReclusa;
    private JTextField txtIdentificacionReclusa;
    private JTextField txtEstadoReclusa;

    private HashMap<String, String[]> datosDelitos;

    private List<Delito> delitosTemporales = new ArrayList<>();

    private Preso presoOriginal;
    CeldaDAO celda = new CeldaDAO();
    private int delitoActual = 1;
    private int totalDelitos = 1;
    private BufferedImage originalImage;
    private File selectedImageFile;
    private ExpedienteDAO expedienteDAO;
    private final DelitoDAO delitoDAO = DelitoDAO.getInstancia();

    private final ExpedienteController expedienteController;
    private final PresoController presoController;
    private final Validador validador;
    private final CeldaDAO celdaDAO;

    public OficialDeRegistro() {
        this.celdaDAO = CeldaDAO.getInstancia();

        this.expedienteDAO = ExpedienteDAO.getInstancia();
        this.presoController = PresoController.getInstancia();
        this.expedienteController = ExpedienteController.getInstancia();
        this.validador = Validador.getInstancia();

        initComponents();
        inicializarMenuPresos();
        inicializarMenuDelitos();
        cargarDelitosConCodigoYArticulo();
        this.setLocationRelativeTo(null);
        OficialDeRegistroView.setSelectedIndex(3);

        cargarTodosLosPresos();
        presoController.configurarTablaImagenes(TablaPresos);
        presoController.configurarTablaImagenes(tablaInactivos);
        actualizarProgreso();
        inicializarMenuPresosInactivos();
        configurarCampoIdentificacion();

        cargarPresosInactivos();

        celda.generarCeldas("Seccion A", 20, 2);
        celda.generarCeldas("Seccion B", 20, 2);
        celda.generarCeldas("Seccion C", 20, 2);

        soloNumeros(nuevaEdadField);
        soloNumeros(nuevoPesoField);
        soloNumeros(nuevaEstaturaField);
        soloLetras(nuevoPrimerNombreField);
        soloLetras(nuevoSegundoNombreField);
        soloLetras(nuevoPrimerApellidoField);
        soloLetras(nuevoSegundoApellidoField);
        soloLetras(nuevoPrimerNombre);
        soloLetras(nuevoSegundoNombre);
        soloLetras(primerNuevoApellido);
        soloLetras(segundoNuevoApellido1);
        soloNumeros(nuevaEdad);
        soloNumeros(nuevaIdenti);
        soloNumeros(txtBusquedPresosInactivos);
        soloNumeros(identificacionB);
        soloLetras(InputNombrePreso);
        soloLetras(InputApellidoPreso);
        soloLetras(InputSegundoNombre);
        soloLetras(InputSegundoApellido);
        soloNumeros(InputEdadPreso);
        soloNumeros(InputIdentificacionPreso);

        actualizarEstadisticasCompletas();

        cantidadDelitos.addActionListener(e -> {
            actualizarProgreso();
            actualizarEstadoBotonDelito();
        });

        agregarValidacionInstantanea();

        AñadirPreso.setEnabled(false);

        actualizarEstadoBotonDelito();

        DescripcionDelito.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                actualizarEstadoBoton();
            }
        });

    }

    private void actualizarEstadisticasCompletas() {
        String estadisticas = presoController.obtenerEstadisticasPresosHorizontal();

        lblFallecidas.setText(estadisticas);

    }

    private void configurarCampoIdentificacion() {
        identificacionB.setText("Buscar preso por identificación");
        identificacionB.setForeground(Color.GRAY);

        identificacionB.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (identificacionB.getText().equals("Buscar preso por identificación")) {
                    identificacionB.setText("");
                    identificacionB.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (identificacionB.getText().isEmpty()) {
                    identificacionB.setText("Buscar preso por identificación");
                    identificacionB.setForeground(Color.GRAY);
                }
            }
        });
    }

    public void inicializarMenuPresosInactivos() {
        JPopupMenu ppMenuInactivos = new JPopupMenu();

        JMenuItem historicoExp = new JMenuItem("Historico de Expediente");
        JMenuItem verInfoGeneralInactivo = new JMenuItem("Información General");

        ppMenuInactivos.add(historicoExp);
        ppMenuInactivos.add(verInfoGeneralInactivo);

        tablaInactivos.setComponentPopupMenu(ppMenuInactivos);

                verInfoGeneralInactivo.addActionListener(e -> {
            int fila = tablaInactivos.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un preso primero", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                String identificacion = tablaInactivos.getValueAt(fila, 5).toString();
                Preso preso = new PresoDAO().buscarPresoPorIdentificacion(identificacion);

                if (preso == null) {
                    JOptionPane.showMessageDialog(this, "No se encontró el preso", "Error", JOptionPane.ERROR_MESSAGE);
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
                Image img = icon.getImage().getScaledInstance(ImagenPresoInformacion.getWidth(), ImagenPresoInformacion.getHeight(), Image.SCALE_SMOOTH);
                ImagenPresoInformacion.setIcon(new ImageIcon(img));

                OficialDeRegistroView.setSelectedIndex(3);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar información: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        
      historicoExp.addActionListener(e -> {
    int fila = tablaInactivos.getSelectedRow();
    if (fila >= 0) {
        String identificacion = tablaInactivos.getValueAt(fila, 5).toString();

        HistorialExpedienteDialog dialog = new HistorialExpedienteDialog(
                null,
                identificacion,
                expedienteController,
                OficialDeRegistroView,
                lblFechaSalida,
                RegistroNum,
                CodExpe,
                FechaAper,
                Estado,
                Juzgado,
                nivelRiesgExp,
                nom,
                ape,
                edad,
                identi,
                naciona,
                fotoPresoExpediente,
                tablaExpediente,
                sentenciaTotaal,
                panelEstadoEspecialPreso,
                lblMensajeEspecialPreso,
                txtReclusa,
                txtIdentificacionReclusa,
                txtEstadoReclusa
        );
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    } else {
        JOptionPane.showMessageDialog(this, "Seleccione un preso primero", "Aviso", JOptionPane.WARNING_MESSAGE);
    }
});


    }
    
        public void inicializarMenuPresos() {

        JMenu cambiarEstadoMenu = new JMenu("Cambiar Estado");

        JMenuItem Expediente = new JMenuItem("Expediente");

        JMenuItem Informacion = new JMenuItem("Información General");

        JMenuItem Actualizar = new JMenuItem("Actualizar Información");

        JMenuItem HistorialExpediente = new JMenuItem("Ver historial de Expedientes");

        JMenuItem AñadirDelito = new JMenuItem("Añadir Delito");
        JMenuItem ActivoItem = new JMenuItem("Marcar como Activo");
        JMenuItem fugadoItem = new JMenuItem("Marcar como Fugado");
        JMenuItem liberarItem = new JMenuItem("Liberar Preso");
        JMenuItem fallecidoItem = new JMenuItem("Marcar como Fallecido");

        cambiarEstadoMenu.add(ActivoItem);
        cambiarEstadoMenu.add(fugadoItem);
        cambiarEstadoMenu.add(liberarItem);
        cambiarEstadoMenu.add(fallecidoItem);

        ppMenuTablaPresos.add(Expediente);
        ppMenuTablaPresos.add(Informacion);
        ppMenuTablaPresos.add(AñadirDelito);
        ppMenuTablaPresos.add(Actualizar);
        ppMenuTablaPresos.add(HistorialExpediente);

        ppMenuTablaPresos.addSeparator();
        ppMenuTablaPresos.add(cambiarEstadoMenu);

        TablaPresos.setComponentPopupMenu(ppMenuTablaPresos);

        Expediente.addActionListener(e -> {
            try {
                int fila = TablaPresos.getSelectedRow();
                if (fila == -1) {
                    throw new IllegalArgumentException("Seleccione un preso primero");
                }

                String identificacion = TablaPresos.getValueAt(fila, 5).toString();
                expedienteController.cargarExpedienteCompleto(
                        identificacion, lblFechaSalida, RegistroNum, CodExpe, FechaAper, Estado, Juzgado,
                        nivelRiesgExp, nom, ape, edad, identi, naciona, fotoPresoExpediente,
                        tablaExpediente, sentenciaTotaal, panelEstadoEspecialPreso, lblMensajeEspecialPreso
                );
                OficialDeRegistroView.setSelectedIndex(6);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        });

Actualizar.addActionListener(e -> {
    int fila = TablaPresos.getSelectedRow();
    if (fila == -1) {
        JOptionPane.showMessageDialog(this, "Seleccione un preso", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    String id = TablaPresos.getValueAt(fila, 5).toString();
    Preso preso = new PresoDAO().buscarPresoPorIdentificacion(id);

    if (preso != null) {
        presoOriginal = preso; 
        System.out.println("Seleccionado: " + preso.getIdentificacion());
        cargarDatosPresoEnFormularioActualizacion(preso);
        OficialDeRegistroView.setSelectedIndex(0);
        TabbedAñadirInformacionGeneral.setSelectedIndex(3);
    } else {
        JOptionPane.showMessageDialog(this, "Preso no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
    }
});

        AñadirDelito.addActionListener(e -> {
            int fila = TablaPresos.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(null, "Seleccione un preso primero", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String identificacion = TablaPresos.getValueAt(fila, 5).toString();
            Preso preso = new PresoDAO().buscarPresoPorIdentificacion(identificacion);

            if (preso == null) {
                JOptionPane.showMessageDialog(null, "Preso no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            presoOriginal = preso;
            OficialDeRegistroView.setSelectedIndex(7);
        });

        Informacion.addActionListener(e -> {
            int fila = TablaPresos.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "¡Seleccione un preso primero!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                String identificacion = TablaPresos.getValueAt(fila, 5).toString();
                Preso preso = new PresoDAO().buscarPresoPorIdentificacion(identificacion);

                if (preso == null) {
                    JOptionPane.showMessageDialog(this, "No se encontró el preso", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                nombre.setText(preso.getNombresCompletos());
                apellido.setText(preso.getApellidosCompletos());
                edad1.setText(String.valueOf(preso.getEdad()));
                nacionali.setText(preso.getNacionalidad());
                sexo.setText(preso.getSexo());
                identi1.setText(preso.getIdentificacion());
                estatura.setText(String.valueOf(preso.getEstatura()));
                peso.setText(String.valueOf(preso.getPeso()));
                sangre.setText(preso.getGrupoSanguineo());

                ImageIcon icon = new ImageIcon(preso.getFotoPath());
                Image img = icon.getImage().getScaledInstance(ImagenPresoInformacion.getWidth(), ImagenPresoInformacion.getHeight(), Image.SCALE_SMOOTH);
                ImagenPresoInformacion.setIcon(new ImageIcon(img));

                OficialDeRegistroView.setSelectedIndex(2);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar información: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        HistorialExpediente.addActionListener(e -> {
            int fila = TablaPresos.getSelectedRow();
            if (fila >= 0) {
                String identificacion = TablaPresos.getValueAt(fila, 5).toString();

                HistorialExpedienteDialog dialog = new HistorialExpedienteDialog(
                        null,
                        identificacion,
                        expedienteController,
                        OficialDeRegistroView,
                        lblFechaSalida,
                        RegistroNum,
                        CodExpe,
                        FechaAper,
                        Estado,
                        Juzgado,
                        nivelRiesgExp,
                        nom,
                        ape,
                        edad,
                        identi,
                        naciona,
                        fotoPresoExpediente,
                        tablaExpediente,
                        sentenciaTotaal,
                        panelEstadoEspecialPreso,
                        lblMensajeEspecialPreso,
                        txtReclusa,
                        txtIdentificacionReclusa,
                        txtEstadoReclusa
                );
                dialog.setLocationRelativeTo(null);

                dialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un preso primero", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        fugadoItem.addActionListener(e -> manejarCambioEstado(EstadoPresoEnum.FUGADO));
        liberarItem.addActionListener(e -> manejarCambioEstado(EstadoPresoEnum.LIBERADO));
        fallecidoItem.addActionListener(e -> manejarCambioEstado(EstadoPresoEnum.FALLECIDO));
        ActivoItem.addActionListener(e -> manejarCambioEstado(EstadoPresoEnum.ACTIVO));
    }

    private void manejarCambioEstado(EstadoPresoEnum nuevoEstado) {
        try {
            int filaSeleccionada = TablaPresos.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(null, "Seleccione un preso", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String identificacion = TablaPresos.getValueAt(filaSeleccionada, 5).toString();
            Preso preso = presoController.obtenerPresoDesdeTabla(filaSeleccionada, TablaPresos);
            LocalDate fechaCambio;

            if (nuevoEstado == EstadoPresoEnum.FUGADO || nuevoEstado == EstadoPresoEnum.ACTIVO) {
                fechaCambio = LocalDate.now();
            } else {
                ValidarFechaDialog dialogo = new ValidarFechaDialog(null, true);
                dialogo.setTitle("Fecha para " + nuevoEstado.toString().toLowerCase());
                dialogo.setVisible(true);

                if (!dialogo.isAceptado() || dialogo.getFechaSeleccionada() == null) {
                    return;
                }

                fechaCambio = dialogo.getFechaSeleccionada().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
            }

            String mensaje = String.format(
                    "¿Confirmar cambio de estado?\n\n"
                    + "Preso: %s %s\n"
                    + "ID: %s\n"
                    + "El estado será: %s\n"
                    + "Fecha: %s",
                    preso.getPrimerNombre(), preso.getPrimerApellido(),
                    identificacion,
                    nuevoEstado,
                    fechaCambio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            );

            int confirmacion = JOptionPane.showConfirmDialog(
                    null, mensaje, "Confirmar", JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {
                boolean actualizado = presoController.cambiarEstadoPreso(identificacion, nuevoEstado, fechaCambio);
                if (actualizado) {
                    JOptionPane.showMessageDialog(null, "Estado actualizado", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarTodosLosPresos();
                    cargarPresosInactivos();
                } else {
                    JOptionPane.showMessageDialog(null, "No se pudo actualizar el estado del preso", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (IllegalArgumentException | IllegalStateException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error inesperado", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void inicializarMenuDelitos() {
        JMenuItem descripcion = new JMenuItem("Descripción del delito");
        ppMenuDelito.add(descripcion);
        tablaExpediente.setComponentPopupMenu(ppMenuDelito);

        descripcion.addActionListener(e -> {
            int fila = tablaExpediente.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(null, "Seleccione un delito", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                String nombreDelito = tablaExpediente.getValueAt(fila, 0).toString();
                Object idDelito = tablaExpediente.getValueAt(fila, 1);

                if (idDelito != null) {
                    String descripciond = expedienteController.obtenerDescripcionDelito(Integer.parseInt(idDelito.toString()));

                    new DescripcionDelitoDialog(null, true)
                            .mostrarDescripcion(nombreDelito,
                                    (descripciond != null) ? descripciond : "No hay descripción disponible");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

    }

    private void cargarPresosInactivos() {
        List<Object[]> presosInactivos = presoController.obtenerPresosInactivosParaTabla();
        DefaultTableModel model = (DefaultTableModel) tablaInactivos.getModel();
        model.setRowCount(0);

        for (Object[] fila : presosInactivos) {
            model.addRow(fila);
        }

    }



    private void cargarDatosPresoEnFormularioActualizacion(Preso preso) {
        this.delitosTemporales = new ArrayList<>();

        nuevoPrimerNombreField.setText("");
        nuevoPrimerApellidoField.setText("");
        nuevaEdadField.setText("");
        nuevoSegundoApellidoField.setText("");
        nuevaEstaturaField.setText("");
        nuevoPesoField.setText("");
        nuevaNacionalidadField.setSelectedIndex(0);
        nuevoGrupoSanguineoCombo.setSelectedIndex(0);
        nuevoNivelSeguridadCombo.setSelectedIndex(0);
        nuevoAislamientoCombo.setSelectedIndex(0);
        nuevoNivelRiesgoCombo.setSelectedIndex(0);

        cod.setText("");
        art.setText("");
        nuevoGrupoSanguineoCombo.setSelectedIndex(0);
        textAreaDescripcion1.setText("");

        nuevaFoto.setIcon(null);
        selectedImageFile = null;
    }

    private void actualizarEstadoBoton() {
        boolean camposPersonalesLlenos = !InputNombrePreso.getText().trim().isEmpty()
                && !InputApellidoPreso.getText().trim().isEmpty()
                && !InputEdadPreso.getText().trim().isEmpty()
                && !InputIdentificacionPreso.getText().trim().isEmpty()
                && !InputSexoPreso.getSelectedItem().toString().equals("Seleccione")
                && !InputNacionalidadPreso.getSelectedItem().toString().equals("Seleccione")
                && !InputEstaturaPreso.getText().trim().isEmpty()
                && !InputPesoPreso.getText().trim().isEmpty()
                && !TipoSangreCombobox.getSelectedItem().toString().equals("Seleccione")
                && lblFoto.getIcon() != null;

        boolean camposJudicialesLlenos = !seccion.getSelectedItem().toString().equals("<Seleccionar>")
                && !seguridad.getSelectedItem().toString().equals("<Seleccionar>")
                && !riesgo.getSelectedItem().toString().equals("<Seleccionar>")
                && !condicionComb.getSelectedItem().toString().equals("<Seleccione>");

        boolean delitosCompletos = !delitosTemporales.isEmpty()
                && delitosTemporales.size() >= (cantidadDelitos.getSelectedIndex() > 0
                ? Integer.parseInt(cantidadDelitos.getSelectedItem().toString()) : 1);

        AñadirPreso.setEnabled(camposPersonalesLlenos && camposJudicialesLlenos && delitosCompletos);
    }

    private void agregarValidacionInstantanea() {
        Runnable actualizarEstado = () -> {
            SwingUtilities.invokeLater(() -> {
                actualizarEstadoBoton();
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
            InputIdentificacionPreso, InputEstaturaPreso, InputPesoPreso,
            Codigo, ArticuloLey, DescripcionDelito
        };

        for (Component campo : camposTexto) {
            if (campo instanceof JTextComponent) {
                ((JTextComponent) campo).addKeyListener(keyListener);
            }
        }

        PropertyChangeListener propertyListener = evt -> actualizarEstado.run();

        FechaComision.getDateEditor().addPropertyChangeListener("date", propertyListener);

        ActionListener comboListener = e -> actualizarEstado.run();

        Component[] comboboxes = {
            TipoSangreCombobox, InputSexoPreso, riesgo,
            delito, seguridad, seccion, condicionComb, Gravedad,
            cantidadDelitos, InputNacionalidadPreso
        };

        for (Component combo : comboboxes) {
            if (combo instanceof JComboBox) {
                ((JComboBox<?>) combo).addActionListener(comboListener);
            }
        }

        AñoD.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        MesD.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
            }
        });
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

    private void limpiarCamposDelito() {
        delito.setSelectedIndex(0);
        Codigo.setText("");
        ArticuloLey.setText("");
        Gravedad.setSelectedIndex(0);
        FechaComision.setDate(null);
        DescripcionDelito.setText("");
        AñoD.setValue(0);
        MesD.setValue(0);
    }

    private void limpiarCamposPersonales() {
        InputNombrePreso.setText("");
        InputSegundoNombre.setText("");
        InputSegundoApellido.setText("");
        InputApellidoPreso.setText("");
        InputEdadPreso.setText("");
        InputIdentificacionPreso.setText("");
        InputSexoPreso.setSelectedItem(0);
        InputNacionalidadPreso.setSelectedItem(0);
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
        nuevaNacionalidadField.setSelectedIndex(0);
        nuevoGrupoSanguineoCombo.setSelectedIndex(0);

        nuevoNivelSeguridadCombo.setSelectedIndex(0);
        nuevoAislamientoCombo.setSelectedIndex(0);
        nuevoNivelRiesgoCombo.setSelectedIndex(0);

        nuevaFoto.setIcon(null);
        selectedImageFile = null;
        delitosTemporales.clear();
    }

    private void limpiarCamposJudiciales() {
        seccion.setSelectedIndex(0);
        seguridad.setSelectedIndex(0);
        riesgo.setSelectedIndex(0);
        condicionComb.setSelectedIndex(0);

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

    private void cargarTodosLosPresos() {
        try {
            DefaultTableModel modelo = (DefaultTableModel) TablaPresos.getModel();
            modelo.setRowCount(0);

            List<Object[]> datosPresos = presoController.obtenerTodosLosPresosParaTabla();

            for (Object[] fila : datosPresos) {
                modelo.addRow(fila);
            }

            TablaPresos.revalidate();
            TablaPresos.repaint();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los presos: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel22 = new javax.swing.JPanel();
        ppMenuTablaPresos = new javax.swing.JPopupMenu();
        ppMenuDelito = new javax.swing.JPopupMenu();
        ppTablaInactivos = new javax.swing.JPopupMenu();
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
        jSeparator8 = new javax.swing.JSeparator();
        Siguiente1 = new javax.swing.JButton();
        InputApellidoPreso = new javax.swing.JTextField();
        InputEdadPreso = new javax.swing.JTextField();
        InputIdentificacionPreso = new javax.swing.JTextField();
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
        InputNacionalidadPreso = new javax.swing.JComboBox<>();
        CancelarD3 = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        InformacionJudicialPanel = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jSeparator9 = new javax.swing.JSeparator();
        jSeparator10 = new javax.swing.JSeparator();
        jSeparator11 = new javax.swing.JSeparator();
        riesgo = new javax.swing.JComboBox<>();
        seguridad = new javax.swing.JComboBox<>();
        condicionComb = new javax.swing.JComboBox<>();
        seccion = new javax.swing.JComboBox<>();
        jLabel19 = new javax.swing.JLabel();
        jSeparator13 = new javax.swing.JSeparator();
        Siguiente2 = new javax.swing.JButton();
        cancelarD2 = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        btnRegresarAInfoGeneral = new javax.swing.JButton();
        PanelIngresarDelito = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        delito = new javax.swing.JComboBox<>();
        Gravedad = new javax.swing.JComboBox<>();
        cantidadDelitos = new javax.swing.JComboBox<>();
        jLabel28 = new javax.swing.JLabel();
        FechaComision = new com.toedter.calendar.JDateChooser();
        jLabel32 = new javax.swing.JLabel();
        Codigo = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        DescripcionDelito = new javax.swing.JTextArea();
        jLabel33 = new javax.swing.JLabel();
        guardarDelito = new javax.swing.JButton();
        jLabel105 = new javax.swing.JLabel();
        ArticuloLey = new javax.swing.JLabel();
        lblProgreso = new javax.swing.JLabel();
        AñoD = new com.toedter.components.JSpinField();
        MesD = new com.toedter.components.JSpinField();
        jLabel82 = new javax.swing.JLabel();
        jLabel83 = new javax.swing.JLabel();
        jLabel84 = new javax.swing.JLabel();
        cancelarD = new javax.swing.JButton();
        AñadirPreso = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        btnRegresarAJudicial = new javax.swing.JButton();
        ActualizarInformacionPreso = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jPanel33 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jPanel39 = new javax.swing.JPanel();
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
        jLabel72 = new javax.swing.JLabel();
        jSeparator56 = new javax.swing.JSeparator();
        jLabel88 = new javax.swing.JLabel();
        jSeparator59 = new javax.swing.JSeparator();
        jSeparator68 = new javax.swing.JSeparator();
        nuevoNivelSeguridadCombo = new javax.swing.JComboBox<>();
        nuevoAislamientoCombo = new javax.swing.JComboBox<>();
        nuevoNivelRiesgoCombo = new javax.swing.JComboBox<>();
        ActualizarFotoBoton = new javax.swing.JButton();
        nuevaFoto = new javax.swing.JLabel();
        jLabel74 = new javax.swing.JLabel();
        jSeparator29 = new javax.swing.JSeparator();
        nuevoPrimerNombreField = new javax.swing.JTextField();
        nuevoPrimerApellidoField = new javax.swing.JTextField();
        nuevaEdadField = new javax.swing.JTextField();
        nuevoSegundoApellidoField = new javax.swing.JTextField();
        nuevaEstaturaField = new javax.swing.JTextField();
        nuevoPesoField = new javax.swing.JTextField();
        jSeparator81 = new javax.swing.JSeparator();
        jLabel73 = new javax.swing.JLabel();
        jSeparator82 = new javax.swing.JSeparator();
        nuevoSegundoNombreField = new javax.swing.JTextField();
        nuevaNacionalidadField = new javax.swing.JComboBox<>();
        jPanel34 = new javax.swing.JPanel();
        jPanel38 = new javax.swing.JPanel();
        jLabel91 = new javax.swing.JLabel();
        jSeparator61 = new javax.swing.JSeparator();
        nuevaSeccion = new javax.swing.JComboBox<>();
        jLabel30 = new javax.swing.JLabel();
        actualizarPreso = new javax.swing.JButton();
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
        jButton3 = new javax.swing.JButton();
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
        jLabel95 = new javax.swing.JLabel();
        jSeparator73 = new javax.swing.JSeparator();
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
        cbxNacionalidad = new javax.swing.JComboBox<>();
        jLabel94 = new javax.swing.JLabel();
        btnActualilzarODR = new javax.swing.JButton();
        presosInactivos = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaInactivos = new javax.swing.JTable();
        jPanel15 = new RoundedPanel(30);
        ;
        jLabel79 = new javax.swing.JLabel();
        comboInactivos = new javax.swing.JComboBox<>();
        lblFallecidas = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jPanel16 = new RoundedPanel(30);
        txtBusquedPresosInactivos = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jLabel86 = new javax.swing.JLabel();
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
        jLabel62 = new javax.swing.JLabel();
        jSeparator53 = new javax.swing.JSeparator();
        jPanel35 = new RoundedPanel(32)
        ;
        jLabel85 = new javax.swing.JLabel();
        fotoPresoExpediente = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
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
        jPanel5 = new RoundedPanel(25);
        jLabel78 = new javax.swing.JLabel();
        sentenciaTotaal = new javax.swing.JLabel();
        jLabel81 = new javax.swing.JLabel();
        lblFechaSalida = new javax.swing.JLabel();
        panelEstadoEspecialPreso = new RoundedPanel(30);
        ;
        lblMensajeEspecialPreso = new javax.swing.JLabel();
        BtnExportarPDF = new javax.swing.JButton();
        btnVerIntentosFuga = new javax.swing.JButton();
        AñadirDelito = new javax.swing.JPanel();
        jPanel36 = new RoundedPanel(30);
        ;
        jPanel37 = new javax.swing.JPanel();
        jLabel80 = new javax.swing.JLabel();
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
        AñosS = new com.toedter.components.JSpinField();
        Meses = new com.toedter.components.JSpinField();
        jLabel124 = new javax.swing.JLabel();
        jLabel128 = new javax.swing.JLabel();
        jLabel129 = new javax.swing.JLabel();
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
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 120, 120, 20));

        jLabel6.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Edad*:");
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 190, 50, 20));

        jLabel7.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Identificación*:");
        jPanel3.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 190, 130, 20));

        jLabel8.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("Nacionalidad*:");
        jPanel3.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 260, 90, 20));

        jLabel9.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Genero:");
        jPanel3.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 350, 50, 20));

        jLabel10.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setText("Segundo nombre:");
        jPanel3.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 50, 120, 20));

        jLabel11.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setText("Estatura (m)*:");
        jPanel3.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 280, 90, 20));

        InputSexoPreso.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        InputSexoPreso.setForeground(new java.awt.Color(255, 255, 255));
        InputSexoPreso.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Seleccione>", "F", "M" }));
        jPanel3.add(InputSexoPreso, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 370, 140, 30));

        TipoSangreCombobox.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        TipoSangreCombobox.setForeground(new java.awt.Color(255, 255, 255));
        TipoSangreCombobox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Seleccione>", "A+", "A-", "O+", "O-", "B+", "B-", "AB+", "AB-" }));
        jPanel3.add(TipoSangreCombobox, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 370, 150, 30));

        jLabel12.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 0, 0));
        jLabel12.setText("Peso (kg)*:");
        jPanel3.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 280, 90, 20));

        jLabel13.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 0, 0));
        jLabel13.setText("Tipo sangre: ");
        jPanel3.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 350, 100, 20));

        jSeparator2.setForeground(new java.awt.Color(0, 0, 0));
        jPanel3.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 140, 110, 10));

        jSeparator3.setForeground(new java.awt.Color(0, 0, 0));
        jPanel3.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 210, 50, 10));

        jSeparator4.setForeground(new java.awt.Color(0, 0, 0));
        jPanel3.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 210, 110, 10));

        jSeparator5.setForeground(new java.awt.Color(0, 0, 0));
        jPanel3.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 280, 90, 10));

        jSeparator8.setForeground(new java.awt.Color(0, 0, 0));
        jPanel3.add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 70, 110, 10));

        Siguiente1.setBackground(new java.awt.Color(19, 65, 19));
        Siguiente1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Siguiente1.setForeground(new java.awt.Color(255, 255, 255));
        Siguiente1.setText("Siguiente");
        Siguiente1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Siguiente1ActionPerformed(evt);
            }
        });
        jPanel3.add(Siguiente1, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 430, 170, 40));

        InputApellidoPreso.setBackground(new java.awt.Color(204, 204, 204));
        InputApellidoPreso.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel3.add(InputApellidoPreso, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 150, 330, 30));

        InputEdadPreso.setBackground(new java.awt.Color(204, 204, 204));
        InputEdadPreso.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel3.add(InputEdadPreso, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 220, 330, 30));

        InputIdentificacionPreso.setBackground(new java.awt.Color(204, 204, 204));
        InputIdentificacionPreso.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        InputIdentificacionPreso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InputIdentificacionPresoActionPerformed(evt);
            }
        });
        jPanel3.add(InputIdentificacionPreso, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 220, 310, 30));

        InputEstaturaPreso.setBackground(new java.awt.Color(204, 204, 204));
        InputEstaturaPreso.setForeground(new java.awt.Color(0, 0, 0));
        InputEstaturaPreso.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel3.add(InputEstaturaPreso, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 300, 130, 30));

        InputPesoPreso.setBackground(new java.awt.Color(204, 204, 204));
        InputPesoPreso.setForeground(new java.awt.Color(0, 0, 0));
        InputPesoPreso.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel3.add(InputPesoPreso, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 300, 150, 30));

        InputNombrePreso.setBackground(new java.awt.Color(204, 204, 204));
        InputNombrePreso.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        InputNombrePreso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InputNombrePresoActionPerformed(evt);
            }
        });
        jPanel3.add(InputNombrePreso, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 80, 330, 30));

        btnIngresarFoto.setBackground(new java.awt.Color(102, 102, 102));
        btnIngresarFoto.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnIngresarFoto.setForeground(new java.awt.Color(255, 255, 255));
        btnIngresarFoto.setText("Ingresar foto");
        btnIngresarFoto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIngresarFotoActionPerformed(evt);
            }
        });
        jPanel3.add(btnIngresarFoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 340, 110, -1));

        jSeparator27.setForeground(new java.awt.Color(0, 0, 0));
        jPanel3.add(jSeparator27, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 140, 110, 10));

        jSeparator84.setForeground(new java.awt.Color(0, 0, 0));
        jPanel3.add(jSeparator84, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 70, 100, 10));

        jLabel110.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel110.setForeground(new java.awt.Color(0, 0, 0));
        jLabel110.setText("Primer nombre*:");
        jPanel3.add(jLabel110, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 50, 100, 20));

        jLabel111.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel111.setForeground(new java.awt.Color(0, 0, 0));
        jLabel111.setText("Primer apellido*:");
        jPanel3.add(jLabel111, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 120, 110, 20));

        InputSegundoApellido.setBackground(new java.awt.Color(204, 204, 204));
        InputSegundoApellido.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        InputSegundoApellido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InputSegundoApellidoActionPerformed(evt);
            }
        });
        jPanel3.add(InputSegundoApellido, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 150, 310, 30));

        InputSegundoNombre.setBackground(new java.awt.Color(204, 204, 204));
        InputSegundoNombre.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        InputSegundoNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InputSegundoNombreActionPerformed(evt);
            }
        });
        jPanel3.add(InputSegundoNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 80, 310, 30));

        lblFoto.setBackground(new java.awt.Color(129, 129, 164));
        lblFoto.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));
        jPanel3.add(lblFoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 370, 90, 90));

        InputNacionalidadPreso.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        InputNacionalidadPreso.setForeground(new java.awt.Color(255, 255, 255));
        InputNacionalidadPreso.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Seleccione>", "Afgana", "Alemana", "Americana", "Andorrana", "Angoleña", "Antiguana", "Árabe Saudita", "Argelina", "Argentina", "Armenia", "Arubeña", "Australiana", "Austriaca", "Azerbaiyana", "Bahameña", "Bahreiní", "Bangladesí", "Barbadense", "Belga", "Beliceña", "Beninesa", "Bermudeña", "Bielorrusa", "Birmana", "Boliviana", "Bosnia", "Botsuana", "Brasileña", "Británica", "Bruneana", "Búlgara", "Burkinesa", "Burundesa", "Butanesa", "Cabo Verdiana", "Camboyana", "Camerunesa", "Canadiense", "Catari", "Centroafricana", "Chadiana", "Checa", "Chilena", "China", "Chipriota", "Colombiana", "Comorense", "Congoleña", "Costarricense", "Croata", "Cubana", "Danesa", "Dominicana", "Ecuatoriana", "Egipcia", "Emiratí", "Eritrea", "Eslovaca", "Eslovena", "Española", "Estadounidense", "Estonia", "Etíope", "Filipina", "Finlandesa", "Fiyiana", "Francesa", "Gabonesa", "Galesa", "Gambiana", "Georgiana", "Ghanesa", "Gibraltareña", "Granadina", "Griega", "Guatemalteca", "Guineana", "Guineana-Bisáu", "Guineana Ecuatorial", "Guyanesa", "Haitiana", "Hondureña", "Hongkonesa", "Húngara", "India", "Indonesa", "Iraní", "Iraquí", "Irlandesa", "Islandesa", "Israelí", "Italiana", "Jamaicana", "Japonesa", "Jordana", "Kazaja", "Keniata", "Kirguisa", "Kiribatiana", "Kuwaití", "Laosiana", "Lesotense", "Letona", "Libanesa", "Liberiana", "Libia", "Liechtensteiniana", "Lituana", "Luxemburguesa", "Macedonia", "Malasia", "Malauí", "Maldiva", "Malgache", "Maliense", "Maltesa", "Marfileña", "Marroquí", "Marshallesa", "Mauriciana", "Mauritana", "Mexicana", "Micronesia", "Moldava", "Monegasca", "Mongola", "Montenegrina", "Mozambiqueña", "Namibia", "Nauruana", "Nepalí", "Nicaragüense", "Nigeriana", "Nigerina", "Norcoreana", "Noruega", "Neozelandesa", "Omana", "Neerlandesa (Holandesa)", "Paquistaní", "Palaosiana", "Panameña", "Papú", "Paraguaya", "Peruana", "Polaca", "Portuguesa", "Puertorriqueña", "Ruandesa", "Rumana", "Rusa", "Saharaui", "Salomonense", "Salvadoreña", "Samoana", "Sanmarinense", "Santotomense", "Saudí", "Senegalesa", "Serbia", "Seychellense", "Sierraleonesa", "Singapurense", "Siria", "Somalí", "Sri Lanka", "Sudafricana", "Sudanesa", "Sueca", "Suiza", "Surcoreana", "Surinamense", "Suazi", "Tailandesa", "Taiwanesa", "Tayika", "Tanzana", "Timorense", "Togolesa", "Tongana", "Trinitense", "Tunecina", "Turca", "Turkmena", "Tuvaluana", "Ucraniana", "Ugandesa", "Uruguaya", "Uzbeca", "Vanuatuense", "Venezolana", "Vietnamita", "Yemení", "Yibutiana", "Zambiana", "Zimbabuense" }));
        jPanel3.add(InputNacionalidadPreso, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 290, 300, 40));

        InformacionGeneralPanel.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 30, 890, 480));

        CancelarD3.setBackground(new java.awt.Color(66, 11, 11));
        CancelarD3.setForeground(new java.awt.Color(255, 255, 255));
        CancelarD3.setText("Cancelar");
        CancelarD3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelarD3ActionPerformed(evt);
            }
        });
        InformacionGeneralPanel.add(CancelarD3, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 30, -1, -1));

        jLabel14.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(102, 0, 0));
        jLabel14.setText("Todos los campos son obligarorios*");
        InformacionGeneralPanel.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 10, 200, -1));

        TabbedAñadirInformacionGeneral.addTab("Añadir Informacion General", InformacionGeneralPanel);

        InformacionJudicialPanel.setBackground(new java.awt.Color(255, 255, 255));
        InformacionJudicialPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel7.setBackground(new java.awt.Color(180, 180, 195));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel8.setBackground(new java.awt.Color(29, 35, 51));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel7.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 790, 30));

        jPanel9.setBackground(new java.awt.Color(29, 35, 51));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel7.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 450, 780, 10));

        jLabel15.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 0, 0));
        jLabel15.setText("Nivel de riesgo:");
        jPanel7.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 160, 130, 30));

        jLabel16.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 0, 0));
        jLabel16.setText("Nivel de seguridad:");
        jPanel7.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 230, 130, 30));

        jLabel17.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 0, 0));
        jLabel17.setText("Condición:");
        jPanel7.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 300, 130, 30));

        jSeparator9.setForeground(new java.awt.Color(0, 0, 0));
        jPanel7.add(jSeparator9, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 190, 110, 20));

        jSeparator10.setForeground(new java.awt.Color(0, 0, 0));
        jPanel7.add(jSeparator10, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 260, 110, 20));

        jSeparator11.setForeground(new java.awt.Color(0, 0, 0));
        jPanel7.add(jSeparator11, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 330, 110, 20));

        riesgo.setBackground(new java.awt.Color(51, 51, 51));
        riesgo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Seleccionar>", "Riesgo bajo", "Riesgo medio", "Riesgo alto" }));
        jPanel7.add(riesgo, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 150, 150, 40));

        seguridad.setBackground(new java.awt.Color(51, 51, 51));
        seguridad.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Seleccionar>", "Bajo", "Medio", "Alto" }));
        jPanel7.add(seguridad, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 220, 150, 40));

        condicionComb.setBackground(new java.awt.Color(51, 51, 51));
        condicionComb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Seleccione>", "Condenado", "Condenado en traslado" }));
        jPanel7.add(condicionComb, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 290, 150, 40));

        seccion.setBackground(new java.awt.Color(51, 51, 51));
        seccion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Seleccionar>", "Sección A", "Sección B", "Sección C" }));
        jPanel7.add(seccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 80, 150, 40));

        jLabel19.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(0, 0, 0));
        jLabel19.setText("Asignar sección:");
        jPanel7.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 90, 130, 30));

        jSeparator13.setForeground(new java.awt.Color(0, 0, 0));
        jPanel7.add(jSeparator13, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 120, 110, 10));

        Siguiente2.setBackground(new java.awt.Color(7, 56, 7));
        Siguiente2.setText("Siguiente");
        Siguiente2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Siguiente2ActionPerformed(evt);
            }
        });
        jPanel7.add(Siguiente2, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 380, 130, 30));

        cancelarD2.setBackground(new java.awt.Color(51, 0, 0));
        cancelarD2.setForeground(new java.awt.Color(255, 255, 255));
        cancelarD2.setText("Cancelar");
        cancelarD2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarD2ActionPerformed(evt);
            }
        });
        jPanel7.add(cancelarD2, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 380, 130, 30));

        InformacionJudicialPanel.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 50, 510, 460));

        jLabel27.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(102, 0, 0));
        jLabel27.setText("Todos los campos son obligarorios*");
        InformacionJudicialPanel.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 10, 200, -1));

        btnRegresarAInfoGeneral.setBackground(new java.awt.Color(51, 0, 0));
        btnRegresarAInfoGeneral.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        btnRegresarAInfoGeneral.setForeground(new java.awt.Color(255, 255, 255));
        btnRegresarAInfoGeneral.setText("<---");
        btnRegresarAInfoGeneral.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegresarAInfoGeneralActionPerformed(evt);
            }
        });
        InformacionJudicialPanel.add(btnRegresarAInfoGeneral, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 60, -1));

        TabbedAñadirInformacionGeneral.addTab("Añadir  Informacion Judicial", InformacionJudicialPanel);

        PanelIngresarDelito.setBackground(new java.awt.Color(255, 255, 255));
        PanelIngresarDelito.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel10.setBackground(new java.awt.Color(204, 204, 204));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel11.setBackground(new java.awt.Color(29, 35, 51));
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel10.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 960, 10));

        jPanel12.setBackground(new java.awt.Color(29, 35, 51));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel10.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 460, 960, 10));

        jLabel24.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(0, 0, 0));
        jLabel24.setText("Mes");
        jPanel10.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 330, 50, 30));

        jLabel25.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(0, 0, 0));
        jLabel25.setText("Articulo:");
        jPanel10.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 250, 70, 30));

        jLabel26.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(0, 0, 0));
        jLabel26.setText("Gravedad:");
        jPanel10.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 160, 70, 20));

        delito.setBackground(new java.awt.Color(51, 51, 51));
        delito.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Seleccionar>", "Injuria", "Calumnia", "Daño en bien ajeno", "Violación de habitación ajena", "Inasistencia alimentaria", "Omisión de socorro", "Lesiones personales leves", "Falsedad en documento privado", "Usurpación de derechos", "Uso de documento falso", "Abuso de confianza", "Hurto simple", "Receptación", "Estafa", "Violación de cerraduras o sellos", "Fraude", "Violación de medidas sanitarias", "Invasión de tierras o edificaciones", "Suplantación de identidad", "Contrabando", "Hurto calificado", "Lesiones personales graves", "Extorsión", "Falsedad en documento público", "Lavado de activos", "Peculado por uso", "Violencia intrafamiliar", "Acoso sexual", "Acceso abusivo a sistema informático", "Suplantación en medios electrónicos", "Daño informático", "Tráfico de influencias", "Porte ilegal de armas", "Cohecho", "Concusión", "Prevaricato", "Abuso de autoridad", "Perturbación del orden público", "Enriquecimiento ilícito", "Tráfico de fauna o flora silvestre", "Minería ilegal", "Hurto agravado", "Homicidio culposo", "Acceso carnal abusivo con menor de 14 años", "Actos sexuales con menor de 14 años", "Acceso carnal violento", "Acto sexual violento", "Violación", "Secuestro simple", "Tráfico de estupefacientes", "Fabricación o porte de estupefacientes", "Concierto para delinquir", "Homicidio", "Homicidio agravado", "Tortura", "Desaparición forzada", "Terrorismo", "Rebelión", "Genocidio", "Crímenes de lesa humanidad" }));
        jPanel10.add(delito, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 100, 280, 40));

        Gravedad.setBackground(new java.awt.Color(51, 51, 51));
        Gravedad.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Seleccionar>", "Baja", "Media", "Alta" }));
        jPanel10.add(Gravedad, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 150, 280, 40));

        cantidadDelitos.setBackground(new java.awt.Color(51, 51, 51));
        cantidadDelitos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Seleccionar>", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30" }));
        cantidadDelitos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cantidadDelitosActionPerformed(evt);
            }
        });
        jPanel10.add(cantidadDelitos, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 50, 210, 30));

        jLabel28.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(0, 0, 0));
        jLabel28.setText("Cantidad de delitos");
        jPanel10.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 60, 130, 20));
        jPanel10.add(FechaComision, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 70, 310, 30));

        jLabel32.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(0, 0, 0));
        jLabel32.setText("Descripcion delito:");
        jPanel10.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 120, 120, 30));

        Codigo.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        Codigo.setForeground(new java.awt.Color(0, 0, 0));
        Codigo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanel10.add(Codigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 210, 280, 30));

        DescripcionDelito.setColumns(20);
        DescripcionDelito.setRows(5);
        jScrollPane2.setViewportView(DescripcionDelito);

        jPanel10.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 150, 330, 100));

        jLabel33.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(0, 0, 0));
        jLabel33.setText("Sentencia por delito:");
        jPanel10.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 290, 140, 30));

        guardarDelito.setBackground(new java.awt.Color(0, 0, 51));
        guardarDelito.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        guardarDelito.setForeground(new java.awt.Color(255, 255, 255));
        guardarDelito.setText("Guardar delito");
        guardarDelito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarDelitoActionPerformed(evt);
            }
        });
        jPanel10.add(guardarDelito, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 270, 150, 30));

        jLabel105.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel105.setForeground(new java.awt.Color(0, 0, 0));
        jLabel105.setText("Codigo:");
        jPanel10.add(jLabel105, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 210, 100, 20));

        ArticuloLey.setBackground(new java.awt.Color(204, 204, 204));
        ArticuloLey.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        ArticuloLey.setForeground(new java.awt.Color(0, 0, 0));
        ArticuloLey.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        jPanel10.add(ArticuloLey, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 250, 280, 30));

        lblProgreso.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        lblProgreso.setForeground(new java.awt.Color(0, 0, 0));
        jPanel10.add(lblProgreso, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 10, 260, 20));
        jPanel10.add(AñoD, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 330, 90, -1));
        jPanel10.add(MesD, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 330, 80, -1));

        jLabel82.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel82.setForeground(new java.awt.Color(0, 0, 0));
        jLabel82.setText("Fecha comisión:");
        jPanel10.add(jLabel82, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 40, 120, 30));

        jLabel83.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel83.setForeground(new java.awt.Color(0, 0, 0));
        jLabel83.setText("Delito:");
        jPanel10.add(jLabel83, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 110, 60, 20));

        jLabel84.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel84.setForeground(new java.awt.Color(0, 0, 0));
        jLabel84.setText("Año");
        jPanel10.add(jLabel84, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 330, 50, 30));

        cancelarD.setBackground(new java.awt.Color(51, 0, 0));
        cancelarD.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        cancelarD.setForeground(new java.awt.Color(255, 255, 255));
        cancelarD.setText("Cancelar");
        cancelarD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarDActionPerformed(evt);
            }
        });
        jPanel10.add(cancelarD, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 270, 160, 30));

        AñadirPreso.setBackground(new java.awt.Color(7, 56, 7));
        AñadirPreso.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        AñadirPreso.setForeground(new java.awt.Color(255, 255, 255));
        AñadirPreso.setText("Finalizar y añadir preso");
        AñadirPreso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AñadirPresoActionPerformed(evt);
            }
        });
        jPanel10.add(AñadirPreso, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 410, 170, 40));

        PanelIngresarDelito.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 40, 960, 470));

        jLabel29.setFont(new java.awt.Font("Arial", 2, 12)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(102, 0, 0));
        jLabel29.setText("Todos los campos son obligarorios*");
        PanelIngresarDelito.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 10, 200, -1));

        btnRegresarAJudicial.setBackground(new java.awt.Color(51, 0, 0));
        btnRegresarAJudicial.setFont(new java.awt.Font("Arial Black", 1, 14)); // NOI18N
        btnRegresarAJudicial.setForeground(new java.awt.Color(255, 255, 255));
        btnRegresarAJudicial.setText("<---");
        btnRegresarAJudicial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegresarAJudicialActionPerformed(evt);
            }
        });
        PanelIngresarDelito.add(btnRegresarAJudicial, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 60, 30));

        TabbedAñadirInformacionGeneral.addTab("TabbedAñadirInformacionGeneral", PanelIngresarDelito);

        ActualizarInformacionPreso.setBackground(new java.awt.Color(255, 255, 255));
        ActualizarInformacionPreso.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel13.setBackground(new java.awt.Color(204, 204, 204));
        jPanel13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel13.add(jPanel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 20, 10, 430));

        jPanel17.setBackground(new java.awt.Color(29, 35, 51));

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1060, Short.MAX_VALUE)
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );

        jPanel13.add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(-30, 430, 1060, 20));

        jPanel39.setBackground(new java.awt.Color(17, 17, 42));

        javax.swing.GroupLayout jPanel39Layout = new javax.swing.GroupLayout(jPanel39);
        jPanel39.setLayout(jPanel39Layout);
        jPanel39Layout.setHorizontalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel39Layout.setVerticalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel13.add(jPanel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 20, 20, 430));

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
        jPanel13.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 390, 130, 20));

        jLabel35.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(0, 0, 0));
        jLabel35.setText("Actualizar datos judiciales");
        jPanel13.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 30, 190, -1));

        jLabel36.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(0, 0, 0));
        jLabel36.setText("Actualizar datos generales");
        jPanel13.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 30, 200, -1));

        jLabel37.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(0, 0, 0));
        jLabel37.setText("Segundo nombre:");
        jPanel13.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 110, 130, -1));

        jLabel38.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(0, 0, 0));
        jLabel38.setText("Primer apellido:");
        jPanel13.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 150, 110, -1));

        jLabel39.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(0, 0, 0));
        jLabel39.setText("Edad:");
        jPanel13.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 230, 90, -1));

        jLabel40.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(0, 0, 0));
        jLabel40.setText("Segundo apellido:");
        jPanel13.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 190, 120, -1));

        jLabel41.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(0, 0, 0));
        jLabel41.setText("Estatura:");
        jPanel13.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 270, 90, -1));

        jLabel42.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(0, 0, 0));
        jLabel42.setText("Peso:");
        jPanel13.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 310, 90, 20));

        jLabel43.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(0, 0, 0));
        jLabel43.setText("Nacionalidad:");
        jPanel13.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 350, 90, 20));

        jSeparator19.setForeground(new java.awt.Color(0, 0, 0));
        jPanel13.add(jSeparator19, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 210, 110, 10));

        jSeparator24.setForeground(new java.awt.Color(0, 0, 0));
        jPanel13.add(jSeparator24, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 290, 60, 10));

        jSeparator25.setForeground(new java.awt.Color(0, 0, 0));
        jPanel13.add(jSeparator25, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 330, 50, 10));

        jSeparator26.setForeground(new java.awt.Color(0, 0, 0));
        jPanel13.add(jSeparator26, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 370, 90, 10));

        jSeparator28.setForeground(new java.awt.Color(0, 0, 0));
        jPanel13.add(jSeparator28, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 170, 100, 10));

        nuevoGrupoSanguineoCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Seleccionar>", "A+", "A-", "O+", "O-", "B+", "B-", "AB+", "AB-" }));
        jPanel13.add(nuevoGrupoSanguineoCombo, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 380, -1, 30));

        jLabel72.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel72.setForeground(new java.awt.Color(0, 0, 0));
        jLabel72.setText("Nivel de seguridad:");
        jPanel13.add(jLabel72, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 90, 130, -1));

        jSeparator56.setForeground(new java.awt.Color(0, 0, 0));
        jPanel13.add(jSeparator56, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 110, 130, 10));

        jLabel88.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel88.setForeground(new java.awt.Color(0, 0, 0));
        jLabel88.setText("En aislamiento:");
        jPanel13.add(jLabel88, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 150, -1, 20));

        jSeparator59.setForeground(new java.awt.Color(0, 0, 0));
        jPanel13.add(jSeparator59, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 270, 110, 10));

        jSeparator68.setForeground(new java.awt.Color(0, 0, 0));
        jPanel13.add(jSeparator68, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 170, 110, 10));

        nuevoNivelSeguridadCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Seleccionar>", "Baja", "Media", "Alta" }));
        nuevoNivelSeguridadCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevoNivelSeguridadComboActionPerformed(evt);
            }
        });
        jPanel13.add(nuevoNivelSeguridadCombo, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 80, 220, 30));

        nuevoAislamientoCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Seleccionar>", "Sí", "No" }));
        jPanel13.add(nuevoAislamientoCombo, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 140, 220, 30));

        nuevoNivelRiesgoCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Seleccionar>", "Riesgo bajo", "Riesgo medio", "Riesgo alto" }));
        nuevoNivelRiesgoCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevoNivelRiesgoComboActionPerformed(evt);
            }
        });
        jPanel13.add(nuevoNivelRiesgoCombo, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 200, 220, 30));

        ActualizarFotoBoton.setText("Actualizar foto");
        ActualizarFotoBoton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ActualizarFotoBotonActionPerformed(evt);
            }
        });
        jPanel13.add(ActualizarFotoBoton, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 350, -1, -1));

        nuevaFoto.setBackground(new java.awt.Color(255, 255, 255));
        nuevaFoto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        jPanel13.add(nuevaFoto, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 310, 150, 110));

        jLabel74.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel74.setForeground(new java.awt.Color(0, 0, 0));
        jLabel74.setText("Sección:");
        jPanel13.add(jLabel74, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 250, 110, 20));

        jSeparator29.setForeground(new java.awt.Color(0, 0, 0));
        jPanel13.add(jSeparator29, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 250, 50, 10));

        nuevoPrimerNombreField.setBackground(new java.awt.Color(255, 255, 255));
        nuevoPrimerNombreField.setForeground(new java.awt.Color(0, 0, 0));
        nuevoPrimerNombreField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        nuevoPrimerNombreField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevoPrimerNombreFieldActionPerformed(evt);
            }
        });
        jPanel13.add(nuevoPrimerNombreField, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 60, 200, 30));

        nuevoPrimerApellidoField.setBackground(new java.awt.Color(255, 255, 255));
        nuevoPrimerApellidoField.setForeground(new java.awt.Color(0, 0, 0));
        nuevoPrimerApellidoField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel13.add(nuevoPrimerApellidoField, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 140, 200, 30));

        nuevaEdadField.setBackground(new java.awt.Color(255, 255, 255));
        nuevaEdadField.setForeground(new java.awt.Color(0, 0, 0));
        nuevaEdadField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        nuevaEdadField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevaEdadFieldActionPerformed(evt);
            }
        });
        jPanel13.add(nuevaEdadField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 220, 250, 30));

        nuevoSegundoApellidoField.setBackground(new java.awt.Color(255, 255, 255));
        nuevoSegundoApellidoField.setForeground(new java.awt.Color(0, 0, 0));
        nuevoSegundoApellidoField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        nuevoSegundoApellidoField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevoSegundoApellidoFieldActionPerformed(evt);
            }
        });
        jPanel13.add(nuevoSegundoApellidoField, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 180, 190, 30));

        nuevaEstaturaField.setBackground(new java.awt.Color(255, 255, 255));
        nuevaEstaturaField.setForeground(new java.awt.Color(0, 0, 0));
        nuevaEstaturaField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel13.add(nuevaEstaturaField, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 260, 240, 30));

        nuevoPesoField.setBackground(new java.awt.Color(255, 255, 255));
        nuevoPesoField.setForeground(new java.awt.Color(0, 0, 0));
        nuevoPesoField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        nuevoPesoField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevoPesoFieldActionPerformed(evt);
            }
        });
        jPanel13.add(nuevoPesoField, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 300, 250, 30));

        jSeparator81.setForeground(new java.awt.Color(0, 0, 0));
        jPanel13.add(jSeparator81, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 130, 110, 10));

        jLabel73.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel73.setForeground(new java.awt.Color(0, 0, 0));
        jLabel73.setText("Primer nombre:");
        jPanel13.add(jLabel73, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 70, 110, -1));

        jSeparator82.setForeground(new java.awt.Color(0, 0, 0));
        jPanel13.add(jSeparator82, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 90, 100, 10));

        nuevoSegundoNombreField.setBackground(new java.awt.Color(255, 255, 255));
        nuevoSegundoNombreField.setForeground(new java.awt.Color(0, 0, 0));
        nuevoSegundoNombreField.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        nuevoSegundoNombreField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevoSegundoNombreFieldActionPerformed(evt);
            }
        });
        jPanel13.add(nuevoSegundoNombreField, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 100, 180, 30));

        nuevaNacionalidadField.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Seleccionar>", "Afgana", "Alemana", "Americana", "Andorrana", "Angoleña", "Antiguana", "Árabe Saudita", "Argelina", "Argentina", "Armenia", "Arubeña", "Australiana", "Austriaca", "Azerbaiyana", "Bahameña", "Bahreiní", "Bangladesí", "Barbadense", "Belga", "Beliceña", "Beninesa", "Bermudeña", "Bielorrusa", "Birmana", "Boliviana", "Bosnia", "Botsuana", "Brasileña", "Británica", "Bruneana", "Búlgara", "Burkinesa", "Burundesa", "Butanesa", "Cabo Verdiana", "Camboyana", "Camerunesa", "Canadiense", "Catari", "Centroafricana", "Chadiana", "Checa", "Chilena", "China", "Chipriota", "Colombiana", "Comorense", "Congoleña", "Costarricense", "Croata", "Cubana", "Danesa", "Dominicana", "Ecuatoriana", "Egipcia", "Emiratí", "Eritrea", "Eslovaca", "Eslovena", "Española", "Estadounidense", "Estonia", "Etíope", "Filipina", "Finlandesa", "Fiyiana", "Francesa", "Gabonesa", "Galesa", "Gambiana", "Georgiana", "Ghanesa", "Gibraltareña", "Granadina", "Griega", "Guatemalteca", "Guineana", "Guineana-Bisáu", "Guineana Ecuatorial", "Guyanesa", "Haitiana", "Hondureña", "Hongkonesa", "Húngara", "India", "Indonesa", "Iraní", "Iraquí", "Irlandesa", "Islandesa", "Israelí", "Italiana", "Jamaicana", "Japonesa", "Jordana", "Kazaja", "Keniata", "Kirguisa", "Kiribatiana", "Kuwaití", "Laosiana", "Lesotense", "Letona", "Libanesa", "Liberiana", "Libia", "Liechtensteiniana", "Lituana", "Luxemburguesa", "Macedonia", "Malasia", "Malauí", "Maldiva", "Malgache", "Maliense", "Maltesa", "Marfileña", "Marroquí", "Marshallesa", "Mauriciana", "Mauritana", "Mexicana", "Micronesia", "Moldava", "Monegasca", "Mongola", "Montenegrina", "Mozambiqueña", "Namibia", "Nauruana", "Nepalí", "Nicaragüense", "Nigeriana", "Nigerina", "Norcoreana", "Noruega", "Neozelandesa", "Omana", "Neerlandesa (Holandesa)", "Paquistaní", "Palaosiana", "Panameña", "Papú", "Paraguaya", "Peruana", "Polaca", "Portuguesa", "Puertorriqueña", "Ruandesa", "Rumana", "Rusa", "Saharaui", "Salomonense", "Salvadoreña", "Samoana", "Sanmarinense", "Santotomense", "Saudí", "Senegalesa", "Serbia", "Seychellense", "Sierraleonesa", "Singapurense", "Siria", "Somalí", "Sri Lanka", "Sudafricana", "Sudanesa", "Sueca", "Suiza", "Surcoreana", "Surinamense", "Suazi", "Tailandesa", "Taiwanesa", "Tayika", "Tanzana", "Timorense", "Togolesa", "Tongana", "Trinitense", "Tunecina", "Turca", "Turkmena", "Tuvaluana", "Ucraniana", "Ugandesa", "Uruguaya", "Uzbeca", "Vanuatuense", "Venezolana", "Vietnamita", "Yemení", "Yibutiana", "Zambiana", "Zimbabuense" }));
        jPanel13.add(nuevaNacionalidadField, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 340, 210, 30));

        javax.swing.GroupLayout jPanel34Layout = new javax.swing.GroupLayout(jPanel34);
        jPanel34.setLayout(jPanel34Layout);
        jPanel34Layout.setHorizontalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel34Layout.setVerticalGroup(
            jPanel34Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel13.add(jPanel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jPanel38.setBackground(new java.awt.Color(17, 17, 42));

        javax.swing.GroupLayout jPanel38Layout = new javax.swing.GroupLayout(jPanel38);
        jPanel38.setLayout(jPanel38Layout);
        jPanel38Layout.setHorizontalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel38Layout.setVerticalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel13.add(jPanel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 20, 20, 430));

        jLabel91.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel91.setForeground(new java.awt.Color(0, 0, 0));
        jLabel91.setText("Nivel de riesgo:");
        jPanel13.add(jLabel91, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 210, 110, 20));

        jSeparator61.setForeground(new java.awt.Color(0, 0, 0));
        jPanel13.add(jSeparator61, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 230, 110, 10));

        nuevaSeccion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Seleccionar>", "Sección A", "Sección B", "Sección C" }));
        jPanel13.add(nuevaSeccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 250, 220, 30));

        ActualizarInformacionPreso.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 1030, 450));

        jLabel30.setFont(new java.awt.Font("Arial", 2, 14)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(102, 0, 0));
        jLabel30.setText("Los campos que no desee actualizar déjelos en blanco");
        ActualizarInformacionPreso.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 10, 370, 20));

        actualizarPreso.setBackground(new java.awt.Color(29, 35, 51));
        actualizarPreso.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        actualizarPreso.setForeground(new java.awt.Color(255, 255, 255));
        actualizarPreso.setText("Actualizar Preso");
        actualizarPreso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                actualizarPresoActionPerformed(evt);
            }
        });
        ActualizarInformacionPreso.add(actualizarPreso, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 490, 170, 40));

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
        jLabel45.setText("Apellidos:");
        jPanel19.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 100, 140, -1));

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
        jLabel54.setText("Nombres:");
        jPanel19.add(jLabel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 60, 150, -1));

        jSeparator34.setForeground(new java.awt.Color(0, 0, 0));
        jPanel19.add(jSeparator34, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 80, 410, 10));

        jSeparator36.setForeground(new java.awt.Color(0, 0, 0));
        jPanel19.add(jSeparator36, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 120, 410, 10));

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
        jPanel19.add(ApellidoODR, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 90, 260, 30));

        IdentificacionODR.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        IdentificacionODR.setForeground(new java.awt.Color(0, 0, 0));
        jPanel19.add(IdentificacionODR, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 130, 280, 30));

        EdadODR.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        EdadODR.setForeground(new java.awt.Color(0, 0, 0));
        jPanel19.add(EdadODR, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 170, 330, 30));

        SexoODR.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        SexoODR.setForeground(new java.awt.Color(0, 0, 0));
        jPanel19.add(SexoODR, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 210, 330, 30));

        NacionalidadODR.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        NacionalidadODR.setForeground(new java.awt.Color(0, 0, 0));
        jPanel19.add(NacionalidadODR, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 250, 280, 30));

        nombreODR.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        nombreODR.setForeground(new java.awt.Color(0, 0, 0));
        jPanel19.add(nombreODR, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 50, 260, 30));

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
        jPanel18.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 330, 130, -1));
        jPanel18.add(LabelNumeroPlaca, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 330, 80, 20));

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
        jPanel18.add(jSeparator41, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 350, 220, 10));

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

        DatosPersonalesPreso.setBackground(new java.awt.Color(255, 255, 255));
        DatosPersonalesPreso.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel68.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        jLabel68.setForeground(new java.awt.Color(0, 0, 0));
        jLabel68.setText("DATOS PERSONALES");
        DatosPersonalesPreso.add(jLabel68, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 80, -1, -1));

        jLabel69.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel69.setForeground(new java.awt.Color(0, 0, 0));
        jLabel69.setText("Nombres:");
        DatosPersonalesPreso.add(jLabel69, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 150, 140, 20));

        jSeparator54.setForeground(new java.awt.Color(0, 0, 0));
        DatosPersonalesPreso.add(jSeparator54, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 170, 400, 10));

        jLabel70.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel70.setForeground(new java.awt.Color(0, 0, 0));
        jLabel70.setText("Apellidos:");
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
        DatosPersonalesPreso.add(jLabel93, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 60, 480, 460));

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
        TablaPresos.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        TablaPresos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Foto", "Id", "Nombres", "Apellidos", "Edad", "Identificación", "Nacionalidad", "Seccion", "Celda", "Estado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
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
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 30, 90, 30));

        SelectorSeccion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sección A", "Sección B", "Sección C" }));
        SelectorSeccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SelectorSeccionActionPerformed(evt);
            }
        });
        jPanel2.add(SelectorSeccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 30, 140, 30));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 330, 80));

        btnRestaurarTabla.setBackground(new java.awt.Color(29, 35, 51));
        btnRestaurarTabla.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnRestaurarTabla.setForeground(new java.awt.Color(255, 255, 255));
        btnRestaurarTabla.setText("Actualizar tabla");
        btnRestaurarTabla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRestaurarTablaActionPerformed(evt);
            }
        });
        jPanel1.add(btnRestaurarTabla, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 10, 150, 30));

        jButton3.setBackground(new java.awt.Color(0, 0, 0));
        jButton3.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Ver presos inactivos");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 40, 150, 30));

        PanelTablaPresoBase.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 90, 1010, 80));
        PanelTablaPresoBase.add(identificacionB, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 20, 590, 40));

        btnBuscarIdentificacion.setBackground(new java.awt.Color(25, 25, 47));
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
        jPanel27.add(jPanel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 440, 960, 10));

        jLabel98.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel98.setForeground(new java.awt.Color(0, 0, 0));
        jLabel98.setText("DATOS PERSONALES");
        jPanel27.add(jLabel98, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 60, -1, -1));

        jLabel99.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel99.setForeground(new java.awt.Color(0, 0, 0));
        jLabel99.setText("Segundo nombre:");
        jPanel27.add(jLabel99, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 160, -1, -1));

        jSeparator69.setForeground(new java.awt.Color(0, 0, 0));
        jPanel27.add(jSeparator69, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 180, 350, 10));

        nuevoPrimerNombre.setBackground(new java.awt.Color(204, 204, 204));
        nuevoPrimerNombre.setForeground(new java.awt.Color(0, 0, 0));
        nuevoPrimerNombre.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        nuevoPrimerNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevoPrimerNombreActionPerformed(evt);
            }
        });
        jPanel27.add(nuevoPrimerNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 110, 230, 30));

        jLabel100.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel100.setForeground(new java.awt.Color(0, 0, 0));
        jLabel100.setText("Segundo apellido:");
        jPanel27.add(jLabel100, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 240, -1, -1));

        jSeparator70.setForeground(new java.awt.Color(0, 0, 0));
        jPanel27.add(jSeparator70, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 260, 350, 10));

        primerNuevoApellido.setBackground(new java.awt.Color(204, 204, 204));
        primerNuevoApellido.setForeground(new java.awt.Color(0, 0, 0));
        primerNuevoApellido.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        primerNuevoApellido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                primerNuevoApellidoActionPerformed(evt);
            }
        });
        jPanel27.add(primerNuevoApellido, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 190, 230, 30));

        nuevaEdad.setBackground(new java.awt.Color(204, 204, 204));
        nuevaEdad.setForeground(new java.awt.Color(0, 0, 0));
        nuevaEdad.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel27.add(nuevaEdad, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 270, 280, 30));

        jLabel101.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel101.setForeground(new java.awt.Color(0, 0, 0));
        jLabel101.setText("Edad: ");
        jPanel27.add(jLabel101, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 280, -1, -1));

        jSeparator71.setForeground(new java.awt.Color(0, 0, 0));
        jPanel27.add(jSeparator71, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 300, 350, 10));

        nuevaIdenti.setBackground(new java.awt.Color(204, 204, 204));
        nuevaIdenti.setForeground(new java.awt.Color(0, 0, 0));
        nuevaIdenti.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel27.add(nuevaIdenti, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 310, 240, 30));

        jLabel96.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel96.setForeground(new java.awt.Color(0, 0, 0));
        jLabel96.setText("Identificación:");
        jPanel27.add(jLabel96, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 320, -1, 20));

        jSeparator72.setForeground(new java.awt.Color(0, 0, 0));
        jPanel27.add(jSeparator72, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 340, 350, 10));

        jLabel95.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel95.setForeground(new java.awt.Color(0, 0, 0));
        jLabel95.setText("Nacionalidad: ");
        jPanel27.add(jLabel95, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 360, -1, -1));

        jSeparator73.setForeground(new java.awt.Color(0, 0, 0));
        jPanel27.add(jSeparator73, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 380, 100, 10));

        jLabel104.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel104.setForeground(new java.awt.Color(0, 0, 0));
        jLabel104.setText("CUENTA");
        jPanel27.add(jLabel104, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 60, -1, -1));

        jLabel102.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel102.setForeground(new java.awt.Color(0, 0, 0));
        jLabel102.setText("Correo Electronico:");
        jPanel27.add(jLabel102, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 110, -1, -1));

        nuevoCorreo.setBackground(new java.awt.Color(204, 204, 204));
        nuevoCorreo.setForeground(new java.awt.Color(0, 0, 0));
        nuevoCorreo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        nuevoCorreo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevoCorreoActionPerformed(evt);
            }
        });
        jPanel27.add(nuevoCorreo, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 100, 250, 30));

        jSeparator75.setForeground(new java.awt.Color(0, 0, 0));
        jPanel27.add(jSeparator75, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 130, 120, 10));

        jLabel103.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel103.setForeground(new java.awt.Color(0, 0, 0));
        jLabel103.setText("Contraseña:");
        jPanel27.add(jLabel103, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 160, -1, 20));

        jSeparator76.setForeground(new java.awt.Color(0, 0, 0));
        jPanel27.add(jSeparator76, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 180, 110, 10));

        nuevaContra.setBackground(new java.awt.Color(204, 204, 204));
        nuevaContra.setForeground(new java.awt.Color(0, 0, 0));
        nuevaContra.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        nuevaContra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevaContraActionPerformed(evt);
            }
        });
        jPanel27.add(nuevaContra, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 150, 260, 30));

        LabelFOTO.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));
        jPanel27.add(LabelFOTO, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 280, 120, 140));

        Actualizarimagenodr.setText("Actualizar foto");
        Actualizarimagenodr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ActualizarimagenodrActionPerformed(evt);
            }
        });
        jPanel27.add(Actualizarimagenodr, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 240, -1, -1));

        jPanel29.setBackground(new java.awt.Color(29, 35, 51));
        jPanel29.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel27.add(jPanel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 960, 10));

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
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel31Layout.setVerticalGroup(
            jPanel31Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 430, Short.MAX_VALUE)
        );

        jPanel27.add(jPanel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 10, 10, 430));

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

        jPanel27.add(jPanel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 10, 10, 440));

        jLabel112.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel112.setForeground(new java.awt.Color(0, 0, 0));
        jLabel112.setText("Primer nombre:");
        jPanel27.add(jLabel112, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 120, -1, -1));

        jSeparator83.setForeground(new java.awt.Color(0, 0, 0));
        jPanel27.add(jSeparator83, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 140, 350, 10));

        jLabel113.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel113.setForeground(new java.awt.Color(0, 0, 0));
        jLabel113.setText("Primer apellido:");
        jPanel27.add(jLabel113, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 200, -1, -1));

        jSeparator85.setForeground(new java.awt.Color(0, 0, 0));
        jPanel27.add(jSeparator85, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 220, 350, 10));

        segundoNuevoApellido1.setBackground(new java.awt.Color(204, 204, 204));
        segundoNuevoApellido1.setForeground(new java.awt.Color(0, 0, 0));
        segundoNuevoApellido1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        segundoNuevoApellido1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                segundoNuevoApellido1ActionPerformed(evt);
            }
        });
        jPanel27.add(segundoNuevoApellido1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 230, 230, 30));

        nuevoSegundoNombre.setBackground(new java.awt.Color(204, 204, 204));
        nuevoSegundoNombre.setForeground(new java.awt.Color(0, 0, 0));
        nuevoSegundoNombre.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        nuevoSegundoNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevoSegundoNombreActionPerformed(evt);
            }
        });
        jPanel27.add(nuevoSegundoNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 150, 230, 30));

        cbxNacionalidad.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Seleccione>", "Afgana", "Alemana", "Americana", "Andorrana", "Angoleña", "Antiguana", "Árabe Saudita", "Argelina", "Argentina", "Armenia", "Arubeña", "Australiana", "Austriaca", "Azerbaiyana", "Bahameña", "Bahreiní", "Bangladesí", "Barbadense", "Belga", "Beliceña", "Beninesa", "Bermudeña", "Bielorrusa", "Birmana", "Boliviana", "Bosnia", "Botsuana", "Brasileña", "Británica", "Bruneana", "Búlgara", "Burkinesa", "Burundesa", "Butanesa", "Cabo Verdiana", "Camboyana", "Camerunesa", "Canadiense", "Catari", "Centroafricana", "Chadiana", "Checa", "Chilena", "China", "Chipriota", "Colombiana", "Comorense", "Congoleña", "Costarricense", "Croata", "Cubana", "Danesa", "Dominicana", "Ecuatoriana", "Egipcia", "Emiratí", "Eritrea", "Eslovaca", "Eslovena", "Española", "Estadounidense", "Estonia", "Etíope", "Filipina", "Finlandesa", "Fiyiana", "Francesa", "Gabonesa", "Galesa", "Gambiana", "Georgiana", "Ghanesa", "Gibraltareña", "Granadina", "Griega", "Guatemalteca", "Guineana", "Guineana-Bisáu", "Guineana Ecuatorial", "Guyanesa", "Haitiana", "Hondureña", "Hongkonesa", "Húngara", "India", "Indonesa", "Iraní", "Iraquí", "Irlandesa", "Islandesa", "Israelí", "Italiana", "Jamaicana", "Japonesa", "Jordana", "Kazaja", "Keniata", "Kirguisa", "Kiribatiana", "Kuwaití", "Laosiana", "Lesotense", "Letona", "Libanesa", "Liberiana", "Libia", "Liechtensteiniana", "Lituana", "Luxemburguesa", "Macedonia", "Malasia", "Malauí", "Maldiva", "Malgache", "Maliense", "Maltesa", "Marfileña", "Marroquí", "Marshallesa", "Mauriciana", "Mauritana", "Mexicana", "Micronesia", "Moldava", "Monegasca", "Mongola", "Montenegrina", "Mozambiqueña", "Namibia", "Nauruana", "Nepalí", "Nicaragüense", "Nigeriana", "Nigerina", "Norcoreana", "Noruega", "Neozelandesa", "Omana", "Neerlandesa (Holandesa)", "Paquistaní", "Palaosiana", "Panameña", "Papú", "Paraguaya", "Peruana", "Polaca", "Portuguesa", "Puertorriqueña", "Ruandesa", "Rumana", "Rusa", "Saharaui", "Salomonense", "Salvadoreña", "Samoana", "Sanmarinense", "Santotomense", "Saudí", "Senegalesa", "Serbia", "Seychellense", "Sierraleonesa", "Singapurense", "Siria", "Somalí", "Sri Lanka", "Sudafricana", "Sudanesa", "Sueca", "Suiza", "Surcoreana", "Surinamense", "Suazi", "Tailandesa", "Taiwanesa", "Tayika", "Tanzana", "Timorense", "Togolesa", "Tongana", "Trinitense", "Tunecina", "Turca", "Turkmena", "Tuvaluana", "Ucraniana", "Ugandesa", "Uruguaya", "Uzbeca", "Vanuatuense", "Venezolana", "Vietnamita", "Yemení", "Yibutiana", "Zambiana", "Zimbabuense" }));
        jPanel27.add(cbxNacionalidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 350, 240, 30));

        ActualizarODR.add(jPanel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 60, 950, 450));

        jLabel94.setBackground(new java.awt.Color(0, 0, 0));
        jLabel94.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        jLabel94.setForeground(new java.awt.Color(204, 0, 0));
        jLabel94.setText("Los campos que no desee modificar déjelos en blanco*");
        ActualizarODR.add(jLabel94, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 30, 290, -1));

        btnActualilzarODR.setBackground(new java.awt.Color(29, 35, 51));
        btnActualilzarODR.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnActualilzarODR.setForeground(new java.awt.Color(255, 255, 255));
        btnActualilzarODR.setText("Actualizar");
        btnActualilzarODR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualilzarODRActionPerformed(evt);
            }
        });
        ActualizarODR.add(btnActualilzarODR, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 520, 150, 40));

        OficialDeRegistroView.addTab("ActualizarInfoODR", ActualizarODR);

        presosInactivos.setBackground(new java.awt.Color(255, 255, 255));
        presosInactivos.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tablaInactivos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Foto", "Id", "Nombres", "Apellidos", "Edad", "Identificación", "Nacionalidad", "Estado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaInactivos.setRowHeight(50);
        jScrollPane3.setViewportView(tablaInactivos);

        presosInactivos.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 200, 1000, 330));

        jPanel15.setBackground(new java.awt.Color(29, 35, 51));
        jPanel15.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel79.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        jLabel79.setForeground(new java.awt.Color(255, 255, 255));
        jLabel79.setText("Filtrar por: ");
        jPanel15.add(jLabel79, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 30, -1, 30));

        comboInactivos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "TODAS", "LIBERADAS", "FALLECIDAS" }));
        comboInactivos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboInactivosActionPerformed(evt);
            }
        });
        jPanel15.add(comboInactivos, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 30, 120, 30));

        presosInactivos.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 30, 410, 90));

        lblFallecidas.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblFallecidas.setForeground(new java.awt.Color(0, 0, 0));
        presosInactivos.add(lblFallecidas, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 140, 970, 50));

        jButton1.setBackground(new java.awt.Color(51, 0, 0));
        jButton1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Volver");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        presosInactivos.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 540, 110, 30));

        jPanel16.setBackground(new java.awt.Color(180, 180, 195));
        jPanel16.setForeground(new java.awt.Color(0, 0, 0));
        jPanel16.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtBusquedPresosInactivos.setForeground(new java.awt.Color(0, 0, 0));
        txtBusquedPresosInactivos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBusquedPresosInactivosActionPerformed(evt);
            }
        });
        jPanel16.add(txtBusquedPresosInactivos, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 30, 220, 30));

        jButton4.setBackground(new java.awt.Color(23, 23, 43));
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("Buscar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel16.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 30, -1, 30));

        jLabel86.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel86.setForeground(new java.awt.Color(0, 0, 0));
        jLabel86.setText("INGRESE LA IDENTIFICACIÓN:");
        jPanel16.add(jLabel86, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 220, 30));

        presosInactivos.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, 570, 90));

        OficialDeRegistroView.addTab("Presos  inactivos", presosInactivos);

        Expediente.setBackground(new java.awt.Color(255, 255, 255));
        Expediente.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tablaExpediente.setBackground(new java.awt.Color(255, 255, 255));
        tablaExpediente.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        tablaExpediente.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Delito", "Id", "Fecha Sentencia", "Tiempo de condena", "Gravedad", "Fecha comisión"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaExpediente.setRowHeight(45);
        jScrollPane4.setViewportView(tablaExpediente);
        if (tablaExpediente.getColumnModel().getColumnCount() > 0) {
            tablaExpediente.getColumnModel().getColumn(0).setPreferredWidth(60);
            tablaExpediente.getColumnModel().getColumn(1).setPreferredWidth(1);
            tablaExpediente.getColumnModel().getColumn(2).setPreferredWidth(15);
            tablaExpediente.getColumnModel().getColumn(3).setPreferredWidth(15);
            tablaExpediente.getColumnModel().getColumn(4).setPreferredWidth(15);
            tablaExpediente.getColumnModel().getColumn(5).setPreferredWidth(15);
        }

        Expediente.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 340, 1040, 210));

        jPanel23.setBackground(new java.awt.Color(215, 215, 215));
        jPanel23.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel56.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel56.setForeground(new java.awt.Color(0, 0, 0));
        jLabel56.setText("Codigo Expediente:");
        jPanel23.add(jLabel56, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, -1));

        jSeparator43.setForeground(new java.awt.Color(0, 0, 0));
        jPanel23.add(jSeparator43, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 280, 10));

        jLabel57.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel57.setForeground(new java.awt.Color(0, 0, 0));
        jLabel57.setText("Fecha de Apertura:");
        jPanel23.add(jLabel57, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, -1, -1));
        jPanel23.add(FechaAper, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 110, 120, 20));

        jSeparator44.setForeground(new java.awt.Color(0, 0, 0));
        jPanel23.add(jSeparator44, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 280, 10));

        jLabel58.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel58.setForeground(new java.awt.Color(0, 0, 0));
        jLabel58.setText("Estado:");
        jPanel23.add(jLabel58, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, -1, 20));
        jPanel23.add(Estado, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 140, 140, 20));

        jSeparator45.setForeground(new java.awt.Color(0, 0, 0));
        jPanel23.add(jSeparator45, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 280, 10));

        jLabel59.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel59.setForeground(new java.awt.Color(0, 0, 0));
        jLabel59.setText("Juzgado:");
        jPanel23.add(jLabel59, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, -1, -1));
        jPanel23.add(Juzgado, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 170, 190, 20));

        jSeparator46.setForeground(new java.awt.Color(0, 0, 0));
        jPanel23.add(jSeparator46, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 280, 10));
        jPanel23.add(RegistroNum, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 50, 120, 20));
        jPanel23.add(CodExpe, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 80, 120, 20));

        jLabel60.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel60.setForeground(new java.awt.Color(0, 0, 0));
        jLabel60.setText("Nivel de riesgo:");
        jPanel23.add(jLabel60, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, 110, -1));

        jSeparator47.setForeground(new java.awt.Color(0, 0, 0));
        jPanel23.add(jSeparator47, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, 280, 10));

        nivelRiesgExp.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        nivelRiesgExp.setForeground(new java.awt.Color(0, 0, 0));
        jPanel23.add(nivelRiesgExp, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 200, 160, 20));

        jLabel62.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel62.setForeground(new java.awt.Color(0, 0, 0));
        jLabel62.setText("Numero de registro:");
        jPanel23.add(jLabel62, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, -1, 20));

        jSeparator53.setForeground(new java.awt.Color(0, 0, 0));
        jPanel23.add(jSeparator53, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 280, 10));

        jPanel35.setBackground(new java.awt.Color(29, 35, 51));
        jPanel35.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel85.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel85.setForeground(new java.awt.Color(255, 255, 255));
        jLabel85.setText("EXPEDIENTE");
        jPanel35.add(jLabel85, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 10, 110, 20));

        jPanel23.add(jPanel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 340, 30));

        Expediente.add(jPanel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, 320, 230));

        fotoPresoExpediente.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 3, true));
        Expediente.add(fotoPresoExpediente, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 30, 150, 190));

        jLabel55.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel55.setForeground(new java.awt.Color(0, 0, 0));
        jLabel55.setText("Apellidos completos:");
        Expediente.add(jLabel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 80, 150, 20));

        jLabel64.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel64.setForeground(new java.awt.Color(0, 0, 0));
        jLabel64.setText("Identificación:");
        Expediente.add(jLabel64, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 160, -1, 20));

        jLabel65.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel65.setForeground(new java.awt.Color(0, 0, 0));
        jLabel65.setText("Nacionalidad:");
        Expediente.add(jLabel65, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 200, -1, 20));

        jLabel66.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel66.setForeground(new java.awt.Color(0, 0, 0));
        jLabel66.setText("Edad:");
        Expediente.add(jLabel66, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 120, -1, 20));

        jLabel67.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel67.setForeground(new java.awt.Color(0, 0, 0));
        jLabel67.setText("Nombre  completo:");
        Expediente.add(jLabel67, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 40, -1, 20));

        jSeparator42.setForeground(new java.awt.Color(0, 0, 0));
        Expediente.add(jSeparator42, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 60, 480, 10));

        jSeparator49.setForeground(new java.awt.Color(0, 0, 0));
        Expediente.add(jSeparator49, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 100, 480, 10));

        jSeparator50.setForeground(new java.awt.Color(0, 0, 0));
        Expediente.add(jSeparator50, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 140, 480, 10));

        jSeparator51.setForeground(new java.awt.Color(0, 0, 0));
        Expediente.add(jSeparator51, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 180, 480, 10));

        jSeparator52.setForeground(new java.awt.Color(0, 0, 0));
        Expediente.add(jSeparator52, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 220, 480, 10));

        ape.setForeground(new java.awt.Color(0, 0, 0));
        Expediente.add(ape, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 80, 300, 20));

        edad.setForeground(new java.awt.Color(0, 0, 0));
        Expediente.add(edad, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 120, 420, 20));

        identi.setForeground(new java.awt.Color(0, 0, 0));
        Expediente.add(identi, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 160, 370, 20));

        naciona.setForeground(new java.awt.Color(0, 0, 0));
        Expediente.add(naciona, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 200, 380, 20));

        nom.setForeground(new java.awt.Color(0, 0, 0));
        Expediente.add(nom, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 40, 330, 20));

        jPanel5.setBackground(new java.awt.Color(29, 35, 51));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel78.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel78.setForeground(new java.awt.Color(255, 255, 255));
        jLabel78.setText("Fecha de salida:");
        jPanel5.add(jLabel78, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 130, 30));

        sentenciaTotaal.setBackground(new java.awt.Color(255, 255, 255));
        sentenciaTotaal.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        sentenciaTotaal.setForeground(new java.awt.Color(255, 255, 255));
        jPanel5.add(sentenciaTotaal, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 10, 180, 30));

        jLabel81.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel81.setForeground(new java.awt.Color(255, 255, 255));
        jLabel81.setText("Sentencia Total");
        jPanel5.add(jLabel81, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 110, 30));

        lblFechaSalida.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        lblFechaSalida.setForeground(new java.awt.Color(255, 255, 255));
        jPanel5.add(lblFechaSalida, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 40, 170, 30));

        panelEstadoEspecialPreso.setBackground(new java.awt.Color(102, 102, 102));
        panelEstadoEspecialPreso.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblMensajeEspecialPreso.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        lblMensajeEspecialPreso.setForeground(new java.awt.Color(255, 255, 255));
        panelEstadoEspecialPreso.add(lblMensajeEspecialPreso, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 270, 30));

        jPanel5.add(panelEstadoEspecialPreso, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 10, 320, 60));

        Expediente.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 240, 660, 80));

        BtnExportarPDF.setBackground(new java.awt.Color(204, 204, 204));
        BtnExportarPDF.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        BtnExportarPDF.setForeground(new java.awt.Color(0, 0, 0));
        BtnExportarPDF.setText("Exportar a pdf");
        BtnExportarPDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnExportarPDFActionPerformed(evt);
            }
        });
        Expediente.add(BtnExportarPDF, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 270, 160, 30));

        btnVerIntentosFuga.setBackground(new java.awt.Color(204, 204, 204));
        btnVerIntentosFuga.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnVerIntentosFuga.setForeground(new java.awt.Color(0, 0, 0));
        btnVerIntentosFuga.setText("Ver intento de fugas");
        btnVerIntentosFuga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerIntentosFugaActionPerformed(evt);
            }
        });
        Expediente.add(btnVerIntentosFuga, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 300, 160, 30));

        OficialDeRegistroView.addTab("Expediente", Expediente);

        AñadirDelito.setBackground(new java.awt.Color(255, 255, 255));
        AñadirDelito.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel36.setBackground(new java.awt.Color(204, 204, 204));
        jPanel36.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel37.setBackground(new java.awt.Color(29, 35, 51));
        jPanel37.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel80.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel80.setForeground(new java.awt.Color(255, 255, 255));
        jLabel80.setText("AÑADIR DELITO");
        jPanel37.add(jLabel80, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 10, 110, -1));

        jPanel36.add(jPanel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 870, 30));

        jLabel121.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel121.setForeground(new java.awt.Color(0, 0, 0));
        jLabel121.setText("Delito:");
        jPanel36.add(jLabel121, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 100, 90, 30));

        jLabel122.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel122.setForeground(new java.awt.Color(0, 0, 0));
        jLabel122.setText("Articulo:");
        jPanel36.add(jLabel122, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 130, 60, -1));

        jLabel123.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel123.setForeground(new java.awt.Color(0, 0, 0));
        jLabel123.setText("Meses:");
        jPanel36.add(jLabel123, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 320, 50, 30));

        jSeparator89.setForeground(new java.awt.Color(0, 0, 0));
        jPanel36.add(jSeparator89, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 150, 290, 10));

        NuevosDelitosNombre1.setBackground(new java.awt.Color(51, 51, 51));
        NuevosDelitosNombre1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Seleccionar>", "Injuria", "Calumnia", "Daño en bien ajeno", "Violación de habitación ajena", "Inasistencia alimentaria", "Omisión de socorro", "Lesiones personales leves", "Falsedad en documento privado", "Usurpación de derechos", "Uso de documento falso", "Abuso de confianza", "Hurto simple", "Receptación", "Estafa", "Violación de cerraduras o sellos", "Fraude", "Violación de medidas sanitarias", "Invasión de tierras o edificaciones", "Suplantación de identidad", "Contrabando", "Hurto calificado", "Lesiones personales graves", "Extorsión", "Falsedad en documento público", "Lavado de activos", "Peculado por uso", "Violencia intrafamiliar", "Acoso sexual", "Acceso abusivo a sistema informático", "Suplantación en medios electrónicos", "Daño informático", "Tráfico de influencias", "Porte ilegal de armas", "Cohecho", "Concusión", "Prevaricato", "Abuso de autoridad", "Perturbación del orden público", "Enriquecimiento ilícito", "Tráfico de fauna o flora silvestre", "Minería ilegal", "Hurto agravado", "Homicidio culposo", "Acceso carnal abusivo con menor de 14 años", "Actos sexuales con menor de 14 años", "Acceso carnal violento", "Acto sexual violento", "Violación", "Secuestro simple", "Tráfico de estupefacientes", "Fabricación o porte de estupefacientes", "Concierto para delinquir", "Homicidio", "Homicidio agravado", "Tortura", "Desaparición forzada", "Terrorismo", "Rebelión", "Genocidio", "Crímenes de lesa humanidad" }));
        NuevosDelitosNombre1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NuevosDelitosNombre1ActionPerformed(evt);
            }
        });
        jPanel36.add(NuevosDelitosNombre1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 90, 240, 40));

        grav.setBackground(new java.awt.Color(51, 51, 51));
        grav.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "<Seleccionar>", "Baja", "Media", "Alta" }));
        jPanel36.add(grav, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 160, 220, 40));

        jSeparator92.setForeground(new java.awt.Color(0, 0, 0));
        jPanel36.add(jSeparator92, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 190, 100, 10));
        jPanel36.add(fechaComisionActualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 160, 180, 30));

        jLabel125.setBackground(new java.awt.Color(51, 0, 0));
        jLabel125.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel125.setForeground(new java.awt.Color(51, 0, 0));
        jLabel125.setText("Sentencia por delito:");
        jPanel36.add(jLabel125, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 240, 140, 20));

        cod.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        cod.setForeground(new java.awt.Color(0, 0, 0));
        jPanel36.add(cod, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 80, 210, 30));

        textAreaDescripcion1.setColumns(20);
        textAreaDescripcion1.setRows(5);
        jScrollPane7.setViewportView(textAreaDescripcion1);

        jPanel36.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 250, 320, 100));

        jLabel126.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel126.setForeground(new java.awt.Color(0, 0, 0));
        jLabel126.setText("Fecha comisión:");
        jPanel36.add(jLabel126, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 170, 120, 20));

        jLabel127.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel127.setForeground(new java.awt.Color(0, 0, 0));
        jLabel127.setText("Codigo:");
        jPanel36.add(jLabel127, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 90, 80, 20));

        jSeparator93.setForeground(new java.awt.Color(0, 0, 0));
        jPanel36.add(jSeparator93, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 110, 290, 10));

        art.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        art.setForeground(new java.awt.Color(0, 0, 0));
        jPanel36.add(art, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 120, 200, 30));

        lblProgreso1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        lblProgreso1.setForeground(new java.awt.Color(0, 0, 0));
        jPanel36.add(lblProgreso1, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 40, 410, 20));
        jPanel36.add(AñosS, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 280, 80, -1));
        jPanel36.add(Meses, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 320, 80, -1));

        jLabel124.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel124.setForeground(new java.awt.Color(0, 0, 0));
        jLabel124.setText("Gravedad:");
        jPanel36.add(jLabel124, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 170, 70, 30));

        jLabel128.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel128.setForeground(new java.awt.Color(0, 0, 0));
        jLabel128.setText("Años:");
        jPanel36.add(jLabel128, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 280, 40, 30));

        jLabel129.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        jLabel129.setForeground(new java.awt.Color(0, 0, 0));
        jLabel129.setText("Descripcion delito:");
        jPanel36.add(jLabel129, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 220, 120, 20));

        AñadirDelito.add(jPanel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 80, 820, 400));

        finalizarDelito.setBackground(new java.awt.Color(9, 82, 9));
        finalizarDelito.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        finalizarDelito.setForeground(new java.awt.Color(255, 255, 255));
        finalizarDelito.setText("Finalizar y guardar");
        finalizarDelito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                finalizarDelitoActionPerformed(evt);
            }
        });
        AñadirDelito.add(finalizarDelito, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 500, 200, 40));

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
        try {
            DefaultTableModel modelo = (DefaultTableModel) TablaPresos.getModel();
            modelo.setRowCount(0);

            List<Object[]> datosPresos = presoController.obtenerTodosLosPresosParaTabla();

            for (Object[] fila : datosPresos) {
                modelo.addRow(fila);
            }

            TablaPresos.revalidate();
            TablaPresos.repaint();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al restaurar la tabla: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_btnRestaurarTablaActionPerformed

    private void nuevoNivelRiesgoComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevoNivelRiesgoComboActionPerformed

    }//GEN-LAST:event_nuevoNivelRiesgoComboActionPerformed

    private void nuevoNivelSeguridadComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevoNivelSeguridadComboActionPerformed

    }//GEN-LAST:event_nuevoNivelSeguridadComboActionPerformed

    private void actualizarPresoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_actualizarPresoActionPerformed

        try {
            if (ppMenuTablaPresos.isVisible()) {
                ppMenuTablaPresos.setVisible(false);
            }

            PresoController controller = PresoController.getInstancia();

            boolean actualizado = controller.actualizarPreso(
                    presoOriginal,
                    nuevoPrimerNombreField.getText(),
                    nuevoSegundoNombreField.getText(),
                    nuevoPrimerApellidoField.getText(),
                    nuevoSegundoApellidoField.getText(),
                    nuevaEdadField.getText(),
                    nuevaNacionalidadField.getSelectedItem(),
                    nuevaEstaturaField.getText(),
                    nuevoPesoField.getText(),
                    nuevoGrupoSanguineoCombo.getSelectedItem(),
                    nuevoNivelSeguridadCombo.getSelectedItem(),
                    nuevoAislamientoCombo.getSelectedItem(),
                    nuevoNivelRiesgoCombo.getSelectedItem(),
                    selectedImageFile,
                    nuevaSeccion.getSelectedItem()
                    );

            if (actualizado) {
                cargarTodosLosPresos();
                TablaPresos.revalidate();
                TablaPresos.repaint();
                limpiarCamposActualizacion();

                OficialDeRegistroView.setSelectedIndex(3);
                TablaPresos.clearSelection();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error al actualizar: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            ppMenuTablaPresos.setEnabled(true);
            TablaPresos.requestFocus();
            presoOriginal = null;
            selectedImageFile = null;
            System.gc();
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
        OficialDeRegistroView.setSelectedIndex(3);
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
  try {
        String idBuscado = identificacionB.getText().trim();
        
        if (idBuscado.isEmpty() || idBuscado.equals("Buscar preso por identificación")) {
            JOptionPane.showMessageDialog(this, 
                "Debe ingresar una identificación para buscar",
                "Campo vacío",
                JOptionPane.WARNING_MESSAGE);
            identificacionB.requestFocus();
            return; 
        }

        Preso preso = presoController.buscarPreso(idBuscado);
        DefaultTableModel model = (DefaultTableModel) TablaPresos.getModel();
        model.setRowCount(0);

        if (preso != null) {
            ImageIcon foto = presoController.obtenerFotoPreso(preso);
            model.addRow(new Object[]{
                foto,
                preso.getId(),
                preso.getNombresCompletos(),
                preso.getApellidosCompletos(),
                preso.getEdad(),
                preso.getIdentificacion(),
                preso.getNacionalidad(),
                preso.getSeccionAsignada(),
                preso.getCeldaAsignada(),
                preso.getEstado()
            });
            
            TablaPresos.setRowSelectionInterval(0, 0);
            TablaPresos.scrollRectToVisible(TablaPresos.getCellRect(0, 0, true));
        } else {
            JOptionPane.showMessageDialog(this,
                "No se encontró ningún preso con la identificación: " + idBuscado,
                "Búsqueda sin resultados",
                JOptionPane.INFORMATION_MESSAGE);
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Error al buscar preso: " + e.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }


    }//GEN-LAST:event_btnBuscarIdentificacionActionPerformed

    private void SelectorSeccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SelectorSeccionActionPerformed
        try {
            String seccionSeleccionada = SelectorSeccion.getSelectedItem().toString();

            DefaultTableModel model = (DefaultTableModel) TablaPresos.getModel();
            model.setRowCount(0);

            List<Object[]> filasPresos = presoController.obtenerPresosPorSeccion(seccionSeleccionada);

            for (Object[] fila : filasPresos) {
                model.addRow(fila);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los presos por sección: " + e.getMessage());


        }    }//GEN-LAST:event_SelectorSeccionActionPerformed

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


    private void AñadirPresoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AñadirPresoActionPerformed
        try {
            if (delitosTemporales.size() < totalDelitos) {
                throw new IllegalArgumentException(
                        "Faltan delitos por ingresar. Debe ingresar " + totalDelitos + " delitos.");
            }

            if (lblFoto.getIcon() == null) {
                throw new IllegalArgumentException("Debe seleccionar una foto del preso");
            }

            PresoController controller = PresoController.getInstancia();

            LocalDate fechaIngreso = LocalDate.now();

            boolean exito = controller.registrarPresoCompleto(
                    InputNombrePreso.getText().trim(),
                    InputSegundoNombre.getText().trim(),
                    InputApellidoPreso.getText().trim(),
                    InputSegundoApellido.getText().trim(),
                    InputEdadPreso.getText().trim(),
                    InputNacionalidadPreso.getSelectedItem().toString(),
                    InputSexoPreso.getSelectedItem().toString(),
                    InputEstaturaPreso.getText().trim(),
                    InputPesoPreso.getText().trim(),
                    TipoSangreCombobox.getSelectedItem(),
                    InputIdentificacionPreso.getText().trim(),
                    seccion.getSelectedItem(),
                    riesgo.getSelectedItem(),
                    seguridad.getSelectedItem(),
                    delitosTemporales,
                    selectedImageFile.getAbsoluteFile()
            );

            if (exito) {
               
                limpiarFormularioCompleto();
                cargarTodosLosPresos();
                cargarPresosInactivos();
                OficialDeRegistroView.setSelectedIndex(3);
            } else {
                throw new RuntimeException("Error al guardar el preso en la base de datos");
            }

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error de validación", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error inesperado: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

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
        try {
            Validador.validarCampoObligatorio("primer nombre", InputNombrePreso.getText());
            Validador.validarCampoObligatorio("primer apellido", InputApellidoPreso.getText());
            Validador.validarCampoObligatorio("edad", InputEdadPreso.getText());
            Validador.validarCampoObligatorio("estatura", InputEstaturaPreso.getText());
            Validador.validarCampoObligatorio("peso", InputPesoPreso.getText());

            Validador.validarNombre(InputNombrePreso.getText());
            if (!InputSegundoNombre.getText().isEmpty()) {
                Validador.validarString(InputSegundoNombre.getText());
            }

            if (!InputSegundoApellido.getText().isEmpty()) {
                Validador.validarString(InputSegundoApellido.getText());
            }

            Validador.validarNombre(InputApellidoPreso.getText());

            validador.validarIdentificacionUnica(InputIdentificacionPreso.getText());

            Validador.validarEdad(InputEdadPreso.getText());
            Validador.validarEstatura(InputEstaturaPreso.getText());
            Validador.validarPeso(InputPesoPreso.getText());

            Validador.validarSexoFemenino(InputSexoPreso.getSelectedItem());
            Validador.validarSeleccion("grupo sanguíneo", TipoSangreCombobox.getSelectedItem(), "<Seleccione>");
            Validador.validarSeleccion("sexo", InputSexoPreso.getSelectedItem(), "<Seleccione>");
            Validador.validarSeleccion("nacionalidad", InputNacionalidadPreso.getSelectedItem(), "<Seleccione>");

            if (lblFoto.getIcon() == null) {
                throw new IllegalArgumentException("Debe subir una foto del preso.");
            }

            TabbedAñadirInformacionGeneral.setSelectedIndex(1);

        } catch (IllegalArgumentException ex) {
            Validador.mostrarError(ex.getMessage());
        }
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
            OficialDeRegistroView.setSelectedIndex(3);
        }
    }//GEN-LAST:event_CancelarD3ActionPerformed

    private void Siguiente2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Siguiente2ActionPerformed
        try {

            Validador.validarSeleccion("sección", seccion.getSelectedItem(), "<Seleccionar>");
            Validador.validarSeleccion("nivel de riesgo", riesgo.getSelectedItem(), "<Seleccionar>");
            Validador.validarSeleccion("nivel de seguridad", seguridad.getSelectedItem(), "<Seleccionar>");
            Validador.validarSeleccion("condición", condicionComb.getSelectedItem(), "<Seleccione>");

            String seccionStr = seccion.getSelectedItem().toString();
            int celdasDisponibles = celdaDAO.obtenerCeldasDisponibles(seccionStr);
            if (celdasDisponibles <= 0) {
                throw new IllegalArgumentException("No hay celdas disponibles en la sección " + seccionStr);
            }

            TabbedAñadirInformacionGeneral.setSelectedIndex(2);

        } catch (IllegalArgumentException ex) {
            Validador.mostrarError(ex.getMessage());
        }    }//GEN-LAST:event_Siguiente2ActionPerformed

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
        Object[] options = {"Tomar Foto", "Seleccionar Archivo", "Cancelar"};
        int opcion = JOptionPane.showOptionDialog(
                this,
                "¿Cómo desea obtener la imagen del preso?",
                "Seleccionar Imagen",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        try {
            File nuevaImagen = null;

            if (opcion == 0) {
                nuevaImagen = presoController.capturarImagenPreso();
            } else if (opcion == 1) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "Imágenes (JPG, PNG, JPEG)", "jpg", "png", "jpeg");
                fileChooser.setFileFilter(filter);
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));

                int resultado = fileChooser.showOpenDialog(this);

                if (resultado == JFileChooser.APPROVE_OPTION) {
                    nuevaImagen = fileChooser.getSelectedFile();

                    String nombreArchivo = nuevaImagen.getName().toLowerCase();
                    if (!nombreArchivo.endsWith(".jpg")
                            && !nombreArchivo.endsWith(".jpeg")
                            && !nombreArchivo.endsWith(".png")) {
                        JOptionPane.showMessageDialog(this,
                                "Formato de imagen no válido. Use JPG, JPEG o PNG.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }

            if (nuevaImagen != null && nuevaImagen.exists()) {
                ImageIcon icono = new ImageIcon(nuevaImagen.getAbsolutePath());
                int width = lblFoto.getWidth();
                int height = lblFoto.getHeight();

                Image imagenEscalada = icono.getImage()
                        .getScaledInstance(
                                width,
                                height,
                                Image.SCALE_SMOOTH
                        );

                lblFoto.setIcon(new ImageIcon(imagenEscalada));

                selectedImageFile = nuevaImagen;
                lblFoto.setToolTipText("Imagen seleccionada: " + nuevaImagen.getAbsolutePath());
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al obtener imagen: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();

        }    }//GEN-LAST:event_btnIngresarFotoActionPerformed

    private void InputIdentificacionPresoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InputIdentificacionPresoActionPerformed
    }//GEN-LAST:event_InputIdentificacionPresoActionPerformed

    private void ActualizarFotoBotonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ActualizarFotoBotonActionPerformed
        Object[] options = {"Tomar Foto", "Seleccionar Archivo", "Cancelar"};
        int opcion = JOptionPane.showOptionDialog(
                this,
                "¿Cómo desea obtener la imagen del preso?",
                "Seleccionar Imagen",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        try {
            File nuevaImagen = null;

            if (opcion == 0) {
                nuevaImagen = presoController.capturarImagenPreso();
            } else if (opcion == 1) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "Imágenes (JPG, PNG, JPEG)", "jpg", "png", "jpeg");
                fileChooser.setFileFilter(filter);
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));

                int resultado = fileChooser.showOpenDialog(this);

                if (resultado == JFileChooser.APPROVE_OPTION) {
                    nuevaImagen = fileChooser.getSelectedFile();

                    String nombreArchivo = nuevaImagen.getName().toLowerCase();
                    if (!nombreArchivo.endsWith(".jpg")
                            && !nombreArchivo.endsWith(".jpeg")
                            && !nombreArchivo.endsWith(".png")) {
                        JOptionPane.showMessageDialog(this,
                                "Formato de imagen no válido. Use JPG, JPEG o PNG.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }

            if (nuevaImagen != null && nuevaImagen.exists()) {
                ImageIcon icono = new ImageIcon(nuevaImagen.getAbsolutePath());
                int width = nuevaFoto.getWidth();
                int height = nuevaFoto.getHeight();

                Image imagenEscalada = icono.getImage()
                        .getScaledInstance(width, height, Image.SCALE_SMOOTH);

                nuevaFoto.setIcon(new ImageIcon(imagenEscalada));
                selectedImageFile = nuevaImagen;
                nuevaFoto.setToolTipText("Imagen seleccionada: " + nuevaImagen.getAbsolutePath());
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al obtener imagen: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

    }//GEN-LAST:event_ActualizarFotoBotonActionPerformed

    private void nuevoSegundoNombreFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevoSegundoNombreFieldActionPerformed

    }//GEN-LAST:event_nuevoSegundoNombreFieldActionPerformed

    private void InputSegundoApellidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InputSegundoApellidoActionPerformed
    }//GEN-LAST:event_InputSegundoApellidoActionPerformed

    private void guardarDelitoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarDelitoActionPerformed
        try {
            String codigoStr = Codigo.getText();
            String articuloLeyStr = ArticuloLey.getText();
            Date fechaComision = FechaComision.getDate();
            String gravedadStr = Gravedad.getSelectedItem().toString();
            String descripcionStr = DescripcionDelito.getText();
            String nombreDelitoStr = delito.getSelectedItem().toString();
            int añosSentencia = (int) AñoD.getValue();
            int mesesSentencia = (int) MesD.getValue();

            Validador.validarCampoObligatorio("código", codigoStr);
            Validador.validarCampoObligatorio("artículo de ley", articuloLeyStr);
            Validador.validarCampoObligatorio("descripción del delito", descripcionStr);
            Validador.validarSeleccion("gravedad", gravedadStr, "<Seleccionar>");
            Validador.validarSeleccion("nombre del delito", nombreDelitoStr, "<Seleccionar>");
            Validador.validarFechaNoFutura(fechaComision.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), "fecha de comisión");
            Validador.validarSentencia(añosSentencia, mesesSentencia);

            Delito nuevoDelito = new Delito(
                    0,
                    "",
                    Integer.parseInt(codigoStr.trim()),
                    nombreDelitoStr,
                    articuloLeyStr.trim(),
                    gravedadStr,
                    descripcionStr.trim(),
                    fechaComision.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                    new Sentencia(añosSentencia, mesesSentencia, LocalDate.now())
            );

            if (delitosTemporales != null) {
                delitosTemporales.add(nuevoDelito);
            }

            SwingUtilities.invokeLater(() -> {
                actualizarProgreso();
                actualizarEstadoBotonDelito();
                actualizarEstadoBoton();

                Codigo.setText("");
                ArticuloLey.setText("");
                FechaComision.setDate(null);
                Gravedad.setSelectedIndex(0);
                DescripcionDelito.setText("");

                JOptionPane.showMessageDialog(this,
                        "Delito agregado temporalmente (Total: " + delitosTemporales.size() + ")",
                        "Información", JOptionPane.INFORMATION_MESSAGE);
            });

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error inesperado: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();

        }
    }//GEN-LAST:event_guardarDelitoActionPerformed

    private void limpiarCamposDelitoNuevo() {
        cod.setText("");
        art.setText("");
        grav.setSelectedIndex(0);
        NuevosDelitosNombre1.setSelectedIndex(0);
        fechaComisionActualizar.setDate(null);
        textAreaDescripcion1.setText("");
    }


    private void finalizarDelitoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_finalizarDelitoActionPerformed

        try {
            if (presoOriginal == null) {
                throw new IllegalStateException("Datos del preso no están inicializados correctamente");
            }

            boolean delitoAgregado = PresoController.getInstancia().agregarDelitoAPreso(
                    presoOriginal,
                    cod.getText(),
                    art.getText(),
                    NuevosDelitosNombre1.getSelectedItem().toString(),
                    grav.getSelectedItem().toString(),
                    textAreaDescripcion1.getText(),
                    fechaComisionActualizar.getDate(),
                    String.valueOf(AñosS.getValue()),
                    String.valueOf(Meses.getValue())
            );

            if (!delitoAgregado) {
                throw new IllegalStateException("No se pudo agregar el delito");
            }

            ExpedienteJudicial expediente = ExpedienteController.getInstancia()
                    .actualizarExpedienteConDelitos(presoOriginal.getIdentificacion());

            if (expediente == null || expediente.getDelitos().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No se encontraron delitos registrados en el expediente.",
                        "Expediente vacío",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            ExpedienteController.getInstancia().cargarTablaDelitos(expediente.getDelitos(), tablaExpediente);

            Sentencia sentenciaTotal = expedienteDAO.calcularSentenciaTotal(expediente.getDelitos());

            SentenciaTotal.setText(sentenciaTotal.getSentenciaFormateada());

            JOptionPane.showMessageDialog(this,
                    "Delito registrado exitosamente\n"
                    + "Nueva sentencia total: " + sentenciaTotal.getSentenciaFormateada() + "\n"
                    + "Total delitos registrados: " + expediente.getDelitos().size(),
                    "Operación Exitosa",
                    JOptionPane.INFORMATION_MESSAGE);

            limpiarCamposDelitoNuevo();
            OficialDeRegistroView.setSelectedIndex(3);

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    "Error en datos: " + e.getMessage(),
                    "Error de Validación",
                    JOptionPane.WARNING_MESSAGE);
        } catch (IllegalStateException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error crítico al procesar delito: " + e.getMessage(),
                    "Error del Sistema",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

    }//GEN-LAST:event_finalizarDelitoActionPerformed

    public void soloNumeros(JTextField campo) {
        campo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!Character.isDigit(c) && c != '\b') {
                    evt.consume();
                    JOptionPane.showMessageDialog(null, "Solo se permiten números.", "Entrada inválida", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    public void soloLetras(JTextField campo) {
        campo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!Character.isLetter(c) && !Character.isWhitespace(c) && c != '\b') {
                    evt.consume();
                    JOptionPane.showMessageDialog(null, "Solo se permiten letras.", "Entrada inválida", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }


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
        OficialDeRegistroView.setSelectedIndex(3);


    }//GEN-LAST:event_regresarActionPerformed

    private void btnActualizarInfoODRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarInfoODRActionPerformed
        OficialDeRegistroView.setSelectedIndex(4);
    }//GEN-LAST:event_btnActualizarInfoODRActionPerformed

    private void cerrarSesionODRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cerrarSesionODRActionPerformed
 int respuesta = JOptionPane.showConfirmDialog(
        this,
        "¿Está seguro de que desea cerrar sesión?",
        "Confirmar cierre de sesión",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE
    );

    if (respuesta == JOptionPane.YES_OPTION) {
        Login login = new Login(); 
        login.setVisible(true);
        this.dispose(); 
    }

    }//GEN-LAST:event_cerrarSesionODRActionPerformed

    private void nuevoPrimerNombreFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevoPrimerNombreFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nuevoPrimerNombreFieldActionPerformed

    private void btnRegresarAInfoGeneralActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegresarAInfoGeneralActionPerformed
        TabbedAñadirInformacionGeneral.setSelectedIndex(0);
    }//GEN-LAST:event_btnRegresarAInfoGeneralActionPerformed

    private void btnRegresarAJudicialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegresarAJudicialActionPerformed
        TabbedAñadirInformacionGeneral.setSelectedIndex(1);
    }//GEN-LAST:event_btnRegresarAJudicialActionPerformed

    private void btnActualilzarODRActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualilzarODRActionPerformed
        try {

            String nvNombre = nuevoPrimerNombre.getText();
            String nvSNombre = nuevoSegundoNombre.getText();
            String nvApellido = primerNuevoApellido.getText();
            String nvSApellido = segundoNuevoApellido1.getText();
            int nvedad = Integer.parseInt(nuevaEdad.getText());
            Object nvNacionalidad = cbxNacionalidad.getSelectedItem();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al intentar actualizar");
        }
    }//GEN-LAST:event_btnActualilzarODRActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        OficialDeRegistroView.setSelectedIndex(3);


    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        OficialDeRegistroView.setSelectedIndex(5);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void InputSegundoNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InputSegundoNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_InputSegundoNombreActionPerformed

    private void cantidadDelitosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cantidadDelitosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cantidadDelitosActionPerformed

    private void btnVerIntentosFugaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerIntentosFugaActionPerformed
        try {
            int filaSeleccionada = TablaPresos.getSelectedRow();
            if (filaSeleccionada == -1) {
                throw new IllegalStateException("No hay preso seleccionado");
            }

            String identificacion = TablaPresos.getValueAt(filaSeleccionada, 5).toString();

            IntentosFuga dialog = new IntentosFuga(
                    (JFrame) SwingUtilities.getWindowAncestor(this),
                    true,
                    presoController,
                    identificacion
            );
            dialog.setVisible(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al mostrar intentos de fuga: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnVerIntentosFugaActionPerformed

    private void BtnExportarPDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnExportarPDFActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar expediente como PDF");
        fileChooser.setSelectedFile(new File("Expediente_" + identi.getText() + ".pdf"));

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            String id = identi.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(null, "La identificación no puede estar vacía");
                return;
            }

            expedienteController.exportarExpedienteComoPDF(id, fileToSave.getAbsolutePath());
        }


    }//GEN-LAST:event_BtnExportarPDFActionPerformed

    private void txtBusquedPresosInactivosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBusquedPresosInactivosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBusquedPresosInactivosActionPerformed

    private void nuevoSegundoApellidoFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevoSegundoApellidoFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nuevoSegundoApellidoFieldActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

        try {

            String idBuscado = txtBusquedPresosInactivos.getText().trim();

            if (idBuscado.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Ingrese una identificacion a buscar");
                return;
            }

            Validador.validarFormatoIdentificacion(idBuscado);

            Preso preso = presoController.buscarPreso(idBuscado);

            DefaultTableModel model = (DefaultTableModel) tablaInactivos.getModel();
            model.setRowCount(0);

            if (preso != null) {
                ImageIcon foto = presoController.obtenerFotoPreso(preso);
                model.addRow(new Object[]{
                    foto,
                    preso.getId(),
                    preso.getPrimerNombre(),
                    preso.getPrimerApellido(),
                    preso.getEdad(),
                    preso.getIdentificacion(),
                    preso.getNacionalidad(),
                    preso.getEstado()

                });
            } else {
                JOptionPane.showMessageDialog(null, "No se encontró ningún preso con la identificación " + idBuscado);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al buscar al preso: " + e.getMessage());
        }


    }//GEN-LAST:event_jButton4ActionPerformed

    private void comboInactivosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboInactivosActionPerformed
        try {
        DefaultTableModel model = (DefaultTableModel) tablaInactivos.getModel();
        model.setRowCount(0);
        
        String seleccion = comboInactivos.getSelectedItem().toString();
        List<Object[]> filasPresos = new ArrayList<>();
        
        switch(seleccion) {
            case "LIBERADAS":
                filasPresos.addAll(presoController.obtenerPresosPorEstado(EstadoPresoEnum.LIBERADO));
                break;
            case "FALLECIDAS":
                filasPresos.addAll(presoController.obtenerPresosPorEstado(EstadoPresoEnum.FALLECIDO));
                break;
            case "TODAS":
                filasPresos.addAll(presoController.obtenerPresosPorEstado(EstadoPresoEnum.LIBERADO));
                filasPresos.addAll(presoController.obtenerPresosPorEstado(EstadoPresoEnum.FALLECIDO));
                break;
        }
        
        filasPresos.sort((a, b) -> ((String)b[2]).compareTo((String)a[2]));
        
        for (Object[] fila : filasPresos) {
            model.addRow(fila);
        }
        
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null,
            "Error al cargar presos: " + e.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
    }

    }//GEN-LAST:event_comboInactivosActionPerformed

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
    private com.toedter.components.JSpinField AñoD;
    private com.toedter.components.JSpinField AñosS;
    private javax.swing.JButton BtnExportarPDF;
    private javax.swing.JButton CancelarD3;
    private javax.swing.JLabel CodExpe;
    private javax.swing.JLabel Codigo;
    private javax.swing.JPanel DatosPersonalesPreso;
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
    private javax.swing.JComboBox<String> InputNacionalidadPreso;
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
    private com.toedter.components.JSpinField MesD;
    private com.toedter.components.JSpinField Meses;
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
    private javax.swing.JLabel ape;
    private javax.swing.JLabel apellido;
    private javax.swing.JLabel art;
    private javax.swing.JButton btnActualilzarODR;
    private javax.swing.JButton btnActualizarInfoODR;
    private javax.swing.JButton btnBuscarIdentificacion;
    private javax.swing.JButton btnIngresarFoto;
    private javax.swing.JButton btnRegresarAInfoGeneral;
    private javax.swing.JButton btnRegresarAJudicial;
    private javax.swing.JButton btnRestaurarTabla;
    private javax.swing.JButton btnVerIntentosFuga;
    private javax.swing.JButton cancelarD;
    private javax.swing.JButton cancelarD2;
    private javax.swing.JComboBox<String> cantidadDelitos;
    private javax.swing.JComboBox<String> cbxNacionalidad;
    private javax.swing.JButton cerrarSesionODR;
    private javax.swing.JLabel cod;
    private javax.swing.JComboBox<String> comboInactivos;
    private javax.swing.JComboBox<String> condicionComb;
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
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel102;
    private javax.swing.JLabel jLabel103;
    private javax.swing.JLabel jLabel104;
    private javax.swing.JLabel jLabel105;
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
    private javax.swing.JLabel jLabel128;
    private javax.swing.JLabel jLabel129;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
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
    private javax.swing.JLabel jLabel62;
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
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
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
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator19;
    private javax.swing.JSeparator jSeparator2;
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
    private javax.swing.JSeparator jSeparator60;
    private javax.swing.JSeparator jSeparator61;
    private javax.swing.JSeparator jSeparator62;
    private javax.swing.JSeparator jSeparator63;
    private javax.swing.JSeparator jSeparator64;
    private javax.swing.JSeparator jSeparator65;
    private javax.swing.JSeparator jSeparator68;
    private javax.swing.JSeparator jSeparator69;
    private javax.swing.JSeparator jSeparator70;
    private javax.swing.JSeparator jSeparator71;
    private javax.swing.JSeparator jSeparator72;
    private javax.swing.JSeparator jSeparator73;
    private javax.swing.JSeparator jSeparator75;
    private javax.swing.JSeparator jSeparator76;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator81;
    private javax.swing.JSeparator jSeparator82;
    private javax.swing.JSeparator jSeparator83;
    private javax.swing.JSeparator jSeparator84;
    private javax.swing.JSeparator jSeparator85;
    private javax.swing.JSeparator jSeparator89;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JSeparator jSeparator92;
    private javax.swing.JSeparator jSeparator93;
    private javax.swing.JLabel lblFallecidas;
    private javax.swing.JLabel lblFechaSalida;
    private javax.swing.JLabel lblFoto;
    private javax.swing.JLabel lblMensajeEspecialPreso;
    private javax.swing.JLabel lblProgreso;
    private javax.swing.JLabel lblProgreso1;
    private javax.swing.JLabel naciona;
    private javax.swing.JLabel nacionali;
    private javax.swing.JLabel nivelRiesgExp;
    private javax.swing.JLabel nom;
    private javax.swing.JLabel nombre;
    private javax.swing.JLabel nombreODR;
    private javax.swing.JTextField nuevaContra;
    private javax.swing.JTextField nuevaEdad;
    private javax.swing.JTextField nuevaEdadField;
    private javax.swing.JTextField nuevaEstaturaField;
    private javax.swing.JLabel nuevaFoto;
    private javax.swing.JTextField nuevaIdenti;
    private javax.swing.JComboBox<String> nuevaNacionalidadField;
    private javax.swing.JComboBox<String> nuevaSeccion;
    private javax.swing.JComboBox<String> nuevoAislamientoCombo;
    private javax.swing.JTextField nuevoCorreo;
    private javax.swing.JComboBox<String> nuevoGrupoSanguineoCombo;
    private javax.swing.JComboBox<String> nuevoNivelRiesgoCombo;
    private javax.swing.JComboBox<String> nuevoNivelSeguridadCombo;
    private javax.swing.JTextField nuevoPesoField;
    private javax.swing.JTextField nuevoPrimerApellidoField;
    private javax.swing.JTextField nuevoPrimerNombre;
    private javax.swing.JTextField nuevoPrimerNombreField;
    private javax.swing.JTextField nuevoSegundoApellidoField;
    private javax.swing.JTextField nuevoSegundoNombre;
    private javax.swing.JTextField nuevoSegundoNombreField;
    private javax.swing.JPanel panelEstadoEspecialPreso;
    private javax.swing.JLabel peso;
    private javax.swing.JPopupMenu ppMenuDelito;
    private javax.swing.JPopupMenu ppMenuTablaPresos;
    private javax.swing.JPopupMenu ppTablaInactivos;
    private javax.swing.JPanel presosInactivos;
    private javax.swing.JTextField primerNuevoApellido;
    private javax.swing.JButton regresar;
    private javax.swing.JComboBox<String> riesgo;
    private javax.swing.JLabel sangre;
    private javax.swing.JComboBox<String> seccion;
    private javax.swing.JTextField segundoNuevoApellido1;
    private javax.swing.JComboBox<String> seguridad;
    private javax.swing.JLabel sentenciaTotaal;
    private javax.swing.JLabel sexo;
    private javax.swing.JTable tablaExpediente;
    private javax.swing.JTable tablaInactivos;
    private javax.swing.JTextArea textAreaDescripcion1;
    private javax.swing.JTextField txtBusquedPresosInactivos;
    // End of variables declaration//GEN-END:variables
}
