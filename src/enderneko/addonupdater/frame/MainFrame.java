package enderneko.addonupdater.frame;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Collections;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import enderneko.addonupdater.dao.IAddonDao;
import enderneko.addonupdater.domain.Addon;
import enderneko.addonupdater.util.AUConfigUtil;
import enderneko.addonupdater.util.AUUpdater;
import enderneko.addonupdater.util.AUUtil;
import enderneko.addonupdater.widget.AUButton;
import enderneko.addonupdater.widget.AULabel;
import enderneko.addonupdater.widget.AUTable;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	private AULabel dirLable = new AULabel(null);
	private AUButton dirButton = new AUButton("WoW Dir");
	private AUButton manageButton = new AUButton("Manage Addons");
	private AUButton checkButton = new AUButton("Check for Updates");
	private AUButton updateAllButton = new AUButton("Update All");
	private AUButton customButton = new AUButton("Custom");
	private AUButton curseButton = new AUButton("CurseForge");
	private AUTable addonTable;
	private JScrollPane addonScrollPane;
	private AUFileChooser fileChooser = new AUFileChooser();
	private AddonManagementDialog manageDialog;
	private CustomAddonUrlDialog customDialog;
	private CurseForgeDialog curseDialog;
	private IAddonDao dao = AUConfigUtil.getAddonDAO();

	static {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			// UIManager.getDefaults().entrySet().parallelStream()
			// .filter(e -> e.getKey()
			// .equals("ProgressBar.background"))
			// .forEach(e -> System.out.println(e));
			// UIManager.put("ProgressBar.background", Color.BLACK);
			// UIManager.put("ProgressBar.border", BorderFactory.createLineBorder(Color.RED,
			// 2));
			// Arrays.asList(UIManager.getPropertyChangeListeners()).forEach(l ->
			// UIManager.removePropertyChangeListener(l));
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

	public MainFrame() {
		super("WoW Addon Updater");
		getContentPane().setLayout(new MigLayout());
		setSize(850, 605);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		// getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
		setIconImage(AUUtil.getImage("curse.png"));
		
		initWidgets();
		addListeners();
		loadAddon();
		checkDirValidity();
	}

	// add all components to frame
	private void initWidgets() {
		addonTable = new AUTable(this);
		addonScrollPane = new JScrollPane(addonTable);
		addonScrollPane.setPreferredSize(new Dimension(getWidth(), 600));
		// addonScrollPane.setBorder(new CompoundBorder(new TitledBorder("Addon List"),
		// new EmptyBorder(8, 8, 8, 8)));

		add(dirButton, "split 2");
		add(dirLable, "wrap");
		add(manageButton, "split 5");
		add(checkButton);
		add(updateAllButton);
		add(customButton);
		add(curseButton, "wrap");
		add(addonScrollPane);

		dirLable.setText(AUConfigUtil.getProperty("dir"));
		manageDialog = new AddonManagementDialog(this);
		curseDialog = new CurseForgeDialog(this);
		customDialog = new CustomAddonUrlDialog(this);

		// TODO
		customButton.setEnabled(false);
		curseButton.setEnabled(false);
	}

	// set components' listeners
	private void addListeners() {
		dirButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String path = AUUtil.isEmpty(AUConfigUtil.getProperty("dir")) ? "" : AUConfigUtil.getProperty("dir");
				fileChooser.showFileChooser(path, MainFrame.this);
			}
		});

		fileChooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File f = fileChooser.getSelectedFile();
				// not CANCEL
				if (f != null) {
					AUConfigUtil.setProperty("dir", f.getAbsolutePath());
					dirLable.setText(f.getAbsolutePath());
				}
			}
		});

		dirLable.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if ("text".equals(evt.getPropertyName())) {
					checkDirValidity();
				}
			}
		});

		manageButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				manageDialog.setVisible(true);
			}
		});

		checkButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				checkAddons();
			}
		});
		
		updateAllButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				addonTable.getAllAddonsFromTable().forEach(a -> {
					if (AUUpdater.HAS_UPDATE.equals(a.getStatus())) {
						AUUpdater.download(a);
					}
				});
			}
		});
		
		curseButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				curseDialog.setVisible(true);
			}
		});
	}

	private void checkDirValidity() {
		if (AUUtil.checkDirValidity()) {
			manageButton.setEnabled(true);
			checkButton.setEnabled(true);
			updateAllButton.setEnabled(true);
		} else {
			manageButton.setEnabled(false);
			checkButton.setEnabled(false);
			updateAllButton.setEnabled(false);
		}
	}

	/**
	 * load addons from db to table.
	 */
	public void loadAddon() {
		Vector<Addon> addons = dao.getAll();
		Collections.sort(addons);
		addons.forEach(a -> a.setTable(addonTable));
		addonTable.setData(addons);
	}

	/**
	 * try to check for updates.
	 */
	public void checkAddons() {
		addonTable.getAllAddonsFromTable().forEach(a -> {
			if (!AUUpdater.HAS_UPDATE.equals(a.getStatus())) {
				AUUpdater.check(addonTable, a);
			}
		});
		
//		addonTable.getAllAddonsFromTable().stream().filter(a -> !AUUpdater.HAS_UPDATE.equals(a.getStatus()))
//				.forEach(a -> AUUpdater.check(addonTable, a));
	}
	
	public void showCustomDialog(Addon a) {
		customDialog.show(a);
	}
}
