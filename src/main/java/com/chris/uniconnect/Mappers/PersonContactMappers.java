package com.chris.uniconnect.Mappers;

import com.chris.uniconnect.Model.Dto.ContactDto;
import com.chris.uniconnect.Model.Dto.PersonContactDto;
import com.chris.uniconnect.Model.Dto.PersonDto;
import com.chris.uniconnect.Model.Dto.Response.RedesContactoResponse;
import com.chris.uniconnect.Model.Entity.Contact;
import com.chris.uniconnect.Model.Entity.PersonContact;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PersonContactMappers {

    PersonContactMappers INSTANCE = Mappers.getMapper(PersonContactMappers.class);


    @Mapping(source = "id", target = "id")
    @Mapping(source = "url", target = "url")
    @Mapping(source = "contactos", target = "contact")
    PersonContact PersonContactDtoToPersonContact(PersonContactDto personContactDto);

    @InheritInverseConfiguration
    PersonContactDto personContactToPersonContactDto(PersonContact personContact);

    @Mapping(source = "idContact", target = "idContacto")
    @Mapping(source = "contact", target = "red")
    ContactDto contactToContactDto(Contact contact);

    PersonContactDto toDto(RedesContactoResponse redesContactoResponse);

    RedesContactoResponse toEntity(PersonContactDto personDto);

    List<PersonContact> toPersonContacList(List<PersonContactDto> redes);

    List<PersonContactDto> toPersonContacDtoList(List<PersonContact> redes);

}
