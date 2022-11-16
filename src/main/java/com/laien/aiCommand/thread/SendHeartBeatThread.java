package com.laien.aiCommand.thread;

import com.laien.aiCommand.config.AppliacationInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SendHeartBeatThread extends Thread {


    @Value("${laien.aiManager.heartbeat.url}")
    private String heartBeatCallbackUrl;

    public SendHeartBeatThread() {
        this.setName(this.getClass().getSimpleName());
    }

    @Override
    public void run() {
        while (!AppliacationInfo.isSendHeartbeatSuccess) {
            try {
                sendHeartBeat();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                //每五秒上报一次心跳 直到心跳上报成功
                try {
                    Thread.sleep(5 * 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendHeartBeat() {
        log.info("开始上报心跳情况,目标地址:" + heartBeatCallbackUrl);
        log.info("心跳上报成功");
        AppliacationInfo.isSendHeartbeatSuccess = true;
    }
}
