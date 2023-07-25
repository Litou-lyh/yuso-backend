package com.yihan.yuso.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yihan.yuso.common.ErrorCode;
import com.yihan.yuso.exception.BusinessException;
import com.yihan.yuso.model.entity.Picture;
import com.yihan.yuso.service.PictureService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 图片服务实现
 *
 * @author <a href="https://github.com/Litou-lyh">liyihan</a>
 */
@Service
public class PictureServiceImpl implements PictureService {
    @Override
    public Page<Picture> searchPicture(String searchText, long pageNum, long pageSize) {
        long current = (pageNum - 1) * pageSize;
        String url = String.format("https://www.bing.com/images/search?q=%s&first=%s", searchText, current);
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据获取异常");
        }
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
            if (pictures.size() >= pageSize) {
                break;
            }
        }
//        System.out.println(pictures);
        Page<Picture> picturePage = new Page<>(pageNum, pageSize);
        picturePage.setRecords(pictures);
        return picturePage;
    }
}
