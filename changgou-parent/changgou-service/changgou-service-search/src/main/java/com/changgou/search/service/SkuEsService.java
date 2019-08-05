package com.changgou.search.service;

import java.util.Map;

public interface SkuEsService {
    void importSku();

    Map search(Map<String, String> searchMap);

    void deleteAll();
}
