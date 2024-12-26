package com.ben.learning;
import com.ben.learning.Repository.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.List;

@RestController
public class FirstController {

    private final StudentRepository studentRepository;

    public FirstController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    @PostMapping("/students")
    public Student helloPost(
            @RequestBody Student student
    ){
        return this.studentRepository.save(student);
    }

    @GetMapping("/students")
    public List<Student> getStudents(){
        return this.studentRepository.findAll();
    }

    @GetMapping("/students/{id}")
    public Student getStudent(@PathVariable Integer id){
        return this.studentRepository.findById(id).orElse(new Student());
    }

    @GetMapping("/students/search/{student-name}")
    public List<Student> findStudentsByName(
            @PathVariable("student-name") String studentName
    ){
        return this.studentRepository.findAllByFirstNameContains(studentName);
    }

    @PostMapping("/students/{id}")
    public Student updateStudent(
            @PathVariable("id") Integer id,
            @RequestBody Student student
    ){
        Student oldStudent = this.studentRepository.findById(id).orElse(new Student());
        oldStudent.setFirstName(student.getFirstName());
        oldStudent.setLastName(student.getLastName());
        oldStudent.setEmail(student.getEmail());
        oldStudent.setAge(student.getAge());
        return this.studentRepository.save(oldStudent);
    }

    @DeleteMapping("/students/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteStudent(
            @PathVariable("id") Integer id
    )
    {
        this.studentRepository.deleteById(id);
    }
}
