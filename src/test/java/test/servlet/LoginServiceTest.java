package test.servlet;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.dao.DBManager;
import model.dao.UserDAO;
import model.service.LoginService;

class LoginServiceTest {

	private UserDAO userDAO;
	private LoginService service;

	private static final String U_NAME = "user";
	private static final String U_PASS = "1111";

	@BeforeEach
	void setUp() throws Exception {
		userDAO = new UserDAO();
		service = new LoginService(userDAO);

		try (Connection con = DBManager.getConnection()) {
			try (PreparedStatement del = con.prepareStatement(
					"DELETE FROM users WHERE username = ?")) {
				del.setString(1, U_NAME);
				del.executeUpdate();
			}
			try (PreparedStatement ins = con.prepareStatement(
					"INSERT INTO users(username, password) VALUES (?, ?)")) {
				ins.setString(1, U_NAME);
				ins.setString(2, U_PASS);
				ins.executeUpdate();
			}
		}
	}

	@AfterEach
	void tearDown() throws Exception {
		try (Connection con = DBManager.getConnection();
				PreparedStatement pst = con.prepareStatement(
						"DELETE FROM users WHERE username = ?")) {
			pst.setString(1, U_NAME);
			pst.executeUpdate();
		}
	}

	@Test
	void 正しい資格情報ならログイン成功() {
		assertTrue(service.login(U_NAME, U_PASS));
	}

	@Test
	void パスワード誤りなら失敗() {
		assertFalse(service.login(U_NAME, "wrong"));
	}

	@Test
	void 未登録ユーザーなら失敗() {
		assertFalse(service.login("no_user", U_PASS));
	}

	@Test
	void null入力は失敗() {
		assertFalse(service.login(null, U_PASS));
		assertFalse(service.login(U_NAME, null));
	}
}