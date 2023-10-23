<%@ taglib prefix="tag" tagdir="/WEB-INF/tags" %>
<tag:master pageTitle="Error"/>
<h1>Sorry, an error has occurred</h1>
<h2>Phone not found</h2>
<button onclick="location.href = '${pageContext.servletContext.contextPath}/productList'">Back to product list</button>