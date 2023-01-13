package com.example.springboot.rate.limit.service;

import com.example.springboot.rate.limit.entity.Student;
import java.util.List;
import java.util.Optional;

public interface StudentService {

    Student saveOrUpadte(Student student);
    List<Student> listAllStudent();
    Optional<Student> getStudentById (Long id);
    void deleteStudent(Long id);
}
