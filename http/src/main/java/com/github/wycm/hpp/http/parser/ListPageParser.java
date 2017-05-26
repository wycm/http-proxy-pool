package com.github.wycm.hpp.http.parser;


import com.github.wycm.hpp.http.entity.Page;

import java.util.List;

public interface ListPageParser<T> extends Parser {
    List<T> parseListPage(Page page);
}
