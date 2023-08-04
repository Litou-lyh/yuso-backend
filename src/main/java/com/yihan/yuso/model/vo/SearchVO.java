package com.yihan.yuso.model.vo;

import com.google.gson.Gson;
import com.yihan.yuso.model.entity.Picture;
import com.yihan.yuso.model.enums.SearchTypeEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 搜索结果视图
 *
 * @author <a href="https://github.com/Litou-lyh">liyihan</a>
 */
@Data
public class SearchVO implements Serializable {

    private final static Gson GSON = new Gson();


    private Map<String, List<Object>> dataListMap = new HashMap<>();


    public SearchVO() {
        dataListMap.put(SearchTypeEnum.POST.getValue(), null);
        dataListMap.put(SearchTypeEnum.USER.getValue(), null);
        dataListMap.put(SearchTypeEnum.PICTURE.getValue(), null);
    }
    public void setData(String type, List dataList) {
        dataListMap.put(type, dataList);
    }

}