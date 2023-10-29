function addToCart(phoneId, url) {
    let quantity = $('#quantity' + phoneId).val();
    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: "POST",
        url: url,
        data: JSON.stringify({phoneId: phoneId, quantity: quantity}),
        dataType: "json",
        success: function(data) {
            const message = document.querySelector("#message" + phoneId);
            const successMessage = document.querySelector("#successMessage");
            if (data.errorStatus === true) {
                successMessage.innerText = "";
                message.innerText = data.message;
            } else {
                message.innerText = "";
                successMessage.innerText = data.message;
            }
            updateMiniCart(data.totalQuantity, data.totalPrice);
        }
    });
}