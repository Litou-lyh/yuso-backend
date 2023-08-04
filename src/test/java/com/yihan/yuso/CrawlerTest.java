package com.yihan.yuso;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.gson.Gson;
import com.yihan.yuso.model.entity.Picture;
import com.yihan.yuso.model.entity.Post;
import com.yihan.yuso.service.PostService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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

    @Test
    void testFetchPictures() throws IOException {
        int current = 1;
        String url = "https://www.bing.com/images/search?q=%e5%b0%8f%e9%bb%91%e5%ad%90&form=HDRSC3&first=" + current;
        Document doc = Jsoup.connect(url).get();
        System.out.println(doc);
        Elements elements = doc.select(".iuscp.isv"); // 包络图片卡片的单元类
        List<Picture> pictures = new ArrayList<>();
        for (Element elem : elements) {
            // 取图片地址
            String m = elem.select(".iusc").get(0).attr("m");
            String title = elem.select(".inflnk").get(0).attr("aria-label");
            Map<String, String> map = JSONUtil.toBean(m, Map.class);
            String murl = map.get("murl");
//            System.out.println(murl);
            Picture picture = new Picture();
            picture.setTitle(title);
            picture.setUrl(murl);
            pictures.add(picture);
        }
        System.out.println(pictures);
    }

    @Test
    void testFetchBaiduHot() throws IOException {
        String url = "https://top.baidu.com/board?tab=realtime&sa=fyb_realtime_31065";

        Document doc = Jsoup.connect(url).get();
        //标题
        Elements titles = doc.select(".c-single-text-ellipsis");

        Elements urls = doc.select(".category-wrap_iQLoo a.img-wrapper_29V76");

        System.out.println(titles);
    }

    @Test
    void testFetchZhihuHot() throws IOException {
        String url = "https://www.zhihu.com/billboard";

        Document doc = Jsoup.connect(url).get();
        //标题
        Elements titles = doc.select("#js-initialData");

        Elements urls = doc.select(".category-wrap_iQLoo a.img-wrapper_29V76");

        System.out.println(titles);
    }
}
