package com.github.wycm.hpp.proxy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.wycm.hpp.proxy.entity.Proxy;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2017/5/26.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        ProxyHttpClient.getInstance().startCrawl();
        InetSocketAddress addr = new InetSocketAddress(8080);
        HttpServer server = HttpServer.create(addr, 0);
        server.createContext("/http-proxy-pool", new SimpleHandler());
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
        System.out.println("Server is listening on port 8080");
    }

    static class SimpleHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            String requestMethod = exchange.getRequestMethod();
            if (requestMethod.equalsIgnoreCase("GET")) {
                exchange.sendResponseHeaders(200, 0);

                OutputStream responseBody = exchange.getResponseBody();
                List<Proxy> proxyList = new ArrayList<>(ProxyPool.proxySet.size());
                proxyList.addAll(ProxyPool.proxySet);
                Collections.sort(proxyList, new Comparator<Proxy>() {
                    @Override
                    public int compare(Proxy o1, Proxy o2) {
                        return (int) (o1.getCostTime() - o2.getCostTime());
                    }
                });
                JSONArray jsonArray = (JSONArray) JSON.toJSON(proxyList);
                for(int i = 0; i < jsonArray.size(); i++){
                    jsonArray.getJSONObject(i).remove("availableFlag");
                    jsonArray.getJSONObject(i).remove("anonymousFlag");
                    jsonArray.getJSONObject(i).remove("lastSuccessfulTime");
                    jsonArray.getJSONObject(i).remove("failureTimes");
                    jsonArray.getJSONObject(i).remove("successfulTimes");
                    jsonArray.getJSONObject(i).remove("successfulTotalTime");
                    jsonArray.getJSONObject(i).remove("successfulAverageTime");
                    jsonArray.getJSONObject(i).remove("timeInterval");
                    jsonArray.getJSONObject(i).remove("proxyStr");
                }
                responseBody.write(jsonArray.toJSONString().getBytes("GBK"));
                responseBody.close();
            }
        }
    }
}
