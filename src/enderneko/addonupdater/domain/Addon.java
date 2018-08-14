package enderneko.addonupdater.domain;

import java.util.Observable;
import java.util.Observer;

import enderneko.addonupdater.util.AUUpdater;
import enderneko.addonupdater.util.Download;
import enderneko.addonupdater.widget.AUTable;

/**
 * Addon's name must be unique.
 * 
 * @author enderneko Aug 6, 2018
 */
public class Addon implements Comparable<Addon>, Observer {
	private String name;
	private String version;
	private String author;
	private String status = "Not Checked";
	private String url;
	private String latestVersion;
	private String latestDate;
	private String latestFile;

	private AUTable table;

	public Addon() {
	}

	public Addon(String name) {
		this.name = name;
	}

	public Addon(String name, String version, String author, String status, String url, String latestVersion,
			String latestDate, String latestFile) {
		super();
		this.name = name;
		this.version = version;
		this.author = author;
		this.status = status;
		this.url = url;
		this.latestVersion = latestVersion;
		this.latestDate = latestDate;
		this.latestFile = latestFile;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLatestVersion() {
		return latestVersion;
	}

	public void setLatestVersion(String latestVersion) {
		this.latestVersion = latestVersion;
	}

	public String getLatestDate() {
		return latestDate;
	}

	public void setLatestDate(String latestDate) {
		this.latestDate = latestDate;
	}

	public String getLatestFile() {
		return latestFile;
	}

	public void setLatestFile(String latestFile) {
		this.latestFile = latestFile;
	}

	public AUTable getTable() {
		return table;
	}

	public void setTable(AUTable table) {
		this.table = table;
	}

	@Override
	public void update(Observable o, Object arg) {
		Integer state = (Integer) arg;
		switch (state) {
		case Download.DOWNLOADING:
			setStatus(AUUpdater.DOWNLOADING + Math.round(((Download) o).getProgress()) + "%");
			table.refresh();
			break;
		case Download.ERROR:
			setStatus(AUUpdater.DOWNLOAD_FAILED);
			table.refresh();
			break;
		case Download.COMPLETE:
			setStatus(AUUpdater.UNZIPPING);
			AUUpdater.extract(Addon.this);
			table.refresh();
		}
	}

	/**
	 * 仅用name区分是否是同一个Addon, 仅按name排序
	 */
	@Override
	public int compareTo(Addon a) {
		return this.name.compareTo(a.name);
	}

	/**
	 * 仅用name区分是否是同一个Addon
	 */
	@Override
	public boolean equals(Object obj) {
		return name.equals(((Addon) obj).getName());
	}

	@Override
	public String toString() {
		return "Addon [name=" + name + ", version=" + version + ", author=" + author + ", status=" + status + ", url="
				+ url + ", latestVersion=" + latestVersion + ", latestDate=" + latestDate + ", latestFile=" + latestFile
				+ "]";
	}
}
