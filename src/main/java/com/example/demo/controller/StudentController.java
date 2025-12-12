package com.example.demo.controller;

import com.example.demo.model.Student;
import com.example.demo.repo.StudentRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentRepo studentRepo;

    // Constructor injection only for the repo (don't inject entity beans)
    public StudentController(StudentRepo studentRepo) {
        this.studentRepo = studentRepo;
    }

    // Create / Add a student
    @PostMapping("/add")
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        Student saved = studentRepo.save(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // Get all students
    @GetMapping
    public List<Student> getAllStudents() {
        return studentRepo.findAll();
    }

    // Get student by id
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable int id) {
        Optional<Student> s = studentRepo.findById(id);
        return s.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Update student (replace)
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable int id, @RequestBody Student updated) {
        return studentRepo.findById(id)
                .map(existing -> {
                    existing.setName(updated.getName());
                    existing.setAge(updated.getAge());
                    Student saved = studentRepo.save(existing);
                    return ResponseEntity.ok(saved);
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Delete student
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable int id) {
        if (studentRepo.existsById(id)) {
            studentRepo.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
