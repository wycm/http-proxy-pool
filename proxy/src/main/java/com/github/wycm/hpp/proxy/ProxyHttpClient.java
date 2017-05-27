package com.github.wycm.hpp.proxy;

import com.github.wycm.hpp.proxy.entity.Proxy;
import com.github.wycm.hpp.proxy.site.DefaultParserTemplate;
import com.github.wycm.hpp.proxy.task.ProxyPageTask;
import com.github.wycm.hpp.proxy.task.ProxySerializeTask;
import com.github.wycm.hpp.proxy.util.Config;
import com.github.wycm.hpp.http.util.SimpleThreadPoolExecutor;
import com.github.wycm.hpp.http.util.ThreadPoolMonitor;
import com.github.wycm.hpp.http.httpclient.AbstractHttpClient;
import com.github.wycm.hpp.proxy.util.ProxyUtil;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ProxyHttpClient extends AbstractHttpClient {
    private static final Logger logger = Logger.getLogger(ProxyHttpClient.class);
    private volatile static ProxyHttpClient instance;

    public static ProxyHttpClient getInstance(){
        if (instance == null){
            synchronized (ProxyHttpClient.class){
                if (instance == null){
                    instance = new ProxyHttpClient();
                }
            }
        }
        return instance;
    }
    /**
     * 代理测试线程池
     */
    private ThreadPoolExecutor proxyTestThreadExecutor;

    private Map<String, SimpleThreadPoolExecutor> threadPoolExecutorMap = new HashMap<>();
    public ProxyHttpClient(){
        initThreadPool();
        initProxy();
    }
    /**
     * 初始化线程池
     */
    private void initThreadPool(){
        proxyTestThreadExecutor = new SimpleThreadPoolExecutor(300, 300,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(10000),
                new ThreadPoolExecutor.DiscardPolicy(),
                "proxyTestThreadExecutor");
        for(DefaultParserTemplate template : ProxyPool.defalutParserTemplateList){
            threadPoolExecutorMap.put(template.getDomain()
                    ,new SimpleThreadPoolExecutor(1, 2,
                            0L, TimeUnit.MILLISECONDS,
                            new LinkedBlockingQueue<Runnable>(1000), template.getDomain() +
                            "-ProxyDownloadThreadExecutor"));
        }
        new Thread(new ThreadPoolMonitor(proxyTestThreadExecutor, "ProxyTestThreadPool")).start();
    }

    /**
     * 初始化proxy
     */
    private void initProxy(){
        Proxy[] proxyArray = null;
        try {
//            proxyArray = (Proxy[]) HttpClientUtil.deserializeObject(Config.getProperty("proxyPath"));
            proxyArray = ProxyUtil.deserializeObject(Config.getProperty("proxyPath"));
            int usableProxyCount = 0;
            for (Proxy p : proxyArray){
                if (p == null){
                    continue;
                }
            }
            logger.info("反序列化proxy成功，" + proxyArray.length + "个代理,可用代理" + usableProxyCount + "个");
        } catch (Exception e) {
            logger.warn("反序列化proxy失败");
        }
    }
    /**
     * 抓取代理
     */
    public void startCrawl(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    for(DefaultParserTemplate template : ProxyPool.defalutParserTemplateList){
                        for(String url : template.getUrlList()) {
                            threadPoolExecutorMap.get(template.getDomain())
                                    .execute(new ProxyPageTask(template.getDomain(), url, false, template.getCharsetName()));
                        }
                    }
                    try {
                        Thread.sleep(1000 * 60 * 60);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        new Thread(new ProxySerializeTask()).start();
    }
    public ThreadPoolExecutor getProxyTestThreadExecutor() {
        return proxyTestThreadExecutor;
    }

    public Map<String, SimpleThreadPoolExecutor> getThreadPoolExecutorMap() {
        return threadPoolExecutorMap;
    }
}