package com.github.wycm.hpp.proxy.task;

import com.github.wycm.hpp.http.entity.Page;
import com.github.wycm.hpp.proxy.entity.Proxy;
import com.github.wycm.hpp.http.httpclient.AbstractHttpClient;
import com.github.wycm.hpp.proxy.ProxyPool;
import com.github.wycm.hpp.http.util.Constants;
import org.apache.http.HttpHost;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * 代理检测task
 * 将可用代理添加到DelayQueue延时队列中
 */
public class ProxyTestTask implements Runnable{
    private final static Logger logger = Logger.getLogger(ProxyTestTask.class);
    private Proxy proxy;
    private AbstractHttpClient httpClient;
    private String url;
    public ProxyTestTask(Proxy proxy, AbstractHttpClient httpClient, String url){
        this.proxy = proxy;
        this.httpClient = httpClient;
        url = this.url;
    }
    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        HttpGet request = null;
        if (proxy.getType() != null && proxy.getType().toLowerCase().contains("https")){
            request = new HttpGet("https://www.baidu.com");
        } else {
            request = new HttpGet("http://www.baidu.com");
        }
        try {
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(Constants.TIMEOUT).
                    setConnectTimeout(Constants.TIMEOUT).
                    setConnectionRequestTimeout(Constants.TIMEOUT).
                    setProxy(new HttpHost(proxy.getIp(), proxy.getPort())).
                    setCookieSpec(CookieSpecs.STANDARD).
                    build();
            request.setConfig(requestConfig);
            Page page = httpClient.getWebPage(request);
            long endTime = System.currentTimeMillis();
            String logStr = Thread.currentThread().getName() + " " + proxy.getProxyStr() +
                    "  executing request " + page.getUrl()  + " response statusCode:" + page.getStatusCode() +
                    "  request cost time:" + (endTime - startTime) + "ms";
            if (page == null || page.getStatusCode() != 200){
                logger.warn(logStr);
                return;
            }
            request.releaseConnection();
            proxy.setCostTime(endTime - startTime);
            logger.debug(proxy.toString() + "---------" + page.toString());
            if(!ProxyPool.proxySet.contains(proxy)){
                logger.debug(proxy.toString() + "----------代理可用--------请求耗时:" + (endTime - startTime) + "ms");
                ProxyPool.lock.writeLock().lock();
                try {
                    proxy.setLastSuccessfulTime(System.currentTimeMillis());
                    ProxyPool.proxySet.add(proxy);
                } finally {
                    ProxyPool.lock.writeLock().unlock();
                }
                ProxyPool.proxyQueue.add(proxy);
            }
        } catch (IOException e) {
            logger.debug("IOException:", e);
        } finally {
            if (request != null){
                request.releaseConnection();
            }
        }
    }
    private String getProxyStr(){
        return proxy.getIp() + ":" + proxy.getPort();
    }
}
