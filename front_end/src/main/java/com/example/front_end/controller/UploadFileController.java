package com.example.front_end.controller;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UploadFileController {
    public static String uploadFile(String pathfile) throws IOException, InterruptedException, URISyntaxException {
        String url = "http://localhost:8080/upload"; // Thay đổi URL của service Spring Boot nếu cần thiết

        Path path = Paths.get(pathfile); // Thay đổi đường dẫn đến file tải lên

        HttpClient httpClient = HttpClientBuilder.create().build();

        HttpPost uploadFile = new HttpPost(new URI(url));
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

        File file = new File(path.toString());
        FileBody fileBody = new FileBody(file, ContentType.DEFAULT_BINARY);
        builder.addPart("file", fileBody);

        HttpEntity entity = builder.build();
        uploadFile.setEntity(entity);

        HttpResponse response = httpClient.execute(uploadFile);
        String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
        System.out.println(responseBody);
        return responseBody;
    }
}
