<%@ tag trimDirectiveWhitespaces="true" %>
<%@attribute name="pageTitle" required="true" %>
<html>
<head>
    <title>${pageTitle}</title>
    <script src="https://code.jquery.com/jquery-3.6.0.js" integrity="sha256-H+K7U5CnXl1h5ywQfKtSj8PCmoN9aaq30gDh27Xc0jk=" crossorigin="anonymous"></script>
</head>
<body>
<header>
    <a href="${pageContext.servletContext.contextPath}?page=1">
        <h1>Phonify</h1>
    </a>
    <jsp:include page="/cart/minicart"/>
</header>
<main>
    <jsp:doBody/>
</main>
</body>
</html>