//EVENT
$(function () {
    socket("event", parseEvents);

    function parseEvents(gameResult) {
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

    $('.console input').keypress(function (e) {
        if (e.which == 13) {
            let value = $('.console input').val().match(/^(\S+)\s(.*)/).slice(1);
            action(value[0], value[1]);
            $('.console input').val("");
        }
    });
});




