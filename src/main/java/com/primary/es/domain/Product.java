package com.primary.es.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: wangshen
 * @Date: 2021/06/27/19:09
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(indexName = "product", shards = 5, replicas = 1)
public class Product {
    /**
     * id
     */
    @Id
    private Long id;
    /**
     * 商品id
     */
    @Field(type = FieldType.Keyword)
    private String skuId;
    /**
     * 第一分类
     */
    @Field(type = FieldType.Text)
    private String classificationFirst;
    /**
     * 第二分类
     */
    @Field(type = FieldType.Text)
    private String classificationSecond;
    /**
     * 第三分类
     */
    @Field(type = FieldType.Text)
    private String classificationThird;
    /**
     * 品牌
     */
    @Field(type = FieldType.Keyword)
    private String brand;
    /**
     * 商品名称
     */
    @Field(type = FieldType.Keyword)
    private String name;
    /**
     * 规格
     */
    @Field(type = FieldType.Keyword)
    private String specifications;
    /**
     * 价格区间
     */
    @Field(type = FieldType.Keyword)
    private String priceRange;
    /**
     * 产品链接
     */
    @Field(type = FieldType.Text)
    private String url;
}