package com.chris.uniconnect.Model.Dto;

import com.chris.uniconnect.Model.Entity.PersonContactPK;
import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class PersonContactDto {

    private PersonContactPK id;

    private String url;

    private ContactDto contactos;

}
