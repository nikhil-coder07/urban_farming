package com.aeroponic.farmerp.service;

import com.aeroponic.farmerp.exception.ResourceNotFoundException;
import com.aeroponic.farmerp.model.Tower;
import com.aeroponic.farmerp.repository.TowerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TowerService {

    @Autowired
    private TowerRepository towerRepository;

    public List<Tower> getAllTowers() {
        return towerRepository.findAll();
    }

    public Tower getTowerById(Long id) {
        return towerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tower not found with ID: " + id));
    }

    public Tower createTower(Tower tower) {
        return towerRepository.save(tower);
    }

    public Tower updateTower(Long id, Tower details) {
        Tower tower = getTowerById(id);
        tower.setZoneName(details.getZoneName());
        tower.setCapacity(details.getCapacity());
        tower.setLedSpectrum(details.getLedSpectrum());
        return towerRepository.save(tower);
    }

    public void deleteTower(Long id) {
        Tower tower = getTowerById(id);
        towerRepository.delete(tower);
    }
}
