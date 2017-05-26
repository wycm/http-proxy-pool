package com.wy.hpp.proxy.site;


import com.wy.hpp.http.parser.ListPageParser;
import com.wy.hpp.proxy.entity.Proxy;

import java.util.HashMap;
import java.util.Map;

public class ProxyListPageParserFactory {
    private static Map<String, ListPageParser<Proxy>> map  = new HashMap<>();
    public static ListPageParser<Proxy> getProxyListPageParser(Class clazz){
        String parserName = clazz.getSimpleName();
        ListPageParser<Proxy> proxyListPageParser = null;
        if (map.containsKey(parserName)){
            return map.get(parserName);
        }
        else {
            try {
                ListPageParser<Proxy> parser = (ListPageParser<Proxy>) clazz.newInstance();
                parserName = clazz.getSimpleName();
                map.put(parserName, parser);
                return parser;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
