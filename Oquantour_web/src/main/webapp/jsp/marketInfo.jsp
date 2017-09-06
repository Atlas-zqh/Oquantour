<%--
  Created by IntelliJ IDEA.
  User: st
  Date: 2017/5/12
  Time: 16:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--<html>--%>
<%--<head>--%>
<%--<meta name="viewport" content="width=device-width, initial-scale=1.0">--%>

<%--&lt;%&ndash;<script src="${pageContext.request.contextPath}/js/jquery-3.2.1.min.js"></script>&ndash;%&gt;--%>
<%--<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">--%>
<%--<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>--%>

<%--<link rel="stylesheet" href="${pageContext.request.contextPath}/css/theme.css">--%>
<%--<link rel="stylesheet" href="${pageContext.request.contextPath}/css/stockInfo.css">--%>
<%--&lt;%&ndash;<script src="http://cdn.static.runoob.com/libs/jquery/2.1.1/jquery.min.js"></script>&ndash;%&gt;--%>
<%--&lt;%&ndash;<script&ndash;%&gt;--%>
<%--&lt;%&ndash;&lt;%&ndash;src="https://code.jquery.com/jquery-3.1.1.min.js"></script>&ndash;%&gt;&ndash;%&gt;--%>
<%--&lt;%&ndash;<script&ndash;%&gt;--%>
<%--&lt;%&ndash;src="https://code.jquery.com/jquery-3.1.1.min.js"&ndash;%&gt;--%>
<%--&lt;%&ndash;integrity="sha256-hVVnYaiADRTO2PzUGmuLJr8BLUSjGIZsDYGmIJLv2b8="&ndash;%&gt;--%>
<%--&lt;%&ndash;crossorigin="anonymous"></script>&ndash;%&gt;--%>
<%--</head>--%>
<%--<body>--%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/material.indigo-pink.min.css">
<script defer src="${pageContext.request.contextPath}/js/material.min.js"></script>

<div align="left" style="width: 100%;margin-top: 40px">

    <div style="display:inline-block;zoom: 1">
        <p style="font-size: 22px;font-weight: normal;color: #8d8d8d">
            <span id="date"></span>龙虎榜
        </p>
    </div>
    <div style="display: inline-block">
        <img src="${pageContext.request.contextPath}/css/pic/question.png" id="TopRankIntro" width="25"
             style="vertical-align: text-bottom">
    </div>
    <div class="mdl-tooltip" for="TopRankIntro">
        <span>龙虎榜是游资们表演的舞台！<br>分析龙虎榜，最重要是避免亏欠，再是分析赚钱。</span>
    </div>
    <table class="mdl-data-table mdl-js-data-table mdl-shadow--2dp" id="topRankTable"
           style="width: 800px;margin-top: 20px">
        <thead>
        <tr>
            <th class="mdl-data-table__cell--non-numeric">代码</th>
            <th class="mdl-data-table__cell--non-numeric">名称</th>
            <th class="mdl-data-table__cell--non-numeric">涨跌幅</th>
            <%--<th class="mdl-data-table__cell--non-numeric">成交额（万）</th>--%>
            <%--<th class="mdl-data-table__cell--non-numeric">买入额（万）</th>--%>
            <th class="mdl-data-table__cell--non-numeric">买入占比</th>
            <%--<th class="mdl-data-table__cell--non-numeric">卖出额</th>--%>
            <th class="mdl-data-table__cell--non-numeric">卖出占比</th>
            <th class="mdl-data-table__cell--non-numeric">上榜原因</th>
        </tr>
        <div class="mdl-tooltip" for="stockInfo">
            Share your content<br>via social media
        </div>
        </thead>
        <tbody>
        </tbody>
    </table>


</div>

<%--</body>--%>
<%--</html>--%>
