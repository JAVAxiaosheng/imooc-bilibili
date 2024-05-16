package com.imooc.bilibili.controller;

import com.imooc.bilibili.domain.JsonResponse;
import com.imooc.bilibili.elasticsearch.ElasticsearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/system")
@Api(tags = "ES操作", produces = MediaType.APPLICATION_JSON_VALUE)
public class SystemController {
    @Resource
    private ElasticsearchService elasticsearchService;

    @GetMapping("/es/search/contents")
    @ApiOperation(value = "ES全文搜索", httpMethod = "GET")
    public JsonResponse<List<Map<String, Object>>> getContents(
            @RequestParam String keyword,
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize) throws Exception {
        List<Map<String, Object>> result = elasticsearchService.getContents(keyword, pageNum, pageSize);
        return new JsonResponse<>(result);
    }
}
