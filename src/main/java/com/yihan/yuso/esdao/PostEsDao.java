package com.yihan.yuso.esdao;

import com.yihan.yuso.model.dto.post.PostEsDTO;
import java.util.List;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 帖子 ES 操作
 *

 */
public interface PostEsDao extends ElasticsearchRepository<PostEsDTO, Long> {
    // ElasticsearchRepository 中已实现基本的CRUD

    List<PostEsDTO> findByUserId(Long userId);
    List<PostEsDTO> findByTitle(String title);
}