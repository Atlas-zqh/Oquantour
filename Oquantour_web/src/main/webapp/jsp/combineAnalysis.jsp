<%--
  Created by IntelliJ IDEA.
  User: st
  Date: 2017/6/8
  Time: 10:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="navBar.jsp" %>
<html>
<head>
    <title>组合分析</title>

    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <script src="${pageContext.request.contextPath}/js/g2.js"></script>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/combineAnalysis.css">
    <script src="${pageContext.request.contextPath}/js/combineComponent.js"></script>
</head>
<body style="background-color: #f6f6f6">

<div align="center">
    <div style="width: 1100px;height: 140px;background-color: #C5CED5;padding: 45px 30px 20px;margin-top: 40px"
         align="left">
        <div style="display: inline-block">
            <p class="combineInfoHead" id="combineName">
                组合名称
            </p>
        </div>

        <div style="display: inline-block;margin-left: 40px">
            <p class="combineInfoHead" style="font-size: 14px">
                总收益
            </p>
        </div>

        <div style="display: inline-block;">
            <p class="combineInfoHead" id="combineTotalProfit" style="font-size: 35px">
                --
            </p>
        </div>

        <div style="display: inline-block;margin-left: 20px">
            <p class="combineInfoHead" style="font-size: 14px">
                净值
            </p>
        </div>

        <div style="display: inline-block;">
            <p class="combineInfoHead" id="combineWorth" style="font-size: 35px">
                --
            </p>
        </div>

        <div id="adjustDiv" style="display:inline-block;float: right;margin-top: 40px;margin-left: 20px"
             onclick="goToAdjust()">
            <div style="display: inline-block">
                <img src="${pageContext.request.contextPath}/css/pic/adjustment.png" width="20">
            </div>
            <div style="display: inline-block">
                <p style="color: #fff;font-weight: 300;font-size: 16px">调仓</p>
            </div>
        </div>

        <div id="deleteDiv" style="display:inline-block;float: right;margin-top: 40px"
             onclick="deleteCombine()">
            <div style="display: inline-block">
                <img src="${pageContext.request.contextPath}/css/pic/delete1.png" width="20">
            </div>
            <div style="display: inline-block">
                <p style="color: #fff;font-weight: 300;font-size: 16px">删除</p>
            </div>
        </div>

    </div>

    <div style="width:1100px;border: solid 1px #e1e1e1;margin-top: 40px;padding: 30px;background-color: #fff"
         align="left">
        <div style="display: inline-block;zoom: 1">
            <p style="color: #9a9a9a;font-weight: normal;font-size: 20px;margin-right: 30px">
                || 收益情况
            </p>
        </div>
        <div style="display: inline-block">
            <img src="${pageContext.request.contextPath}/css/pic/question.png" id="combineProIntro" width="25"
                 style="vertical-align: text-bottom">
        </div>
        <div class="mdl-tooltip" for="combineProIntro">
            <span>组合收益怎么样？<br>来看看组合收益与大盘指数的对比，组合能力一目了然。<br>温馨提示：股市变数多，组合数据与实际操作结果存在差异</span>
        </div>

        <div id="loadingReturn" style="margin-top: 80px" align="center">
            <img src="${pageContext.request.contextPath}/css/pic/loading.gif" width="60">
        </div>

        <div id="returnRateChartDiv" style="width: 100%;height: 400px;margin-top: 20px">

        </div>
    </div>

    <div style="width:1100px;margin-top: 40px;">
        <div style="display:inline-block;width: 400px;border: solid 1px #e1e1e1;padding: 30px;vertical-align: top;background-color: #fff"
             align="left">
            <div style="display: inline-block;zoom: 1">
                <p style="color: #9a9a9a;font-weight: normal;font-size: 20px;margin-right: 30px">
                    || 组合成分
                </p>
            </div>
            <div style="display: inline-block">
                <img src="${pageContext.request.contextPath}/css/pic/question.png" id="combineCompoIntro" width="25"
                     style="vertical-align: text-bottom">
            </div>
            <div class="mdl-tooltip" for="combineCompoIntro">
                <span>持股分散度越高，组合高收益可能越高。分散投资，才能抵挡股市的汹涌波涛！</span>
            </div>

            <div id="pieChartDiv" style="width: 340px;height:480px">

            </div>
        </div>

        <div style="display: inline-block;width: 670px;height:590px;overflow:auto;margin-left: 24px;border: solid 1px #e1e1e1;padding: 30px;vertical-align: top;background-color: #fff"
             align="left">
            <p style="color: #9a9a9a;font-weight: normal;font-size: 20px;margin-right: 30px">
                || 调仓记录
            </p>

            <table class="mdl-data-table mdl-js-data-table mdl-shadow--2dp" id="combinationTable" style="width:100%;">

            </table>
        </div>

    </div>

    <div style="width:1100px;border: solid 1px #e1e1e1;margin-top: 40px;padding: 30px;background-color: #fff"
         align="left">
        <div style="display: inline-block;zoom: 1">
            <p style="color: #9a9a9a;font-weight: normal;font-size: 20px;margin-right: 30px">
                || 组合分析
            </p>
        </div>
        <div style="display: inline-block">
            <img src="${pageContext.request.contextPath}/css/pic/question.png" id="combineRankIntro" width="25"
                 style="vertical-align: text-bottom">
        </div>
        <div class="mdl-tooltip" for="combineRankIntro">
            <span>组合股票哪只表现最好？看各股收益贡献率，帮您抉择组合内股票去留。</span>
        </div>

        <div>
            <div style="width: 1100px;height: auto">
                <div style="display: inline-block;width: 49%;border-right: solid 2px #e1e1e1">
                    <p class="divHead" style="margin-bottom: 0">最赚钱股票</p>
                    <%--<div class="splitLine"></div>--%>

                    <div>
                        <table style="width: 100%;border-collapse: separate;border-spacing: 10px" id="maxTable">
                            <tr class="tableHead">
                                <th>排名</th>
                                <th>股票名</th>
                                <th>平均仓位</th>
                                <th>交易次数</th>
                                <th>收益贡献率</th>
                            </tr>
                        </table>
                    </div>
                </div>
                <div style="display: inline-block;width: 49%">
                    <p class="divHead" style="margin-bottom: 0">最输钱股票</p>
                    <%--<div class="splitLine"></div>--%>

                    <div>
                        <table style="width: 100%;border-collapse: separate;border-spacing: 10px" id="minTable">
                            <tr class="tableHead">
                                <th>排名</th>
                                <th>股票名</th>
                                <th>平均仓位</th>
                                <th>交易次数</th>
                                <th>收益贡献率</th>
                            </tr>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>


</div>

<script>
    var combinationStock = [];
    var combineName;
    var userName = getCookie("account");
    $(document).ready(function () {
        if (userName == "") {
            fail_prompt("请先登录！", 2000);
            $(".loadingDiv").hide();
        }
        var url = document.location.toString();
        var arrObj = url.split("?");
        if (arrObj.length > 1) {
            var name = decodeURI(arrObj[1].split("=")[1]);
            console.log(name);
            combineName = name;
            $("#combineName").html(combineName);
        }
        else {
            return "";
        }

        $.ajax({
            url: "getPortfolio.action",
            dataType: 'json',
            type: 'GET',
            data: {
                username: userName,
                portfolioName: combineName
            },
            success: function (data) {
                var jsonObj = eval('(' + data + ')');
                $("#combineTotalProfit").html((jsonObj[0]["totalReturnRate"] * 100).toFixed(3) + "%");
                $("#combineWorth").html(jsonObj[0]["newNet"].toFixed(3));
                var pieData = [];
                var i = 1
                for (; i < jsonObj.length; i++) {
                    if (jsonObj[i]["name"] != null) {
                        pieData.push(jsonObj[i]);
                    } else {
                        i++;
                        break;
                    }
                }
                drawPieChart(pieData);

                var returnRateDate = [];
                var returnRateData = [];
                var maxProfit = [];
                var minProfit = [];
                var sza = [];
                var zx = [];
                var cy = [];
                var hs = [];
                var szc = [];
                var sz = [];
                for (; i < jsonObj.length; i++) {
                    if (jsonObj[i]["date"] != null) {
                        returnRateData.push(jsonObj[i]["y"].toFixed(3));
                        returnRateDate.push(jsonObj[i]["date"]);
                    } else if (jsonObj[i]["timeStamp"] != null) {
                        combinationStock.push(jsonObj[i]);
                    } else if (jsonObj[i]["maxPortName"] != null) {
                        maxProfit.push(jsonObj[i])
                    } else if (jsonObj[i]["SZA"] != null) {
                        sza.push(jsonObj[i]["SZA"].toFixed(3));
                    } else if (jsonObj[i]["ZX"] != null) {
                        zx.push(jsonObj[i]["ZX"].toFixed(3));
                    } else if (jsonObj[i]["CY"] != null) {
                        cy.push(jsonObj[i]["CY"].toFixed(3));
                    } else if (jsonObj[i]["HS"] != null) {
                        hs.push(jsonObj[i]["HS"].toFixed(3));
                    } else if (jsonObj[i]["SZC"] != null) {
                        szc.push(jsonObj[i]["SZC"].toFixed(3));
                    } else if (jsonObj[i]["SZ"] != null) {
                        sz.push(jsonObj[i]["SZ"].toFixed(3));
                    }
                    else {
                        minProfit.push(jsonObj[i])
                    }
                }

                drawReturnRateChart(returnRateData, returnRateDate, sza, zx, cy, hs, szc, sz);
                addCombinationStock(combinationStock);

                addProfitTable(maxProfit, 'maxTable', 'maxPortName');
                addProfitTable(minProfit, 'minTable', 'minPortName');
            }


        })


    });

    function addProfitTable(data, id, name) {
        var length = data.length > 5 ? 5 : data.length;
        for (var i = 0; i < length; i++) {
            var insertTr = document.getElementById(id).insertRow(i + 1);
            insertTr.className = "tableContent";
            var insertTd = insertTr.insertCell(0);
            insertTd.innerHTML = "" + (i + 1);
            var insertTd = insertTr.insertCell(1);
            insertTd.innerHTML = "<span style='cursor: pointer' title=data[i][name] onclick='goToStockInfo(this.title)'>" + getStockName(data[i][name]) + "</span>";
            var insertTd = insertTr.insertCell(2);
            insertTd.innerHTML = (data[i]["avg"] * 100).toFixed(2) + "%";
            var insertTd = insertTr.insertCell(3);
            insertTd.innerHTML = data[i]["trade"];
            var insertTd = insertTr.insertCell(4);
            insertTd.innerHTML = data[i]["y"].toFixed(2);
        }
    }

    function addCombinationStock(data) {
        for (var i = data.length - 1; i >= 0; i--) {
            var currentRows = document.getElementById("combinationTable").rows.length;
            var insertTr = document.getElementById("combinationTable").insertRow(currentRows);
            insertTr.style.textAlign = "center";

            var insertTd = insertTr.insertCell(0);
//            insertTd.className = "combinationTableDate";
            insertTd.innerHTML = data[i]["timeStamp"];
            insertTd.style.textAlign = "center";
            insertTd.style.backgroundColor = "#c5ced5";
            insertTd.style.color = "#fff";
            insertTd.style.fontSize = "16px";
            insertTd.colSpan = 8;

            insertTr = document.getElementById("combinationTable").insertRow(currentRows + 1);
            insertTr.className = "combineTableSpecInfoHead";
            var insertTd = insertTr.insertCell(0);
            insertTd.innerHTML = "名称";
            insertTd.style.textAlign = "left";
            insertTd.style.color = "#7a7a7a";
            var insertTd = insertTr.insertCell(1);
            insertTd.innerHTML = "调仓记录";
            insertTd.style.textAlign = "center";
            insertTd.style.color = "#7a7a7a";

            for (var j = 0; j < data[i]["stocks"].length; j++) {
                var name = getStockName(data[i]["stocks"][j])
                if (getStockName(data[i]["stocks"][j]) == null) {
                    continue;
                }
                currentRows = document.getElementById("combinationTable").rows.length;
                insertTr = document.getElementById("combinationTable").insertRow(currentRows);
                var insertTd = insertTr.insertCell(0);
                insertTd.innerHTML = "<span style='cursor: pointer' title=data[i]['stocks'][j] onclick='goToStockInfo(this.title)'>" + name + "</span>";
                insertTd.style.textAlign = "left";
                insertTd.style.color = "#7a7a7a";
                var insertTd = insertTr.insertCell(1);
                insertTd.style.textAlign = "center";
                insertTd.style.color = "#7a7a7a";
                if (i == 0)
                    insertTd.innerHTML = "0%   ->   " + (data[i]["positions"][j] * 100).toFixed(2) + "%";
                else
                    insertTd.innerHTML = (data[i]["positions2"][j] * 100).toFixed(2) + "%   ->   " + (data[i]["positions"][j] * 100).toFixed(2) + "%";
            }
        }

    }

    function goToStockInfo(stockID) {
        console.log(stockID)
        window.open("../jsp/stockInfo.jsp?stockName=" + stockID);
    }

    function drawPieChart(data) {
        var Stat = G2.Stat;
        G2.Global.colors['default'] = ['#A19FBA', '#B39EBA', '#BA95A2', '#BAB0A0', '#B0BAA4', '#A1BAA3', '#A3BAB6']; // 更改默认的颜色
        var chart = new G2.Chart({
            id: 'pieChartDiv',
//                forceFit: true,
            height: 480,
            width: 340
        });
        chart.source(data);
        chart.coord('theta', {
            radius: 0.8 // 设置饼图的大小
        });
        chart.legend('name', {
            position: 'bottom',
            itemWrap: true,
            formatter: function (val) {
                for (var i = 0, len = data.length; i < len; i++) {
                    var obj = data[i];
                    if (obj.name === val) {
                        return val + ': ' + obj.value.toFixed(2) + '%';
                    }
                }
            }
        });
        chart.tooltip({
            title: null,
            map: {
                value: 'value'
            }
        });
        chart.legend({
            word: {fill: '#8d8d8d'}
        });
        chart.intervalStack()
            .position(Stat.summary.percent('value'))
            .color('name')
            .label('name*..percent', function (name, percent) {
                percent = (percent * 100).toFixed(2) + '%';
                return name + ' ' + percent;
            });
        chart.render();
        // 设置默认选中
        var geom = chart.getGeoms()[0]; // 获取所有的图形
        var items = geom.getData(); // 获取图形对应的数据
        geom.setSelected(items[1]); // 设置选中
    }

    function drawReturnRateChart(data, date, sza, zx, cy, hs, szc, sz) {
        var myChart = echarts.init(document.getElementById('returnRateChartDiv'));
        myChart.setOption(option = {
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'cross',
                    crossStyle: {
                        color: '#999'
                    }
                }
            },
            toolbox: {
                feature: {
                    dataView: {show: true, readOnly: false},
                    magicType: {show: true, type: ['line', 'bar']},
                    restore: {show: true},
                    saveAsImage: {show: true}
                }
            }, dataZoom: [
                {
                    start: 95,
                    end: 100,
                }, {
                    type: 'inside'
                }
            ],
            legend: {
                data: ['我的组合', '上证指数', '中小板指', '创业板指', '沪深300', '深证成指', '深证A指'],
                selected: {
                    // 选中'系列1'
                    '我的组合': true,
                    // 不选中'系列2'
                    '上证指数': false,
                    '中小板指': false,
                    '沪深300': false,
                    '创业板指': false,
                    '深证A指': false,
                    '深证成指': false
                }
            },
            xAxis: [
                {
                    type: 'category',
                    data: date,
                    axisPointer: {
                        type: 'shadow'
                    }
                }
            ],
            yAxis: [
                {
                    type: 'value',
                    name: '',
                    axisLabel: {
                        formatter: '{value} '
                    }
                }
            ],
            series: [
                {
                    name: '我的组合',
                    type: 'line',
                    data: data,
                    itemStyle: {
                        normal: {
                            color: "#cd8585"
                        }
                    }
                }, {
                    name: '深证A指',
                    type: 'line',
                    data: sza,
                    itemStyle: {
                        normal: {
                            color: "#A2A7C3"
                        }
                    }
                }, {
                    name: '中小板指',
                    type: 'line',
                    data: zx,
                    itemStyle: {
                        normal: {
                            color: "#C3A9C1"
                        }
                    }
                }, {
                    name: '创业板指',
                    type: 'line',
                    data: cy,
                    itemStyle: {
                        normal: {
                            color: "#C3A9AE"
                        }
                    }
                }, {
                    name: '沪深300',
                    type: 'line',
                    data: hs,
                    itemStyle: {
                        normal: {
                            color: "#B5C3B5"
                        }
                    }
                }, {
                    name: '深证成指',
                    type: 'line',
                    data: szc,
                    itemStyle: {
                        normal: {
                            color: "#74a57e"
                        }
                    }
                }, {
                    name: '上证指数',
                    type: 'line',
                    data: sz,
                    itemStyle: {
                        normal: {
                            color: "#A7C3BD"
                        }
                    }
                }
            ]
        })
//        var slider = new G2.Plugin.slider({
//            domId: 'slider', //DOM id
//            height: 26,
//            xDim: 'time',
//            yDim: 'flow',
//            charts: [chart]
//        });
//        chart.line().position('date*value').color('type', ['#A2A7C3', '#C3A9C1', '#C3A9AE', '#B5C3B5', '#A7C3BD']).shape('spline').size(2);
//        chart.render();

        $("#loadingReturn").hide();


    }

    function getBack() {
        self.location = "${pageContext.request.contextPath}/jsp/userHomepage.jsp";
    }

    function goToAdjust() {
        combineName = encodeURI(combineName);
        self.location = "${pageContext.request.contextPath}/jsp/combineAdjustment.jsp?" + combineName;
    }

    function deleteCombine() {
        $.ajax({
            url: 'deletePortfolio.action',
            dataType: 'json',
            success: function () {
                success_prompt("删除成功！", 2000);
                setTimeout('getBack()', 2000);
            }
        })
    }

    function getStockName(code) {
        var allStock = window.localStorage.getItem("allStock").split(";");
        for (var i = 0; i < allStock.length; i++) {
            if (allStock[i].split(",")[0] == code)
                return allStock[i].split(",")[1];
        }
    }
</script>
</body>

</html>


<%@ include file="pageFoot.jsp" %>