<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head><title>エラー</title></head>
<body>
    <h2>Error</h2>
    <p><%= request.getAttribute("errorMessage") %></p>
</body>
</html>