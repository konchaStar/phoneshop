<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="field" required="true" %>
<c:set var="searchParam" value="${not empty param.search ? '&search='.concat(param.search) : ''}"/>
<a href="?sort=${field}&order=asc${searchParam}">&#8593</a>
<a href="?sort=${field}&order=desc${searchParam}">&#8595</a>