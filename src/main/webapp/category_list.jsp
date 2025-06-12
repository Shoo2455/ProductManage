<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="model.entity.CategoryBean"%>
<html>
<head>
<title>カテゴリリスト</title>
</head>
<body>
	<h2>カテゴリリスト</h2>
	<table border="1">
		<tr>
			<th>カテゴリID</th>
			<th>カテゴリ名</th>
		</tr>
		<%
		List<CategoryBean> list = (List<CategoryBean>) request.getAttribute("categoryList");

		if (list == null) {
		%>

		<%
		} else {
		for (CategoryBean category : list) {
		%>
		<tr>
			<td><%=category.getId()%></td>
			<td><%=category.getName()%></td>
		</tr>
		<%
		}
		}
		%>
	</table>
</body>
</html>