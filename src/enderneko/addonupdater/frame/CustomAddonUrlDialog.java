package enderneko.addonupdater.frame;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JDialog;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import enderneko.addonupdater.dao.IAddonDAO;
import enderneko.addonupdater.dao.impl.AddonDAOImpl;
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
	private IAddonDAO dao = new AddonDAOImpl();
	
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
