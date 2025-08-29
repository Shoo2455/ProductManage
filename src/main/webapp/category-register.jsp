<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<form action="/ProductManage/CategoryRegisterServlet" method="post" accept-charset="UTF-8">
  カテゴリID: <input type="number" name="id" required><br>
  カテゴリ名: <input type="text" name="name" required><br>
  <input type="submit" value="登録">
</form>
</body>
</html>