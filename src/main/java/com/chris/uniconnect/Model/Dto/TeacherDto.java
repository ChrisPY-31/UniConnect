package com.chris.uniconnect.Model.Dto;

import com.chris.uniconnect.Enum.AcademicDegree;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeacherDto extends PersonDto {

    @Enumerated(EnumType.STRING)
    private AcademicDegree gradoAcademico;

    private String departamento;

    private List<RecomendationDto> recomendaciones;
}
