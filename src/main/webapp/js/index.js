$(document).ready(function() {
	// on first execution
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
	$('#loading').css('visibility', 'visible');

	switch (url) {
	case '#welcome':
	case undefined:
		$.getJSON("/Items/", function(data) {
			$('#center_content').html(itemsToHtml(data));
		});
		break;

	case '#account':
		showOrders();
		break;

	case '#basket':
		showBasket();
		break;

	default:
		break;
	}

	$('#loading').css('visibility', 'hidden');
}
