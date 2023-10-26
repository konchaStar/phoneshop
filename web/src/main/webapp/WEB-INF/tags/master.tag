<%@ tag trimDirectiveWhitespaces="true" %>
<%@attribute name="pageTitle" required="true" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
    <title>${pageTitle}</title>
    <link rel="stylesheet" href="resources/styles/style.css">
    <script src="https://code.jquery.com/jquery-3.6.0.js" integrity="sha256-H+K7U5CnXl1h5ywQfKtSj8PCmoN9aaq30gDh27Xc0jk=" crossorigin="anonymous"></script>
</head>
<body>
<header>
    <a href="${pageContext.servletContext.contextPath}?page=1">
        <h1>Phonify</h1>
    </a>
    <jsp:include page="/cart/minicart"/>
    <div style="float: right">
        <security:authorize access="hasRole('ADMIN')">
            <a href="${pageContext.servletContext.contextPath}/admin/orders">Admin</a>
        </security:authorize>
        <security:authorize access="isAuthenticated()">
            <a href="${pageContext.servletContext.contextPath}/logout">Logout</a>
        </security:authorize>
        <security:authorize access="!isAuthenticated()">
            <a href="${pageContext.servletContext.contextPath}/login">Login</a>
        </security:authorize>
    </div>
</header>
<main>
    <jsp:doBody/>
</main>
</body>
</html>