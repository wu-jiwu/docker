package com.gzu.backendcrud.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gzu.backendcrud.dto.ProductDTO;
import com.gzu.backendcrud.entity.Product;
import com.gzu.backendcrud.vo.ProductVO;
import com.gzu.backendcrud.vo.ResultVO;

import java.util.List;

public interface ProductService extends IService<Product> {

    /**
     * 添加商品
     */
    ResultVO<ProductVO> addProduct(ProductDTO dto);

    /**
     * 更新商品
     */
    ResultVO<ProductVO> updateProduct(Long id, ProductDTO dto);

    /**
     * 删除商品（软删除）
     */
    ResultVO<?> deleteProduct(Long id);

    /**
     * 获取商品详情
     */
    ResultVO<ProductVO> getProductById(Long id);

    /**
     * 获取商品列表
     */
    ResultVO<List<ProductVO>> getProducts();

    /**
     * 根据分类获取商品
     */
    ResultVO<List<ProductVO>> getProductsByCategory(String category);

    /**
     * 搜索商品
     */
    ResultVO<List<ProductVO>> searchProducts(String keyword);

    /**
     * 检查商品是否存在
     */
    boolean productExists(String productCode);
}