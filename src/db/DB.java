package db;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DB {
	
	private static Connection con = null;
	
	public static Connection getConnection(){
		if(con == null) {
			try {
				Properties props = loadProperties();
				String url = props.getProperty("dburl");
				con = DriverManager.getConnection(url, props);
			}
			catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
		return con;
	}
	
	public static void closeConnection() {
		if (con != null) {
			try {
				con.close();
			}
			catch(SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
	
	private static Properties loadProperties() {
		try (FileReader fs = new FileReader("db.properties")){
			Properties props = new Properties();
			props.load(fs);
			return props;
		}
		catch(IOException e) {
			throw new DbException(e.getMessage());
		}
	}
	
	public static void closeOperations(Statement st, ResultSet rs) throws DbException {
		try {
			if(st != null && rs != null) {
				st.close();
				rs.close();
			}
			else if (st != null) {
				st.close();
			}
			else if (rs != null) {
				rs.close();
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		
	}
	
	public static void closeOperation(Statement st) throws DbException{
		try {
			if(st != null) st.close();
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
	}
	
	public static void closeOperation(ResultSet rs) throws DbException{
		try {
			if(rs != null) rs.close();
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
	}

}
