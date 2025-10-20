package com.chris.uniconnect.Model.Dto.Response;

import com.chris.uniconnect.Model.Dto.RecruiterDto;
import com.chris.uniconnect.Model.Dto.StudentDto;
import com.chris.uniconnect.Model.Dto.TeacherDto;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "tipo"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TeacherResponse.class, name = "teacher"),
        @JsonSubTypes.Type(value = RecruiterResponse.class, name = "recruiter"),
        @JsonSubTypes.Type(value = StudentResponse.class, name = "student")
})
@Data
public abstract class PersonaResponseM {

    private Integer id;

    private String nombre;

    private String apellido;

    private String imagen;

    private String especialidad;
}
