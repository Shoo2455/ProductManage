package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.dao.CategoryDAO;
import model.dao.ProductDAO;
import model.entity.CategoryBean; // ←同上
import model.entity.ProductBean; // ←パッケージはあなたの環境に合わせて

@WebServlet("/EditProductServlet")
public class EditProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			String sid = request.getParameter("id");
			if (sid == null || sid.isBlank()) {
				request.setAttribute("errorMsg", "IDが指定されていません。");
				request.getRequestDispatcher("product_list.jsp").forward(request, response);
				return;
			}

			int id = Integer.parseInt(sid);

			ProductDAO pdao = new ProductDAO();
			ProductBean product = pdao.getProductById(id);
			if (product == null) {
				request.setAttribute("errorMsg", "指定IDの商品が見つかりません。");
				request.getRequestDispatcher("product_list.jsp").forward(request, response);
				return;
			}

			CategoryDAO cdao = new CategoryDAO();
			List<CategoryBean> categories = cdao.getAllCategories();

			request.setAttribute("product", product);
			request.setAttribute("categories", categories);
			request.getRequestDispatcher("edit_product.jsp").forward(request, response);

		} catch (NumberFormatException e) {
			request.setAttribute("errorMsg", "IDが不正です。");
			request.getRequestDispatcher("product_list.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMsg", "編集画面の表示に失敗しました。");
			request.getRequestDispatcher("product_list.jsp").forward(request, response);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

		try {
			int id = Integer.parseInt(request.getParameter("id"));
			String name = request.getParameter("name");
			String priceStr = request.getParameter("price");
			String stockStr = request.getParameter("stock");
			String categoryIdStr = request.getParameter("categoryId");

			if (name == null || name.isBlank()
					|| priceStr == null || stockStr == null || categoryIdStr == null) {
				throw new IllegalArgumentException("必須項目が未入力です。");
			}

			int price = Integer.parseInt(priceStr);
			int stock = Integer.parseInt(stockStr);
			int categoryId = Integer.parseInt(categoryIdStr);

			if (price < 0 || stock < 0) {
				throw new IllegalArgumentException("価格・在庫は0以上で入力してください。");
			}

			ProductBean product = new ProductBean();
			product.setId(id);
			product.setName(name);
			product.setPrice(price);
			product.setStock(stock);
			product.setCategoryId(categoryId);

			ProductDAO dao = new ProductDAO();
			int updated = dao.updateProduct(product);
			if (updated == 0) {
				throw new IllegalStateException("更新対象が存在しません。");
			}

			response.sendRedirect("ProductListServlet");

		} catch (NumberFormatException e) {
			backToEditWithError(request, response, "数値項目は数字で入力してください。");
		} catch (IllegalArgumentException e) {
			backToEditWithError(request, response, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			backToEditWithError(request, response, "商品の更新に失敗しました。");
		}
	}

	private void backToEditWithError(HttpServletRequest request, HttpServletResponse response, String msg)
			throws ServletException, IOException {
		try {
			request.setAttribute("errorMsg", msg);

			ProductBean p = new ProductBean();
			if (request.getParameter("id") != null && !request.getParameter("id").isBlank()) {
				p.setId(Integer.parseInt(request.getParameter("id")));
			}
			p.setName(request.getParameter("name"));
			try {
				p.setPrice(Integer.parseInt(request.getParameter("price")));
			} catch (Exception ignore) {
			}
			try {
				p.setStock(Integer.parseInt(request.getParameter("stock")));
			} catch (Exception ignore) {
			}
			try {
				p.setCategoryId(Integer.parseInt(request.getParameter("categoryId")));
			} catch (Exception ignore) {
			}

			request.setAttribute("product", p);

			CategoryDAO cdao = new CategoryDAO();
			List<CategoryBean> categories = cdao.getAllCategories();
			request.setAttribute("categories", categories);

			request.getRequestDispatcher("edit_product.jsp").forward(request, response);
		} catch (Exception ex) {
			ex.printStackTrace();
			request.setAttribute("errorMsg", "再表示時にエラーが発生しました。");
			request.getRequestDispatcher("product_list.jsp").forward(request, response);
		}
	}
}