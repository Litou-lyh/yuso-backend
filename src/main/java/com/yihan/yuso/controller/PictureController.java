package com.yihan.yuso.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.yihan.yuso.common.BaseResponse;
import com.yihan.yuso.common.ErrorCode;
import com.yihan.yuso.common.ResultUtils;
import com.yihan.yuso.exception.ThrowUtils;
import com.yihan.yuso.model.dto.picture.PictureQeuryRequest;
import com.yihan.yuso.model.entity.Picture;
import com.yihan.yuso.model.entity.Post;
import com.yihan.yuso.service.PictureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 图片接口
 *
 * @author <a href="https://github.com/Litou-lyh">liyihan</a>
 */
@RestController
@RequestMapping("/picture")
@Slf4j
public class PictureController {

    @Resource
    private PictureService pictureService;

    private final static Gson GSON = new Gson();

    // region 增删改查

    /**
     * 分页获取列表（封装类）
     *
     * @param pictureQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<Picture>> listPictureVOByPage(@RequestBody PictureQeuryRequest pictureQueryRequest,
                                                        HttpServletRequest request) {
        // 限制爬虫
        String searchText = pictureQueryRequest.getSearchText();
        long current = pictureQueryRequest.getCurrent();
        long pageSize = pictureQueryRequest.getPageSize();

        ThrowUtils.throwIf(pageSize > 20, ErrorCode.PARAMS_ERROR);
        Page<Picture> picturePage = pictureService.searchPicture(searchText, current, pageSize);

        return ResultUtils.success(picturePage);
    }

}
