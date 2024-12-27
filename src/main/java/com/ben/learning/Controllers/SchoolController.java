package com.ben.learning.Controllers;


import com.ben.learning.DTOS.SchoolDto;
import com.ben.learning.Entities.School;
import com.ben.learning.Repository.SchoolRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SchoolController {

    private final SchoolRepository schoolRepository;

    public SchoolController(SchoolRepository schoolRepository){
        this.schoolRepository = schoolRepository;
    }

    @PostMapping("/schools")
    public SchoolDto createSchool(
        @RequestBody SchoolDto schoolDto
    ){
       School school = this.toSchool(schoolDto);
       School savedSchool = this.schoolRepository.save(school);
       return this.toSchoolDto(savedSchool);
    }

    @GetMapping("/schools")
    public List<SchoolDto> getSchools(){
        List<School> schools =  this.schoolRepository.findAll();
        return schools.stream().map(this::toSchoolDto).toList();
    }

    private School toSchool(SchoolDto schoolDto){
        return new School(
            schoolDto.name()
        );
    }

    private SchoolDto toSchoolDto(School school){
        return new SchoolDto(
            school.getName()
        );
    }

}
