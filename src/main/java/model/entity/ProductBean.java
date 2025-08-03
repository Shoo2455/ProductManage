package model.entity;

public class ProductBean {
	private int id;
	private String name;
	private int price;
	private int stock;
	private int categoryId;

	//コンストラクター
	public ProductBean() {
	}

	public ProductBean(String name, int price, int stock, int categoryId) {
		this.name = name;
		this.price = price;
		this.stock = stock;
		this.categoryId = categoryId;
	}

	//getter　setter
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
}