package com.yihan.yuso.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.yihan.yuso.common.BaseResponse;
import com.yihan.yuso.common.ErrorCode;
import com.yihan.yuso.common.ResultUtils;
import com.yihan.yuso.exception.BusinessException;
import com.yihan.yuso.model.dto.post.PostQueryRequest;
import com.yihan.yuso.model.dto.search.SearchRequest;
import com.yihan.yuso.model.dto.user.UserQueryRequest;
import com.yihan.yuso.model.entity.Picture;
import com.yihan.yuso.model.vo.PostVO;
import com.yihan.yuso.model.vo.SearchVO;
import com.yihan.yuso.model.vo.UserVO;
import com.yihan.yuso.service.PictureService;
import com.yihan.yuso.service.PostService;
import com.yihan.yuso.service.UserService;
import lombok.extern.slf4j.Slf4j;
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

        String searchText = searchRequest.getSearchText();


        CompletableFuture<Page<UserVO>> userTask = CompletableFuture.supplyAsync( () -> {
            UserQueryRequest userQueryRequest = new UserQueryRequest();
            userQueryRequest.setUserName(searchText);
            Page<UserVO> userVOPage = userService.listUserVOByPage(userQueryRequest);
            return userVOPage;
        });

        CompletableFuture<Page<PostVO>>postTask = CompletableFuture.supplyAsync( () -> {
            PostQueryRequest postQueryRequest = new PostQueryRequest();
            postQueryRequest.setSearchText(searchText);
            Page<PostVO> postVOPage = postService.listPostVOByPage(postQueryRequest, request);
            return postVOPage;

        });

        CompletableFuture<Page<Picture>> pictureTask = CompletableFuture.supplyAsync( () -> {
            Page<Picture> picturePage = pictureService.searchPicture(searchText, 1, 10);
            return picturePage;
        });

        CompletableFuture.allOf(userTask, postTask, pictureTask).join();

        try {
            Page<UserVO> userVOPage = userTask.get();
            Page<PostVO> postVOPage = postTask.get();
            Page<Picture> picturePage = pictureTask.get();

            SearchVO searchVO = new SearchVO();
            searchVO.setUserList(userVOPage.getRecords());
            searchVO.setPostList(postVOPage.getRecords());
            searchVO.setPictureList(picturePage.getRecords());

            return ResultUtils.success(searchVO);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "搜索失败");
        }



    }
}
