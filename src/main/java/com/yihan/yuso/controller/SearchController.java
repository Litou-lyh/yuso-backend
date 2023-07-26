package com.yihan.yuso.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.yihan.yuso.common.BaseResponse;
import com.yihan.yuso.common.ErrorCode;
import com.yihan.yuso.common.ResultUtils;
import com.yihan.yuso.exception.BusinessException;
import com.yihan.yuso.exception.ThrowUtils;
import com.yihan.yuso.model.dto.post.PostQueryRequest;
import com.yihan.yuso.model.dto.search.SearchRequest;
import com.yihan.yuso.model.dto.user.UserQueryRequest;
import com.yihan.yuso.model.entity.Picture;
import com.yihan.yuso.model.enums.SearchTypeEnum;
import com.yihan.yuso.model.vo.PostVO;
import com.yihan.yuso.model.vo.SearchVO;
import com.yihan.yuso.model.vo.UserVO;
import com.yihan.yuso.service.PictureService;
import com.yihan.yuso.service.PostService;
import com.yihan.yuso.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 统一搜索接口接口
 *
 * @author <a href="https://github.com/Litou-lyh">liyihan</a>
 */
@RestController
@RequestMapping("/search")
@Slf4j
public class SearchController {

    @Resource
    private PictureService pictureService;
    @Resource
    private UserService userService;
    @Resource
    private PostService postService;

    private final static Gson GSON = new Gson();

    // region 增删改查

    /**
     * 分页获取列表（封装类）
     *
     * @param searchRequest
     * @return
     */
    @PostMapping("/all")
    public BaseResponse<SearchVO> doSearchAll(@RequestBody SearchRequest searchRequest, HttpServletRequest request) {
        String searchType = searchRequest.getType();
        SearchTypeEnum searchTypeEnum = SearchTypeEnum.getEnumByValue(searchType);
        ThrowUtils.throwIf(StringUtils.isBlank(searchType), ErrorCode.PARAMS_ERROR);

        String searchText = searchRequest.getSearchText();
        SearchVO searchVO = new SearchVO();

        if (searchTypeEnum == null) {
            UserQueryRequest userQueryRequest = new UserQueryRequest();
            userQueryRequest.setUserName(searchText);
            Page<UserVO> userVOPage = userService.listUserVOByPage(userQueryRequest);

            PostQueryRequest postQueryRequest = new PostQueryRequest();
            postQueryRequest.setSearchText(searchText);
            Page<PostVO> postVOPage = postService.listPostVOByPage(postQueryRequest, request);

            Page<Picture> picturePage = pictureService.searchPicture(searchText, 1, 10);

            searchVO.setUserList(userVOPage.getRecords());
            searchVO.setPostList(postVOPage.getRecords());
            searchVO.setPictureList(picturePage.getRecords());
        }
        else {
            switch (searchTypeEnum) {
                case POST:
                    PostQueryRequest postQueryRequest = new PostQueryRequest();
                    postQueryRequest.setSearchText(searchText);
                    Page<PostVO> postVOPage = postService.listPostVOByPage(postQueryRequest, request);
                    searchVO.setPostList(postVOPage.getRecords());
                    break;
                case USER:
                    UserQueryRequest userQueryRequest = new UserQueryRequest();
                    userQueryRequest.setUserName(searchText);
                    Page<UserVO> userVOPage = userService.listUserVOByPage(userQueryRequest);
                    searchVO.setUserList(userVOPage.getRecords());
                    break;
                case PICTURE:
                    Page<Picture> picturePage = pictureService.searchPicture(searchText, 1, 10);
                    searchVO.setPictureList(picturePage.getRecords());
                    break;
                default:
                    break;
            }
        }
        return ResultUtils.success(searchVO)；
    }
}
