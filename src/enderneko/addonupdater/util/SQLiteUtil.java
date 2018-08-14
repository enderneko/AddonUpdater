package enderneko.addonupdater.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteUtil {
	private static String fileName = System.getProperty("user.dir") + "\\db\\addons.db";

	public static Connection getConnection() {
		Connection conn = null;

		try {
			String url = "jdbc:sqlite:" + fileName;
			conn = DriverManager.getConnection(url);
			// System.out.println("Connection to SQLite has been established.");

			String sql = "CREATE TABLE IF NOT EXISTS addons (name text PRIMARY KEY, version text, author text, "
					+ "status text DEFAULT 'Not Checked', "
					+ "url text, latestVersion text, latestDate text, latestFile text)";

			Statement stmt = conn.createStatement();
			stmt.execute(sql);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
}
