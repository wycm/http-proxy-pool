package com.wy.hpp.site;

import com.wy.hpp.entity.Page;
import com.wy.hpp.proxy.ProxyHttpClient;
import com.wy.hpp.proxy.entity.Proxy;
import com.wy.hpp.proxy.site.ip181.Ip181ProxyListPageParser;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

public class Ip181ProxyListPageParserTest {
    @Test
    public void testParse() throws IOException {
        System.out.println(Charset.defaultCharset().toString());
//        Page page = ProxyHttpClient.getInstance().getWebPage("http://www.ip181.com/daili/1.html");
        Page page = ProxyHttpClient.getInstance().getWebPage("http://www.ip181.com/daili/1.html", "gb2312");
        List<Proxy> urlList = new Ip181ProxyListPageParser().parseListPage(page);
        System.out.println(urlList.size());
    }
}