package com.erato.enchanter.mall.product.service.impl;

import com.erato.enchanter.mall.common.util.JsonUtils;
import com.erato.enchanter.mall.product.entity.Category;
import com.erato.enchanter.mall.product.dao.CategoryDao;
import com.erato.enchanter.mall.product.service.CategoryService;
import com.erato.enchanter.mall.product.vo.CategoryVo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品三级分类(Category)表服务实现类
 *
 * @author zhangyuan
 * @since 2023-02-14 13:06:24
 */
@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {
    
    @Resource
    private CategoryDao categoryDao;
    
    @Autowired
    StringRedisTemplate strRedisTemplate;
    
    @Autowired
    ObjectMapper OBJECT_MAPPER;
    
    public List<CategoryVo> listWithTree() {
        //1. Query all the categories
        List<CategoryVo> categoryVos = this.queryAll();
        //2. Assemble into a tree structure
        //2.1 Find all level1 categories
        List<CategoryVo> level1Cats = categoryVos.stream().filter(category -> category.getParentCid() == 0
        ).map(categoryVo -> {
            //2.2 Stuff children
            categoryVo.setChildren(getChildren(categoryVo, categoryVos));
            return categoryVo;
        }).sorted(Comparator.comparingInt(catVo -> catVo.getSort() == null ? 0 : catVo.getSort())
        ).collect(Collectors.toList());
        return level1Cats;
    }
    
    private List<CategoryVo> queryAll() {
        /* 1. Add cache logic */
        String categoryJSON = strRedisTemplate.opsForValue().get("categoryJSON");
        List<CategoryVo> categoryVoList;
        if (StringUtils.hasText(categoryJSON)) {
            /* 2.1 If it exists in cache. */
            categoryVoList = JsonUtils.readValue(categoryJSON, new TypeReference<List<CategoryVo>>() {});
        } else {
            /* 2.2 If it doesn't exist in cache. Query Db.  Transfer this java obj to JSON, store it into cache. */
            categoryVoList = this.queryAllFromDb();
            String categoryJsonValue;
            categoryJsonValue = JsonUtils.writeValueAsString(categoryVoList);
            strRedisTemplate.opsForValue().set("categoryJSON", categoryJsonValue);
        }
        return categoryVoList;
    }
    
    /**
     * Query all the categories
     * @return list of  categories
     */
    private List<CategoryVo> queryAllFromDb(){
        // Query all the categories
        List<Category> categories = categoryDao.queryAll();
        List<CategoryVo> categoryVos = categories.stream().map(category -> {
            CategoryVo categoryVo = new CategoryVo();
            BeanUtils.copyProperties(category, categoryVo);
            return categoryVo;
        }).collect(Collectors.toList());
        return categoryVos;
    }
    
    private List<CategoryVo> getChildren(CategoryVo root, List<CategoryVo> all) {
        List<CategoryVo> children = all.stream().filter(category -> category.getParentCid() == root.getCatId()
        ).map(category -> {
            CategoryVo categoryVo = new CategoryVo();
            BeanUtils.copyProperties(category, categoryVo);
            categoryVo.setChildren(getChildren(categoryVo, all));
            return categoryVo;
        }).sorted(Comparator.comparingInt(CategoryVo -> (CategoryVo.getSort() == null ? 0 : CategoryVo.getSort()))
        ).collect(Collectors.toList());
        return children;
    }
    
    /**
     * 通过ID查询单条数据
     *
     * @param catId 主键
     * @return 实例对象
     */
    @Override
    public Category queryById(Long catId) {
        return this.categoryDao.queryById(catId);
    }
    
    /**
     * 新增数据
     *
     * @param category 实例对象
     * @return 实例对象
     */
    @Override
    public Category insert(Category category) {
        this.categoryDao.insert(category);
        return category;
    }
    
    /**
     * 修改数据
     *
     * @param category 实例对象
     * @return 实例对象
     */
    @Override
    public Category update(Category category) {
        this.categoryDao.update(category);
        return this.queryById(category.getCatId());
    }
    
    /**
     * 通过主键删除数据
     *
     * @param catId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long catId) {
        return this.categoryDao.deleteById(catId) > 0;
    }
}
