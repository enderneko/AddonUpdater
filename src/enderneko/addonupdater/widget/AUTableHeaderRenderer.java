package enderneko.addonupdater.widget;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.SwingConstants;

import enderneko.addonupdater.util.AUFont;
import sun.swing.table.DefaultTableCellHeaderRenderer;

@SuppressWarnings("serial")
public class AUTableHeaderRenderer extends DefaultTableCellHeaderRenderer {
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
			int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//		setBackground(Color.WHITE);
		setValue(value);
//		setPreferredSize(new Dimension(70, 20));
		setFont(AUFont.NORMAL);
		setHorizontalAlignment(SwingConstants.LEFT);
		return this;
	}
}
