package com.yihan.yuso.job.once;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yihan.yuso.esdao.PostEsDao;
import com.yihan.yuso.model.dto.post.PostEsDTO;
import com.yihan.yuso.model.entity.Post;
import com.yihan.yuso.service.PostService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 全量同步帖子到 es
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
// todo 取消注释开启任务
//@Component
@Slf4j
public class FetchInitPostList implements CommandLineRunner {

    @Resource
    private PostService postService;


    @Override
    public void run(String... args) {
        String json = "{\"current\":1,\"pageSize\":8,\"sortField\":\"createTime\",\"sortOrder\":\"descend\",\"category\":\"文章\",\"reviewStatus\":1}";
        String url = "https://www.code-nav.cn/api/post/search/page/vo";
        String result = HttpRequest.post(url)
                .body(json)
                .execute()
                .body();

        Map<String, Object> map = JSONUtil.toBean(result, Map.class);
        JSONObject data = (JSONObject) map.get("data");
        JSONArray records = (JSONArray) data.get("records");

        List<Post> postList = new ArrayList<>();
        for (Object record : records) {
            JSONObject tmp = (JSONObject) record;
            Post post = new Post();
            post.setTitle(tmp.getStr("title"));
            post.setContent(tmp.getStr("content"));
            JSONArray tags = (JSONArray) tmp.get("tags");
            List<String> tagList = tags.toList(String.class);
            post.setTags(JSONUtil.toJsonStr(tagList));
            post.setUserId(1L);
            post.setCreateTime(new Date());
            post.setUpdateTime(new Date());
            postList.add(post);
        }
//        System.out.println(postList);

        boolean b = postService.saveBatch(postList);
        if (b) {
            log.info("Fetched {} initial post list", postList.size());
        }
        else {
            log.error("Failed to fetch initial post list");
        }
    }
}
