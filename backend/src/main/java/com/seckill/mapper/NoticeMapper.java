package com.seckill.mapper;

import com.seckill.pojo.Notice;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoticeMapper {

    @Select("SELECT * FROM t_notice WHERE id = #{id}")
    Notice findById(@Param("id") Long id);

    @Select("SELECT * FROM t_notice WHERE status = 1 ORDER BY create_time DESC")
    List<Notice> findPublished();

    @Select("<script>" +
            "SELECT * FROM t_notice WHERE 1=1 " +
            "<if test='status != null'> AND status = #{status} </if>" +
            "<if test='keyword != null'> AND (title LIKE CONCAT('%',#{keyword},'%') OR content LIKE CONCAT('%',#{keyword},'%')) </if>" +
            " ORDER BY create_time DESC LIMIT #{offset}, #{limit}" +
            "</script>")
    List<Notice> findByCondition(@Param("status") Integer status, @Param("keyword") String keyword,
                                  @Param("offset") int offset, @Param("limit") int limit);

    @Insert("INSERT INTO t_notice(title, content, admin_id, status) VALUES(#{title}, #{content}, #{adminId}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Notice notice);

    @Update("UPDATE t_notice SET title=#{title}, content=#{content}, status=#{status} WHERE id=#{id}")
    int update(Notice notice);

    @Delete("DELETE FROM t_notice WHERE id = #{id}")
    int deleteById(@Param("id") Long id);
}
