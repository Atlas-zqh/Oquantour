package oquantour.service.serviceImpl;

import oquantour.data.dao.UserDao;
import oquantour.exception.*;
import oquantour.po.UserPO;
import oquantour.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * Created by keenan on 06/05/2017.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    /**
     * add a new user
     *
     * @param userPO
     * @throws InvalidInfoException 所填信息错误
     * @throws UserExistedException 用户已经存在
     */
    @Override
    @SuppressWarnings("Duplicates")
    public void addUser(UserPO userPO) throws InvalidPhoneException, InvalidPasswordException, InvalidUsernameException, UserExistedException {
        // username and password are invalid
        if (!isValidPhone(userPO.getPhone())) {
            throw new InvalidPhoneException();
        } else if (!isValidPassword(userPO.getUserPassword())) {
            throw new InvalidPasswordException();
        } else if (!isValidUsername(userPO.getUserName())) {
            throw new InvalidUsernameException();
        }

        if (userDao.findUserByName(userPO.getUserName()) != null) {
            throw new UserExistedException();
        }

        userDao.addUser(userPO);
    }

    /**
     * modify user info
     *
     * @param userPO
     * @throws InvalidInfoException 所填信息错误
     */
    @Override
    @SuppressWarnings("Duplicates")
    public void modifyUser(UserPO userPO) throws InvalidPhoneException, InvalidPasswordException {

        if (!isValidPhone(userPO.getPhone())) {
            throw new InvalidPhoneException();
        } else if (!isValidPassword(userPO.getUserPassword())) {
            throw new InvalidPasswordException();
        }

        userDao.modifyUser(userPO);
    }

    /**
     * find an user by ID
     *
     * @param ID
     * @return 查找结果
     * @throws UserNotExistException 用户不存在
     */
    @Override
    public UserPO findUserByID(Serializable ID) throws UserNotExistException {
        UserPO userPO = userDao.findUserByName((String) ID);

        if (null == userPO) {
            throw new UserNotExistException();
        }

        return userPO;
    }

    /**
     * 验证用户名有效性
     *
     * 只含有汉字、数字、字母、下划线
     * 不能以下划线开头和结尾
     *
     * @param username 用户名
     * @return 是否有效
     */
    private boolean isValidUsername(String username) {
        if (StringUtils.isEmpty(username)) {
            return false;
        }

        String regex = "^(?!_)(?!.*?_$)[a-zA-Z0-9_\\u4e00-\\u9fa5]+$";
        return username.matches(regex);
    }


    /**
     * 验证密码有效性
     * <p>
     * 中英文字符组合，6～16位
     *
     * @param password
     * @return
     */
    private boolean isValidPassword(String password) {
        if (StringUtils.isEmpty(password)) {
            return false;
        }

        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
        return password.matches(regex);

    }

    /**
     * 验证联系方式的有效性（必须为中国大陆手机号）
     * @param phone 手机号
     * @return 是否有效
     */
    private boolean isValidPhone(String phone) {
        String regex = "^1(3[0-9]|4[57]|5[0-35-9]|7[0135678]|8[0-9])\\d{8}$";
        return phone.matches(regex);
    }
}
