var getval = "";
var theComponents = [];

var rangeSlider = function () {
    var slider = $('.range-slider'),
        range = $('.range-slider__range'),
        value = $('.range-slider__value');

    //value.html(range.value);

    slider.each(function () {

        value.each(function () {
            var value = $(this).prev().attr('value');
            $(this).html();
        });

        range.on('input', function () {
            $(this).next(value).html(this.value);
        });
    });
};

$(document).ready(function () {
    if (window.localStorage.getItem("allStock") == null) {
        $.ajax({
            url: "getAllStockNameAndCode.action",
            dataType: 'json',
            success: function (data) {
                var jsonObj = eval('(' + data + ')');
                addAllStockToDownList(jsonObj["stockNameAndCode"]);
                window.localStorage.setItem("allStock", list2str(jsonObj["stockNameAndCode"]));
            },
            error: function () {
                fail_prompt("ajax error", 1500);
            }
        });
    } else {
        addAllStockToDownList(str2list(window.localStorage.getItem("allStock")));
    }

    var thisURL = document.URL;
    getval = thisURL.split('?')[1];
    if (getval != null) {
        getval = decodeURI(getval);
        document.getElementById("combineName").value = getval;
        $("#combineName").attr("disabled", "disabled");
        console.log(getval);
    }
});

content = [];
window.allStock = "";

function addAllStockToDownList(data) {
    for (var i = 0; i < data.length; i++) {
        var row = '' + data[i].split(";")[0] + " " + data[i].split(";")[1];
        window.allStock += data[i].split(";")[0] + ";";
        content.push(row);
    }

    $("#search_ui").select2({
        data: content
    });

    $('.select2-selection__rendered').text('添加股票');
}

function list2str(list) {
    var str = "";
    for (var i = 0; i < list.length; i++) {
        str += list[i].split(";")[0] + "," + list[i].split(";")[1] + ";";
    }
    return str;
}

function str2list(str) {
    var list = str.split(";");
    var newList = [];
    for (var i = 0; i < list.length; i++) {
        newList[i] = list[i].split(",")[0] + ";" + list[i].split(",")[1];
    }
    return newList;
}

function addStockToCombine(stock) {
    var table = document.getElementById("combineTable");
    var rows = table.rows;

    for (var i = 1; i < rows.length; i++) {
        //$("#combineTable tr:eq(" + i + ") td:eq(0)").css("background-color", "#000");
        //console.log($("#combineTable tr:eq(" + i + ") td:eq(1)").html());
        if ($("#combineTable tr:eq(" + i + ") td:eq(1)").html() == stock.split(" ")[0]) {
            fail_prompt("已存在该股票！", 2000);
            return;
        }
    }

    var insertTr = document.getElementById("combineTable").insertRow(1);
    var insertTd = insertTr.insertCell(0);
    insertTd.className = "mdl-data-table__cell--non-numeric";
    insertTd.innerHTML = stock.split(" ")[1];
    var insertTd = insertTr.insertCell(1);
    insertTd.className = "mdl-data-table__cell--non-numeric";
    insertTd.innerHTML = stock.split(" ")[0];
    // var insertTd = insertTr.insertCell(2);
    // insertTd.className = "mdl-data-table__cell--non-numeric";
    // insertTd.innerHTML = "行业";
    var insertTd = insertTr.insertCell(2);
    insertTd.innerHTML = "<div class='range-slider'> <input class='range-slider__range' type='range' value='0' min='0' max='100' onchange='checkOverflow(this)'> <span class='range-slider__value'></span> </div>";
    var insertTd = insertTr.insertCell(3);
    insertTd.innerHTML = "<img src='../css/pic/delete.png' class='deleteButton' width='25' onclick='deleteStock(this)'>";
    rangeSlider();
}

function deleteStock(cell) {
    var rowIndex = $(cell).parent().parent().index();
    //console.log(rowIndex);
    var table = document.getElementById("combineTable");
    table.deleteRow(rowIndex);
}

function addCombineStock() {
    if (getval != null) {
        var username = getCookie("account");
        var table = document.getElementById("combineTable");

        $.ajax({
            url: 'getCurrentStocks.action',
            dataType: 'json',
            type: 'GET',
            data: {
                username: username,
                portfolioName: getval
            },
            success: function (data) {
                var jsonObj = eval('(' + data + ')');
                console.log(jsonObj);
                for (var i = 0; i < jsonObj.length; i++) {
                    var insertTr = table.insertRow(1);
                    var insertTd = insertTr.insertCell(0);
                    insertTd.className = "mdl-data-table__cell--non-numeric";
                    insertTd.innerHTML = getStockName(jsonObj[i]["stock"]);
                    var insertTd = insertTr.insertCell(1);
                    insertTd.className = "mdl-data-table__cell--non-numeric";
                    insertTd.innerHTML = jsonObj[i]["stock"];
                    // var insertTd = insertTr.insertCell(2);
                    // insertTd.className = "mdl-data-table__cell--non-numeric";
                    // insertTd.innerHTML = "行业";
                    var position = jsonObj[i]["position"] * 100;
                    console.log("position:" + position);
                    var insertTd = insertTr.insertCell(2);
                    insertTd.innerHTML = "<div class='range-slider'> <input class='range-slider__range' type='range' value='5' min='0' max='100' onchange='checkOverflow(this)'> <span class='range-slider__value'></span> </div>";
                    var insertTd = insertTr.insertCell(3);
                    insertTd.innerHTML = "<img src='../css/pic/delete.png' class='deleteButton' width='25' onclick='deleteStock(this)'>";
                    rangeSlider();
                }
            }
        });
        // console.log("11111");

        // var content = theComponents[theComponents.length - 1];
        // console.log(content);

    }
}

function getStockName(code) {
    var allStock = window.localStorage.getItem("allStock").split(";");
    for (var i = 0; i < allStock.length; i++) {
        if (allStock[i].split(",")[0] == code)
            return allStock[i].split(",")[1];
    }
}

function initialStocks(components) {
    theComponents = components;
    console.log(theComponents);
}

function checkOverflow(theInput) {
    var table = document.getElementById("combineTable");
    var rows = table.rows;
    var total = 0;

    for (var i = 1; i < rows.length; i++) {
//                $("#combineTable tr:eq(" + i + ")").css("background-color", "#000");
//                console.log(parseInt($("#combineTable tr:eq(" + i + ") td:eq(3) .range-slider .range-slider__range").val()));
        total += parseInt($("#combineTable tr:eq(" + i + ") td:eq(2) .range-slider .range-slider__range").val());
        //console.log($('#combineTable td:eq(3) .range-slider .range-slider__range').val());
    }

//            console.log("++++++++++++");
//            console.log(total);

    if (total > 100) {
        fail_prompt("总仓位不能超过100%！");
        var value = $(theInput).prev().attr('value');
        $(theInput).html(0);
        $(theInput).next(value).html(0);
        theInput.value = '0';

    }
}

function confirmCreateCombine() {
    if ($("#combineName").val() == "") {
        fail_prompt("请填写组合名称！", 2000);
    } else if (document.getElementById("combineTable").rows.length - 1 == 0) {
        fail_prompt("请选择组合股票！", 2000);
    } else {
        if (getval == null) {
            var username = getCookie("account");
            var portfolioName = $('#combineName').val();
            var stocks = "";
            var positions = "";

            console.log(username);
            console.log(portfolioName);
            for (var i = 1; i < document.getElementById("combineTable").rows.length; i++) {
                //console.log($("#combineTable tr:eq(" + i + ") td:eq(1)").html() + $("#combineTable tr:eq(" + i + ") td:eq(3) .range-slider .range-slider__range").val() / 100);
                stocks += $("#combineTable tr:eq(" + i + ") td:eq(1)").html() + ";";
                positions += $("#combineTable tr:eq(" + i + ") td:eq(3) .range-slider .range-slider__range").val() / 100 + ";";
            }

            $.ajax({
                url: "addPortfolio.action",
                dataType: 'json',
                type: 'POST',
                data: {
                    username: username,
                    portfolioName: portfolioName,
                    stocks: stocks,
                    positions: positions,
                },
                success: function (data) {
                    var jsonObj = eval('(' + data + ')');
                    if (jsonObj[0]["info"] == "添加组合成功") {
                        success_prompt("创建成功！", 2000);
                        setTimeout('getBack()', 2000);

                    } else {
                        fail_prompt(jsonObj[0]["info"], 2000);
                    }
                },
                error: function () {
                    fail_prompt("ajax error", 2000);
                }
            });
        } else {
            success_prompt("调仓成功！", 2000);
            setTimeout('getBack()', 2000);
        }
    }
}

function getBack() {
    self.location = "/jsp/userHomepage.jsp";
}