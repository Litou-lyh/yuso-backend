package com.yihan.yuso.manager;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yihan.yuso.common.ErrorCode;
import com.yihan.yuso.dataSource.*;
import com.yihan.yuso.exception.BusinessException;
import com.yihan.yuso.exception.ThrowUtils;
import com.yihan.yuso.model.dto.search.SearchRequest;
import com.yihan.yuso.model.entity.Picture;
import com.yihan.yuso.model.enums.SearchTypeEnum;
import com.yihan.yuso.model.vo.PostVO;
import com.yihan.yuso.model.vo.SearchVO;
import com.yihan.yuso.model.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


@Slf4j
@Component
public class SearchFacade {
    @Resource
    private PostDataSource postDataSource;
    @Resource
    private UserDataSource userDataSource;
    @Resource
    private PictureDataSource pictureDataSource;
    @Resource
    private DataSourceRegistry dataSourceRegistry;
    public SearchVO searchAll(@RequestBody SearchRequest searchRequest, HttpServletRequest request) {
        String searchType = searchRequest.getType();
        if (searchType == null || StringUtils.isBlank(searchType)) {
            searchType = "all";
        }
        SearchTypeEnum searchTypeEnum = SearchTypeEnum.getEnumByValue(searchType);
        ThrowUtils.throwIf(searchTypeEnum == null, ErrorCode.PARAMS_ERROR);

        String searchText = searchRequest.getSearchText();
        long current = searchRequest.getCurrent();
        long pageSize = searchRequest.getPageSize();
        SearchVO searchVO = new SearchVO();
        if (searchTypeEnum == SearchTypeEnum.ALL) {
            // 子线程中拿不到request，这里inheritable set true
            RequestContextHolder.setRequestAttributes(RequestContextHolder.getRequestAttributes(),true);
            CompletableFuture<Page<PostVO>> postTask = CompletableFuture.supplyAsync(
                    () -> postDataSource.doSearch(searchText, current, pageSize)
            );
            CompletableFuture<Page<Picture>> pictureTask = CompletableFuture.supplyAsync(
                    () -> pictureDataSource.doSearch(searchText, current, pageSize)
            );
            CompletableFuture<Page<UserVO>> userTask = CompletableFuture.supplyAsync(
                    () -> userDataSource.doSearch(searchText, current, pageSize)
            );

//            Page<PostVO> postVOPage;
//            Page<UserVO> userVOPage = userDataSource.doSearch(searchText, current, pageSize);
//            postVOPage = postDataSource.doSearch(searchText, current, pageSize);
//            Page<Picture> picturePage = pictureDataSource.doSearch(searchText, current, pageSize);

            CompletableFuture.allOf(userTask, postTask, pictureTask).join();
            Page<UserVO> userVOPage;
            Page<PostVO> postVOPage;
            Page<Picture> picturePage;
            try {
                userVOPage = userTask.get();
                postVOPage = postTask.get();
                picturePage = pictureTask.get();
            } catch (Exception e) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "CompletableFuture failed in search!");
            }
            searchVO.setData(SearchTypeEnum.USER.getValue(), userVOPage.getRecords());
            searchVO.setData(SearchTypeEnum.POST.getValue(), postVOPage.getRecords());
            searchVO.setData(SearchTypeEnum.PICTURE.getValue(), picturePage.getRecords());
        }
        else {
            DataSource dataSource = dataSourceRegistry.getDataSourceByType(searchType);
            Page page = dataSource.doSearch(searchText, current, pageSize);
            searchVO.setData(searchType, page.getRecords());
        }
        return searchVO;
    }
}
