package com.yihan.yuso.model.dto.picture;

import com.yihan.yuso.common.PageRequest;
import lombok.Data;

/**
 * 图片查询请求dto
 *
 * @author <a href="https://github.com/Litou-lyh">liyihan</a>
 */
@Data
public class PictureQeuryRequest extends PageRequest {
    private String searchText;
}
