package com.yihan.yuso.controller;

import com.yihan.yuso.common.BaseResponse;
import com.yihan.yuso.common.ResultUtils;
import com.yihan.yuso.manager.SearchFacade;
import com.yihan.yuso.model.dto.search.SearchRequest;
import com.yihan.yuso.model.vo.SearchVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 统一搜索接口接口
 *
 * @author <a href="https://github.com/Litou-lyh">liyihan</a>
 */
@RestController
@RequestMapping("/search")
@CrossOrigin("http://111.231.22.221")
@Slf4j
public class SearchController {
    @Resource
    SearchFacade searchFacade;

    @PostMapping("/all")
    public BaseResponse<SearchVO> doSearchAll(@RequestBody SearchRequest searchRequest, HttpServletRequest request) {
        return ResultUtils.success(searchFacade.searchAll(searchRequest, request));
    }
}
