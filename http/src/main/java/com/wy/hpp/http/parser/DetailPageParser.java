package com.wy.hpp.http.parser;


import com.wy.hpp.http.entity.Page;

public interface DetailPageParser<T> extends Parser {
    T parseDetailPage(Page page);
}
