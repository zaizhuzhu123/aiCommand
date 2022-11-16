package com.laien.aiCommand.thread.step.dreambooth.impl;

import com.laien.aiCommand.schedule.impl.process.util.CommandExecutor;
import com.laien.aiCommand.thread.step.dreambooth.InstallDreamBoothStep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
@Order(6)
@Slf4j
public class KillWebuiStep implements InstallDreamBoothStep {

    @Resource
    private CommandExecutor commandExecutor;

    @Override
    public void run() throws IOException, InterruptedException {
        log.info("-------------------------------------------");
        log.info(this.getClass().getSimpleName());
        String cmd;
        cmd = "chmod +x /workspace/aiCommand/target/soft/shell/killReLauncher.sh";
        commandExecutor.execResult(30, TimeUnit.SECONDS, cmd);
        cmd = "sh /workspace/aiCommand/target/soft/shell/killReLauncher.sh";
        commandExecutor.execResult(30, TimeUnit.SECONDS, cmd);
        cmd = "chmod +x /workspace/aiCommand/target/soft/shell/killWebui.sh";
        commandExecutor.execResult(30, TimeUnit.SECONDS, cmd);
        cmd = "sh /workspace/aiCommand/target/soft/shell/killWebui.sh";
        commandExecutor.execResult(30, TimeUnit.SECONDS, cmd);
    }
}
