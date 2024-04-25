package com.Spring.application.repository;

import com.Spring.application.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long>{
    List<Student> findAll();
}
