package enderneko.addonupdater.frame;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

/**
 * 
 * @author enderneko
 * Aug 6, 2018
 */
@SuppressWarnings("serial")
public class AUFileChooser extends JFileChooser {
	public AUFileChooser() {
		setFileSelectionMode(DIRECTORIES_ONLY);
		setMultiSelectionEnabled(false);
	}
	
	public void showFileChooser(String path, Component parent) {
		File dir = new File(path);
		if (dir != null && dir.exists()) {
			setCurrentDirectory(dir);
		} else {
			setCurrentDirectory(FileSystemView.getFileSystemView().getHomeDirectory());
		}
		showOpenDialog(parent);
	}
}
