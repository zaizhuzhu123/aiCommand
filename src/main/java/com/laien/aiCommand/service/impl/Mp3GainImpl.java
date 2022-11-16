//package com.laien.aiCommand.service.impl;
//
//import com.laien.aiCommand.service.IMp3Gain;
//import com.sshtools.forker.client.DefaultNonBlockingProcessListener;
//import com.sshtools.forker.client.ForkerBuilder;
//import com.sshtools.forker.client.impl.nonblocking.NonBlockingProcess;
//import com.sshtools.forker.common.IO;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import java.io.IOException;
//import java.nio.ByteBuffer;
//import java.util.concurrent.TimeUnit;
//
//@Service
//@Slf4j
//public class Mp3GainImpl implements IMp3Gain {
//
//    @Override
//    public void process(String waitProcessDir) throws IOException, InterruptedException {
//        execResult(10, TimeUnit.SECONDS, new String[]{"docker", "run", "-i", "--rm", "--name=mp3gain", "--env", "parameters=-r -c -d 8", "-v", waitProcessDir + ":/data", "fkrivsky/mp3gain"});
//    }
//
//    public String execResult(long timeout, TimeUnit unit, String... command) throws IOException, InterruptedException {
//        ForkerBuilder builder = new ForkerBuilder().io(IO.NON_BLOCKING).redirectErrorStream(true);
//        builder.command(command);
//        log.debug("runCommond : " + command);
//        StringBuffer processOutMsg = new StringBuffer();
//        Process process = builder.start(new DefaultNonBlockingProcessListener() {
//            @Override
//            public void onStdout(NonBlockingProcess process, ByteBuffer buffer, boolean closed) {
//                if (!closed) {
//                    byte[] bytes = new byte[buffer.remaining()];
//                    /* Consume bytes from buffer (so position is updated) */
//                    buffer.get(bytes);
//                    processOutMsg.append(new String(bytes));
//                }
//            }
//        });
//        if (timeout == 0) {
//            process.waitFor();
//        } else {
//            process.waitFor(timeout, unit);
//        }
//        log.debug(processOutMsg.toString());
//        return processOutMsg.toString().replace("mp3gain will run with the following parameters: -r -c -d 8", "").replace("/data/waitProcessSound.mp3", "").trim();
//    }
//}
