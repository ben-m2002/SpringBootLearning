package com.ben.learning.Controllers;

import com.ben.learning.DTOS.StudentDto;
import com.ben.learning.DTOS.StudentResponseDto;
import com.ben.learning.Entities.School;
import com.ben.learning.Entities.Student;
import com.ben.learning.Repository.SchoolRepository;
import com.ben.learning.Repository.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StudentController {

    private final StudentRepository studentRepository;
    private final SchoolRepository schoolRepository;

    public StudentController(StudentRepository studentRepository, SchoolRepository schoolRepository) {
        this.studentRepository = studentRepository;
        this.schoolRepository = schoolRepository;
    }


    @PostMapping("/students")
    public StudentResponseDto helloPost(
            @RequestBody StudentDto studentDto
    ) {
        var student = this.toStudent(studentDto); // this method takes care of whether school exists or not
        var school = this.schoolRepository.findById(studentDto.schoolId()).orElse(null);
        assert school != null;
        student.setSchool(school);
        school.getStudents().add(student);
        this.schoolRepository.save(school); // this will update school
        var savedStudent = this.studentRepository.save(student);
        return this.toStudentResponseDto(savedStudent);
    }

    private Student toStudent(StudentDto studentDto) {
        var student = new Student();
        student.setFirstName(studentDto.firstName());
        student.setLastName(studentDto.lastName());
        student.setEmail(studentDto.email());
        School school = this.schoolRepository.findById(studentDto.schoolId()).orElse(null);
        if (school == null) {
            throw new RuntimeException("School not found");
        }
        return student;
    }

    private StudentResponseDto toStudentResponseDto(Student student) {
        return new StudentResponseDto(
                student.getFirstName(),
                student.getLastName(),
                student.getEmail()
        );
    }

    @GetMapping("/students")
    public List<Student> getStudents() {
        return this.studentRepository.findAll();
    }

    @GetMapping("/students/{id}")
    public Student getStudent(@PathVariable Integer id) {
        return this.studentRepository.findById(id).orElse(new Student());
    }

    @GetMapping("/students/search/{student-name}")
    public List<Student> findStudentsByName(
            @PathVariable("student-name") String studentName
    ) {
        return this.studentRepository.findAllByFirstNameContains(studentName);
    }

    @PostMapping("/students/{id}")
    public Student updateStudent(
            @PathVariable("id") Integer id,
            @RequestBody Student student
    ) {
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
    ) {
        this.studentRepository.deleteById(id);
    }
}
