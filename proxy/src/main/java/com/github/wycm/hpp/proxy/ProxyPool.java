package com.github.wycm.hpp.proxy;


import com.github.wycm.hpp.proxy.entity.Proxy;
import com.github.wycm.hpp.proxy.site.DefaultParserTemplate;
import com.github.wycm.hpp.proxy.site.ip66.Ip66ProxyListPageParser;
import com.github.wycm.hpp.proxy.site.mimiip.MimiipProxyListPageParser;
import com.github.wycm.hpp.proxy.entity.Direct;
import com.github.wycm.hpp.proxy.site.ip181.Ip181ProxyListPageParser;
import com.github.wycm.hpp.proxy.site.xicidaili.XicidailiProxyListPageParser;
import com.github.wycm.hpp.proxy.util.Config;

import java.util.*;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.github.wycm.hpp.proxy.util.Constants.TIME_INTERVAL;

/**
 * 代理池
 */
public class ProxyPool {
    /**
     * proxySet读写锁
     */
    public final static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    public final static Set<Proxy> proxySet = new HashSet<Proxy>();
    /**
     * 代理池延迟队列
     */
    public final static DelayQueue<Proxy> proxyQueue = new DelayQueue();
    public final static Map<String, Class> proxyMap = new HashMap<>();
    public final static List<DefaultParserTemplate> defalutParserTemplateList = new ArrayList<>();
    static {
        int pages = Integer.valueOf(Config.getProperty("proxyPages"));
        DefaultParserTemplate xiciTemplate = new DefaultParserTemplate();
        xiciTemplate.setDomain("xicidaili.com");
        defalutParserTemplateList.add(xiciTemplate);

        DefaultParserTemplate ip181Template = new DefaultParserTemplate();
        ip181Template.setDomain("ip181.com");
        ip181Template.setCharsetName("gb2312");
        defalutParserTemplateList.add(ip181Template);

        DefaultParserTemplate mimiipTemplate = new DefaultParserTemplate();
        mimiipTemplate.setDomain("mimiip.com");
        defalutParserTemplateList.add(mimiipTemplate);

        DefaultParserTemplate _66ipTemplate = new DefaultParserTemplate();
        _66ipTemplate.setDomain("66ip.cn");
        _66ipTemplate.setCharsetName("gb2312");
        defalutParserTemplateList.add(_66ipTemplate);



        for(int i = 1; i <= pages; i++){
            proxyMap.put("http://www.xicidaili.com/wt/" + i + ".html", XicidailiProxyListPageParser.class);
            proxyMap.put("http://www.xicidaili.com/nn/" + i + ".html", XicidailiProxyListPageParser.class);
            proxyMap.put("http://www.xicidaili.com/wn/" + i + ".html", XicidailiProxyListPageParser.class);
            proxyMap.put("http://www.xicidaili.com/nt/" + i + ".html", XicidailiProxyListPageParser.class);
            xiciTemplate.getUrlList().add("http://www.xicidaili.com/wt/" + i + ".html");
            xiciTemplate.getUrlList().add("http://www.xicidaili.com/nn/" + i + ".html");
            xiciTemplate.getUrlList().add("http://www.xicidaili.com/wn/" + i + ".html");
            xiciTemplate.getUrlList().add("http://www.xicidaili.com/nt/" + i + ".html");

            proxyMap.put("http://www.ip181.com/daili/" + i + ".html", Ip181ProxyListPageParser.class);
            ip181Template.getUrlList().add("http://www.ip181.com/daili/" + i + ".html");

            proxyMap.put("http://www.mimiip.com/gngao/" + i, MimiipProxyListPageParser.class);
            proxyMap.put("http://www.mimiip.com/gnpu/" + i, MimiipProxyListPageParser.class);
            mimiipTemplate.getUrlList().add("http://www.mimiip.com/gngao/" + i);
            mimiipTemplate.getUrlList().add("http://www.mimiip.com/gngao/" + i);

            proxyMap.put("http://www.66ip.cn/" + i + ".html", Ip66ProxyListPageParser.class);
            _66ipTemplate.getUrlList().add("http://www.66ip.cn/" + i + ".html");
            for(int j = 1; j < 34; j++){
                proxyMap.put("http://www.66ip.cn/areaindex_" + j + "/" + i + ".html", Ip66ProxyListPageParser.class);
                _66ipTemplate.getUrlList().add("http://www.66ip.cn/areaindex_" + j + "/" + i + ".html");
            }
        }
        proxyQueue.add(new Direct(TIME_INTERVAL));
    }

}
