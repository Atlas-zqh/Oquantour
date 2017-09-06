<%--
  Created by IntelliJ IDEA.
  User: st
  Date: 2017/5/12
  Time: 15:22
  To change this template use File | Settings | File Templates.
--%>


<%@taglib prefix="struts" uri="/struts-tags" %>
<%@ include file="userAuth.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%--<%--%>
<%--String path = request.getContextPath();--%>
<%--String basePath = request.getScheme() + "://"--%>
<%--+ request.getServerName() + ":" + request.getServerPort()--%>
<%--+ path;--%>
<%--request.setAttribute("basePath", basePath);--%>
<%--%>--%>

<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <%--<script src="../resources/jsp/jquery-1.8.3.min.js"></script>--%>
    <%--<script src="bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>--%>
    <struts:head theme="xhtml"/>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/material.icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/material.indigo-pink.min.css">
    <script defer src="${pageContext.request.contextPath}/js/material.min.js"></script>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>

    <script src="${pageContext.request.contextPath}/js/echarts.js"></script>
    <script src="${pageContext.request.contextPath}/js/ecStat.min.js"></script>

    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/semantic.min.css">
    <script src="${pageContext.request.contextPath}/js/semantic.min.js"></script>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/flatpickr.min.css">

    <link href="${pageContext.request.contextPath}/css/select2.min.css" rel="stylesheet"/>
    <script src="${pageContext.request.contextPath}/js/select2.min.js"></script>

    <script src="${pageContext.request.contextPath}/js/smoothscroll.js"></script>

    <script src="${pageContext.request.contextPath}/js/alert.js"></script>
    <script src="${pageContext.request.contextPath}/js/g2.js"></script>

    <script src="${pageContext.request.contextPath}/js/underscore.js"></script>

    <script src="${pageContext.request.contextPath}/js/amcharts.js"></script>
    <script src="${pageContext.request.contextPath}/js/serial.js"></script>
    <script src="${pageContext.request.contextPath}/js/patterns.js"></script>
    <script src="https://www.amcharts.com/lib/3/plugins/export/export.min.js"></script>
    <link rel="stylesheet" href="https://www.amcharts.com/lib/3/plugins/export/export.css" type="text/css" media="all"/>

    <%--<script src="../js/jquery-3.2.1.min.js"></script>--%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/alert.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/theme.css">


    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/homePage.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/userInfo.css">

    <script>
        <%--点击登录按钮时，跳出登录框--%>
        $(document).ready(function () {
            $("#loginButton").click(function () {
                $("#loginButton").css("color", "#f2cdd2");
                $(".bgShadow").fadeIn();
                $(".login-Register-Panel").fadeIn("3000");
                $(".register-panel").hide();
                $(".login-panel").show();
                $("#loginButton-panel").css("border-bottom", "solid 2px #95a6b3");
                $("#registerButton-panel").css("border-bottom", "0");
                $("body").css("overflow", "hidden");
            })
        });
        <%--点击注册按钮时，跳出注册框--%>
        $(document).ready(function () {
            $("#registerButton").click(function () {
                $("#registerButton").css("color", "#f2cdd2");
                $(".bgShadow").fadeIn();
                $(".login-Register-Panel").fadeIn("3000");
                $(".register-panel").show();
                $(".login-panel").hide();
                $("#registerButton-panel").css("border-bottom", "solid 2px #95a6b3");
                $("#loginButton-panel").css("border-bottom", "0");
                $("body").css("overflow", "hidden");
            })
        });
        <%--关闭登录注册框--%>
        $(document).ready(function () {
            $(".closeButton").click(function () {
                $("#loginButton").css("color", "#ffffff");
                $("#registerButton").css("color", "#ffffff");
                $(".bgShadow").fadeOut();
                $(".login-Register-Panel").fadeOut();
                $("body").css("overflow", "auto");
            })
        });
        <%--在登录注册框中切换成注册--%>
        $(document).ready(function () {
            $("#registerButton-panel").click(function () {
                $(".login-panel").hide();
                $(".register-panel").fadeIn();
                $("#loginButton-panel").css("border-bottom", "0");
                $("#registerButton-panel").css("border-bottom", "solid 2px #D1A5A5");
                $("#registerButton").css("color", "#f2cdd2");
                $("#loginButton").css("color", "#ffffff");
            })
        });
        <%--在登录注册框中切换成登录--%>
        $(document).ready(function () {
            $("#loginButton-panel").click(function () {
                $(".register-panel").hide();
                $(".login-panel").fadeIn();
                $("#loginButton-panel").css("border-bottom", "solid 2px #95a6b3");
                $("#registerButton-panel").css("border-bottom", "0");
                $("#registerButton").css("color", "#ffffff");
                $("#loginButton").css("color", "#f2cdd2");
            })
        });

        $(document).ready(function () {
            $("#loginButton").mouseover(function () {
                $("#loginButton").css("color", "#f2cdd2");
            })
            $("#loginButton").mouseout(function () {
                $("#loginButton").css("color", "#fff");
            })
        });

        $(document).ready(function () {
            $("#registerButton").mouseover(function () {
                $("#registerButton").css("color", "#f2cdd2");
            })
            $("#registerButton").mouseout(function () {
                $("#registerButton").css("color", "#fff");
            })
        });

        //        $(document).ready(function () {
        //            $("#func1").mouseover(function () {
        //                $("#mock1").css("background-color", "#ffff00");
        //                $("#func1").content("666666").css("font-size", "12px");
        //            })
        //        });


        //        $(document).ready(function () {
        //            $('.nav_lf a').each(function () {
        //                alert($($(this))[0].href);
        //                if ($($(this))[0].href == String(window.location))
        //                    $(this).parent().addClass('active');
        //            })
        //        });

        function loginInfo(data) {
            //登录成功后，将登录注册按钮切换成用户名与头像
            $('.bgShadow').hide();
//            $('.loginButton').hide();
//            $(".nav_user").css("display", "table-cell");
//            $("body").css("overflow", "auto");
//            $(".userNameButton").innerHTML = (data["account"]);
            $('.loginDiv').hide();
            $('.userDiv').css("display", "inline-block");
            $('#userName').html(data["userName"]);
            $("body").css("overflow", "auto");
        }

        function moveLoginInfo() {
//            $('.loginDiv').css("display", "inline-block");
            $('.userDiv').hide();
            $('.loginDiv').show();
        }

        //        $(document).ready(function () {
        //            $("#exitButton").click(function () {
        //                alert("退出了");
        //                $('.userDiv').hide();
        //                $('.loginDiv').css("display", "inline-block");
        //            })
        //        });

        //        function getUserPO(userPO) {
        //            return {
        //                account: userPO["userName"],
        //                phone: userPO["phone"],
        //                password: userPO["password"],
        //                selectedStocks: userPO["selectStocks"]
        //            }
        //        }


        /**
         * 此方法用于每次刷新该页面时，判断是否有登录的用户，利用cookie查找
         */
        function checkLogin() {
//            delCookie("account");
//            alert(getCookie("account"));
            if (getCookie("account") != null) {//有登录的用户
                $('.loginDiv').hide();
                $('.userDiv').css("display", "inline-block");
                $('#userName').html(getCookie("account"));
            }
        }

    </script>

</head>
<body onload="checkLogin()">
<div class="navigationBar">
    <div align="right" style="height: 50px;width: 20%;display: inline-block">
        <div id="iconDiv">
            <a href="/jsp/index.jsp" class="iconButton">
                <!--<img src="css/pic/icon2.png" width="40" align="center">-->
            </a>
            <a href="/jsp/index.jsp" id="iconButtonLabel">Oquantour</a>
        </div>

        <div class="splitLine1" style="display: inline-block"></div>
    </div>

    <div style="height: 50px;width: 79%;display:inline-block;">

        <div class="navButtons">
            <div class="nav_lf" align="left">
                <a href="/jsp/stockInfo.jsp" class="navigationButton">
                    股票信息
                </a>

                <a href="/jsp/market.jsp" class="navigationButton">
                    市场分析
                </a>

                <a href="/jsp/backTest.jsp" class="navigationButton">
                    策略回测
                </a>

                <a href="/jsp/indexInfo.jsp" class="navigationButton">
                    因子说明
                </a>

            </div>
            <div class="nav_rt" align="center">
                <div class="loginDiv">
                    <button class="loginButton" id="loginButton">
                        登录
                    </button>

                    <!--<div class="splitLine2"></div>-->

                    <button class="loginButton" id="registerButton">
                        注册
                    </button>
                </div>

                <div class="userDiv" style="display: none">

                    <button id="userAva"></button>
                    <span id="userName">ST</span>

                    <div class="userInfoDiv" align="left">

                        <div class="personalHomeDiv">
                            <!--<img src="css/pic/user.png" width="35" align="center" style="padding: 0;">-->
                            <a href="/jsp/userHomepage.jsp"
                               style="text-decoration: none;font-size: 16px;color: #95a6b3;font-weight: 300">我的主页</a>
                        </div>

                        <div class="personalSettingDiv">
                            <a href="/jsp/personalInfo.jsp"
                               style="text-decoration: none;font-size: 16px;color: #95a6b3;font-weight: 300">个人设置</a>
                        </div>

                        <div class="exitDiv" align="right">
                            <button id="exitButton" onclick="logout()">退出登录</button>
                        </div>

                    </div>
                </div>
            </div>

        </div>


    </div>


</div>
</body>
</html>
