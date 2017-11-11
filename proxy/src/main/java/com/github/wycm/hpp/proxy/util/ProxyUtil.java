package com.github.wycm.hpp.proxy.util;


import com.alibaba.fastjson.JSON;
import com.github.wycm.hpp.http.entity.Page;
import com.github.wycm.hpp.http.util.*;
import com.github.wycm.hpp.proxy.ProxyHttpClient;
import com.github.wycm.hpp.proxy.entity.Proxy;
import org.apache.http.HttpHost;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProxyUtil {
    /**
     * 是否丢弃代理
     * 失败次数大于3，且失败率超过60%，丢弃
     */
    public static boolean isDiscardProxy(Proxy proxy){
        if (proxy.getSuccessfulAverageTime() >= 5000){
            return true;
        }
        int succTimes = proxy.getSuccessfulTimes();
        int failTimes = proxy.getFailureTimes();
        if(failTimes >= 3){
            double failRate = (failTimes + 0.0) / (succTimes + failTimes);
            if (failRate > 0.1){
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
//        FileInputStream fis = new FileInputStream(file);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream is = classLoader.getResourceAsStream(filePath);
        Proxy[] proxyArray = JSON.parseObject(is, new Proxy[0].getClass());
        return proxyArray;
    }
    public static boolean isAnonymous(Proxy proxy) throws IOException {

        HttpGet request = new HttpGet("http://1212.ip138.com/ic.asp");
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(com.github.wycm.hpp.http.util.Constants.TIMEOUT).
                setConnectTimeout(com.github.wycm.hpp.http.util.Constants.TIMEOUT).
                setConnectionRequestTimeout(com.github.wycm.hpp.http.util.Constants.TIMEOUT).
                setProxy(new HttpHost(proxy.getIp(), proxy.getPort())).
                setCookieSpec(CookieSpecs.STANDARD).
                build();
        request.setConfig(requestConfig);
        Page page = ProxyHttpClient.getInstance().getWebPage(request, "gb2312");
        System.out.println(page.getHtml());
        Pattern pattern = Pattern.compile("您的IP是：\\[(.*?)\\] 来自：(.*?)\\<");
        Matcher matcher = pattern.matcher(page.getHtml());
        if (matcher.find()){
            String ip = matcher.group(1);
            String location = matcher.group(2);
            if (proxy.getIp().equals(ip)){
                return true;
            }
        }
        return false;
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
