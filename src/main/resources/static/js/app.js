var stompClient = null;
var gameId = getUrlParameter("gameId");

function socket(channel, handleMessage ) {
    var socket = new SockJS('/websocket');
    var gameId = getUrlParameter("gameId");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/'+channel+'/'+gameId, function (message) {
            handleMessage(JSON.parse(message.body));
        });
    });
}


function showMessage(message) {
    if(message.content != "") {
        $("#chat").append("<div>" + message.content + "</div>");
        $("html, body").animate({ scrollTop: $(document).height() }, 1000)
    }
}

function getUrlParameter(sParam) {
    var sPageURL = window.location.search.substring(1),
        sURLVariables = sPageURL.split('&'),
        sParameterName,
        i;

    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
            return sParameterName[1] === undefined ? true : decodeURIComponent(sParameterName[1]);
        }
    }
}