package com.chris.uniconnect.Mappers;

import com.chris.uniconnect.Model.Dto.CompanyProfileDto;
import com.chris.uniconnect.Model.Entity.CompanyProfile;
import com.chris.uniconnect.Model.Entity.CorporatePartnershipsUAEMex;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CompanyProfileMappers {

    CompanyProfileMappers INSTANCE = Mappers.getMapper(CompanyProfileMappers.class);

    //profile puede ser null (convenio que aun no tiene perfil): solo se llenan los datos del convenio
    @Mapping(source = "partnership.idCorporatePartnership", target = "idCorporacion")
    @Mapping(source = "partnership.companyName", target = "nombreEmpresa")
    @Mapping(source = "partnership.category", target = "categoria")
    @Mapping(source = "profile.description", target = "descripcion")
    @Mapping(source = "profile.logoUrl", target = "logo")
    @Mapping(source = "profile.bannerUrl", target = "banner")
    @Mapping(source = "profile.website", target = "sitioWeb")
    @Mapping(source = "profile.sector", target = "sector")
    @Mapping(source = "profile.companySize", target = "tamano")
    @Mapping(source = "profile.location", target = "ubicacion")
    @Mapping(source = "profile.contactEmail", target = "correoContacto")
    @Mapping(source = "profile.updatedAt", target = "actualizado")
    CompanyProfileDto toDto(CorporatePartnershipsUAEMex partnership, CompanyProfile profile);
}
