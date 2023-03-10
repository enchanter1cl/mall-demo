package com.erato.enchanter.mall.product.service;

import com.erato.enchanter.mall.product.entity.Category;
import com.erato.enchanter.mall.product.vo.CategoryVo;

import java.util.List;

/**
 * 商品三级分类(Category)表服务接口
 *
 * @author zhangyuan
 * @since 2023-02-14 13:06:24
 */
public interface CategoryService {
    
    /**
     * 3-level catalog of product category.
     * @return All categories, listed in a tree structure. The subcategories of each category are in the 'children' field.
     */
    List<CategoryVo> listWithTree();
    
    /**
     * 通过ID查询单条数据
     *
     * @param catId 主键
     * @return 实例对象
     */
    Category queryById(Long catId);
    
    /**
     * 新增数据
     *
     * @param category 实例对象
     * @return 实例对象
     */
    Category insert(Category category);
    
    /**
     * 修改数据
     *
     * @param category 实例对象
     * @return 实例对象
     */
    Category update(Category category);
    
    /**
     * 通过主键删除数据
     *
     * @param catId 主键
     * @return 是否成功
     */
    boolean deleteById(Long catId);
}
