package JDBC_Application.JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @author Yoeun Sathya
 *
 */
public class App {
	
	private static final Logger logger = LogManager.getLogger(App.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		App localMain = new App();
		logger.info(" --- Welcome to JDBC TryOn --- ");	
		//		Class.forName("com.mysql.jdbc.Driver");
		String url = "jdbc:mysql://localhost:3306/javacambodia-jdbc";
		String username = "root";
		String password = "";

		Connection con = DriverManager.getConnection(url, username, password);
		if (con != null) {
			System.out.println("Database Connected successfully");
			logger.info("Database connected!");
		} else {
			System.out.println("Database Connection failed");
			logger.info("Database connected failed!");
		}
		
		// Select statement
		Statement stmt = con.createStatement();
		localMain.selectOutput(stmt);
		
		localMain.insertNewStudent(stmt, "Kosal", "Tep");
		localMain.insertNewStudent(stmt, "Raksmey", "Vy");
		
		logger.info("All students now: ");
		localMain.selectOutput(stmt);
		
		stmt.close();
		con.close();
		logger.info("Connection has been closed.");
		
		

	}
	
	/**
	 * Syntax insert into 
	 * @param stmt
	 * @throws SQLException
	 */
	private void selectOutput(Statement stmt) throws SQLException {
		String selectSql = "SELECT stu_id, stu_first_name, stu_last_name FROM td_student";
		ResultSet rs = stmt.executeQuery(selectSql);
		
		while(rs.next()) {
			int id = rs.getInt("stu_id");
			String firstname = rs.getString("stu_first_name");
			String lastName = rs.getString("stu_last_name");
			
			logger.info("> " + id + " : " + firstname + " " + lastName);
		}
		logger.info("Select has been done");
		rs.close();
	}
	
	/**
	 * Basic/simple way to insert into database using raw syntax of MySQL
	 * 
	 * @param stmt
	 * @param firstname
	 * @param lastname
	 * @throws SQLException
	 */
	private void insertNewStudent(Statement stmt, String firstname, String lastname) throws SQLException {
		String insertSql = "insert into td_student (stu_first_name, stu_last_name) "
				+ "(SELECT '" + firstname + "', '" + lastname + "' FROM DUAL "
				+ "	WHERE NOT EXISTS (select 1 from td_student "
				+ "			WHERE stu_first_name = '" + firstname + "' AND stu_last_name = '" + lastname + "'))";
		
		String fullname = firstname + " " + lastname;
		int indx = stmt.executeUpdate(insertSql);
		
		if (indx == 1) {
			logger.info(fullname + " is inserted.");
		} else {
			logger.warn(fullname + " is not inserted.");
		}
	}

}
