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
import model.entity.CategoryBean;
import model.entity.ProductBean;

@WebServlet("/ProductRegisterServlet")
public class ProductRegisterServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession s = request.getSession(false);
		if (s == null || s.getAttribute("username") == null) {
		    response.sendRedirect("login.jsp"); 
		    return;
		}
		
		try {
			List<CategoryBean> categories = new CategoryDAO().getAllCategories();
			request.setAttribute("categories", categories);
			request.getRequestDispatcher("product_register.jsp").forward(request, response);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession s = request.getSession(false);
		if (s == null || s.getAttribute("username") == null) {
		    response.sendRedirect("login.jsp"); 
		    return;
		}
		
		request.setCharacterEncoding("UTF-8");

		String name = request.getParameter("name");
		String priceStr = request.getParameter("price");
		String stockStr = request.getParameter("stock");
		String categoryIdStr = request.getParameter("categoryId");

		String error = null;
		int price = 0, stock = 0, categoryId = 0;

		try {
			price = Integer.parseInt(priceStr);
			stock = Integer.parseInt(stockStr);
			categoryId = Integer.parseInt(categoryIdStr);
			if (name == null || name.isEmpty())
				error = "商品名を入力してください。";
			else if (price < 0)
				error = "価格は0以上を入力してください。";
			else if (stock < 0)
				error = "在庫数は0以上を入力してください。";
		} catch (NumberFormatException e) {
			error = "価格・在庫は整数で入力してください。";
		}

		if (error != null) {

			try {
				request.setAttribute("errorMsg", error);
				request.setAttribute("categories", new CategoryDAO().getAllCategories());
				request.getRequestDispatcher("product_register.jsp").forward(request, response);
			} catch (Exception ex) {
				throw new ServletException(ex);
			}
			return;
		}

		try {
			ProductBean p = new ProductBean(name, price, stock, categoryId);
			new ProductDAO().addProduct(p);
			response.sendRedirect("product_list.jsp");
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
}