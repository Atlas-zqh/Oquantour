package oquantour.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.JSONObject;
import oquantour.exception.*;
import oquantour.po.UserPO;
import oquantour.service.UserService;
import org.apache.struts2.json.annotations.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by keenan on 07/05/2017.
 */
@Controller
public class UserAuthAction extends ActionSupport {
    @Autowired
    private UserService userService;

    private String account;

    private String password;

    private String loginInfo;

    private String registerInfo;

    private String phone;

    private UserPO loginUserPO;

    private String result;

    /**
     * 登入
     *
     * @return 登陆是否成功
     */
    public String login() {
        ActionContext.getContext().getSession().put("" + account, account);

        loginInfo = userCheck(account, password);

        Map<String, Object> map = new HashMap<>();
        map.put("loginInfo", loginInfo);

        if (loginInfo.equals("登录成功"))
            map.put("userPO", loginUserPO);

        JSONObject jsonObject = JSONObject.fromObject(map);
        result = jsonObject.toString();
        return SUCCESS;
    }

    public String register() {
        try {
            userService.addUser(new UserPO(account, phone, password));
            registerInfo = "注册成功";
        } catch (InvalidPhoneException e1) {
            registerInfo = "手机号码无效";
        } catch (UserExistedException e2) {
            registerInfo = "该用户已存在";
        } catch (InvalidPasswordException e3) {
            registerInfo = "密码不规范";
        } catch (InvalidUsernameException e4) {
            registerInfo = "用户名不规范";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("registerInfo", registerInfo);

        JSONObject jsonObject = JSONObject.fromObject(map);
        result = jsonObject.toString();
        return SUCCESS;
    }

    public String modifyUserInfo() {
        String modifyInfo;
        try {
            userService.modifyUser(new UserPO(account, phone, password));
            modifyInfo = "modifySuccess";
        } catch (InvalidPhoneException e1) {
            modifyInfo = "电话号码不规范";
        } catch (InvalidPasswordException e2) {
            modifyInfo = "密码不规范";
        }
        Map<String, Object> map = new HashMap<>();
        map.put("modifyInfo", modifyInfo);
        JSONObject jsonObject = JSONObject.fromObject(map);
        result = jsonObject.toString();
        return SUCCESS;
    }

//    public String getUserInfo() {
//        try {
//            loginUserPO = userService.findUserByID(login_account);
//            Map<String, Object> map = new HashMap<>();
//            map.put("userPO", loginUserPO);
//            JSONObject jsonObject = JSONObject.fromObject(map);
//            result = jsonObject.toString();
//
//        } catch (UserNotExistException e) {
//            e.printStackTrace();
//        }
//        return SUCCESS;
//    }

    /**
     * 登出
     *
     * @return 登出是否成功
     */
    public String logout() {
        ActionContext.getContext().getSession().remove("" + account);
        return SUCCESS;
    }

    /**
     * 验证输入的用户名和密码的有效性
     *
     * @param msg
     * @return 是否有效
     */
    private boolean isValid(String msg) {
        return msg != null && !msg.equals("");
    }

    /**
     * 验证是否存在用户，密码是否正确
     *
     * @param account  用户名
     * @param password 密码
     * @return 用户名与密码是否匹配
     */
    private String userCheck(String account, String password) {
        try {
//            ActionContext.getContext().getSession().remove(account);
//            if (ActionContext.getContext().getSession().get(account) != null) {
//                return "已经登录";
//            }

            if (!isValid(account)) {
                return "账号为空";
            }
            if (!isValid(password)) {
                return "密码为空";
            }
            loginUserPO = userService.findUserByID(account);

            if (null == loginUserPO) {
                return "用户名错误，请重新检查！";
            } else if (!loginUserPO.getUserPassword().equals(password)) {
                return "密码错误，请重新检查！";
            }
            return "登录成功";
        } catch (UserNotExistException e) {
            return "用户不存在";
        }
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLoginInfo(String loginInfo) {
        this.loginInfo = loginInfo;
    }

    public void setRegisterInfo(String registerInfo) {
        this.registerInfo = registerInfo;
    }

    public void setLoginUserPO(UserPO loginUserPO) {
        this.loginUserPO = loginUserPO;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
