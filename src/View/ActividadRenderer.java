
package View;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class ActividadRenderer extends DefaultTableCellRenderer {
@Override
public Component getTableCellRendererComponent(JTable table, Object value, 
    boolean isSelected, boolean hasFocus, int row, int column) {

    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

    String estado = "";
    if (value != null) {
        if (value instanceof Enum) {
            estado = value.toString();
        } else {
            estado = value.toString();
        }
    }
    
    Color verde = new Color(146, 241, 131);
    Color cafe = new Color(186, 141, 111);
    
    if (isSelected) {
        c.setBackground(table.getSelectionBackground());
        c.setForeground(table.getSelectionForeground());
    } else {
        if ("Cancelada".equalsIgnoreCase(estado)) {
            c.setBackground(cafe);
            c.setForeground(Color.BLACK);
        } else if ("Activa".equalsIgnoreCase(estado)) {
            c.setBackground(verde);
            c.setForeground(Color.BLACK);
        } else {
            c.setBackground(table.getBackground());
            c.setForeground(table.getForeground());
        }
    }
    
    return c;
}
}

