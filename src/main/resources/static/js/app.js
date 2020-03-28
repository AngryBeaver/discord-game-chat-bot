var stompClient = null;

function connect() {
    var socket = new SockJS('/chat');
    var channelId = getUrlParameter("channelId");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/topic/'+channelId, function (message) {
            console.log(message)
            showMessage(JSON.parse(message.body));
        });
    });
}


function showMessage(message) {
    if(message.content != "") {
        $("#chat").append("<div>" + message.content + "</div>");
        $("html, body").animate({ scrollTop: $(document).height() }, 1000)
    }
}

$(function () {
    connect();
});

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