package com.wy.hpp.proxy.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wy.hpp.proxy.entity.Proxy;

import java.io.*;

public class ProxyUtil {
    /**
     * 是否丢弃代理
     * 失败次数大于３，且失败率超过60%，丢弃
     */
    public static boolean isDiscardProxy(Proxy proxy){
        int succTimes = proxy.getSuccessfulTimes();
        int failTimes = proxy.getFailureTimes();
        if(failTimes >= 3){
            double failRate = (failTimes + 0.0) / (succTimes + failTimes);
            if (failRate > 0.6){
                return true;
            }
        }
        return false;
    }

    public static void serializeProxy(Proxy[] proxyArray, String filePath) throws FileNotFoundException {
        PrintStream printStream = new PrintStream(filePath);
        printStream.println(JSON.toJSONString(proxyArray));
    }

    public static Proxy[] deserializeObject(String filePath) throws IOException {
        File file = new File(filePath);
        FileInputStream fis = new FileInputStream(file);
        Proxy[] proxyArray = JSON.parseObject(fis, new Proxy[0].getClass());
        return proxyArray;
    }

    public static void main(String[] args) throws FileNotFoundException {
        String filePath = "proxy/src/main/resources/proxies.json";
        System.out.println(filePath);
        File file = new File(filePath);
        filePath = file.getAbsolutePath();
//        System.out.println(file.getAbsolutePath());
        FileOutputStream fis = new FileOutputStream(filePath);
        PrintWriter pw = new PrintWriter(fis);
//        PrintStream printStream = new PrintStream(file.getAbsolutePath());
    }
}
