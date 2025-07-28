<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="javax.servlet.http.*, javax.servlet.*" %>
<%
    HttpSession mysession = request.getSession(false);
    String username = null;
    if (session != null) {
        username = (String) session.getAttribute("username");
    }
    if (username == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>ホーム</title>
</head>
<body>
    <h2>ホーム画面</h2>
    <p>ようこそ、<%= username %> さん！</p>
    <form action="LogoutServlet" method="get">
        <input type="submit" value="ログアウト">
    </form>
</body>
</html>