package com.wy.hpp.http.parser;


import com.wy.hpp.http.entity.Page;

import java.util.List;

public interface ListPageParser<T> extends Parser {
    List<T> parseListPage(Page page);
}
