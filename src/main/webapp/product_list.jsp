<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.entity.ProductBean" %>
<%@ page import="model.dao.ProductDAO" %>

<html>
<head>
  <title>商品一覧</title>
</head>
<body>
  <h2>商品一覧ページ</h2>
  <p><a href="ProductRegisterServlet">新規登録</a></p>
   <p><a href="home.jsp">ホームへ戻る</a></p>

  <table border="1">
    <tr>
      <th>ID</th><th>商品名</th><th>価格</th><th>在庫</th><th>カテゴリID</th>
    </tr>
    <%
      List<ProductBean> list = new ProductDAO().getAllProducts();
      for (ProductBean p : list) {
    %>
    <tr>
      <td><%= p.getId() %></td>
      <td><%= p.getName() %></td>
      <td><%= p.getPrice() %></td>
      <td><%= p.getStock() %></td>
      <td><%= p.getCategoryId() %></td>
      <td><a href="EditProductServlet?id=<%= p.getId() %>">編集</a></td>
      <td><a href="delete_confirm.jsp?id=<%= p.getId() %>">削除</a></td>
    </tr>
    <% } %>
  </table>
</body>
</html>