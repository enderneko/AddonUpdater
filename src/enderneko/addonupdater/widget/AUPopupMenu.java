package enderneko.addonupdater.widget;

import java.awt.Point;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import enderneko.addonupdater.domain.Addon;
import enderneko.addonupdater.frame.MainFrame;
import enderneko.addonupdater.util.AUUpdater;
import enderneko.addonupdater.util.AUUtil;

/**
 * 
 * @author enderneko
 * Aug 12, 2018
 */
@SuppressWarnings("serial")
public class AUPopupMenu extends JPopupMenu {
	private MainFrame main;
	private AUTable table;
	private Addon addon;
	private JMenuItem reinstall = new JMenuItem("Reinstall");
	private JMenuItem check = new JMenuItem("Check for updates");
	private JMenuItem upToDate = new JMenuItem("Already up-to-date");
	private JMenuItem custom = new JMenuItem("Custom URL");
	
	public AUPopupMenu() {
		init();
	}
	
	private void init() {
		add(reinstall);
		add(check);
		add(upToDate);
		add(custom);
		
		// disabled by default, only available if addon has latestFile
		reinstall.setIcon(AUUtil.getIcon("reinstall.png"));
		reinstall.addActionListener(e -> AUUpdater.download(addon));
		
		check.setIcon(AUUtil.getIcon("check.png"));
		check.addActionListener(e -> AUUpdater.check(table, addon));
		
		// disabled by default, only available after check
		upToDate.setIcon(AUUtil.getIcon("uptodate.png"));
		upToDate.addActionListener(e -> {
			addon.setVersion(addon.getLatestVersion());
			addon.setStatus(AUUpdater.UP_TO_DATE);
			table.refreshAndSave(addon);
		});
		
		custom.setIcon(AUUtil.getIcon("link.png"));
		custom.addActionListener(e -> main.showCustomDialog(addon));

		addPopupMenuListener(new PopupMenuListener() {
			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				// reinstall LATEST version in db
				if (!AUUtil.isEmpty(addon.getLatestFile())) {
					reinstall.setEnabled(true);
				} else {
					reinstall.setEnabled(false);
				}
				
				// already checked, not always reliable
				if (AUUpdater.HAS_UPDATE.equals(addon.getStatus())) {
					upToDate.setEnabled(true);
				} else {
					upToDate.setEnabled(false);
				}
			}
			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) { }
			@Override
			public void popupMenuCanceled(PopupMenuEvent e) { }
		});
	}
	
	public void showPopupMenu(MainFrame m, AUTable t, Point p, Addon a) {
		if (AUUtil.checkDirValidity()) {
			main = m;
			table = t;
			addon = a;
			show(t, (int) p.getX(), (int) p.getY());
		}
	}
}
