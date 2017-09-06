var stocks = "";
var isBackTesting;
$(document).ready(function () {
    if (isBackTesting == 1) {
        console.log("document!!!");
        $("#indexSelectionMenu")
            .dropdown({
                onChange: function (value, text, $selectedItem) {
                    // custom action
                    addIndex_StockTable(value);
                }
            });
    }
});

function initial() {
    // document.getElementById("indexSelectionMenu")
    $("#indexSelectionMenu")
        .dropdown({
            onChange: function (value, text, $selectedItem) {
                // custom action
                addIndex_StockTable(value);
            }
        });

    console.log("initial!!!")
}

function addIndex_StockTable(value) {
    var table = document.getElementById("selectStockTable");
    var rows = table.rows;
    for (var i = 1; i < rows.length; i++) {
        //$("#combineTable tr:eq(" + i + ") td:eq(0)").css("background-color", "#000");
        //console.log($("#combineTable tr:eq(" + i + ") td:eq(1)").html());
        if ($("#selectStockTable tr:eq(" + i + ") td:eq(0)").html() == value) {
            fail_prompt("已存在该因子！", 2000);
            return;
        }
    }

    var insertTr = document.getElementById("selectStockTable").insertRow(1);
    var insertTd = insertTr.insertCell(0);
    insertTd.className = "mdl-data-table__cell--non-numeric";
    insertTd.innerHTML = value;
    var insertTd = insertTr.insertCell(1);
    insertTd.innerHTML = "<select class='ui dropdown' onchange='change_stockTable(this)'> <option value='0'>大于</option> <option value='1'>小于</option> <option value='2'>区间</option> </select>";
    var insertTd = insertTr.insertCell(2);
    insertTd.innerHTML = "<div class='ui form'> <div class='field'> <input type='text'> <span style='color: #7a7a7a;font-size: 16px'>%</span> </div> </div> ";
    var insertTd = insertTr.insertCell(3);
    insertTd.innerHTML = '<img src="../css/pic/delete.png" class="deleteButton" width="25" onclick="deleteIndex(this)">'
    setDropdown(table.rows.length - 1);
}

function change_stockTable(cell) {
//        $(cell).parent().parent().parent().css("background-color","#000");

    var index = $(cell).parent().parent().parent().index();
    if ($(cell).val() == 2) {
        $("#selectStockTable tr:eq(" + index + ") td:eq(2)").html("<div class='ui form me'> <input type='text' class='input1' style='width: 120px;'> <span style='color: #7a7a7a;font-size: 16px'>%～</span> <input type='text' class='input2' style='width: 120px;'> <span style='color: #7a7a7a;font-size: 16px'>%</span> </div> </div> ");
    } else {
        $("#selectStockTable tr:eq(" + index + ") td:eq(2)").html("<div class='ui form'> <div class='field'> <input type='text'> <span style='color: #7a7a7a;font-size: 16px'>%</span> </div> </div> ");
    }
}

function setDropdown(index) {
    $('.ui.dropdown:not(#indexSelectionMenu):not(#buySelectionMenu)').dropdown({
//                onChange: function (value, text, $selectedItem) {
//                    // custom action
//                    console.log(index);
////                    $(cell).parent().css("background-color","#000");
////                    var rowIndex = $(cell).parent().index();
////                    console.log(rowIndex);
//                }
    })
    ;
}

function openInfo() {
    window.open("/jsp/indexInfo.jsp")
}

function deleteIndex(cell) {
    var rowIndex = $(cell).parent().parent().index();
    //console.log(rowIndex);
    var table = document.getElementById("selectStockTable");
    table.deleteRow(rowIndex);
}

function getSecurity() {
    $("#selectingDiv").show();
    var table = document.getElementById("selectStockTable");
    var rows = table.rows;

    var indexMap = new Object();
    var indexName = "";
    var indexVal = "";
    for (var i = 1; i < rows.length; i++) {
//            var array = [];
//            console.log($("#selectStockTable tr:eq(" + i + ") td:eq(1) select").val());

        indexName += $("#selectStockTable tr:eq(" + i + ") td:eq(0)").html() + ";";


        if ($("#selectStockTable tr:eq(" + i + ") td:eq(1) select").val() == 0) {
//                array.push(Number.MIN_VALUE);
            var number = $("#selectStockTable tr:eq(" + i + ") td:eq(2) input").val();
            if (number == "") {
                fail_prompt("请填写正确的区间！");
                return
            }

            indexVal += parseFloat(number) + ";";
            indexVal += Number.MAX_VALUE + ";";
        } else if ($("#selectStockTable tr:eq(" + i + ") td:eq(1) select").val() == 1) {
            var number = $("#selectStockTable tr:eq(" + i + ") td:eq(2) input").val();
            if (number == "") {
                fail_prompt("请填写正确的区间！");
                return
            }

            indexVal += -Number.MAX_VALUE + ";";
            indexVal += parseFloat(number) + ";";
        } else if ($("#selectStockTable tr:eq(" + i + ") td:eq(1) select").val() == 2) {
            var number1 = $("#selectStockTable tr:eq(" + i + ") td:eq(2) .input1").val();
            var number2 = $("#selectStockTable tr:eq(" + i + ") td:eq(2) .input2").val();
            if (number1 == "" || number2 == "" || parseFloat(number1) >= parseFloat(number2)) {
                fail_prompt("请填写正确的区间！");
                return
            }

            indexVal += parseFloat(number1) + ";";
            indexVal += parseFloat(number2) + ";";
        }
//            indexMap[$("#selectStockTable tr:eq(" + i + ") td:eq(0)").html()] = array;
    }

    console.log(indexName);
    console.log(indexVal);

    $.ajax({
        url: "selectStock.action",
        dataType: 'json',
        type: 'POST',
        data: {
            indexName: indexName,
            indexVal: indexVal
        },
        success: function (data) {
            var jsonObj = eval('(' + data + ')');
            var stockNum = jsonObj["stockID"].length;
            console.log(stockNum);
            if (isBackTesting == 1) {
                if (stockNum < 100) {
                    fail_prompt("筛出股票少于一百只，无法回测，改改参数吧！", 2000);
                    $("#selectingDiv").hide();
                    return
                }
            } else {
                if (stockNum == 0) {
                    fail_prompt("没有符合条件的股票，改改参数吧！", 2000);
                    $("#selectingDiv").hide();
                    return
                }
            }
            for (var i = 0; i < stockNum; i++) {
                stocks += jsonObj["stockID"][i] + ";";
            }
            addFilteredStock(jsonObj);
            $("#stockTotalNum").html(stockNum);
            $("#selectingDiv").hide();
            if (isBackTesting == 1) {
                $(".firstStepDiv").hide();
                $("#hide").show();
            } else {
                $("#tempDiv").hide();
            }
            $("#filteredStockDiv").show();
            $("#filteredStockDiv")[0].scrollIntoView({
                behavior: "smooth"
            })
        }

    })
}

function addFilteredStock(jsonObj) {
    var table = document.getElementById("filteredStockTable");
    var rows = table.rows;
    for (var i = 1; i < rows.length; i++) {
        table.deleteRow(1);
    }
    for (var i = 0; i < jsonObj["stockID"].length; i++) {
        var insertTr = document.getElementById("filteredStockTable").insertRow(rows.length);
        var insertTd = insertTr.insertCell(0);
        insertTd.className = "mdl-data-table__cell--non-numeric";
        insertTd.innerHTML = "<span onclick='goToStockInfo(this.innerHTML)' style='cursor: pointer'>" + jsonObj["stockID"][i] + "</span>";
        var insertTd = insertTr.insertCell(1);
        insertTd.className = "mdl-data-table__cell--non-numeric";
        insertTd.innerHTML = jsonObj["stockName"][i];
    }
}

function goToStockInfo(stockID) {
    window.open("/jsp/stockInfo.jsp?stockName=" + stockID);
}
