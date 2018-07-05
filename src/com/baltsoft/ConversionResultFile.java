package com.baltsoft;

import com.baltsoft.Model.ConversionResponseFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.CompletableFuture;

public class ConversionResultFile {
    private final ConversionResponseFile conversionResponseFile;

    public ConversionResultFile(ConversionResponseFile conversionResponseFile) {
        this.conversionResponseFile = conversionResponseFile;
    }

    public String getName() {
        return conversionResponseFile.FileName;
    }

    public int getSize() {
        return conversionResponseFile.FileSize;
    }

    public String getUrl() {
        return conversionResponseFile.Url;
    }

    public CompletableFuture<InputStream> asStream() {
        return Http.requestGet(getUrl());
    }

    public CompletableFuture<Path> saveFile(Path path) {
        return asStream().thenApplyAsync(s -> {
            try {
                Path filePath = Files.isDirectory(path) ? Paths.get(path.toString(), getName()) : path;
                Files.copy(s, filePath, StandardCopyOption.REPLACE_EXISTING);
                return filePath;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}