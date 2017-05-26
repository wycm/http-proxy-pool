package com.wy.hpp.proxy.task;

import com.wy.hpp.proxy.ProxyPool;
import com.wy.hpp.proxy.entity.Proxy;
import com.wy.hpp.proxy.util.Config;
import com.wy.hpp.proxy.util.ProxyUtil;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;

/**
 * 代理序列化
 */
public class ProxySerializeTask implements Runnable{
    private static Logger logger = Logger.getLogger(ProxySerializeTask.class);
    @Override
    public void run() {
        while (true){

            Proxy[] proxyArray = null;
            ProxyPool.lock.readLock().lock();
            try {
                proxyArray = new Proxy[ProxyPool.proxySet.size()];
                int i = 0;
                for (Proxy p : ProxyPool.proxySet){
                    if (!ProxyUtil.isDiscardProxy(p)){
                        proxyArray[i++] = p;
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
            try {
                Thread.sleep(1000 * 60 * 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
