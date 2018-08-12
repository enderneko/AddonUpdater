package enderneko.addonupdater.widget;

import javax.swing.JButton;

import enderneko.addonupdater.util.AUFont;

/**
 * 
 * @author enderneko
 * Aug 6, 2018
 */
@SuppressWarnings("serial")
public class AUButton extends JButton {
	public AUButton(String label) {
		super(label);
		setFont(AUFont.NORMAL);
		setFocusable(false);
//		setFocusPainted(false);
	}

//	@Override
//	protected void paintComponent(Graphics g) {
//		super.paintChildren(g);
//	}
}
