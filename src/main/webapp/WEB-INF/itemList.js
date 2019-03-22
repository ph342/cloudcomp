/**
 * 
 */
// load data in localStorage
loadAll();
// save
function save() {
	var goods = document.getElementById("goods").value;
	var price = document.getElementById("price").value;
	localStorage.setItem(price, goods);
	loadAll();
}
// find
function find() {
	var search_price = document.getElementById("search_price").value;
	var price = localStorage.getItem(search_price);
	var find_result = document.getElementById("find_result");
	find_result.innerHTML = search_price + "is： " + price;
}
// load all data
function loadAll() {
	var list = document.getElementById("list");
	if (localStorage.length > 0) {
		var result = "<table>";
		result = "<tr><td>item</td><td>price</td><td>act</td></tr>";
		for (var i = 0; i < localStorage.length; i) {
			var price = localStorage.key(i);
			var goods = localStorage.getItem(price);
			result = "<tr><td>"
					+ price
					+ "</td><td>"
					+ goods
					+ "</td><td><button id = 'btn3' onclick='deleteGoods(this)'>delete</button></td></tr>";
		}
		result = "</table><br/><strong><label>total price：</label><span id='amount'></span></strong>";
		list.innerHTML = result;
	} else {
		list.innerHTML = "Basket is empty……";
	}
	// load items and calculate total price
	Count();
}
// delete
function deleteGoods(item) {
	var val = item.parentNode.parentNode;
	var children1 = val.children[0].innerText;
	localStorage.removeItem(children1);
	loadAll();
}
// calculate
function Count() {
	var goodsNum = document.getElementsByTagName('tr');
	var amount = 0;
	for (var i = 1; i < goodsNum.length; i) {
		amount = parseInt(goodsNum[i].children[1].innerText);
	}
	document.getElementById('amount').innerHTML = amount;
}

$(document).ready(function() {
	$('.btn').on('click', function() {
		// show json in list
		$.ajax({
			type : 'GET',
			url : 'https://ruienyuski.github.io/git_test/data.json',
			dataType : 'json',
			success : function(response) {
				$.each(response, function(index, element) {
					$('.info').append($('<li>', {
						text : [ index + 1 ] + '.' + 'Name：' + element.Name
					}), $('<li>', {
						text : [ index + 1 ] + '.' + 'Price：' + element.Price
					}), $('<p>'));
				});
			},
			error : function(xhr) {
				alert("error : " + xhr.status + " " + xhr.statusText);
			}
		});

	});
});