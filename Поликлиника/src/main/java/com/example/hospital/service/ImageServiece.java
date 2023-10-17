package com.example.hospital.service;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

@Service
@NoArgsConstructor(force = true)
public class ImageServiece {

    @Autowired
    private ResourceLoader resourceLoader;

    private  String bucket;

    @SneakyThrows
    void uploadImage(String imagePath, InputStream content) {
        Path fullImagePath=Path.of(bucket,imagePath);
        try(content){
            Files.createDirectories(fullImagePath.getParent());
            Files.write(fullImagePath,content.readAllBytes(), StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING);
        }
    }

    @SneakyThrows
    @PostConstruct
    private void init(){
        Resource resource = resourceLoader.getResource("file:");
        bucket= resource.getFile().getAbsolutePath()+"\\images";
    }

    @SneakyThrows
    public Optional<byte[]> getImage(String imagePath ){
        Path fullImagePath=Path.of(bucket,imagePath);
        return Files.exists(fullImagePath)
                ?Optional.of(Files.readAllBytes(fullImagePath))
                :Optional.empty();
    }
}
