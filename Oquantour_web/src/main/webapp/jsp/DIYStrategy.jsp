<%--
  Created by IntelliJ IDEA.
  User: st
  Date: 2017/6/10
  Time: 14:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="navBar.jsp" %>
<html>
<head>
    <title>策略DIY</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/backTest.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/DIYStrategy.css">

    <script src="${pageContext.request.contextPath}/js/backtest.js"></script>
    <script src="${pageContext.request.contextPath}/js/multifactorialSelection.js"></script>
</head>
<body>
<div class="alert"></div>
<div>
    <div align="center">
        <div class="searchFilter" align="left">
            <!-- List with avatar and controls -->

            <div style="display: inline-block">
                <p style="font-size: 25px;font-weight: normal;color: #7c7c7c">
                    策略DIY 发现更好的策略！
                </p>
            </div>

            <div id="goBackTest" style="display:inline-block;margin-left: 20px">
                <a href="${pageContext.request.contextPath}/jsp/backTest.jsp"
                   style="font-size: 20px;font-weight: normal;color: #b1898c;text-decoration: underline">
                    经典策略
                </a>
            </div>
            <div class="mdl-tooltip" id="toolTip0" for="goBackTest">
                多种经典策略模式
                <br>
                为您提供更多选择
            </div>


            <div>
                <!--第一步部分-->
                <div class="firstStepDiv" style="margin-top: 60px">
                    <p style="font-size: 18px;font-weight: 300;color: #787878"><i>第 <img
                            src="${pageContext.request.contextPath}/css/pic/1.png" width="30"
                            align="middle"> 步：根据自定义因子筛选股票池</i></p>
                </div>

                <%@ include file="MultifactorialSelection.jsp" %>

            </div>
            <div id="hide" style="display: none">
                <!--第二步部分-->
                <div style="margin-top: 40px">
                    <p style="font-size: 18px;font-weight: 300;color: #787878"><i>第 <img
                            src="${pageContext.request.contextPath}/css/pic/2.png" width="40"
                            align="middle"> 步：根据自定义因子设置买入条件</i></p>
                </div>

                <div class="ui floating labeled icon dropdown button" id="buySelectionMenu"
                     style="display:inline-block;background-color:#fff;width:200px;border:solid 1px #e1e1e1;margin-top: 30px">
                    <i class="Share Alternate icon"></i>
                    <span class="text">选择自定义因子</span>
                    <div class="menu">
                        <div class="header">
                            <i class="tags icon"></i>
                            行情指标
                        </div>
                        <div class="divider"></div>
                        <div class="item">
                            调仓日开盘价
                        </div>
                        <div class="item">
                            调仓日收盘价
                        </div>
                        <div class="item">
                            调仓日复权收盘价
                        </div>
                        <div class="item">
                            调仓日最高价
                        </div>
                        <div class="item">
                            调仓日最低价
                        </div>
                        <div class="item">
                            调仓日涨跌幅
                        </div>
                        <div class="item">
                            调仓日成交量
                        </div>
                        <div class="item">
                            调仓日成交额
                        </div>
                        <div class="header">
                            <i class="tags icon"></i>
                            技术指标
                        </div>
                        <div class="divider"></div>
                        <div class="item">
                            KDJ线k值
                        </div>
                        <div class="item">
                            KDJ线d值
                        </div>
                        <div class="item">
                            KDJ线j值
                        </div>
                        <div class="item">
                            12日EMA
                        </div>
                        <div class="item">
                            26日EMA
                        </div>
                        <div class="item">
                            布林上线
                        </div>
                        <div class="item">
                            布林下线
                        </div>
                        <div class="item">
                            MACD离差值
                        </div>
                        <div class="item">
                            MACD讯号线
                        </div>
                        <div class="item">
                            RSI
                        </div>
                    </div>
                </div>

                <div class="indexQuestion" id="question2" style="display: inline-block;outline: none"
                     onclick="openInfo()">
                    <img src="${pageContext.request.contextPath}/css/pic/question.png" width="30">
                </div>
                <div class="mdl-tooltip" id="toolTip3" for="question2">
                    什么是多因子？
                    <br>
                    点击查看因子信息
                </div>

                <div>
                    <ul id="myTab" class="nav nav-tabs">
                        <li onclick="setActive('range')" class="active">
                            <a href="#range" data-toggle="tab" id="rangeTab">
                                区间选股
                            </a>
                        </li>
                        <div class="mdl-tooltip" id="toolTip1" for="rangeTab">
                            区间和权重只可二选一
                            <br>
                            默认选择为最后修改
                        </div>
                        <li onclick="setActive('weight')"><a href="#weight" data-toggle="tab"
                                                             id="weightTab">权重选股</a></li>
                        <!-- Multiline Tooltip -->
                        <div class="mdl-tooltip" id="toolTip2" for="weightTab">
                            区间和权重只可二选一
                            <br>
                            默认选择为最后修改
                        </div>
                    </ul>

                    <div id="myTabContent" class="tab-content">
                        <div class="tab-pane fade in active" id="range">

                            <table class="mdl-data-table mdl-js-data-table mdl-shadow--2dp" id="buyStockTable"
                                   style="width: 100%;margin-top: 20px">
                                <thead>
                                <tr>
                                    <th class="mdl-data-table__cell--non-numeric">指标名称</th>
                                    <th class="mdl-data-table__cell--non-numeric">选项</th>
                                    <th class="mdl-data-table__cell--non-numeric">值</th>
                                    <th class="mdl-data-table__cell--non-numeric">操作</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td class="mdl-data-table__cell--non-numeric">
                                        是否可重复买入
                                    </td>
                                    <td class="mdl-data-table__cell--non-numeric">
                                        是
                                    </td>
                                    <td class="mdl-data-table__cell--non-numeric">
                                        --
                                    </td>
                                    <td class="mdl-data-table__cell--non-numeric">
                                        --
                                    </td>
                                </tr>
                                <tr>
                                    <td class="mdl-data-table__cell--non-numeric">
                                        买入方式
                                    </td>
                                    <td class="mdl-data-table__cell--non-numeric">
                                        平分可用资金
                                    </td>
                                    <td class="mdl-data-table__cell--non-numeric">
                                        --
                                    </td>
                                    <td class="mdl-data-table__cell--non-numeric">
                                        --
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>

                        <div class="tab-pane fade" id="weight">
                            <table class="mdl-data-table mdl-js-data-table mdl-shadow--2dp" id="weightStockTable"
                                   style="width: 100%;margin-top: 20px">
                                <thead>
                                <tr>
                                    <th class="mdl-data-table__cell--non-numeric">指标名称</th>
                                    <th class="mdl-data-table__cell--non-numeric">选项</th>
                                    <th class="mdl-data-table__cell--non-numeric">值</th>
                                    <th class="mdl-data-table__cell--non-numeric">操作</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td class="mdl-data-table__cell--non-numeric">
                                        是否可重复买入
                                    </td>
                                    <td class="mdl-data-table__cell--non-numeric">
                                        是
                                    </td>
                                    <td class="mdl-data-table__cell--non-numeric">
                                        --
                                    </td>
                                    <td class="mdl-data-table__cell--non-numeric">
                                        --
                                    </td>
                                </tr>
                                <tr>
                                    <td class="mdl-data-table__cell--non-numeric">
                                        买入方式
                                    </td>
                                    <td class="mdl-data-table__cell--non-numeric">
                                        平分可用资金
                                    </td>
                                    <td class="mdl-data-table__cell--non-numeric">
                                        --
                                    </td>
                                    <td class="mdl-data-table__cell--non-numeric">
                                        --
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <!--第三步部分-->
                <div style="margin-top: 40px">
                    <p style="font-size: 18px;font-weight: 300;color: #787878"><i>第 <img
                            src="${pageContext.request.contextPath}/css/pic/3.png" width="40"
                            align="middle"> 步：日期和更多设置</i></p>
                </div>

                <script src="${pageContext.request.contextPath}/js/flatpickr.js"></script>

                <div style="margin-top: 30px;border: solid 1px #f1f1f1;padding: 40px 20px 20px;">
                    <div style="padding: 20px">
                        <div style="display:inline-block;">
                            <p style="font-size: 18px;font-weight: normal;color: #969696">选择回测时间&nbsp;&nbsp;&nbsp;</p>
                        </div>
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

                    <div style="padding-left: 22px;margin-top: 20px">
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

                        <div style="display: inline-block;zoom: 1">
                            <form action="#">
                                <div class="mdl-textfield mdl-js-textfield">
                                    <input class="mdl-textfield__input" type="text" pattern="^[1-9]{1}[\d]*$"
                                           id="holdingPeriod">
                                    <label class="mdl-textfield__label" for="holdingPeriod">持有期</label>
                                    <span class="mdl-textfield__error">请输入大于0的整数！</span>
                                </div>
                            </form>
                        </div>

                        <div style="display: inline-block;zoom: 1;margin-left: 22px">
                            <form action="#">
                                <div class="mdl-textfield mdl-js-textfield">
                                    <input class="mdl-textfield__input" type="text" pattern="^[1-9]{1}[\d]*$"
                                           id="maxStockNum">
                                    <label class="mdl-textfield__label" for="maxStockNum">最大持仓股</label>
                                    <span class="mdl-textfield__error">请输入大于0的整数！</span>
                                </div>
                            </form>
                        </div>


                    </div>
                </div>

                <div align="right" style="margin-top: 40px;margin-bottom: 80px">
                    <button class="confirmChoose" onclick="confirmBackTest()">
                        确认回测
                    </button>
                </div>

                <%@ include file="backTestResultChart.jsp" %>

            </div>


        </div>
    </div>
</div>

<script>
    var isRangeActive = 1;
    var stocks = "";
    isBackTesting = 1;
    $(document).ready(function () {
        $("#buySelectionMenu")
            .dropdown({
                onChange: function (value, text, $selectedItem) {
                    // custom action
                    if (isRangeActive == 1) {
                        addIndex_BuyTable(value);
                    } else {
                        addIndex_WeightTable(value);
                    }

                }
            })
    });

    function setActive(value) {
        if (value == "range") {
            isRangeActive = 1;
        } else {
            isRangeActive = 0;
        }

        console.log("-----" + isRangeActive);
    }

    function deleteBuy(cell) {
        var rowIndex = $(cell).parent().parent().index() + 1;
        //console.log(rowIndex);
        var table = document.getElementById("buyStockTable");
        table.deleteRow(rowIndex);
    }

    function deleteWeight(cell) {
        var rowIndex = $(cell).parent().parent().index() + 1;
        //console.log(rowIndex);
        var table = document.getElementById("weightStockTable");
        table.deleteRow(rowIndex);
    }

    function addIndex_BuyTable(value) {
        var table = document.getElementById("buyStockTable");
        var rows = table.rows;
        for (var i = 1; i < rows.length; i++) {
            //$("#combineTable tr:eq(" + i + ") td:eq(0)").css("background-color", "#000");
            //console.log($("#combineTable tr:eq(" + i + ") td:eq(1)").html());
            if ($("#buyStockTable tr:eq(" + i + ") td:eq(0)").html() == value) {
                fail_prompt("已存在该因子！", 2000);
                return;
            }
        }

        var insertTr = document.getElementById("buyStockTable").insertRow(rows.length);
        var insertTd = insertTr.insertCell(0);
        insertTd.className = "mdl-data-table__cell--non-numeric";
        insertTd.innerHTML = value.toString().toUpperCase();
        var insertTd = insertTr.insertCell(1);
        insertTd.innerHTML = "<select class='ui dropdown' onchange='change_buyTable(this)'> <option value='0'>大于</option> <option value='1'>小于</option> <option value='2'>区间</option> </select>";
        var insertTd = insertTr.insertCell(2);
        if (value == "调仓日涨跌幅") {
            insertTd.innerHTML = "<div class='ui form'> <div class='field'> <input type='text'> <span style='color: #7a7a7a;font-size: 16px'>%</span> </div> </div> ";
        } else {
            insertTd.innerHTML = "<div class='ui form'> <div class='field'> <input type='text'> </div> </div> ";
        }
        var insertTd = insertTr.insertCell(3);
        insertTd.innerHTML = "<img src='${pageContext.request.contextPath}/css/pic/delete.png' class='deleteButton' width='25' onclick='deleteBuy(this)'>"
        setDropdown(table.rows.length - 1);
    }

    function addIndex_WeightTable(value) {
        var table = document.getElementById("weightStockTable");
        var rows = table.rows;
        for (var i = 1; i < rows.length; i++) {
            //$("#combineTable tr:eq(" + i + ") td:eq(0)").css("background-color", "#000");
            //console.log($("#combineTable tr:eq(" + i + ") td:eq(1)").html());
            if ($("#weightStockTable tr:eq(" + i + ") td:eq(0)").html() == value) {
                fail_prompt("已存在该因子！", 2000);
                return;
            }
        }
        var insertTr = document.getElementById("weightStockTable").insertRow(rows.length);
        var insertTd = insertTr.insertCell(0);
        insertTd.className = "mdl-data-table__cell--non-numeric";
        insertTd.innerHTML = value.toString().toUpperCase();
        var insertTd = insertTr.insertCell(1);
        insertTd.className = "mdl-data-table__cell--non-numeric";
        insertTd.innerHTML = "权重";
        var insertTd = insertTr.insertCell(2);
        insertTd.innerHTML = "<div class='ui form'> <div class='field'> <input type='text'> </div> </div> ";
        var insertTd = insertTr.insertCell(3);
        insertTd.innerHTML = "<img src='${pageContext.request.contextPath}/css/pic/delete.png' class='deleteButton' width='25' onclick='deleteWeight(this)'>"
        setDropdown(table.rows.length - 1);
    }


    function change_buyTable(cell) {
//        $(cell).parent().parent().parent().css("background-color","#000");

        var index = $(cell).parent().parent().parent().index() + 1;
        if ($(cell).val() == 2) {
//            console.log("111111" + $("#buyStockTable tr:eq(" + index + ") td:eq(0)").html());
            if ($("#buyStockTable tr:eq(" + index + ") td:eq(0)").html() == "调仓日涨跌幅") {
                $("#buyStockTable tr:eq(" + index + ") td:eq(2)").html("<div class='ui form me'> <input type='text' class='input1' style='width: 120px;'> <span style='color: #7a7a7a;font-size: 16px'>%～</span> <input type='text' class='input2' style='width: 120px;'> <span style='color: #7a7a7a;font-size: 16px'>%</span> </div> </div> ");
            } else {
                $("#buyStockTable tr:eq(" + index + ") td:eq(2)").html("<div class='ui form me'> <input type='text' class='input1' style='width: 120px;'> <span style='color: #7a7a7a;font-size: 16px'>～</span> <input type='text' class='input2' style='width: 120px;'> </div> </div> ");
            }
        } else {
            if ($("#buyStockTable tr:eq(" + index + ") td:eq(0)").html() == "调仓日涨跌幅") {

                $("#buyStockTable tr:eq(" + index + ") td:eq(2)").html("<div class='ui form'> <div class='field'> <input type='text'> <span style='color: #7a7a7a;font-size: 16px'>%</span> </div> </div> ");
            } else {
                $("#buyStockTable tr:eq(" + index + ") td:eq(2)").html("<div class='ui form'> <div class='field'> <input type='text'> </div> </div> ");
            }
        }
    }

    function confirmBackTest() {

        var strategyType = "自定义策略";
        var startDate = $('#datePicker_start').val().split(" ")[0];
        var endDate = $('#datePicker_start').val().split(" ")[2];
        var holdingPeriod = parseInt($("#holdingPeriod").val());
        var maxholdingStocks = parseInt($("#maxStockNum").val());
        var filter_Suspension = $("#filter_Suspension").prop('checked');
        var filter_NoData = $("#filter_NoData").prop('checked');
        var filter_ST = $("#filter_ST").prop('checked');

        console.log("startDate: " + startDate);
        console.log("endDate: " + endDate);
        console.log("holdingPeriod:" + holdingPeriod);
        console.log("maxHolding: " + maxholdingStocks);

        if ($("#holdingPeriod").val() == "" || $("#maxStockNum").val() == "") {
            fail_prompt("请填写持有期／最大持仓股！", 2000);
            return
        }

        var indexName = "";
        var indexVal = "";
        var strategyType = "DIY";

        if (isRangeActive == 1) {
            //区间选股
            var table = document.getElementById("buyStockTable");
            var rows = table.rows;
            for (var i = 3; i < rows.length; i++) {
                console.log($("#buyStockTable tr:eq(" + i + ") td:eq(1) select").val());
                indexName += $("#buyStockTable tr:eq(" + i + ") td:eq(0)").html() + ";";
                if ($("#buyStockTable tr:eq(" + i + ") td:eq(1) select").val() == 0) {
                    var number = $("#buyStockTable tr:eq(" + i + ") td:eq(2) input").val();
                    if (number == "") {
                        fail_prompt("请填写正确的区间！");
                        return
                    }

                    indexVal += parseFloat(number) + ";";
                    indexVal += Number.MAX_VALUE + ";";
                } else if ($("#buyStockTable tr:eq(" + i + ") td:eq(1) select").val() == 1) {
                    var number = $("#buyStockTable tr:eq(" + i + ") td:eq(2) input").val();
                    if (number == "") {
                        fail_prompt("请填写正确的区间！");
                        return
                    }

                    indexVal += -Number.MAX_VALUE + ";";
                    indexVal += parseFloat(number) + ";";
                } else if ($("#buyStockTable tr:eq(" + i + ") td:eq(1) select").val() == 2) {
                    var number1 = $("#buyStockTable tr:eq(" + i + ") td:eq(2) .input1").val();
                    var number2 = $("#buyStockTable tr:eq(" + i + ") td:eq(2) .input2").val();
                    if (number1 == "" || number2 == "" || parseFloat(number1) >= parseFloat(number2)) {
                        fail_prompt("请填写正确的区间！");
                        return
                    }
//                array.push(parseFloat(number1));
//                array.push(parseFloat(number2));

                    indexVal += parseFloat(number1) + ";";
                    indexVal += parseFloat(number2) + ";";
                }
            }
        } else if (isRangeActive == 0) {
            //权重选股
            var table = document.getElementById("weightStockTable");
            var rows = table.rows;
            for (var i = 3; i < rows.length; i++) {
                indexName += $("#weightStockTable tr:eq(" + i + ") td:eq(0)").html() + ";";
                var number = $("#weightStockTable tr:eq(" + i + ") td:eq(2) input").val();
                if (number == "" || parseFloat(number) < 0) {
                    fail_prompt("请填写正确的区间！");
                    return
                }
                indexVal += parseFloat(number) + ";";

            }
        }
        if (indexName == "") {
            fail_prompt("请至少选择一个买入条件！", 2000);
            return
        }
        $("#loadingDiv").show();
        $("#loadingDiv")[0].scrollIntoView({
            behavior: 'smooth'
        })
        $.ajax({
            url: "backTest.action",
            dataType: "json",
            type: 'POST',
            data: {
                flag: 1,
                isRangeActive: isRangeActive,
                startDate: startDate,
                endDate: endDate,
                stocks: stocks,
                holdingPeriod: holdingPeriod,
                maxholdingStocks: maxholdingStocks,
                filter_ST: filter_ST,
                filter_NoData: filter_NoData,
                filter_Suspension: filter_Suspension,
                indexName: indexName,
                indexVal: indexVal
            },
            success: function (data) {
                var jsonObj = eval('(' + data + ')');
                if (jsonObj[0]["info"] == "回测成功") {
                    drawBackTestResultChart(jsonObj, strategyType);
                } else {
                    fail_prompt(jsonObj[0]["info"], 2000);
                }
                $("#loadingDiv").hide();
            },
            error: function () {
                fail_prompt("ajax error", 2000);
            }
        })
    }
</script>

</body>
</html>
