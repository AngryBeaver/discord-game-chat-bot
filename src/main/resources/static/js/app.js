var stompClient = null;
var gameId = getUrlParameter("gameId");
let userId = "guest";
let selectedUserId;
let userMap = {};

function socket(channel, handleMessage) {
    var socket = new SockJS('/websocket');
    var gameId = getUrlParameter("gameId");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/' + channel + '/' + gameId, function (message) {
            handleMessage(JSON.parse(message.body));
        });
    });
}

//overlay
function showGameResult(gameResult) {
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
    if (message.content != "") {
        $("#chat").append("<div>" + message.content + "</div>");
        $("html, body").animate({scrollTop: $(document).height()}, 1000)
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

//GAME

//UTILITIES
function getHtmlFromDeck(deck) {
    return getHtmlFromDeckSize(deck,"");
}

function deckToSrc(gameName,  directory, deck){
    let result = []
    deck.forEach(function (value) {
        result.push('/'+gameName+'/'+directory+'/'+ value+ '.png');
    });
    return result;
}

function getHtmlFromDeckSize(deck, size) {
    var html = '';
    deck.forEach(function (value) {
        html += '<a class="cardContainer '+size+'" href="#">';
        if (value != "") {
            html += '<img src="' + value + '">';
        }
        html += '</a>';
    });
    return html;
}

function activateSelectableCards() {
    var selectCard = $('.cards .area a.cardContainer');
    selectCard.on('click', function () {
        $(this).closest('.area').find('a.cardContainer.glowBorder').removeClass('glowBorder');
        $(this).addClass('glowBorder');
    });
}

function doCardAction(activity, selector) {
    let cardId = getCardSelection(selector);
    if (cardId == undefined) {
        throw "Select a Card";
    }
    return action(activity, cardId);
}

function getCardPosition(selector) {
    let selection = $(selector).find('a.cardContainer.glowBorder');
    return $(selector).find('a.cardContainer').index(selection);
}

function getCardSelection(selector) {
    let parts = $(selector).find('a.cardContainer.glowBorder img').attr('src').split("/");
    return parts[parts.length - 1].split(".")[0];
    //return parts[parts.length - 1]
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

$(function () {
    $('.console input').keypress(function (e) {
        if (e.which == 13) {
            let value = $('.console input').val().match(/^(\S+)\s(.*)/).slice(1);
            action(value[0], value[1]);
            $('.console input').val("");
        }
    });
});


//USER
fetch("/user")
    .then(function (response) {
        response.json().then(function (data) {
            userId = data.id;
            getUserInfo();
        });
    });

function getUserInfo() {
    return action("getUserInfo", userId).then(gameResult => {
        userMap = gameResult.map.userMap;
        fillUserInfo();
        fillUserList();
    })
}

function fillUserList() {
    let html = '<button onclick="selectUserId(\'' + userId + '\')" class="btn btn-primary" data-toggle="collapse" data-target="#userDetails">';
    html += userMap[userId].userName;
    html += '</button>';
    Object.entries(userMap).forEach(([id, userInfo]) => {
        if (id != userId) {
            html += '<button onclick="selectUserId(\'' + id + '\')" class="btn btn-primary" data-toggle="collapse" data-target="#userDetails">';
            html += userMap[id].userName;
            html += '</button>';
        }
    });
    $('#userList').html(html);
}

let reselectUser = false;
function selectUserId(id) {
    if(selectedUserId != id){
        selectedUserId = id;
        reselectUser = true;
    }
}

$(function () {
    $('#footer .collapse').on('hidden.bs.collapse', function () {
        if (reselectUser) {
            $('#userDetails').collapse('show')
        }
        reselectUser = false;
    });
    $('#footer .collapse').on('show.bs.collapse', function () {
        reselectUser = false;
    });
});