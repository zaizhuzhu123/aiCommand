package com.laien.aiCommand.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.laien.aiCommand.service.IProcessStepService;
import com.laien.aiCommand.thread.step.ProcessStep;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Component
public class ProcessStepServiceImpl implements IProcessStepService {

    @Resource
    private List<ProcessStep> allProcessSteps;

    private Map<String, List<ProcessStep>> stepNameProcess = Maps.newHashMap();

    public ProcessStepServiceImpl() {
        for (ProcessStep processStep : allProcessSteps) {
            List<ProcessStep> processes = stepNameProcess.get(processStep.type());
            if (processes == null) {
                processes = Lists.newArrayList();
                stepNameProcess.put(processStep.type(), processes);
            }
            processes.add(processStep);
        }
    }


    @Override
    public List<ProcessStep> getProcessSteps(String stepName) {
        return stepNameProcess.get(stepName);
    }

}
