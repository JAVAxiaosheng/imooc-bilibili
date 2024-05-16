package com.imooc.bilibili.elasticsearch;

import cn.hutool.core.util.ObjectUtil;
import com.imooc.bilibili.domain.UserInfo;
import com.imooc.bilibili.domain.video.Video;
import com.imooc.bilibili.repository.UserInfoRepository;
import com.imooc.bilibili.repository.VideoRepository;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class ElasticsearchService {

    @Resource
    private VideoRepository videoRepository;

    @Resource
    private UserInfoRepository userInfoRepository;

    @Resource
    private RestHighLevelClient restHighLevelClient;

    public void addVideo(Video video) {
        videoRepository.save(video);
    }

    public void addUserInfo(UserInfo userInfo) {
        userInfoRepository.save(userInfo);
    }

    public List<Map<String, Object>> getContents(String keyword,
                                                 Integer pageNum, Integer pgeSize) throws Exception {
        String[] indices = {"videos", "user_infos"};
        SearchRequest searchRequest = new SearchRequest(indices);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        // 分页
        sourceBuilder.from(pageNum - 1);
        sourceBuilder.size(pgeSize);
        MultiMatchQueryBuilder matchQueryBuilder = QueryBuilders.multiMatchQuery(
                keyword, "title", "nick", "description"
        );
        sourceBuilder.query(matchQueryBuilder);
        searchRequest.source(sourceBuilder);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        // 高亮显示
        String[] array = {"title", "nick", "description"};
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        for (String key : array) {
            highlightBuilder.fields().add(new HighlightBuilder.Field(key));
        }
        // 如果需要多个字段高亮,需要置为false
        highlightBuilder.requireFieldMatch(false);
        highlightBuilder.preTags("<span style=\"color:red\">");
        highlightBuilder.postTags("</span>");
        sourceBuilder.highlighter(highlightBuilder);
        // 执行搜索
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        List<Map<String, Object>> arrayList = new ArrayList<>();
        for (SearchHit hit : searchResponse.getHits()) {
            // 处理高亮字段
            Map<String, HighlightField> highlightBuilderFields = hit.getHighlightFields();
            Map<String, Object> sourceMap = hit.getSourceAsMap();
            for (String key : array) {
                HighlightField field = highlightBuilderFields.get(key);
                if (ObjectUtil.isNotNull(field)) {
                    Text[] fragments = field.fragments();
                    String str = Arrays.toString(fragments);
                    str = str.substring(1, str.length() - 1);
                    sourceMap.put(key, str);
                }
            }
            arrayList.add(sourceMap);
        }
        return arrayList;
    }


    public Video getVideos(String keyword) {
        return videoRepository.findByTitleLike(keyword);
    }

}
