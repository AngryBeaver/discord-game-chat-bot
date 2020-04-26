//EVENT
let EVENT_INFO = "info";
let EVENT_JOIN = "join";
let EVENT_REFRESH_DUNGEON = "dungeon";
let EVENT_REFRESH_PLAY_AREA = "playArea";
let EVENT_REFRESH_GAME = "game";
let EVENT_REFRESH_USER = "user";

let currentSelection = EVENT_INFO;
let playArea = [];
let clankArea = [];
let currentUserId;
let dungeon = [];

$('#footer .collapse').on('shown.bs.collapse', function (e) {
    if (e.target.id.trim() == 'userDetails') {
        currentSelection = EVENT_REFRESH_USER;
        getUserInfo();
        getUserHand();
        if (selectedUserId != userId) {
            $('#userDetails nav li').hide();
        } else {
            $('#userDetails nav li').show();
        }
    }
    if (e.target.id.trim() == 'selectUser') {
        currentSelection = EVENT_INFO;
        openSelection();
    }
    if (e.target.id.trim() == 'gameDetails') {
        currentSelection = EVENT_REFRESH_GAME;
        showGame();
    }
});


$(function () {
    $('#gameInfo nav li:gt(1)').hide();
    $('#gameInfo nav li:lt(2)').show();
    socket("event", parseEvents);

    function parseEvents(gameResult) {
        if(gameResult.map.playArea){
            playArea = gameResult.map.playArea;
        }
        if(gameResult.map.currentUser){
            currentUserId = gameResult.map.currentUser;
        }

        if (gameResult.events.includes(EVENT_INFO)) {
            showGameResult(gameResult);
        }
        if (gameResult.events.includes(EVENT_JOIN)) {
            getUserInfo();
        }
        if (gameResult.events.includes(EVENT_REFRESH_GAME)) {
            getGame();
        }
        if (gameResult.events.includes(EVENT_REFRESH_DUNGEON) && currentSelection == EVENT_REFRESH_DUNGEON) {
            getDungeon();
        }
        if (gameResult.events.includes(EVENT_REFRESH_PLAY_AREA) && currentSelection == EVENT_REFRESH_PLAY_AREA) {
            getPlayer();
        }
        if (gameResult.events.includes(EVENT_REFRESH_USER)) {
            getUserInfo();
            if (currentSelection == EVENT_REFRESH_USER) {
                getUserHand();
            }
        }


    }

});
//user
//initial loading or event "join"
function getUserHand() {
    $('#handArea').html("");
    if (userId == selectedUserId) {
        action("getHand").then(
            gameResult => {
                let html = getHtmlFromDeck(deckToSrc("clank", "images", gameResult.map.hand));
                $('#handArea').html(html);
                activateSelectableCards();
            }
        );
    }
    activateSelectableCards();
}

function fillUserInfo() {
    if (selectedUserId != undefined) {
        let html = '<a data-toggle="collapse" data-target="#userDetails" href="#" ><img class="fix-icon" src="char/' + userMap[selectedUserId].userChar + '-avatar.png"></a>';
        html += userMap[selectedUserId].userName;
        $('#userDetails .nav-link.disabled').html(html);
        fillClankUser(userMap[selectedUserId], "#userData")

        $('#userItems').html(getHtmlFromDeckSize(deckToSrc("clank", "images", userMap[selectedUserId].items), "sm"));
        activateSelectableCards();


    }
    if (userMap[currentUserId]) {
        fillClankUser(userMap[currentUserId], "#activeUserData");
    }
}

function fillClankUser(user, target) {
    let health = ((10 - user.damage) * 10);
    let cubes = [];
    for (let i = 0; i < user.clankCubes; i++) {
        cubes[i] = user.userChar;
    }
    $(target).children('div:eq(0)').find('span').html(user.userName);
    $(target).children('div:eq(0)').find(' img').attr('src', 'char/' + user.userChar + '-avatar.png');
    $(target).children('div:eq(1)').find('span').html(user.coins);
    $(target).children('div:eq(2)').find('.progress-bar').css({width: health + '%'});
    $(target).children('div:eq(2)').find('.progress-bar').html(health + '%');
    $(target).children('div:eq(3)').html(itemListHtml(user.items));
    $(target).children('div:eq(4)').html(clankCubesHtml(cubes));
    activateSelectableCards();
}


function getGame() {
    showGameNavi();
    action("getGame").then(
        gameResult => {
            clankArea = gameResult.map.clankArea;
            showGameNavi();
            $('#clankArea span').html(clankCubesHtml(clankArea));
        }
    )
    activateSelectableCards();
}

function getDungeon() {
    showGameNavi();
    action("getDungeon").then(gameResult => {
        dungeon = gameResult.map.dungeon;
        let html = getHtmlFromDeck(deckToSrc("clank", "images", dungeon));
        $('#dungeonsDeck .area').html(html);
        activateSelectableCards();
    });
}

function getPlayer() {
    showGameNavi();
    action("getPlayArea", currentUserId).then(gameResult => {
        playArea = gameResult.map.playArea;
        fillPlayer();
        activateSelectableCards();
    });
}

function fillPlayer(){
    fillClankUser(userMap[currentUserId], "#activeUserData")
    let html = getHtmlFromDeck(deckToSrc("clank", "images", playArea));
    $('#playArea').html(html);
    activateSelectableCards();
}


//game
$('#gameDetails a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
    showGame();
});

function showGame() {
    var tab = $('#gameDetails .nav-link.active').text().trim();
    $('#gameDetails').find('a.cardContainer.glowBorder').removeClass('glowBorder');
    if (tab == "info") {
        currentSelection = EVENT_REFRESH_GAME;
        getGame();
    }
    if (tab == "dungeon") {
        currentSelection = EVENT_REFRESH_DUNGEON;
        getDungeon();
    }
    if (tab == "player") {
        currentSelection = EVENT_REFRESH_PLAY_AREA;
        getPlayer();
    }
}

function showGameNavi() {
    if (currentUserId) {
        if (currentUserId != userId) {
            $('#gameDetails nav li').hide();
        } else {
            $('#gameDetails nav li').show();
            $('#gameInfo nav li:gt(1)').show();
            $('#gameInfo nav li:lt(2)').hide();
        }
    }
}

function clankCubesHtml(colors) {
    var html = '';
    colors.forEach(function (color) {
        html += '<div class="clankCube ' + color + '"></div>'
    });
    return html;
}

function itemListHtml(items) {
    var html = '';
    items.forEach(function (item) {
        html += '<a class="cardContainer ssm-icon" href="#">'
        html += '<img src="items/' + item + '.png"/></a>'
    });
    return html;
}





