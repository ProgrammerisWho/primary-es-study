package com.primary.es;

import com.alibaba.fastjson.JSONObject;
import com.primary.es.domain.Product;
import com.primary.es.mapper.ProductDao;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
class EsApplicationTests {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Autowired
    private ProductDao productDao;

    @Qualifier("es")
    @Autowired
    private RestHighLevelClient client;

    @Test
    void contextLoads() {
        System.out.println(123);
    }

    @Test
    public void deleteIndex() {
        //创建索引，系统初始化会自动创建索引
//        boolean flg = elasticsearchRestTemplate.deleteIndex(Product.class);
        //        elasticsearchRestTemplate.delete("product");
//        System.out.println("删除索引 = " + flg);
    }

    /**
     * 新增
     */
    @Test
    public void save() {
        Product product = new Product();
        product.setId(2L);
        product.setUrl("http://www.atguigu/hw.jpg");
        productDao.save(product);
    }

    /**
     * 修改
     */
    @Test
    public void update() {
        Product product = new Product();
        product.setId(2L);
        productDao.save(product);
    }

    //根据 id 查询
    @Test
    public void findById() {
        Product product = productDao.findById(1L).get();
        System.out.println(product);
    }

    @Test
    public void findAll() {
        Iterable<Product> products = productDao.findAll(PageRequest.of(1, 2));
        for (Product product : products) {
            System.out.println(product);
        }
    }

    @Test
    public void delete() {
        Product product = new Product();
        product.setId(1L);
        productDao.delete(product);
    }

    //批量新增
    @Test
    public void saveAll() {
        List<Product> productList = new ArrayList<>();
        for (int i = 5; i < 10; i++) {
            Product product = new Product();
            product.setId(Long.valueOf(i));
            product.setUrl("http://www.atguigu/xm.jpg");
            productList.add(product);
        }
        productDao.saveAll(productList);
    }

    @Test
    public void findByPageable() {
        //设置排序(排序方式，正序还是倒序，排序的 id)
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        int currentPage = 0;
        int pageSize = 5;
        //设置查询分页
        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, sort);
        //分页查询
        Page<Product> productPage = productDao.findAll(pageRequest);
        for (Product Product : productPage.getContent()) {
            System.out.println(Product);
        }
    }

    /**
     * 实现productDao接口的方法
     */
    @Test
    public void productDao() {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        int currentPage = 0;
        int pageSize = 5;
        //设置查询分页
        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, sort);

        List<Product> byNameAndBrand = productDao.findByNameLikeAndBrand("骨架", "NAK", pageRequest);
        byNameAndBrand.forEach(item -> {
            System.out.println(item);
        });
    }

    /**
     * term 查询
     * search(termQueryBuilder) 调用搜索方法，参数查询构建器对象
     */
    @Test
    public void termQuery() throws IOException {
        SearchRequest request = new SearchRequest();
        request.indices("mfrs_plctemplate_info");
// 构建查询的请求体
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//        sourceBuilder.query(QueryBuilders.termQuery("price", "1999"));
//        sourceBuilder.query(QueryBuilders.fuzzyQuery("title", "1小米手机").fuzziness(Fuzziness.ONE));
        //范围
        RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("plctemp_id");
// 大于等于
        rangeQuery.gte("1536");
// 小于等于
        rangeQuery.lte("1602");
        sourceBuilder.query(rangeQuery);
        sourceBuilder.size(1000000);
        sourceBuilder.from(1);
//        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//        sourceBuilder.aggregation(AggregationBuilders.terms("plctemp_id").field("1004"));
        request.source(sourceBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
//// 查询匹配
        SearchHits hits = response.getHits();
        int i = 0;
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
        }
        System.out.println("<<========");
        System.out.println(i);
    }

    /**
     * 模糊查询
     */
    @Test
    public void likeData() throws IOException {
        SearchRequest request = new SearchRequest();
        request.indices("product");
        // 构建查询的请求体
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.fuzzyQuery("name", "封圈").fuzziness(Fuzziness.ONE));
        request.source(sourceBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        // 查询匹配
        SearchHits hits = response.getHits();
        System.out.println("took:" + response.getTook());
        System.out.println("timeout:" + response.isTimedOut());
        System.out.println("total:" + hits.getTotalHits());
        System.out.println("MaxScore:" + hits.getMaxScore());
        System.out.println("hits========>>");
        List<Map<String, Object>> list = new ArrayList<>();
        for (SearchHit hit : hits) {
            //输出每条查询的结果信息
//            System.out.println(JSONObject.parse(hit.getSourceAsString()));
            Map<String, Object> map = hit.getSourceAsMap();
            list.add(map);
        }
        System.out.println(list);

    }

}
