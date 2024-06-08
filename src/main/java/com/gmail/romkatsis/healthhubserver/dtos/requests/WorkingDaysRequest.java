package com.gmail.romkatsis.healthhubserver.dtos.requests;

import com.gmail.romkatsis.healthhubserver.dtos.embedded.WorkingDayDto;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public class WorkingDaysRequest {

    @NotNull
    private Set<WorkingDayDto> days;

    public WorkingDaysRequest() {
    }

    public @NotNull Set<WorkingDayDto> getDays() {
        return days;
    }

    public void setDays(@NotNull Set<WorkingDayDto> days) {
        this.days = days;
    }


}
