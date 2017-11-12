package com.github.wycm.hpp.proxy.site.ip66;


import com.github.wycm.hpp.http.entity.Page;
import com.github.wycm.hpp.proxy.entity.Proxy;
import com.github.wycm.hpp.http.parser.ListPageParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;


/**
 * http://www.66ip.cn/
 */
public class Ip66ProxyListPageParser implements ListPageParser<Proxy> {
    @Override
    public List<Proxy> parseListPage(Page page) {
        Document document = Jsoup.parse(page.getHtml());
        Elements elements = document.select("table tr:gt(1)");
        List<Proxy> proxyList = new ArrayList<>(elements.size());
        for (Element element : elements){
            Proxy proxy = new Proxy();
            String ip = element.select("td:eq(0)").first().text();
            String port  = element.select("td:eq(1)").first().text();
            String location  = element.select("td:eq(2)").first().text();
            String anonymous = element.select("td:eq(3)").first().text();
            proxy.setIp(ip);
            proxy.setPort(Integer.valueOf(port));
            proxy.setLocation(location);
            proxy.setAnonymous(anonymous);
            if(!anonymous.contains("匿")){
                //只要匿明代理
                continue;
            }
            proxyList.add(proxy);
        }
        return proxyList;
    }
}
