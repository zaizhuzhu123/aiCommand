//package com.laien.aiCommand.interceptor;
//
//import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
//import org.apache.ibatis.reflection.MetaObject;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//
//@Component
//public class MyDetaObjectHander implements MetaObjectHandler {
//
//    @Override
//    public void insertFill(MetaObject metaObject) {
//        // 当期操作人处理
//        String username = "admin";
//        // 当期操作人，操作时间处理
//        LocalDateTime now = LocalDateTime.now();
//        this.setFieldValByName("delFlag", 0, metaObject);
//        this.setFieldValByName("createUser", username, metaObject);
//        this.setFieldValByName("updateUser", username, metaObject);
//        this.setFieldValByName("createTime", now, metaObject);
//        this.setFieldValByName("updateTime", now, metaObject);
//
//    }
//
//    @Override
//    public void updateFill(MetaObject metaObject) {
//        // 当期操作人处理
//        String username = "admin";
//        setFieldValByName("updateUser", username, metaObject);
//        setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
//    }
//
//}