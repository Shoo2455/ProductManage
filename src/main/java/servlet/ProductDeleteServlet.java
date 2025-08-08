package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.dao.ProductDAO;

@WebServlet("/ProductDeleteServlet")
public class ProductDeleteServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String idStr = request.getParameter("id");

        try {
            int id = Integer.parseInt(idStr);
            ProductDAO dao = new ProductDAO();
            boolean success = dao.deleteProductById(id);

            if (success) {
                response.sendRedirect("ProductListServlet");
            } else {
                request.setAttribute("errorMessage", "削除に失敗しました。");
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace(); 
            request.setAttribute("errorMessage", "エラーが発生しました: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}