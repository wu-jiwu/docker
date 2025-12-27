package com.gzu.backendcrud.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gzu.backendcrud.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {

    /**
     * 根据分类查询商品
     */
    @Select("SELECT * FROM products WHERE category = #{category} AND is_active = true")
    List<Product> findByCategory(@Param("category") String category);

    /**
     * 查询库存不足的商品
     */
    @Select("SELECT * FROM products WHERE stock < #{threshold} AND is_active = true")
    List<Product> findLowStockProducts(@Param("threshold") Integer threshold);

    /**
     * 更新商品状态
     */
    @Update("UPDATE products SET is_active = #{isActive} WHERE id = #{id}")
    int updateProductStatus(@Param("id") Long id, @Param("isActive") Boolean isActive);

    /**
     * 根据商品编码查询
     */
    @Select("SELECT * FROM products WHERE product_code = #{productCode}")
    Product findByProductCode(@Param("productCode") String productCode);

    /**
     * 更新库存
     */
    @Update("UPDATE products SET stock = stock - #{quantity} WHERE id = #{id} AND stock >= #{quantity}")
    int reduceStock(@Param("id") Long id, @Param("quantity") Integer quantity);
}