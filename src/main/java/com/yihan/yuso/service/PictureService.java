package com.yihan.yuso.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yihan.yuso.model.dto.post.PostQueryRequest;
import com.yihan.yuso.model.entity.Picture;
import com.yihan.yuso.model.entity.Post;
import com.yihan.yuso.model.vo.PostVO;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 图片服务
 *
 * @author <a href="https://github.com/Litou-lyh">liyihan</a>
 */
public interface PictureService {
    /**
     * @param searchText
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<Picture> searchPicture(String searchText, long pageNum, long pageSize);
}
