package com.dvk.ct250backend.infrastructure.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class FileUtils {

    private final Cloudinary cloudinary;

    public String saveTempFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        File tempFile = File.createTempFile("temp", Objects.requireNonNull(file.getOriginalFilename()));
        try (InputStream inputStream = file.getInputStream();
             FileOutputStream outputStream = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        return tempFile.getAbsolutePath();
    }

    public File convertMultipartFileToFile(MultipartFile file) throws IOException {
        File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        }
        return convFile;
    }

    public String uploadFileToCloudinary(File file) throws IOException {
        var uploadResult = cloudinary.uploader().upload(file, ObjectUtils.asMap("folder", "/airports/"));
        return uploadResult.get("url").toString();
    }

    public void deleteFileFromCloudinary(String name) throws IOException {
    cloudinary.uploader().destroy(name, ObjectUtils.emptyMap());
}

    public String getPublicIdFromCloudinary(String imageUrl) {
    String[] parts = imageUrl.split("/");
    return parts[parts.length - 2] + "/" + parts[parts.length - 1].substring(0, parts[parts.length - 1].lastIndexOf('.'));
}

}
