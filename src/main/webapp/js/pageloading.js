// variable holding the url
var lasturl = "";

function checkURL(hash) {
    // handle navigation
    if (!hash){
    	// if a hash was provided, a link was clicked and we definitely need a reload
    	// otherwise automatic hash detection
        hash = (window.location.hash) ? window.location.hash : '#';

	    if (hash == lasturl)
	        return;
    }
    
    lasturl = hash;

    loadPage(hash);
}

function loadPage(url) {
    // load page content dynamically

    switch (url) {
        case '#welcome':
        case undefined:
        case '#':
        	// load the main page
            $('#loading').css('visibility', 'visible');
            $.getJSON("/Items/", function(data) {
                $('#center_content').html(itemsToHtml(data));
            }).fail(function() {
                alertify.error('Items could not be retrieved from server');
            }).always(function() {
                $('#loading').css('visibility', 'hidden');
            });
            break;

        case '#account':
        	// load the orders page
            login().then(function(user) {
                showOrders(user);
            }).catch(function(err) {
                alertify.error(err);
            });
            break;

        case '#basket':
        	// load the shopping basket page
            login().then(function(user) {
                showBasket(user);
            }).catch(function(err) {
                alertify.error(err);
            });
            break;

        default:
            break;
    }
}