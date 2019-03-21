package enderneko.addonupdater.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import enderneko.addonupdater.dao.IAddonDao;
import enderneko.addonupdater.domain.Addon;
import enderneko.addonupdater.widget.AUTable;

/**
 * Check and Update.
 * 
 * @author enderneko Aug 10, 2018
 */
public final class AUUpdater {
	public static final String NOT_CHECKED = "Not Checked";
	public static final String WAITING = "Waiting...";
	public static final String CHECKING = "Checking...";
	public static final String GETTING_FILE = "Getting File...";
	public static final String DOWNLOADING = "Downloading... ";
	public static final String DOWNLOAD_FAILED = "Download Failed";
	public static final String UNZIPPING = "Unzipping...";
	public static final String EXTRACTION_FAILED = "Extraction Failed";
	public static final String UP_TO_DATE = "Up to Date";
	public static final String HAS_UPDATE = "Has Update";
	public static final String NOT_AVAILABLE = "Not Available";
	
	public static final String TUKUI = "https://www.tukui.org";

	private static AUTable tbl;
	private static IAddonDao dao = AUConfigUtil.getAddonDAO();

	private static Map<String, Runnable> updateRunnables = new HashMap<>();
	private static Map<String, Runnable> downloadRunnables = new HashMap<>();
	// private static ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
	private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
	private static ExecutorService downloadThreadPool = Executors.newFixedThreadPool(5);

	private static void fetch(Addon a, String url) {
		try {
			Document doc = Jsoup.connect(url).timeout(15000).get();
			Elements list = doc.getElementsByAttributeValue("class", "project-file-list__item");
			if (list.size() == 0) throw new IndexOutOfBoundsException();
			
			Element latest = null;
			
			if ("true".equals(AUConfigUtil.getProperty("allowAlphaOrBeta"))) {
				// first is the latest
				latest = list.get(0);
			
			} else {
				boolean found = false;
				for (Element ele : list) {
					String releaseType = ele.getElementsByAttributeValue("class", "project-file__release-type").get(0).child(0).attr("title");
					if ("Release".equals(releaseType)) {
						latest = ele;
						found = true;
						break;
					}
				}
				if (!found) latest = list.get(0); // not found in this page
			}
			
			a.setUrl(url);
			a.setLatestVersion(latest.getElementsByAttributeValue("class", "table__content file__name full").get(0).text());
			a.setLatestDate(
					latest.getElementsByAttributeValue("class", "tip standard-date standard-datetime").get(0).text());
			
			a.setStatus(GETTING_FILE);
			tbl.refresh();
			
			String href = latest.getElementsByAttributeValue("data-action", "download-file").attr("href");
			a.setLatestFile(AUUtil.getFinalURL("https://www.curseforge.com" + href + "/file"));

			if (a.getLatestVersion().equals(a.getVersion())) {
				a.setStatus(UP_TO_DATE);
			} else {
				a.setStatus(HAS_UPDATE);
			}
			dao.update(a);

		} catch (Exception e) {
			e.printStackTrace();
			// not available
			a.setStatus(NOT_AVAILABLE);
		}
		updateRunnables.remove(a.getName());
	}
	
	private static void fetchTUKUI(Addon a) {
		try {
			String url = TUKUI + "/download.php?ui=" + a.getName().toLowerCase();
			Document doc = Jsoup.connect(url).timeout(15000).get();
			
			Element linkEl = doc.getElementsByAttributeValue("class", "btn btn-mod btn-border-w btn-round btn-large").get(0);
			String latestVersion = doc.getElementsByAttributeValue("class", "Premium").get(0).text();
			String latestDate = doc.getElementsByAttributeValue("class", "Premium").get(1).text();
			
			a.setUrl(url);
			a.setLatestVersion(latestVersion);
			a.setLatestDate(latestDate);

			a.setStatus(GETTING_FILE);
			tbl.refresh();
			
			a.setLatestFile(TUKUI + linkEl.attr("href"));
			
			if (a.getLatestVersion().equals(a.getVersion())) {
				a.setStatus(UP_TO_DATE);
			} else {
				a.setStatus(HAS_UPDATE);
			}
			dao.update(a);

		} catch (Exception e) {
			// not available
			a.setStatus(NOT_AVAILABLE);
		}
		updateRunnables.remove(a.getName());
	}

	private static void fetchAddonInfo(Addon a, ExecutorService executor) {
		if (!updateRunnables.containsKey(a.getName())) {
			a.setStatus(WAITING);
			tbl.refresh();
			Runnable r = new Runnable() {
				@Override
				public void run() {
					a.setStatus(CHECKING);
					tbl.refresh();
					if ("Tukui".equals(a.getName()) || "ElvUI".equals(a.getName())) {
						fetchTUKUI(a);
					} else {
						fetch(a, AUUtil.getAddonURL(a));
					}
				}
			};
			updateRunnables.put(a.getName(), r);
			executor.execute(r);
		}
	}

	public static void check(AUTable tbl, Addon a) {
		AUUpdater.tbl = tbl;
		fetchAddonInfo(a, fixedThreadPool);
	}

	public static void download(Addon a) {
		if (!downloadRunnables.containsKey(a.getName())) {
			Runnable r = new Runnable() {
				@Override
				public void run() {
					try {
						a.setStatus(DOWNLOADING);
						a.getTable().refresh();
						Download d = new Download(new URL(a.getLatestFile()));
						d.addObserver(a);
					} catch (MalformedURLException e) {
						e.printStackTrace();
					}
				}
			};
			downloadRunnables.put(a.getName(), r);
			downloadThreadPool.execute(r);
		}
	}
	
	public static void extract(Addon a) {
		AUUtil.extractAndMove(a);
		downloadRunnables.remove(a.getName()); // download finished
	}
}
