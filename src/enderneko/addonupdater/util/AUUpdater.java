package enderneko.addonupdater.util;

import java.io.IOException;
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

	private static AUTable tbl;
	private static IAddonDao dao = AUConfigUtil.getAddonDAO();

	private static Map<String, Runnable> updateRunnables = new HashMap<>();
	private static Map<String, Runnable> downloadRunnables = new HashMap<>();
	private static ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
	private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
	private static ExecutorService downloadThreadPool = Executors.newFixedThreadPool(5);

	private static void update(Addon a, String url) {
		try {
			Document doc = Jsoup.connect(url).timeout(15000).get();
			Elements list = doc.getElementsByAttributeValue("class", "project-file-list__item");
			if (list.size() == 0) throw new IndexOutOfBoundsException();
			// first is the latest
			Element latest = list.get(0);
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

		} catch (IOException | IndexOutOfBoundsException e) {
			// not available
			a.setStatus(NOT_AVAILABLE);
		}
		updateRunnables.remove(a.getName());
	}

	// public static void updateAddonInfo(Addon a) {
	// // prevent create "same" thread again and again
	// if (!threads.containsKey(a.getName()) || threads.get(a.getName()).getState()
	// != Thread.State.RUNNABLE) {
	// Thread t = new Thread(() -> {
	// a.setStatus(CHECKING);
	// tbl.updateAndSort();
	// update(a, AUUtil.getAddonURL(a));
	// tbl.updateAndSort();
	// });
	// threads.put(a.getName(), t);
	// t.start();
	// }
	// }

	public static void updateAddonInfo(Addon a, ExecutorService executor) {
		if (!updateRunnables.containsKey(a.getName())) {
			a.setStatus(WAITING);
			tbl.refresh();
			Runnable r = new Runnable() {
				@Override
				public void run() {
					a.setStatus(CHECKING);
					tbl.refresh();
					update(a, AUUtil.getAddonURL(a));
					tbl.refresh();
				}
			};
			updateRunnables.put(a.getName(), r);
			executor.execute(r);
		}
	}

	public static void check(AUTable tbl, Addon a) {
		AUUpdater.tbl = tbl;
		updateAddonInfo(a, fixedThreadPool);
	}

	public static void download(Addon a) {
		if (!downloadRunnables.containsKey(a.getName())) {
			Runnable r = new Runnable() {
				@Override
				public void run() {
					try {
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
