<%--
  Created by IntelliJ IDEA.
  User: st
  Date: 2017/5/12
  Time: 16:06
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="navBar.jsp" %>

<html>
<head>
    <title>股票信息</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <script src="${pageContext.request.contextPath}/js/getStockInfo.js"></script>
    <script src="${pageContext.request.contextPath}/js/multifactorialSelection.js"></script>
    <script src="${pageContext.request.contextPath}/js/marketInfo.js"></script>

    <%--<script src="${pageContext.request.contextPath}/js/jquery-3.2.1.min.js"></script>--%>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/homePage.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/backTest.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/DIYStrategy.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/stockInfo.css">

    <script>
        $(document).ready(function () {
            var thisURL = document.URL;
            var getval = thisURL.split('?')[1];
            if (getval != null) {
                var showval = getval.split("=")[1];
                console.log(showval);
                $("#quotes").removeClass("active");
                $("#search").addClass("in active");
                $('#select2-search_ui-container.select2-selection__rendered').text(showval);
                searchStock(showval);
            } else {
                $("#quotes").addClass("active");
            }
        });

        function drawAllDateAnaChart(data) {
            var mychart = echarts.init(document.getElementById('allAna_charts'));
            mychart.setOption(option = {
                backgroundColor: '#FFFFFF',
                legend: {
                    data: ['kdj', 'boll', 'macd', 'dmi'],
                    align: 'left',
                    left: 10
                },
                brush: {
                    toolbox: ['rect', 'polygon', 'lineX', 'lineY', 'keep', 'clear'],
                    xAxisIndex: 0
                },
                toolbox: {
                    feature: {
                        magicType: {
                            type: ['stack', 'tiled']
                        },
                        dataView: {}
                    }
                },
                tooltip: {},
                xAxis: {
                    data: data.allAnaDate,
                    name: 'X Axis',
                    silent: false,
                    axisLine: {onZero: true},
                    splitLine: {show: false},
                    splitArea: {show: false}
                },
                yAxis: {
                    name: ">0为买入点，<0为卖出点",
                    inverse: false,
                    splitArea: {show: false}
                },
                grid: {
                    left: 100
                },
                dataZoom: [
                    {
                        start: 95,
                        end: 100,
                    }, {
                        type: 'inside'
                    }
                ],
                series: [
                    {
                        name: 'kdj',
                        type: 'bar',
                        stack: 'one',
                        itemStyle: {
                            normal: {
                                color: '#cd8585'
                            }
                        },
                        data: data.kdjList
                    },
                    {
                        name: 'boll',
                        type: 'bar',
                        stack: 'one',
                        itemStyle: {
                            normal: {
                                color: '#74a57e'
                            }
                        },
                        data: data.bollList
                    },
                    {
                        name: 'macd',
                        type: 'bar',
                        stack: 'one',
                        itemStyle: {
                            normal: {
                                color: '#F5C07D'
                            }
                        },
                        data: data.macdList
                    },
                    {
                        name: 'dmi',
                        type: 'bar',
                        stack: 'one',
                        itemStyle: {
                            normal: {
                                color: '#A9CCF5'
                            }
                        },
                        data: data.dmiList
                    }
                ]
            });
        }

        function drawInnerValue(data) {
            var myChart = echarts.init(document.getElementById('innerValue_charts'));
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
                    data: ['综合评价参数']
                },
                xAxis: [
                    {
                        type: 'category',
                        data: data.innerValueDate,
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
                        name: '综合评价参数',
                        type: 'line',
                        data: data.innerValue,
                        itemStyle: {
                            normal: {
                                color: "#cd8585"
                            }
                        }
                    }
                ]
            })
        }

        function drawDMI(data) {
            var myChart = echarts.init(document.getElementById('dmi_charts'));

            var markPointData = [];

            console.log(data.dmiAna);
            for (var i = 0; i < data.dmiDate.length; i++) {
                if (data.dmiAna[i] == "买入") {
                    markPointData.push({
                        itemStyle: {
                            normal: {
                                color: "#cd8585"
                            }
                        },
                        name: data.dmiAna[i],
                        xAxis: data.dmiDate[i],
                        yAxis: 0
                    });
                } else {
                    markPointData.push({
                        itemStyle: {
                            normal: {
                                color: "#74a57e"
                            }
                        },
                        name: data.dmiAna[i],
                        xAxis: data.dmiDate[i],
                        yAxis: 0
                    });
                }
            }
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
                        end: 100
                    }, {
                        type: 'inside'
                    }
                ],
                legend: {
                    data: ['adx', 'adxr', 'di+', 'di-']
                },
                xAxis: [
                    {
                        type: 'category',
                        data: data.dates,
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
                        name: 'adx',
                        type: 'line',
                        data: data.adx,
                        itemStyle: {
                            normal: {
                                color: "#cd8585"
                            }
                        },
                        markPoint: {
                            symbolSize: 40,       // 标注大小，半宽（半径）参数，当图形为方向或菱形则总宽度为symbolSize * 2

                            effect: {
                                show: true,
                                shadowBlur: 0
                            },
                            label: {
                                normal: {
                                    show: true,
                                    formatter: function (param) {
                                        return param.name;
                                    }
                                }
                            },
                            data: markPointData
                        }
                    },
                    {
                        name: 'adxr',
                        type: 'line',
                        data: data.adxr,
                        itemStyle: {
                            normal: {
                                color: "#74a57e"
                            }
                        }
                    }, {
                        name: 'di+',
                        type: 'line',
                        data: data.diPlus,
                        itemStyle: {
                            normal: {
                                color: "#A9CCF5"
                            }
                        }
                    }, {
                        name: 'di-',
                        type: 'line',
                        data: data.diMinus,
                        itemStyle: {
                            normal: {
                                color: "#F5C07D"
                            }
                        }
                    }
                ]
            })

        }

        function drawMACD(data) {
            var myChart = echarts.init(document.getElementById('macd_charts'));
            var markPointData = [];
            for (var i = 0; i < data.macdDate.length; i++) {
                if (data.macdAna[i].split(";")[1] == "买进") {
                    markPointData.push({
                        itemStyle: {
                            normal: {
                                color: "#cd8585"
                            }
                        },
                        name: data.macdAna[i].split(";")[1],
                        xAxis: data.macdDate[i],
                        yAxis: 0
                    });
                } else {
                    markPointData.push({
                        itemStyle: {
                            normal: {
                                color: "#74a57e"
                            }
                        },
                        name: data.macdAna[i].split(";")[1],
                        xAxis: data.macdDate[i],
                        yAxis: 0
                    });
                }
            }
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
                    data: ['macd', 'dif', 'dea']
                },
                xAxis: [
                    {
                        type: 'category',
                        data: data.dates,
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
                        name: 'macd',
                        type: 'bar',
                        data: data.macd,
                        itemStyle: {
                            normal: {
                                color: function (data) {
                                    if (data.value < 0) {
                                        return '#74a57e'
                                    } else
                                        return '#cd8585'
                                }
                            }

                        },
                        markPoint: {
                            symbolSize: 40,       // 标注大小，半宽（半径）参数，当图形为方向或菱形则总宽度为symbolSize * 2

                            effect: {
                                show: true,
                                shadowBlur: 0
                            },
                            label: {
                                normal: {
                                    show: true,
                                    formatter: function (param) {
                                        return param.name;
                                    }
                                }
                            },
                            data: markPointData
                        }
                    },
                    {
                        name: 'dif',
                        type: 'line',
                        data: data.dif,
                        itemStyle: {
                            normal: {
                                color: "#A9CCF5"
                            }
                        }
                    },
                    {
                        name: 'dea',
                        type: 'line',
                        data: data.dea,
                        itemStyle: {
                            normal: {
                                color: "#F5C07D"
                            }
                        }
                    }
                ]
            })

        }

        function drawKDJ(data) {
            var myChart = echarts.init(document.getElementById('kdj_charts'));

            var markPointData = [];


            for (var i = 0; i < data.kdjDate.length; i++) {
                if (data.kdjAna[i].split(";")[1] == "买进") {
                    markPointData.push({
                        itemStyle: {
                            normal: {
                                color: "#cd8585"
                            }
                        },
                        name: data.kdjAna[i].split(";")[1],
                        xAxis: data.kdjDate[i],
                        yAxis: 0
                    });
                } else {
                    markPointData.push({
                        itemStyle: {
                            normal: {
                                color: "#74a57e"
                            }
                        },
                        name: data.kdjAna[i].split(";")[1],
                        xAxis: data.kdjDate[i],
                        yAxis: 0
                    });
                }
            }
            myChart.setOption(option = {
                tooltip: {
                    trigger: 'axis'
                }, dataZoom: [
                    {
                        start: 95,
                        end: 100,
                    }, {
                        type: 'inside'
                    }
                ],
                legend: {
                    data: ['k', 'd', 'j']
                },
                grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '3%',
                    containLabel: true
                },
                toolbox: {
                    feature: {
                        saveAsImage: {}
                    }
                },
                xAxis: {
                    type: 'category',
                    boundaryGap: false,
                    data: data.dates
                },
                yAxis: {
                    type: 'value'
                },
                series: [
                    {
                        name: 'k',
                        type: 'line',
                        data: data.k,
                        symbol: 'none',
                        smooth: true,
                        lineStyle: {
                            normal: {opacity: 0.5}
                        },
                        markLine: {
                            data: [{
                                label: {
                                    normal: {
                                        formatter: 'k超买'
                                    }
                                },
                                name: 'k线超买区',
                                yAxis: 90
                            }, {
                                label: {
                                    normal: {
                                        formatter: 'k超卖'
                                    }
                                },
                                name: 'k线超卖区',
                                yAxis: 10
                            }]
                        },
                        markPoint: {
                            symbolSize: 40,       // 标注大小，半宽（半径）参数，当图形为方向或菱形则总宽度为symbolSize * 2

                            effect: {
                                show: true,
                                shadowBlur: 0
                            },
                            label: {
                                normal: {
                                    show: true,
                                    formatter: function (param) {
                                        return param.name;
                                    }
                                }
                            },
                            data: markPointData
                        }


                    },
                    {
                        name: 'd',
                        type: 'line',
                        data: data.d,
                        symbol: 'none',
                        smooth: true,
                        lineStyle: {
                            normal: {opacity: 0.5}
                        },
                        markLine: {
                            data: [{
                                label: {
                                    normal: {
                                        formatter: 'd超买'
                                    }
                                },
                                name: 'd线超买区',
                                yAxis: 80
                            }, {
                                label: {
                                    normal: {
                                        formatter: 'd超卖'
                                    }
                                },
                                name: 'd线超卖区',
                                yAxis: 20
                            }]
                        }
                    },
                    {
                        name: 'j',
                        type: 'line',
                        data: data.j,
                        symbol: 'none',
                        smooth: true,
                        lineStyle: {
                            normal: {opacity: 0.5}
                        },
                        markLine: {
                            data: [{
                                label: {
                                    normal: {
                                        formatter: 'j超买'
                                    }
                                },
                                name: 'j线超买区',
                                yAxis: 100
                            }, {
                                label: {
                                    normal: {
                                        formatter: 'j超卖'
                                    }
                                },
                                name: 'j线超卖区',
                                yAxis: 0
                            }]
                        }
                    }
                ]
            })
        }

        function drawFluctuationChart(data) {
//            $.getJSON('stock-calendar.json', function (data) {
            console.log(data);
            var chart = new G2.Chart({
                id: 'fluctuation_charts',
                forceFit: false,
                width: 800,
                height: 750,
                plotCfg: {
                    margin: [20, 80, 20, 40]
                }
            });

            // 获取当前月的第几周,从 0 开始
            function getMonthWeek(date) {
                var year = date.getFullYear();
                var month = date.getMonth();
                var monthFirst = new Date(year, month, 0);
                var intervalDays = Math.round((date.getTime() - monthFirst.getTime()) / 86400000);
                var index = Math.round((intervalDays + monthFirst.getDay()) / 7);
                return index;
            }

            // 加工数据
            // 增加涨幅、跌幅
            // 添加所属月、周几、每个月的第几周
            data.forEach(function (obj) {
                var date = new Date(obj['日期']);
                var month = date.getMonth();
                obj.month = month;
                obj.day = date.getDay();
                obj.week = getMonthWeek(date).toString();
            });
            // 对数据进行排序
            var Frame = G2.Frame;
            var frame = new Frame(data);
            frame = Frame.sort(frame, 'day');
            console.log(data);
            var defs = {
                month: {
                    type: 'cat',
                    values: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", '十二月']
                },
                day: {
                    type: 'cat'
                },
                week: {
                    type: 'cat',
                    values: ['5', '4', '3', '2', '1', '0']
                },
                '涨跌幅': {
                    type: 'linear',
                    min: -10,
                    max: 10
                },
                time: {
                    type: 'time'
                }
            };
            chart.axis(false);
            chart.col('日期', {
                type: 'time'
            });
            chart.tooltip({
                map: {
                    title: '日期'
                }
            });
            chart.source(frame, defs);
            chart.facet(['month'], {
                type: 'list',
                cols: 3,
                margin: 30,
                facetTitle: {
                    titleOffset: 3,
                    colTitle: {
                        title: {
                            fontSize: 14,
                            textAlign: 'center',
                            fill: '#6b6b6b'
                        }
                    }
                }
            });
            chart.polygon().position('day*week*日期')
                .color('涨跌幅', '#006837-#ffffbf-#d73027')
                .style({
                    lineWidth: 1,
                    stroke: '#999'
                });
            chart.render();
//            });
        }

        function showRecommendedStock(data) {
            $("#loadingDiv3").hide();
            $("#recommendTable tr:not(:first)").empty();
            for (var i = 0; i < data.length; i++) {
                var insertTr = document.getElementById("recommendTable").insertRow(i + 1);
                insertTr.className = "tableContent";
                var insertTd = insertTr.insertCell(0);
                insertTd.innerHTML = "<span onclick='searchRecommendStock(this.innerHTML)'  style='cursor: pointer'>" + data[i]["stockId"] + "</span>";
                var insertTd = insertTr.insertCell(1);
                var chg = data[i]["changepercent"];
                insertTd.innerHTML = data[i]["stockName"];
                var insertTd = insertTr.insertCell(2);
                if (chg < 0) {
                    insertTd.style.color = "#74a57e";
                } else {
                    insertTd.style.color = "#cd8585";
                }
//
                insertTd.innerHTML = data[i]["trade"];
                var insertTd = insertTr.insertCell(3);

                if (chg < 0) {
                    insertTd.style.color = "#74a57e";
                    insertTd.innerHTML = (chg * 100).toFixed(2) + "%";
                } else {
                    insertTd.style.color = "#cd8585";
                    insertTd.innerHTML = "+" + (chg * 100).toFixed(2) + "%";
                }

            }
        }

        function searchRecommendStock(id) {
            $("#recommendTable tr:not(:first)").empty();
            $("#select2-compare_ui-container.select2-selection__rendered").text("请输入股票名称／代码")
            $('#select2-search_ui-container.select2-selection__rendered').text(id);
            searchStock(id);
            scrollIntoView("#search")
        }

        function showChartDiv() {
            $("#charts").css("display", "inline-block");
            $("#otherChartsDiv").show();
//            $("#charts")[0].scrollIntoView({
//                behavior: "smooth"
//            });
            closeLoadingGIF();
        }

        function closeLoadingGIF() {
            $("#loadingDiv").hide();
        }

        function setAdvice(stockInfo) {
            $("#stockPredictionDiv").fadeIn();
            $("#stockName").html(stockInfo[0]["stockID"] + " " + stockInfo[0]["basicInfo"]["stockName"]);
            $("#stockAdvice").html(stockInfo[0]["advice"]);
            $("#estimatedOpen").html(stockInfo[0]["estimatedOpen"].toFixed(2));
            $("#estimatedClose").html(stockInfo[0]["estimatedClose"].toFixed(2));
            $("#estimatedHigh").html(stockInfo[0]["estimatedHigh"].toFixed(2));
            $("#estimatedLow").html(stockInfo[0]["estimatedLow"].toFixed(2));
            $("#estimatedAdj").html(stockInfo[0]["estimatedAdjClose"].toFixed(2));
        }

        function setBasicInfo(rawData) {
            var data = rawData.basic;
            $("#basicInfo").show();
            console.log("st++++++++" + data["stockName"] + " " + data["stockId"]);
            $("#stockNameID").html(data["stockName"] + " " + data["stockId"]);
            $("#price").html(rawData.realTime["trade"]);
            if (rawData.realTime["changepercent"] > 0) {
                $("#price").css("color", "#cd8585");
                $("#chg").css("color", "#cd8585");
            } else {
                $("#price").css("color", "#74a57e");
                $("#chg").css("color", "#74a57e");
            }
            $("#chg").html((rawData.realTime["changepercent"] * 100).toFixed(2) + "%");
            $("#score").html(rawData.score.toFixed(2));
//            $("#currentStock").html(data["stockName"] + " " + data["stockId"] + " " + rawData.realTime["trade"]+" "+rawData.realTime["changepercent"]);
            $("#plate").html(data["plate"]);
            $("#industry").html(data["industry"]);
            $("#area").html(data["area"]);
            $("#pe").html(data["pe"]);
            $("#outstanding").html(data["outstanding"]);
            $("#totals").html(data["totals"]);
            $("#totalAssets").html(data["totalAssets"]);
            $("#liquidAssets").html(data["liquidAssets"]);
            $("#fixedAssets").html(data["fixedAssets"]);
            $("#reserved").html(data["reserved"]);
            $("#reservedPerShare").html(data["reservedPerShare"]);
            $("#esp").html(data["esp"]);
            $("#bvps").html(data["bvps"]);
            $("#pb").html(data["pb"]);
            $("#timeToMarket").html(data["timeToMarket"]);
            $("#undp").html(data["undp"]);
            $("#perundp").html(data["perundp"]);
            $("#rev").html(data["rev"]);
            $("#profit").html(data["profit"]);
            $("#gpr").html(data["gpr"]);
            $("#npr").html(data["npr"]);
            $("#holders").html(data["holders"]);
        }

        function compareStockReturnRate(data, flag) {
            var myChart1 = echarts.init(document.getElementById('line_charts2'));
            if (flag == 0) {

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
                        data: [data.basic["stockName"]]
                    },
                    xAxis: [
                        {
                            type: 'category',
                            boundaryGap: false,
                            axisLine: {onZero: true},
                            data: data.dates
                        }
                    ],
                    yAxis: [
                        {
                            name: '对数收益率',
                            type: 'value'
                        }
                    ],
                    series: [
                        {
                            name: data.basic["stockName"],
                            type: 'line',
                            symbol: 'none',
                            symbolSize: 8,
                            hoverAnimation: false,
                            data: data.logReturn,
                            itemStyle: {
                                normal: {
                                    color: '#F5C07D',
                                    lineStyle: {
                                        color: '#F5C07D',
                                        width: 2.5
                                    }
                                }
                            }
                        }
                    ]
                };
            } else if (flag == 1) {
                var item1 = {
                    name: data.basic["stockName"],
                    type: 'line',
                    symbol: 'none',
                    hoverAnimation: false,
                    data: data.logReturn
                };
                option1.series.push(item1);
                option1.legend.data.push(data.basic["stockName"]);
            }

            myChart1.setOption(option1);
        }

        function compareStockChg(data, flag) {
            var myChart2 = echarts.init(document.getElementById('line_charts'));
            if (flag == 0) {
                option2 = {
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
                        data: [data.basic["stockName"]]
                    },
                    xAxis: [
                        {
                            type: 'category',
                            boundaryGap: false,
                            axisLine: {onZero: true},
                            data: data.dates
                        }
                    ],
                    yAxis: [
                        {
                            name: '涨跌幅(%)',
                            type: 'value'
                        }
                    ],
                    series: [
                        {
                            name: data.basic["stockName"],
                            type: 'line',
                            symbol: 'none',
                            symbolSize: 8,
                            hoverAnimation: false,
                            data: data.chg,
                            itemStyle: {
                                normal: {
                                    color: '#F5C07D',
                                    lineStyle: {
                                        color: '#F5C07D',
                                        width: 2.5
                                    }
                                }
                            }
                        }
                    ]
                };
            } else if (flag == 1) {
                var item1 = {
                    name: data.basic["stockName"],
                    type: 'line',
                    symbol: 'none',
                    hoverAnimation: false,
                    data: data.chg
                };
                option2.series.push(item1);
                option2.legend.data.push(data.basic["stockName"]);
            }

            myChart2.setOption(option2);
        }

        function splitData(rawData) {
//            var dates = rawData.map(function (item) {
//                return item[0];
//            });
            var dates = [];
            var values = [];
            var volume = [];
            var Ma5 = [];
            var Ma10 = [];
            var Ma20 = [];
            var Ma30 = [];
            var Ma60 = [];
            var Ma120 = [];
            var Ma240 = [];
            var chg = [];
            var logReturn = [];

            var dates_week = [];
            var values_week = [];
            var volume_week = [];
            var WMa5 = [];
            var WMa10 = [];
            var WMa20 = [];
            var WMa30 = [];
            var WMa60 = [];
            var WMa120 = [];
            var WMa240 = [];

            var dates_month = [];
            var values_month = [];
            var volume_month = [];
            var MMa5 = [];
            var MMa10 = [];
            var MMa20 = [];
            var MMa30 = [];
            var MMa60 = [];
            var MMa120 = [];
            var MMa240 = [];
            var mb = [];
            var up = [];
            var dn = [];

            var k = [];
            var d = [];
            var j = [];
            var kdjDate = [];
            var kdjAna = [];

            var bollDate = [];
            var bollAna = [];

            var macdDate = [];
            var macdAna = [];

            var dmiDate = [];
            var dmiAna = [];

            var dif = [];
            var dea = [];
            var macd = [];

            var basicInfo = rawData[0]["basicInfo"];

            var stockID = rawData[0]["stockID"];
            var stockName = rawData[0]["stockName"];

            var boll = [];

            var fluctuation = [];

            var diPlus = [];
            var diMinus = [];
            var adx = [];
            var adxr = [];

            var innerValueDate = [];
            var innerValue = [];

            var allAnaDate = [];
            var kdjList = [];
            var bollList = [];
            var macdList = [];
            var dmiList = [];

            var i = 1;
            for (; i < rawData.length; i++) {
                if (rawData[i]["日期"] == -1) {
                    i++;
                    break;
                }
                dates[i - 1] = rawData[i]["日期"];
                values[i - 1] = [rawData[i]["开盘价"], rawData[i]["复权收盘价"], rawData[i]["最高价"], rawData[i]["最低价"]];

                volume[i - 1] = rawData[i]["交易量"];
                rawData[i]["5日均线"] == -1 ? Ma5[i - 1] = null : Ma5[i - 1] = rawData[i]["5日均线"];
                rawData[i]["10日均线"] == -1 ? Ma10[i - 1] = null : Ma10[i - 1] = rawData[i]["10日均线"];
                rawData[i]["20日均线"] == -1 ? Ma20[i - 1] = null : Ma20[i - 1] = rawData[i]["20日均线"];
                rawData[i]["30日均线"] == -1 ? Ma30[i - 1] = null : Ma30[i - 1] = rawData[i]["30日均线"];
                rawData[i]["60日均线"] == -1 ? Ma60[i - 1] = null : Ma60[i - 1] = rawData[i]["60日均线"];
                rawData[i]["120日均线"] == -1 ? Ma120[i - 1] = null : Ma120[i - 1] = rawData[i]["120日均线"];
                rawData[i]["240日均线"] == -1 ? Ma240[i - 1] = null : Ma240[i - 1] = rawData[i]["240日均线"];
                rawData[i]["mb"] == -1 ? mb[i - 1] = null : mb[i - 1] = rawData[i]["mb"].toFixed(2);
                rawData[i]["dn"] == -1 ? dn[i - 1] = null : dn[i - 1] = rawData[i]["dn"].toFixed(2);
                rawData[i]["up"] == -1 ? up[i - 1] = null : up[i - 1] = rawData[i]["up"].toFixed(2);
                chg[i - 1] = rawData[i]["涨跌幅"];
                logReturn[i - 1] = rawData[i]["对数收益率"];
                boll[i - 1] = {
                    "date": rawData[i]["日期"],
                    "open": rawData[i]["开盘价"],
                    "high": rawData[i]["最高价"],
                    "low": rawData[i]["最低价"],
                    "close": rawData[i]["复权收盘价"],
                    "Ma20": Ma20[i - 1],
                    "mb": mb[i - 1],
                    "dn": dn[i - 1],
                    "up": up[i - 1]
                };
                if (rawData[i]["日期"].indexOf('2016-07') >= 0 || rawData[i]["日期"].indexOf('2016-08') >= 0 || rawData[i]["日期"].indexOf('2016-09') >= 0
                    || rawData[i]["日期"].indexOf('2016-09') >= 0 || rawData[i]["日期"].indexOf('2016-10') >= 0 || rawData[i]["日期"].indexOf('2016-11') >= 0
                    || rawData[i]["日期"].indexOf('2016-12') >= 0 || rawData[i]["日期"].indexOf('2017-01') >= 0 || rawData[i]["日期"].indexOf('2017-02') >= 0
                    || rawData[i]["日期"].indexOf('2017-03') >= 0 || rawData[i]["日期"].indexOf('2017-04') >= 0 || rawData[i]["日期"].indexOf('2017-05') >= 0
                    || rawData[i]["日期"].indexOf('2017-06') >= 0) {

                    fluctuation.push({
                        '日期': rawData[i]["日期"],
                        '涨跌幅': parseFloat(rawData[i]["涨跌幅"])
                    });
                }
                k[i - 1] = rawData[i]["k"].toFixed(2);
                d[i - 1] = rawData[i]["d"].toFixed(2);
                j[i - 1] = rawData[i]["j"].toFixed(2);

                dif[i - 1] = rawData[i]["dif"].toFixed(3);
                dea[i - 1] = rawData[i]["dea"].toFixed(3);
                macd[i - 1] = rawData[i]["macd"].toFixed(3);

                diPlus[i - 1] = rawData[i]["+di"].toFixed(3);
                diMinus[i - 1] = rawData[i]["-di"].toFixed(3);
                adx[i - 1] = rawData[i]["adx"].toFixed(3);
                adxr[i - 1] = rawData[i]["adxr"].toFixed(3);

            }
            var Ma = [Ma5, Ma10, Ma20, Ma30, Ma60, Ma120, Ma240];
            var m = 0;
            for (; i < rawData.length; i++) {
                if (rawData[i]["日期（周）"] == -1) {
                    i++;
                    break;
                }
                dates_week[m] = rawData[i]["日期（周）"];
                values_week[m] = [rawData[i]["开盘价（周）"], rawData[i]["复权收盘价（周）"], rawData[i]["最高价（周）"], rawData[i]["最低价（周）"]];
                volume_week[m] = rawData[i]["交易量（周）"];
                rawData[i]["5日均线"] == -1 ? WMa5[m] = null : WMa5[m] = rawData[i]["5日均线"];
                rawData[i]["10日均线"] == -1 ? WMa10[m] = null : WMa10[m] = rawData[i]["10日均线"];
                rawData[i]["20日均线"] == -1 ? WMa20[m] = null : WMa20[m] = rawData[i]["20日均线"];
                rawData[i]["30日均线"] == -1 ? WMa30[m] = null : WMa30[m] = rawData[i]["30日均线"];
                rawData[i]["60日均线"] == -1 ? WMa60[m] = null : WMa60[m] = rawData[i]["60日均线"];
                rawData[i]["120日均线"] == -1 ? WMa120[m] = null : WMa120[m] = rawData[i]["120日均线"];
                rawData[i]["240日均线"] == -1 ? WMa240[m] = null : WMa240[m] = rawData[i]["240日均线"];
                m++;
            }
            var WMa = [WMa5, WMa10, WMa20, WMa30, WMa60, WMa120, WMa240];

            var n = 0;
            for (; i < rawData.length; i++) {
                if (rawData[i]["kdjDate"] != null) {
                    kdjDate.push(rawData[i]["kdjDate"]);
                    kdjAna.push(rawData[i]["kdjAna"]);
                } else if (rawData[i]["bollDate"] != null) {
                    bollDate.push(rawData[i]["bollDate"]);
                    bollAna.push(rawData[i]["bollAna"]);
                } else if (rawData[i]["macdDate"] != null) {
                    macdDate.push(rawData[i]["macdDate"]);
                    macdAna.push(rawData[i]["macdAna"]);
                } else if (rawData[i]["xAxis"] != null) {
                    innerValue.push(rawData[i]["value"].toFixed(3));
                    innerValueDate.push(rawData[i]["xAxis"]);
                } else if (rawData[i]["dmiDate"] != null) {
                    dmiDate.push(rawData[i]["dmiDate"]);
                    dmiAna.push(rawData[i]["dmiAna"]);
                } else if (rawData[i]["allDate"] != null) {
                    allAnaDate.push(rawData[i]["allDate"]);
                } else {
                    dates_month[n] = rawData[i]["日期（月）"];
                    values_month[n] = [rawData[i]["开盘价（月）"], rawData[i]["复权收盘价（月）"], rawData[i]["最高价（月）"], rawData[i]["最低价（月）"]];
                    volume_month[n] = rawData[i]["交易量（月）"];
                    rawData[i]["5日均线"] == -1 ? MMa5[n] = null : MMa5[n] = rawData[i]["5日均线"];
                    rawData[i]["10日均线"] == -1 ? MMa10[n] = null : MMa10[n] = rawData[i]["10日均线"];
                    rawData[i]["20日均线"] == -1 ? MMa20[n] = null : MMa20[n] = rawData[i]["20日均线"];
                    rawData[i]["30日均线"] == -1 ? MMa30[n] = null : MMa30[n] = rawData[i]["30日均线"];
                    rawData[i]["60日均线"] == -1 ? MMa60[n] = null : MMa60[n] = rawData[i]["60日均线"];
                    rawData[i]["120日均线"] == -1 ? MMa120[n] = null : MMa120[n] = rawData[i]["120日均线"];
                    rawData[i]["240日均线"] == -1 ? MMa240[n] = null : MMa240[n] = rawData[i]["240日均线"];
                    n++;
                }

                kdjList = rawData[i]["kdjList"];
                bollList = rawData[i]["bollList"];
                macdList = rawData[i]["macdList"];
                dmiList = rawData[i]["dmiList"];
            }
            var MMa = [MMa5, MMa10, MMa20, MMa30, MMa60, MMa120, MMa240];

            return {
                stockID: stockID,
                stockName: stockName,
                dates: dates,
                values: values,
                volume: volume,
                dates_week: dates_week,
                values_week: values_week,
                volume_week: volume_week,
                dates_month: dates_month,
                values_month: values_month,
                volume_month: volume_month,
                Ma: Ma,
                MMa: MMa,
                WMa: WMa,
                chg: chg,
                logReturn: logReturn,
                boll: boll,
                basic: basicInfo,
                fluctuation: fluctuation,
                k: k,
                d: d,
                j: j,
                dif: dif,
                dea: dea,
                macd: macd,
                kdjDate: kdjDate,
                kdjAna: kdjAna,
                adx: adx,
                adxr: adxr,
                diPlus: diPlus,
                diMinus: diMinus,
                bollDate: bollDate,
                bollAna: bollAna,
                macdDate: macdDate,
                macdAna: macdAna,
                innerValueDate: innerValueDate,
                innerValue: innerValue,
                dmiDate: dmiDate,
                dmiAna: dmiAna,
                score: rawData[0]["score"],
                allAnaDate: allAnaDate,
                kdjList: kdjList,
                bollList: bollList,
                macdList: macdList,
                dmiList: dmiList,
                realTime: rawData[0]["实时"]
            };
        }

        function getKLine(data) {
            showChartDiv();
            setKLine(data.dates, data.values, data.volume, data, 'k_charts', data.Ma);
            setKLine(data.dates_week, data.values_week, data.volume_week, data, 'k_charts_weekly', data.WMa);
            setKLine(data.dates_month, data.values_month, data.volume_month, data, 'k_charts_monthly', data.MMa);
        }

        function setKLine(date, value, volume, otherInfo, divId, ma) {
            var myChart = echarts.init(document.getElementById(divId));
            myChart.setOption(option = {
                backgroundColor: '#fff',
                animation: true,
                legend: {
                    bottom: 10,
                    left: 'center',
                    data: [otherInfo.basic["stockName"], 'MA5', 'MA10', 'MA20', 'MA30', 'MA60', 'MA120', 'MA240']
                },
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'cross'
                    },
                    backgroundColor: 'rgba(245, 245, 245, 0.8)',
                    borderWidth: 1,
                    borderColor: '#ccc',
                    padding: 10,
                    textStyle: {
                        color: '#000'
                    },
                    position: function (pos, params, el, elRect, size) {
                        var obj = {top: 10};
                        obj[['left', 'right'][+(pos[0] < size.viewSize[0] / 2)]] = 30;
                        return obj;
                    },
                    extraCssText: 'width: 170px'
                },
                axisPointer: {
                    link: {xAxisIndex: 'all'},
                    label: {
                        backgroundColor: '#777'
                    }
                },
                toolbox: {
                    feature: {
                        dataZoom: {
                            yAxisIndex: false
                        },
                        brush: {
                            type: ['lineX', 'clear']
                        },
                        saveAsImage: {}
                    }
                },
                brush: {
                    xAxisIndex: 'all',
                    brushLink: 'all',
                    outOfBrush: {
                        colorAlpha: 0.1
                    }
                },
                grid: [
                    {
                        left: '10%',
                        right: '8%',
                        height: '50%'
                    },
                    {
                        left: '10%',
                        right: '8%',
                        top: '63%',
                        height: '16%'
                    }
                ],
                xAxis: [
                    {
                        type: 'category',
                        data: date,
                        scale: true,
                        boundaryGap: false,
                        axisLine: {onZero: false},
                        splitLine: {show: false},
                        splitNumber: 20,
                        min: 'dataMin',
                        max: 'dataMax',
                        axisPointer: {
                            z: 100
                        }
                    },
                    {
                        type: 'category',
                        gridIndex: 1,
                        data: date,
                        scale: true,
                        boundaryGap: false,
                        axisLine: {onZero: false},
                        axisTick: {show: false},
                        splitLine: {show: false},
                        axisLabel: {show: false},
                        splitNumber: 20,
                        min: 'dataMin',
                        max: 'dataMax'
                    }
                ],
                yAxis: [
                    {
                        scale: true,
//                        minInterval: 50,
                        splitArea: {
                            show: true
                        }
                    },
                    {
                        scale: true,
                        gridIndex: 1,
                        splitNumber: 2,
                        axisLabel: {show: false},
                        axisLine: {show: false},
                        axisTick: {show: false},
                        splitLine: {show: false}
                    }
                ],
                dataZoom: [
                    {
                        type: 'inside',
                        xAxisIndex: [0, 1],
                        start: 95,
                        end: 100
                    },
                    {
                        show: true,
                        xAxisIndex: [0, 1],
                        type: 'slider',
                        top: '85%',
                        start: 95,
                        end: 100
                    }
                ],
                series: [
                    {
                        name: otherInfo.basic["stockName"],
                        type: 'candlestick',
                        data: value,
                        itemStyle: {
                            normal: {
                                borderColor: null,
                                borderColor0: null,
                                color: '#cd8585', // 阳线填充颜色
                                color0: '#74a57e', // 阴线填充颜色
                                lineStyle: {
                                    width: 2,
                                    color: '#cd8585', // 阳线边框颜色
                                    color0: '#74a57e' // 阴线边框颜色
                                }
                            }
                        },
                        tooltip: {
                            formatter: function (param) {
                                param = param[0];
                                return [
                                    'Date: ' + param.name + '<hr size=1 style="margin: 3px 0">',
                                    'Open: ' + param.data[0] + '<br/>',
                                    'Close: ' + param.data[1] + '<br/>',
                                    'Lowest: ' + param.data[2] + '<br/>',
                                    'Highest: ' + param.data[3] + '<br/>'
                                ].join('');
                            }
                        }
                    },
                    {
                        name: 'MA5',
                        symbol: 'none',
                        type: 'line',
                        data: ma[0],
                        smooth: true,
                        lineStyle: {
                            normal: {opacity: 0.5}
                        }
                    },
                    {
                        name: 'MA10',
                        symbol: 'none',
                        type: 'line',
                        data: ma[1],
                        smooth: true,
                        lineStyle: {
                            normal: {opacity: 0.5}
                        }
                    },
                    {
                        name: 'MA20',
                        symbol: 'none',
                        type: 'line',
                        data: ma[2],
                        smooth: true,
                        lineStyle: {
                            normal: {opacity: 0.5}
                        }
                    },
                    {
                        name: 'MA30',
                        symbol: 'none',
                        type: 'line',
                        data: ma[3],
                        smooth: true,
                        lineStyle: {
                            normal: {opacity: 0.5}
                        }
                    },
                    {
                        name: 'MA60',
                        symbol: 'none',
                        type: 'line',
                        data: ma[4],
                        smooth: true,
                        lineStyle: {
                            normal: {opacity: 0.5}
                        }
                    },
                    {
                        name: 'MA120',
                        symbol: 'none',
                        type: 'line',
                        data: ma[5],
                        smooth: true,
                        lineStyle: {
                            normal: {opacity: 0.5}
                        }
                    }, {
                        name: 'MA240',
                        symbol: 'none',
                        type: 'line',
                        data: ma[6],
                        smooth: true,
                        lineStyle: {
                            normal: {opacity: 0.5}
                        }
                    },
                    {
                        name: '成交量',
                        type: 'bar',
                        xAxisIndex: 1,
                        yAxisIndex: 1,
                        data: volume
                    }
                ]
            });

        }

        function search(info, flag) {
            if (flag == 1) {
                $("#loadingDiv2").css("display", "inline-block");
            }
//            var searchInfo = info.split(" ")[0];
            $.ajax({
                url: "getKLineInfo.action",
                type: 'POST',
                dataType: 'json',
                data: {
                    stockInfo: info
                },
                success: function (data) {
                    var jsonObj = eval('(' + data + ')');
//                    var jsonObj = eval(data);

                    if (jsonObj[0]["searchResult"] != "搜索的股票不存在") {
                        var json = splitData(jsonObj);
                        if (flag == 0) {
                            $("#tempDiv").hide();
                            $("#fluctuation_charts").empty();

                            setBasicInfo(json);
                            getKLine(json);
                            drawKDJ(json);
                            getBoll(json);
                            drawMACD(json);
                            drawDMI(json);
                            compareStockChg(json, flag);
                            compareStockReturnRate(json, flag);
                            setAdvice(jsonObj);
                            drawFluctuationChart(json.fluctuation);
//                            getRecommendedStock(info)
                            if (jsonObj[0]["searchResult"] == "搜索成功") {
                                $("#innerValueLi").show()
                                drawInnerValue(json);
                            } else {
                                fail_prompt(jsonObj[0]["searchResult"], 1500);
                                $("#innerValueLi").hide();
                            }
                            drawAllDateAnaChart(json);
                            $("#recommendedStockDiv").fadeIn();
                            $("#buySellDiv").fadeIn();
                            $("#loadingDiv").hide();
                        } else if (flag == 1) {
                            compareStockChg(json, flag);
                            compareStockReturnRate(json, flag);
                            $("#loadingDiv2").hide()
                        }
                    } else {
                        fail_prompt(jsonObj[0]["searchResult"], 1500);
                        $("#loadingDiv").hide();
                    }
                },
                error: function () {
                    fail_prompt("ajax error", 1500)
                }
            })
            ;
        }

        function getBoll(data) {
            var stockEvents = [];
            for (var i = 0; i < data.bollDate.length; i++) {
                stockEvents.push({
                    "date": data.bollDate[i],
                    "type": "sign",
                    "backgroundColor": "#85CDE6",
                    "graph": "g1",
                    "text": data.bollAna[i],
                    "description": "This is description of an event"
                })
//                console.log(stockEvents[i])
            }
            var chart = AmCharts.makeChart("boll_charts", {
                "type": "serial",
                "theme": "patterns",
                "dataDateFormat": "yyyy-MM-dd",
                "valueAxes": [{
                    "position": "left"
                }],
                "dataProvider": data.boll,
                "categoryField": "date",
//                "dataSets": [{
//                    "stockEvents": [{
//                        "date": "2016-06-07",
//                        "type": "sign",
//                        "showOnAxis": true,
//                        "graph": "mb",
//                        "text": "abcefg",
//                        "description": "This is description of an event"
//                    }]
//                }],
//                "stockEvents": stockEvents,
                "graphs": [{
                    "id": "g1",
                    "balloonText": "Open:<b>[[open]]</b><br>Low:<b>[[low]]</b><br>High:<b>[[high]]</b><br>Close:<b>[[close]]</b><br>",
                    "closeField": "close",
                    "fillColors": "#7f8da9",
                    "highField": "high",
                    "lineColor": "#7f8da9",
                    "lineAlpha": 1,
                    "fillAlphas": 0,
                    "lineThickness": 2,
                    "lowField": "low",
                    "negativeFillColors": "#cd8585",
                    "negativeLineColor": "#cd8585",
                    "openField": "open",
                    "title": "Price:",
                    "type": "ohlc",
                    "valueField": "close"
                }, {
                    "id": "ma20",
//                    "bullet": "round",
                    "balloonText": "Ma20:<b>[[Ma20]]</b>",
                    "lineThickness": 2,
                    "bulletSize": 0,
                    "bulletBorderAlpha": 1,
                    "bulletColor": "#FFFFFF",
                    "useLineColorForBulletBorder": true,
                    "bulletBorderThickness": 3,
                    "fillAlphas": 0,
                    "lineAlpha": 1,
                    "valueField": "Ma20",
                    "lineColor": "#F5C07D"
                }, {
                    "id": "mb",
//                    "bullet": "round",
                    "balloonText": "mb:<b>[[mb]]</b>",
                    "lineThickness": 2,
                    "bulletSize": 0,
                    "bulletBorderAlpha": 1,
                    "bulletColor": "#FFFFFF",
                    "useLineColorForBulletBorder": true,
                    "bulletBorderThickness": 3,
                    "fillAlphas": 0,
                    "lineAlpha": 1,
                    "valueField": "mb",
                    "lineColor": "#DAB3F5"
                }, {
                    "id": "dn",
//                    "bullet": "round",
                    "balloonText": "dn:<b>[[dn]]</b>",
                    "lineThickness": 2,
                    "bulletSize": 0,
                    "bulletBorderAlpha": 1,
                    "bulletColor": "#FFFFFF",
                    "useLineColorForBulletBorder": true,
                    "bulletBorderThickness": 3,
                    "fillAlphas": 0,
                    "lineAlpha": 1,
                    "valueField": "dn",
                    "lineColor": "#74a57e"
                }, {
                    "id": "up",
//                    "bullet": "round",
                    "balloonText": "up:<b>[[up]]</b>",
                    "lineThickness": 2,
                    "bulletSize": 0,
                    "bulletBorderAlpha": 1,
                    "bulletColor": "#FFFFFF",
                    "useLineColorForBulletBorder": true,
                    "bulletBorderThickness": 3,
                    "fillAlphas": 0,
                    "lineAlpha": 1,
                    "valueField": "up",
                    "lineColor": "#A9CCF5"
                }],

                "chartScrollbar": {
                    "graph": "g1",
                    "oppositeAxis": false,
                    "offset": 30,
                    "scrollbarHeight": 30,
                    "backgroundAlpha": 0,
                    "selectedBackgroundAlpha": 0.1,
                    "selectedBackgroundColor": "#888888",
                    "graphFillAlpha": 0,
                    "graphLineAlpha": 0.5,
                    "selectedGraphFillAlpha": 0,
                    "selectedGraphLineAlpha": 1,
                    "autoGridCount": true,
                    "color": "#AAAAAA"
                },
                "chartCursor": {},
                "categoryAxis": {
                    "parseDates": false
                }
//                "credit": {
//                    "enable": false
//                }
            });

            zoomChart();

// this method is called when chart is first inited as we listen for "dataUpdated" event
            function zoomChart() {
                chart.zoomToIndexes(chart.dataProvider.length - 40, chart.dataProvider.length - 1);
            }
        }

        function getRecommendedStock(stockInfo) {
            $.ajax({
                url: "getRecommendedStock.action",
                type: 'POST',
                dataType: 'json',
                data: {
                    stockInfo: stockInfo
                },
                success: function (data) {
                    var jsonObj = eval('(' + data + ')');
                    showRecommendedStock(jsonObj["recommend"]);
                },
                error: function () {
                    fail_prompt("ajax error", 1500)
                }
            })
            ;
        }

        function addOptionalStock(stockID) {
            var account = getCookie("account");

            if (account == null) {
                fail_prompt("请先登录！", 1500);
            } else {
                $.ajax({
                    url: "addOptionalStock.action",
                    type: 'POST',
                    dataType: 'json',
                    data: {
                        stockCode: stockID,
                        account: account
                    },
                    success: function () {
                        $("#addOptionalButton").html("取消关注");
                        success_prompt("添加成功！", 15000)
                    },
                    error: function () {
                        fail_prompt("ajax error", 1500)
                    }
                });
            }
        }

        function removeOptionalStock(stockID) {
            var account = getCookie("account");

            if (account == null) {
                fail_prompt("登录已过期，请重新登录", 1500);
            } else {
                $.ajax({
                    url: "deleteOptionalStock.action",
                    type: 'POST',
                    dataType: 'json',
                    data: {
                        stockCode: stockID,
                        account: account
                    },
                    success: function (r) {
                        $("#addOptionalButton").html("+关注");
                        success_prompt("移除成功！", 1500)
                    },
                    error: function () {
                        fail_prompt("ajax error", 1500)
                    }
                });
            }
        }

        function ifHasSelected(stockID) {
            if (getCookie("account") != null) {
                $.ajax({
                    url: "getAllOptionalStock.action",
                    type: 'POST',
                    dataType: 'json',
                    data: {
                        account: getCookie("account")
                    },
                    success: function (data) {
                        var jsonObj = eval('(' + data + ')');
                        $("#addOptionalButton").html("+关注");
                        for (var i = 0; i < jsonObj.length; i++) {
                            console.log(jsonObj[i]["stockID"])
                            if (jsonObj[i]["stockID"] == stockID) {
                                $("#addOptionalButton").html("取消关注");
                                break;
                            }
                        }
                    },
                    error: function () {
                        fail_prompt("ajax error", 1500)
                    }
                });
            } else {
                $("#addOptionalButton").html("+关注");
            }
        }

        function addOrMoveOptionalStock() {
            if (getCookie("account") == null) {
                fail_prompt("请先登录！", 1500);
            }
            else if ($("#addOptionalButton").html() == "取消关注") {
                removeOptionalStock(window.stockID_global);
            } else {
                addOptionalStock(window.stockID_global);
            }
        }


    </script>

</head>
<body>
<div id="alert"></div>
<div align="center">
    <div align="center" style="padding: 40px;width: 1100px">
        <ul id="wholeTab" class="nav nav-tabs" style="width:100%;">
            <li class="active">
                <a href="#quotes" data-toggle="tab" id="quotesTab">
                    <img src="${pageContext.request.contextPath}/css/pic/recommend.png" width="20">
                    今日资讯
                </a>
            </li>
            <li>
                <a href="#search" data-toggle="tab" id="searchTab">
                    <img src="${pageContext.request.contextPath}/css/pic/search.png" width="20">
                    搜索股票
                </a>
            </li>
        </ul>

        <div id="wholeTabContent" class="tab-content">
            <div id="quotes" class="tab-pane fade in active" style="width:1100px;">
                <%@ include file="marketInfo.jsp" %>
            </div>
            <div id="search" class="tab-pane fade">
                <div align="center">
                    <div class="searchDiv" align="left">

                        <div align="left" style="display: inline-block;vertical-align: top">
                            <div style="width: auto">
                                <div style="display:inline-block;">
                                    <select id="search_ui" style="width:300px;outline: none"
                                            onchange='searchStock(this.value.split(" ")[0])'>
                                        <!-- Dropdown List Option -->
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div id="loadingDiv" align="left" style="display: none;margin-left: 20px">
                            <div style="width: 150px">
                                <div>
                                    <img src="${pageContext.request.contextPath}/css/pic/loading.gif" width="40">
                                </div>
                            </div>
                        </div>
                        <div id="loadingStockDiv" align="left" style="display: none;margin-left: 20px">
                            <div>
                                <p style="font-size: 16px;font-weight: 300;color: #c0c0c0">正在加载股票信息......</p>
                            </div>
                        </div>

                        <div>
                            <br>
                            <button type="button" class="btn btn-primary" data-toggle="collapse"
                                    data-target="#multiSelectionDiv"
                                    style="color: #cd8585;font-size: 14px;font-weight: 300;border: none;background: none;text-decoration: underline;outline: none">
                                不知道搜什么？试试这里
                            </button>
                        </div>

                        <div class="collapse" id="multiSelectionDiv">
                            <%@ include file="MultifactorialSelection.jsp" %>
                        </div>

                        <div id="tempDiv" style="height: 450px;"></div>

                        <div id="basicInfo"
                             style="width: 1000px;height: auto;display: none;margin-top: 20px;font-weight: 100">
                            <div id="currentStock">
                                <span id="stockNameID">--</span>
                                <span style="margin-left: 20px;font-size: 18px;color: #8d8d8d">现价</span>
                                <span id="price">--</span>
                                <span style="margin-left: 20px;font-size: 18px;color: #8d8d8d">涨跌幅</span>
                                <span id="chg">--</span>
                                <div style="display: inline-block">
                                    <span style="margin-left: 20px;font-size: 18px;color: #8d8d8d">股票评分</span>
                                    <span id="score">--</span>
                                </div>
                                <div style="display:inline-block;zoom: 1">
                                    <img src="${pageContext.request.contextPath}/css/pic/question.png"
                                         id="scoreIntro"
                                         width="25"
                                         style="vertical-align: sub;margin-left: 5px;">
                                </div>
                                <div class="mdl-tooltip" for="scoreIntro">
                                    <span>根据股票获利能力、股价波动性、股票市场性、营运能力、短期偿债能力、财务结构六个方面加权计算得分。<br>
1.00~1.09强力买入；<br>1.10~2.09买入；<br>2.10~3.09观望；<br>3.10~4.09适度减持；<br>4.10~5.00卖出
</span>
                                </div>
                            </div>
                            <table style="margin-top:20px;width: 100%;height:150px;color:#8d8d8d;font-weight: 300">
                                <tbody>
                                <tr>
                                    <td>
                                        板块:&nbsp;&nbsp;
                                        <span id="plate"></span>
                                    </td>
                                    <td>
                                        行业:&nbsp;&nbsp;
                                        <span id="industry"></span>
                                    </td>
                                    <td>
                                        地区:&nbsp;&nbsp;
                                        <span id="area"></span>
                                    </td>
                                    <td>
                                        市盈率:&nbsp;&nbsp;
                                        <span id="pe"></span>
                                    </td>
                                    <td>
                                        流通股本(亿):&nbsp;&nbsp;
                                        <span id="outstanding"></span>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        总股本(亿):&nbsp;&nbsp;
                                        <span id="totals"></span>
                                    </td>
                                    <td>
                                        总资产(万):&nbsp;&nbsp;
                                        <span id="totalAssets"></span>
                                    </td>
                                    <td>
                                        流动资产:&nbsp;&nbsp;
                                        <span id="liquidAssets"></span>
                                    </td>
                                    <td>
                                        固定资产:&nbsp;&nbsp;
                                        <span id="fixedAssets"></span>
                                    </td>
                                    <td>
                                        公积金:&nbsp;&nbsp;
                                        <span id="reserved"></span>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        每股公积金:&nbsp;&nbsp;
                                        <span id="reservedPerShare"></span>
                                    </td>
                                    <td>
                                        每股收益:&nbsp;&nbsp;
                                        <span id="esp"></span>
                                    </td>
                                    <td>
                                        每股净资:&nbsp;&nbsp;
                                        <span id="bvps"></span>
                                    </td>
                                    <td>
                                        市净率:&nbsp;&nbsp;
                                        <span id="pb"></span>
                                    </td>
                                    <td>
                                        上市时间:&nbsp;&nbsp;
                                        <span id="timeToMarket"></span>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        未分利润:&nbsp;&nbsp;
                                        <span id="undp"></span>
                                    </td>
                                    <td>
                                        每股未分配:&nbsp;&nbsp;
                                        <span id="perundp"></span>
                                    </td>
                                    <td>
                                        收入同比(%):&nbsp;&nbsp;
                                        <span id="rev"></span>
                                    </td>
                                    <td>
                                        利润同比(%):&nbsp;&nbsp;
                                        <span id="profit"></span>
                                    </td>
                                    <td>
                                        毛利率(%):&nbsp;&nbsp;
                                        <span id="gpr"></span>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        净利润率(%):&nbsp;&nbsp;
                                        <span id="npr"></span>
                                    </td>
                                    <td>
                                        股东人数:&nbsp;&nbsp;
                                        <span id="holders"></span>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                        <div id="charts"
                             style="width: 1000px;height: 600px;display: none;margin-bottom: 50px;margin-top: 20px">
                            <ul id="myTab" class="nav nav-tabs">
                                <li class="active">
                                    <a href="#k_charts" data-toggle="tab">
                                        日K
                                    </a>
                                </li>
                                <li><a href="#k_charts_weekly" data-toggle="tab">周K</a></li>
                                <li><a href="#k_charts_monthly" data-toggle="tab">月K</a></li>
                                <li style="float: right">
                                    <button onclick="addOrMoveOptionalStock()" id="addOptionalButton">+关注</button>
                                </li>
                            </ul>
                            <div id="myTabContent" class="tab-content" style="width: 1000px;height: 600px">

                                <div class="tab-pane fade in active" id="k_charts" style="width: 950px;height: 600px">
                                </div>

                                <div class="tab-pane fade" id="k_charts_weekly" style="width: 950px;height: 600px;">
                                </div>

                                <div class="tab-pane fade" id="k_charts_monthly" style="width: 950px;height: 600px;">
                                </div>

                            </div>


                        </div>


                        <div id="stockPredictionDiv" style="display:none;width: 1000px;margin-top: 40px">
                            <div style="display:inline-block;zoom: 1">
                                <img src="${pageContext.request.contextPath}/css/pic/information.png"
                                     id="predictionIntro"
                                     width="20"
                                     style="vertical-align: sub;margin-left: 5px;">
                            </div>
                            <!-- Multiline Tooltip -->
                            <div class="mdl-tooltip" for="predictionIntro">
                                <span>明天股价怎么样？ARIMA模型帮你预测。<br>基于每日成交量与每日收盘价的非线性回归的弹性分析，告诉你，在风险股市，下一步怎么办！</span>

                            </div>

                            <div style="display:inline-block;zoom: 1">
                                <p style="font-size: 20px;font-weight: normal;color: #858585">
                                    股价预测
                                    <span style="font-size: 14px;font-weight: 300;color: #858585">（仅供参考）</span>
                                    <span id="stockName"
                                          style="font-size: 18px;font-weight: 300;color: #858585">error   </span>

                                </p>
                            </div>

                            <div style="border: solid 1px #e1e1e1;padding: 20px">
                                <div style="display:inline-block;">
                                    <p style="font-size: 18px;font-weight: normal;color: #566d8a">
                                        投资建议：<span id="stockAdvice"></span>
                                    </p></div>
                                <br>

                                <p id="predictionInfo" style="font-size: 16px;font-weight: 300;color: #585858">
                                    预测开盘价：
                                    <span id="estimatedOpen">error</span>
                                    <span>&nbsp;&nbsp;&nbsp;</span>
                                    预测收盘价：
                                    <span id="estimatedClose">error</span>
                                    <span>&nbsp;&nbsp;&nbsp;</span>
                                    预测最高价：
                                    <span id="estimatedHigh">error</span>
                                    <span>&nbsp;&nbsp;&nbsp;</span>
                                    预测最低价：
                                    <span id="estimatedLow">error</span>
                                    <span>&nbsp;&nbsp;&nbsp;</span>
                                    预测复权收盘价：
                                    <span id="estimatedAdj">error</span>
                                </p>
                            </div>
                        </div>
                        <div id="recommendedStockDiv" style="display:none;width: 1000px;margin-top: 40px;height: auto">
                            <div style="display:inline-block;zoom: 1">
                                <img src="${pageContext.request.contextPath}/css/pic/information.png"
                                     id="recommendIntro"
                                     width="20"
                                     style="vertical-align: sub;margin-left: 5px;">
                            </div>
                            <!-- Multiline Tooltip -->
                            <div class="mdl-tooltip" for="recommendIntro">
                                <span>发现好股票了？来看看还有哪些股票和它一样好。好了还要更好。</span>
                            </div>
                            <div style="display:inline-block;zoom: 1">
                                <p style="font-size: 20px;font-weight: normal;color: #858585">
                                    相似股票推荐
                                </p>
                            </div>
                            <div id="loadingDiv3" align="left" style="display: none;margin-left: 20px">
                                <div style="width: 150px">
                                    <div>
                                        <img src="${pageContext.request.contextPath}/css/pic/loading.gif"
                                             width="40">
                                    </div>
                                </div>
                            </div>
                            <div class="recommendedStock" style="border: solid 1px #e1e1e1;padding: 20px">
                                <table id="recommendTable"
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

                        <div id="buySellDiv"
                             style="display:none;width: 1000px;margin-top: 40px;margin-bottom: 50px;margin-top: 20px">
                            <ul id="myTab2" class="nav nav-tabs">
                                <li class="active"><a href="#kdj_charts" data-toggle="tab">KDJ</a></li>
                                <li><a href="#boll_charts" data-toggle="tab">BOLL</a></li>

                                <li><a href="#macd_charts" data-toggle="tab">MACD</a></li>
                                <li><a href="#dmi_charts" data-toggle="tab">DMI</a></li>
                                <li><a href="#allAna_charts" data-toggle="tab" id="buySell">买入卖出综合</a></li>
                            </ul>

                            <div class="mdl-tooltip" for="buySell">
                                <span>我们为您综合统计了KDJ、MACD、DMI和布林线分别产生的买入和卖出信号</span>
                            </div>

                            <%--<ul>--%>
                            <%--<li><button onclick="addOptionalStock($("#"))">+关注</button></li>--%>
                            <%--</ul>--%>

                            <div id="myTabContent2" class="tab-content"
                                 style="width: 1000px;height: 600px;border: solid 1px #e1e1e1">
                                <div class="tab-pane fade in active" id="kdj_charts"
                                     style="width: 950px;height: 600px;">
                                </div>

                                <div class="tab-pane fade" id="boll_charts" style="width: 950px;height: 600px;">
                                </div>


                                <div class="tab-pane fade" id="macd_charts" style="width: 950px;height: 600px;">
                                </div>

                                <div class="tab-pane fade" id="dmi_charts" style="width: 950px;height: 600px;">
                                </div>

                                <div class="tab-pane fade" id="allAna_charts" style="width: 950px;height: 600px;">
                                </div>
                            </div>
                        </div>

                        <div id="otherChartsDiv"
                             style="display:none;width: 1000px;margin-top: 40px;margin-bottom: 50px;margin-top: 20px">
                            <ul id="myTab3" class="nav nav-tabs">
                                <li class="active">
                                    <a href="#lineChartDiv" data-toggle="tab" id="compareTab">涨跌幅</a></li>
                                <li><a href="#fluctuationChartDiv" data-toggle="tab" id="fluctuationTab">近期波动</a></li>
                                <li id="innerValueLi"><a href="#innerValue" data-toggle="tab"
                                                         id="innerValueTab">内在价值</a></li>
                            </ul>

                            <div class="mdl-tooltip" for="compareTab">
                                <span>要比较多只股票涨跌幅和对数收益率？<br>Oquantour帮您轻松搞定。</span>
                            </div>

                            <div class="mdl-tooltip" for="fluctuationTab">
                                <span>不喜欢折线图？<br>我们给您提供了日历形式的涨跌幅统计图。滑动右下角的色柱可以查看更小的涨跌幅范围。</span>
                            </div>

                            <div class="mdl-tooltip" for="innerValueTab">
                                <span>股市风险高？来看看我们的股票内在价值评估。<br>综合评价参数大于0，则股票价值被低估；参数小于0，则说明股票市场价高于其应有价值，买进或持有会增加风险。</span>
                            </div>

                            <%--<ul>--%>
                            <%--<li><button onclick="addOptionalStock($("#"))">+关注</button></li>--%>
                            <%--</ul>--%>

                            <div id="myTabContent3" class="tab-content"
                                 style="width: 1000px;height: auto;border: solid 1px #e1e1e1;" align="center">
                                <div class="tab-pane active" id="lineChartDiv">
                                    <div style="display:inline-block;zoom: 1;margin-top: 10px;padding: 30px;width: 100%"
                                    >
                                        <%--<span style="font-size: 18px;font-weight: 300;color: #858585">添加比较股票：</span>--%>
                                        <div style="display:inline-block;">
                                            <span style="font-size: 18px;font-weight: 300;color: #7a7a7a;vertical-align: middle">添加比较股票：&nbsp;&nbsp;</span>

                                            <select id="compare_ui" style="width:300px;outline: none"
                                                    onchange='search(this.value.split(" ")[0], 1)'>
                                                <!-- Dropdown List Option -->
                                            </select>
                                        </div>
                                        <div id="loadingDiv2" align="left" style="display: none;margin-left: 20px">
                                            <div style="width: 150px">
                                                <div>
                                                    <img src="${pageContext.request.contextPath}/css/pic/loading.gif"
                                                         width="40">
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div id="line_charts" style="width: 800px;height: 300px">

                                    </div>

                                    <div id="line_charts2" style="width: 800px;height: 300px">

                                    </div>
                                </div>
                                <div class="tab-pane" id="fluctuationChartDiv" style="padding: 30px;">
                                    <div id="fluctuation_charts" style="width: 800px;height: 750px">

                                    </div>
                                </div>

                                <div class="tab-pane" id="innerValue" style="padding: 30px;">
                                    <div id="innerValue_charts" style="width: 800px;height: 750px">

                                    </div>
                                </div>
                            </div>
                        </div>

                    </div>

                </div>
            </div>

        </div>
    </div>

</div>

<script>
    function str2list(str) {
        var list = str.split(";");
        var newList = [];
        for (var i = 0; i < list.length; i++) {
            newList[i] = list[i].split(",")[0] + ";" + list[i].split(",")[1];
        }
        return newList;
    }

    $(document).ready(function () {
        isBackTesting = 0;
        initial();
        $("#loadingStockDiv").css("display", "inline-block");
        if (window.localStorage.getItem("allStock") == null) {
            $.ajax({
                url: "getAllStockNameAndCode.action",
                dataType: 'json',
                success: function (data) {
                    var jsonObj = eval('(' + data + ')');
                    addAllStockToDownList(jsonObj["stockNameAndCode"]);
                },
                error: function () {
                    fail_prompt("ajax error", 1500)
                }
            });
        } else {
            addAllStockToDownList(str2list(window.localStorage.getItem("allStock")));
        }

        var trigger = $('.hamburger'),
            overlay = $('.overlay'),
            isClosed = false;

        trigger.click(function () {
            hamburger_cross();
        });

        function hamburger_cross() {

            if (isClosed == true) {
                overlay.hide();
                trigger.removeClass('is-open');
                trigger.addClass('is-closed');
                isClosed = false;
            } else {
                overlay.show();
                trigger.removeClass('is-closed');
                trigger.addClass('is-open');
                isClosed = true;
            }
        }

        $('[data-toggle="offcanvas"]').click(function () {
            $('#wrapper').toggleClass('toggled');
        });
    })
    ;
    content = [];

    function addAllStockToDownList(data) {
        for (var i = 0; i < data.length; i++) {
            var row = '' + data[i].split(";")[0] + " " + data[i].split(";")[1];
            content.push(row);
//            console.log(row);
        }

        $("#search_ui").select2({
            data: content
        });

        $("#compare_ui").select2({
            data: content
        });

        $("#loadingStockDiv").hide();

        $('.select2-selection__rendered').text('请输入股票名称／代码');

    }

    function scrollIntoView(id) {
        $(id)[0].scrollIntoView({
            behavior: "smooth"
        })
    }

    function searchStock(stock) {
        window.stockID_global = stock;
        search(stockID_global, 0);
        $("#loadingDiv").css("display", "inline-block");
        ifHasSelected(stockID_global);
        $("#loadingDiv3").css("display", "inline-block");
        getRecommendedStock(stockID_global);
    }


</script>

</body>
</html>
<%@ include file="pageFoot.jsp" %>