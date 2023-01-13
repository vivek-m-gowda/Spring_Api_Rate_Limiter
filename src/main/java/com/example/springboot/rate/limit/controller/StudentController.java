package com.example.springboot.rate.limit.controller;

import java.time.Duration;
import java.util.List;

import com.example.springboot.rate.limit.entity.Student;
import com.example.springboot.rate.limit.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    Refill refill = Refill.of(5, Duration.ofMinutes(1));
    private Bucket bucket = Bucket4j.builder().addLimit(Bandwidth.classic(5, refill)).build();

    @PostMapping("/save")
    public ResponseEntity<Object> saveOrUpadte(@RequestBody Student student){
        return new ResponseEntity(studentService.saveOrUpadte(student), HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Object> update(@RequestBody Student student){
        return new ResponseEntity(studentService.saveOrUpadte(student), HttpStatus.OK);
    }

    @GetMapping("/allStudent")
    public ResponseEntity<List<Student>> listAllStudent() {
        if(bucket.tryConsume(1)) {
            List<Student> students = studentService.listAllStudent();
            return new ResponseEntity<>(students, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
        }
    }

    @GetMapping("/findById")
    public ResponseEntity<Object> getStudentById(@RequestParam Long id){
        return new ResponseEntity(studentService.getStudentById(id), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public  ResponseEntity<Object> deleteStudent(@RequestParam Long id){
        studentService.deleteStudent(id);
        return new ResponseEntity<>("Student Deleted", HttpStatus.OK);
    }
}
