<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<tags:master pageTitle="Cart"/>
<script src="resources/scripts/updateMiniCart.js"></script>
<h3>Cart</h3>
<button type="button" onclick="location.href = '${pageContext.servletContext.contextPath}/productList'">Back to product list</button><br>
<br>
<form:form method="post" modelAttribute="itemsDto">
  <table border="1px" id="phonesTable">
    <thead>
    <tr>
      <td>Brand</td>
      <td>Model</td>
      <td>Color</td>
      <td>Display size</td>
      <td>Price</td>
      <td>Quantity</td>
      <td>Action</td>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="phone" items="${cart.phones.keySet()}" varStatus="status">
      <tr id="row${phone.id}">
          <td>${phone.brand}</td>
          <td>${phone.model}</td>
          <td>
          <c:forEach var="color" items="${phone.colors}">
            ${color.code}
          </c:forEach>
          </td>
          <td>${phone.displaySizeInches}</td>
          <td>${phone.price}$</td>
          <td>
              <form:input path="items[${status.index}].phoneId" type="hidden"/>
              <form:input path="items[${status.index}].quantity"/>
              <c:if test="${not empty errors[status.index]}">
                  <span class="error">${errors[status.index]}</span>
              </c:if>
          </td>
          <td><button type="button" onclick="deleteFromCart(${phone.id})">Delete</button></td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
  <br>
  <c:if test="${not empty cart.phones}">
    <input type="submit" value="Update" id="update-btn">
  </c:if>
</form:form>
<c:if test="${not empty cart.phones}">
    <button onclick="location.href='${pageContext.servletContext.contextPath}/order'">Order</button>
</c:if>
<br>
<script>
  function deleteFromCart(id) {
    $.ajax({
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      type: "DELETE",
      url: "${pageContext.servletContext.contextPath}/ajaxCart",
      data: JSON.stringify(id),
      dataType: "json",
      success: function(data) {
          $('#row' + id).remove();
          if(data.cartPhonesAmount === 0) {
              $('#update-btn').remove();
          }
          updateMiniCart(data.totalQuantity, data.totalPrice);
      }
    });
  }
</script>