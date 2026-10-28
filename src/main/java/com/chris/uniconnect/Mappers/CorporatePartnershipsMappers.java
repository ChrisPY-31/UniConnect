package com.chris.uniconnect.Mappers;

import com.chris.uniconnect.Model.Dto.CorporatePartnershipsUAEMexDto;
import com.chris.uniconnect.Model.Entity.CorporatePartnershipsUAEMex;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CorporatePartnershipsMappers {

    CorporatePartnershipsMappers INSTANCE = Mappers.getMapper(CorporatePartnershipsMappers.class);

    @Mapping(source = "idCorporatePartnership", target = "idCorporacion")
    @Mapping(source = "companyName", target = "nombreEmpresa")
    @Mapping(source = "category", target = "categoria")
    @Mapping(source = "agreementType", target = "tipoAcuerdo")
    @Mapping(source = "startDate", target = "fechaInicio")
    @Mapping(source = "endDate", target = "fechaFinal")
    @Mapping(source = "active", target = "activo")
    @Mapping(source = "vigente", target = "vigente")
    CorporatePartnershipsUAEMexDto toDto(CorporatePartnershipsUAEMex partnership);

    //id, estado y reclutadores nunca se toman del body
    @Mapping(target = "idCorporatePartnership", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "recruiters", ignore = true)
    @Mapping(source = "nombreEmpresa", target = "companyName")
    @Mapping(source = "categoria", target = "category")
    @Mapping(source = "tipoAcuerdo", target = "agreementType")
    @Mapping(source = "fechaInicio", target = "startDate")
    @Mapping(source = "fechaFinal", target = "endDate")
    CorporatePartnershipsUAEMex toEntity(CorporatePartnershipsUAEMexDto dto);

    @InheritConfiguration(name = "toEntity")
    void updateFromDto(CorporatePartnershipsUAEMexDto dto, @MappingTarget CorporatePartnershipsUAEMex partnership);

    List<CorporatePartnershipsUAEMexDto> toDtoList(List<CorporatePartnershipsUAEMex> partnerships);
}
