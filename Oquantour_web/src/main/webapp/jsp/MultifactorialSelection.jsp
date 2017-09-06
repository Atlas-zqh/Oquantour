<%--
  Created by IntelliJ IDEA.
  User: st
  Date: 2017/6/12
  Time: 20:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script src="${pageContext.request.contextPath}/js/semantic.min.js"></script>

<style>
    .mdl-data-table td, .mdl-data-table td .mdl-data-table__select {
        text-align: left;
    }
</style>
<div class="firstStepDiv">
    <div class="ui floating labeled icon dropdown button" id="indexSelectionMenu"
         style="display:inline-block;font-size:14px;background-color:#fff;width:200px;border:solid 1px #e1e1e1;margin-top: 30px">
        <i class="Share Alternate icon"></i>
        <span class="text">选择自定义因子</span>
        <div class="menu">
            <div class="header">
                <i class="tags icon"></i>
                价值能力因子
            </div>
            <div class="divider"></div>
            <div class="item">
                市净率
            </div>
            <div class="item">
                市盈率
            </div>
            <div class="item">
                每股收益同比
            </div>
            <div class="item">
                收入同比
            </div>
            <div class="item">
                利润同比
            </div>
            <div class="item">
                净利润率
            </div>
            <div class="header">
                <i class="tags icon"></i>
                盈利能力因子
            </div>
            <div class="divider"></div>
            <div class="item">
                净资产收益率
            </div>
            <div class="item">
                净利润同比
            </div>
            <div class="item">
                净利率
            </div>
            <div class="item">
                毛利率
            </div>
            <div class="header">
                <i class="tags icon"></i>
                营运能力因子
            </div>
            <div class="divider"></div>
            <div class="item">
                应收账款周转率
            </div>
            <div class="item">
                存货周转率
            </div>
            <div class="header">
                <i class="tags icon"></i>
                成长能力因子
            </div>
            <div class="divider"></div>
            <div class="item">
                主营业务收入增长率
            </div>
            <div class="item">
                净利率增长率
            </div>
            <div class="item">
                净资产增长率
            </div>
            <div class="item">
                总资产增长率
            </div>
            <div class="item">
                每股收益增长率
            </div>
            <div class="header">
                <i class="tags icon"></i>
                偿债能力因子
            </div>
            <div class="divider"></div>
            <div class="item">
                流动比率
            </div>
            <div class="item">
                速动比率
            </div>
            <div class="item">
                现金比率
            </div>
            <div class="item">
                股东权益比率
            </div>
            <div class="item">
                股东权益增长率
            </div>
            <div class="header">
                <i class="tags icon"></i>
                现金流量因子
            </div>
            <div class="item">
                经营现金净流量对销售收入比率
            </div>
            <div class="item">
                资产的经营现金流量回报率
            </div>
            <div class="item">
                经营现金净流量与净利润的比率
            </div>
            <div class="item">
                经营现金净流量对负债比率
            </div>
            <div class="item">
                现金流量比率
            </div>

        </div>
    </div>

    <div class="indexQuestion" id="question1" style="display: inline-block;outline: none"
         onclick="openInfo()">
        <img src="${pageContext.request.contextPath}/css/pic/question.png" width="30">
    </div>
    <div class="mdl-tooltip" id="toolTip" for="question1">
        什么是多因子？
        <br>
        点击查看因子信息
    </div>

    <div style="display:inline-block;">
        <p style="font-size: 14px;font-weight: normal;color: #969696">（若不选择因子，则默认选择全部股票）</p>
    </div>

    <table class="mdl-data-table mdl-js-data-table mdl-shadow--2dp" id="selectStockTable"
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
        </tbody>
    </table>

    <div align="right" style="margin-top: 40px;margin-bottom: 40px">
        <button class="confirmChoose" onclick="getSecurity()">
            确认选股
        </button>
    </div>

    <div id="selectingDiv" align="center" style="margin-top: 40px;margin-bottom: 40px;display: none">
        <!-- MDL Progress Bar with Indeterminate Progress -->
        <p style="font-weight: 300;font-size: 18px;color: #9b9b9b;">
            正在选择股票，请稍等......
        </p>
        <div class="mdl-progress mdl-js-progress mdl-progress__indeterminate"
        ></div>
    </div>
</div>


<div id="filteredStockDiv" style="display: none;margin-top: 40px">
    <button type="button" class="btn btn-primary" data-toggle="collapse"
            data-target="#selectedStocks"
            style="background-color: #95a6b3;border: none;border-radius: 0;font-weight: 300;">
        查看筛出的股票
    </button>

    <div id="selectedStocks" class="collapse in" style="margin-top: 30px;">
        <div style="height: 300px;overflow: auto">
            <p style="font-size: 18px;font-weight: 300;color: #929292">
                * 共有 <i><span id="stockTotalNum" style="color: #D1A5A5">0</span></i>
                只股票
            </p>
            <table class="mdl-data-table mdl-js-data-table mdl-shadow--2dp" id="filteredStockTable"
                   style="width: 100%;margin-top: 20px">
                <thead>
                <tr>
                    <th class="mdl-data-table__cell--non-numeric">股票代码</th>
                    <th class="mdl-data-table__cell--non-numeric">股票名称</th>
                </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
        </div>
    </div>
</div>
