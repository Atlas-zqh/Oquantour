<%--
  Created by IntelliJ IDEA.
  User: Pxr
  Date: 2017/5/15
  Time: 下午2:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="navBar.jsp" %>
<html>
<head>
    <title>个人信息</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/personalInfo.css">

    <script>
        $(document).ready(function () {
            $('#modifyInfo_account').val(getCookie("account"));
            $('#modifyInfo_phone').val(getCookie("phone"));
        });

        function modify(flag) {
            var account = $("#modifyInfo_account").val();
            var phone = $("#modifyInfo_phone").val();
            var password;

            if (flag == 0) {//仅修改用户资料，手机号
                password = getCookie("password");
            }

            if (flag == 1) {//修改密码
                password = $("#modifyInfo_oldPass").val();
                var newPassword = $("#modifyInfo_newPass").val();
                var confirmNewPass = $("#modifyInfo_newPassAgain").val();

                if (password != getCookie("password")) {
                    fail_prompt("密码错误！", 1500);
                    return;
                } else if (newPassword != confirmNewPass) {
                    fail_prompt("两次密码不一致！", 1500);
                    return;
                }
            }

            $.ajax({
                url: "modifyUserInfo.action",
                type: "POST",
                dataType: 'json',
                data: {
                    account: account,
                    password: password,
                    phone: phone
                },
                success: function (data) {
                    var jsonObj = eval('(' + data + ')');
                    if (jsonObj["modifyInfo"] == "modifySuccess") {
                        if (flag == 0) {
                            setCookie("phone", phone);
                            $('#modifyInfo_phone').val(getCookie("phone"));
                        }
                        if (flag == 1)
                            setCookie("password", newPassword);
                    }

                },
                error: function () {
                    fail_prompt("ajax error", 1500);
                }
            });
        }
    </script>
</head>

<body>
<div class="alert"></div>
<div class="userInfoNav" align="center">
    <div style="width: 1100px;padding-top: 80px">
        <div id="modifyBasicInfo"
             style="width: 100%;height: auto;padding: 20px;border: solid 1px #e1e1e1;margin-bottom: 40px"
             align="center">
            <div align="left" style="margin-bottom: 50px;padding-left: 20px">
                <p class="infoHead"><span style="border-bottom: solid 2px #95a6b3;">基本信息</span></p>
            </div>
            <div class="userImgDiv">
                <img src="${pageContext.request.contextPath}/css/pic/user.png" class="userImg">
            </div>

            <div style="display: inline-block;margin-left: 100px;padding: 10px 0;width: 20%;" align="left">
                <form class="ui form">
                    <div class="field">
                        <label class="input-tips">用户名</label>
                        <input type="text" id="modifyInfo_account" disabled="disabled">
                    </div>
                    <div class="field">
                        <label class="input-tips">手机号</label>
                        <input type="text" id="modifyInfo_phone">
                    </div>
                </form>
                <button class="saveModifyButton" onclick="modify(0)">保存修改</button>
            </div>
        </div>

        <div id="modifySecurityInfo"
             style="width: 100%;height: auto;padding: 20px;border: solid 1px #e1e1e1;margin-bottom: 40px"
             align="center">
            <div align="left" style="margin-bottom: 50px;padding-left: 20px">
                <p class="infoHead"><span style="border-bottom: solid 2px #95a6b3;">密码安全</span></p>
            </div>
            <div style="display: inline-block;margin-left: 100px;padding: 10px 0;width: 60%" align="left">

                <form class="ui form">
                    <div class="field">
                        <label class="input-tips">原密码</label>
                        <input type="text" id="modifyInfo_oldPass">
                    </div>
                    <div class="field">
                        <label class="input-tips">新密码</label>
                        <input type="text" id="modifyInfo_newPass">
                    </div>
                    <div class="field">
                        <label class="input-tips">再次输入新密码</label>
                        <input type="text" id="modifyInfo_newPassAgain">
                    </div>
                </form>
                <button class="saveModifyButton" onclick="modify(1)">保存修改</button>
            </div>
        </div>
    </div>

</div>
</body>
</html>
