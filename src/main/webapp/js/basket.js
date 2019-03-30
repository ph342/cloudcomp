function showBasket(user) {
	// get basket from backend and create HTML
    if (!user) {
        return;
    }

    $('#loading').css('visibility', 'visible');

    $.getJSON("/Baskets/" + user.uid, function(data) {
        basketToHtml(data).then(function(html) {
            $('#center_content').html(html);
        });
    }).fail(function() {
        alertify.error('Basket could not be loaded');
    }).always(function() {
        $('#loading').css('visibility', 'hidden');
    });
}

function addToBasket() {
	// add an item to the shopping basket via asynchronous POST
    var itemNr = $(this).data('itemnr');

    login().then(function(user) {
        // get current basket of user
        $.getJSON("/Baskets/" + user.uid)
            .done(function(basketOld) {
                // see if basket already has this item, and if not, add it
            	
                var basketNew;
                var updateNecessary = true;

                if (basketOld) {
                    // basket contains items
                    basketOld.items.forEach(function(item) {
                        if (item.itemNr == itemNr) {
                            alertify.notify('Item is already in basket', 'success', 2);
                           
                            // no update of basket necessary
                            updateNecessary = false; 
                        }
                    });

                    if (updateNecessary) {
                        basketNew = basketOld;
                        basketNew.items.push({
                            itemNr: itemNr
                        });
                    }

                } else {
                    // basket is empty right now
                    basketNew = {};
                    basketNew.firebaseUid = user.uid;
                    basketNew.items = [];
                    basketNew.items.push({
                        itemNr: itemNr
                    });
                }

                if (updateNecessary)
                    $.ajax({
                        type: 'PUT',
                        url: '/Baskets/',
                        contentType: 'application/json',
                        data: JSON.stringify(basketNew)
                    })
                    .done(function() {
                        alertify.notify('Item has been added to your basket', 'success', 2);
                    })
                    .fail(function(){
                    	alertify.error('Item could not be added to your basket');
                    });
            });
    }).catch(function(err) {
        alertify.error(err);
    });
}

function removeFromBasket() {
	// remove an item from the basket
    var itemNr = $(this).data('itemnr');

    login().then(function(user) {
        // get current basket of user
        $.getJSON("/Baskets/" + user.uid)
            .done(function(basketOld) {
                // see if basket already has this item, and if not, add it
                if (!basketOld) return;

                var basketNew = basketOld;
                basketNew.items = basketOld.items.filter(function(item) {
                    return item.itemNr != itemNr;
                });

                $.ajax({
                        type: 'PUT',
                        url: '/Baskets/',
                        contentType: 'application/json',
                        data: JSON.stringify(basketNew)
                    })
                    .done(function(result) {
                        basketToHtml(result).then(function(html) {
                            $('#center_content').html(html);
                        });
                    })
                    .fail(function(){
                    	alertify.error('Failed to remove item from basket');
                    });
            });
    }).catch(function(err) {
        alertify.error(err);
    });
}