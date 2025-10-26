package com.chris.uniconnect.Mappers;

import com.chris.uniconnect.Model.Dto.CompanyDto;
import com.chris.uniconnect.Model.Entity.Company;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CompanyMappers {

    CompanyMappers INSTANCE = Mappers.getMapper(CompanyMappers.class);

    @Mapping(source = "idCompany", target = "idCompania")
    @Mapping(source = "name", target = "nombre")
    @Mapping(source = "sector", target = "sector")
    @Mapping(source = "location", target = "locacion")
    CompanyDto companyToCompanyDto(Company company);

    @InheritInverseConfiguration
    Company companyDtoToCompany(CompanyDto companyDto);

    List<CompanyDto> companyListToCompanyDtoList(List<Company> companyList);
}
