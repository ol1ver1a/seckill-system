package com.seckill.mapper;

import com.seckill.pojo.Goods;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface GoodsMapper {

    @Select("SELECT * FROM t_goods WHERE id = #{id}")
    Goods findById(@Param("id") Long id);

    @Select("SELECT * FROM t_goods WHERE status = 1 ORDER BY create_time DESC")
    List<Goods> findAllOnSale();

    @Select("<script>" +
            "SELECT * FROM t_goods WHERE 1=1 " +
            "<if test='merchantId != null'> AND merchant_id = #{merchantId} </if>" +
            "<if test='status != null'> AND status = #{status} </if>" +
            "<if test='keyword != null'> AND (goods_name LIKE CONCAT('%',#{keyword},'%') OR goods_title LIKE CONCAT('%',#{keyword},'%')) </if>" +
            " ORDER BY create_time DESC LIMIT #{offset}, #{limit}" +
            "</script>")
    List<Goods> findByCondition(@Param("merchantId") Long merchantId, @Param("status") Integer status,
                                 @Param("keyword") String keyword, @Param("offset") int offset, @Param("limit") int limit);

    @Insert("INSERT INTO t_goods(merchant_id, goods_name, goods_title, goods_img, goods_detail, goods_price, seckill_price, stock_count, status, category) " +
            "VALUES(#{merchantId}, #{goodsName}, #{goodsTitle}, #{goodsImg}, #{goodsDetail}, #{goodsPrice}, #{seckillPrice}, #{stockCount}, #{status}, #{category})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Goods goods);

    @Update("UPDATE t_goods SET goods_name=#{goodsName}, goods_title=#{goodsTitle}, goods_img=#{goodsImg}, " +
            "goods_detail=#{goodsDetail}, goods_price=#{goodsPrice}, seckill_price=#{seckillPrice}, " +
            "stock_count=#{stockCount}, status=#{status}, category=#{category} WHERE id=#{id}")
    int update(Goods goods);

    @Update("UPDATE t_goods SET status = #{status} WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    @Delete("DELETE FROM t_goods WHERE id = #{id}")
    int deleteById(@Param("id") Long id);

    @Update("UPDATE t_goods SET stock_count = stock_count - #{count} WHERE id = #{id} AND stock_count >= #{count}")
    int reduceStock(@Param("id") Long id, @Param("count") int count);
}
