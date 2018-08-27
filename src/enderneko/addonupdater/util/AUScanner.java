package enderneko.addonupdater.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import enderneko.addonupdater.domain.Addon;

/**
 * 扫描所有合法的插件.
 * 
 * @author enderneko Aug 26, 2018
 */
public final class AUScanner {

	public static Vector<Addon> scanAddons() {
		Vector<Addon> addons = new Vector<>();
		File folder = new File(AUConfigUtil.getProperty("dir") + "\\Interface\\AddOns");

		// iterate all addons
		if (folder.exists() && folder.isDirectory()) {
			Pattern titlePattern = Pattern.compile("## Title:\\s*(.+)");
			// version 不一定与 latestVersion 格式一致
			Pattern versionPattern = Pattern.compile("## Version:\\s*(.+)");
			Pattern authorPattern = Pattern.compile("## Author:\\s*(.+)");

			for (File f : folder.listFiles()) {
				// locate .toc file
				File toc = new File(f.getPath() + "\\" + f.getName() + ".toc");

				if (toc.exists()) { // is an illegal addon
					Addon a = new Addon();
					try (Stream<String> s = Files.lines(toc.toPath())) {
						s.forEach(line -> {
							if (a.getName() == null) {
								Matcher m = titlePattern.matcher(line);
								if (m.find()) {
									a.setName(m.group(1).replaceAll("\\|c\\S{8}|\\|r", ""));
								}
							}
							
							if (a.getVersion() == null) {
								Matcher m = versionPattern.matcher(line);
								if (m.find()) {
									a.setVersion(m.group(1));
								}
							}
							
							if (a.getAuthor() == null) {
								Matcher m = authorPattern.matcher(line);
								if (m.find()) {
									a.setAuthor(m.group(1));
								}
							}
						});
						// must has title
						if (!AUUtil.isEmpty(a.getName())) {
							addons.add(a);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
		}
		return addons;
	}
}
