package com.laien.aiCommand.schedule.impl.process.util;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.sshtools.forker.client.DefaultNonBlockingProcessListener;
import com.sshtools.forker.client.ForkerBuilder;
import com.sshtools.forker.client.impl.nonblocking.NonBlockingProcess;
import com.sshtools.forker.common.IO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class CommandExecutor {

    /**
     * 执行命令 只关注命令是否执行成功
     *
     * @param timeout
     * @param unit
     * @param commond
     * @param successMark 成功标识
     * @throws IOException
     * @throws InterruptedException
     */
    public void exec(long timeout, TimeUnit unit, String commond, String successMark) throws IOException, InterruptedException {
        final boolean[] compressionFalg = {false};
        StringBuffer progressInfo = new StringBuffer();
        List<String> strings = Splitter.on(" ").omitEmptyStrings().splitToList(commond);
        execResult(timeout, unit, new CommandExecutor.CommondListener() {
            @Override
            public void onStdout(String str) {
                log.debug(str);
                progressInfo.append(str);
                if (str.contains(successMark)) {
                    compressionFalg[0] = true;
                }
            }

            @Override
            public void onExit(int exitCode) {

            }

            @Override
            public void onError(Exception exception) {
                throw new RuntimeException(exception);
            }
        }, strings.toArray(new String[strings.size()]));
        if (!compressionFalg[0]) {
            throw new RuntimeException(StringUtils.substring(progressInfo.toString(), progressInfo.length() - 200 > 0 ? (progressInfo.length() - 200) : 0, progressInfo.length()));
        }
    }

    private void execResult(long timeout, TimeUnit unit, CommondListener listener, String... command) throws IOException, InterruptedException {
        ForkerBuilder builder = new ForkerBuilder().io(IO.NON_BLOCKING).redirectErrorStream(true);
        builder.command(command);
        log.debug("runCommond : " + Joiner.on(" ").skipNulls().join(command));
        Process process = builder.start(new DefaultNonBlockingProcessListener() {
            @Override
            public void onStdout(NonBlockingProcess process, ByteBuffer buffer, boolean closed) {
                if (!closed) {
                    byte[] bytes = new byte[buffer.remaining()];
                    /* Consume bytes from buffer (so position is updated) */
                    buffer.get(bytes);
                    listener.onStdout(new String(bytes));
                }
            }

            @Override
            public void onExit(int exitCode, NonBlockingProcess process) {
                listener.onExit(exitCode);
            }

            @Override
            public void onError(Exception exception, NonBlockingProcess process, boolean existing) {
                listener.onError(exception);
            }
        });
        if (timeout == 0) {
            process.waitFor();
        } else {
            process.waitFor(timeout, unit);
        }
    }

    /**
     * 执行命令 并获取命令的输出信息
     *
     * @param timeout
     * @param unit
     * @param command
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public String execResult(long timeout, TimeUnit unit, String command, CommondListener listener) throws IOException, InterruptedException {
        ForkerBuilder builder = new ForkerBuilder().io(IO.NON_BLOCKING).redirectErrorStream(true);
        List<String> strings = Splitter.on(" ").omitEmptyStrings().splitToList(command);
        List<String> newCommand = Lists.newArrayList();
        if (strings.contains("--prompt")) {
            for (int i = 0; i < strings.size(); i++) {
                String s = strings.get(i);
                if (!s.equals("--prompt")) {
                    newCommand.add(s);
                } else {
                    newCommand.add(s);
                    if ((i + 1) < strings.size()) {
                        String a = "";
                        for (int i1 = i + 1; i1 < strings.size(); i1++) {
                            String a1 = strings.get(i1);
                            a += (a1 + " ");
                        }
                        newCommand.add(a);
                    }
                    break;
                }
            }
        } else {
            newCommand = strings;
        }
        builder.command(newCommand.toArray(new String[newCommand.size()]));

        log.info("runCommond : " + command);
        StringBuffer processOutMsg = new StringBuffer();
        Process process = builder.start(new DefaultNonBlockingProcessListener() {
            @Override
            public void onStdout(NonBlockingProcess process, ByteBuffer buffer, boolean closed) {
                if (!closed) {
                    byte[] bytes = new byte[buffer.remaining()];
                    /* Consume bytes from buffer (so position is updated) */
                    buffer.get(bytes);
                    String str = new String(bytes);
                    processOutMsg.append(str);
                    if (listener != null) {
                        listener.onStdout(str);
                    }
                }
            }

            @Override
            public void onExit(int exitCode, NonBlockingProcess process) {
                super.onExit(exitCode, process);
                listener.onExit(exitCode);
            }

            @Override
            public void onError(Exception exception, NonBlockingProcess process, boolean existing) {
                super.onError(exception, process, existing);
                listener.onError(exception);
            }
        });
        if (timeout == 0) {
            process.waitFor();
        } else {
            process.waitFor(timeout, unit);
        }
        log.info(processOutMsg.toString());
        return processOutMsg.toString();
    }

    /**
     * 执行命令 并获取命令的输出信息
     *
     * @param timeout
     * @param unit
     * @param command
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public String execResult(long timeout, TimeUnit unit, String command) throws IOException, InterruptedException {
        ForkerBuilder builder = new ForkerBuilder().io(IO.NON_BLOCKING).redirectErrorStream(true);
        List<String> strings = Splitter.on(" ").omitEmptyStrings().splitToList(command);
        builder.command(strings.toArray(new String[strings.size()]));
        log.info("runCommond : " + command);
        StringBuffer processOutMsg = new StringBuffer();
        Process process = builder.start(new DefaultNonBlockingProcessListener() {
            @Override
            public void onStdout(NonBlockingProcess process, ByteBuffer buffer, boolean closed) {
                if (!closed) {
                    byte[] bytes = new byte[buffer.remaining()];
                    /* Consume bytes from buffer (so position is updated) */
                    buffer.get(bytes);
                    processOutMsg.append(new String(bytes));
                }
            }
        });
        if (timeout == 0) {
            process.waitFor();
        } else {
            process.waitFor(timeout, unit);
        }
        log.info(processOutMsg.toString());
        return processOutMsg.toString();
    }


    public interface CommondListener {
        /**
         * 执行中的正常信息输出
         *
         * @param str
         */
        void onStdout(String str);

        /**
         * 执行退出
         *
         * @param exitCode
         */
        void onExit(int exitCode);

        /**
         * 执行异常
         *
         * @param exception
         */
        void onError(Exception exception);
    }

//    public static void main(String[] args) throws IOException, InterruptedException {
//        CommandUtils.runCommond(20L, TimeUnit.SECONDS, new CommandUtils.CommondListener() {
//            @Override
//            public void onStdout(String str) {
//                System.out.println(str);
//            }
//
//            @Override
//            public void onExit(int exitCode) {
//                System.out.println(exitCode);
//            }
//
//            @Override
//            public void onError(Exception exception) {
//                System.out.println(exception.getMessage());
//            }
//        }, "java", "-jar", "/Users/apple/Documents/代码/content-admin-java/cms/target/cms.jar");
//    }
}
