<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.net.URLEncoder"%>
<%
String idStr = request.getParameter("id");
if (idStr == null) {
	response.sendRedirect("error.jsp");
	return;
}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>削除確認</title>
</head>
<body>
	<h2>本当に削除しますか？</h2>
	<form action="ProductDeleteServlet" method="post">
		<input type="hidden" name="id" value="<%=idStr%>"> <input
			type="submit" value="はい">
	</form>
	<form action="ProductListServlet" method="get">
		<input type="submit" value="いいえ">
	</form>
</body>
</html>