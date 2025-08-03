<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="model.entity.CategoryBean" %>
<%@ page import="java.util.List" %>
<html><head><title>商品登録</title></head><body>
  <h2>商品登録フォーム</h2>

  <% String error = (String) request.getAttribute("errorMsg");
     if (error != null) { %>
    <p style="color:red;"><%= error %></p>
  <% } %>

  <form action="ProductRegisterServlet" method="post">
    商品名:<input type="text" name="name"><br>
    価格:<input type="text" name="price" pattern="[0-9]+" inputmode="numeric"><br>
    在庫数:<input type="number" name="stock" min="0"><br>
    カテゴリ:
    <select name="categoryId">
      <% 
        List<CategoryBean> cats = (List<CategoryBean>) request.getAttribute("categories");
        for (CategoryBean c : cats) { 
      %>
        <option value="<%= c.getId() %>"><%= c.getName() %></option>
      <% } %>
    </select><br>
    <input type="submit" value="登録">
  </form>

  <p><a href="product_list.jsp">一覧に戻る</a></p>
</body></html>