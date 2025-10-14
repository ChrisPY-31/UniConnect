package com.chris.uniconnect.Model.Dto.Response;

import com.chris.uniconnect.Model.Dto.ContactDto;
import lombok.Data;

@Data
public class RedesContactoResponse {
    private String url;

    private ContactDto contactos;
}
