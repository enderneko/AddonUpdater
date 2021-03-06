package enderneko.addonupdater.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import enderneko.addonupdater.dao.IAddonDao;
import enderneko.addonupdater.domain.Addon;

public final class AUConfigUtil {
	private static Properties config = new Properties();
	private static File configFile;
	private static Properties addonUrl = new Properties();
	private static File addonUrlFile;
	private static File temp;
	private static File db;
	private static IAddonDao dao;

	static {
		try {
			AUUtil.checkDir(new File(System.getProperty("user.dir") + "\\config"));
			
			configFile = new File(System.getProperty("user.dir") + "\\config\\config.ini");
			AUUtil.checkFile(configFile);
			config.load(new FileInputStream(configFile));

			addonUrlFile = new File(System.getProperty("user.dir") + "\\config\\addonUrl.ini");
			AUUtil.checkFile(addonUrlFile);
			addonUrl.load(new FileInputStream(addonUrlFile));
			
			temp = new File(System.getProperty("user.dir") + "\\temp");
			AUUtil.checkDir(temp);
			
			db = new File(System.getProperty("user.dir") + "\\db");
			AUUtil.checkDir(db);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// get config
	public static String getProperty(String key) {
		return config.getProperty(key);
	}

	// set config and save to file
	public static void setProperty(String key, String value) {
		config.setProperty(key, value);
		writeToFile(config, configFile);
	}

	// get addon url from file.
	public static String getAddonUrl(Addon a) {
		return addonUrl.getProperty(a.getName());
	}
	
	// store addon url to file.
	public static void setAddonUrl(String addonName, String url) {
		addonUrl.setProperty(addonName, url);
		writeToFile(addonUrl, addonUrlFile);
	}
	
	public static synchronized IAddonDao getAddonDAO() {
		if (dao == null) {
			String daoName = config.getProperty("dao");
			if (!AUUtil.isEmpty(daoName)) {
				try {
					dao = (IAddonDao) Class.forName(daoName).newInstance();
				} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
					e.printStackTrace();
				}
			} else {
				dao = new enderneko.addonupdater.dao.impl.sqlite.AddonDaoImpl();
			}
		}
		return dao;
	}

	// public static List<String> getManagedAddons() {
	// Set<String> properties = addon.stringPropertyNames();
	// return properties.parallelStream()
	// .filter(p -> checkManagement(p))
	// .collect(Collectors.toList());
	// }

	// // set addon management
	// public static void setManagement(String name, boolean b) {
	// addon.setProperty(name, b ? "true" : "false");
	// writeToFile(addon, addonFile);
	// }

	private static void writeToFile(Properties p, File f) {
		try {
			p.store(new FileOutputStream(f), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
