package com.production.planner.sevice;

import com.production.planner.dto.DayProductionDTO;
import com.production.planner.dto.ProductionPlanResponseDTO;
import com.production.planner.dto.ProductionRequestDTO;
import com.production.planner.dto.ResponseWrapper;
import com.production.planner.model.Planner;
import com.production.planner.repository.PlannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class ProductionPlanService {
    @Autowired
    private PlannerRepository plannerRepository;

    public ResponseEntity<?> productionPlanner(ProductionRequestDTO productionRequestDTO) {
        List<DayProductionDTO> dailyPlans = productionRequestDTO.getDailyPlans();
        List<ProductionPlanResponseDTO> plannerList = new ArrayList<>();

        // ambil data daily plannya
        int[] dailyPlanArray = dailyPlans.stream().mapToInt(DayProductionDTO::getDailyPlan).toArray();

        // Identifikasi hari-hari yang tidak libur (produksi > 0)
        int[] activeDays = IntStream.range(0, dailyPlanArray.length)
                .filter(i -> dailyPlanArray[i] > 0)
                .toArray();

        // Hitung total produksi hanya untuk hari yang aktif
        int totalProduction = IntStream.of(dailyPlanArray).filter(p -> p > 0).sum();
        int activeDayCount = activeDays.length;

        // Rata-rata produksi untuk hari yang aktif
        int average = totalProduction / activeDayCount;
        int remainder = totalProduction % activeDayCount;

        // Membuat rencana produksi baru dengan nilai rata-rata
        int[] balancedPlan = dailyPlanArray.clone();
        for (int i : activeDays) {
            balancedPlan[i] = average;
        }

        // Distribusi sisa ke hari-hari dengan produksi tertinggi dalam rencana awal
        activeDays = IntStream.of(activeDays).boxed()
                .sorted((a, b) -> Integer.compare(dailyPlanArray[b], dailyPlanArray[a]))
                .mapToInt(i -> i).toArray();

        for (int i = 0; i < remainder; i++) {
            balancedPlan[activeDays[i]]++;
        }
        // Simpan data ke database
        for (int i = 0; i < dailyPlans.size(); i++) {
            DayProductionDTO dayProductionDTO = dailyPlans.get(i);
            Planner plan;
            if(dayProductionDTO.getId() != null){
             plan = plannerRepository.getById(dayProductionDTO.getId());
            } else {
                plan = new Planner();
            }
            plan.setDay(dayProductionDTO.getDay());
            plan.setInitialPlan(dailyPlanArray[i]);
            plan.setBalancedPlan( balancedPlan[i]);
            plannerRepository.save(plan);
            plannerList.add(new ProductionPlanResponseDTO(plan.getId(),plan.getDay(),plan.getInitialPlan(),plan.getBalancedPlan()));

        }
        return new ResponseEntity<>(ResponseWrapper.builder()
                .statusCode(200)
                .message("successfully")
                .data(plannerList)
                .build(), HttpStatus.OK);
    }

    public ResponseEntity<?> getAllPlans() {
        List<Planner> datas = plannerRepository.findAll();
        if (!datas.isEmpty()) {
            return new ResponseEntity<>(ResponseWrapper.builder()
                    .statusCode(200)
                    .message("successfully")
                    .data(datas)
                    .build(), HttpStatus.OK);
        } else {
            return ResponseEntity
                    .status(404)
                    .body(new ResponseWrapper<>(404, "planner not found", null));
        }
    }

    public ResponseEntity<?> getById(Integer id) {
        Planner data = plannerRepository.findById(id).orElse(null);
        if (data != null ) {
            return new ResponseEntity<>(ResponseWrapper.builder()
                    .statusCode(200)
                    .message("successfully")
                    .data(data)
                    .build(), HttpStatus.OK);
        } else {
            return ResponseEntity
                    .status(404)
                    .body(new ResponseWrapper<>(404, "planner by id = " + id + " not found", null));
        }
    }
}
