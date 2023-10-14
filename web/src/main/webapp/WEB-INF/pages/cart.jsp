<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<tags:master pageTitle="Cart"></tags:master>
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
      <tr>
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
              <c:if test="${not empty error && error.phoneId eq phone.id}">
                  <span class="error">${error.message}</span>
              </c:if>
              <c:if test="${not empty validationErrors[status.index]}">
                  <span class="error">${validationErrors[status.index]}</span>
              </c:if>
          </td>
          <td><button type="button" onclick="deleteFromCart(${phone.id})">Delete</button></td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
  <br>
  <input type="submit" value="Update">
</form:form>
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
        let table = document.querySelector("#phonesTable");
        let rows = "<thead> <tr> <td>Brand</td> <td>Model</td> <td>Color</td> <td>Display size</td> <td>Price</td> <td>Quantity</td> <td>Action</td> </tr> </thead>";
        rows += "<tbody>";
        if(Object.keys(data).length > 0) {
          for (let i = 0; i < data["brands"].length; i++) {
            rows += "<tr>";
            Object.keys(data).forEach((key) => {
              if (key != "totalQuantity" && key != "quantities" && key != "ids") {
                  if (key == "colors") {
                      rows += "<td>";
                      for (let i = 0; i < data[key][0].length; i++) {
                          rows += data[key][0][i].code + " ";
                      }
                      rows += "</td>";
                  } else {
                      rows += "<td>" + data[key][i] + (key == "prices" ? "$" : "") + "</td>";
                  }
              }
            });
            rows += "<td><input type='text' value='" + data["quantities"][i] + "'></td>";
            rows += "<td><button type='button' onclick='deleteFromCart(" + data["ids"][i] + ")'>Delete</button></td>";
            rows += "</tr>";
          }
        }

        rows += "</tbody>";
        table.innerHTML = rows;
        $("#minicart").text("Cart: " + data.totalQuantity);
      }
    });
  }
</script>