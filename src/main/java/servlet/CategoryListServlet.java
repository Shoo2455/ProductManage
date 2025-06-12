package servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.CategoryDAO;
import model.entity.CategoryBean;

@WebServlet("/CategoryListServlet")
public class CategoryListServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("【デバッグ】Servletが呼ばれました");

		try {
			CategoryDAO dao = new CategoryDAO();
			List<CategoryBean> list = dao.getAllCategories();

			for (CategoryBean c : list) {
				System.out.println("カテゴリID: " + c.getId() + ", カテゴリ名: " + c.getName());
			}

			System.out.println("【デバッグ】カテゴリ件数：" + (list != null ? list.size() : "null"));

			request.setAttribute("categoryList", list);
			RequestDispatcher dispatcher = request.getRequestDispatcher("category_list.jsp");
			dispatcher.forward(request, response);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
