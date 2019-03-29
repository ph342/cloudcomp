$(document).ready(function() {
	// on first executionhttp://localhost:8080/#
	if (!window.location.hash)
		window.location.hash = '#welcome';

	checkURL();
	$('ul li a').click(function(e) {
		checkURL(this.hash);
	});

	// check the url 4 times a
	// second (on the frontend)
	setInterval("checkURL()", 250);
});

// variable holding the url
var lasturl = "";

function checkURL(hash) {
	// handle navigation
	if (!hash)
		hash = window.location.hash;

	if (hash == lasturl)
		return;

	lasturl = hash;

	loadPage(hash);
}

function loadPage(url) {
	// load page content dynamically

	switch (url) {
	case '#welcome':
	case undefined:
	case '#':
		$('#loading').css('visibility', 'visible');
		$.getJSON("/Items/", function(data) {
			$('#center_content').html(itemsToHtml(data));
		}).always(function() {
			$('#loading').css('visibility', 'hidden');
		});
		break;

	case '#account':
		login().then(function(user) {
			showOrders(user);
		}).catch(function(err) {
			alert(err);
		});
		break;

	case '#basket':
		login().then(function(user) {
			showBasket(user);
		}).catch(function(err) {
			alert(err);
		});
		break;

	default:
		break;
	}
}

function showBasket(user) {
	if (!user) {
		return;
	}

	$('#loading').css('visibility', 'visible');

	$.getJSON("/Baskets/" + user.uid, function(data) {
		basketToHtml(data).then(function(html) {
			$('#center_content').html(html);
		});
	}).fail(function() {
		alert("Could not retrieve basket.");
	}).always(function() {
		$('#loading').css('visibility', 'hidden');
	});
}

function showOrders(user) {
	if (!user) {
		return;
	}

	$('#loading').css('visibility', 'visible');

	$.getJSON("/Orders/firebaseUid/" + user.uid, function(data) {
		ordersToHtml(data).then(function(html) {
			$('#center_content').html(html);
		});
	}).fail(function() {
		alert("Could not retrieve order history.");
	}).always(function() {
		$('#loading').css('visibility', 'hidden');
	});
}

function showItemDetails(itemNr){
	if(!alertify.itemDetails){
		  // define a new dialog
		  alertify.dialog('itemDetails',function factory(){
		    return{
		      main:function(message){
		        this.message = message;
		      },
		      setup:function(){
		          return { 
		            buttons:[{text: "OK", key:27/* Esc */}],
		            focus: { element:0 },
		            options:{ title: 'Details' }
		          };
		      },
		      prepare:function(){
		        this.setContent(this.message);
		      }
		  }});
		}

	$.getJSON("/Items/"+itemNr, function(data) {
		alertify.itemDetails(data.descr);
	}).fail(function() {
		alert("Could not retrieve item details.");
	});
}

function addToBasket(itemNr){
	
	alertify.notify('Item added to Basket', 'success', 2);
}