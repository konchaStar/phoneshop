<%@ taglib prefix="tag" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<tag:master pageTitle="Quick cart"></tag:master>
<h3>Quick cart</h3>
<span class="success">
    <c:forEach var="successMessage" items="${successMessages}">
        ${successMessage}<br>
    </c:forEach>
</span>
<form:form method="post" modelAttribute="quickCartItemsDto">
    <table border="1">
    <tr>
    <td>Phone model</td>
    <td>Quantity</td>
    </tr>
    <c:forEach begin="0" end="7" var="i">
    <tr>
        <td>
            <form:input path="quickCartItems[${i}].model"/><br>
            <span class="error">${phoneErrors[i]}</span>
        </td>
        <td>
            <form:input path="quickCartItems[${i}].quantity"/><br>
            <span class="error">${quantityErrors[i]}</span>
        </td>
    </tr>
    </c:forEach>
    </table>
    <br>
    <form:button>Add to cart</form:button>
</form:form>

