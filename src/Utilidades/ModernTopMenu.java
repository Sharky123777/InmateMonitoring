package utilidades;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * Menú superior moderno para sistema carcelario
 */
public class ModernTopMenu extends JPanel {
    
    private JTabbedPane tabbedPane;
    private Color primaryColor = new Color(20, 25, 40);     // Azul oscuro principal
    private Color hoverColor = new Color(29, 35, 51);      // Azul oscuro hover
    private Color textColor = new Color(255, 255, 255);    // Texto blanco
    private Color backgroundColor = new Color(15, 20, 30); // Fondo más oscuro
    private Color dropdownBgColor = new Color(25, 30, 45); // Fondo del desplegable
    private Font menuFont = new Font("Segoe UI", Font.BOLD, 14);
    private Font dropdownFont = new Font("Segoe UI", Font.PLAIN, 13);
    
    private Map<String, java.util.List<MenuItem>> menuStructure;
    private JPanel menuBar;
    private JPopupMenu activePopup;

    public ModernTopMenu(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
        setupMenuStructure();
        initializeUI();
    }

    private void setupMenuStructure() {
    menuStructure = new LinkedHashMap<>();
    
  
    ArrayList<MenuItem> directorItems = new ArrayList<>();
    directorItems.add(new MenuItem("Ver Perfil", 0)); // Director
    directorItems.add(new MenuItem("Disminuir Sentencia de un Recluso", 1)); // DisminuirSentencia
    directorItems.add(new MenuItem("Añadir Delito", 2)); // AñadirDelito
    menuStructure.put("Director", directorItems);
    
    // Menú Enfermeras
    ArrayList<MenuItem> enfermerasItems = new ArrayList<>();
    enfermerasItems.add(new MenuItem("Añadir Enfermera", 3)); // AñadirEnfermera
   
    enfermerasItems.add(new MenuItem("Ver Lista de Enfermeras", 5)); // MostrarEnfermeras
    menuStructure.put("Enfermeras", enfermerasItems);
    
   
    ArrayList<MenuItem> guardiasItems = new ArrayList<>();
    guardiasItems.add(new MenuItem("Añadir Guardia", 6)); 
   
    guardiasItems.add(new MenuItem("Ver Lista de Guardias", 8)); // ListaDeGuardias
    menuStructure.put("Guardias", guardiasItems);
    
     ArrayList<MenuItem> oficialItems = new ArrayList<>();
    oficialItems.add(new MenuItem("Agregar oficial", 15)); // AgregarCDA
    menuStructure.put("Oficial", oficialItems );
    
    
    ArrayList<MenuItem> cdaItems = new ArrayList<>();
    cdaItems.add(new MenuItem("Agregar CDA", 9)); // AgregarCDA
    
    cdaItems.add(new MenuItem("Mostrar CDA", 11)); // mostrarCoordinadora
    menuStructure.put("CDA", cdaItems);
    
    
    ArrayList<MenuItem> configItems = new ArrayList<>();
    configItems.add(new MenuItem("Cambiar credenciales", 18));
    configItems.add(new MenuItem("Usuarios", 13));
    configItems.add(new MenuItem("Sistema", 14));
    menuStructure.put("Configuración", configItems);
}

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(backgroundColor);
        
        menuBar = new JPanel();
        menuBar.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        menuBar.setBackground(backgroundColor);
        
        createMenuItems();
        
        add(menuBar, BorderLayout.NORTH);
        setPreferredSize(new Dimension(tabbedPane.getWidth(), 45));
    }

    private void createMenuItems() {
        for (Map.Entry<String, java.util.List<MenuItem>> entry : menuStructure.entrySet()) {
            String menuName = entry.getKey();
            java.util.List<MenuItem> menuItems = entry.getValue();
            
            JButton menuButton = createMenuButton(menuName);
            JPopupMenu popupMenu = createDropdownMenu(menuItems);
                
            menuButton.addActionListener(e -> {
                if (activePopup != null && activePopup.isVisible()) {
                    activePopup.setVisible(false);
                }
                activePopup = popupMenu;
                popupMenu.show(menuButton, 0, menuButton.getHeight());
            });
            
            menuBar.add(menuButton);
        }
    }

    private JButton createMenuButton(String menuName) {
        JButton menuButton = new JButton(menuName) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isRollover()) {
                    g2.setColor(hoverColor);
                } else {
                    g2.setColor(primaryColor);
                }
                
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                g2.setColor(textColor);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(getText());
                int textHeight = fm.getHeight();
                g2.drawString(getText(), (getWidth() - textWidth) / 2, 
                             (getHeight() - textHeight) / 2 + fm.getAscent());
                
                // Triángulo desplegable
                int[] xPoints = {getWidth() - 12, getWidth() - 8, getWidth() - 4};
                int[] yPoints = {getHeight() / 2 - 2, getHeight() / 2 + 2, getHeight() / 2 - 2};
                g2.fillPolygon(xPoints, yPoints, 3);
                
                g2.dispose();
            }
        };
        
        menuButton.setFont(menuFont);
        menuButton.setForeground(textColor);
        menuButton.setBackground(primaryColor);
        menuButton.setFocusPainted(false);
        menuButton.setBorderPainted(false);
        menuButton.setContentAreaFilled(false);
        menuButton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        int buttonWidth = tabbedPane.getWidth() / menuStructure.size();
        if (buttonWidth < 120) buttonWidth = 120;
        
        menuButton.setPreferredSize(new Dimension(buttonWidth, 45));
        
        menuButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                menuButton.repaint();
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                menuButton.repaint();
            }
        });
        
        return menuButton;
    }

    private JPopupMenu createDropdownMenu(java.util.List<MenuItem> menuItems) {
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setBackground(dropdownBgColor);
        popupMenu.setBorder(new LineBorder(new Color(50, 55, 70)));
        
        for (MenuItem item : menuItems) {
            JMenuItem menuItem = new JMenuItem(item.getName()) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    if (getModel().isArmed()) {
                        g2.setColor(new Color(40, 45, 60));
                    } else {
                        g2.setColor(dropdownBgColor);
                    }
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    
                    g2.setColor(textColor);
                    g2.setFont(getFont());
                    FontMetrics fm = g2.getFontMetrics();
                    int textY = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                    g2.drawString(getText(), 20, textY);
                    
                    g2.dispose();
                }
            };
            
            menuItem.setFont(dropdownFont);
            menuItem.setForeground(textColor);
            menuItem.setBackground(dropdownBgColor);
            menuItem.setBorder(new EmptyBorder(8, 20, 8, 15));
            menuItem.setPreferredSize(new Dimension(220, 35));
            
            final int tabIndex = item.getTabIndex();
            menuItem.addActionListener(e -> {
                if (tabIndex >= 0 && tabIndex < tabbedPane.getTabCount()) {
                    tabbedPane.setSelectedIndex(tabIndex);
                }
                if (activePopup != null) {
                    activePopup.setVisible(false);
                }
            });
            
            popupMenu.add(menuItem);
        }
        
        return popupMenu;
    }

    public void setColors(Color primary, Color hover, Color text, Color background, Color dropdown) {
        this.primaryColor = primary;
        this.hoverColor = hover;
        this.textColor = text;
        this.backgroundColor = background;
        this.dropdownBgColor = dropdown;
        
        menuBar.setBackground(backgroundColor);
        repaint();
    }

    public void adjustButtonWidths() {
        int width = getWidth();
        int buttonWidth = width / menuStructure.size();
        
        for (Component c : menuBar.getComponents()) {
            if (c instanceof JButton) {
                c.setPreferredSize(new Dimension(buttonWidth, 45));
            }
        }
        
        menuBar.revalidate();
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        adjustButtonWidths();
    }

    private static class MenuItem {
        private final String name;
        private final int tabIndex;
        
        public MenuItem(String name, int tabIndex) {
            this.name = name;
            this.tabIndex = tabIndex;
        }
        
        public String getName() {
            return name;
        }
        
        public int getTabIndex() {
            return tabIndex;
        }
    }
}