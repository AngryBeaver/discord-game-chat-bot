//TEST
let MAP_KEY_USER_ID = "userId";

let EVENT_START = "start";
let EVENT_REFRESH_HQ = "hq";
let EVENT_REFRESH_BARRACKS = "barracks";
let EVENT_REFRESH_USER_HAND = "hand";
let EVENT_REFRESH_USER_STRIKES = "strike";
let EVENT_REFRESH_USER_INFO = "user";
let EVENT_REFRESH_GAME = "game";
let EVENT_REFRESH_USER_DRAW = "draw";
let EVENT_REFRESH_USER_DISCARD = "discard";
let EVENT_REFRESH_ALIEN = "alien";
let EVENT_REFRESH_OPERATIONS = "operations";
let EVENT_INFO = "info";
let EVENT_JOIN = "join";


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
let strikes = {};
let hand = {}
let operations = [];
let currentSelection = EVENT_INFO;
let userSelection = EVENT_INFO;


//FOOTER
function openData(target){
    if (target == '#barracksDetails') {
        currentSelection = EVENT_REFRESH_BARRACKS;
        openBarracks();
    }
    if (target == '#headQuarterDetails') {
        currentSelection = EVENT_REFRESH_HQ;
        openHQ();
    }
    if (target == '#userDetails') {
        userSelection = EVENT_REFRESH_USER_INFO;
        openUser();
    }
    if (target == '#gameDetails') {
        currentSelection = EVENT_REFRESH_GAME;
        openGame();
    }
    if (target == '#operationsDetails') {
        currentSelection = EVENT_REFRESH_OPERATIONS;
        openOperations();
    }
}


//GAME
function getGameInfo() {
    action("getGameInfo").then(gameResult => {
        game.mission = gameResult.map.mission;
        game.objective = gameResult.map.objective;
        fillMission();
    })
}

function joinGame() {
    action("join");
}

function fillMission() {
    if (game.mission != undefined) {
        var html = '<img src="mission/' + game.mission + '-location.png"/>';
        html += '<img src="mission/' + game.mission + '-objective' + game.objective + '.png"/>';
        $('#missionInfo').html(html);
    }
}

function openGame() {
    getGameInfo();
}



//COMPLEX
function openComplex() {
    activateDragDrop('#complexDeck')
}


//SERGEANT
function fillSergeant() {
    $('#sergeant .cardContainer .cardHolder').html("");
    sergeant.forEach(function (value) {
        let html = "";
        if(value != "") {
            html = '<img src="' + value + '"/>';
        }
        $('#sergeant .cardContainer .cardHolder').html(html);
    });
}

//HQ
function getHqInfo() {
    action("getHq").then(gameResult => {
        hqCards = gameResult.map.hq;
        sergeant = gameResult.map.sergeant;
        fillHqDetails();
        fillSergeant();
    })
}

function fillHqDetails() {
    let html = getHtmlFromDeck(sergeant);
    html += getHtmlFromDeck(hqCards);
    $('#headQuarterDeck').html(html);
    activateSelectableCards();
}

function openHQ() {
    getHqInfo();
}

//OPERATIONS
function getOperationsInfo() {
    action("getOperationsInfo").then(gameResult => {
        operations = gameResult.map.operations;
        fillOperationDetails();
    })
}

function fillOperationDetails() {
    let html = getHtmlFromDeck(operations);
    $('#operationsDeck').html(html);
    activateSelectableCards();
}

function openOperations() {
    getOperationsInfo();
}

//BARRACKS
$('#barracksDetails a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
    showBarrackDetails();
});

function getBarracksInfo() {
    action("getBarracks", "all").then(gameResult => {
        barracks = gameResult.map.barracks;
        fillBarrackDetails();
    })
}

function openBarracks() {
    $('#barracksDetails .nav-link:eq(1)').tab('show');
    getBarracksInfo();
}

function fillBarrackDetails() {
    let html = getHtmlFromDeck(barracks);
    $('#barracksDeck').html(html);
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


//initial loading or event "join"
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

function getUserDiscard() {  //load on open or event "discard"
    action("getUserDiscard", selectedUserId).then(gameResult => {
        hand[gameResult.map[MAP_KEY_USER_ID]] = gameResult.imageIds;
        fillUserDiscard();
    })
}

function getUserDraw() {  //load on open or event "draw"
    action("getUserDraw", selectedUserId).then(gameResult => {
        hand[gameResult.map[MAP_KEY_USER_ID]] = gameResult.imageIds;
        fillUserDraw();
    })
}




function fillStrikes() {
    if (selectedUserId != undefined) {
        let html = getHtmlFromDeck(strikes[selectedUserId]);
        $('#strikeDeck.area').html(html);
        activateSelectableCards();
    }
}

function fillUserInfo() {
    if (selectedUserId != undefined) {
        let html = '<a data-toggle="collapse" data-target="#userDetails" href="#" ><img class="fix-icon" src="char/' + userMap[selectedUserId].userChar + '-avatar.png"></a>';
        html += userMap[selectedUserId].userName;
        $('#userDetails .nav-link.disabled').html(html);

        html = '<div class="cardContainer"><img src="char/' + userMap[selectedUserId].userChar + '-char.png"></div>';
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

$('#userDetails a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
    showUser();
});

function showUser() {
    var userTab = $('#userDetails .nav-link.active').text().trim();
    $('#userDetails').find('a.cardContainer.glowBorder').removeClass('glowBorder');
    if (userTab == "info") {
        userSelection = EVENT_REFRESH_USER_INFO;
        getUserInfo();
        getUserStrikes();
    }
    if (userTab == "hand") {
        userSelection = EVENT_REFRESH_USER_HAND;
        getUserHand();
    }
    if (userTab == "discard pile") {
        userSelection = EVENT_REFRESH_USER_DISCARD;
        getUserDiscard();
    }
    if (userTab == "draw pile") {
        userSelection = EVENT_REFRESH_USER_DRAW;
        getUserDraw();
    }
}

function showUserNavi() {
    $('#userDetails nav li').hide();
    if (selectedUserId == userId) {
        $('#userDetails nav li').show();
    }
}

function openUser() {
    $('#userDetails .nav-link:eq(1)').tab('show');
    showUser();
    showUserNavi();
}


function doHqAction(activity, selector) {
    let position = getCardPosition(selector);
    if (position == undefined) {
        throw "Select a Card";
    }
    return action(activity, position - 1);
}


//DRAG&DROP
function activateDragDrop(selector) {
    $(selector).find('a.cardContainer img').draggable({
        containment: selector + " .area",
        stack: selector,
        cursor: 'move',
        zIndex: 50,
        revert: true
    });

    $(selector).find('a.cardContainer').droppable({
        accept: "a.cardContainer img",
        activeClass: "glowBorder",
        hoverClass: 'hovered',
        drop: handleCardDrop
    });
}

function handleCardDrop(event, ui) {
    let area = $(this).parents(".area");
    let from = area.find('.cardContainer').index(ui.draggable.parents(".cardContainer"));
    let to = area.find('.cardContainer').index($(this));
    area.find(".cardContainer:eq(" + from + ") .cardHolder").append($(this).find(".cardHolder img"));
    area.find(".cardContainer:eq(" + to + ") .cardHolder").append(ui.draggable[0])

}


//EVENT
$(function () {
    getHqInfo();
    getBarracksInfo();
    getGameInfo();

    socket("event", parseEvents);


    function parseEvents(gameResult) {
        if (gameResult.events.includes(EVENT_START)) {
            eventStart(gameResult);
        }
        if (gameResult.events.includes(EVENT_JOIN)) {
            getUserInfo();
        }
        if (gameResult.events.includes(EVENT_REFRESH_GAME) && currentSelection == EVENT_REFRESH_GAME) {
            getGameInfo();
        }
        if (gameResult.events.includes(EVENT_REFRESH_HQ)) {
            getHqInfo();
        }
        if (gameResult.events.includes(EVENT_REFRESH_BARRACKS) && currentSelection == EVENT_REFRESH_BARRACKS) {
            getBarracksInfo();
        }
        if (gameResult.events.includes(EVENT_REFRESH_USER_INFO) && userSelection == EVENT_REFRESH_USER_INFO && gameResult.map.userId == selectedUserId) {
            getUserInfo();
        }
        if (gameResult.events.includes(EVENT_REFRESH_USER_HAND) && userSelection == EVENT_REFRESH_USER_HAND && gameResult.map.userId == selectedUserId) {
            getUserHand();
        }
        if (gameResult.events.includes(EVENT_REFRESH_USER_DRAW) && userSelection == EVENT_REFRESH_USER_DRAW && gameResult.map.userId == selectedUserId) {
            getUserDraw();
        }
        if (gameResult.events.includes(EVENT_REFRESH_USER_DISCARD) && userSelection == EVENT_REFRESH_USER_DISCARD && gameResult.map.userId == selectedUserId) {
            getUserDiscard();
        }
        if (gameResult.events.includes(EVENT_REFRESH_USER_STRIKES) && userSelection == EVENT_REFRESH_USER_INFO && gameResult.map.userId == selectedUserId) {
            getUserStrikes();
        }
        if (gameResult.events.includes(EVENT_REFRESH_OPERATIONS) && currentSelection == EVENT_REFRESH_OPERATIONS) {
            getOperationsInfo();
        }
        if (gameResult.events.includes(EVENT_INFO)) {
            showGameResult(gameResult);
        }
    }

    function eventStart(gameResult) {
        let html = '<video autoplay id="vid">';
        html += '<source src="' + gameResult.map.mission + '.mp4" type="video/mp4">';
        html += '</video>';
        showMessage(html);
        setTimeout(function () {
            typeWriteMessage(gameResult.text);
        }, 7000);
        setTimeout(function () {
            html = '<img src="mission/' + gameResult.map.mission + '-location.png">';
            showMessage(html);
        }, 14000);
        setTimeout(function () {
            getBarracksInfo();
        }, 20000);
        setTimeout(function () {
            getHqInfo();
        }, 22000);
        setTimeout(function () {
            html = '<img src="mission/' + gameResult.map.mission + '-objective1.png">';
            showMessage(html);
            getGameInfo();
        }, 28000);
    }

});




