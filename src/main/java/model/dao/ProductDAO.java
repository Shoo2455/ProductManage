package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.entity.ProductBean;

public class ProductDAO {

	public List<ProductBean> getAllProducts() throws Exception {
		String sql = "SELECT id, name, price, stock, category_id FROM products";
		try (Connection conn = DBManager.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			List<ProductBean> list = new ArrayList<>();
			while (rs.next()) {
				ProductBean p = new ProductBean();
				p.setId(rs.getInt("id"));
				p.setName(rs.getString("name"));
				p.setPrice(rs.getInt("price"));
				p.setStock(rs.getInt("stock"));
				p.setCategoryId(rs.getInt("category_id"));
				list.add(p);
			}
			return list;
		}
	}

	
	
	public void addProduct(ProductBean p) throws Exception {
		String sql = "INSERT INTO products(name, price, stock, category_id) VALUES(?,?,?,?)";
		try (Connection conn = DBManager.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, p.getName());
			ps.setInt(2, p.getPrice());
			ps.setInt(3, p.getStock());
			ps.setInt(4, p.getCategoryId());
			ps.executeUpdate();
		}
	}
	
	public boolean deleteProductById(int id) throws Exception {
	    String sql = "DELETE FROM products WHERE id = ?";

	    try (Connection conn = DBManager.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

	        stmt.setInt(1, id);
	        int result = stmt.executeUpdate();
	        return result > 0;
	    }
	}



	public List<ProductBean> findAll() throws Exception {
	    List<ProductBean> list = new ArrayList<>();
	
	    try (Connection conn = DBManager.getConnection();
	         Statement stmt = conn.createStatement();
	         ResultSet rs = stmt.executeQuery("SELECT * FROM products")) {
	
	        while (rs.next()) {
	            ProductBean p = new ProductBean();
	            p.setId(rs.getInt("id"));
	            p.setName(rs.getString("name"));
	            p.setPrice(rs.getInt("price"));
	            p.setStock(rs.getInt("stock"));
	            p.setCategoryId(rs.getInt("category_id"));
	            list.add(p);
	        }
	    }
	
	    return list;
	}
	
}
