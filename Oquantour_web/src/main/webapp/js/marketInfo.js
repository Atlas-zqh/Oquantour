$(document).ready(function () {
    $.ajax({
        url: "getTopListInfo.action",
        dataType: 'json',
        type: 'GET',
        success: function (data) {
            var jsonObj = eval('(' + data + ')');
            $("#date").html(jsonObj[0]["date"]);
            initialTopList(jsonObj);
        }
    })
});

function goToStockInfo(stockID) {
    console.log(stockID)
    window.open("../jsp/stockInfo.jsp?stockName=" + stockID);
}

function initialTopList(data) {
    for (var i = 0; i < 12; i++) {
        var table = document.getElementById("topRankTable");
        var insertTr = table.insertRow(i + 1);
        insertTr.id = "stockInfo";
        var insertTd = insertTr.insertCell(0);
        insertTd.className = "mdl-data-table__cell--non-numeric";
        insertTd.innerHTML = "<span style='cursor: pointer' onclick='goToStockInfo(this.innerHTML)'>" + data[i]["stockID"] + "</span>";
        var insertTd = insertTr.insertCell(1);
        insertTd.className = "mdl-data-table__cell--non-numeric";
        insertTd.innerHTML = data[i]["stockName"];

        var chg = data[i]["pchange"];
        var insertTd = insertTr.insertCell(2);
        insertTd.className = "mdl-data-table__cell--non-numeric";
        if (chg > 0) {
            insertTd.innerHTML = "+" + data[i]["pchange"].toFixed(2) + "%";
            insertTd.style.color = "#cd8585"
        } else {
            insertTd.innerHTML = data[i]["pchange"].toFixed(2) + "%";
            insertTd.style.color = "#74a57e"
        }

        // var insertTd = insertTr.insertCell(3);
        // insertTd.className = "mdl-data-table__cell--non-numeric";
        // insertTd.innerHTML = data[i]["amount"];
        // var insertTd = insertTr.insertCell(4);
        // insertTd.className = "mdl-data-table__cell--non-numeric";
        // insertTd.innerHTML = data[i]["buy"];
        var insertTd = insertTr.insertCell(3);
        insertTd.className = "mdl-data-table__cell--non-numeric";
        insertTd.innerHTML = (data[i]["bratio"] * 100).toFixed(1) + "%";
        // var insertTd = insertTr.insertCell(6);
        // insertTd.className = "mdl-data-table__cell--non-numeric";
        // insertTd.innerHTML = data[i]["sell"];
        var insertTd = insertTr.insertCell(4);
        insertTd.className = "mdl-data-table__cell--non-numeric";
        insertTd.innerHTML = (data[i]["sratio"] * 100).toFixed(1) + "%";
        var insertTd = insertTr.insertCell(5);
        insertTd.className = "mdl-data-table__cell--non-numeric";
        insertTd.innerHTML = data[i]["reason"];
    }
}