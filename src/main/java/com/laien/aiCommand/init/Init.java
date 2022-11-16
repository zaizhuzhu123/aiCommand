//package com.laien.aiCommand.init;
//
//import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
//import com.laien.aiCommand.entity.Task;
//import com.laien.aiCommand.service.ITaskService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.event.ApplicationPreparedEvent;
//import org.springframework.context.ApplicationListener;
//import org.springframework.stereotype.Component;
//
//import static com.laien.aiCommand.constant.MediaConstant.COMPRESSION_STATUS_ING;
//import static com.laien.aiCommand.constant.MediaConstant.COMPRESSION_STATUS_WAIT;
//
//@Component
///**
// * spring容器实例初始化完成后需要执行的操作
// */
//public class Init implements ApplicationListener<ApplicationPreparedEvent> {
//
//    @Autowired
//    private ITaskService taskService;
//
//    @Override
//    public void onApplicationEvent(ApplicationPreparedEvent applicationPreparedEvent) {
//        /**
//         * 在程序运行中 可能存在停电，内存不足等等情况造成程序中断的问题
//         * 此时，可能有压缩任务正在运行中 为了保证重启后 压缩任务仍然能够正常运行
//         * 需要将异常中断的正在执行的任务恢复到待处理状态 确保压缩任务都能得到完整的执行流程
//         */
//        LambdaUpdateWrapper<Task> updateWrapper = new LambdaUpdateWrapper<>();
//        updateWrapper.eq(Task::getCompressionStatus, COMPRESSION_STATUS_ING);
//        updateWrapper.set(Task::getCompressionStatus, COMPRESSION_STATUS_WAIT);
//        taskService.update(new Task(), updateWrapper);
//    }
//}
