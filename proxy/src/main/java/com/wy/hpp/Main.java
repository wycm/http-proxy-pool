package com.wy.hpp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.wy.hpp.proxy.ProxyHttpClient;
import com.wy.hpp.proxy.ProxyPool;
import com.wy.hpp.proxy.entity.Proxy;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
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
//            if (requestMethod.equalsIgnoreCase("GET")) {
//                OutputStream responseBody = exchange.getResponseBody();
//                JSONArray jsonArray = (JSONArray) JSON.toJSON(ProxyPool.proxySet);
//                responseBody.write(jsonArray.toJSONString().getBytes());
//                responseBody.close();
//            }
            if (requestMethod.equalsIgnoreCase("GET")) {
                exchange.sendResponseHeaders(200, 0);

                OutputStream responseBody = exchange.getResponseBody();
                JSONArray jsonArray = (JSONArray) JSON.toJSON(ProxyPool.proxySet);
                responseBody.write(jsonArray.toJSONString().getBytes("GBK"));
                responseBody.close();
            }
        }
    }
}
