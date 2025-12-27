package com.gzu.backendcrud.controller;

import com.gzu.backendcrud.dto.ProductDTO;
import com.gzu.backendcrud.service.ProductService;
import com.gzu.backendcrud.vo.ProductVO;
import com.gzu.backendcrud.vo.ResultVO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * 获取所有商品
     */
    @GetMapping
    public ResultVO<List<ProductVO>> getProducts() {
        return productService.getProducts();
    }

    /**
     * 根据ID获取商品
     */
    @GetMapping("/{id}")
    public ResultVO<ProductVO> getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    /**
     * 添加商品
     */
    @PostMapping
    public ResultVO<ProductVO> addProduct(@RequestBody @Valid ProductDTO dto) {
        return productService.addProduct(dto);
    }

    /**
     * 更新商品
     */
    @PutMapping("/{id}")
    public ResultVO<ProductVO> updateProduct(@PathVariable Long id,
                                             @RequestBody @Valid ProductDTO dto) {
        return productService.updateProduct(id, dto);
    }

    /**
     * 删除商品
     */
    @DeleteMapping("/{id}")
    public ResultVO<?> deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id);
    }

    /**
     * 根据分类获取商品
     */
    @GetMapping("/category/{category}")
    public ResultVO<List<ProductVO>> getProductsByCategory(@PathVariable String category) {
        return productService.getProductsByCategory(category);
    }

    /**
     * 搜索商品
     */
    @GetMapping("/search")
    public ResultVO<List<ProductVO>> searchProducts(@RequestParam String keyword) {
        return productService.searchProducts(keyword);
    }

    /**
     * 批量删除商品
     */
    @DeleteMapping("/batch")
    public ResultVO<?> batchDeleteProducts(@RequestBody List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return ResultVO.error("请选择要删除的商品");
        }

        for (Long id : ids) {
            productService.deleteProduct(id);
        }

        return ResultVO.success("批量删除成功", null);
    }

    /**
     * 获取库存不足的商品
     */
    @GetMapping("/low-stock")
    public ResultVO<List<ProductVO>> getLowStockProducts(@RequestParam(defaultValue = "10") Integer threshold) {
        // 这里可以添加逻辑，调用mapper中的findLowStockProducts方法
        return ResultVO.success("功能待实现", null);
    }
}