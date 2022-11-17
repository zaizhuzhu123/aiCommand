package com.laien.aiCommand;

//import org.mybatis.spring.annotation.MapperScan;

import com.laien.aiCommand.config.AppliacationInfo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

/**
 * 启动
 *
 * @author xsd
 */
@MapperScan({"com.laien.media_compression.mapper"})
@SpringBootApplication(scanBasePackages = "com.laien.*", exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class})
@EnableScheduling
public class AiCommandJavaApplication {

    public static void main(String[] args) {
        if (args != null & args.length > 0) {
            AppliacationInfo.machineId = args[0];
        }
        AppliacationInfo.applicationContext = SpringApplication.run(AiCommandJavaApplication.class, args);
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

}
