<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<tags:master pageTitle="ProductList"/>
<script src="resources/scripts/addToCart.js"></script>
<script src="resources/scripts/updateMiniCart.js"></script>
<h4>Phones</h4>
<span id="successMessage" class="success"></span>
<form>
    <input type="text" name="search" value="${param.search}">
    <input type="submit" value="Search">
</form>
<table border="1px">
    <thead>
    <tr>
        <td>Image</td>
        <td>Brand<tags:sortList field="BRAND"/></td>
        <td>Model<tags:sortList field="MODEL"/></td>
        <td>Color</td>
        <td>Display size<tags:sortList field="DISPLAYSIZEINCHES"/></td>
        <td>Price<tags:sortList field="PRICE"/></td>
        <td>Quantity</td>
        <td>Action</td>
    </tr>
    </thead>
    <c:forEach var="phone" items="${phones}">
        <tr>
            <form>
                <td>
                    <img src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${phone.imageUrl}">
                </td>
                <td>${phone.brand}</td>
                <td><a href="${pageContext.servletContext.contextPath}/productDetails/${phone.id}">${phone.model}</a></td>
                <td>
                    <c:forEach var="color" items="${phone.colors}">
                        ${color.code}
                    </c:forEach>
                </td>
                <td>${phone.displaySizeInches}</td>
                <td>$ ${phone.price}</td>
                <td>
                    <input id="quantity${phone.id}" value="1"/><br>
                    <span id="message${phone.id}" class="error"></span>
                </td>
                <td>
                    <button type="button" onclick="addToCart(${phone.id}, '${pageContext.servletContext.contextPath}/ajaxCart')">Add to cart</button>
                </td>
            </form>
        </tr>
    </c:forEach>
</table>
<div>
    <c:set var="sortParam" value="${not empty param.sort ? '&sort='.concat(param.sort) : ''}"/>
    <c:set var="orderParam" value="${not empty param.order ? '&order='.concat(param.order) : ''}"/>
    <c:set var="searchParam" value="${not empty param.search ? '&search='.concat(param.search) : ''}"/>
    <a href="?page=1${sortParam}${orderParam}${searchParam}"><<</a>
    <% Long pages = (Long) request.getAttribute("pages");
    long pg = request.getParameter("page") == null ? 1L : Long.parseLong(request.getParameter("page"));
    long pagesAmount = Math.min(pages, 9L);
    for (long i = 1; i <= pagesAmount; i++) {
        if(pg < (pagesAmount + 1) / 2) {%>
    <a href="?page=<%=i%>${sortParam}${orderParam}${searchParam}"><%=i%></a>
        <%} else if (pg > pages - (pagesAmount + 1) / 2) {%>
    <a href="?page=<%=pages - pagesAmount + i%>${sortParam}${orderParam}${searchParam}"><%=pages - pagesAmount + i%></a>
        <%} else {%>
    <a href="?page=<%=pg - (pagesAmount + 1) / 2 + i%>${sortParam}${orderParam}${searchParam}"><%=pg - (pagesAmount + 1) / 2 + i%></a>
        <%}%>
    <%}%>
    <a href="?page=${pages}${sortParam}${orderParam}${searchParam}">>></a>
</div>