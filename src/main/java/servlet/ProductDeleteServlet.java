package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.dao.ProductDAO;

@WebServlet("/ProductDeleteServlet")
public class ProductDeleteServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession s = request.getSession(false);
		if (s == null || s.getAttribute("username") == null) {
			response.sendRedirect(request.getContextPath() + "/login.jsp");
			return;
		}

		String idStr = request.getParameter("id");
		if (idStr == null || idStr.isBlank()) {
			request.setAttribute("errorMessage", "IDが不正です");
			request.getRequestDispatcher("error.jsp").forward(request, response);
			return;
		}

		try {
			int id = Integer.parseInt(idStr.trim());

			boolean ok = new ProductDAO().deleteProductById(id);
			System.out.println("[DELETE] id=" + id + " result=" + ok);

			if (ok) {

				response.sendRedirect(request.getContextPath() + "/ProductListServlet");
			} else {
				request.setAttribute("errorMessage", "削除対象が見つかりません。");
				request.getRequestDispatcher("error.jsp").forward(request, response);
			}

		} catch (NumberFormatException e) {
			request.setAttribute("errorMessage", "IDの形式が不正です");
			request.getRequestDispatcher("error.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "削除中にエラー: " + e.getMessage());
			request.getRequestDispatcher("error.jsp").forward(request, response);
		}
	}
}