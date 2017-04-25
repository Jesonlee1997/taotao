package com.taotao.search.service;

import com.taotao.common.pojo.SearchResult;

/**
 * Created by JesonLee
 * on 2017/4/24.
 */
public interface SearchService {
    SearchResult search(String queryString, int page, int rows);

}
