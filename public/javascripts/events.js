/*Event js*/
$(document).ready(function(){
    // load log for 1 day
    $(document).on('tab.log.refresh', function(event) {
        var to = moment().utc().valueOf(),
            from = tabLogDuration;
            //console.log(from);
            room_id = $currentLog.closest(".tab-pane").attr("data-room-id");
        jsRoutes.controllers.Rooms.getMessages(room_id, from, to).ajax({
            success : function(data) {
                //console.log("Messages:"+data)
                logRendering($currentLog, data)
            },
            error : function(err) {
                alert("Error on load messsages:"+err)
            }
        });
    });

    // load full list of users
    $(document).on('tab.users.list.load', function(event) {
        var room_id = $currentLog.closest(".tab-pane").attr("data-room-id");
        jsRoutes.controllers.Rooms.getUsers(room_id).ajax({
            success : function(data) {
                //console.log("Users:"+data)
                userListRendering($currentUsersList, data)
            },
            error : function(err) {
                alert("Error on load users:"+err)
            }
        });
    });
});
