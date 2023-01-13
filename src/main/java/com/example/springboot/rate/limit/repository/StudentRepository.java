package com.example.springboot.rate.limit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.springboot.rate.limit.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Long>{

}

