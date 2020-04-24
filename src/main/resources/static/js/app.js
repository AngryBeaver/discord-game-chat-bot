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

function showGameResult(gameResult){
    let message = gameResult.text
    if (gameResult.imageIds == undefined) {
        message = message + "<br/>";
    } else {
        gameResult.imageIds.forEach(
            cardId => message += "<br/><img src='" + cardId + "' />");
    }
    showMessage(message);
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

//GENERIC REST
function action(command, option) {
    return fetch("/games/" + gameId + "/" + command + "/" + option)
        .then(function (response) {
            return response.json().then(function (data) {
                if (response.status != 200) {
                    throw Exception(data.message);
                }
                return data;
            })
        });
}

//CHAT
function showMessage(message) {
    if (message.content != "") {
        $("#eventChannel .chat").append("<div>" + message + "</div>");
        scrollChat();
    }
}

function scrollChat() {
    $("#eventChannel .cardArea").animate({scrollTop: $('#eventChannel .chat').height()}, 1000);
}


function typeWriteMessage(message) {
    var i = 0;
    let speed = 100;
    typeWriter();

    function typeWriter() {
        if (i < message.length) {
            $("#eventChannel .chat").append(message.charAt(i));
            i++;
            setTimeout(typeWriter, speed);
        } else {
            $("#eventChannel .chat").append("<br/>");
            scrollChat();
        }

    }
}
$(function(){
    $('.console input').keypress(function (e) {
        if (e.which == 13) {
            let value = $('.console input').val().match(/^(\S+)\s(.*)/).slice(1);
            action(value[0], value[1]);
            $('.console input').val("");
        }
    });
});
