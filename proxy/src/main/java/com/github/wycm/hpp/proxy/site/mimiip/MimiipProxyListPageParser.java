package com.github.wycm.hpp.proxy.site.mimiip;


import com.github.wycm.hpp.http.entity.Page;
import com.github.wycm.hpp.proxy.entity.Proxy;
import com.github.wycm.hpp.http.parser.ListPageParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class MimiipProxyListPageParser implements ListPageParser<Proxy> {
    @Override
    public List<Proxy> parseListPage(Page page) {
        Document document = Jsoup.parse(page.getHtml());
        Elements elements = document.select("table[class=list] tr");
        List<Proxy> proxyList = new ArrayList<>(elements.size());
        for (int i = 1; i < elements.size(); i++){
            Proxy proxy = new Proxy();
            String ip = elements.get(i).select("td:eq(0)").first().text();
            String port  = elements.get(i).select("td:eq(1)").first().text();
            String location  = elements.get(i).select("td:eq(2)").first().text();
            String anonymous = elements.get(i).select("td:eq(3)").first().text();
            String type = elements.get(i).select("td:eq(4)").first().text();
            proxy.setIp(ip);
            proxy.setPort(Integer.valueOf(port));
            proxy.setLocation(location);
            proxy.setAnonymous(anonymous);
            proxy.setType(type);
            proxyList.add(proxy);
        }
        return proxyList;
    }
}
