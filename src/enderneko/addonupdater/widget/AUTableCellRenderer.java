package enderneko.addonupdater.widget;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import enderneko.addonupdater.util.AUFont;

@SuppressWarnings("serial")
public class AUTableCellRenderer extends DefaultTableCellRenderer {
	public int rowAtPoint = -1;

	public AUTableCellRenderer() {
		setFont(AUFont.NORMAL);
		setHorizontalAlignment(SwingConstants.LEFT);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		// highlight mouseover
		if (row == rowAtPoint) {
			setBackground(new Color(252, 81, 133, 100));
		} else {
			setBackground(table.getBackground());
		}

		setValue(value);
		return this;
	}

	// @Override
	// protected void paintComponent(Graphics g) {
	// super.paintComponent(g);
	// Graphics2D g2=(Graphics2D)g;
	// final BasicStroke stroke=new BasicStroke(2.0f);
	// g2.setColor(Color.RED);
	// g2.setStroke(stroke);
	// if (row % 2 == 0) {
	// g2.drawLine(0,getHeight()/2,getWidth(),getHeight()/2);
	// }
	// }
}
