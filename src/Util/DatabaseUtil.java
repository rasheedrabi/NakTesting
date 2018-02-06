package Util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import Configuration.Constant;

public class DatabaseUtil {

	Connection connection = null;
	Statement statement = null;
	static ResultSet resultSet = null;
	ResultSet ReturnresultSet = null;

	public DatabaseUtil() {
		connection = getConnection();
	}

	public ResultSet executeQuery(String query) {

		String schemaName = Constant.Schema;
		if (schemaName != null && schemaName.length() != 0) {
			try {
				statement = connection.createStatement();
				statement.executeUpdate("set current sqlid = " + schemaName);
				System.out.println("The schema is set successfully.");
			} catch (SQLException exception) {
				exception.printStackTrace();

			}
		}
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			resultSet = statement.executeQuery(query);

			if (!resultSet.next()) {
				System.out.println("no data");
				resultSet.beforeFirst();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return resultSet;

	}

	public void CloseDB() {
		try {
			resultSet.close();
			statement.close();
			connection.close();
		} catch (SQLException e) {
			Log.error(e.getStackTrace().toString());
			ExtentLogs.error(e.getStackTrace().toString());
		}
	}

	private static Connection getConnection() {

		Connection connection = null;

		try {
			// Required Jars : db2jcc_license_cu.jar and db2jcc.jar
			// Java SE 6 and higher automatically registers the driver.
			Class.forName("COM.ibm.db2os390.sqlj.jdbc.DB2SQLJDriver");

			Properties properties = new Properties();

			properties.setProperty("user", Constant.DB2UserName);
			properties.setProperty("password", Constant.DB2Password);
			String url = "jdbc:db2://" + Constant.DB2IP + ":" + Constant.DB2Port + "/" + Constant.DB2DatabaseName;
			connection = DriverManager.getConnection(url, properties);

			// Verify the Connection
			DatabaseMetaData metaData = connection.getMetaData();
			System.out.println("Database Name: " + metaData.getDatabaseProductName());
			System.out.println("Database Version: " + metaData.getDatabaseProductVersion());

		} catch (ClassNotFoundException e) {
			Log.error(e.getStackTrace().toString());
			ExtentLogs.error(e.getStackTrace().toString());
		} catch (SQLException e) {
			Log.error(e.getStackTrace().toString());
			ExtentLogs.error(e.getStackTrace().toString());
		}
		return connection;
	}
}