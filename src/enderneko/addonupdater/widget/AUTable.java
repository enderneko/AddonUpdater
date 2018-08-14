package enderneko.addonupdater.widget;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import enderneko.addonupdater.dao.IAddonDao;
import enderneko.addonupdater.domain.Addon;
import enderneko.addonupdater.frame.MainFrame;
import enderneko.addonupdater.util.AUConfigUtil;
import enderneko.addonupdater.util.AUUpdater;

@SuppressWarnings("serial")
public class AUTable extends JTable {
	private AUTableModel tableModel = new AUTableModel();
	private TableRowSorter<TableModel> sorter;
	private AUTableCellRenderer cellRenderer = new AUTableCellRenderer();
	private AUTableStatusCellRenderer statusRenderer = new AUTableStatusCellRenderer();
	private AUTableHeaderRenderer headerRenderer = new AUTableHeaderRenderer();
	private AUPopupMenu popupMenu = new AUPopupMenu();
	private float[] columnWidthPercentage = { 30.0f, 16.0f, 18.0f, 18.0f, 18.0f };
	private MainFrame owner;
	private IAddonDao dao = AUConfigUtil.getAddonDAO();

	public AUTable(MainFrame owner) {
		this.owner = owner;
		initTable();
		addListeners();
	}

	private void initTable() {
		// table header
		setModel(tableModel);

		// sorter FIXME
//		sorter = new TableRowSorter<>(tableModel);
//		setRowSorter(sorter);
//		// setAutoCreateRowSorter(true);
//		List<RowSorter.SortKey> sortKeys = new ArrayList<>();
//		sortKeys.add(new SortKey(1, SortOrder.ASCENDING));
//		sortKeys.add(new SortKey(0, SortOrder.ASCENDING));
//		sorter.setSortKeys(sortKeys);
//		sorter.setSortable(0, false);
//		sorter.setSortable(1, true);
//		sorter.setSortable(2, false);
//		sorter.setSortable(3, false);
//		sorter.setSortable(4, false);

		// set column not movable
		getTableHeader().setReorderingAllowed(false);

		// set renderer/editor
		setDefaultRenderer(Object.class, cellRenderer);
		getColumnModel().getColumn(1).setCellRenderer(statusRenderer);
		// getColumnModel().getColumn(1).setCellEditor(statusEditor);
		getTableHeader().setDefaultRenderer(headerRenderer);

		// set row height
		setRowHeight(20);

		// set selection mode
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// style
		// setIntercellSpacing(new Dimension(0, 0)); // spacing
		setShowGrid(false);
		// setShowVerticalLines(false);
		// setGridColor(Color.GRAY);
		// getTableHeader().setOpaque(false);
		setOpaque(false);
	}

	private void addListeners() {
		// highlight onEnter row
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				cellRenderer.rowAtPoint = rowAtPoint(e.getPoint());
				statusRenderer.rowAtPoint = rowAtPoint(e.getPoint());
				updateUI();
			}
		});

		// remove highlight onLeave table
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				cellRenderer.rowAtPoint = -1;
				statusRenderer.rowAtPoint = -1;
				updateUI();
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				int row = rowAtPoint(e.getPoint());
				int col = columnAtPoint(e.getPoint());
				if (e.getButton() == 3) {
					Addon a = getAddonFromTable((String) getValueAt(row, 0));
					popupMenu.showPopupMenu(owner, AUTable.this, e.getPoint(), a);
				} else {
					if (AUUpdater.HAS_UPDATE.equals(getValueAt(row, col))) { // show update button
						Addon a = getAddonFromTable((String) getValueAt(row, 0));
						AUUpdater.download(a);
					} else if (AUUpdater.NOT_AVAILABLE.equals(getValueAt(row, col))) { // show retry button
						Addon a = getAddonFromTable((String) getValueAt(row, 0));
						AUUpdater.check(AUTable.this, a);
					}
				}
			}
		});

		// resize column width
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				int totalWidth = getWidth();
				// set column header/width
				for (int i = 0; i < tableModel.getColumnCount(); i++) {
					TableColumn colHeader = getTableHeader().getColumnModel().getColumn(i);
					colHeader.setResizable(false);

					int pWidth = Math.round(columnWidthPercentage[i] * totalWidth);
					colHeader.setPreferredWidth(pWidth);
				}
			}
		});

	}
	
	public void addRow(Addon a) {
		tableModel.addRow(a);
	}

	public void setData(Vector<Addon> addons) {
		tableModel.setData(addons);
	}

	/**
	 * through tableModel.fireTableStructureChanged()
	 */
	public void refresh() {
		tableModel.fireTableDataChanged();
	}

	public void refreshAndSave(Addon a) {
		tableModel.fireTableDataChanged();
		dao.update(a);
	}
	
	public Addon getAddonFromTable(String name) {
		return tableModel.getAddon(name);
	}

	public Vector<Addon> getAllAddonsFromTable() {
		return tableModel.getAllAddons();
	}


	/*
	 * // hide a column public void hideColumn(int columnId) { TableColumn col =
	 * getColumnModel().getColumn(columnId); col.setPreferredWidth(0);
	 * col.setMaxWidth(0); col.setMinWidth(0);
	 * 
	 * TableColumn colHeader =
	 * getTableHeader().getColumnModel().getColumn(columnId);
	 * colHeader.setPreferredWidth(0); colHeader.setMaxWidth(0);
	 * colHeader.setMinWidth(0);
	 * 
	 * colHeader.setResizable(false); }
	 * 
	 * // re-show a column public void showColumn(int columnId, int width) {
	 * TableColumn colHeader =
	 * getTableHeader().getColumnModel().getColumn(columnId);
	 * 
	 * colHeader.setPreferredWidth(width); colHeader.setMaxWidth(200);
	 * colHeader.setMinWidth(70);
	 * 
	 * TableColumn col = getColumnModel().getColumn(columnId);
	 * col.setPreferredWidth(width); col.setMaxWidth(200); col.setMinWidth(70);
	 * 
	 * colHeader.setResizable(false); }
	 */
}
