package com.yihan.yuso.model.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 图片
 */
@Data
public class Picture implements Serializable {
    public static final long serialVersionUID = -1782001018306780165L;

    private String title;

    private String url;
}
