<%@ taglib prefix="tag" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tag:master pageTitle="Login"/>
<c:if test="${not empty param.error}">
  <span class="error">
    Invalid credentials
  </span>
</c:if>
<form method="post">
  <label>Login</label>
  <input type="text" name="username"><br>
  <label>Password</label>
  <input type="password" name="password"><br>
  <input type="submit" value="Submit">
</form>