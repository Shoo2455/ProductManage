package servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.dao.ProductDAO;
import model.entity.ProductBean;

@WebServlet("/ProductListServlet")
public class ProductListServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        try {
            ProductDAO dao = new ProductDAO();
            List<ProductBean> productList = dao.findAll(); // 一覧取得メソッド
            request.setAttribute("productList", productList);
            RequestDispatcher dispatcher = request.getRequestDispatcher("product_list.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "商品一覧の取得中にエラーが発生しました。");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}