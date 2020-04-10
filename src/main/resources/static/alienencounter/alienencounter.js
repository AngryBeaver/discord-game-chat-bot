//TEST
let MAP_KEY_USER_ID = "userId";
let MAP_KEY_USER_MAP = "userMap";

var testBarracks = ["crew/captain-of-the-ship.png", "crew/captain-of-the-ship.png", "crew/captain-of-the-ship.png"];
var testHqCards = ["crew/first-aid.png", "crew/here-kitty-kitty.png", "crew/right.png", "crew/captain-of-the-ship.png", "crew/motion-detector.png"];
var testSergeant = ["crew/sergeant-green.png"];
var testStrikes = {"10": ["strike/flesh-wound.png"]};
var testUser = {
    "10": {"userChar": "priest", "userName": "AngryBeaver"},
    "11": {"userChar": "scout", "userName": "Player2"},
    "12": {"userChar": "scout", "userName": "Player3"}
}
let barracks = []
let hqCards = [];
let sergeant = "";
let game = {};
let userMap = {};
let userId = "guest";
let selectedUserId;
let strikes = {};
let hand = {}

//FOOTER
var footer = $('#footer');
footer.on('shown.bs.collapse', '.collapseFooter', function (e) {
    if (e.target.id.trim() == 'barracksDetails') {
        openBarracks();
    }
    if (e.target.id.trim() == 'headQuarterDetails') {
        openHQ();
    }
    if (e.target.id.trim() == 'userDetails') {
        openUser();
    }
    if (e.target.id.trim() == 'selectUser') {
        openSelection();
    }
    if (e.target.id.trim() == 'gameDetails') {
        openGame();
    }
});

//GAME
function startGame() {
    action("start", "nostromo");
}

function joinGame() {
    action("join");
}

function fillMission() {
    if (game.mission != undefined) {
        var html = '<img height="315px" src="mission/' + game.mission + '-location.png"/>';
        html += '<img height="315px" src="mission/' + game.mission + '-objective' + game.objective + '.png"/>';
        $('#missionInfo').html(html);
    }
}

function openGame() {
    fillMission();
}

function getGameInfo() {
    action("getGameInfo").then(gameResult => {
        game.mission = gameResult.map.mission;
        game.objective = gameResult.map.objective;
        fillMission();
    })
}


//SELECTION
var fallback = false;
footer.on('hidden.bs.collapse', '.collapseFooter', function () {
    if (!fallback) {
        footer.find('#selectUser').collapse('show');
    }
    fallback = false;
    $('#barracksDeck .area').html("");
});
footer.on('show.bs.collapse', '.collapseFooter', function () {
    fallback = true;
});

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

function openSelection() {

}

//SERGEANT
function fillSergeant(){
    $('#sergeant .cardContainer .cardHolder').html("");
    sergeant.forEach(function (value) {
        let html ='<img src="' + value + '"/>';
        $('#sergeant .cardContainer .cardHolder').html(html);
    });
}

//HQ
function getHqInfo() {
    action("getHq").then(gameResult => {
        hqCards = gameResult.map.hq;
        sergeant = gameResult.map.sergeant;
        fillHqDetails();
        fillHq();
        fillSergeant();
    })
}

function fillHqDetails() {
    let html = getHtmlFromDeck(sergeant);
    html += getHtmlFromDeck(hqCards);
    $('#headQuarterDeck .area').html(html);
    activateSelectableCards();
}

function openHQ() {

}

function fillHq() {
    $('#hq .cardContainer .cardHolder').html("");
    Object.entries(hqCards).forEach(([key, value]) => {
        $('#hq .cardContainer .cardHolder:eq(' + key + ')').html('<img src="' + value + '"/>');
    });
}

//BARRACKS
$('#barracksDetails a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
    showBarrackDetails();
});

function getBarracksInfo() {
    action("getBarracks", "all").then(gameResult => {
        barracks = gameResult.map.barracks;
        fillBarracks();
        fillBarrackDetails();
    })
}

function openBarracks() {
    $('#barracksDetails .nav-link:eq(1)').tab('show');
    fillBarrackDetails();
}

function fillBarracks() {
    $('#barracks .cardHolder').html("");
    if (barracks.length > 0) {
        $('#barracks .cardHolder').html('<img src="back.png"/>');
    }
}

function fillBarrackDetails() {
    let html = getHtmlFromDeck(barracks);
    $('#barracksDeck .area').html(html);
    showBarrackDetails();
}

function showBarrackDetails() {
    $('#barracksDeck a.cardContainer').hide();
    var visibility = $('#barracksDetails .nav-link.active').text().trim();
    if (visibility == "all") {
        $('#barracksDeck a.cardContainer').show();
    }
    if (visibility == "first") {
        $('#barracksDeck a.cardContainer').first().show();
    }
    if (visibility == "second") {
        $('#barracksDeck a.cardContainer:lt(2)').show();
    }
    activateSelectableCards();
}


//MYSelf
fetch("/user")
    .then(function (response) {
        response.json().then(function (data) {
            console.log(data);
            userId = data.id;
            getUserInfo();
        });
    });

//USER
function selectUserId(id) {
    selectedUserId = id;
}

//initial loading or event "join"
function getUserInfo() {
    action("getUserInfo", userId).then(gameResult => {
        userMap = gameResult.map[MAP_KEY_USER_MAP];
        fillUserList();
        fillUserInfo();
    })
}

function getUserStrikes() {  //load on open or event "strikes"
    action("getUserStrikes", selectedUserId).then(gameResult => {
        strikes[gameResult.map[MAP_KEY_USER_ID]] = gameResult.imageIds;
        fillStrikes();
    })
}

function getUserHand() {  //load on open or event "hand"
    action("getUserHand", selectedUserId).then(gameResult => {
        hand[gameResult.map[MAP_KEY_USER_ID]] = gameResult.imageIds;
        fillUserHand();
    })
}

function getUserDiscard() {  //load on open or event "hand"
    action("getUserDiscard", selectedUserId).then(gameResult => {
        hand[gameResult.map[MAP_KEY_USER_ID]] = gameResult.imageIds;
        fillUserDiscard();
    })
}

function getUserDraw() {  //load on open or event "hand"
    action("getUserDraw", selectedUserId).then(gameResult => {
        hand[gameResult.map[MAP_KEY_USER_ID]] = gameResult.imageIds;
        fillUserDraw();
    })
}

$('#userDetails a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
    showUser();
});


function fillStrikes() {
    if (selectedUserId != undefined) {
        let html = getHtmlFromDeck(strikes[selectedUserId]);
        $('#strikeDeck.area').html(html);
        activateSelectableCards();
    }
}

function fillUserInfo() {
    if (selectedUserId != undefined) {
        let html = '<a data-toggle="collapse" data-target="#userDetails" href="#" ><img src="char/' + userMap[selectedUserId].userChar + '-avatar.png"></a>';
        html += userMap[selectedUserId].userName;
        $('#userDetails .nav-link.disabled').html(html);

        html = '<img src="char/' + userMap[selectedUserId].userChar + '-char.png">';
        $('#userChar').html(html);
        activateSelectableCards();
    }
}

function fillUserHand() {
    if (selectedUserId != undefined) {
        let html = getHtmlFromDeck(hand[selectedUserId]);
        $('#userHand .area').html(html);
        activateSelectableCards();
    }
}

function fillUserDiscard() {
    if (selectedUserId != undefined) {
        let html = getHtmlFromDeck(hand[selectedUserId]);
        $('#userDiscardPile .area').html(html);
        activateSelectableCards();
    }
}

function fillUserDraw() {
    if (selectedUserId != undefined) {
        let html = getHtmlFromDeck(hand[selectedUserId]);
        $('#userDrawPile .area').html(html);
        activateSelectableCards();
    }
}

function showUser() {
    var userTab = $('#userDetails .nav-link.active').text().trim();
    $('#userDetails').find('a.cardContainer.glowBorder').removeClass('glowBorder');
    if (userTab == "info") {

    }
    if (userTab == "hand") {

    }
    if (userTab == "discard pile") {
        getUserDiscard();
    }
    if (userTab == "draw pile") {
        getUserDraw();
    }
}

function showUserNavi(){
    $('#userDetails .col-sm-1').hide();
 if(selectedUserId == userId){
     $('#userDetails .col-sm-1').show();
 }
}

function openUser() {
    $('#userDetails .nav-link:eq(1)').tab('show');
    fillUserInfo();
    getUserStrikes();
    getUserHand();
    showUserNavi();
}

//USER ACTION
function discardKill(){
    doCardAction("discardKill",'#userDiscardPile').then(gameResult=>getUserDiscard());
}

function drawKill(){
    doCardAction("drawKill",'#userDrawPile').then(gameResult=>getUserDraw());
}

function handKill(){
    doCardAction("handKill",'#userHand');
}



//UTILITIES
function getHtmlFromDeck(deck) {
    var html = '';
    deck.forEach(function (value) {
        html += '<a class="cardContainer" href="#">';
        html += '<img height="315px" src="' + value + '">';
        html += '</a>';
    });
    return html;
}

function activateSelectableCards() {
    var selectCard = $('.cards .area a.cardContainer');
    selectCard.on('click', function () {
        $(this).parent('.area').find('a.cardContainer.glowBorder').removeClass('glowBorder');
        $(this).addClass('glowBorder');
    });
}

function doHqAction(activity, selector) {
    let position = getCardPosition(selector);
    if (position == undefined) {
        throw "Select a Card";
    }
    return action(activity,position-1);
}

function doCardAction(activity, selector) {
    let cardId = getCardSelection(selector);
    if (cardId == undefined) {
        throw "Select a Card";
    }
    return action(activity,cardId);
}

function getCardPosition(selector) {
    let selection = $(selector).find('a.cardContainer.glowBorder img');
    return $(selector).find('a.cardContainer img').index(selection);
}

function getCardSelection(selector) {
    let parts = $(selector).find('a.cardContainer.glowBorder img').attr('src').split("/");
    return parts[parts.length-1]
}


//CHAT
function showMessage(message) {
    if (message.content != "") {
        $("#eventChannel .chat").append("<div>" + message + "</div>");
        scrollChat();
    }
}

function scrollChat() {
    $("#eventChannel").animate({scrollTop: $('#eventChannel .chat').height()}, 1000);
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

//EVENT
$(function () {
    getHqInfo();
    getBarracksInfo();

    socket("event", parseEvents);

    let EVENT_START = "start";
    let EVENT_REFRESH_HQ = "hq";
    let EVENT_REFRESH_BARRACKS = "barracks";
    let EVENT_REFRESH_USER_HAND = "hand";
    let EVENT_REFRESH_USER_STRIKES = "strike";
    let EVENT_REFRESH_USER_INFO = "user";
    let EVENT_REFRESH_GAME = "game";
    let EVENT_REFRESH_USER_DRAW = "draw";
    let EVENT_REFRESH_USER_DISCARD = "discard";
    let EVENT_INFO = "info";

    function parseEvents(gameResult) {
        if (gameResult.events.includes(EVENT_START)) {
            eventStart(gameResult);
        }
        if (gameResult.events.includes(EVENT_REFRESH_GAME)) {
            getGameInfo();
        }
        if (gameResult.events.includes(EVENT_REFRESH_HQ)) {
            getHqInfo();
        }
        if (gameResult.events.includes(EVENT_REFRESH_BARRACKS)) {
            getBarracksInfo();
        }
        if (gameResult.events.includes(EVENT_REFRESH_USER_INFO)) {
            getUserInfo();
        }
        if (gameResult.events.includes(EVENT_REFRESH_USER_HAND)) {
            getUserHand();
        }
        if (gameResult.events.includes(EVENT_REFRESH_USER_DRAW)) {
            getUserDraw();
        }
        if (gameResult.events.includes(EVENT_REFRESH_USER_DISCARD)) {
            getUserDiscard();
        }
        if (gameResult.events.includes(EVENT_REFRESH_USER_STRIKES)) {
            getUserStrikes();
        }
        if (gameResult.events.includes(EVENT_INFO)) {
            let message = gameResult.text
            if (gameResult.imageIds == undefined) {
                message = message + "<br/>";
            } else {
                gameResult.imageIds.forEach(
                    cardId => message += "<br/><img width='300' src='" + cardId + "' />");
            }
            showMessage(message);
        }

    }

    function eventStart(gameResult) {
        let html = '<video autoplay id="vid" width="300">';
        html += '<source src="startsequence.mp4" type="video/mp4">';
        html += '</video>';
        showMessage(html);
        setTimeout(function () {
            typeWriteMessage(gameResult.text);
        }, 7000);
        setTimeout(function () {
            html = '<img width="300" src="mission/' + gameResult.map.mission + '-location.png">';
            showMessage(html);
        }, 24000);
        setTimeout(function () {
            getBarracksInfo();
        }, 46000);
        setTimeout(function () {
            getHqInfo();
        }, 62000);
        setTimeout(function () {
            html = '<img width="300" src="mission/' + gameResult.map.mission + '-objective1.png">';
            showMessage(html);
            getGameInfo();
        }, 79000);

    }
});


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
