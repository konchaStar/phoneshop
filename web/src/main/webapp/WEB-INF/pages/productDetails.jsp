<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<tags:master pageTitle="Product details"/>
<script src="${pageContext.servletContext.contextPath}/resources/scripts/addToCart.js"></script>
<script src="${pageContext.servletContext.contextPath}/resources/scripts/updateMiniCart.js"></script>
<br>
<button onclick="location.href = '${pageContext.servletContext.contextPath}/productList'">Back to product list</button><br>
<img src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${phone.imageUrl}">
<h3>${phone.model}</h3>
<p>${phone.description}</p>
<h3>Display</h3>
<table border="1px">
    <tr>
        <td>Size</td>
        <td>${phone.displaySizeInches}</td>
    </tr>
    <tr>
        <td>Resolution</td>
        <td>${phone.displayResolution}</td>
    </tr>
    <tr>
        <td>Technology</td>
        <td>${phone.displayTechnology}</td>
    </tr>
    <tr>
        <td>Pixel density</td>
        <td>${phone.pixelDensity}</td>
    </tr>
</table>
<h3>Dimension & weight</h3>
<table border="1px">
    <tr>
        <td>Length</td>
        <td>${phone.lengthMm}mm</td>
    </tr>
    <tr>
        <td>Width</td>
        <td>${phone.widthMm}mm</td>
    </tr>
    <tr>
        <td>Weight</td>
        <td>${phone.weightGr}</td>
    </tr>
</table>
<h3>Camera</h3>
<table border="1px">
    <tr>
        <td>Front</td>
        <td>${phone.frontCameraMegapixels}</td>
    </tr>
    <tr>
        <td>Back</td>
        <td>${phone.backCameraMegapixels}</td>
    </tr>
</table>
<h3>Battery</h3>
<table border="1px">
    <tr>
        <td>Talk time</td>
        <td>${phone.talkTimeHours}</td>
    </tr>
    <tr>
        <td>Stand by time</td>
        <td>${phone.standByTimeHours}</td>
    </tr>
    <tr>
        <td>Battery capacity</td>
        <td>${phone.batteryCapacityMah}</td>
    </tr>
</table>
<h3>Other</h3>
<table border="1px">
    <tr>
        <td>Colors</td>
        <td>
            <c:forEach var="color" items="${phone.colors}">
                ${color.code}
            </c:forEach>
        </td>
    </tr>
    <tr>
        <td>Device type</td>
        <td>${phone.deviceType}</td>
    </tr>
    <tr>
        <td>Bluetooth</td>
        <td>${phone.bluetooth}</td>
    </tr>
</table>
<table border="1px">
    <h3>Price ${phone.price}$</h3>
    <form id="addToCart">
        <input id="quantity${phone.id}" type="text" value="1">
        <button type="button" onclick="addToCart(${phone.id}, '${pageContext.servletContext.contextPath}/ajaxCart')">Add</button>
    </form><br>
</table>
<span id="message${phone.id}" class="error"></span>
<span id="successMessage" class="success"></span>