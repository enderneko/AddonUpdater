package enderneko.addonupdater.dao.impl.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import enderneko.addonupdater.dao.IAddonDao;
import enderneko.addonupdater.domain.Addon;
import enderneko.addonupdater.util.SQLiteUtil;

public class AddonDaoImpl implements IAddonDao {
	// private Connection conn = SQLiteUtil.getConnection();

	@Override
	public synchronized void add(Addon a) {
		String sql = "INSERT INTO addons(name, version, author, status, url, latestVersion, latestDate, latestFile) VALUES(?,?,?,?,?,?,?,?)";
		Connection conn = SQLiteUtil.getConnection();
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, a.getName());
			pstmt.setString(2, a.getVersion());
			pstmt.setString(3, a.getAuthor());
			pstmt.setString(4, a.getStatus());
			pstmt.setString(5, a.getUrl());
			pstmt.setString(6, a.getLatestVersion());
			pstmt.setString(7, a.getLatestDate());
			pstmt.setString(8, a.getLatestFile());

			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public synchronized Addon get(String name) {
		String sql = "SELECT * FROM addons WHERE name=?";
		Connection conn = SQLiteUtil.getConnection();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return resultSetToAddon(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public synchronized Vector<Addon> getAll() {
		Vector<Addon> addons = new Vector<>();
		String sql = "SELECT * FROM addons";
		Connection conn = SQLiteUtil.getConnection();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				Addon a = resultSetToAddon(rs);
				addons.add(a);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return addons;
	}

	@Override
	public synchronized boolean isManaged(String name) {
		String sql = "SELECT * FROM addons WHERE name=?";
		Connection conn = SQLiteUtil.getConnection();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public synchronized void update(Addon a) {
		String sql = "UPDATE addons SET version=?, author=?, url=?, status=?, latestVersion=?, latestDate=?, latestFile=? WHERE name=?";
		Connection conn = SQLiteUtil.getConnection();
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, a.getVersion());
			pstmt.setString(2, a.getAuthor());
			pstmt.setString(3, a.getUrl());
			pstmt.setString(4, a.getStatus());
			pstmt.setString(5, a.getLatestVersion());
			pstmt.setString(6, a.getLatestDate());
			pstmt.setString(7, a.getLatestFile());
			pstmt.setString(8, a.getName());

			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public synchronized void delete(String name) {
		String sql = "DELETE FROM addons WHERE name=?";
		Connection conn = SQLiteUtil.getConnection();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, name);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private Addon resultSetToAddon(ResultSet rs) {
		Addon a = new Addon();
		try {
			a.setName(rs.getString("name"));
			a.setVersion(rs.getString("version"));
			a.setAuthor(rs.getString("author"));
			a.setStatus(rs.getString("status"));
			a.setUrl(rs.getString("url"));
			a.setLatestVersion(rs.getString("latestVersion"));
			a.setLatestDate(rs.getString("latestDate"));
			a.setLatestFile(rs.getString("latestFile"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return a;
	}
}
