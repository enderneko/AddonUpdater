package enderneko.addonupdater.widget;

import java.awt.Point;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import enderneko.addonupdater.domain.Addon;
import enderneko.addonupdater.frame.MainFrame;
import enderneko.addonupdater.util.AUUtil;

/**
 * 
 * @author enderneko
 * Aug 12, 2018
 */
@SuppressWarnings("serial")
public class AUPopupMenu extends JPopupMenu {
	private MainFrame owner;
	private Addon currentAddon;
	private JMenuItem custom = new JMenuItem("Custom URL");
	
	public AUPopupMenu() {
		initItems();
	}
	
	private void initItems() {
		add(custom);
		custom.setIcon(AUUtil.getIcon("link.png"));
		custom.addActionListener(e -> {
			owner.showCustomDialog(currentAddon);
		});
	}
	
	public void showPopupMenu(MainFrame owner, AUTable t, Point p, Addon a) {
		this.owner = owner;
		currentAddon = a;
		show(t, (int) p.getX(), (int) p.getY());
	}
}
