package com.torchbell.lovecoach.user.dao;

import com.torchbell.lovecoach.user.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;


@Mapper
public interface UserDao {
    int insertUser(User user);
    Optional<User> selectUserById(@Param("userId") Long userId);
    Optional<User> selectUserByEmail(@Param("email") String email);
    int updateUser(User user);
    int deleteUser(@Param("userId") Long userId);
    int decreaseCredit(@Param("userId") Long userId, @Param("amount") Integer amount);
}
