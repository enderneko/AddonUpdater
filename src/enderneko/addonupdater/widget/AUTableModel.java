package enderneko.addonupdater.widget;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import enderneko.addonupdater.domain.Addon;

/**
 * 
 * @author enderneko Aug 8, 2018
 */
@SuppressWarnings("serial")
public class AUTableModel extends AbstractTableModel {
	private Vector<Addon> addons = null;
	private String[] columns = { "ADDON", "STATUS", "LATEST VERSION", "LATEST DATE", "AUTHOR" };

	public AUTableModel() {
		addons = new Vector<>();
	}
 
	public AUTableModel(Vector<Addon> addon) {
		this.addons = addon;
		fireTableDataChanged();
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public int getRowCount() {
		return addons.size();
	}

	@Override
	public int getColumnCount() {
		return columns.length;
	}

	@Override
	public String getColumnName(int column) {
		return columns[column];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Addon a = addons.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return a.getName();
		case 1:
			return a.getStatus();
		case 2:
			return a.getLatestVersion();
		case 3:
			return a.getLatestDate();
		case 4:
			return a.getAuthor();
		}
		return null;
	}

	public void addRow(Addon a) {
		if (!addons.contains(a)) {
			addons.add(a);
			fireTableDataChanged();
		}
	}
	
	public Addon getAddon(String name) {
		int index = addons.indexOf(new Addon(name));
		return index == -1 ? null : addons.get(index);
	}
	
	public Vector<Addon> getAllAddons() {
		return addons;
	}
	
	public void setData(Vector<Addon> addons) {
		this.addons = addons;
		fireTableDataChanged();
	}
}