package com.ulaiber.web.dao;

import java.util.List;
import java.util.Map;

import com.ulaiber.web.model.Bank;
import com.ulaiber.web.model.Menu;
import com.ulaiber.web.model.SecondAccountAO;
import com.ulaiber.web.model.User;
import com.ulaiber.web.model.ShangHaiAcount.SecondAcount;

/**
 * user数据库接口
 *
 * @author huangguoqing
 */
public interface UserDao {

    /**
     * 新增用户
     *
     * @param user User
     * @return int
     */
    int save(User user);

    /**
     * 更新ticket和token
     *
     * @param user User
     * @return boolean true/flase
     */
    boolean update(User user);

    /**
     * 删除用户
     *
     * @param mobile 手机号码
     * @return boolean true/flase
     */
    boolean delete(String mobile);

    /**
     * 根据手机号获取用户详情
     *
     * @param mobile 手机号码
     * @return User User
     */
    User getUserByMobile(String mobile);

    /**
     * 获取所有用户
     *
     * @return List<User> List<User>
     */
    List<User> findAll();

    /**
     * 根据ticket和token获取用户
     *
     * @param user user
     * @return User User
     */
    User getUserByTicketAndToken(User user);

    /**
     * 更新登录密码
     *
     * @param user User
     * @return boolean
     */
    boolean updateLoginPwd(User user);

    /**
     * 更新支付密码
     *
     * @param user
     * @return boolean
     */
    boolean updatePayPwd(User user);

    /**
     * 更改用户银行卡信息
     *
     * @param user
     * @return boolean
     */
    boolean updateForBankCard(User user);

    /**
     * 根据用户名获取用户信息
     *
     * @param uesrName
     * @return User
     */
    User getUserByName(String userName);
    
    /**
     * 根据用户ID获取用户信息
     * @param userId
     * @return
     */
    User getUserById(long userId);
    
    /**
     * 根据用户ID集合查询CID
     * @param userIds
     * @return
     */
    List<String> queryCIDsByIds(List<Long> userIds);

    /**
     * 根据用户名获取系统所有菜单
     *
     * @return
     */
    List<Menu> getAllMenuByUser(Map<String, Object> map);

    /**
     * 获取所有系统菜单
     *
     * @return
     */
    List<Menu> getAllMenu(Map<String, Object> map);

    /**
     * 获取一个公司的各个部门下的所有用户
     *
     * @param params
     * @return
     */
    List<User> getUsersByComNum(Map<String, Object> params);

    /**
     * 新增二类账户信息
     * @param sa
     * @return int
     */
    int insertSecondAccount(SecondAcount sa);

    /**
     * 根据公司编号查询绑定的银行
     * @param companyNumber 公司编号
     * @return Bank
     */
    Bank queryBankByCompay(int companyNumber);

    /**
     * 获取二类帐户信息
     * @param userid
     * @return SecondAcount
     */
    SecondAccountAO getSecondAccountByUserId(int userid);



    /**
     * 新增用户绑定银行卡信息
     * @param map
     * @return int
     */
    int insertUserToBank(Map<String, Object> map);

    /**
     * 查询用户是否已注册二类账户
     * @param id 用户ID
     * @return SecondAcount
     */
    SecondAcount findSecondAcc(int id);

    /**
     * 根据电话获取二类帐户信息
     * @param mobile 电话
     * @return SecondAcount
     */
    SecondAccountAO getSecondAccountByMobile(String mobile);

    /**
     * 根据用户ID修改银行卡预留电话
     * @param paramMap
     * @return int
     */
    int modifyReserveMobile(Map<String, Object> paramMap);

    /**
     * 修改密码
     * @param map
     * @return int
     */
    int modifyPwd(Map<String, Object> map);

    /**
     * 更新用户信息
     * @param user 用户信息
     * @return int
     */
    int updateUserInfo(User user);

    /**
     * 上传头像
     * @param map
     * @return int
     */
    int uploadIcon(Map<String, Object> map);
}
