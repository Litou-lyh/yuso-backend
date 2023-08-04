package com.yihan.yuso.dataSource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yihan.yuso.model.entity.Picture;

/**
 * 数据源接口（新接入数据必须实现）
 * @param <T>
 */
public interface DataSource <T> {
    /**
     * @param searchText
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<T> doSearch(String searchText, long pageNum, long pageSize);
}
