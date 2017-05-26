package com.github.wycm.hpp.http.parser;


import com.github.wycm.hpp.http.entity.Page;

public interface DetailPageParser<T> extends Parser {
    T parseDetailPage(Page page);
}
