package com.imooc.bilibili.repository;

import com.imooc.bilibili.domain.video.Video;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface VideoRepository extends ElasticsearchRepository<Video, Long> {

    // find by title like
    Video findByTitleLike(String keyword);
}
