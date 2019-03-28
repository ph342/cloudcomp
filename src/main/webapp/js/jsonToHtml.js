function itemsToHtml(json) {
	var data = '<div class="center_title_bar">Latest Products</div>';
	for (var i = 0; i < json.length; i++) {

		data += '<div class="prod_box">    <div class="center_prod_box">      '
				+ '<div class="product_title"><a href="#">'
				+ json[i].name
				+ '</a></div><div class="product_img">'
				+ '<a href="#"><img src="'
				+ json[i].imageUri
				+ '" alt="Image could not be retrieved from Datastore" border="0" width="180px" height"auto" object-fit="cover"/></a></div><div class="prod_price">'
				+ '<span class="price">' + json[i].price + ' ' + json[i].curr
				+ '</span></div></div>';

		data += '<div class="prod_details_tab"> <a href="#" class="prod_buy">Add to Cart</a> <a href="#" class="prod_details">Details</a> </div>';
		data += '</div>';
	}

	return data;
}

function basketToHtml(json) {

	if (!json) {
		var data = '<div class="center_title_bar">Your shopping basket is empty</div>';
		return data;
	} else {
		var data = '<div class="center_title_bar">Your shopping basket</div>';
	}

	for (var i = 0; i < json.items.length; i++) {
		$
				.getJSON(
						"/Items/" + json.items[i].itemNr,
						function(item) {
							data += '	      <div class="basketItem">	        <div class="basketButtons">	          <span class="basketDelete-btn"></span>	        </div>	        <div class="product_img">	          <img src="'
									+ item.imageUri
									+ '" alt="Image could not be retrieved from Datastore"/>	        </div>	        <div class="basketDescription">	          <span>'
									+ item.name
									+ '</span>	          <span>'
									+ item.description
									+ '</span>	        </div>	        <div class="basketPrice">'
									+ item.price
									+ ' '
									+ item.curr
									+ '</div>	      </div>';
						});
	}

	return data;
}