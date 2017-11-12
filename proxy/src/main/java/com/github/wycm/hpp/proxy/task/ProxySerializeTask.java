package com.github.wycm.hpp.proxy.task;

import com.alibaba.fastjson.JSON;
import com.github.wycm.hpp.proxy.ProxyPool;
import com.github.wycm.hpp.proxy.entity.Proxy;
import com.github.wycm.hpp.proxy.util.Config;
import com.github.wycm.hpp.proxy.util.ProxyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;

/**
 * 代理序列化
 */
public class ProxySerializeTask implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(ProxySerializeTask.class);
    @Override
    public void run() {
        while (true){

            Proxy[] proxyArray = null;
            ProxyPool.lock.readLock().lock();
            try {
                proxyArray = new Proxy[ProxyPool.proxyQueue.size()];
                int i = 0;
                Object[] proxies = ProxyPool.proxyQueue.toArray();
                for (Object p : proxies){
                    Proxy proxy = (Proxy) p;
                    if (!ProxyUtil.isDiscardProxy(proxy)){
                        proxyArray[i++] = proxy;
                    }
                }
            } finally {
                ProxyPool.lock.readLock().unlock();
            }

//            HttpClientUtil.serializeObject(proxyArray, Config.getProperty("proxyPath"));
            try {
                ProxyUtil.serializeProxy(proxyArray, Config.getProperty("proxyPath"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            logger.info("成功序列化" + proxyArray.length + "个代理");
            logger.info(JSON.toJSONString(proxyArray));
            try {
                Thread.sleep(1000 * 60 * 5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
