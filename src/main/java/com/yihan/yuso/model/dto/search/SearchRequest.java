package com.yihan.yuso.model.dto.search;

import com.yihan.yuso.common.PageRequest;
import lombok.Data;

/**
 * 图片查询请求dto
 *
 * @author <a href="https://github.com/Litou-lyh">liyihan</a>
 */
@Data
public class SearchRequest extends PageRequest {
    private String searchText;
}
