<%@ taglib prefix="tag" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<tag:master pageTitle="Order page"/>
<h3>Order</h3>
<button onclick="location.href='${pageContext.servletContext.contextPath}/cart'">Back to cart</button>
<c:forEach var="error" items="${outOfStockErrors}">
    <span class="error">${error}</span><br>
</c:forEach>
<table border="1px">
    <thead>
    <tr>
        <td>Brand</td>
        <td>Model</td>
        <td>Color</td>
        <td>Display size</td>
        <td>Quantity</td>
        <td>Price</td>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="orderItem" items="${order.orderItems}">
        <tr>
            <td>${orderItem.phone.brand}</td>
            <td>${orderItem.phone.model}</td>
            <td>
                <c:forEach var="color" items="${orderItem.phone.colors}">
                    ${color.code}
                </c:forEach>
            </td>
            <td>${orderItem.phone.displaySizeInches}</td>
            <td>${orderItem.quantity}</td>
            <td>${orderItem.phone.price}$</td>
        </tr>
    </c:forEach>
    <tr>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td>Subtotal</td>
        <td>${order.subtotal}$</td>
    </tr>
    <tr>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td>Delivery price</td>
        <td>${order.deliveryPrice}$</td>
    </tr>
    <tr>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td>Total price</td>
        <td>${order.totalPrice}$</td>
    </tr>
    </tbody>
</table>
<br>
<form:form method="post" modelAttribute="order">
    <form:input path="subtotal" type="hidden"/>
    <form:input path="deliveryPrice" type="hidden"/>
    <form:input path="totalPrice" type="hidden"/>
    <label>First name*</label>
    <form:input path="firstName" type="text"/><br>
    <c:if test="${not empty errors['firstName']}">
        <span class="error">${errors['firstName']}</span><br>
    </c:if>
    <label>Last name*</label>
    <form:input path="lastName" type="text"/><br>
    <c:if test="${not empty errors['lastName']}">
        <span class="error">${errors['lastName']}</span><br>
    </c:if>
    <label>Address*</label>
    <form:input path="deliveryAddress" type="text"/><br>
    <c:if test="${not empty errors['deliveryAddress']}">
        <span class="error">${errors['deliveryAddress']}</span><br>
    </c:if>
    <label>Phone*</label>
    <form:input path="contactPhoneNo" type="text"/><br>
    <c:if test="${not empty errors['contactPhoneNo']}">
        <span class="error">${errors['contactPhoneNo']}</span><br>
    </c:if>
    <c:choose>
        <c:when test="${not empty order.orderItems}">
            <input type="submit" value="Order">
        </c:when>
        <c:otherwise>
            <span>Empty cart</span><br>
            <button>Back to shopping</button>
        </c:otherwise>
    </c:choose>
</form:form>