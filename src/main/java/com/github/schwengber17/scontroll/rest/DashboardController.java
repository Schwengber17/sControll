package com.github.schwengber17.scontroll.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.schwengber17.scontroll.dto.FamilyDashboard;
import com.github.schwengber17.scontroll.services.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
   
    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/family/{familyId}")
    public ResponseEntity<FamilyDashboard> getFamilyDashboard(@PathVariable Integer familyId) {
        FamilyDashboard dashboard = dashboardService.getFamilyDashboard(familyId);
        return ResponseEntity.ok(dashboard);
    }
}
