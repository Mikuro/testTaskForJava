package ru.pushpyshev.testtaskparqour.service;

public interface IUrlRepository {
    String findUrlById(String id);

    void storeUrl(String id, String url);
}
