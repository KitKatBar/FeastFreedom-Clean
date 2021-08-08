package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Kitchen;

@Repository
public interface KitchenRepo extends JpaRepository<Kitchen, Long> {
	Kitchen findByEmail(String email);
}
