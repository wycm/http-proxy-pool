package com.wy.hpp.proxy;


import com.wy.hpp.proxy.entity.Direct;
import com.wy.hpp.proxy.entity.Proxy;
import com.wy.hpp.proxy.site.ip181.Ip181ProxyListPageParser;
import com.wy.hpp.proxy.site.ip66.Ip66ProxyListPageParser;
import com.wy.hpp.proxy.site.mimiip.MimiipProxyListPageParser;
import com.wy.hpp.proxy.site.xicidaili.XicidailiProxyListPageParser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.wy.hpp.proxy.util.Constants.TIME_INTERVAL;

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
    static {
        int pages = 8;
        for(int i = 1; i <= pages; i++){
            proxyMap.put("http://www.xicidaili.com/wt/" + i + ".html", XicidailiProxyListPageParser.class);
            proxyMap.put("http://www.xicidaili.com/nn/" + i + ".html", XicidailiProxyListPageParser.class);
            proxyMap.put("http://www.xicidaili.com/wn/" + i + ".html", XicidailiProxyListPageParser.class);
            proxyMap.put("http://www.xicidaili.com/nt/" + i + ".html", XicidailiProxyListPageParser.class);
            proxyMap.put("http://www.ip181.com/daili/" + i + ".html", Ip181ProxyListPageParser.class);
            proxyMap.put("http://www.mimiip.com/gngao/" + i, MimiipProxyListPageParser.class);
            proxyMap.put("http://www.mimiip.com/gnpu/" + i, MimiipProxyListPageParser.class);
//            proxyMap.put("http://www.66ip.cn/" + i + ".html", Ip66ProxyListPageParser.class);
//            for(int j = 1; j < 34; j++){
//                proxyMap.put("http://www.66ip.cn/areaindex_" + j + "/" + i + ".html", Ip66ProxyListPageParser.class);
//            }
        }
        proxyQueue.add(new Direct(TIME_INTERVAL));
    }

}
