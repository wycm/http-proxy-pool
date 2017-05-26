package com.github.wycm.hpp.http;

import org.junit.Test;

import java.io.File;

/**
 * Created by Administrator on 2017/4/6.
 */
public class PathTest {
    @Test
    public void testPath(){
        File file = new File("src/main/resources/proxies.json");
        System.out.println(file);
    }
}
