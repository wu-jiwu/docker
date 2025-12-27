package com.gzu.backendcrud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gzu.backendcrud.dto.ProductDTO;
import com.gzu.backendcrud.entity.Product;
import com.gzu.backendcrud.mapper.ProductMapper;
import com.gzu.backendcrud.service.ProductService;
import com.gzu.backendcrud.vo.ProductVO;
import com.gzu.backendcrud.vo.ResultVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Override
    public ResultVO<ProductVO> addProduct(ProductDTO dto) {
        // 检查商品编码是否已存在
        if (productExists(dto.getProductCode())) {
            return ResultVO.error("商品编码已存在");
        }

        Product product = new Product();
        BeanUtils.copyProperties(dto, product);

        boolean saved = save(product);
        if (saved) {
            ProductVO vo = convertToVO(product);
            return ResultVO.success("商品添加成功", vo);
        }
        return ResultVO.error("商品添加失败");
    }

    @Override
    public ResultVO<ProductVO> updateProduct(Long id, ProductDTO dto) {
        Product product = getById(id);
        if (product == null) {
            return ResultVO.error("商品不存在");
        }

        // 检查商品编码是否被其他商品使用
        Product existingProduct = baseMapper.findByProductCode(dto.getProductCode());
        if (existingProduct != null && !existingProduct.getId().equals(id)) {
            return ResultVO.error("商品编码已被其他商品使用");
        }

        BeanUtils.copyProperties(dto, product);
        product.setId(id);

        boolean updated = updateById(product);
        if (updated) {
            ProductVO vo = convertToVO(product);
            return ResultVO.success("商品更新成功", vo);
        }
        return ResultVO.error("商品更新失败");
    }

    @Override
    public ResultVO<?> deleteProduct(Long id) {
        Product product = getById(id);
        if (product == null) {
            return ResultVO.error("商品不存在");
        }

        // 硬删除，直接从数据库删除记录
        boolean removed = removeById(id);

        if (removed) {
            // 如果有关联的图片或其他资源，可以在这里添加清理逻辑
            // 例如：imageService.deleteProductImages(id);

            return ResultVO.success("商品删除成功", null);
        }
        return ResultVO.error("商品删除失败");
    }

    @Override
    public ResultVO<ProductVO> getProductById(Long id) {
        Product product = getById(id);
        if (product == null) {
            return ResultVO.error("商品不存在");
        }

        if (!product.getIsActive()) {
            return ResultVO.error("商品已被删除");
        }

        ProductVO vo = convertToVO(product);
        return ResultVO.success(vo);
    }

    @Override
    public ResultVO<List<ProductVO>> getProducts() {
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Product::getIsActive, true)
                .orderByDesc(Product::getCreatedAt);

        List<Product> products = list(queryWrapper);
        List<ProductVO> voList = products.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return ResultVO.success(voList);
    }

    @Override
    public ResultVO<List<ProductVO>> getProductsByCategory(String category) {
        List<Product> products = baseMapper.findByCategory(category);
        List<ProductVO> voList = products.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return ResultVO.success(voList);
    }

    @Override
    public ResultVO<List<ProductVO>> searchProducts(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return getProducts();
        }

        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Product::getIsActive, true)
                .and(wrapper -> wrapper
                        .like(Product::getProductName, keyword)
                        .or()
                        .like(Product::getProductCode, keyword)
                        .or()
                        .like(Product::getDescription, keyword)
                )
                .orderByDesc(Product::getCreatedAt);

        List<Product> products = list(queryWrapper);
        List<ProductVO> voList = products.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return ResultVO.success(voList);
    }

    @Override
    public boolean productExists(String productCode) {
        Product product = baseMapper.findByProductCode(productCode);
        return product != null;
    }

    private ProductVO convertToVO(Product product) {
        ProductVO vo = new ProductVO();
        BeanUtils.copyProperties(product, vo);
        return vo;
    }
}