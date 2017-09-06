<%@taglib prefix="struts" uri="/struts-tags" %>
<%--
  Created by IntelliJ IDEA.
  User: keenan
  Date: 06/05/2017
  Time: 22:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/css/pic/titleIcon.png"/>
    <struts:head theme="xhtml"/>

    <%--<script src="${pageContext.request.contextPath}/js/jquery-3.2.1.min.js"></script>--%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/theme.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/homePage.css">
    <%--<script--%>
    <%--src="https://code.jquery.com/jquery-3.1.1.min.js"></script>--%>
    <script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
    <script>
        function login() {
            var account = $('#login-account-form').val();
            var password = $('#login-password-form').val();
            $.ajax({
                url: "login.action",
                type: 'POST',
                dataType: 'json',
                data: {
                    account: account,
                    password: password
                },
                success: function (data) {
                    var jsonObj = eval('(' + data + ')');
                    if (jsonObj["loginInfo"] == "登录成功") {
                        success_prompt("登录成功！", 1500)
                        setCookie("account", account);
//                        setCookie_forUser(jsonObj["userPO"]);
//                        alert(document.cookie+"   pxr");
                        setCookie("password", jsonObj["userPO"]["userPassword"]);
                        setCookie("phone", jsonObj["userPO"]["phone"]);
                        loginInfo(jsonObj["userPO"]);
                    } else {
                        fail_prompt(jsonObj["loginInfo"], 1500);
                    }
                },
                error: function () {
                    fail_prompt("ajax error", 1500);
                }
            });
        }


        function register() {
            var account = $('#register-account-form').val();
            var phone = $('#register-phone-form').val();
            var password = $('#register-password-form').val();
            var confirmPassword = $('#register-confirmPass-form').val();

            if (password != confirmPassword) {
                fail_prompt("两次密码不一致", 1500);
            } else {
                $.ajax({
                    url: "register.action",
                    type: 'POST',
                    dataType: 'json',
                    data: {
                        account: account,
                        phone: phone,
                        password: password
                    },
                    success: function (data) {
                        var jsonObj = eval('(' + data + ')');
                        fail_prompt(jsonObj["registerInfo"], 1500);
                    },
                    error: function () {
                        fail_prompt("ajax error", 1500);
                    }
                });
            }
        }


        function logout() {
            var username = document.cookie.split(";")[0].split("=")[1];
            $.ajax({
                url: "logout.action",
                type: 'POST',
                dataType: 'json',
                data: {
                    login_account: username
                },
                success: function () {
                    delCookie("account");
                    delCookie("phone");
                    delCookie("password");
                    success_prompt("登出成功", 1500);
                    moveLoginInfo();
                },
                error: function () {
                    fail_prompt("ajax error", 1500);
                }
            });
        }

        function setCookie(name, value) {
            var exp = new Date();
            exp.setTime(exp.getTime() + 60 * 1000 * 300000);
            document.cookie = name + "=" + escape(value) + ";expires=" + exp.toUTCString();
        }

        //        function setCookie(account) {
        //            var exp = new Date();
        //            exp.setTime(exp.getTime() + 60 * 5000);//过期时间 2分钟
        //            document.cookie = "account=" + account + ";expires=" + exp.toUTCString();
        ////            document.cookie = "account=" + userPO["userName"] + ";password=" + userPO["userPassword"] +
        ////                ";phone=" + userPO["phone"] +
        ////                ";expires=" + exp.toUTCString();
        //
        function getCookie(name) {
            var arr = document.cookie.match(new RegExp("(^| )" + name + "=([^;]*)(;|$)"));
            if (arr != null) return unescape(arr[2]);
            return null;
        }

        //        function getCookie() {
        //            var strCookie = document.cookie;
        //            //将多cookie切割为多个名/值对
        //            var arrCookie = strCookie.split(";");
        //            //遍历cookie数组，处理每个cookie对
        //            for (var i = 0; i < arrCookie.length; i++) {
        //                var arr = arrCookie[i].split("=");
        //                //找到名称为userId的cookie，并返回它的值
        //                if ("account" == arr[0]) {
        //                    return arr[1];
        //                }
        //            }
        //            return null;
        //        }

        //        function setCookie_forUser(userPO) {
        //            var exp = new Date();
        //            exp.setTime(exp.getTime() + 60 * 5000);//过期时间 2分钟
        //            document.cookie = "account=" + userPO["userName"] + ";password=" + userPO["userPassword"] +
        //                ";phone=" + userPO["phone"] +
        //                ";expires=" + exp.toUTCString();
        //        }

        //        function getCookie_forUser(c_name) {
        //            if (document.cookie.length > 0) {
        //                c_start = document.cookie.indexOf(c_name + "=");
        //                if (c_start != -1) {
        //                    c_start = c_start + c_name.length + 1;
        //                    c_end = document.cookie.indexOf(";", c_start);
        //                    if (c_end == -1) c_end = document.cookie.length;
        //                    return unescape(document.cookie.substring(c_start, c_end))
        //                }
        //            }
        //            return ""
        //        }

        //        function delCookie() {
        //            //获取当前时间
        //            var date = new Date();
        //            //将date设置为过去的时间
        //            date.setTime(date.getTime() - 10000);
        //            //将userId这个cookie删除
        //            document.cookie = "account=" + getCookie() + ";expires = " + date.toUTCString();
        //        }
        function delCookie(name) {
            var exp = new Date();
            exp.setTime(exp.getTime() - 10000);
            var cval = getCookie(name);
            if (cval != null) document.cookie = name + "=" + cval + ";expires=" + exp.toUTCString();
        }

        //        function jumpToUserInfo(account) {
        //            window.location.href = "/jsp/personalInfo.jsp";
        //            getUserInfo(account);
        //        }

        //        function getUserInfo(account) {
        //            $.ajax({
        //                url: "getUserInfo.action",
        //                type: 'POST',
        //                dataType: 'json',
        //                data: {
        //                    login_account: account
        //                },
        //                success: function (data) {
        //                    var jsonObj = eval('(' + data + ')');
        //                    showUserInfo(jsonObj["userPO"]);
        //                },
        //                error: function () {
        //                    alert("ajax error");
        //                }
        //            });
        //        }

    </script>

</head>
<body>
<div class="alert"></div>

<div class="bgShadow">
    <div class="login-Register-Panel">
        <button type="button" class="closeButton"><img src="${pageContext.request.contextPath}/css/pic/close.png"
                                                       width="200%"></button>
        <div class="select-login-register">
            <button class="sel-word" id="loginButton-panel">
                登录
            </button>
            <%--<div class="splitLine3"></div>--%>
            <button class="sel-word" id="registerButton-panel" style="color: #D1A5A5">
                注册
            </button>
        </div>
        <div class="login-panel">
            <form method="post">
                <label class="input-tips">用户名／手机号</label>
                <input type="text" class="login-form" name="account" id="login-account-form"/>

                <br><br><label class="input-tips">密码</label><br>
                <input type="password" class="login-form" name="password" id="login-password-form"/>
                <br>

                <%--<div class="remPw">--%>
                <%--<br><label class="checkbox">--%>
                <%--<input type="checkbox" value="remember"/>--%>
                <%--</label>记住密码--%>
                <%--</div>--%>
                <div align="right">
                    <button type="button" class="login-confirm" onclick="login()">登录</button>
                </div>
            </form>
        </div>

        <div class="register-panel">
            <label class="input-tips">用户名</label>
            <form method="post">
                <input class="login-form" type="text" name="account" id="register-account-form"/>
                <br><br><label class="input-tips">手机号</label><br>
                <input class="login-form" type="text" name="phone" id="register-phone-form"/>
                <br><br><label class="input-tips">密码</label><br>
                <input class="login-form" type="password" name="password" id="register-password-form"/>
                <br><br><label class="input-tips">确认密码</label><br>
                <input class="login-form" type="password" name="confirmPassword" id="register-confirmPass-form"/>
                <br>
                <div align="right">
                    <button type="button" class="register-confirm" onclick="register()">注册</button>
                </div>
            </form>
        </div>
    </div>
</div>

</body>
</html>
