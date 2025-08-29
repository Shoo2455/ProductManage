<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="model.entity.CategoryBean1"%>
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
		List<CategoryBean1> list = (List<CategoryBean1>) request.getAttribute("categoryList");

				if (list == null) {
		%>

		<%
		} else {
				for (CategoryBean1 category : list) {
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