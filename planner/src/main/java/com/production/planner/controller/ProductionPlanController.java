package com.production.planner.controller;

import com.production.planner.dto.ProductionRequestDTO;
import com.production.planner.sevice.ProductionPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/production")
public class ProductionPlanController {

    @Autowired
    private ProductionPlanService productionPlanService;
    @PostMapping("/savePlanner")
    public ResponseEntity<?> savePlanner(@RequestBody ProductionRequestDTO request) {
        return productionPlanService.productionPlanner(request);
    }
    @GetMapping("/plans")
    public ResponseEntity<?> getAllPlans() {
        return productionPlanService.getAllPlans();
    }

    @GetMapping("/plans/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        return productionPlanService.getById(id);
    }
}
