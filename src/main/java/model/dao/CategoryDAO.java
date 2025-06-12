package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.entity.CategoryBean;

public class CategoryDAO {

	public static Connection getConnection() throws Exception {
		Class.forName("com.mysql.cj.jdbc.Driver");
		return DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/product_management", "Shoo2455", "Shoo0036");
	}

	public List<CategoryBean> getAllCategories() throws Exception {
		List<CategoryBean> list = new ArrayList<>();
		String sql = "SELECT id, category_name FROM categories";

		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("category_name");
				CategoryBean category = new CategoryBean(id, name);
				list.add(category);
			}

		} catch (Exception e) {
			System.out.println("【エラー】カテゴリ一覧取得中に例外発生");
			e.printStackTrace();
		}

		return list;
	}
}