package com.wy.hpp.parser;


import com.wy.hpp.entity.Page;

import java.util.List;

public interface ListPageParser<T> extends Parser {
    List<T> parseListPage(Page page);
}
