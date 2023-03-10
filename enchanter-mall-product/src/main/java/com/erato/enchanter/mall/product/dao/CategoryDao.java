package com.erato.enchanter.mall.product.dao;

import com.erato.enchanter.mall.product.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品三级分类(Category)表数据库访问层
 *
 * @author zhangyuan
 * @since 2023-02-14 13:06:23
 */
@Mapper
public interface CategoryDao {
    
    /**
     * query all the category
     *
     * @return list of categories
     */
    List<Category> queryAll();
    
    /**
     * 通过ID查询单条数据
     *
     * @param catId 主键
     * @return 实例对象
     */
    Category queryById(Long catId);
    
    /**
     * 统计总行数
     *
     * @param category 查询条件
     * @return 总行数
     */
    long count(Category category);
    
    /**
     * 新增数据
     *
     * @param category 实例对象
     * @return 影响行数
     */
    int insert(Category category);
    
    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Category> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Category> entities);
    
    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Category> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<Category> entities);
    
    /**
     * 修改数据
     *
     * @param category 实例对象
     * @return 影响行数
     */
    int update(Category category);
    
    /**
     * 通过主键删除数据
     *
     * @param catId 主键
     * @return 影响行数
     */
    int deleteById(Long catId);
    
}

