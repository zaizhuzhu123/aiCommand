package com.laien.aiCommand.service;

import com.laien.aiCommand.thread.step.ProcessStep;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface IProcessStepService {

    public List<ProcessStep> getProcessSteps(String stepName);
}
