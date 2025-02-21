package com.ajasielec.simulation.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimulationResult {
    private final List<StepStatus> stepStatuses = new ArrayList<>();

    public void addStepStatus(StepStatus stepStatus) {
        stepStatuses.add(stepStatus);
    }

    public List<StepStatus> getStepStatuses() {
        return Collections.unmodifiableList(stepStatuses);
    }
}
