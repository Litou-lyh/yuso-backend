package com.yihan.yuso.model.dto.search;

import com.yihan.yuso.common.PageRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * 图片查询请求dto
 *
 * @author <a href="https://github.com/Litou-lyh">liyihan</a>
 */
@Data
public class SearchRequest extends PageRequest implements Serializable {

    private String type;

    /**
     * 搜索词
     */
    private String searchText;

    public static final long serialVersionUID = -653828008626665475L;
}
