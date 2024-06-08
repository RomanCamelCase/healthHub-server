package com.gmail.romkatsis.healthhubserver.dtos.embedded;

import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class WorkingDayDto {

    @NotNull
    private DayOfWeek dayOfWeek;

    @NotNull
    private LocalTime openingTime;

    @NotNull
    private LocalTime closingTime;

    public WorkingDayDto() {
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(LocalTime openingTime) {
        this.openingTime = openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }
}
