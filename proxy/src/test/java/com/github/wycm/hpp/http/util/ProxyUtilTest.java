package com.github.wycm.hpp.http.util;

import com.github.wycm.hpp.proxy.entity.Proxy;
import com.github.wycm.hpp.proxy.util.ProxyUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by Administrator on 2017/5/27.
 */
public class ProxyUtilTest {
    @Test
    public void isAnonymousTest() throws IOException {
        Proxy proxy = new Proxy("120.24.63.205", 80);
//        Proxy proxy = new Proxy("61.180.233.194", 8998);
        boolean result = ProxyUtil.isAnonymous(proxy);
        Assert.assertEquals(result, true);
    }
}
