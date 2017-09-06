<%--suppress ALL --%>
<%--
  Created by IntelliJ IDEA.
  User: st
  Date: 2017/5/13
  Time: 13:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="navBar.jsp" %>
<html>
<head>
    <title>回测</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/backTest.css">

    <script src="${pageContext.request.contextPath}/js/backtest.js"></script>

    <script>

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

        $(document).ready(function () {
            //$("#list-checkbox-2").prop("checked", true);
            $("#sample3").dropdown();
//            localStorage.clear();

            $("#loadingRandomStockDiv").show();
//            alert(getCookie("allStock"));
            if (window.localStorage.getItem("allStock") == null) {
                $.ajax({
                    url: "getAllStockNameAndCode.action",
                    dataType: 'json',
                    success: function (data) {
                        var jsonObj = eval('(' + data + ')');
                        addAllStockToDownList(jsonObj["stockNameAndCode"]);
                        randomSelection(jsonObj["stockNameAndCode"]);
                        window.localStorage.setItem("allStock", list2str(jsonObj["stockNameAndCode"]));
                    },
                    error: function () {
                        fail_prompt("ajax error", 1500);
                    }
                });

            } else {
                addAllStockToDownList(str2list(window.localStorage.getItem("allStock")));
                randomSelection(str2list(window.localStorage.getItem("allStock")));
            }

            $(".deleteButton").click(function () {
                deleteStock(this);
            });

//            //随机选择200只股票
//            var count = 8;
//            var originalArray = [];//原数组
//            //给原数组originalArray赋值
//            for (var i = 0; i < count; i++) {
//                originalArray[i] = i + 1;
//            }
//
//            originalArray.sort(function () {
//                return 0.5 - Math.random();
//            });
//
//            for (var j = 0; j < 5; j++) {
//                console.log(originalArray[j] + "; ");
//                var index = originalArray[j];
//                $("#selectStockTable tr:eq("+ index +") ").css("background-color", "#ffff00");
////                $("#selectStockTable tr:eq("+ index +") ").className = "mdl-data-table tbody tr.is-selected";
//                $("#selectStockTable tr:eq(1)").find(".mdl-checkbox__input").prop("checked", true);
//            }
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

            $('.select2-selection__rendered').text('请输入股票名称／代码');
        }

        function randomSelection(data) {
            randomList = _.sample(content, 200);
            for (var i = 1; i <= 200; i++) {
                insertTr = document.getElementById("selectStockTable").insertRow(i);
                var insertTd = insertTr.insertCell(0);
                insertTd.className = "mdl-data-table__cell--non-numeric";
                insertTd.innerHTML = randomList[i - 1].split(" ")[0];
                var insertTd = insertTr.insertCell(1);
                insertTd.className = "mdl-data-table__cell--non-numeric";
                insertTd.innerHTML = randomList[i - 1].split(" ")[1];
                var insertTd = insertTr.insertCell(2);
                insertTd.innerHTML = "<img src='${pageContext.request.contextPath}/css/pic/delete.png' class='deleteButton' width='25' onclick='deleteStock(this)'>";
            }
            $("#loadingRandomStockDiv").hide();
            $("#selectedStockNum_self").html(document.getElementById("selectStockTable").rows.length - 1);

        }

        function addStock(stock) {
            insertTr = document.getElementById("selectStockTable").insertRow(1);
            var insertTd = insertTr.insertCell(0);
            insertTd.className = "mdl-data-table__cell--non-numeric";
            insertTd.innerHTML = stock.split(" ")[0];
            var insertTd = insertTr.insertCell(1);
            insertTd.className = "mdl-data-table__cell--non-numeric";
            insertTd.innerHTML = stock.split(" ")[1];
            var insertTd = insertTr.insertCell(2);
            insertTd.innerHTML = "<img src='${pageContext.request.contextPath}/css/pic/delete.png' class='deleteButton' width='25' onclick='deleteStock(this)'>";
            $("#selectedStockNum_self").html(document.getElementById("selectStockTable").rows.length - 1);
        }

        function backTest() {
            var stock = "";
            var startDate = $('#datePicker_start').val().split(" ")[0];
            var endDate = $('#datePicker_start').val().split(" ")[2];
            var strategyType = $('#strategySelected').html();
            var formativePeriod = 0;
            var holdingPeriod = 0;
            var ma_length = 0;
            var maxholdingStocks = 0;
            if (strategyType == "动量策略") {
                formativePeriod = $("#sample1").val();
                holdingPeriod = $("#sample2").val();
                if (formativePeriod == "" || holdingPeriod == "") {
                    fail_prompt("请输入形成期和持有期！", 2000);
                    return
                }
            } else if (strategyType == "均值回归") {
                ma_length = $("#sample3").val();
                console.log(ma_length)
                holdingPeriod = $("#sample4").val();
                maxholdingStocks = $("#sample5").val();
                formativePeriod = ma_length;
                if (holdingPeriod == "" || maxholdingStocks == "") {
                    fail_prompt("请输入持有期和最大持仓股票！", 2000);
                    return
                }
            } else if (strategyType == "小市值轮动策略") {
                formativePeriod = $("#sample6").val();
                holdingPeriod = $("#sample7").val();
                maxholdingStocks = $("#sample8").val();
                if (holdingPeriod == "" || maxholdingStocks == "" || formativePeriod == "") {
                    fail_prompt("请输入持有期和最大持仓股票和形成期！", 2000);
                    return
                }
            } else {
                fail_prompt("请选择一个策略！", 2000);
                return
            }
            var filter_Suspension = $("#filter_Suspension").prop('checked');
            var filter_NoData = $("#filter_NoData").prop('checked');
            var filter_ST = $("#filter_ST").prop('checked');

            $("#loadingDiv").show();
            $("#loadingDiv")[0].scrollIntoView({
                behavior: "smooth"
            })

            if ($("#list-checkbox-0").prop('checked')) {
                var s = window.allStock.split(";");
                var t = "";
                for (var i = 0; i < 120; i++) {
                    t += s[i] + ";";
                }
                submit(t);
            } else if ($("#list-checkbox-1").prop('checked') || $("#list-checkbox-2").prop('checked') || $("#list-checkbox-3").prop('checked')) {
                var plateName;
                if ($("#list-checkbox-1").prop('checked')) {
                    plateName = "深市A股";
                } else if ($("#list-checkbox-2").prop('checked')) {
                    plateName = "创业板";
                } else if ($("#list-checkbox-3").prop('checked')) {
                    plateName = "中小板";
                }
                $.ajax({
                    url: "getStockByPlate.action",
                    data: {
                        plateName: plateName
                    },
                    dataType: 'json',
                    success: function (data) {
                        var jsonObj = eval('(' + data + ')');

                        for (var i = 0; i < jsonObj["stockInPlate"].length; i++) {
                            stock += jsonObj["stockInPlate"][i].split(";")[0] + ";";
                        }
                        submit(stock);
                    },
                    error: function () {
                        fail_prompt("ajax error", 1500);
                    }
                });
            } else {
                for (var i = 0; i < randomList.length; i++) {
                    stock += randomList[i].split(" ")[0] + ";";
                }
                submit(stock);
            }

            function submit(stock) {
                $.ajax({
                    url: "backTest.action",
                    dataType: 'json',
                    type: "POST",
                    data: {
                        startDate: startDate, // 回测结束日期
                        endDate: endDate,
                        // 要回测的多只股票代码
                        stocks: stock,
                        // 形成期
                        formativePeriod: formativePeriod,
                        // 持有期
                        holdingPeriod: holdingPeriod,
                        // 几日均线
                        ma_length: ma_length,
                        // 策略类型
                        strategyType: strategyType,
                        // 最大持仓股票数目
                        maxholdingStocks: maxholdingStocks,
                        // 过滤ST股票
                        filter_ST: filter_ST,
                        // 过滤无数据股票
                        filter_NoData: filter_NoData,
                        // 过滤停牌股票
                        filter_Suspension: filter_Suspension,
                        // 是否忽略100只约束
                        ignore_100: false,
                        flag: 0
                    }
                    ,
                    success: function (data) {
                        var jsonObj = eval('(' + data + ')');
                        if (jsonObj[0]["info"] == "回测成功") {
                            drawBackTestResultChart(jsonObj, strategyType);
                        } else {
                            fail_prompt(jsonObj[0]["info"], 1500);
                        }
                        $("#loadingDiv").hide();
                    }
                    ,
                    error: function () {
                        fail_prompt("ajax error", 1500);
                    }
                });
            }
        }


        ;

        function deleteStock(cell) {
            var rowIndex = $(cell).parent().parent().index();
            //console.log(rowIndex);
            var table = document.getElementById("selectStockTable");
            table.deleteRow(rowIndex);
            $("#selectedStockNum_self").html(document.getElementById("selectStockTable").rows.length - 1);
        }
    </script>
    <%--<script--%>
    <%--src="https://code.jquery.com/jquery-3.1.1.min.js"></script>--%>


</head>


<body>
<div class="alert"></div>
<div>
    <div align="center">
        <div class="searchFilter" align="left">
            <!-- List with avatar and controls -->

            <div style="display:inline-block;">
                <p style="font-size: 25px;font-weight: normal;color: #7c7c7c">
                    Oquantour 策略选择器
                </p>
            </div>

            <div id="goDIY" style="display:inline-block;margin-left: 20px">
                <a href="${pageContext.request.contextPath}/jsp/DIYStrategy.jsp"
                   style="font-size: 20px;font-weight: normal;color: #b1898c;text-decoration: underline">
                    策略DIY
                </a>
            </div>
            <div class="mdl-tooltip" id="toolTip1" for="goDIY">
                自定义多因子参数调优
                <br>
                发现更好的策略
            </div>

            <!--第一步部分-->
            <div style="margin-top: 60px">
                <p style="font-size: 18px;font-weight: 300;color: #787878"><i>第 <img
                        src="${pageContext.request.contextPath}/css/pic/1.png" width="40"
                        align="middle"> 步：选择股票池</i></p>
            </div>


            <div>
                <ul id="myTab" class="nav nav-tabs">
                    <li class="active">
                        <a href="#home" data-toggle="tab" id="plateSelectionTab">
                            板块回测
                        </a>
                    </li>
                    <div class="mdl-tooltip" id="toolTip1" for="plateSelectionTab">
                        板块和自选股只可二选一
                    </div>
                    <li><a href="#ios" data-toggle="tab" id="selfSelectionTab">自选股回测</a></li>
                    <!-- Multiline Tooltip -->
                    <div class="mdl-tooltip" id="toolTip2" for="selfSelectionTab">
                        板块和自选股只可二选一
                    </div>
                </ul>

                <div id="myTabContent" class="tab-content">
                    <div class="tab-pane fade in active" id="home">
                        <div>
                            <%--<p style="font-size: 18px;font-weight: 300;color: #929292">--%>
                            <%--* 已选择 <span class="stockTotalNum" id="selectedStockNum_plate" style="color: #D1A5A5"><i>0</i></span>--%>
                            <%--只股票--%>
                            <%--</p>--%>
                        </div>
                        <table style="margin-top: 30px" id="plateSelection">
                            <tr>
                                <td>
                                    <label class="mdl-checkbox mdl-js-checkbox mdl-js-ripple-effect"
                                           for="list-checkbox-0">
                                        <input type="checkbox" id="list-checkbox-0" class="mdl-checkbox__input"
                                               onclick="checkSelected()"/>
                                    </label>
                                </td>
                                <td>
                                    &nbsp;&nbsp;
                                </td>
                                <td class="selectStock">
                                    <!--<i class="material-icons  mdl-list__item-avatar">person</i>-->
                                    全选
                                </td>
                                <td>
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                </td>
                                <td>
                                    <label class="mdl-checkbox mdl-js-checkbox mdl-js-ripple-effect"
                                           for="list-checkbox-1">
                                        <input type="checkbox" id="list-checkbox-1" class="mdl-checkbox__input"
                                               onclick="checkSelected()"/>
                                    </label>
                                </td>
                                <td>
                                    &nbsp;&nbsp;
                                </td>
                                <td class="selectStock">
                                    <!--<i class="material-icons  mdl-list__item-avatar">person</i>-->
                                    深市A股
                                </td>
                                <td>
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                </td>
                                <td>
                                    <label class="mdl-checkbox mdl-js-checkbox mdl-js-ripple-effect"
                                           for="list-checkbox-2">
                                        <input type="checkbox" id="list-checkbox-2" class="mdl-checkbox__input"
                                               onclick="checkSelected()"/>
                                    </label>
                                </td>
                                <td>
                                    &nbsp;&nbsp;
                                </td>
                                <td class="selectStock">
                                    <!--<i class="material-icons  mdl-list__item-avatar">person</i>-->
                                    创业板
                                </td>
                                <td>
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                </td>
                                <td>
                                    <label class="mdl-checkbox mdl-js-checkbox mdl-js-ripple-effect"
                                           for="list-checkbox-3">
                                        <input type="checkbox" id="list-checkbox-3" class="mdl-checkbox__input"
                                               onclick="checkSelected()"/>
                                    </label>
                                </td>
                                <td>
                                    &nbsp;&nbsp;
                                </td>
                                <td class="selectStock">
                                    <!--<i class="material-icons  mdl-list__item-avatar">person</i>-->
                                    中小板
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="tab-pane fade" id="ios">

                        <div style="display: inline-block;zoom: 1;margin-right: 400px">
                            <p style="font-size: 18px;font-weight: 300;color: #929292">
                                * 已选择 <span class="stockTotalNum" id="selectedStockNum_self"
                                            style="color: #D1A5A5"><i>0</i></span>
                                只股票
                            </p>
                        </div>

                        <div style="display: inline-block;zoom: 1">

                            <span style="font-size: 18px;font-weight: 300;color: #7a7a7a;vertical-align: middle">添加股票：&nbsp;&nbsp;</span>

                            <select id="search_ui" style="width:200px;outline: none"
                                    onchange="addStock(this.value)">
                                <!-- Dropdown List Option -->
                            </select>
                        </div>


                        <p style="font-size: 12px;color: #afafaf;font-weight: 300">*已为您随机选择了200只股票 请选择不少于100只股票</p>

                        <button type="button" class="btn btn-primary" data-toggle="collapse"
                                data-target="#selectedStocks">
                            查看已选股票
                        </button>


                        <div id="selectedStocks" class="collapse" style="margin-top: 30px" align="right">

                            <div style="height: 400px;overflow: auto">

                                <div id="loadingRandomStockDiv" align="center"
                                     style="margin-top: 20px;margin-bottom: 20px;display: none">
                                    <!-- MDL Progress Bar with Indeterminate Progress -->
                                    <p style="font-weight: 300;font-size: 18px;color: #9b9b9b;">
                                        正在添加随机股票......
                                    </p>
                                    <div class="mdl-progress mdl-js-progress mdl-progress__indeterminate"
                                    ></div>
                                </div>


                                <table class="mdl-data-table mdl-js-data-table mdl-shadow--2dp"
                                       id="selectStockTable"
                                       style="width: 100%;margin-top: 20px;margin-bottom: 20px">
                                    <thead>
                                    <tr>
                                        <th class="mdl-data-table__cell--non-numeric"
                                            style="font-weight: 400;font-size: 16px">
                                            名称
                                        </th>
                                        <th class="mdl-data-table__cell--non-numeric"
                                            style="font-weight: 400;font-size: 16px">代码
                                        </th>
                                        <th style="font-weight: 400;font-size: 16px">操作</th>
                                    </tr>
                                    </thead>
                                    <tbody></tbody>
                                </table>
                            </div>
                        </div>

                    </div>
                </div>
            </div>


            <script>
                function checkSelected() {
                    if ($("#list-checkbox-0").prop("checked")) {
                        $("#list-checkbox-1").attr("disabled", "disabled");
                        $("#list-checkbox-2").attr("disabled", "disabled");
                        $("#list-checkbox-3").attr("disabled", "disabled");
                    } else {
                        $("#list-checkbox-1").attr("disabled", false);
                        $("#list-checkbox-2").attr("disabled", false);
                        $("#list-checkbox-3").attr("disabled", false);
                    }

                    if ($("#list-checkbox-1").prop("checked") || $("#list-checkbox-2").prop("checked") || $("#list-checkbox-3").prop("checked")) {
                        $("#list-checkbox-0").attr("disabled", "disabled");
                    } else {
                        $("#list-checkbox-0").attr("disabled", false);
                    }

                    if ($("#list-checkbox-0").prop("checked") || $("#list-checkbox-1").prop("checked") || $("#list-checkbox-2").prop("checked") || $("#list-checkbox-3").prop("checked")) {
//                            alert("6666");
                        $("#selfSelectionTab").css("pointer-events", "none");
                        $("#selfSelectionTab").css("cursor", "default");
                    } else {
                        $("#selfSelectionTab").css("pointer-events", "auto");
                        $("#selfSelectionTab").css("cursor", "pointer");
                    }
                }
            </script>

            <!--第二步部分-->
            <script src="${pageContext.request.contextPath}/js/flatpickr.js"></script>

            <div style="margin-top: 80px">
                <p style="font-size: 18px;font-weight: 300;color: #787878"><i>第 <img
                        src="${pageContext.request.contextPath}/css/pic/2.png" width="40"
                        align="middle"> 步：选择回测时间</i></p>

                <div style="margin-top: 30px;border: solid 1px #f1f1f1;padding: 20px">
                    <div style="display: inline-block;zoom: 1.2">
                        <img src="${pageContext.request.contextPath}/css/pic/calendar.png" width="25">

                        <input id="datePicker_start" type="text" placeholder="开始日期"
                               style="font-size: 14px;width: 300px;border:none;
                               border-bottom:solid 1px #f1f1f1;padding-left: 20px">

                    </div>

                </div>

                <script>
                    $("#datePicker_start").flatpickr({
                        mode: "range",
                        dateFormat: "Y-m-d",
                        minDate: "2010-01-01",
                        maxDate: "today",
                        defaultDate: ["2017-01-01", "2017-03-01"]
                    });
                </script>

            </div>

            <!--第三步部分-->
            <div style="margin-top: 80px">
                <p style="font-size: 18px;font-weight: 300;color: #787878"><i>第 <img
                        src="${pageContext.request.contextPath}/css/pic/3.png" width="40"
                        align="middle"> 步：选择回测策略和回测条件</i>
                </p>
                <div style="margin-top: 30px;border: solid 1px #f1f1f1;padding: 20px">

                    <div style="padding-left: 22px;">
                        <table style="margin-bottom: 20px" id="backTestRestriction">
                            <tr>
                                <td>
                                    <label class="mdl-checkbox mdl-js-checkbox mdl-js-ripple-effect"
                                           for="filter_Suspension">
                                        <input type="checkbox" id="filter_Suspension" class="mdl-checkbox__input"/>
                                    </label>
                                </td>
                                <td>
                                    &nbsp;&nbsp;
                                </td>
                                <td class="selectStock">
                                    <!--<i class="material-icons  mdl-list__item-avatar">person</i>-->
                                    过滤停牌股
                                </td>
                                <td>
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                </td>

                                <td>
                                    <label class="mdl-checkbox mdl-js-checkbox mdl-js-ripple-effect"
                                           for="filter_NoData">
                                        <input type="checkbox" id="filter_NoData" class="mdl-checkbox__input"/>
                                    </label>
                                </td>
                                <td>
                                    &nbsp;&nbsp;
                                </td>
                                <td class="selectStock">
                                    <!--<i class="material-icons  mdl-list__item-avatar">person</i>-->
                                    过滤数据缺失股
                                </td>
                                <td>
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                </td>
                                <td>
                                    <label class="mdl-checkbox mdl-js-checkbox mdl-js-ripple-effect"
                                           for="filter_ST">
                                        <input type="checkbox" id="filter_ST" class="mdl-checkbox__input"/>
                                    </label>
                                </td>
                                <td>
                                    &nbsp;&nbsp;
                                </td>
                                <td class="selectStock">
                                    <!--<i class="material-icons  mdl-list__item-avatar">person</i>-->
                                    过滤ST股
                                </td>
                            </tr>
                        </table>
                    </div>

                    <!--下拉菜单部分-->
                    <div style="display: inline-block;zoom: 1">
                        <!-- Left aligned menu below button -->
                        <ul class="nav navbar-nav" style="display: inline-block">

                            <li class="dropdown">
                                <a href="#" class="dropdown-toggle" data-toggle="dropdown" id="strategySelected">
                                    回测策略
                                    <b class="caret"></b>
                                </a>
                                <ul class="dropdown-menu">
                                    <li>
                                        <button id="momentumSelected">动量策略</button>
                                    </li>
                                    <li>
                                        <button id="meanReversionSelected">均值回归</button>
                                    </li>
                                    <li>
                                        <button id="smallMarketWheeledStrategySelected">小市值轮动策略</button>
                                    </li>
                                    <li class="divider"></li>
                                    <li>
                                        <button onclick="goToDIY()">去原创</button>
                                    </li>
                                </ul>
                            </li>
                        </ul>
                    </div>

                    <div id="strategyInfo" style="display: inline-block;zoom: 1" align="center">
                        <img src="${pageContext.request.contextPath}/css/pic/question.png" width="25"
                             style="vertical-align: baseline">
                    </div>
                    <!-- Multiline Tooltip -->
                    <div class="mdl-tooltip" for="strategyInfo">
                        <span id="strategyInfoHead">什么是回测？</span>
                        <br>
                        <span id="strategyInfoContent">回测涉及到基于历史数据模拟实施某项交易策略。</span>
                    </div>

                    <!--动量策略和其他策略部分，要求填写形成期和持有期-->
                    <div id="momentum_OtherDiv" style="display: none;zoom: 1;vertical-align: super">
                        <!-- Numeric Textfield -->
                        <div style="display: inline-block;zoom: 1">
                            <form action="#" style="margin-left: 100px;margin-right: 100px">
                                <div class="mdl-textfield mdl-js-textfield">
                                    <input class="mdl-textfield__input" type="text" pattern="^[1-9]{1}[\d]*$"
                                           id="sample1">
                                    <label class="mdl-textfield__label" for="sample1">形成期</label>
                                    <span class="mdl-textfield__error">请输入大于0的整数！</span>
                                </div>
                            </form>
                        </div>

                        <div style="display: inline-block;zoom: 1">
                            <form action="#">
                                <div class="mdl-textfield mdl-js-textfield">
                                    <input class="mdl-textfield__input" type="text" pattern="^[1-9]{1}[\d]*$"
                                           id="sample2">
                                    <label class="mdl-textfield__label" for="sample2">持有期</label>
                                    <span class="mdl-textfield__error">请输入大于0的整数！</span>
                                </div>
                            </form>
                        </div>
                    </div>

                    <!--均值回归部分，要求填写几日均线、持有期和最大持仓股-->
                    <div id="meanReversionDiv" style="display: none;zoom: 1;vertical-align: super">
                        <div style="display: inline-block;zoom: 1;margin-right: 50px">
                            <!-- Left aligned menu below button -->
                            <select class='ui dropdown' id="sample3">
                                <option value='5'>5日均线</option>
                                <option value='10'>10日均线</option>
                                <option value='20'>20日均线</option>
                                <option value='30'>30日均线</option>
                                <option value='60'>60日均线</option>
                                <option value='120'>120日均线</option>
                                <option value='240'>240日均线</option>
                            </select>
                        </div>

                        <div style="display: inline-block;zoom: 1;margin-right: 50px">
                            <form action="#">
                                <div class="mdl-textfield mdl-js-textfield">
                                    <input class="mdl-textfield__input" type="text" pattern="^[1-9]{1}[\d]*$"
                                           id="sample4">
                                    <label class="mdl-textfield__label" for="sample4">持有期</label>
                                    <span class="mdl-textfield__error">请输入大于0的整数！</span>
                                </div>
                            </form>
                        </div>

                        <div style="display: inline-block;zoom: 1;margin-right: 50px">
                            <form action="#">
                                <div class="mdl-textfield mdl-js-textfield">
                                    <input class="mdl-textfield__input" type="text" pattern="^[1-9]{1}[\d]*$"
                                           id="sample5">
                                    <label class="mdl-textfield__label" for="sample5">最大持仓股</label>
                                    <span class="mdl-textfield__error">请输入大于0的整数！</span>
                                </div>
                            </form>
                        </div>
                    </div>

                    <!--小市值轮动策略部分，要求填写形成期、持有期和最大持仓股-->
                    <div id="marketWheeledReversionDiv" style="display: none;zoom: 1;vertical-align: super">
                        <!-- Numeric Textfield -->
                        <div style="display: inline-block;zoom: 1">
                            <form action="#" style="margin-left: 50px;margin-right: 50px">
                                <div class="mdl-textfield mdl-js-textfield">
                                    <input class="mdl-textfield__input" type="text" pattern="^[1-9]{1}[\d]*$"
                                           id="sample6">
                                    <label class="mdl-textfield__label" for="sample6">形成期</label>
                                    <span class="mdl-textfield__error">请输入大于0的整数！</span>
                                </div>
                            </form>
                        </div>

                        <div style="display: inline-block;zoom: 1;margin-right: 50px">
                            <form action="#">
                                <div class="mdl-textfield mdl-js-textfield">
                                    <input class="mdl-textfield__input" type="text" pattern="^[1-9]{1}[\d]*$"
                                           id="sample7">
                                    <label class="mdl-textfield__label" for="sample7">持有期</label>
                                    <span class="mdl-textfield__error">请输入大于0的整数！</span>
                                </div>
                            </form>
                        </div>

                        <div style="display: inline-block;zoom: 1;margin-right: 50px">
                            <form action="#">
                                <div class="mdl-textfield mdl-js-textfield">
                                    <input class="mdl-textfield__input" type="text" pattern="^[1-9]{1}[\d]*$"
                                           id="sample8">
                                    <label class="mdl-textfield__label" for="sample8">最大持仓股</label>
                                    <span class="mdl-textfield__error">请输入大于0的整数！</span>
                                </div>
                            </form>
                        </div>
                    </div>

                    <script>
                        $(document).ready(function () {
                            $("#momentumSelected").click(function () {
                                $("#strategySelected").html("动量策略");
                                $("#meanReversionDiv").fadeOut();
                                $("#marketWheeledReversionDiv").fadeOut();
                                $("#momentum_OtherDiv").css("display", "inline-block");
                                $("#strategyInfoHead").html("什么是动量策略？");
                                $("#strategyInfoContent").html("价格动量是指价格变化的速度，或一段时间内价格的变化率。在一般情况下，我们预期表现最好的股票将会继续表现强劲，而表现最差的将继续表现不佳。用通俗的话来讲就是“追涨杀跌”");
                            });

                            $("#meanReversionSelected").click(function () {
                                $("#strategySelected").html("均值回归");
                                $("#momentum_OtherDiv").fadeOut();
                                $("#marketWheeledReversionDiv").fadeOut();
                                $("#meanReversionDiv").css("display", "inline-block");
                                $("#strategyInfoHead").html("什么是均值回归？");
                                $("#strategyInfoContent").html("均值回归（Mean Reversion），是在价格震荡中博取反弹的交易思路。这个理论基于以下观测：价格的波动一般会以它的均线为中心。也就是说，当标的价格由于波动而偏离移动均线时，它将调整并重新归于均线。那么如果我们如果能捕捉偏离股价的回归，就可以从此获利。");
                            });

                            $("#smallMarketWheeledStrategySelected").click(function () {
                                $("#strategySelected").html("小市值轮动策略");
                                $("#momentum_OtherDiv").fadeOut();
                                $("#meanReversionDiv").fadeOut();
                                $("#marketWheeledReversionDiv").css("display", "inline-block");
                                $("#strategyInfoHead").html("什么是小市值轮动策略？");
                                $("#strategyInfoContent").html("每隔若干个交易日，等金额持有市值排名最小的前几只股票，卖出其他股票。");
                            });

                            $("#otherStrategySelected").click(function () {
                                $("#strategySelected").html("其他策略");
                                $("#meanReversionDiv").fadeOut();
                                $("#momentum_OtherDiv").css("display", "inline-block");
                            })
                        })
                    </script>


                </div>
            </div>


            <div align="right" style="margin-top: 40px;margin-bottom: 80px">
                <%--<div id="#loadingDiv" style="display: none">--%>
                <%--<img src="${pageContext.request.contextPath}/css/pic/loading.gif" width="40">--%>
                <%--</div>--%>
                <button class="confirmBackTest" id="confirmBackTest">
                    确认回测
                </button>
            </div>

            <%@ include file="backTestResultChart.jsp" %>

            <script>

                $("#confirmBackTest").click(function () {

                    backTest();
                });

                function goToDIY() {
                    self.location.href = "${pageContext.request.contextPath}/jsp/DIYStrategy.jsp";
                }
            </script>


        </div>

    </div>

</div>

</body>
</html>

<%----%>