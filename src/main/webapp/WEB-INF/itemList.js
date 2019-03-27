/**
 * 
 */
// load data in localStorage
loadAll();


var db = firebase.firestore();
// save
/*
 * function storedata() { var goods = document.getElementById("goods").value;
 * var price = document.getElementById("price").value; console.log("gogogo:");
 * db.collection("items").doc(goods).set({ name: good, date: "12/03/2019",
 * desctiption: "big cutter", price: price }); }
 */
function storedata() {
	var goods = document.getElementById("goods").value;
	var price = document.getElementById("price").value;
	var Data = {
			   "name":goods,
			   "price":price
			}
			firebase.database().ref("/items").push(Data);
	}

function getdata() {
	  var goods = document.getElementById("goods").value;
	  var docRef = db.collection("items").doc(goods);
	  docRef.get().then(function(doc) {
	      if (doc.exists) {
	        console.log(doc.data().price);
	        
	      } else {
	        console.log("lose");
	      }
	    })
	    .catch(function(error) {
	      console.log("wrong:", error);
	    });
	}
function getdbdata(){
	  var path = "items";
	  var itemv = "name"
	db.ref(`/${path}`).once('value', function (snapshot) {
	    snapshot.forEach(function (item) {
	        console.log(item.key + " " + item.val());
	        var table = document.getElementById("tbody1"); 
	        var row = table.insertRow(table.rows.length);
	        var cell = row.insertCell();
	      cell.innerHTML = "<input type='checkbox' value='1' name='c"+item.val()+"'/>"; 
	    })
	});
	  
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