package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {

	public boolean authenticate(String username, String password) throws Exception {
		boolean isValid = false;

		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement(
						"SELECT * FROM users WHERE username = ? AND password = ?")) {

			stmt.setString(1, username);
			stmt.setString(2, password);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					isValid = true;
				}
			}
		}

		return isValid;
	}
}