package com.production.planner.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductionPlanResponseDTO {
    private Long id;
    private String day;
    private int initialPlan;
    private int balancedPlan;
}
