package com.github.wycm.hpp.proxy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.wycm.hpp.proxy.entity.Proxy;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.jsoup.helper.StringUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * Created by Administrator on 2017/5/27.
 */
public class HttpRequestHandler implements HttpHandler{
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestMethod = httpExchange.getRequestMethod();
        if (requestMethod.equalsIgnoreCase("GET")) {
            process(httpExchange);
        }
    }
    private void process(HttpExchange httpExchange) throws IOException {httpExchange.sendResponseHeaders(200, 0);
        Map <String,String> parms = queryToMap(httpExchange.getRequestURI().getQuery());

        OutputStream responseBody = httpExchange.getResponseBody();
        List<Proxy> proxyList = new ArrayList<>(ProxyPool.proxySet.size());

        proxyList = getProxyList(parms.get("protocol"), parms.get("anonymous"));
//        proxyList.addAll(ProxyPool.proxySet);

        JSONArray jsonArray = (JSONArray) JSON.toJSON(proxyList);
        for (int i = 0; i < jsonArray.size(); i++) {
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
    private List<Proxy> getProxyList(String protocol, String anonymous){
        if (protocol == null){
            protocol = new String("");
        }
        if (anonymous == null){
            anonymous = new String("");
        }
        List<Proxy> proxyList = new ArrayList<>(ProxyPool.proxySet.size());
        proxyList.addAll(ProxyPool.proxySet);
        List<Proxy> newProxyList = new LinkedList<>();
        for (Proxy proxy : proxyList){
            if (!StringUtil.isBlank(proxy.getType())
                    && proxy.getType().toLowerCase().contains(protocol)
                    && !StringUtil.isBlank(proxy.getAnonymous())
                    && proxy.getAnonymous().toLowerCase().contains(anonymous)){
                newProxyList.add(proxy);
            }
        }
        Collections.sort(newProxyList, new Comparator<Proxy>() {
            @Override
            public int compare(Proxy o1, Proxy o2) {
                return (int) (o1.getResponseTime() - o2.getResponseTime());
            }
        });
        return newProxyList;
    }
    /**
     * returns the url parameters in a map
     * @param query
     * @return map
     */
    public static Map<String, String> queryToMap(String query){
        if (query == null){
            query = "";
        }
        Map<String, String> result = new HashMap<String, String>();
        for (String param : query.split("&")) {
            String pair[] = param.split("=");
            if (pair.length>1) {
                result.put(pair[0], pair[1]);
            }else{
                result.put(pair[0], "");
            }
        }
        return result;
    }
}
