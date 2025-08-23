package model.service;

import java.util.List;

import model.dao.ProductDAO;
import model.entity.ProductBean;

public class ProductService {
	private final ProductDAO productDAO;

	public ProductService(ProductDAO productDAO) {
		this.productDAO = productDAO;
	}

	// 一覧
	public List<ProductBean> listAll() {
		try {
			return productDAO.findAll();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// 追加
	public ProductBean add(String name, int price, int stock) {
		if (name == null || name.isBlank())
			throw new IllegalArgumentException("name is required");
		if (price < 0)
			throw new IllegalArgumentException("price must be >= 0");
		if (stock < 0)
			throw new IllegalArgumentException("stock must be >= 0");

		ProductBean p = new ProductBean();
		p.setName(name);
		p.setPrice(price);
		p.setStock(stock);
		p.setCategoryId(1);

		try {
			productDAO.addProduct(p);
			return p;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// 編集
	public void edit(int id, String name, int price, int stock) {
		ProductBean existing;
		try {
			existing = productDAO.getProductById(id);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		if (existing == null) {
			throw new IllegalArgumentException("product not found: " + id);
		}

		existing.setName(name);
		existing.setPrice(price);
		existing.setStock(stock);

		try {
			productDAO.updateProduct(existing);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// 削除
	public boolean remove(int id) {
		try {
			return productDAO.deleteProductById(id);
		} catch (Exception e) {
			return false;
		}

	}
}