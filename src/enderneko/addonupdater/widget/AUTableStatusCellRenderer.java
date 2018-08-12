package enderneko.addonupdater.widget;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;

import enderneko.addonupdater.util.AUUpdater;

/**
 * NOT USED
 * 
 * @author enderneko Aug 9, 2018
 */
@SuppressWarnings("serial")
public class AUTableStatusCellRenderer extends AUTableCellRenderer {
	/*
	 * 仅用于呈现，用table的mouse listener实现click效果
	 */
	private AUButton button = new AUButton("");

	public AUTableStatusCellRenderer() {
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
		
		if (AUUpdater.HAS_UPDATE.equals(value)) {
			button.setText("Update");
			return button;
		} else if (AUUpdater.NOT_AVAILABLE.equals(value)) {
			button.setText("Retry");
			return button;
		} else {
			setValue(value);
			return this;
		}
//		setValue(value);
//		return this;
	}
}
