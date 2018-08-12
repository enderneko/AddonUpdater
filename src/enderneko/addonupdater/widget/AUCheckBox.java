package enderneko.addonupdater.widget;

import javax.swing.JCheckBox;

import enderneko.addonupdater.domain.Addon;
import enderneko.addonupdater.util.AUFont;

/**
 * 
 * @author enderneko
 * Aug 8, 2018
 */
@SuppressWarnings("serial")
public class AUCheckBox extends JCheckBox implements Comparable<AUCheckBox> {
	public Addon addon;
	public AUCheckBox(Addon a) {
		super(a.getName());
		addon = a;
		setFont(AUFont.NORMAL);
	}

	@Override
	public int compareTo(AUCheckBox cb) {
		return this.getText().compareTo(cb.getText());
	}
}
