package com.example.flyhas.service;

import com.example.flyhas.model.Customer;
import com.example.flyhas.model.Manager;
import com.example.flyhas.model.Admin;
import com.example.flyhas.repository.CustomerRepository;
import com.example.flyhas.repository.ManagerRepository;
import com.example.flyhas.repository.AdminRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;
    private final ManagerRepository managerRepository;
    private final AdminRepository adminRepository;

    public CustomUserDetailsService(CustomerRepository customerRepository,
            ManagerRepository managerRepository,
            AdminRepository adminRepository) {
        this.customerRepository = customerRepository;
        this.managerRepository = managerRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return customerRepository.findByEmail(email).map(this::buildUserDetails)
                .or(() -> managerRepository.findByEmail(email).map(this::buildUserDetails))
                .or(() -> adminRepository.findByEmail(email).map(this::buildUserDetails))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    private UserDetails buildUserDetails(Object userObj) {
        String email;
        String password;
        List<SimpleGrantedAuthority> authorities;
        if (userObj instanceof Customer) {
            Customer customer = (Customer) userObj;
            email = customer.getEmail();
            password = customer.getPassword();
            authorities = List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
        } else if (userObj instanceof Manager) {
            Manager manager = (Manager) userObj;
            email = manager.getEmail();
            password = manager.getPassword();
            authorities = List.of(new SimpleGrantedAuthority("ROLE_MANAGER"));
        } else if (userObj instanceof Admin) {
            Admin admin = (Admin) userObj;
            email = admin.getEmail();
            password = admin.getPassword();
            authorities = List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            throw new UsernameNotFoundException("Invalid user type");
        }
        return new org.springframework.security.core.userdetails.User(email, password, authorities);
    }
}
