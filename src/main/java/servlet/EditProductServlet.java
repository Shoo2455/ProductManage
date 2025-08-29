package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

		HttpSession s = request.getSession(false);
		if (s == null || s.getAttribute("username") == null) {
			response.sendRedirect("login.jsp");
			return;
		}

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

		HttpSession s = request.getSession(false);
		if (s == null || s.getAttribute("username") == null) {
			response.sendRedirect(request.getContextPath() + "/login.jsp");
			return;
		}

		try {

			int id = Integer.parseInt(request.getParameter("id").trim());
			String name = request.getParameter("name");
			int price = Integer.parseInt(request.getParameter("price"));
			int stock = Integer.parseInt(request.getParameter("stock"));
			int categoryId = Integer.parseInt(request.getParameter("categoryId"));

			ProductBean p = new ProductBean();
			p.setId(id);
			p.setName(name);
			p.setPrice(price);
			p.setStock(stock);
			p.setCategoryId(categoryId);

			int rows = new ProductDAO().updateProduct(p);
			System.out.println("[UPDATE] id=" + id + " rows=" + rows + " name=" + name);

			if (rows > 0) {
				response.sendRedirect(request.getContextPath() + "/ProductListServlet");
			} else {
				request.setAttribute("errorMessage", "更新対象が見つかりません");
				request.getRequestDispatcher("error.jsp").forward(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "更新中にエラー: " + e.getMessage());
			request.getRequestDispatcher("error.jsp").forward(request, response);
		}
	}
}