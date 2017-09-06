package oquantour.service;

import oquantour.exception.*;
import oquantour.po.UserPO;

import java.io.Serializable;

/**
 * Created by keenan on 06/05/2017.
 */
public interface UserService {
    /**
     * add a new user
     *
     * @param userPO
     * @throws InvalidInfoException 所填信息错误
     * @throws UserExistedException 用户已经存在
     */
    void addUser(UserPO userPO) throws InvalidPhoneException, InvalidPasswordException, InvalidUsernameException, UserExistedException;

    /**
     * modify user info
     *
     * @param userPO
     * @throws InvalidInfoException 所填信息错误
     */
    void modifyUser(UserPO userPO) throws InvalidPhoneException, InvalidPasswordException;

    /**
     * find an user by ID
     *
     * @param ID
     * @return 查找结果
     * @throws UserNotExistException 用户不存在
     */
    UserPO findUserByID(Serializable ID) throws UserNotExistException;

}
