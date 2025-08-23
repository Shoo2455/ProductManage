package model.service;

import model.dao.UserDAO;

public class LoginService {
	private final UserDAO userDAO;

	public LoginService(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	// ログイン
	public boolean login(String username, String password) {
		if (username == null || password == null)
			return false;
		try {

			return userDAO.authenticate(username, password);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}