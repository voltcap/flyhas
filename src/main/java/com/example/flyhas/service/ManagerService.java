package com.example.flyhas.service;

import com.example.flyhas.model.Manager;
import com.example.flyhas.repository.ManagerRepository;
import org.springframework.stereotype.Service;

@Service
public class ManagerService {

    private final ManagerRepository managerRepository;

    public ManagerService(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    public void save(Manager manager) {
        managerRepository.save(manager);
    }
}
