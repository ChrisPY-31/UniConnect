package com.chris.uniconnect.Model.Dto;

import com.chris.uniconnect.Enum.ContactType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class ContactDto {

    private Integer idContacto;

    @Enumerated(EnumType.STRING)
    private ContactType red;

}
