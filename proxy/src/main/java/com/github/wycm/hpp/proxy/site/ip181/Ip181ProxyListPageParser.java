package com.github.wycm.hpp.proxy.site.ip181;


import com.github.wycm.hpp.http.entity.Page;
import com.github.wycm.hpp.http.parser.ListPageParser;
import com.github.wycm.hpp.proxy.entity.Proxy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class Ip181ProxyListPageParser implements ListPageParser<Proxy> {

    @Override
    public List<Proxy> parseListPage(Page page) {
        Document document = Jsoup.parse(page.getHtml());
        Elements elements = document.select("table tr:gt(0)");
        List<Proxy> proxyList = new ArrayList<>(elements.size());
        for (Element element : elements){
            Proxy proxy = new Proxy();
            String ip = element.select("td:eq(0)").first().text();
            String port  = element.select("td:eq(1)").first().text();
            String anonymous  = element.select("td:eq(2)").first().text();
            String type = element.select("td:eq(3)").first().text();
            String location = element.select("td:eq(5)").first().text();
            proxy.setIp(ip);
            proxy.setPort(Integer.valueOf(port));
            proxy.setLocation(location);
            proxy.setAnonymous(anonymous);
            proxy.setType(type);
            if(!anonymous.contains("匿")){
                //只要匿名代理
                continue;
            }
            proxyList.add(proxy);
        }
        return proxyList;
    }
}
