$(document).ready(function() {
    // on first execution
    checkURL();

    // check the url 4 times a
    // second (on the frontend)
    setInterval("checkURL()", 250);
});

// set event handlers
$(document).on('click', '.basketDelete-btn', removeFromBasket);
$(document).on('click', '.prod_details', showItemDetails);
$(document).on('click', '.prod_buy', addToBasket);
$(document).on('click', '#orderButton', placeOrder);
$(document).on('click', 'ul li a', function() { checkURL(this.hash); });

// popup for item details on the main page
function showItemDetails() {
    var itemNr = $(this).data('itemnr');

    if (!alertify.itemDetails) {
        // define a new dialog (Alertify factory)
        alertify.dialog('itemDetails', function factory() {
            return {
                main: function(message) {
                    this.message = message;
                },
                setup: function() {
                    return {
                        buttons: [{
                            text: "OK",
                            key: 27 /* Esc */
                        }],
                        focus: {
                            element: 0
                        },
                        options: {
                            title: 'Details'
                        }
                    };
                },
                prepare: function() {
                    this.setContent(this.message);
                }
            }
        });
    }
    
    // get the item object from the backend
    $.getJSON("/Items/" + itemNr, function(data) {
        alertify.itemDetails(data.descr);
    }).fail(function() {
        alertify.error('Items could not be retrieved from server');
    });
}