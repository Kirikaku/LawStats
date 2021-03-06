function getQueriedVerdicts() {
    var url = './filter/searchVerdicts';

    console.info('In GetQueriedVerdicts');

    $("#verdictTableBlock").load(url);
}

function reset() {
    var table = document.getElementById("attributeTable").children[0];
    while (table.firstElementChild) {
        table.removeChild(table.firstElementChild);
        console.log(table.firstChild);
    }

    var row = document.createElement("tr");
    console.log(row);
    var td1 = document.createElement("td");
    var td2 = document.createElement("td");
    var td3 = document.createElement("td");
    var td4 = document.createElement("td");

    td1.innerHTML = "Kategorie";
    td2.innerHTML = "Schlagwort";
    td3.innerHTML = "Startdatum";
    td4.innerHTML = "Enddatum";

    td1.style.fontWeight = 'bold';
    td2.style.fontWeight = 'bold';
    td3.style.fontWeight = 'bold';
    td4.style.fontWeight = 'bold';

    row.appendChild(td1);
    row.appendChild(td2);
    row.appendChild(td3);
    row.appendChild(td4);

    table.appendChild(row);

    var xhttpp = new XMLHttpRequest();
    xhttpp.open("PUT", "./filter/reset", true);
    xhttpp.send('')
}

function insert() {
    var attribute = document.getElementById("attribute").value;
    var value = document.getElementById("tag").value.toLowerCase();
    var dateStart = document.getElementById("start").value;
    var dateEnd = document.getElementById("end").value;

    console.log(value);

    if (attribute.indexOf("Date") !== -1) {
        var xhttpp = new XMLHttpRequest();
        var dateStartLong = new Date(dateStart).getTime();
        var dateEndLong = new Date(dateEnd).getTime();
        if (!dateStartLong) {
            dateStartLong = 0;
        }
        if (!dateEndLong) {
            dateEndLong = 8640000000000000;
        }
        console.log("ist ein Date");
        xhttpp.open("PUT", "./input/date/" + attribute + "/" + dateStartLong + "/to/" + dateEndLong, true);
        xhttpp.send('')
    } else {
        var xhttp = new XMLHttpRequest();
        xhttp.open("PUT", "./input/string/" + attribute + "/" + value.replace('.', '__'), true);
        xhttp.send('')
    }

    var table = document.getElementById("attributeTable");

    var row = document.createElement("tr");
    console.log(row);
    var td1 = document.createElement("td");
    var td2 = document.createElement("td");
    var td3 = document.createElement("td");
    var td4 = document.createElement("td");

    td1.innerHTML = document.getElementById("attribute").value;
    td2.innerHTML = document.getElementById("tag").value;
    td3.innerHTML = document.getElementById("start").value;
    td4.innerHTML = document.getElementById("end").value;
    //console.info(attribute);

    td1.innerHTML = attribute;
    td2.innerHTML = value;
    td3.innerHTML = dateStart;
    td4.innerHTML = dateEnd;

    row.appendChild(td1);
    row.appendChild(td2);
    row.appendChild(td3);
    row.appendChild(td4);

    table.children[0].appendChild(row);

}

function getStats() {
        var xhtttpp = new XMLHttpRequest();
            xhtttpp.open("GET","./stats/aggrcourts?attribute=olgs", true);
            xhtttpp.setRequestHeader("Content-type", "application/json");
            xhtttpp.send('')

            var response = xhtttpp.responseText;
            var parsedresponse = JSON.parse(response);
            console.log(parsedresponse);
            console.log(parsedresponse.labels);
            console.log(parsedresponse.labels[0]);
            console.log("Hello world!");

}

/**
 * Hides the not needed options on the filter site for the given category
 * "tag" hided for the date categorys
 * "start" and "end" hided for the text categorys
 */
function hideOptions() {
    var attribute = document.getElementById("attribute").value;
    //Ist es Datum? - Wann ja: if - sonst else
    if (attribute.indexOf("Date") !== -1) {
        document.getElementById("tag").value = '';
        document.getElementById("tag").style.display = 'none';
        document.getElementById("start").style.display = 'inline';
        document.getElementById("end").style.display = 'inline';
    }
    else {
        document.getElementById("start").value = '';
        document.getElementById("end").value = '';
        document.getElementById("tag").style.display = 'inline';
        document.getElementById("start").style.display = 'none';
        document.getElementById("end").style.display = 'none';
    }
}


(function ($) {

    var settings = {

        // Carousels
        carousels: {
            speed: 4,
            fadeIn: true,
            fadeDelay: 250
        }

    };

    skel.breakpoints({
        wide: '(max-width: 1680px)',
        normal: '(max-width: 1280px)',
        narrow: '(max-width: 960px)',
        narrower: '(max-width: 840px)',
        mobile: '(max-width: 736px)'
    });

    $(function () {

        var $window = $(window),
            $body = $('body');

        // Disable animations/transitions until the page has loaded.
        $body.addClass('is-loading');

        $window.on('load', function () {
            $body.removeClass('is-loading');
        });

        // CSS polyfills (IE<9).
        if (skel.vars.IEVersion < 9)
            $(':last-child').addClass('last-child');

        // Fix: Placeholder polyfill.
        $('form').placeholder();

        // Prioritize "important" elements on mobile.
        skel.on('+mobile -mobile', function () {
            $.prioritize(
                '.important\\28 mobile\\29',
                skel.breakpoint('mobile').active
            );
        });

        // Dropdowns.
        $('#nav > ul').dropotron({
            mode: 'fade',
            speed: 350,
            noOpenerFade: true,
            alignment: 'center'
        });

        // Scrolly links.
        $('.scrolly').scrolly();

        // Off-Canvas Navigation.

        // Navigation Button.
        $(
            '<div id="navButton">' +
            '<a href="#navPanel" class="toggle"></a>' +
            '</div>'
        )
            .appendTo($body);

        // Navigation Panel.
        $(
            '<div id="navPanel">' +
            '<nav>' +
            $('#nav').navList() +
            '</nav>' +
            '</div>'
        )
            .appendTo($body)
            .panel({
                delay: 500,
                hideOnClick: true,
                hideOnSwipe: true,
                resetScroll: true,
                resetForms: true,
                target: $body,
                visibleClass: 'navPanel-visible'
            });

        // Fix: Remove navPanel transitions on WP<10 (poor/buggy performance).
        if (skel.vars.os == 'wp' && skel.vars.osVersion < 10)
            $('#navButton, #navPanel, #page-wrapper')
                .css('transition', 'none');

        // Carousels.
        $('.carousel').each(function () {

            var $t = $(this),
                $forward = $('<span class="forward"></span>'),
                $backward = $('<span class="backward"></span>'),
                $reel = $t.children('.reel'),
                $items = $reel.children('article');

            var pos = 0,
                leftLimit,
                rightLimit,
                itemWidth,
                reelWidth,
                timerId;

            // Items.
            if (settings.carousels.fadeIn) {

                $items.addClass('loading');

                $t.onVisible(function () {
                    var timerId,
                        limit = $items.length - Math.ceil($window.width() / itemWidth);

                    timerId = window.setInterval(function () {
                        var x = $items.filter('.loading'), xf = x.first();

                        if (x.length <= limit) {

                            window.clearInterval(timerId);
                            $items.removeClass('loading');
                            return;

                        }

                        if (skel.vars.IEVersion < 10) {

                            xf.fadeTo(750, 1.0);
                            window.setTimeout(function () {
                                xf.removeClass('loading');
                            }, 50);

                        }
                        else
                            xf.removeClass('loading');

                    }, settings.carousels.fadeDelay);
                }, 50);
            }

            // Main.
            $t._update = function () {
                pos = 0;
                rightLimit = (-1 * reelWidth) + $window.width();
                leftLimit = 0;
                $t._updatePos();
            };

            if (skel.vars.IEVersion < 9)
                $t._updatePos = function () {
                    $reel.css('left', pos);
                };
            else
                $t._updatePos = function () {
                    $reel.css('transform', 'translate(' + pos + 'px, 0)');
                };

            // Forward.
            $forward
                .appendTo($t)
                .hide()
                .mouseenter(function (e) {
                    timerId = window.setInterval(function () {
                        pos -= settings.carousels.speed;

                        if (pos <= rightLimit) {
                            window.clearInterval(timerId);
                            pos = rightLimit;
                        }

                        $t._updatePos();
                    }, 10);
                })
                .mouseleave(function (e) {
                    window.clearInterval(timerId);
                });

            // Backward.
            $backward
                .appendTo($t)
                .hide()
                .mouseenter(function (e) {
                    timerId = window.setInterval(function () {
                        pos += settings.carousels.speed;

                        if (pos >= leftLimit) {

                            window.clearInterval(timerId);
                            pos = leftLimit;

                        }

                        $t._updatePos();
                    }, 10);
                })
                .mouseleave(function (e) {
                    window.clearInterval(timerId);
                });

            // Init.
            $window.load(function () {

                reelWidth = $reel[0].scrollWidth;

                skel.on('change', function () {

                    if (skel.vars.mobile) {

                        $reel
                            .css('overflow-y', 'hidden')
                            .css('overflow-x', 'scroll')
                            .scrollLeft(0);
                        $forward.hide();
                        $backward.hide();

                    }
                    else {

                        $reel
                            .css('overflow', 'visible')
                            .scrollLeft(0);
                        $forward.show();
                        $backward.show();

                    }

                    $t._update();

                });

                $window.resize(function () {
                    reelWidth = $reel[0].scrollWidth;
                    $t._update();
                }).trigger('resize');

            });

        });

    });

})(jQuery);
