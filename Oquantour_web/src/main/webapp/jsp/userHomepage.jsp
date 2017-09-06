<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="navBar.jsp" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>个人主页</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/userHomepage.css">

</head>
<body>

<div align="center">

    <!--用户信息部分-->
    <div id="personalInfoDiv" align="left">
        <!--用户头像-->
        <div id="userAvaDiv">
            <img src="${pageContext.request.contextPath}/css/pic/avatar.png" width="110">
        </div>

        <!--用户资料-->
        <div id="userOtherInfoDiv">
            <div style="display: inline-block;margin-right: 30px">
                <p id="showUserName">

                </p>
            </div>

            <div style="display: inline-block">
                <a href="${pageContext.request.contextPath}/jsp/personalInfo.jsp" class="modifyPersonalInfo">修改资料</a>
            </div>

            <p class="userStock_P">
                || 我的自选股：
                <span class="userStock_P" id="userStockNum">--</span>
                <span>&nbsp;&nbsp;&nbsp;</span>
                || 我的组合：
                <span class="userStock_P" id="userCombineNum">--</span>
            </p>
            <p class="userPhoneNumber_P">
                手机号：
                <span class="userPhoneNumber_P" id="userPhoneNumber"></span>
            </p>

        </div>

    </div>

    <div style="width: 1100px;height: 900px;margin-top: 40px" align="left">
        <!--&lt;!&ndash;用户回测历史部分&ndash;&gt;-->
        <!--<div id="userBackTestHistory">-->

        <!--</div>-->


        <!--用户自选股信息部分-->
        <div class="infoDiv" id="userStockDiv">

            <div style="display: inline-block;zoom: 1">
                <p style="color: #aeaeae;font-weight: normal;font-size: 20px;margin-right: 30px">
                    || 我的自选股
                </p>
            </div>

            <%--<div style="display:inline-block;zoom: 1">--%>
            <%--<button id="userMyStockCombine">--%>
            <%--<img src="${pageContext.request.contextPath}/css/pic/settingIcon.png" width="20"> 我的组合--%>
            <%--</button>--%>
            <%--</div>--%>

            <table class="mdl-data-table mdl-js-data-table mdl-shadow--2dp"
                   id="userStockTable"
                   style="width: 100%;margin-top: 20px;margin-bottom: 20px">
                <thead>
                <tr>
                    <th class="mdl-data-table__cell--non-numeric"
                        style="font-weight: 400;font-size: 16px">
                        股票代码
                    </th>
                    <th class="mdl-data-table__cell--non-numeric"
                        style="font-weight: 400;font-size: 16px">股票名称
                    </th>
                    <th class="mdl-data-table__cell--non-numeric"
                        style="font-weight: 400;font-size: 16px">价格
                    </th>
                    <th class="mdl-data-table__cell--non-numeric"
                        style="font-weight: 400;font-size: 16px">涨跌幅
                    </th>
                    <th style="font-weight: 400;font-size: 16px">操作</th>
                </tr>
                </thead>
                <tbody>
                </tbody>
            </table>

        </div>


        <!--用户组合部分-->
        <div class="infoDiv" id="userCombineDiv">

            <div style="display: inline-block;zoom: 1">
                <p style="color: #aeaeae;font-weight: normal;font-size: 20px;margin-right: 30px">
                    || 我的组合
                </p>
            </div>

            <div style="display:inline-block;zoom: 1;float: right">
                <a href="/jsp/addNewCombine.jsp" id="addCombine">
                    <img src="${pageContext.request.contextPath}/css/pic/add.png" width="20"> 添加组合
                </a>
            </div>

            <div id="noCombineAlert" align="center" style="display: none">
                <div>
                    <p style="color: #aeaeae;font-weight: normal;font-size: 20px;margin: 40px">
                        您还没有创建组合哦！
                    </p>
                </div>
            </div>


            <div>
                <table class="mdl-data-table mdl-js-data-table mdl-shadow--2dp"
                       id="userCombineTable"
                       style="width: 100%;margin-top: 20px;margin-bottom: 20px">
                    <thead>
                    <tr>
                        <th class="mdl-data-table__cell--non-numeric"
                            style="font-weight: 400;font-size: 16px">
                            组合名称
                        </th>
                        <th class="mdl-data-table__cell--non-numeric"
                            style="font-weight: 400;font-size: 16px">
                            修改时间
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    </tbody>
                </table>

            </div>
        </div>


    </div>
</div>

<script>
    $(document).ready(function () {

        var username = getCookie("account");
        console.log("username:" + username);

        $("#showUserName").html(username);
        $("#userPhoneNumber").html(getCookie("phone"));

        $.ajax({
            url: "getAllOptionalStock.action",
            dataType: 'json',
            type: 'get',
            data: {
                account: username
            },
            success: function (data) {
                var jsonObj = eval('(' + data + ')');
                $("#userStockNum").html(jsonObj.length);
                for (var i = 0; i < jsonObj.length; i++) {
                    var insertTr = document.getElementById("userStockTable").insertRow(1);
                    var insertTd = insertTr.insertCell(0);
                    insertTd.className = "mdl-data-table__cell--non-numeric";
                    insertTd.innerHTML = "<span onclick='goToStockInfo(this.innerHTML)' style='cursor: pointer'>" + jsonObj[i]["stockID"] + "</span>";
                    var insertTd = insertTr.insertCell(1);
                    insertTd.className = "mdl-data-table__cell--non-numeric";
                    insertTd.innerHTML = jsonObj[i]["stockName"];
                    var insertTd = insertTr.insertCell(2);
                    insertTd.className = "mdl-data-table__cell--non-numeric";
                    insertTd.innerHTML = jsonObj[i]["stockTrade"];
                    if (jsonObj[i]["stockChange"] > 0) {
                        insertTd.style.color = '#cd8585';
                    } else {
                        insertTd.style.color = '#74a57e';
                    }
                    var insertTd = insertTr.insertCell(3);
                    insertTd.className = "mdl-data-table__cell--non-numeric";
                    insertTd.innerHTML = (jsonObj[i]["stockChange"] * 100).toFixed(2);
                    if (jsonObj[i]["stockChange"] > 0) {
                        insertTd.innerHTML = "+" + (jsonObj[i]["stockChange"] * 100).toFixed(2);
                        insertTd.style.color = '#cd8585';
                    } else {
                        insertTd.innerHTML = (jsonObj[i]["stockChange"] * 100).toFixed(2);
                        insertTd.style.color = '#74a57e';
                    }
                    var insertTd = insertTr.insertCell(4);
                    insertTd.innerHTML = "<button class='removeUserStock' onclick='deleteUserStock(this)'>移除</button>";

                }

            }
        });

        $.ajax({
            url: "getAllPortfolios.action",
            dataType: 'json',
            type: 'get',
            data: {
                username: username,
            },
            success: function (data) {
                var jsonObj = eval('(' + data + ')');
                $("#userCombineNum").html(jsonObj.length);
                for (var i = 0; i < jsonObj.length; i++) {
                    var insertTr = document.getElementById("userCombineTable").insertRow(1);
                    var insertTd = insertTr.insertCell(0);
                    insertTd.className = "mdl-data-table__cell--non-numeric";
                    insertTd.innerHTML = "<span id='combineNameTd' onclick='goCombineAnalysis(this.innerHTML)'>" + jsonObj[i]['combinationName'] + "</span>";
                    var insertTd = insertTr.insertCell(1);
                    insertTd.className = "mdl-data-table__cell--non-numeric";
                    insertTd.innerHTML = jsonObj[i]["combinationTime"];
                }

            }

        });

        $(".removeUserStock").click(function () {
            deleteUserStock(this, 'userStockTable');
        });


    });

    function deleteUserStock(cell) {
        var rowIndex = $(cell).parent().parent().index();
        var table = document.getElementById("userStockTable");
        table.deleteRow(rowIndex);
    }

    function goCombineAnalysis(combineName) {
        combineName = encodeURI(combineName);
        self.location = "${pageContext.request.contextPath}/jsp/combineAnalysis.jsp?name=" + combineName;
    }

    function goToStockInfo(stockID) {
        window.open("../jsp/stockInfo.jsp?stockName=" + stockID);
    }

</script>
</body>
</html>