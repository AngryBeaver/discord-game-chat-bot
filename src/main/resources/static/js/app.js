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
    return getHtmlFromDeckSize(deck, "");
}

function deckToSrc(gameName, directory, deck,ext) {
    if(!ext){
        ext = ".png";
    }
    let result = []
    deck.forEach(function (value) {
        result.push('/' + gameName + '/' + directory + '/' + value + ext);
    });
    return result;
}

function getHtmlFromDeckSize(deck, size) {
    var html = '';
    deck.forEach(function (value) {
        html += '<a class="cardContainer ' + size + '" href="#">';
        if (value != "") {
            html += '<img src="' + value + '">';
        }
        html += '</a>';
    });
    return html;
}

function activateSelectableCards() {
    var selectCard = $('.area a.cardContainer');
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
    $("#eventChannel .area").animate({scrollTop: $('#eventChannel .chat').height()}, 1000);
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
    let html = '';
    Object.entries(userMap).forEach(([id, userInfo]) => {
        html += '<a href="javascript:selectUser(\''+id+'\');toggleData(\'#footer\',\'#userDetails\');">';
        html += '<img class="fix-icon" src="char/' + userMap[id].userChar + '-avatar.png"></a>';
    });
    $('#userSelection').html(html);
}

function selectUser(id){
    selectedUserId = id;
}

function toggleData(parent,target) {
    $(parent +' .collapse').hide();
    $(target).show();
    openData(target);
}


$(function () {
    var options = {
        cellHeight: '6.25vh',
        verticalMargin: 0,
        maxRow: 16,
        minRow: 16,
        float: true,
        draggable: true
    };
    let grid = GridStack.init(options);
    grid.on('change', function (event, ui) {
        console.log(event.target.id);
        if(event.target.id == 'eventChannel'){
            scrollChat();
        }
    });

});
//DICE
function d6(){
    return "        <ol class=\"die-list\" data-roll=\"1\" id=\"die-1\">\n" +
        "        <li class=\"die-item\" data-side=\"1\">\n" +
        "        <span class=\"dot\"></span>\n" +
        "        </li>\n" +
        "        <li class=\"die-item\" data-side=\"2\">\n" +
        "        <span class=\"dot\"></span>\n" +
        "        <span class=\"dot\"></span>\n" +
        "        </li>\n" +
        "        <li class=\"die-item\" data-side=\"3\">\n" +
        "        <span class=\"dot\"></span>\n" +
        "        <span class=\"dot\"></span>\n" +
        "        <span class=\"dot\"></span>\n" +
        "        </li>\n" +
        "        <li class=\"die-item\" data-side=\"4\">\n" +
        "        <span class=\"dot\"></span>\n" +
        "        <span class=\"dot\"></span>\n" +
        "        <span class=\"dot\"></span>\n" +
        "        <span class=\"dot\"></span>\n" +
        "        </li>\n" +
        "        <li class=\"die-item\" data-side=\"5\">\n" +
        "        <span class=\"dot\"></span>\n" +
        "        <span class=\"dot\"></span>\n" +
        "        <span class=\"dot\"></span>\n" +
        "        <span class=\"dot\"></span>\n" +
        "        <span class=\"dot\"></span>\n" +
        "        </li>\n" +
        "        <li class=\"die-item\" data-side=\"6\">\n" +
        "        <span class=\"dot\"></span>\n" +
        "        <span class=\"dot\"></span>\n" +
        "        <span class=\"dot\"></span>\n" +
        "        <span class=\"dot\"></span>\n" +
        "        <span class=\"dot\"></span>\n" +
        "        <span class=\"dot\"></span>\n" +
        "        </li>\n" +
        "        </ol>";
}

function rollDice(results,char) {
    let dice = $("<div class='dice'></div>");
    $("#eventChannel .chat").append(char+" rolls:");
    $("#eventChannel .chat").append(dice);
    results.forEach(result=>{
        let die = $(d6());
        if(getRandomNumber(0,1)){
            die.addClass("odd-roll");
        }else{
            die.addClass("even-roll");
        }
        dice.append(die);
    });
    scrollChat();
    setTimeout(diceRoll, 200,dice,results)
    setTimeout(diceResult, 5000,dice,results)
}

function diceRoll(dice,results){
    let list = dice.find(".die-list");
    let i = 0;
    results.forEach(result=>{
        toggleClasses(list.get(i));
        list.get(i).dataset.roll = result;
        i++;
    });
}

function getRandomNumber(min, max) {
    min = Math.ceil(min);
    max = Math.floor(max);
    return Math.floor(Math.random() * (max - min + 1)) + min;
}

function diceResult(dice,results){
    dice.remove();
    showMessage(results.join(", "));
}

function toggleClasses(die) {
    die.classList.toggle("odd-roll");
    die.classList.toggle("even-roll");
}
