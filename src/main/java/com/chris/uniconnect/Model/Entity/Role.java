package com.chris.uniconnect.Model.Entity;

import com.chris.uniconnect.Enum.Roles;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class Role {

    private Integer id;

    @Enumerated(EnumType.STRING)
    private Roles role;
}
