package com.seckill.mapper;

import com.seckill.pojo.SeckillOrder;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SeckillOrderMapper {

    @Select("SELECT o.*, g.goods_name, g.goods_img, u.username " +
            "FROM t_seckill_order o " +
            "LEFT JOIN t_goods g ON o.goods_id = g.id " +
            "LEFT JOIN t_user u ON o.user_id = u.id " +
            "WHERE o.id = #{id}")
    SeckillOrder findById(@Param("id") Long id);

    @Select("SELECT * FROM t_seckill_order WHERE user_id = #{userId} AND activity_id = #{activityId}")
    SeckillOrder findByUserAndActivity(@Param("userId") Long userId, @Param("activityId") Long activityId);

    @Select("<script>" +
            "SELECT o.*, g.goods_name, g.goods_img, u.username " +
            "FROM t_seckill_order o " +
            "LEFT JOIN t_goods g ON o.goods_id = g.id " +
            "LEFT JOIN t_user u ON o.user_id = u.id WHERE 1=1 " +
            "<if test='userId != null'> AND o.user_id = #{userId} </if>" +
            "<if test='status != null'> AND o.status = #{status} </if>" +
            "<if test='orderNo != null'> AND o.order_no = #{orderNo} </if>" +
            " ORDER BY o.create_time DESC LIMIT #{offset}, #{limit}" +
            "</script>")
    List<SeckillOrder> findByCondition(@Param("userId") Long userId, @Param("status") Integer status,
                                        @Param("orderNo") String orderNo, @Param("offset") int offset, @Param("limit") int limit);

    @Insert("INSERT INTO t_seckill_order(user_id, goods_id, activity_id, order_no, goods_price, status) " +
            "VALUES(#{userId}, #{goodsId}, #{activityId}, #{orderNo}, #{goodsPrice}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(SeckillOrder order);

    @Update("UPDATE t_seckill_order SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    @Update("UPDATE t_seckill_order SET status = 1, pay_time = NOW() WHERE id = #{id} AND status = 0")
    int pay(@Param("id") Long id);
}
