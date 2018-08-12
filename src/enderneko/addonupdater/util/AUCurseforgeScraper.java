package enderneko.addonupdater.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * 
 * @author enderneko Aug 11, 2018
 */
public final class AUCurseforgeScraper {
	// https://www.curseforge.com/wow/addons?page=2
	private static final String CURSEFORGE = "https://www.curseforge.com/wow/addons?page=";

	private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);

	private static void excute(int page) {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				try {
					Document doc = Jsoup.connect(CURSEFORGE + page).timeout(15000).get();
					Elements list = doc.getElementsByAttributeValue("class", "project-list-item");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		
	}
	
	public static void start() {
		
	}
	
	public static void stop() {
		fixedThreadPool.shutdownNow();
	}
}
