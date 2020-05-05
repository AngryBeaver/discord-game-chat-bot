//EVENT
let EVENT_INFO = "info";
let EVENT_JOIN = "join";
let EVENT_START_GAME = "start";
let EVENT_ROLL = "roll";
let EVENT_REFRESH_USER = "user";
let EVENT_REFRESH_PLAYER = "player";
let EVENT_REFRESH_GAME = "game";

let currentSelection = EVENT_INFO;
let currentUserId;
let drops = [];
let actionInfo = {};

function getActions(){
    action("getActions").then(
        gameResult => {
            actionInfo = gameResult.map.actions;
        }
    )
}

function openData(target){
    if (target == '#userDetails') {
        currentSelection = EVENT_REFRESH_USER;
        getUserInfo();
        if (selectedUserId != userId) {
            $('#userDetails nav li').hide();
        } else {
            $('#userDetails nav li').show();
        }
    }
    if (target == '#gameDetails') {
        currentSelection = EVENT_REFRESH_GAME;
        showGame();
    }
}


$(function () {
    getGame();
    getActions();
    $('[data-toggle="tooltip"]').tooltip()

    $('#gameInfo nav li:gt(2)').hide();
    $('#gameInfo nav li:lt(3)').show();
    socket("event", parseEvents);

    function parseEvents(gameResult) {
        if(gameResult.map.currentUser){
            currentUserId = gameResult.map.currentUser;
            showGameNavi();
        }
        if (gameResult.events.includes(EVENT_INFO)) {
            showGameResult(gameResult);
        }
        if (gameResult.events.includes(EVENT_JOIN)) {
            getUserInfo();
        }
        if (gameResult.events.includes(EVENT_REFRESH_GAME) && currentSelection == EVENT_REFRESH_GAME) {
            getGame();
        }
        if (gameResult.events.includes(EVENT_REFRESH_USER)) {
            getUserInfo();
        }
        if( gameResult.events.includes(EVENT_ROLL)){
            rollDice(gameResult.map.roll,gameResult.map.user.userChar)
        }
    }
});


function fillUserInfo() {
    if (selectedUserId != undefined && currentSelection == EVENT_REFRESH_USER) {
        let html = '<a data-toggle="collapse" data-target="#userDetails" href="#" ><img class="fix-icon" src="char/' + userMap[selectedUserId].userChar + '-avatar.png"></a>';
        html += userMap[selectedUserId].userName;
        $('#userDetails .nav-link.disabled').html(html);

        fillSurvivor(userMap[selectedUserId], "#userInfo")
        activateSelectableCards();
    }
    if (userMap[currentUserId] && currentSelection == EVENT_REFRESH_PLAYER) {
        //TODO only if in view
        fillSurvivor(userMap[currentUserId], "#activePlayer");
    }
}

function fillSurvivor(user, target) {
    let html = '<img class=\"fix-icon\" src=\"char/' + user.userChar + '-avatar.png\"><span> '+user.userName+'</span></br><span> '+user.userChar+'</span></div>';
    $(target +" .user-header div").first().html(html);
    $(target +" .user-char .cardContainer img").attr({src:"/zombicide/char/"+user.userChar+"-card.jpg"});

    let actions = user.survivor.actions;
    if(user.zombified){
        actions = user.survivor.z_actions;
    }
    html = addAction("blue",0,actions);
    html += addAction("yellow",0,actions);
    html += addAction("orange",0,actions);
    html += addAction("orange",1,actions);
    html += addAction("red",0,actions);
    html += addAction("red",1,actions);
    html += addAction("red",2,actions);
    $(target +" .user-action").html(html);
    let percent = ((43-Math.min(user.xp,43))/44*100);
    $(target +" .user-xp .xp-bar").css({width: "calc("+percent+"% + 1px)"});
    $(target + " .user-gear div").html(getHtmlFromDeck(deckToSrc("zombicide","gear",user.gear,".jpg")));
    activateSelectableCards();
}

function addAction(color,amount,actions){
    return "<div class='danger-"+color+" data-toggle='tooltip' data-placement='top' title='"+actionInfo[actions[color][amount]]+"'>"+actions[color][amount]+"</div>";
}

function getGame() {
    showGameNavi();
    action("getGame").then(
        gameResult => {
            drops = gameResult.map.drops;
            let html = getHtmlFromDeck(deckToSrc("zombicide", "gear", drops,".jpg"));
            $('#dropsDeck').html(html);
            activateSelectableCards();
            showGameNavi();
        }
    )
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
    if (tab == "player") {
        currentSelection = "player";
        getUserInfo(currentUserId);
    }
}

function showGameNavi() {
    if (currentUserId) {
        if (currentUserId != userId) {
            $('#gameDetails nav li').hide();
            $('#gameInfo nav li:eq(3)').show();
        } else {
            $('#gameDetails nav li').show();
            $('#gameInfo nav li:gt(2)').show();
            $('#gameInfo nav li:lt(3)').hide();
        }

    }
}
