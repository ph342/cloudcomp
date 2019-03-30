function showOrders(user) {
	// get all orders of this user from the backend and create HTML
    if (!user) {
        return;
    }

    $('#loading').css('visibility', 'visible');

    $.getJSON("/Orders/firebaseUid/" + user.uid, function(data) {
        ordersToHtml(data).then(function(html) {
            $('#center_content').html(html);
        });
    }).fail(function() {
        alertify.error('Orders could not be retrieved from server');
    }).always(function() {
        $('#loading').css('visibility', 'hidden');
    });
}

function placeOrder() {
	// convert the current basket to an order and empty the basket
	
    login().then(function(user) {
        // get current basket of user
        $.getJSON("/Baskets/" + user.uid)
            .done(function(basket) {

                // create order as JS object
                var order = {};
                order.status = 'Created';
                order.firebaseUid = user.uid;
                order.items = [];
                basket.items.forEach(function(item) {
                    order.items.push({
                        itemNr: item.itemNr
                    });
                });
                
                // empty the current basket
                $.ajax({
                    type: 'DELETE',
                    url: '/Baskets/' + user.uid,
                });

                $.ajax({
                        type: 'POST',
                        url: '/Orders/',
                        contentType: 'application/json',
                        data: JSON.stringify(order)
                    })
                    .done(function(result) {
                        alertify.notify('Order has been placed', 'success', 4);
                    	window.location.hash = '#account'; // switch to Account view
                        showOrders(user);
                    })
                    .fail(function(){
                    	alertify.error('Order could not be placed');
                    });
            });
    }).catch(function(err) {
        alertify.error(err);
    });
}