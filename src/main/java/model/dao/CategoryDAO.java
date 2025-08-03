package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import model.entity.CategoryBean;

public class CategoryDAO {

	public List<CategoryBean> getAllCategories() throws Exception {
		String sql = "SELECT id, name FROM categories";
		try (Connection conn = DBManager.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			List<CategoryBean> list = new ArrayList<>();
			while (rs.next()) {
				CategoryBean c = new CategoryBean();
				c.setId(rs.getInt("id"));
				c.setName(rs.getString("name"));
				list.add(c);
			}
			return list;
		}
	}
}