package test.servlet;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.dao.DBManager;
import model.dao.ProductDAO;
import model.entity.ProductBean;
import model.service.ProductService;

class ProductServiceTest {

	private ProductDAO productDAO;
	private ProductService service;

	private static final String P1 = "UT_test1";
	private static final String P2 = "UT_test2";
	private static final String P3 = "UT_test3";

	@BeforeEach
	void setUp() throws Exception {
		productDAO = new ProductDAO();
		service = new ProductService(productDAO);

		try (Connection con = DBManager.getConnection();
				PreparedStatement del = con.prepareStatement(
						"DELETE FROM products WHERE name IN (?,?,?)")) {
			del.setString(1, P1);
			del.setString(2, P2);
			del.setString(3, P3);
			del.executeUpdate();
		}

		try (Connection con = DBManager.getConnection()) {
			try (PreparedStatement chk = con.prepareStatement(
					"SELECT id FROM categories WHERE id=1")) {
				try (ResultSet rs = chk.executeQuery()) {
					if (!rs.next()) {
						try (PreparedStatement ins = con.prepareStatement(
								"INSERT INTO categories(id, name) VALUES (1,'UT_Category')")) {
							ins.executeUpdate();
						}
					}
				}
			}
		}

		ProductBean p1 = new ProductBean();
		p1.setName(P1);
		p1.setPrice(120);
		p1.setStock(5);
		p1.setCategoryId(1);
		productDAO.addProduct(p1);

		ProductBean p2 = new ProductBean();
		p2.setName(P2);
		p2.setPrice(80);
		p2.setStock(10);
		p2.setCategoryId(1);
		productDAO.addProduct(p2);
	}

	@AfterEach
	void tearDown() throws Exception {
		try (Connection con = DBManager.getConnection();
				PreparedStatement del = con.prepareStatement(
						"DELETE FROM products WHERE name LIKE 'UT_%'")) {
			del.executeUpdate();
		}
	}

	@Test
	void 一覧を取得できる() {
		List<ProductBean> list = service.listAll();
		assertTrue(list.stream().anyMatch(p -> P1.equals(p.getName())));
		assertTrue(list.stream().anyMatch(p -> P2.equals(p.getName())));
	}

	@Test
	void カテゴリ別にフィルタできる_代替() throws Exception {
		ProductBean p3 = new ProductBean();
		p3.setName("UT_cat2");
		p3.setPrice(50);
		p3.setStock(3);
		p3.setCategoryId(2);
		productDAO.addProduct(p3);

		List<ProductBean> all = service.listAll();

		List<ProductBean> cat1 = all.stream()
				.filter(p -> p.getCategoryId() == 1)
				.toList();
		assertTrue(cat1.stream().anyMatch(p -> P1.equals(p.getName())));
		assertTrue(cat1.stream().anyMatch(p -> P2.equals(p.getName())));

		List<ProductBean> cat2 = all.stream()
				.filter(p -> p.getCategoryId() == 2)
				.toList();
		assertTrue(cat2.stream().anyMatch(p -> "UT_cat2".equals(p.getName())));
	}

	@Test
	void 追加できる() {
		ProductBean created = service.add(P3, 100, 7);
		assertNotNull(created);
		assertTrue(service.listAll().stream().anyMatch(p -> P3.equals(p.getName())));
	}

	@Test
	void 追加_名前空は例外() {
		assertThrows(IllegalArgumentException.class, () -> service.add("", 100, 1));
	}

	@Test
	void 追加_価格マイナスは例外() {
		assertThrows(IllegalArgumentException.class, () -> service.add("X", -1, 1));
	}

	@Test
	void 追加_在庫マイナスは例外() {
		assertThrows(IllegalArgumentException.class, () -> service.add("X", 100, -1));
	}

	@Test
	void 編集できる() throws Exception {
		int id = service.listAll().stream()
				.filter(p -> P1.equals(p.getName()))
				.findFirst().get().getId();

		service.edit(id, P1 + "_ver1", 150, 3);

		ProductBean after = productDAO.getProductById(id);
		assertEquals(P1 + "_ver1", after.getName());
		assertEquals(150, after.getPrice());
		assertEquals(3, after.getStock());
	}

	@Test
	void 存在しないIDの編集は例外() {
		assertThrows(IllegalArgumentException.class,
				() -> service.edit(999999, "X", 1, 1));
	}

	@Test
	void 削除できる_存在しないIDはfalse() {
		int id = service.listAll().stream()
				.filter(p -> P2.equals(p.getName()))
				.findFirst().get().getId();

		assertTrue(service.remove(id));
		assertFalse(service.remove(999999));
	}

	@Test
	void 削除後_一覧から消える() {
		int id = service.listAll().stream()
				.filter(p -> P1.equals(p.getName()))
				.findFirst().get().getId();

		service.remove(id);

		assertTrue(service.listAll().stream().noneMatch(p -> P1.equals(p.getName())));
	}
}