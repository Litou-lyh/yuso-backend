package com.yihan.yuso.esdao;

import com.yihan.yuso.model.dto.post.PostEsDTO;
import java.util.List;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 帖子 ES 操作
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
public interface PostEsDao extends ElasticsearchRepository<PostEsDTO, Long> {
    // ElasticsearchRepository 中已实现基本的CRUD

    List<PostEsDTO> findByUserId(Long userId);
    List<PostEsDTO> findByTitle(String title);
}