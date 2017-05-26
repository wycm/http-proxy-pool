package com.wy.hpp.proxy.site.mimiip;


import com.wy.hpp.entity.Page;
import com.wy.hpp.parser.ListPageParser;
import com.wy.hpp.proxy.entity.Proxy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static com.wy.hpp.proxy.util.Constants.TIME_INTERVAL;

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
