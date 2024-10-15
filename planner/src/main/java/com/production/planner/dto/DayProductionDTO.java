package com.production.planner.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DayProductionDTO {
    private Integer id;
    private String day;
    private Integer dailyPlan;
}
