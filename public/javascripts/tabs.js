/* Bootstrap tabs modification*/

var $currantTab = ($currantTab == undefined) ? $('[id="#room-1"] .room-message-log .well-large') : $currantTab;

// bind tabs events
$(document).on('click.tab.data-api.show', '.tab-pane', function(event) {
    $currantTab = $(this).find(".room-message-log .well-large");
});