<%--<%@taglib prefix="struts" uri="/struts-tags" %>--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="navBar.jsp" %>

<%----%>
<!DOCTYPE html>
<html>
<head>
    <%--<meta charset="UTF-8">--%>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Welcome!</title>
    <script src="${pageContext.request.contextPath}/js/getStockInfo.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/theme.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/homePage.css">

    <script>
        function startUse() {
            $(".funcDiv")[0].scrollIntoView({
                behavior: "smooth"
            })
        }
    </script>


</head>


<body>

<div>

    <div class="homeBG">
        <div style="text-align: center">
            <p id="head">
                Oquantour
            </p>

            <p id="introduction">
                在这里，体验海量数据、策略回测分析、投资组合实盘模拟
            </p>
            <div id="startDiv">
                <button id="startButton" onclick="startUse()">
                    开始探索
                </button>
            </div>
        </div>

    </div>

    <div class="funcDiv">
        <div>
            <p class="funcIntro">享受完备功能</p>
        </div>
        <div class="funcMock" id="mock1"
             onclick="window.location.href='${pageContext.request.contextPath}/jsp/stockInfo.jsp'">
            <img class="mockPic" src="${pageContext.request.contextPath}/css/pic/stockIcon.png" width="70%">
            <p class="funcName" id="func1" style="color: #D1A5A5;margin-top: 20px">个股信息</p>
        </div>

        <div class="funcMock" onclick="window.location.href='${pageContext.request.contextPath}/jsp/stockInfo.jsp'">
            <img class="mockPic" src="${pageContext.request.contextPath}/css/pic/compareIcon.png" width="70%">
            <p class="funcName" style="color: #A1B2C8;margin-top: 20px">多股比较</p>
        </div>

        <div class="funcMock" onclick="window.location.href='${pageContext.request.contextPath}/jsp/market.jsp'">
            <img class="mockPic" src="${pageContext.request.contextPath}/css/pic/marketIcon.png" width="70%">
            <p class="funcName" style="color: #ADC7C4;margin-top: 20px">市场分析</p>
        </div>

        <div class="funcMock" onclick="window.location.href='${pageContext.request.contextPath}/jsp/backTest.jsp'">
            <img class="mockPic" src="${pageContext.request.contextPath}/css/pic/backtestIcon.png" width="70%">
            <p class="funcName" style="color: #BFAACF;margin-top: 20px">策略回测</p>
        </div>

    </div>

</div>


</body>
</html>
<%@ include file="pageFoot.jsp" %>