package com.defterio.service;

import org.springframework.core.io.Resource;

import java.io.InputStream;

public interface StorageService {
    String save(InputStream inputStream, long size, String contentType, String storageKey);
    Resource loadAsResource(String storageKey);
    void delete(String storageKey);
}
