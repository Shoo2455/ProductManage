package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.entity.CategoryBean;

public class CategoryDAO {

	public List<CategoryBean> getAllCategories() throws Exception {
		List<CategoryBean> list = new ArrayList<>();
		String sql = "SELECT id, category_name FROM categories";

		try (Connection conn = ConnectionManager.getConnection();
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

	//以下⑪週目課題
	public void insertCategory(CategoryBean category) throws Exception {
		String sql = "INSERT INTO categories (id, category_name) VALUES (?, ?)";

		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, category.getId());
			stmt.setString(2, category.getName());
			stmt.executeUpdate();
		}
	}

}