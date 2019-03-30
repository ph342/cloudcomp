function itemsToHtml(json) {
	// create HTML for the main item list
	
    var data = '<div class="center_title_bar">Latest Products</div>';
    for (var i = 0; i < json.length; i++) {

        data += '<div class="prod_box"><div class="center_prod_box"><div class="product_title"><a href="javascript:void(0);">' +
            json[i].name +
            '</a></div><div class="product_img"><a href="javascript:void(0);"><img src="' +
            json[i].imageUri +
            '" alt="Image could not be retrieved from Datastore" border="0" width="180px" height"auto" object-fit="cover"/></a></div><div class="prod_price"><span class="price">' + 
            json[i].price + ' ' + json[i].curr +
            '</span></div></div>';

        data += '<div class="prod_details_tab"> <a href="javascript:void(0);" data-itemnr="' +
            json[i].itemNr +
            '" class="prod_buy">Add to Basket</a><a href="javascript:void(0);" data-itemnr="' +
            json[i].itemNr +
            '" class="prod_details">Details</a></div></div>';
    }
 
    return data;
}

function basketToHtml(json) {
	// create HTML for the shopping basket
	
    return new Promise(
        function(resolve, reject) {
            // get complementary item data first
            $.getJSON("/Items/").done(function(allitems) {

	            if (!json || json.items.length == 0) {
	                resolve('<div class="center_title_bar">Your shopping basket is empty</div><br><br><br><br>');
	                return;
	            } else {
	                var data = '<div class="center_title_bar">Your shopping basket</div>';
	            }
	
	            for (var i = 0; i < json.items.length; i++) {
	                // find Item data
	                for (var j = 0; j < allitems.length; j++) {
	                    if (allitems[j].itemNr == json.items[i].itemNr) {
	                        data += '<div class="basketItem"><div class="basketButtons"><span class="basketDelete-btn" data-itemnr="' +
	                            allitems[j].itemNr +
	                            '"></span></div><div class="product_img"><img src="' +
	                            allitems[j].imageUri +
	                            '" alt="Image could not be retrieved from Datastore"/></div><div class="basketDescription"><span>' +
	                            allitems[j].name +
	                            '</span><span>' +
	                            allitems[j].descr +
	                            '</span></div><div class="basketPrice">' +
	                            allitems[j].price +
	                            ' ' +
	                            allitems[j].curr +
	                            '</div></div>';
	                        break;
	                    }
	                }
	            }
	
	            data += '<a id="orderButton">Place order</a>';
	            resolve(data);
	        });
        });
}

function ordersToHtml(json) {
	// create HTML for the orders list
	
    return new Promise(function(resolve, reject) {

        if (!json || json.length == 0){
        	resolve('<div class="center_title_bar">You have not yet placed orders</div><br><br><br><br>');
        	return;
        }

        // get complementary item data first
        $.getJSON("/Items/").done(function(allitems) {
            var data = '<div class="center_title_bar">Your orders</div>';

            json.forEach(function(order) {
            	data += '<div class="basketItem"><div class="basketDescription"><span></span>';
                order.items.forEach(function(item) {
                    // find Item data
                    for (var j = 0; j < allitems.length; j++) 
                        if (allitems[j].itemNr == item.itemNr) 
                        	data += '<span>' + allitems[j].name + ': ' + allitems[j].price + ' ' + allitems[j].curr + '</span>';                                
                });

                data += '<span>Status: ' + order.status + '</span></div><div class="orderNumber">#' + order.orderNr + '<br>' +
                    `${order.timestamp.date.day}/${order.timestamp.date.month}/${order.timestamp.date.year} ${order.timestamp.time.hour}:${order.timestamp.time.minute}` +
                    '</div></div>';
            });
            resolve(data);
        });
    });
}