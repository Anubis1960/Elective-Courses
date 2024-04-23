package com.Spring.Main.repository;

import com.Spring.Main.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long>{
    List<Student> findAll();
}
