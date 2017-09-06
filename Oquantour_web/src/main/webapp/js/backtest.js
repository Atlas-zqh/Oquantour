function drawBackTestResultChart(jsonObj, strategyType) {
    var json = splitBackTestData(jsonObj);
    setIndex(json.index);
    drawBackTestLineChart('line_chart', json.dates, json.stdValue, json.backTestValue, "基准收益率", "策略收益率");
    drawBackTestBarChart(json);
    drawWinner(json);
    getStraScore(json);
    drawAreaChart("extra_chart", json.bestX, json.extra, "超额收益率", '#F5C07D')
    drawAreaChart("winning_chart", json.bestX, json.winning, "策略胜率", '#CAF3A4')

    if (strategyType != "动量策略") {
        $("#bestHoldingNumLi").show();
        var x = [];
        var extra = [];
        var winning = [];
        var bestHoldingNum = json.bestHoldingNum;
        for (var i = 0; i < bestHoldingNum.length; i++) {
            x.push(bestHoldingNum[i]["x_axis"])
            extra.push(bestHoldingNum[i]["extraReturnRate"].toFixed(3))
            winning.push(bestHoldingNum[i]["winningRate"].toFixed(3))
        }
        drawAreaChart("extraNum_chart", x, extra, "超额收益率", '#F5C07D')
        drawAreaChart("winningNum_chart", x, winning, "策略胜率", '#CAF3A4')

    } else {
        $("#bestHoldingNumLi").hide();
    }
    $("#strategyScoreDiv").show();
    $("#backTestResult").fadeIn();
    $("#backTestResult")[0].scrollIntoView({
        behavior: "smooth"
    });
}

function splitBackTestData(rawData) {
    var dates = [];
    var backTestValue = [];
    var stdValue = [];
    var returnRateDistribution = [];
    var index = [];
    var winner = [];
    var m = 0;
    var score = [];
    var bestHolding = [];
    var bestHoldingNum = [];
    for (var i = 1; i < rawData.length; i++) {
        if (rawData[i]["date"] != null) {
            dates[i - 1] = rawData[i]["date"];
            backTestValue[i - 1] = rawData[i]["backTestValue"];
            stdValue[i - 1] = rawData[i]["stdValue"];
        } else if (rawData[i]["returnRateDistribution"] != null) {
            returnRateDistribution = rawData[i]["returnRateDistribution"];
        } else if (rawData[i]["backTestStatistics"] != null) {
            index = rawData[i]["backTestStatistics"];
        } else if (rawData[i]["winnerDate"] != null) {
            winner[m] = rawData[i];
            m++;
        } else {
            score[0] = rawData[i]["抗风险能力"].toFixed(2);
            score[1] = rawData[i]["稳定性"].toFixed(2);
            score[2] = rawData[i]["盈利能力"].toFixed(2);
            score[3] = rawData[i]["持股分散度"].toFixed(2);
            score[4] = rawData[i]["评分"];
            bestHolding = rawData[i]["最佳持有期"];
            bestHoldingNum = rawData[i]["最佳持仓数"];
        }
    }

    var bestX = [];
    var extra = [];
    var winning = [];
    for (var i = 0; i < bestHolding.length; i++) {
        bestX.push(bestHolding[i]["x_axis"])
        extra.push(bestHolding[i]["extraReturnRate"].toFixed(3))
        winning.push((bestHolding[i]["winningRate"] * 100).toFixed(3))
    }

    var toFixedReturnRateDistribution = [];
    for (var j = 0; j < returnRateDistribution.length; j++) {
        toFixedReturnRateDistribution[j] = returnRateDistribution[j].toFixed(3);
    }
    return {
        dates: dates,
        backTestValue: backTestValue,
        stdValue: stdValue,
        returnRateDistribution: returnRateDistribution,
        index: index,
        toFixedReturnRateDistribution: toFixedReturnRateDistribution,
        winner: winner,
        score: score,
        bestX: bestX,
        extra: extra,
        winning: winning,
        bestHoldingNum: bestHoldingNum
    }
}

function setIndex(data) {
    $("#alpha").html(data["alpha"].toFixed(3));
    $("#beta").html(data["beta"].toFixed(3));
    $("#sharpe").html((data["sharpe"] * 100).toFixed(3) + "%");
    $("#informationRatio").html((data["informationRatio"] * 100).toFixed(3) + "%");
    $("#algorithmVolatility").html((data["algorithmVolatility"] * 100).toFixed(3) + "%");
    $("#benchmarkVolatility").html((data["benchmarkVolatility"] * 100).toFixed(3) + "%");
    $("#maxDrawdown").html(data["maxDrawdown"].toFixed(3));
    $("#totalAnnualizedReturns").html((data["annualizedReturnRate"] * 100).toFixed(3) + "%");
    $("#benchmarkAnnualizedReturns").html((data["stdAnnualizedReturnRate"] * 100).toFixed(3) + "%");
}

function drawBackTestLineChart(chartID, x, series1, series2, s1Name, s2Name) {
    var myChart = echarts.init(document.getElementById(chartID));
    var colors = ['#DAB3F5', '#F39C98', '#CAF3A4'];
    myChart.setOption(option = {
        color: colors,

        tooltip: {
            trigger: 'axis',
            axisPointer: {
                animation: true
            }
        },
        legend: {
            data: [s1Name, s2Name]
        },
        // grid: {
        //     top: 70,
        //     bottom: 50
        // },
        xAxis: [
            {
                type: 'category',
                axisTick: {
                    alignWithLabel: true
                },
                axisLine: {
                    onZero: false,
                    lineStyle: {
                        color: '#F5C07D'
                    }
                },
                data: x
            },
            {
                show: false,
                type: 'category',
                axisTick: {
                    alignWithLabel: true
                },
                axisLine: {
                    onZero: false,
                    lineStyle: {
                        color: '#A9CCF5'
                    }
                },
                data: x
            }
        ],
        yAxis: [
            {
                type: 'value',
                name: '(%)',
                nameGap: 10
            }
        ],
        dataZoom: [
            {
                show: true,
                realtime: true,
                start: 0,
                end: 100,
                xAxisIndex: [0, 1]
            },
            {
                type: 'inside',
                realtime: true,
                start: 0,
                end: 100,
                xAxisIndex: [0, 1]
            }
        ],
        series: [
            {
                symbol: 'circle',
                name: s1Name,
                type: 'line',
                xAxisIndex: 1,
                smooth: true,
                data: series1,
                itemStyle: {
                    normal: {
                        color: '#F5C07D',
                        lineStyle: {
                            color: '#F5C07D',
                            width: 2.5
                        }
                    }
                }
            },
            {
                symbol: 'circle',
                name: s2Name,
                type: 'line',
                smooth: true,
                data: series2,
                itemStyle: {
                    normal: {
                        color: '#A9CCF5',
                        lineStyle: {
                            color: '#A9CCF5',
                            width: 2.5
                        }
                    }
                }
            }
        ]
    });
}

function drawBackTestBarChart(data) {
    var myChart = echarts.init(document.getElementById('bar_chart'));
    var bins = ecStat.histogram(data.toFixedReturnRateDistribution);
    myChart.setOption(option = {
        color: '#A9CCF5',
        grid: {
            left: '3%',
            right: '3%',
            bottom: '3%',
            top: 80,
            containLabel: true
        },
        xAxis: [{
            type: 'value',
            scale: true //这个一定要设，不然barWidth和bins对应不上
        }],
        yAxis: [{
            type: 'value'
        }],
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                animation: true
            }
        },
        series: [{
            name: 'height',
            symbol: 'circle',
            type: 'bar',
            barWidth: '99.3%',
            itemStyle: {
                normal: {
                    color: '#A9CCF5'
                }
            },
            label: {
                normal: {
                    show: true,
                    position: 'insideTop',
                    formatter: function (params) {
                        return params.value[1];
                    }
                }
            },
            data: bins.data
        }
        ]
    });
}

function getStockName(code) {
    var allStock = window.localStorage.getItem("allStock").split(";");
    for (var i = 0; i < allStock.length; i++) {
        if (allStock[i].split(",")[0] == code)
            return allStock[i].split(",")[1];
    }
}

function drawWinner(data) {
    var winner = data.winner;
    var winnerSize = winner.length;
    //把调仓日赢家组合放入表格
    var currentRows = 0;
    var insertTr = 0;

    $("#winnerTable").empty();
    for (var offset = 0; offset < winnerSize; offset++) {

        currentRows = document.getElementById("winnerTable").rows.length;
        insertTr = document.getElementById("winnerTable").insertRow(currentRows);

        insertTr.style.textAlign = "center";
        var insertTd = insertTr.insertCell(0);
        insertTd.innerHTML = winner[offset]["winnerDate"];
///                alert(winner[offset]["winnerDate"]);
        insertTd.colSpan = 8;
        insertTd.style.textAlign = "center";
        insertTd.style.backgroundColor = "#c5ced5";
        insertTd.style.color = "#fff";
        insertTd.style.fontSize = "16px";
        insertTd.colSpan = 8;

        insertTr = document.getElementById("winnerTable").insertRow(currentRows + 1);
        insertTr.className = "winnerTableSpecInfoHead";
        var insertTd = insertTr.insertCell(0);
        insertTd.innerHTML = "代码";
        insertTd.style.color = "#7a7a7a";
        var insertTd = insertTr.insertCell(1);
        insertTd.innerHTML = "名称";
        insertTd.style.color = "#7a7a7a";
        var insertTd = insertTr.insertCell(2);
        insertTd.innerHTML = "开盘价";
        insertTd.style.color = "#7a7a7a";
        var insertTd = insertTr.insertCell(3);
        insertTd.innerHTML = "收盘价";
        insertTd.style.color = "#7a7a7a";
        var insertTd = insertTr.insertCell(4);
        insertTd.innerHTML = "最高价";
        insertTd.style.color = "#7a7a7a";
        var insertTd = insertTr.insertCell(5);
        insertTd.innerHTML = "最低价";
        insertTd.style.color = "#7a7a7a";
        var insertTd = insertTr.insertCell(6);
        insertTd.innerHTML = "份额";
        insertTd.style.color = "#7a7a7a";

        for (var j = offset; j < winnerSize; j++) {
            if (winner[offset]["winnerDate"] == winner[j]["winnerDate"]) {
                currentRows = document.getElementById("winnerTable").rows.length;
                insertTr = document.getElementById("winnerTable").insertRow(currentRows);
                var insertTd = insertTr.insertCell(0);
                insertTd.style.color = "#7a7a7a";
                insertTd.innerHTML = "<span onclick='goToStockInfo(this.innerHTML)' style='cursor: pointer'>" + data.winner[j]["stockCode"] + "</span>";
                var insertTd = insertTr.insertCell(1);
                insertTd.style.color = "#7a7a7a";
                insertTd.innerHTML = getStockName(data.winner[j]["stockCode"]);
//                        var insertTd = insertTr.insertCell(2);
//                        insertTd.innerHTML = data.winner[j]["stockChg"];
                var insertTd = insertTr.insertCell(2);
                insertTd.style.color = "#7a7a7a";
                insertTd.innerHTML = data.winner[j]["stockOpen"];
                var insertTd = insertTr.insertCell(3);
                insertTd.style.color = "#7a7a7a";
                insertTd.innerHTML = data.winner[j]["stockClose"];
                var insertTd = insertTr.insertCell(4);
                insertTd.style.color = "#7a7a7a";
                insertTd.innerHTML = data.winner[j]["stockHigh"];
                var insertTd = insertTr.insertCell(5);
                insertTd.style.color = "#7a7a7a";
                insertTd.innerHTML = data.winner[j]["stockLow"];
                var insertTd = insertTr.insertCell(6);
                insertTd.style.color = "#7a7a7a";
                insertTd.innerHTML = data.winner[j]["share"];
            } else {
                offset = j;
                break;
            }
        }
    }


}

function getStraScore(data) {
//            alert(data.score[0]);
    var myChart = echarts.init(document.getElementById('radarChart'));
    myChart.setOption(option = {
//                title: {
//                    text: '基础雷达图'
//                },
        tooltip: {},
        legend: {
            show: false
        },
        radar: {
            // shape: 'circle',
            indicator: [
                {name: '抗风险能力', max: 1},
                {name: '稳定性', max: 1},
                {name: '盈利能力', max: 1},
                {name: '持股分散度', max: 1}
            ]
        },
        series: [{
            type: 'radar',
            lineStyle: {
                normal: {
                    color: '#F5C07D',
                }
            },
            itemStyle: {
                normal: {
                    color: '#F5C07D',
                }
            },
            // areaStyle: {normal: {}},
            data: [
                {
                    value: [data.score[0], data.score[1], data.score[2], data.score[3]],
                    name: '策略评分'
                }
            ]
        }]
    });
    $("#strategyScore").html(data.score[4]);
}

function drawAreaChart(id, date, y, name, color) {
    var myChart = echarts.init(document.getElementById(id));
    var colors = ['#F5C07D', '#F39C98', '#CAF3A4'];
    myChart.setOption(option = {
        color: colors,

        tooltip: {
            trigger: 'axis',
            axisPointer: {
                animation: true
            }
        },
        legend: {
            data: [name]
        },
        // grid: {
        //     top: 70,
        //     bottom: 50
        // },
        xAxis: [
            {
                type: 'category',
                axisTick: {
                    alignWithLabel: true
                },
                axisLine: {
                    onZero: false,
                    lineStyle: {
                        color: '#F5C07D'
                    }
                },
                data: date
            },
            {
                show: false,
                type: 'category',
                axisTick: {
                    alignWithLabel: true
                },
                axisLine: {
                    onZero: false,
                    lineStyle: {
                        color: '#A9CCF5'
                    }
                },
                data: date
            }
        ],
        yAxis: [
            {
                type: 'value',
                name: '(%)',
                nameGap: 10
            }
        ],
        dataZoom: [
            {
                show: true,
                realtime: true,
                start: 0,
                end: 100,
                xAxisIndex: [0, 1]
            },
            {
                type: 'inside',
                realtime: true,
                start: 0,
                end: 100,
                xAxisIndex: [0, 1]
            }
        ],
        series: [
            {
                symbol: 'none',
                name: name,
                type: 'line',
                xAxisIndex: 1,
                smooth: true,
                data: y,
                itemStyle: {
                    normal: {
                        color: color,
                        lineStyle: {
                            color: color,
                            width: 2.5
                        }
                    }
                }, areaStyle: {
                normal: {
                    color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                        offset: 0,
                        color: color
                    }, {
                        offset: 1,
                        color: color
                    }])
                }
            },
            }
        ]
    })
}

function showWinner() {
    if ($("#ifShowWinner").prop("checked")) {
        $("#displayWinnerDiv").fadeIn();
        $("#displayWinnerDiv")[0].scrollIntoView({
            behavior: "smooth"
        })
    } else {
        $("#displayWinnerDiv").css("display", "none");
    }
}

function goToStockInfo(stockID) {
    console.log(stockID)
    window.open("../jsp/stockInfo.jsp?stockName=" + stockID);
}

