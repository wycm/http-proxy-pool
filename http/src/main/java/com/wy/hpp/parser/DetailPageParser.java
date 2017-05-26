package com.wy.hpp.parser;


import com.wy.hpp.entity.Page;

public interface DetailPageParser<T> extends Parser {
    T parseDetailPage(Page page);
}
