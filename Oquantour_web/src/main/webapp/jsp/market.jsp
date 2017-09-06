<%--
  Created by IntelliJ IDEA.
  User: st
  Date: 2017/5/25
  Time: 15:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="navBar.jsp" %>
<html>
<head>
    <title>市场</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/market.css">

</head>
<body>

<div class="alert"></div>

<div align="center">


    <div align="center" style="width: 1300px;height:auto;margin-top: 40px;margin-bottom: 40px">
        <div id="leftPart" align="center">
            <p class="divHead" style="font-size: 18px;margin: 20px">
                <span id="marketDate" style="font-size: 14px">----/--/--</span>
                <br>
                <span>市场温度</span>
                <img src="${pageContext.request.contextPath}/css/pic/question.png" id="marketTempIntro" width="20"
                     style="vertical-align: sub;outline: none">
            </p>


            <div class="mdl-tooltip" for="marketTempIntro">
                <span>今天市场到底怎么样？我们帮您计算出了了直观的市场温度，供您参考。</span>
            </div>

            <p id="marketTemp">
                --
            </p>

            <div align="left">
                <ul type="none" id="navUl" style="padding: 0;margin-top: 50px;line-height: 45px;font-size: 16px;
    color: #979797;">
                    <li onclick="scrollIntoOverview()">市场概览</li>
                    <li onclick="scrollIntoChangeRanking()">行情排行榜</li>
                    <li onclick="scrollIntoHeatRanking()">热度排行榜</li>
                    <li onclick="scrollIntoPlateInfo()">行业分析</li>
                </ul>
            </div>

        </div>

        <div id="rightPart" align="left">
            <div id="marketOverviewDiv">
                <div style="display:inline-block;zoom: 1">
                    <p class="divHead">市场概览</p>
                </div>
                <div class="splitLine"></div>

                <div class="loadingDiv" align="center" style="margin-top: 80px">
                    <img src="${pageContext.request.contextPath}/css/pic/loading.gif" width="60">
                </div>
                <div id="marketOverviewContent" style="display: none">
                    <table style="width: 100%;border-collapse: separate;border-spacing: 10px">
                        <tr class="indexHead">
                            <td>
                                上证指数
                            </td>
                            <td>
                                沪深300
                            </td>
                            <td>
                                中小板指
                            </td>
                            <td>
                                创业板指
                            </td>
                        </tr>
                        <tr class="indexNum" style="margin-bottom: 20px">
                            <td>
                                <span id="index_SSE">--</span>
                                <span>&nbsp;&nbsp;</span>
                                <span class="indexChange" id="chg_SSE">(+0.45%)</span>

                            </td>
                            <td>
                                <span id="index_CSI300">--</span>
                                <span>&nbsp;&nbsp;</span>
                                <span class="indexChange" id="chg_CSI">(+0.45%)</span>

                            </td>
                            <td>
                                <span id="index_ZXE">--</span>
                                <span>&nbsp;&nbsp;</span>
                                <span class="indexChange" id="chg_ZXE">(-0.6%)</span>
                            </td>
                            <td>
                                <span id="index_GEI">--</span>
                                <span>&nbsp;&nbsp;</span>
                                <span class="indexChange" id="chg_GEI">(+0.4%)</span>

                            </td>
                        </tr>
                        <tr></tr>
                        <tr></tr>
                        <tr class="otherStatistics">
                            <td>
                                总交易量：
                                <span id="totalVolume">--</span>
                            </td>
                            <td>
                                总股票数：
                                <span id="totalNum">--</span>
                            </td>
                            <td>
                                涨停股票数：
                                <span id="limitUpStock">--</span>
                            </td>
                            <td>
                                跌停股票数：
                                <span id="limitDownStock">--</span>
                            </td>
                        </tr>
                        <tr class="otherStatistics">
                            <td>
                                涨幅>5%股票数：
                                <span id="up5perStock">--</span>
                            </td>
                            <td>
                                跌幅>5%股票数：
                                <span id="down5perStock">--</span>
                            </td>
                            <td>
                                大涨（开盘>5%）股票数：
                                <span id="openUp5perStock">--</span>
                            </td>
                            <td>
                                大跌（开盘<5%）股票数：
                                <span id="openDown5perStock">--</span>
                            </td>
                        </tr>


                    </table>
                </div>
            </div>

            <div id="quotesRankingDiv">
                <div style="display:inline-block;zoom: 1">
                    <p class="divHead">行情排行榜</p>
                </div>
                <div class="splitLine"></div>

                <div class="loadingDiv" align="center" style="margin-top: 80px">
                    <img src="${pageContext.request.contextPath}/css/pic/loading.gif" width="60">
                </div>

                <div id="quotesRankingContent" style="display: none">
                    <div id="changeRankingDiv">
                        <p class="rankingDivHead">涨跌幅排行</p>
                        <div>
                            <ul class="nav nav-tabs">
                                <li class="active"><a href="#HS" data-toggle="tab">沪市</a></li>
                                <li><a href="#SS" data-toggle="tab">深市</a></li>
                                <%--<li><a href="#GG" data-toggle="tab">港股</a></li>--%>
                            </ul>
                            <div class="tab-content">
                                <div class="tab-pane fade in active" id="HS" style="padding-top: 10px">
                                    <table class="changeRankingTable" id="hsChgUpListTable">
                                        <tr class="tableHead">
                                            <th>股票名</th>
                                            <th>当前价</th>
                                            <th id="changeRankTh">涨跌幅
                                                <div id="up-down-icon" onclick="showHSDownList(this)"></div>
                                            </th>
                                        </tr>
                                    </table>
                                    <table class="changeRankingTable" id="hsChgDownListTable" style="display: none">
                                        <tr class="tableHead">
                                            <th>股票名</th>
                                            <th>当前价</th>
                                            <th id="changeRankTh1">涨跌幅
                                                <div id="up-down-icon1" onclick="showHSUpList(this)"></div>
                                            </th>
                                        </tr>
                                    </table>
                                </div>
                                <div class="tab-pane" id="SS" style="padding-top: 10px">
                                    <table class="changeRankingTable" id="ssChgUpListTable">
                                        <tr class="tableHead">
                                            <th>股票名</th>
                                            <th>当前价</th>
                                            <th id="changeRankTh2">涨跌幅
                                                <div id="up-down-icon2" onclick="showSSDownList(this)"></div>
                                            </th>
                                        </tr>
                                    </table>

                                    <table class="changeRankingTable" id="ssChgDownListTable" style="display: none">
                                        <tr class="tableHead">
                                            <th>股票名</th>
                                            <th>当前价</th>
                                            <th id="changeRankTh3">涨跌幅
                                                <div id="up-down-icon3" onclick="showSSUpList(this)"></div>
                                            </th>
                                        </tr>
                                    </table>
                                </div>
                                <%--<div class="tab-pane fade" id="GG">--%>

                                <%--</div>--%>

                            </div>
                        </div>

                    </div>
                    <div id="volumeRankingDiv">
                        <p class="rankingDivHead">成交量排行</p>
                        <div>
                            <ul class="nav nav-tabs">
                                <li class="active"><a href="#HS1" data-toggle="tab">沪市</a></li>
                                <li><a href="#SS1" data-toggle="tab">深市</a></li>
                                <%--<li><a href="#GG1" data-toggle="tab">港股</a></li>--%>
                            </ul>
                            <div class="tab-content">
                                <div class="tab-pane fade in active" id="HS1" style="padding-top: 10px">
                                    <table class="changeRankingTable" id="hsVolumeTable">
                                        <tr class="tableHead">
                                            <th>股票名</th>
                                            <th>当前价</th>
                                            <th>涨跌幅</th>
                                            <th>成交量(亿)</th>
                                        </tr>
                                    </table>

                                </div>
                                <div class="tab-pane fade" id="SS1" style="padding-top: 10px">
                                    <table class="changeRankingTable" id="ssVolumeTable">
                                        <tr class="tableHead">
                                            <th>股票名</th>
                                            <th>当前价</th>
                                            <th>涨跌幅</th>
                                            <th>成交量(亿)</th>
                                        </tr>
                                    </table>
                                </div>
                                <%--<div class="tab-pane fade" id="GG1">--%>

                                <%--</div>--%>

                            </div>
                        </div>


                    </div>
                    <div id="turnOverRankingDiv">
                        <p class="rankingDivHead">成交额排行</p>
                        <div>
                            <ul class="nav nav-tabs">
                                <li class="active"><a href="#HS2" data-toggle="tab">沪市</a></li>
                                <li><a href="#SS2" data-toggle="tab">深市</a></li>
                                <%--<li><a href="#GG1" data-toggle="tab">港股</a></li>--%>
                            </ul>
                            <div class="tab-content">
                                <div class="tab-pane fade in active" id="HS2" style="padding-top: 10px">
                                    <table class="changeRankingTable" id="hsAmountTable">
                                        <tr class="tableHead">
                                            <th>股票名</th>
                                            <th>当前价</th>
                                            <th>涨跌幅</th>
                                            <th>成交额(亿)</th>
                                        </tr>
                                    </table>

                                </div>
                                <div class="tab-pane fade" id="SS2" style="padding-top: 10px">
                                    <table class="changeRankingTable" id="ssAmountTable">
                                        <tr class="tableHead">
                                            <th>股票名</th>
                                            <th>当前价</th>
                                            <th>涨跌幅</th>
                                            <th>成交额(亿)</th>
                                        </tr>
                                    </table>
                                </div>
                                <%--<div class="tab-pane fade" id="GG1">--%>

                                <%--</div>--%>

                            </div>

                        </div>
                    </div>

                </div>

                <div id="heatRankingDiv" style="height: auto">
                    <div style="display:inline-block;zoom: 1">
                        <p class="divHead">热度排行榜</p>
                    </div>
                    <div style="display:inline-block;zoom: 1;margin-left: 10px">
                        <img src="${pageContext.request.contextPath}/css/pic/question.png" id="heatRankingIntro"
                             width="25"
                             style="vertical-align: sub;outline: none;">
                    </div>
                    <div class="splitLine"></div>
                    <div class="mdl-tooltip" for="heatRankingIntro">
                        <span>什么股票最热⻔门？我们根据涨跌幅、成交量和成交额计算综合评分帮您寻找。</span>
                    </div>

                    <div class="loadingDiv" align="center" style="margin-top: 80px">
                        <img src="${pageContext.request.contextPath}/css/pic/loading.gif" width="60">
                    </div>

                    <div id="heatRankingContent" style="padding: 10px;display: none">
                        <table id="heatRankingTable" style="border-spacing: 8.5px;border-collapse: inherit;width: 100%">
                            <tr class="tableHead">
                                <th style="text-align: center">综合排行</th>
                                <th>股票名</th>
                                <th>当前价</th>
                                <th>涨跌幅</th>
                                <th>成交额(亿)</th>
                                <th>成交量(亿)</th>
                                <th>涨跌幅排行</th>
                                <th>成交额排行</th>
                                <th>成交量排行</th>
                                <th>综合指标</th>
                            </tr>
                        </table>
                    </div>
                </div>

                <div id="PlateInfoDiv" style="height: auto">
                    <div style="display:inline-block;zoom: 1">
                        <p class="divHead">行业关系图</p>
                    </div>
                    <div style="display:inline-block;zoom: 1;margin-left: 10px">
                        <img src="${pageContext.request.contextPath}/css/pic/question.png" id="industryIntro" width="25"
                             style="vertical-align: sub;outline: none;">
                    </div>
                    <div class="splitLine"></div>

                    <div class="mdl-tooltip" for="industryIntro">
                        <span>哪个行业才是市场最强推动力？
                            <br>我们通过计算分析，为您找到了近期市场的中心行业（处于图中心）、次中心行业（与中心点相邻）和边缘行业（仅与一点相邻，供您参考。选择中心行业，会是您投资的稳健选择。
                        <br>行业整体怎么样？
                            <br>试着点下上图中代表行业的小圆，就可以看到行业与板块的指数对比。
                        </span>
                    </div>

                    <div id="industryTreeDiv" style="width: 100%;height: 600px"></div>

                </div>

                <div id="IndustryInfoDiv" style="height: auto">
                    <div style="display:inline-block;zoom: 1">
                        <p class="divHead">行业大盘对比图</p>
                    </div>
                    <%--<div style="display:inline-block;zoom: 1;margin-left: 10px">--%>
                    <%--<img src="${pageContext.request.contextPath}/css/pic/question.png" id="industryAndPlateIntro"--%>
                    <%--width="25"--%>
                    <%--style="vertical-align: sub;outline: none;">--%>
                    <%--</div>--%>
                    <div class="splitLine"></div>

                    <%--<div class="mdl-tooltip" for="industryAndPlateIntro">--%>
                    <%--<span>点击可以查看行业信息</span>--%>
                    <%--</div>--%>

                    <div id="industryDiv" style="width: 66%;height: 600px;display: inline-block"></div>
                    <div id="industryInfoTable"
                         style="width: 28%;height: 600px;display: inline-block;vertical-align: top;overflow: auto">
                        <div style="display: inline-block;vertical-align: top">
                            <div style="width: auto">
                                <div style="display:inline-block;">
                                    <select id="search_ui" style="width:200px;outline: none;height: 40px"
                                            onchange='getAllStockInIndustry(this.value);'>
                                        <!-- Dropdown List Option -->
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div id="loadingDiv" align="left" style="display: none;margin-left: 20px">
                            <div style="width: 40px">
                                <div>
                                    <img src="${pageContext.request.contextPath}/css/pic/loading.gif" width="40">
                                </div>
                            </div>
                        </div>
                        <table id="industryTable"
                               style="border-spacing: 8.5px;border-collapse: inherit;width: 100%;">
                            <tr class="tableHead">
                                <th>股票号</th>
                                <th>股票名</th>
                                <th>当前价</th>
                                <th>涨跌幅</th>
                            </tr>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    $(document).ready(function () {
        content = [];
        $.ajax({
            url: "getMarketInfo.action",
            type: 'POST',
            dataType: "json",
            success: function (data) {
                var jsonObj = eval('(' + data + ')');
                setMarketInfo(jsonObj);
                var json = splitData(jsonObj)
                drawIndustryAndPlate(json, 0);
            },
            error: function () {
                fail_prompt("ajax error", 1500);
            }

        });
        if (window.localStorage.getItem("industryName") == null) {
            $.ajax({
                url: "getAllIndustryName.action",
                type: 'POST',
                dataType: "json",
                success: function (data) {
                    var jsonObj = eval('(' + data + ')');
                    addAllIndustryToDownList(jsonObj["list"])
                    var allName = '';
                    for (var i = 0; i < jsonObj["list"].length; i++) {
                        allName += jsonObj["list"][i] + ";";
                    }
                    window.localStorage.setItem("industryName", allName);
                },
                error: function () {
                    fail_prompt("ajax error", 1500);
                }

            });
        } else {
            addAllIndustryToDownList(window.localStorage.getItem("industryName").split(";"))
        }


        function addAllIndustryToDownList(data) {
            for (var i = 0; i < data.length; i++) {
                var row = data[i];
                content.push(row);
//            console.log(row);
            }

            $("#search_ui").select2({
                data: content
            });

            $('.select2-selection__rendered').text('请输入行业名称');

        }
    });

    function splitData(jsonObj) {
        var sza = [];
        var zx = [];
        var cy = [];
        var hs = [];
        var szc = [];
        var sz = [];
        var date = [];
        for (var i = 2; i < jsonObj.length; i++) {
            if (jsonObj[i]["SZA"] != null) {
                sza.push((jsonObj[i]["SZA"] * 100).toFixed(3));
                date.push(jsonObj[i]["SZADate"])
            } else if (jsonObj[i]["ZX"] != null) {
                zx.push((jsonObj[i]["ZX"] * 100).toFixed(3));
            } else if (jsonObj[i]["CY"] != null) {
                cy.push((jsonObj[i]["CY"] * 100).toFixed(3));
            } else if (jsonObj[i]["HS"] != null) {
                hs.push((jsonObj[i]["HS"] * 100).toFixed(3));
            } else if (jsonObj[i]["SZC"] != null) {
                szc.push((jsonObj[i]["SZC"] * 100).toFixed(3));
            } else if (jsonObj[i]["SZ"] != null) {
                sz.push((jsonObj[i]["SZ"] * 100).toFixed(3));
            }
        }

        return {
            sza: sza,
            zx: zx,
            cy: cy,
            hs: hs,
            szc: szc,
            sz: sz,
            date: date
        }
    }

    function drawIndustryAndPlate(json, flag, name, y) {
        if (flag == 0) {
            myChart = echarts.init(document.getElementById("industryDiv"));
            option1 = {
                color: ['#A19FBA', '#B39EBA', '#BA95A2', '#BAB0A0', '#B0BAA4', '#A1BAA3', '#A3BAB6'],
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'cross',
                        crossStyle: {
                            color: '#999'
                        }
                    }
                },
                dataZoom: [
                    {
                        start: 95,
                        end: 100,
                    }, {
                        type: 'inside'
                    }
                ],
                legend: {
                    data: ['上证指数', '中小板指', '创业板指', '沪深300', '深证成指', '深证A指'],
                    selected: {
                        '上证指数': true,
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
                        data: json.date,
                        axisPointer: {
                            type: 'shadow'
                        }
                    }
                ],
                yAxis: [
                    {
                        type: 'value',
                        name: '收益率(%)',
                        nameGap: 5,
                        axisLabel: {
                            formatter: '{value} '
                        }
                    }
                ],
                series: [
                    {
                        name: '深证A指',
                        type: 'line',
                        data: json.sza,
                        itemStyle: {
                            normal: {
                                color: "#A2A7C3"
                            }
                        }
                    }, {
                        name: '中小板指',
                        type: 'line',
                        data: json.zx,
                        itemStyle: {
                            normal: {
                                color: "#C3A9C1"
                            }
                        }
                    }, {
                        name: '创业板指',
                        type: 'line',
                        data: json.cy,
                        itemStyle: {
                            normal: {
                                color: "#C3A9AE"
                            }
                        }
                    }, {
                        name: '沪深300',
                        type: 'line',
                        data: json.hs,
                        itemStyle: {
                            normal: {
                                color: "#B5C3B5"
                            }
                        }
                    }, {
                        name: '深证成指',
                        type: 'line',
                        data: json.szc,
                        itemStyle: {
                            normal: {
                                color: "#74a57e"
                            }
                        }
                    }, {
                        name: '上证指数',
                        type: 'line',
                        data: json.sz,
                        itemStyle: {
                            normal: {
                                color: "#A7C3BD"
                            }
                        }
                    }
                ]
            }
            myChart.setOption(option1);
        } else {
            var item = {
                name: name,
                type: 'line',
                data: y
            }
            option1.series.push(item)
            option1.legend.data.push(name)
            myChart.setOption(option1);
        }


    }

    function goToStockInfo(stockID) {
        console.log(stockID)
        window.open("../jsp/stockInfo.jsp?stockName=" + stockID);
    }

    function showHSDownList(button) {
        $("#hsChgUpListTable").hide();
        $("#hsChgDownListTable").show();
        button.style.background = "url('../css/pic/down.png') center no-repeat"
    }

    function showHSUpList(button) {
        $("#hsChgDownListTable").hide();
        $("#hsChgUpListTable").show();
        button.style.background = "url('../css/pic/up.png') center no-repeat"
    }

    function showSSDownList(button) {
        $("#ssChgUpListTable").hide();
        $("#ssChgDownListTable").show();
        button.style.background = "url('../css/pic/down.png') center no-repeat"
    }

    function showSSUpList(button) {
        $("#ssChgDownListTable").hide();
        $("#ssChgUpListTable").show();
        button.style.background = "url('../css/pic/up.png') center no-repeat"
    }

    function setMarketInfo(data) {

        var csi_chg = data[0]["沪深300"]["change"]
        var zxe_chg = data[0]["中小板指"]["change"]
        var gei_chg = data[0]["创业板指"]["change"]
        var sse_chg = data[0]["上证指数"]["change"]


        if (csi_chg > 0) {
            $("#index_CSI300").css("color", "#cd8585");
            $("#chg_CSI").css("color", "#cd8585");
            $("#chg_CSI").html("(+" + csi_chg + "%)")
        } else {
            $("#index_CSI300").css("color", "#74a57e");
            $("#chg_CSI").css("color", "#74a57e");
            $("#chg_CSI").html("(" + csi_chg + "%)")
        }

        if (zxe_chg > 0) {
            $("#index_ZXE").css("color", "#cd8585");
            $("#chg_ZXE").css("color", "#cd8585");
            $("#chg_ZXE").html("(+" + zxe_chg + "%)")
        } else {
            $("#index_ZXE").css("color", "#74a57e");
            $("#chg_ZXE").css("color", "#74a57e");
            $("#chg_ZXE").html("(" + zxe_chg + "%)")
        }

        if (gei_chg > 0) {
            $("#index_GEI").css("color", "#cd8585");
            $("#chg_GEI").css("color", "#cd8585");
            $("#chg_GEI").html("(+" + gei_chg + "%)")
        } else {
            $("#index_GEI").css("color", "#74a57e");
            $("#chg_GEI").css("color", "#74a57e");
            $("#chg_GEI").html("(" + gei_chg + "%)")
        }

        if (sse_chg > 0) {
            $("#index_SSE").css("color", "#cd8585");
            $("#chg_SSE").css("color", "#cd8585");
            $("#chg_SSE").html("(" + sse_chg + "%)")
        } else {
            $("#index_SSE").css("color", "#74a57e");
            $("#chg_SSE").css("color", "#74a57e");
            $("#chg_SSE").html("(" + sse_chg + "%)")
        }
        $("#index_CSI300").html(data[0]["沪深300"]["preclosePrice"])
        $("#index_ZXE").html(data[0]["中小板指"]["preclosePrice"])
        $("#index_GEI").html(data[0]["创业板指"]["preclosePrice"])
        $("#index_SSE").html(data[0]["上证指数"]["preclosePrice"])


        $("#marketDate").html(data[0]["date"]);
        $("#marketTemp").html(data[1]["temperature"] + "℃");
        $("#totalVolume").html(data[1]["totalVolume"]);
        $("#totalNum").html(data[1]["totalNum"]);
        $("#openDown5perStock").html(data[1]["openDown5per"]);
        $("#openUp5perStock").html(data[1]["openUp5per"]);
        $("#up5perStock").html(data[1]["up5per"]);
        $("#down5perStock").html(data[1]["down5per"]);
        $("#limitUpStock").html(data[1]["limitUp"]);
        $("#limitDownStock").html(data[1]["limitDown"]);

        $("#hsChgUpListTable tr:not(:first)").empty();
        addToChgTable(data[0]["ssUpList"], "hsChgUpListTable", "+");
        addToChgTable(data[0]["szUpList"], "ssChgUpListTable", "+");

        addToChgTable(data[0]["ssDownList"], "hsChgDownListTable", "");
        addToChgTable(data[0]["szDownList"], "ssChgDownListTable", "");

        $("#hsVolumeTable tr:not(:first)").empty();
        addToVolumeAmountTable(data[0]["ssVolume"], "hsVolumeTable", "volume");
        addToVolumeAmountTable(data[0]["szVolume"], "ssVolumeTable", "volume");

        $("#hsAmountTable tr:not(:first)").empty();
        addToVolumeAmountTable(data[0]["ssAmount"], "hsAmountTable", "amount");
        addToVolumeAmountTable(data[0]["szAmount"], "ssAmountTable", "amount");

        var hotData = data[0]["热股"];
        for (var i = 0; i < 12; i++) {
            var insertTr = document.getElementById("heatRankingTable").insertRow(i + 1);
            insertTr.className = "tableContent";

            var chg = hotData[i]["stockRealTimePO"]["changepercent"];
            var insertTd = insertTr.insertCell(0);
            insertTd.innerHTML = hotData[i]["comprehensiveRank"];
            if (i < 3) {
                insertTd.style.color = "#cd8585";
                insertTd.style.fontWeight = 500;
            }
            insertTd.style.textAlign = "center";
            var insertTd = insertTr.insertCell(1);
            insertTd.innerHTML = "<span onclick='goToStockInfo(this.title)' style='cursor: pointer' title=" + hotData[i]['stockRealTimePO']['stockId'] + ">" + hotData[i]['stockRealTimePO']['stockName'] + "</span>";
            var insertTd = insertTr.insertCell(2);
            insertTd.innerHTML = hotData[i]["stockRealTimePO"]["trade"];
            if (chg > 0) {
                insertTd.style.color = "#cd8585";
            } else {
                insertTd.style.color = "#74a57e";
            }
            var insertTd = insertTr.insertCell(3);
            if (chg > 0) {
                insertTd.style.color = "#cd8585";
                insertTd.innerHTML = "+" + (chg * 100).toFixed(2) + "%";
            } else {
                insertTd.style.color = "#74a57e";
                insertTd.innerHTML = (chg * 100).toFixed(2) + "%";
            }
            var insertTd = insertTr.insertCell(4);
            insertTd.innerHTML = (hotData[i]["stockRealTimePO"]["amount"] / 100000000).toFixed(2);
            var insertTd = insertTr.insertCell(5);
            insertTd.innerHTML = (hotData[i]["stockRealTimePO"]["volume"] / 100000000).toFixed(2);
            var insertTd = insertTr.insertCell(6);
            insertTd.innerHTML = hotData[i]["changeRank"];
            var insertTd = insertTr.insertCell(7);
            insertTd.innerHTML = hotData[i]["amountRank"];
            var insertTd = insertTr.insertCell(8);
            insertTd.innerHTML = hotData[i]["volumeRank"];

            var insertTd = insertTr.insertCell(9);
            insertTd.innerHTML = hotData[i]["comprehensiveRate"].toFixed(2);
        }

        $(".loadingDiv").hide();
        $("#marketOverviewContent").fadeIn();
        $("#quotesRankingContent").fadeIn();
        $("#heatRankingContent").fadeIn();

    }

    $(document).ready(function () {
        $.ajax({
            url: "getIndustryTree.action",
            type: 'POST',
            dataType: 'json',
            success: function (data) {
                var jsonObj = eval('(' + data + ')');
                var json = splitGraphDiv(jsonObj);
                getIndustryTree(json);
            },
            error: function () {
                fail_prompt("ajax error", 1500)
            }
        })
    });

    function splitGraphDiv(rawData) {
        var nodes = [];
        var links = [];

        var industry = [];

        for (var i = 0; i < rawData.length; i++) {
            if (rawData[i]["edge"] != null) {
                links.push({
                    'source': rawData[i]["edge"]["industryA_id"],
                    'target': rawData[i]["edge"]["industryB_id"],
                    'name': rawData[i]["edge"]["weight"].toFixed(2)
                })

            } else if (rawData[i]["id"] != null) {
                nodes.push({
                    'id': i,
                    'name': rawData[i]["id"]
                })
            }
        }
        return {
            links: links,
            nodes: nodes
        }
    }

    function getIndustryTree(data) {
        var myChart = echarts.init(document.getElementById("industryTreeDiv"));
        myChart.setOption(option = {
            animation: false,
            tooltip: {
                show: false
            },
            legend: [{
                data: function () {
                    return data.nodes.name;
                }
            }],
            series: [
                {
                    height: 600,
                    name: '行业关系图',
                    type: 'graph',
                    layout: 'force',
                    data: data.nodes,
                    links: data.links,
//                        categories: categories,
                    roam: true,
                    label: {
                        normal: {
                            position: 'right',
                            textStyle: {
                                fontSize: 22
                            }
                        }
                    },
                    itemStyle: {
                        normal: {color: "#cd8585"}
                    },
                    force: {
                        repulsion: 80
                    },
                    animation: false,
                    edgeLabel: {
                        normal: {
                            show: true,
                            formatter: function (x) {
                                return x.data.name
                            }
                        }
                    }
                }
            ]
        });

        myChart.on("click", function (params) {
            if (params.seriesType === 'graph') {
                if (params.dataType === 'node') {
                    getAllStockInIndustry(params.name);
                    scrollIntoView('#IndustryInfoDiv')
                }
            }
        })
    }

    function getAllStockInIndustry(name) {
        $("#loadingDiv").css("display", "inline-block");
        $('.select2-selection__rendered').text(name);
        $.ajax({
            url: "getStockInIndustry.action",
            type: 'POST',
            dataType: 'json',
            data: {
                name: name
            },
            success: function (data) {
                var jsonObj = eval('(' + data + ')');
                var y = [];
                for (var i = 1; i < jsonObj.length; i++) {
                    y.push((jsonObj[i]["value"] * 100).toFixed(2))
                }

                drawIndustryAndPlate(jsonObj, 1, name, y)
                addToIndustryTable(jsonObj[0], name);

                $("#loadingDiv").hide();
            }
            ,
            error: function () {
                fail_prompt("ajax error", 1500)
            }
        })
    }

    function addToIndustryTable(data, name) {
        $("#industryTable tr:not(:first)").empty();
        var i = 0;
        for (var key in data) {
            var insertTr = document.getElementById('industryTable').insertRow(i + 1);
            insertTr.className = "tableContent";
            var insertTd = insertTr.insertCell(0);
            insertTd.innerHTML = "<span style='cursor: pointer' onclick='goToStockInfo(this.innerHTML)'>" + key + "</span>";
            var insertTd = insertTr.insertCell(1);
            insertTd.innerHTML = data[key]["stockName"]
            var insertTd = insertTr.insertCell(2);
            insertTd.innerHTML = data[key]["trade"];
            var insertTd = insertTr.insertCell(3);
            var chg = data[key]["changepercent"];
            if (chg < 0) {
                insertTd.style.color = "#74a57e";
                insertTd.innerHTML = (chg * 100).toFixed(2) + "%";
            } else {
                insertTd.style.color = "#cd8585";
                insertTd.innerHTML = "+" + (chg * 100).toFixed(2) + "%";
            }

            i++;
        }
    }

    function addToChgTable(data, tableID, flag) {
        for (var i = 0; i < 12; i++) {
            var insertTr = document.getElementById(tableID).insertRow(i + 1);
            insertTr.className = "tableContent";
            var insertTd = insertTr.insertCell(0);
            insertTd.innerHTML = "<span onclick='goToStockInfo(this.title)'  style='cursor: pointer' title=" + data[i]['stockId'] + ">" + data[i]['stockName'] + "</span>";
            var insertTd = insertTr.insertCell(1);
            if (flag == "") {
                insertTd.style.color = "#74a57e";
            } else {
                insertTd.style.color = "#cd8585";
            }
//                insertTd.style.color = "#cd8585";
            insertTd.innerHTML = data[i]["trade"];
            var insertTd = insertTr.insertCell(2);
            if (flag == "") {
                insertTd.style.color = "#74a57e";
            } else {
                insertTd.style.color = "#cd8585";
            }
            insertTd.innerHTML = flag + (data[i]["changepercent"] * 100).toFixed(2) + "%";
        }
    }

    function addToVolumeAmountTable(data, tableID, type) {
        for (var i = 0; i < 12; i++) {
            var insertTr = document.getElementById(tableID).insertRow(i + 1);
            insertTr.className = "tableContent";
            var insertTd = insertTr.insertCell(0);
            insertTd.innerHTML = "<span onclick='goToStockInfo(this.title)'  style='cursor: pointer' title=" + data[i]['stockId'] + ">" + data[i]['stockName'] + "</span>";
            var insertTd = insertTr.insertCell(1);
            insertTd.innerHTML = data[i]["trade"];
            if (data[i]["changepercent"] > 0) {
                insertTd.style.color = "#cd8585";
            } else {
                insertTd.style.color = "#74a57e";
            }
            var insertTd = insertTr.insertCell(2);
            var chg = data[i]["changepercent"];
            if (chg > 0) {
                insertTd.innerHTML = "+" + (chg * 100).toFixed(2) + "%";
                insertTd.style.color = "#cd8585";
            } else {
                insertTd.innerHTML = (chg * 100).toFixed(2) + "%";
                insertTd.style.color = "#74a57e";
            }
            var insertTd = insertTr.insertCell(3);
            insertTd.innerHTML = (data[i][type] / 100000000).toFixed(2);
        }
    }

    function scrollIntoView(id) {
        $(id)[0].scrollIntoView({
            behavior: "smooth"
        })
    }

    function scrollIntoOverview() {
        $("#marketOverviewDiv")[0].scrollIntoView({
            behavior: "smooth"
        })
    }

    function scrollIntoChangeRanking() {
        $("#quotesRankingDiv")[0].scrollIntoView({
            behavior: "smooth"
        })
    }

    function scrollIntoHeatRanking() {
        $("#heatRankingDiv")[0].scrollIntoView({
            behavior: "smooth"
        })
    }

    function scrollIntoPlateInfo() {
        $("#PlateInfoDiv")[0].scrollIntoView({
            behavior: "smooth"
        })
    }

</script>

</body>

<%@ include file="pageFoot.jsp" %>

</html>
