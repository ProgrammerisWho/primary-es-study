package com.primary.es.mapper;

import com.primary.es.domain.Product;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: wangshen
 * @Date: 2021/06/27/19:12
 * @Description:
 */
@Repository
public interface ProductDao extends ElasticsearchRepository<Product, Long> {
    /**
     * 根据名称和品牌查询
     *
     * @param name
     * @param brand
     * @return
     */
    List<Product> findByNameLikeAndBrand(String name, String brand, PageRequest pageRequest);
}