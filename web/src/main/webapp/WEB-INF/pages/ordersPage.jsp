<%@ taglib prefix="tag" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<tag:master pageTitle="Orders"/>
<h3>Orders</h3>
<table border="1px">
  <thead>
  <tr>
    <td>Order number</td>
    <td>Customer</td>
    <td>Phone</td>
    <td>Address</td>
    <td>Total price</td>
    <td>Status</td>
  </tr>
  </thead>
  <tbody>
  <c:forEach items="${orders}" var="order">
    <tr>
      <td><a href="${pageContext.servletContext.contextPath}/admin/orders/${order.id}">${order.id}</a></td>
      <td>${order.firstName} ${order.lastName}</td>
      <td>${order.contactPhoneNo}</td>
      <td>${order.deliveryAddress}</td>
      <td>${order.totalPrice}$</td>
      <td>${order.status}</td>
    </tr>
  </c:forEach>
  </tbody>
</table>