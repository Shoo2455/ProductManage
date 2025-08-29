<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="model.entity.ProductBean"%>
<%@ page import="model.entity.CategoryBean"%>

<html>
<head>
<meta charset="UTF-8">
<title>商品編集</title>
</head>
<body>

	<h2>商品編集フォーム</h2>

	<%
String error = (String) request.getAttribute("errorMsg");
if (error != null) {
%>
	<p style="color: red;">
		※ エラー：<%= error %></p>
	<%
}
ProductBean product = (ProductBean) request.getAttribute("product");
List<CategoryBean> cats = (List<CategoryBean>) request.getAttribute("categories");
if (product == null) {
%>
	<p style="color: red;">編集対象の商品が見つかりません。</p>
	<p>
		<a href="product_list.jsp">一覧に戻る</a>
	</p>
</body>
</html>
<%
  return;
}
%>

<form action="EditProductServlet" method="post" accept-charset="UTF-8">
	<input type="hidden" name="id" value="<%= product.getId() %>">

	商品名： <input type="text" name="name" value="<%= product.getName() %>"><br>

	価格： <input type="text" name="price" value="<%= product.getPrice() %>"
		pattern="[0-9]+" inputmode="numeric"><br> 在庫： <input
		type="number" name="stock" value="<%= product.getStock() %>" min="0"><br>

	カテゴリ： <select name="categoryId">
		<%
    if (cats != null) {
      for (CategoryBean c : cats) {
        boolean selected = (c.getId() == product.getCategoryId());
  %>
		<option value="<%= c.getId() %>" <%= selected ? "selected" : "" %>>
			<%= c.getName() %>
		</option>
		<%
      }
    }
  %>
	</select><br> <input type="submit" value="保存">
</form>

<p>
	<a href="product_list.jsp">一覧に戻る</a>
</p>

</body>
</html>