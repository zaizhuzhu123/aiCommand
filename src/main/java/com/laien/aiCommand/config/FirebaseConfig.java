package com.laien.aiCommand.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.json.file}")
    private String firebaseJsonFile;
    @Value("${firebase.bucket.name}")
    private String firebaseBucketName;
    @Value("${firebase.bucket.url}")
    private String firebaseBucketUrl;
    @Value("${firebase.database.url}")
    private String firebaseDatabaseUrl;

    @SneakyThrows
    @Bean
    public FirebaseApp firebaseApp() {
        InputStream serviceAccount = new ClassPathResource(firebaseJsonFile).getInputStream();
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket(firebaseBucketName)
                .setDatabaseUrl(firebaseDatabaseUrl)
                .build();
        serviceAccount.close();
        return FirebaseApp.initializeApp(options);
    }

    @Bean
    @DependsOn("firebaseApp")
    public Bucket bucket() {
        return StorageClient.getInstance().bucket();
    }

    public String getFirebaseBucketUrl() {
        return firebaseBucketUrl;
    }

}
