function itemsToHtml(json) {
    var data = '<div class="center_title_bar">Latest Products</div>';
    for (var i = 0; i < json.length; i++) {

        data += '<div class="prod_box">    <div class="center_prod_box">      ' +
            '<div class="product_title"><a href="#">' +
            json[i].name +
            '</a></div><div class="product_img">' +
            '<a href="#"><img src="' +
            json[i].imageUri +
            '" alt="Image could not be retrieved from Datastore" border="0" width="180px" height"auto" object-fit="cover"/></a></div><div class="prod_price">' +
            '<span class="price">' + json[i].price + ' ' + json[i].curr +
            '</span></div></div>';

        data += '<div class="prod_details_tab"> <a href="#" onclick="addToBasket(' + json[i].itemNr +');return null;" class="prod_buy">Add to Basket</a> <a href="#" onclick="showItemDetails(' + json[i].itemNr +');return null;" class="prod_details">Details</a> </div>';
        data += '</div>';
    }

    return data;
}

function basketToHtml(json) {
    return new Promise(
        function(resolve, reject) {
            $.getJSON(
                    "/Items/",
                    function(allitems) {

                        if (!json) {
                            var data = '<div class="center_title_bar">Your shopping basket is empty</div><br><br><br><br>';
                            return data;
                        } else {
                            var data = '<div class="center_title_bar">Your shopping basket</div>';
                        }

                        for (var i = 0; i < json.items.length; i++) {
                            // find Item data
                            for (var j = 0; j < allitems.length; j++) {
                                if (allitems[j].itemNr == json.items[i].itemNr) {
                                    data += '	      <div class="basketItem">	        <div class="basketButtons">	          <span class="basketDelete-btn"></span>	        </div>	        <div class="product_img">	          <img src="' +
                                        allitems[j].imageUri +
                                        '" alt="Image could not be retrieved from Datastore"/>	        </div>	        <div class="basketDescription">	          <span>' +
                                        allitems[j].name +
                                        '</span>	          <span>' +
                                        allitems[j].descr +
                                        '</span>	        </div>	        <div class="basketPrice">' +
                                        allitems[j].price +
                                        ' ' +
                                        allitems[j].curr +
                                        '</div>	      </div>';
                                    break;
                                }
                            }
                        }
                        resolve(data);
                    });
        });
}

function ordersToHtml(json) {
    return new Promise(function(resolve, reject) {

    });
}