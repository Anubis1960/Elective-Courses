package com.Spring.application.controller;

import com.Spring.application.service.impl.ApplicationPeriodServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/application-period")
public class ApplicationPeriodController {

    @Autowired
    private ApplicationPeriodServiceImpl applicationPeriodService;

    @GetMapping("/")
    public ResponseEntity<Boolean> getApplicationPeriod() {
        return new ResponseEntity<>(applicationPeriodService.getApplicationPeriod(), HttpStatus.OK);
    }

    @PutMapping("/")
    public ResponseEntity<Boolean> reverseApplicationPeriod() {
        return new ResponseEntity<>(applicationPeriodService.reverseApplicationPeriod(), HttpStatus.OK);
    }
}
