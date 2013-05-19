/* Bootstrap tabs modification*/

var $currentTab = $('[id="#room-1"]'),
    $currentLog = ($currentTab != undefined) ? $currentTab.find(".room-message-log .well-large") : $currentLog,
    $currentUsersList = ($currentTab != undefined) ? $currentTab.find(".users-list .table") : $currentUsersList;

// bind tabs events
$(document).on('click.tab.data-api.show', '.tab-pane', function(event) {
    $currentTab = $(this);
    $currentLog = $(this).find(".room-message-log .well-large");
    $currentUsersList = $(this).find(".users-list .table");
});