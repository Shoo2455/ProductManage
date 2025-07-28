<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>ログイン</title>
</head>
<body>
    <h2>ログイン画面</h2>
    <c:if test="${not empty errorMsg}">
        <p style="color:red;">${errorMsg}</p>
    </c:if>
    <form action="LoginServlet" method="post">
        <label>ユーザID:</label>
        <input type="text" name="username" required><br><br>
        <label>パスワード:</label>
        <input type="password" name="password" required><br><br>
        <input type="submit" value="ログイン">
    </form>
</body>
</html>