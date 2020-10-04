package p111;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Db {
	PreparedStatement pstmt = null;
	Connection conn = null;
	ResultSet resultSet = null;

	public Db() {

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 적재가 잘못되었습니다.");
			e.printStackTrace();
		}

		String user = "system";
		String pw = "rladudwls1@";
		String url = "jdbc:oracle:thin:@localhost:1521:XE";
		try {
			conn = DriverManager.getConnection(url, user, pw);
		} catch (SQLException e) {
			System.out.println("Connection이 잘못되었습니다.");
			e.printStackTrace();
		}
	}

	public void record(int score) {
		String sql = "insert into GAMESCORE(score) values(?)";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, score);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public int login(String id, String pass) {
		String sql = "select * from game where id=? and pass=?";
		int cnt=0;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, pass);
			resultSet = pstmt.executeQuery();
			while(resultSet.next()) {
				cnt++;
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return cnt;
	}
}
