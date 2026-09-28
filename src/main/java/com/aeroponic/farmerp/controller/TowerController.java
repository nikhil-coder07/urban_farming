package com.aeroponic.farmerp.controller;

import com.aeroponic.farmerp.model.Tower;
import com.aeroponic.farmerp.service.TowerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aeroponics/towers")
public class TowerController {

    @Autowired
    private TowerService towerService;

    @GetMapping
    public List<Tower> getAllTowers() {
        return towerService.getAllTowers();
    }

    @GetMapping("/{id}")
    public Tower getTowerById(@PathVariable Long id) {
        return towerService.getTowerById(id);
    }

    @PostMapping
    public ResponseEntity<Tower> createTower(@Valid @RequestBody Tower tower) {
        return new ResponseEntity<>(towerService.createTower(tower), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public Tower updateTower(@PathVariable Long id, @Valid @RequestBody Tower tower) {
        return towerService.updateTower(id, tower);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTower(@PathVariable Long id) {
        towerService.deleteTower(id);
        return ResponseEntity.noContent().build();
    }
}
