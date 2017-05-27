package com.github.wycm.hpp.proxy.site;

import java.util.List;

/**
 * Created by Administrator on 2017/5/27.
 */
public class UrlTemplate {
    private String urlTemplate;
    /**
     * pageNumber
     * eg:1-8
     * 多个使用/分割
     */
    private String pageNumberRangeList;
    /**
     * classify
     * eg:sortA,sortB,sortC
     * 个使用/分割
     */
    private String classifyList;

    public String getUrlTemplate() {
        return urlTemplate;
    }

    public void setUrlTemplate(String urlTemplate) {
        this.urlTemplate = urlTemplate;
    }

    public String getPageNumberRangeList() {
        return pageNumberRangeList;
    }

    public void setPageNumberRangeList(String pageNumberRangeList) {
        this.pageNumberRangeList = pageNumberRangeList;
    }

    public String getClassifyList() {
        return classifyList;
    }

    public void setClassifyList(String classifyList) {
        this.classifyList = classifyList;
    }
}
