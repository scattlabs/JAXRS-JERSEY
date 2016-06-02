package scattlabs.jaxrs.jersey.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.springframework.jdbc.datasource.SingleConnectionDataSource;

/**
 * 
 * @author endang hidayat
 *
 */
public class MasterConnection {
	protected JdbcTemplate jt;
	protected SingleConnectionDataSource ds;
	protected Connection conn;
	public static Properties properties;
	private String username;
	private String password;

	static {
		try {
			MasterConnection.properties = new Properties();
			properties.load(MasterConnection.class.getResourceAsStream("/config.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void createConnection() {
		try {
			username = properties.getProperty("jdbc.user");
			password = properties.getProperty("jdbc.pass");

			System.out.println("Username  : " + username);
			System.out.println("Password: " + password);

			Class.forName(properties.getProperty("jdbc.class"));
			conn = DriverManager.getConnection(properties.getProperty("jdbc.url"), username, password);
			conn.setAutoCommit(false);
			// conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			ds = new SingleConnectionDataSource(conn, false);
			this.jt = new JdbcTemplate(conn, ds);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	protected void closeConnection() {
		if (conn != null) {
			try {
				conn.rollback();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
