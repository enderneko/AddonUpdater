package enderneko.addonupdater.util;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.apache.commons.io.FileUtils;

import enderneko.addonupdater.domain.Addon;
import net.lingala.zip4j.core.ZipFile;

/**
 * 
 * @author enderneko Aug 7, 2018
 */
public final class AUUtil {

	/**
	 * Check whether the selected directory is a WoW folder.
	 * 
	 * @return
	 */
	public static boolean checkDirValidity() {
		File f = new File(AUConfigUtil.getProperty("dir") + "\\Wow.exe");
		if (f != null && f.exists()) {
			return true;
		}
		return false;
	}

	/**
	 * Guess addon url.
	 * 
	 * @param a
	 * @return
	 */
	public static String guessAddonURL(Addon a) {
		// Set it! even if it's valid.
		a.setUrl("https://www.curseforge.com/wow/addons/" + a.getName().toLowerCase().replaceAll("[^a-zA-Z _\\-0-9]", "").replaceAll(" ", "-") + "/files");
		return a.getUrl();
	}

	/**
	 * Get addon update url.
	 * 
	 * @param a
	 * @return
	 */
	public static String getAddonURL(Addon a) {
		String url;
		if (a.getUrl() != null) {
			url = a.getUrl();
		} else {
			url = AUConfigUtil.getAddonUrl(a) != null ? AUConfigUtil.getAddonUrl(a) : guessAddonURL(a);
		}
		return url;
	}

	/**
	 * Get addon latest file url.
	 * https://stackoverflow.com/questions/14951696/java-urlconnection-get-the-final-redirected-url
	 * 
	 * @author Duyhungws
	 * @param url
	 * @return
	 */
	public static String getFinalURL(String url) throws IOException {
		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		conn.setConnectTimeout(15000);
		conn.setInstanceFollowRedirects(false);
		conn.connect();
		conn.getInputStream();
		int responseCode = conn.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_MOVED_PERM || responseCode == HttpURLConnection.HTTP_MOVED_TEMP
				|| responseCode == 307) {
			String redirectUrl = conn.getHeaderField("Location");
			// System.out.println("responseCode: " + responseCode + ", redirect: " +
			// redirectUrl);
			return getFinalURL(redirectUrl);
		}
		return url;
	}

	public static void extractAndMove(Addon a) {
		String fileName = a.getLatestFile();
		fileName = fileName.substring(fileName.lastIndexOf('/') + 1); // WeakAuras-2.6.6.zip
		String temp = System.getProperty("user.dir") + "\\temp";
		String addonsFolder = AUConfigUtil.getProperty("dir") + "\\Interface\\AddOns\\";

		File zip = new File(temp + "\\" + fileName);
		File source = new File(temp + "\\" + fileName.substring(0, fileName.length() - 3)); // temp\WeakAuras-2.6.6

		try {
			// extract
			ZipFile zipFile = new ZipFile(zip);
			zipFile.extractAll(source.getAbsolutePath()); // temp\WeakAuras-2.6.6
			// get folder(s)
			String[] subFolders = source.list();

			for (String fName : subFolders) {
				// delete old(s)
				File old = new File(addonsFolder + fName);
				if (old.exists()) { FileUtils.deleteDirectory(old); }
				// move
				FileUtils.moveDirectory(new File(source, fName), new File(addonsFolder, fName));
				// delete temp
				new Timer().schedule(new TimerTask() {
					@Override
					public void run() {
						zip.delete();
						source.delete();
					}
				}, 1000);
			}

			a.setVersion(a.getLatestVersion());
			a.setStatus(AUUpdater.UP_TO_DATE);
			a.getTable().refreshAndSave(a);
		} catch (Exception e) {
			e.printStackTrace();
			a.setStatus(AUUpdater.EXTRACTION_FAILED);
			a.getTable().refresh();
		}
	}

	/**
	 * if file not exists then create it.
	 * @param addonUrlFile
	 */
	public static void checkFile(File f) {
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void checkDir(File d) {
		if (!d.exists() || !d.isDirectory()) {
			d.mkdir();
		}
	}

	public static Image getImage(String fileName) {
		return ((ImageIcon) getIcon(fileName)).getImage();
	}
	
	public static Icon getIcon(String fileName) {
		return new ImageIcon(System.getProperty("user.dir") + "\\config\\" + fileName);
	}
	
	public static boolean isEmpty(String s) {
		return (s == null || s.trim().equals(""));
	}
	
	public static boolean isAddonUrl(String s) {
		return s.startsWith("https://www.curseforge.com/wow/addons/") && s.endsWith("/files") && s.length() > 38 + 6;
	}

	/**
	 * Get web content from a connection.
	 * 
	 * @param conn
	 * @return
	 */
	// public static StringBuffer getContentFromConnection(URLConnection conn) {
	// StringBuffer sb = new StringBuffer();
	// try {
	// InputStream in = conn.getInputStream();
	// BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));
	// while (br.ready()) {
	// sb.append(br.readLine());
	// }
	//
	// br.close();
	// in.close();
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return sb;
	// }
}
