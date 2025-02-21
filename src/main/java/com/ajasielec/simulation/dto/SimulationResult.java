package com.ajasielec.simulation.dto;

import java.util.ArrayList;
import java.util.List;

public class SimulationResult {
    private List<StepStatus> stepStatuses;

    public SimulationResult() {
        this.stepStatuses = new ArrayList<>();
    }

    public void addStepStatus(StepStatus stepStatus) {
        stepStatuses.add(stepStatus);
    }

    public List<StepStatus> getStepStatuses() {
        return stepStatuses;
    }

    public void setStepStatuses(List<StepStatus> stepStatuses) {
        this.stepStatuses = stepStatuses;
    }
}
