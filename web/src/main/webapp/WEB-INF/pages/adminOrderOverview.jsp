<%@ taglib prefix="tag" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tag:master pageTitle="Order overview"/>
<h3>Order number: ${order.id}</h3>
<h3>Order status: ${order.status}</h3>
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
<table>
  <tr>
    <td>First name</td>
    <td>${order.firstName}</td>
  </tr>
  <tr>
    <td>Last name</td>
    <td>${order.lastName}</td>
  </tr>
  <tr>
    <td>Address</td>
    <td>${order.deliveryAddress}</td>
  </tr>
  <tr>
    <td>Phone</td>
    <td>${order.contactPhoneNo}</td>
  </tr>
</table><br>
<button onclick="location.href='${pageContext.servletContext.contextPath}/admin/orders'">Back</button><br><br>
<form method="post" action="${pageContext.servletContext.contextPath}/admin/orders/changeStatus/${order.id}">
  <input type="hidden" name="status" value="REJECTED">
  <input type="submit" value="Rejected">
</form>
<form method="post" action="${pageContext.servletContext.contextPath}/admin/orders/changeStatus/${order.id}">
  <input type="hidden" name="status" value="DELIVERED">
  <input type="submit" value="Delivered">
</form>