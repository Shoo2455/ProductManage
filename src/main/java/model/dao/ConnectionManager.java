package model.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.entity.CategoryBean;

public class ConnectionManager {
	public List<CategoryBean> getAllCategories() throws Exception {
		List<CategoryBean> list = new ArrayList<>();
		try (Connection conn = CategoryDAO.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM categories")) {

			while (rs.next()) {
				CategoryBean category = new CategoryBean();
				category.setId(rs.getInt("id"));
				category.setName(rs.getString("name"));
				list.add(category);
			}
		}
		return list;
	}
}
