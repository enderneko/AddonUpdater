package enderneko.addonupdater.frame;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JDialog;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import enderneko.addonupdater.dao.IAddonDao;
import enderneko.addonupdater.dao.impl.xml.AddonDaoImpl;
import enderneko.addonupdater.domain.Addon;
import enderneko.addonupdater.util.AUConfigUtil;
import enderneko.addonupdater.util.AUUtil;
import enderneko.addonupdater.widget.AUButton;
import enderneko.addonupdater.widget.AULabel;
import net.miginfocom.swing.MigLayout;

/**
 * 
 * @author enderneko
 * Aug 12, 2018
 */
@SuppressWarnings("serial")
public class CustomAddonUrlDialog extends JDialog {
	private Addon currentAddon;
	private AULabel infoLabel = new AULabel("Addon: ");
	private JTextField urlField = new JTextField();
	private AUButton confirmButton = new AUButton("Confirm");
	private IAddonDao dao = AUConfigUtil.getAddonDAO();
	
	// TODO java.awt.IllegalComponentStateException: component must be showing on the screen to determine its location
	public CustomAddonUrlDialog(MainFrame owner) {
		super(owner, "Custom Addon URL", true);
		setSize(350, 100);
		setResizable(false);
		setLocationRelativeTo(owner);
		setLayout(new MigLayout("", "[grow][]", "[grow]"));
		
		initWidgets();
	}

	private void initWidgets() {
		add(infoLabel, "wrap");
		add(urlField, "growx");
		add(confirmButton);
		
		urlField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				check(urlField.getText());
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				check(urlField.getText());
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				check(urlField.getText());
			}
		});
		
		confirmButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// set to Addon object
				currentAddon.setUrl(urlField.getText().trim().toLowerCase());
				dao.update(currentAddon);
				// save to addonUrl.ini for further use
				AUConfigUtil.setAddonUrl(currentAddon.getName(), urlField.getText().trim().toLowerCase());
				setVisible(false);
			}
		});
		
		// TODO 当该窗口关闭之前如果有过“不在最前”的情况（即切到其他窗口再切回来），关闭时会报错
		// java.awt.IllegalComponentStateException: component must be showing on the screen to determine its location
	}
	
	private void check(String s) {
		if (AUUtil.isAddonUrl(s.trim().toLowerCase())) {
			confirmButton.setEnabled(true);
		} else {
			confirmButton.setEnabled(false);
		}
	}

	public void show(Addon a) {
		currentAddon = a;
		confirmButton.setEnabled(false);
		infoLabel.setText("Addon: " + a.getName());
		urlField.setText(a.getUrl());
		
		setVisible(true);
	}
}
