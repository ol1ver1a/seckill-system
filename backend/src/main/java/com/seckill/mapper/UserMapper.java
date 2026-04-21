package com.seckill.mapper;

import com.seckill.pojo.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM t_user WHERE id = #{id}")
    User findById(@Param("id") Long id);

    @Select("SELECT * FROM t_user WHERE username = #{username}")
    User findByUsername(@Param("username") String username);

    @Insert("INSERT INTO t_user(username, password, salt, nickname, phone, email, role, status) " +
            "VALUES(#{username}, #{password}, #{salt}, #{nickname}, #{phone}, #{email}, #{role}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    @Update("UPDATE t_user SET nickname=#{nickname}, phone=#{phone}, email=#{email}, avatar=#{avatar} WHERE id=#{id}")
    int update(User user);

    @Update("UPDATE t_user SET password=#{password}, salt=#{salt} WHERE id=#{id}")
    int updatePassword(@Param("id") Long id, @Param("password") String password, @Param("salt") String salt);

    @Update("UPDATE t_user SET status=#{status} WHERE id=#{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    @Select("<script>" +
            "SELECT * FROM t_user WHERE 1=1 " +
            "<if test='role != null'> AND role = #{role} </if>" +
            "<if test='keyword != null'> AND (username LIKE CONCAT('%',#{keyword},'%') OR nickname LIKE CONCAT('%',#{keyword},'%')) </if>" +
            " ORDER BY create_time DESC LIMIT #{offset}, #{limit}" +
            "</script>")
    java.util.List<User> findByRole(@Param("role") Integer role, @Param("keyword") String keyword,
                                     @Param("offset") int offset, @Param("limit") int limit);

    @Select("<script>" +
            "SELECT COUNT(*) FROM t_user WHERE 1=1 " +
            "<if test='role != null'> AND role = #{role} </if>" +
            "<if test='keyword != null'> AND (username LIKE CONCAT('%',#{keyword},'%') OR nickname LIKE CONCAT('%',#{keyword},'%')) </if>" +
            "</script>")
    int countByRole(@Param("role") Integer role, @Param("keyword") String keyword);

    @Select("SELECT * FROM t_user WHERE id = #{id} AND status = 1")
    User findActiveById(@Param("id") Long id);
}
