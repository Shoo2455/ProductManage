package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.UserDAO;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		String username = request.getParameter("username");
		String password = request.getParameter("password");

		try {
			UserDAO dao = new UserDAO();
			boolean result = dao.authenticate(username, password);

			if (result) {
				HttpSession session = request.getSession();
				session.setAttribute("username", username);
				response.sendRedirect("home.jsp");
			} else {
				// 認証失敗 → login.jsp に戻し、エラーメッセージ表示
				request.setAttribute("errorMsg", "IDまたはパスワードが違います");
				request.getRequestDispatcher("login.jsp").forward(request, response);
			}

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMsg", "エラーが発生しました");
			request.getRequestDispatcher("login.jsp").forward(request, response);
		}
	}
}