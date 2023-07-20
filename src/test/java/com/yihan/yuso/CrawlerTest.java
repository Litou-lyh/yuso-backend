package com.yihan.yuso;
import java.util.ArrayList;
import java.util.Date;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.gson.Gson;
import com.yihan.yuso.model.entity.Post;
import com.yihan.yuso.service.PostService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class CrawlerTest {

    @Resource
    PostService postService;
    @Test
    void testFetchPassage() {
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
        System.out.println(postList);

        boolean b = postService.saveBatch(postList);
        Assertions.assertTrue(b);
    }
}
