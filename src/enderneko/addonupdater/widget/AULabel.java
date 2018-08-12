package enderneko.addonupdater.widget;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import enderneko.addonupdater.util.AUFont;

@SuppressWarnings("serial")
public class AULabel extends JLabel {
	public AULabel(String text) {
		super(text, SwingConstants.LEFT);
		setFont(AUFont.NORMAL);
//		setSize(width, height);
//		getText().
	}
}
