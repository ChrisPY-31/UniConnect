package com.chris.uniconnect.Mappers;

import com.chris.uniconnect.Model.Dto.CareerDto;
import com.chris.uniconnect.Model.Dto.StudentAllDto;
import com.chris.uniconnect.Model.Dto.StudentDto;
import com.chris.uniconnect.Model.Entity.Career;
import com.chris.uniconnect.Model.Entity.Student;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;

@Mapper()
public interface StudentMappers {

    StudentMappers INSTANCE = Mappers.getMapper(StudentMappers.class);


    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "name", source = "nombre"),
            @Mapping(target = "lastName", source = "apellido"),
            @Mapping(target = "image", source = "imagen"),
            @Mapping(target = "career", source = "carrera")
    })
    Student studentAllDtoToStudent(StudentAllDto studentDto);

    @InheritInverseConfiguration
    StudentAllDto studentToStudentAllDto(Student student);

    @Mapping(source = "idCareer", target = "idCarrera")
    @Mapping(source = "careerName", target = "carrera")
    CareerDto carrerDtoToCareer(Career career);

}
