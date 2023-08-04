package com.yihan.yuso.dataSource;

import com.yihan.yuso.model.enums.SearchTypeEnum;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Component
public class DataSourceRegistry {

    @Resource
    PostDataSource postDataSource;
    @Resource
    UserDataSource userDataSource;
    @Resource
    PictureDataSource pictureDataSource;

    Map<String, DataSource> dataSourceMap;

    @PostConstruct
    public void doInit() {
        dataSourceMap = new HashMap<>();
        dataSourceMap.put(SearchTypeEnum.POST.getValue(), postDataSource);
        dataSourceMap.put(SearchTypeEnum.USER.getValue(), userDataSource);
        dataSourceMap.put(SearchTypeEnum.PICTURE.getValue(), pictureDataSource);
    }

    public DataSource getDataSourceByType(String searchType) {
        return this.dataSourceMap.get(searchType);
    }
}
