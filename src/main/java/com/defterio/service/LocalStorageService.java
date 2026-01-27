package com.defterio.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@Slf4j
public class LocalStorageService implements StorageService {

    private final Path rootLocation;

    public LocalStorageService(@Value("${app.storage.path:./storage}") String storagePath) {
        this.rootLocation = Paths.get(storagePath).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage directory", e);
        }
    }

    @Override
    public String save(InputStream inputStream, long size, String contentType, String storageKey) {
        try {
            Path targetFile = this.rootLocation.resolve(storageKey).normalize();
            
            if (!targetFile.startsWith(this.rootLocation.normalize())) {
                throw new SecurityException("Cannot store file outside storage directory");
            }

            Path parentDir = targetFile.getParent();
            if (parentDir != null) {
                Files.createDirectories(parentDir);
            }

            Files.copy(inputStream, targetFile, StandardCopyOption.REPLACE_EXISTING);
            return storageKey;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    @Override
    public Resource loadAsResource(String storageKey) {
        try {
            Path file = this.rootLocation.resolve(storageKey).normalize();
            
            if (!file.startsWith(this.rootLocation.normalize())) {
                throw new SecurityException("Cannot access file outside storage directory");
            }

            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("File not found or not readable: " + storageKey);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load file: " + storageKey, e);
        }
    }

    @Override
    public void delete(String storageKey) {
        try {
            Path file = this.rootLocation.resolve(storageKey).normalize();
            
            if (!file.startsWith(this.rootLocation.normalize())) {
                throw new SecurityException("Cannot delete file outside storage directory");
            }

            Files.deleteIfExists(file);
        } catch (IOException e) {
            log.warn("Failed to delete file: " + storageKey, e);
        }
    }
}
