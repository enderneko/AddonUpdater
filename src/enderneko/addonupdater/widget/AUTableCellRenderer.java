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
		// Component c = super.getTableCellRendererComponent(table, value, isSelected,
		// hasFocus, row, column);
		//
		// if (c instanceof JLabel && value != null) {
		// int availableWidth = table.getColumnModel().getColumn(column).getWidth(); //
		// cell total width
		// availableWidth -= table.getIntercellSpacing().getWidth(); // minus table cell
		// spacing
		// Insets boderInsets = getBorder().getBorderInsets(c);
		// availableWidth -= boderInsets.left + boderInsets.right; // minus border
		// insets
		// FontMetrics fm = getFontMetrics(getFont());
		//
		// if (fm.stringWidth(value.toString()) > availableWidth) { // is truncated
		// setToolTipText(value.toString());
		// } else {
		// setToolTipText(null);
		// }
		// }
		
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
