package com.seckill.mapper;

import com.seckill.pojo.SeckillActivity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SeckillActivityMapper {

    @Select("SELECT a.*, g.goods_name, g.goods_img, g.goods_price " +
            "FROM t_seckill_activity a LEFT JOIN t_goods g ON a.goods_id = g.id WHERE a.id = #{id}")
    SeckillActivity findById(@Param("id") Long id);

    @Select("SELECT a.*, g.goods_name, g.goods_img, g.goods_price " +
            "FROM t_seckill_activity a LEFT JOIN t_goods g ON a.goods_id = g.id " +
            "WHERE a.status = 1 ORDER BY a.start_time DESC")
    List<SeckillActivity> findActive();

    @Select("<script>" +
            "SELECT a.*, g.goods_name, g.goods_img, g.goods_price " +
            "FROM t_seckill_activity a LEFT JOIN t_goods g ON a.goods_id = g.id WHERE 1=1 " +
            "<if test='merchantId != null'> AND a.merchant_id = #{merchantId} </if>" +
            "<if test='status != null'> AND a.status = #{status} </if>" +
            "<if test='keyword != null'> AND a.activity_name LIKE CONCAT('%',#{keyword},'%') </if>" +
            " ORDER BY a.create_time DESC LIMIT #{offset}, #{limit}" +
            "</script>")
    List<SeckillActivity> findByCondition(@Param("merchantId") Long merchantId, @Param("status") Integer status,
                                           @Param("keyword") String keyword, @Param("offset") int offset, @Param("limit") int limit);

    @Insert("INSERT INTO t_seckill_activity(goods_id, merchant_id, activity_name, seckill_price, stock_count, " +
            "start_time, end_time, status, limit_count) " +
            "VALUES(#{goodsId}, #{merchantId}, #{activityName}, #{seckillPrice}, #{stockCount}, " +
            "#{startTime}, #{endTime}, #{status}, #{limitCount})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(SeckillActivity activity);

    @Update("UPDATE t_seckill_activity SET activity_name=#{activityName}, seckill_price=#{seckillPrice}, " +
            "stock_count=#{stockCount}, start_time=#{startTime}, end_time=#{endTime}, " +
            "status=#{status}, limit_count=#{limitCount} WHERE id=#{id}")
    int update(SeckillActivity activity);

    @Update("UPDATE t_seckill_activity SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    @Update("UPDATE t_seckill_activity SET stock_count = stock_count - #{count} WHERE id = #{id} AND stock_count >= #{count}")
    int reduceStock(@Param("id") Long id, @Param("count") int count);
}
