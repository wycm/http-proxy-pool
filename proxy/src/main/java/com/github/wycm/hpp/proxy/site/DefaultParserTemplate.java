package com.github.wycm.hpp.proxy.site;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/27.
 */
public class DefaultParserTemplate {
    private String templateId;
    private String domain;//域名
    private List<UrlTemplate> urlTemplateList;
    private List<String> urlList = new ArrayList<>();//url
    private String charsetName = "utf-8";

    private String baseCssQuery;
    private String ipCssQuery;
    private String portCssQuery;
    private String anonymousCssQuery;
    private String typeCssQuery;
    private String locationCssQuery;

    public List<UrlTemplate> getUrlTemplateList() {
        return urlTemplateList;
    }

    public void setUrlTemplateList(List<UrlTemplate> urlTemplateList) {
        this.urlTemplateList = urlTemplateList;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public List<String> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getBaseCssQuery() {
        return baseCssQuery;
    }

    public void setBaseCssQuery(String baseCssQuery) {
        this.baseCssQuery = baseCssQuery;
    }

    public String getIpCssQuery() {
        return ipCssQuery;
    }

    public void setIpCssQuery(String ipCssQuery) {
        this.ipCssQuery = ipCssQuery;
    }

    public String getPortCssQuery() {
        return portCssQuery;
    }

    public void setPortCssQuery(String portCssQuery) {
        this.portCssQuery = portCssQuery;
    }

    public String getAnonymousCssQuery() {
        return anonymousCssQuery;
    }

    public void setAnonymousCssQuery(String anonymousCssQuery) {
        this.anonymousCssQuery = anonymousCssQuery;
    }

    public String getTypeCssQuery() {
        return typeCssQuery;
    }

    public void setTypeCssQuery(String typeCssQuery) {
        this.typeCssQuery = typeCssQuery;
    }

    public String getCharsetName() {
        return charsetName;
    }

    public void setCharsetName(String charsetName) {
        this.charsetName = charsetName;
    }

    public String getLocationCssQuery() {
        return locationCssQuery;
    }

    public void setLocationCssQuery(String locationCssQuery) {
        this.locationCssQuery = locationCssQuery;
    }
}
