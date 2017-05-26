package com.wy.hpp.http;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2017/4/17.
 */
public class ListTest {
    @Test
    public void testList(){
        List<String> tempList = new ArrayList<>();
        tempList.add("str1");
        tempList.add("str2");
        tempList.add("str3");
        Iterator<String> iterator = tempList.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }
    }
}
