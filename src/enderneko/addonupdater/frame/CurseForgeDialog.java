package enderneko.addonupdater.frame;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import enderneko.addonupdater.widget.AUButton;
import enderneko.addonupdater.widget.AULabel;
import net.miginfocom.swing.MigLayout;

/**
 * 
 * @author enderneko
 * Aug 12, 2018
 */
@SuppressWarnings("serial")
public class CurseForgeDialog extends JDialog {
	private MainFrame owner;
	private JPanel scrapePanel = new JPanel();
	private JPanel searchPanel = new JPanel();
	
	private AULabel fromLabel = new AULabel("From:");
	private AULabel toLabel = new AULabel("To:");
	private JComboBox<Integer> fromComboBox = new JComboBox<>();
	private JComboBox<Integer> toComboBox = new JComboBox<>();
	private AUButton scrapeButton = new AUButton("Scrape!");
	
	public CurseForgeDialog(MainFrame owner) {
		super(owner, "CurseForge Scraping", true);
		this.owner = owner;
		setSize(400, 600);
		setResizable(false);
		setLocationRelativeTo(owner);
		setLayout(new MigLayout("insets 5", "[grow]", "[grow]"));
		
		initWidgets();
	}
	
	private void initWidgets() {
		add(scrapePanel, "grow,wrap");
		add(searchPanel, "grow");
		
		scrapePanel.setBorder(new CompoundBorder(new TitledBorder(" Scrape "), new EmptyBorder(1, 1, 1, 1)));
		searchPanel.setBorder(new CompoundBorder(new TitledBorder(" Search "), new EmptyBorder(1, 1, 1, 1)));
		scrapePanel.setLayout(new MigLayout("", "[][][right,grow]", "[c]"));
		searchPanel.setLayout(new MigLayout());
		
		fromComboBox.setFocusable(false);
		toComboBox.setFocusable(false);
		scrapePanel.add(fromLabel, "split 2");
		scrapePanel.add(fromComboBox, "w 50");
		scrapePanel.add(toLabel, "split 2");
		scrapePanel.add(toComboBox, "w 50");
		scrapePanel.add(scrapeButton, "w 100, right");
	}
}
