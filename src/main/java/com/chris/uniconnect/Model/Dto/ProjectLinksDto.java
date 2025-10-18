package com.chris.uniconnect.Model.Dto;

import com.chris.uniconnect.Enum.ProjectLinks;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class ProjectLinksDto {

    private Integer id;

    private Integer idProyecto;

    private String url;

    @Enumerated(EnumType.STRING)
    private ProjectLinks redNombre;

}
