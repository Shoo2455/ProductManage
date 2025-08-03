package servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.CategoryDAO1;
import model.entity.CategoryBean1;

@WebServlet("/CategoryListServlet")
public class CategoryListServlet1 extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("【デバッグ】Servletが呼ばれました");

		try {
			CategoryDAO1 dao = new CategoryDAO1();
			List<CategoryBean1> list = dao.getAllCategories();

			for (CategoryBean1 c : list) {
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
