<%--
  Created by IntelliJ IDEA.
  User: st
  Date: 2017/6/12
  Time: 18:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div id="loadingDiv" align="center" style="margin-top: 40px;margin-bottom: 40px;display: none">
    <!-- MDL Progress Bar with Indeterminate Progress -->
    <p style="font-weight: 300;font-size: 18px;color: #9b9b9b;">
        正在回测中，请稍等......
    </p>
    <div class="mdl-progress mdl-js-progress mdl-progress__indeterminate"
    ></div>
</div>


<!--回测结果图表部分-->
<div id="backTestResult" style="margin-bottom: 50px;display: none">

    <div>
                    <span style="font-size: 20px;font-weight: normal;color: #858585;margin-bottom: 10px">
                        回测结果：
                    </span>
        <%--<button onclick="showWinner()" style="font-size: 14px;font-weight: 300;color: #4e759f;text-decoration: underline">--%>
        <%--查看赢家组合--%>
        <%--</button>--%>
        <div style="float: right">
            <img src="${pageContext.request.contextPath}/css/pic/question.png" id="strategyScoreIntro"
                 width="30"
                 style="vertical-align: bottom;">
            <div class="mdl-tooltip" for="strategyScoreIntro" style="text-align: left">
                <span>抗风险能力：反映组合最大回撤的大小。</span>
                <br>
                <span>稳定性：反映组合收益是否稳定。净值变化波动越小的组合，在该项指标上分值越高。</span>
                <br>
                <span>盈利能力：反映组合的赚钱能力，组合收益率越高，该项分值越高。</span>
                <br>
                <span>持股分散度：考核组合持股是否分散，持股越分散，该项分值越高。</span>
            </div>
        </div>
    </div>


    <div style="margin-top: 20px;padding: 0">
        <div style="display:inline-block;width: 60%;border-right: solid 1px #e1e1e1;">
            <table style="width: 100%;height:300px;border-collapse: separate;border-spacing: 10px;margin-bottom: 20px">
                <tr>
                    <td>
                                <span class="indexInfo" id="strategyScoreLabel">
                                    总评分：
                                </span>
                        <div class="mdl-tooltip" for="strategyScoreLabel">
                            <span>策略总评分</span>
                            <br>
                            <span>通过对各策略分析回测指标进行加权计算得出，基本分为1000分，分数越高，说明策略越优秀。</span>
                        </div>
                    </td>
                    <td>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                    <td>
                                <span class="indexInfo" id="strategyScore">
                                    error
                                </span>
                    </td>
                    <td>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                    <td>
                                <span class="indexInfo" id="alphaLabel">
                                    阿尔法：
                                </span>
                        <div class="mdl-tooltip" for="alphaLabel">
                            <span>Alpha</span>
                            <br>
                            <span>Alpha是投资者获得与市场波动无关的回报。比如投资者获得了15%的回报，其基准获得了10%的回报，那么Alpha或者价值增值的部分就是5%。</span>
                        </div>
                    </td>
                    <td>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                    <td>
                                <span class="indexInfo" id="alpha">
                                    error
                                </span>
                    </td>
                </tr>
                <tr>
                    <td>
                                <span class="indexInfo" id="betaLabel">
                                    贝塔：
                                </span>
                        <div class="mdl-tooltip" for="betaLabel">
                            <span>Beta</span>
                            <br>
                            <span>表示投资的系统性风险，反映了策略对大盘变化的敏感性。例如一个策略的Beta为1.5，则大盘涨1%的时候，策略可能涨1.5%，反之亦然；如果一个策略的Beta为-1.5，说明大盘涨1%的时候，策略可能跌1.5%，反之亦然。</span>
                        </div>
                    </td>
                    <td>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                    <td>
                                <span class="indexInfo" id="beta">
                                    error
                                </span>
                    </td>
                    <td>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                    </td>

                    <td>
                                <span class="indexInfo" id="sharpeLabel">
                                    夏普比率：
                                </span>
                        <div class="mdl-tooltip" for="sharpeLabel">
                            <span>Sharpe</span>
                            <br>
                            <span>表示每承受一单位总风险，会产生多少的超额报酬，可以同时对策略的收益与风险进行综合考虑。</span>
                        </div>
                    </td>
                    <td>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                    <td>
                                <span class="indexInfo" id="sharpe">
                                    error
                                </span>
                    </td>
                </tr>

                <tr>
                    <td>
                                <span class="indexInfo" id="informationRatioLabel">
                                    信息比率：
                                </span>
                        <div class="mdl-tooltip" for="informationRatioLabel">
                            <span>Information Ratio</span>
                            <br>
                            <span>衡量单位超额风险带来的超额收益。信息比率越大，说明该策略单位跟踪误差所获得的超额收益率越高。</span>
                        </div>
                    </td>
                    <td>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                    <td>
                                <span class="indexInfo" id="informationRatio">
                                    error
                                </span>
                    </td>
                    <td>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                    </td>

                    <td>
                                <span class="indexInfo" id="algorithmVolatilityLabel">
                                    策略波动率：
                                </span>
                        <div class="mdl-tooltip" for="algorithmVolatilityLabel">
                            <span>Algorithm Volatility</span>
                            <br>
                            <span>用来衡量策略的风险性，波动越大代表策略风险越高。</span>
                        </div>
                    </td>
                    <td>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                    <td>
                                <span class="indexInfo" id="algorithmVolatility">
                                    error
                                </span>
                    </td>
                </tr>
                <tr>
                    <td>
                                <span class="indexInfo" id="benchmarkVolatilityLabel">
                                    基准波动率：
                                </span>
                        <div class="mdl-tooltip" for="benchmarkVolatilityLabel">
                            <span>Benchmark Volatility</span>
                            <br>
                            <span>用来衡量基准的风险性，波动越大代表基准风险越高。</span>
                        </div>
                    </td>
                    <td>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                    <td>
                                <span class="indexInfo" id="benchmarkVolatility">
                                    error
                                </span>
                    </td>
                    <td>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                    <td>
                                <span class="indexInfo" id="maxDrawdownLabel">
                                    最大回撤：
                                </span>
                        <div class="mdl-tooltip" for="maxDrawdownLabel">
                            <span>Max Drawdown</span>
                            <br>
                            <span>描述策略可能出现的最糟糕的情况，最极端可能的亏损情况。</span>
                        </div>
                    </td>
                    <td>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                    <td>
                                <span class="indexInfo" id="maxDrawdown">
                                    error
                                </span>
                    </td>
                </tr>
                <tr>
                    <td>
                                <span class="indexInfo">
                                    策略年化收益率：
                                </span>
                    </td>
                    <td>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                    <td>
                                <span class="indexInfo" id="totalAnnualizedReturns">
                                    error
                                </span>
                    </td>
                    <td>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                    </td>

                    <td>
                                <span class="indexInfo">
                                    基准年化收益率：
                                </span>
                    </td>
                    <td>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                    </td>
                    <td>
                                <span class="indexInfo" id="benchmarkAnnualizedReturns">
                                    error
                                </span>
                    </td>
                </tr>
            </table>
        </div>

        <div id="radarChart" style="display:inline-block;padding: 0;height: 300px;width: 370px">

        </div>

    </div>


    <ul id="myTab1" class="nav nav-tabs">
        <li class="active">
            <a href="#line_chart" data-toggle="tab">
                基准策略比较图
            </a>
        </li>
        <li><a href="#bar_chart" data-toggle="tab">收益率直方图</a></li>
        <li><a href="#findBest_chart" data-toggle="tab" id="bestHoldingPeriodTab">最佳持有期</a></li>
        <li id="bestHoldingNumLi" style="display: none"><a href="#findBestNum_chart"
                                                           data-toggle="tab" id="bestHoldingNumTab">最佳持仓数</a></li>
    </ul>

    <div class="mdl-tooltip" for="bestHoldingPeriodTab">
        <span>隔多久调仓比较好？<br>超额收益率和策略胜率分析图，帮您寻找当前回测条件下的最佳持有期。</span>

    </div>

    <div class="mdl-tooltip" for="bestHoldingNumTab">
        <span>持仓几只股票比较好？<br>超额收益率和策略胜率分析图，帮您寻找当前回测条件下的最佳持仓股票数。</span>

    </div>

    <div id="myTabContent1" class="tab-content" style="width: 1060px;height: auto">
        <div class="tab-pane fade in active" id="line_chart" style="width: 1060px;height: 600px">
        </div>

        <div class="tab-pane fade" id="bar_chart" style="width: 1060px;height: 600px">
        </div>

        <div class="tab-pane fade" id="findBest_chart" style="width: 1060px;height: 1000px">
            <div id="extra_chart" style="width: 1060px;height: 500px"></div>
            <div id="winning_chart" style="width: 1060px;height: 500px"></div>
        </div>

        <div class="tab-pane fade" id="findBestNum_chart" style="width: 1060px;height: 1000px">
            <div id="extraNum_chart" style="width: 1060px;height: 500px"></div>
            <div id="winningNum_chart" style="width: 1060px;height: 500px"></div>
        </div>
    </div>

</div>

<div id="strategyScoreDiv" style="margin-bottom: 50px;display: none">
    <label class="mdl-checkbox mdl-js-checkbox mdl-js-ripple-effect" for="ifShowWinner">
        <input type="checkbox" id="ifShowWinner" class="mdl-checkbox__input" onclick="showWinner()">
        <span class="mdl-checkbox__label" style="font-family:  'PingFang SC', serif;
                        font-weight: 300;
                        font-size: 18px;
                        color: #9b9b9b;">查看赢家组合</span>
    </label>
</div>


<div id="displayWinnerDiv" style="margin-bottom: 50px;display: none">
    <div>
        <p style="font-size: 20px;font-weight: normal;color: #858585">
            调仓日赢家组合表
        </p>
    </div>

    <div style="border: solid 1px #f1f1f1;padding: 20px;height: 600px;overflow: auto;">

        <div style="width: 100%;height: 100%">
            <table class="totalWinnerTable" id="winnerTable">
            </table>
        </div>
    </div>
</div>
