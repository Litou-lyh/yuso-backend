package com.yihan.yuso.model.vo;

import com.google.gson.Gson;
import com.yihan.yuso.model.entity.Picture;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 搜索结果视图
 *
 * @author <a href="https://github.com/Litou-lyh">liyihan</a>
 */
@Data
public class SearchVO implements Serializable {

    private final static Gson GSON = new Gson();

    /**
     *
     */
    List<UserVO> userList;
    /**
     *
     */
    List<PostVO> postList;
    /**
     *
     */
    List<Picture> pictureList;

}