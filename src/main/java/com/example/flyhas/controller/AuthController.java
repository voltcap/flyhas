package com.example.flyhas.controller;

import com.example.flyhas.dto.AuthRequest;
import com.example.flyhas.dto.AuthResponse;
import com.example.flyhas.model.Admin;
import com.example.flyhas.model.Customer;
import com.example.flyhas.model.Manager;
import com.example.flyhas.model.BaseUser;
import com.example.flyhas.repository.AdminRepository;
import com.example.flyhas.repository.CustomerRepository;
import com.example.flyhas.repository.ManagerRepository;
import com.example.flyhas.util.JwtTokenUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final CustomerRepository customerRepository;
    private final ManagerRepository managerRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager,
            JwtTokenUtil jwtTokenUtil,
            CustomerRepository customerRepository,
            ManagerRepository managerRepository,
            AdminRepository adminRepository,
            PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.customerRepository = customerRepository;
        this.managerRepository = managerRepository;
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest authRequest) {
        // Kullanıcının kimlik doğrulamasını yap
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));

        // Customer, Manager ve Admin repository'lerinden ilgili kullanıcıyı sırayla
        // arayın
        BaseUser baseUser = findBaseUserByEmail(authRequest.getEmail());
        if (baseUser == null) {
            throw new RuntimeException("User not found");
        }

        // BaseUser üzerinden JWT token üret
        String token = jwtTokenUtil.generateToken(baseUser);
        AuthResponse authResponse = new AuthResponse(token, baseUser.getEmail());
        return ResponseEntity.ok(authResponse);
    }

    // Yardımcı metot: email'e göre BaseUser bulur (Customer → Manager → Admin)
    private BaseUser findBaseUserByEmail(String email) {
        Optional<Customer> customerOpt = customerRepository.findByEmail(email);
        if (customerOpt.isPresent()) {
            return customerOpt.get();
        }
        Optional<Manager> managerOpt = managerRepository.findByEmail(email);
        if (managerOpt.isPresent()) {
            return managerOpt.get();
        }
        Optional<Admin> adminOpt = adminRepository.findByEmail(email);
        if (adminOpt.isPresent()) {
            return adminOpt.get();
        }
        return null;
    }

    // Kayıt endpoint'leri
    @PostMapping("/register/customer")
    public ResponseEntity<String> registerCustomer(@Valid @RequestBody Customer customer) {
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customerRepository.save(customer);
        return ResponseEntity.ok("Customer registered successfully!");
    }

    @PostMapping("/register/manager")
    public ResponseEntity<String> registerManager(@Valid @RequestBody Manager manager) {
        manager.setPassword(passwordEncoder.encode(manager.getPassword()));
        managerRepository.save(manager);
        return ResponseEntity.ok("Manager registered successfully!");
    }

    @PostMapping("/register/admin")
    public ResponseEntity<String> registerAdmin(@Valid @RequestBody Admin admin) {
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        adminRepository.save(admin);
        return ResponseEntity.ok("Admin registered successfully!");
    }
}