package com.chris.uniconnect.Model.Dto.Response;

import com.chris.uniconnect.Enum.SkillType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillResponse {

    private Integer id;

    private String nombre;

    private SkillType tipo;
}
