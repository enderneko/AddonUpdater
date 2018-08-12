package enderneko.addonupdater.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import enderneko.addonupdater.domain.Addon;

/**
 * 扫描所有合法的插件.
 * 
 * @author enderneko Aug 6, 2018
 */
public final class AUScanner {

	public static Vector<Addon> scanAddons() {
		Vector<Addon> addons = new Vector<>();
		File folder = new File(AUConfigUtil.getProperty("dir") + "\\Interface\\AddOns");

		// prepare scanner
		Scanner sc = null;

		// iterate all addons
		if (folder.exists() && folder.isDirectory()) {

			for (File f : folder.listFiles()) {
				// locate .toc file
				File toc = new File(f.getPath() + "\\" + f.getName() + ".toc");
				if (toc.exists()) { // is an illegal addon
					Addon a = new Addon();
					try {
						sc = new Scanner(toc);
						Pattern titlePattern = Pattern.compile("## Title:\\s*(.+)");
						// version 不一定与 latestVersion 格式一致，不便于比较
						// Pattern versionPattern = Pattern.compile("## Version:\\s*(.+)");
						Pattern authorPattern = Pattern.compile("## Author:\\s*(.+)");

						while (sc.hasNextLine()) {
							String line = sc.nextLine();

							Matcher m;
							if (a.getName() == null) {
								m = titlePattern.matcher(line);
								if (m.find()) {
									a.setName(m.group(1).replaceAll("\\|c\\S{8}|\\|r", ""));
								}
							}

							// if (a.getVersion() == null) {
							// m = versionPattern.matcher(line);
							// if (m.find()) {
							// a.setVersion(m.group(1));
							// }
							// }

							if (a.getAuthor() == null) {
								m = authorPattern.matcher(line);
								if (m.find()) {
									a.setAuthor(m.group(1));
								}
							}
						}
						// must has title
						addons.add(a);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} finally {
						sc.close();
					}
				}
			}

		}
		return addons;
	}
}
