package org.example.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.common.entity.User;
import java.util.List;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper {

    /**
     * 根据ID查询用户
     *
     * @param id 用户ID
     * @return 用户信息
     */
    User selectById(Long id);

    /**
     * 根据手机号查询用户
     *
     * @param mobile 手机号
     * @return 用户信息
     */
    User selectByMobile(String mobile);

    /**
     * 新增用户
     *
     * @param user 用户信息
     * @return 影响行数
     */
    int insert(User user);

    /**
     * 更新用户
     *
     * @param user 用户信息
     * @return 影响行数
     */
    int update(User user);

    /**
     * 更新用户密码
     *
     * @param id       用户ID
     * @param password 新密码
     * @return 影响行数
     */
    int updatePassword(@Param("id") Long id, @Param("password") String password);

    /**
     * 更新用户登录信息
     *
     * @param id            用户ID
     * @param lastLoginTime 最后登录时间
     * @param lastLoginIp   最后登录IP
     * @return 影响行数
     */
    int updateLoginInfo(@Param("id") Long id, @Param("lastLoginTime") java.util.Date lastLoginTime, @Param("lastLoginIp") String lastLoginIp);

    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return 影响行数
     */
    int deleteById(Long id);
    
    // ==================== Admin相关方法 ====================
    
    /**
     * 根据主键查询用户（兼容方法）
     *
     * @param id 用户ID
     * @return 用户信息
     */
    User selectByPrimaryKey(Long id);
    
    /**
     * 根据主键更新用户（兼容方法）
     *
     * @param user 用户信息
     * @return 影响行数
     */
    int updateByPrimaryKey(User user);
    
    /**
     * 根据主键删除用户（兼容方法）
     *
     * @param id 用户ID
     * @return 影响行数
     */
    int deleteByPrimaryKey(Long id);
    
    /**
     * 分页查询所有用户
     *
     * @return 用户列表
     */
    List<User> selectAllWithPage();
    
    /**
     * 根据条件分页查询用户
     *
     * @param search 搜索关键词
     * @param status 用户状态
     * @return 用户列表
     */
    List<User> selectByConditionsWithPage(@Param("search") String search, @Param("status") String status);
    
    /**
     * 统计用户总数
     *
     * @return 用户总数
     */
    Long countTotal();
    
    /**
     * 统计今日注册用户数
     *
     * @return 今日注册用户数
     */
    Long countTodayRegistered();
    
    /**
     * 根据状态统计用户数
     *
     * @param status 用户状态
     * @return 用户数量
     */
    Long countByStatus(@Param("status") Integer status);
    
    /**
     * 计算月增长率
     *
     * @return 月增长率
     */
    Double calculateMonthlyGrowth();
}