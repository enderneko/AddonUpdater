package enderneko.addonupdater;

import javax.swing.SwingUtilities;

import enderneko.addonupdater.frame.MainFrame;

/**
 * 
 * @author enderneko
 * Aug 10, 2018
 */
public class Main {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new MainFrame().setVisible(true);
			}
		});
	}
}
