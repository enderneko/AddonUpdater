package enderneko.addonupdater.frame;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import enderneko.addonupdater.dao.IAddonDAO;
import enderneko.addonupdater.dao.impl.AddonDAOImpl;
import enderneko.addonupdater.domain.Addon;
import enderneko.addonupdater.util.AUScanner;
import enderneko.addonupdater.widget.AUButton;
import enderneko.addonupdater.widget.AUCheckBox;
import enderneko.addonupdater.widget.AULabel;
import net.miginfocom.swing.MigLayout;

/**
 * 
 * @author enderneko Aug 8, 2018
 */
@SuppressWarnings("serial")
public class AddonManagementDialog extends JDialog {
	private MainFrame owner;
	private JPanel scollChild = new JPanel(new MigLayout("wrap 3", "[]30[]", "[]10[]"));
	private JScrollPane scrollPane = new JScrollPane(scollChild);
	private JPanel bottomPanel = new JPanel(new MigLayout("", "[grow][]"));
	private AULabel infoLabel = new AULabel("Addon Folders: ");
	private AUButton selectAllBtn = new AUButton("Select All");
	private AUButton unselectAllBtn = new AUButton("Unselect All");
	private AUButton confirmBtn = new AUButton("Confirm");
	private AUButton cancelBtn = new AUButton("Cancel");
	private Set<AUCheckBox> checkBoxes = new TreeSet<>();
	private IAddonDAO dao = new AddonDAOImpl();

	public AddonManagementDialog(MainFrame owner) {
		super(owner, "Addon Management", true);
		this.owner = owner;
		setSize(850, 480);
		setResizable(false);
		setLocationRelativeTo(owner);
		setLayout(new BorderLayout());

		// set component
		scrollPane.setBorder(new TitledBorder("Select addons to manage (MAIN ADDON FOLDER ONLY)"));
		scrollPane.getVerticalScrollBar().setUnitIncrement(21);
		add(scrollPane, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);
		bottomPanel.add(infoLabel);
		bottomPanel.add(selectAllBtn);
		bottomPanel.add(unselectAllBtn);
		bottomPanel.add(confirmBtn);
		bottomPanel.add(cancelBtn);

		addListeners();
	}

	private void addListeners() {
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				scanAddon();
			}
		});

		selectAllBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				checkBoxes.forEach(cb -> cb.setSelected(true));
			}
		});

		unselectAllBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				checkBoxes.forEach(cb -> cb.setSelected(false));
			}
		});

		confirmBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				checkBoxes.forEach(cb -> {
					if (cb.isSelected()) {
						// if not in db, then add it.
						if (!dao.isManaged(cb.getText())) {
							dao.add(cb.addon);
						}
					} else {
						dao.delete(cb.addon.getName());
					}
				});
				AddonManagementDialog.this.setVisible(false);
				owner.loadAddon();
			}
		});

		cancelBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				AddonManagementDialog.this.setVisible(false);
			}
		});
	}

	private void scanAddon() {
		Vector<Addon> addons = AUScanner.scanAddons();
		infoLabel.setText("Addon Folders: " + addons.size() + ". DO NOT CHANGE THESE WHILE CHECKING/UPDATING.");

		addons.forEach(a -> {
			AUCheckBox cb = new AUCheckBox(a);
			if (!checkBoxes.contains(cb)) {
				checkBoxes.add(cb);
				cb.addon = a;
			}
		});

		// re-add, new addons will be in the right sort.
		scollChild.removeAll();
		checkBoxes.forEach(cb -> {
			scollChild.add(cb);
			if (dao.isManaged(cb.getText())) {
				cb.setSelected(true);
			} else {
				cb.setSelected(false);
			}
		});
	}
}