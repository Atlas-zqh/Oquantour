<%--
  Created by IntelliJ IDEA.
  User: st
  Date: 2017/6/5
  Time: 16:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="navBar.jsp" %>

<html>
<head>
    <title>添加组合</title>

    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <%--<link rel="stylesheet" href="${pageContext.request.contextPath}/css/rangeslider.css">--%>
    <%--<script src="${pageContext.request.contextPath}/js/rangeslider.min.js"></script>--%>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/addNewCombine.css">
    <script src="${pageContext.request.contextPath}/js/combineComponent.js"></script>

    <script>
        $(document).ready(function () {
            addCombineStock();
        })
    </script>

</head>

<html>
<body>

<div align="center">
    <div style="width: 1100px;height: 900px;margin-top: 40px" align="left">
        <div class="infoDiv">
            <div style="display: inline-block;zoom: 1">
                <p style="color: #95a6b3;font-weight: normal;font-size: 20px;margin-right: 30px">
                    || 调仓
                </p>
            </div>

            <div style="margin: 40px">
                <%@ include file="combineComponent.jsp" %>
            </div>
        </div>
    </div>
</div>

</body>
</html>

<%@ include file="pageFoot.jsp" %>