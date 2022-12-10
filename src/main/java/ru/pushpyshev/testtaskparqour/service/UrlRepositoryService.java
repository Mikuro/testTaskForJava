package ru.pushpyshev.testtaskparqour.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UrlRepositoryService implements IUrlRepository {
    private Map<String, String> urlByIdMap = new ConcurrentHashMap<>();

    @Override
    public String findUrlById(String shortenUrl) {
        return urlByIdMap.get(shortenUrl);
    }

    @Override
    public void storeUrl(String shortenUrl, String url) {
        urlByIdMap.put(shortenUrl, url);
    }
}