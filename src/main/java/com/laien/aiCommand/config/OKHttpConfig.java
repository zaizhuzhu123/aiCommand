package com.laien.aiCommand.config;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class OKHttpConfig {

    @Bean
    public OkHttpClient okHttpClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //设置连接超时时间
                .connectTimeout(10, TimeUnit.SECONDS)
                //设置读取超时时间
                .readTimeout(10, TimeUnit.SECONDS)
                //设置连接池
                .connectionPool(new ConnectionPool())
                .build();
        return okHttpClient;
    }
}
