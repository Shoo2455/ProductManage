package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.CategoryDAO1;
import model.entity.CategoryBean1;

@WebServlet("/CategoryRegisterServlet")
public class CategoryRegisterServlet2 extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

		try {
			int id = Integer.parseInt(request.getParameter("id"));
			String name = request.getParameter("name");

			CategoryBean1 category = new CategoryBean1(id, name);
			CategoryDAO1 dao = new CategoryDAO1();
			dao.insertCategory(category);

			response.sendRedirect("CategoryListServlet");

		} catch (Exception e) {
			request.setAttribute("errorMessage", "登録に失敗しました。：" + e.getMessage());
			request.getRequestDispatcher("error.jsp").forward(request, response);
		}
	}
}
