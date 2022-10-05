package com.hzx.search.service;

import com.hzx.search.vo.SearchParam;
import com.hzx.search.vo.SearchRes;

public interface MallSearchService {
    /**
     * @param searchParam 检索的所有参赛
     * @return 返回检索结果
     */
    SearchRes search(SearchParam searchParam);
}
