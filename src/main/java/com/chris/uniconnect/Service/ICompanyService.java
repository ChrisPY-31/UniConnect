package com.chris.uniconnect.Service;

import com.chris.uniconnect.Model.Dto.CompanyDto;
import java.util.List;

public interface ICompanyService {

    List<CompanyDto> getAllCompanies();

    CompanyDto getCompanyById(int id);

    CompanyDto createCompany(CompanyDto companyDto);

    void deleteCompany(CompanyDto companyDto);

    boolean existCompany(Integer id);
}
