package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.dao.ProductDAO;
import model.entity.ProductBean;

@WebServlet("/ProductListServlet")
public class ProductListServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);

		HttpSession s = request.getSession(false);
		if (s == null || s.getAttribute("username") == null) {
			response.sendRedirect("login.jsp");
			return;
		}

		try {
			ProductDAO dao = new ProductDAO();
			List<ProductBean> productList = dao.findAll();
			request.setAttribute("productList", productList);
			RequestDispatcher dispatcher = request.getRequestDispatcher("product_list.jsp");
			dispatcher.forward(request, response);

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "エラーが発生しました。");
			request.getRequestDispatcher("error.jsp").forward(request, response);
		}
	}
}