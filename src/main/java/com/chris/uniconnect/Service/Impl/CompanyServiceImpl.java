package com.chris.uniconnect.Service.Impl;

import com.chris.uniconnect.Mappers.CompanyMappers;
import com.chris.uniconnect.Model.Dto.CompanyDto;
import com.chris.uniconnect.Repository.CompanyRepository;
import com.chris.uniconnect.Service.ICompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl implements ICompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public List<CompanyDto> getAllCompanies() {
        return CompanyMappers.INSTANCE.companyListToCompanyDtoList(companyRepository.findAll());
    }

    @Override
    public CompanyDto getCompanyById(int id) {
        return CompanyMappers.INSTANCE.companyToCompanyDto(companyRepository.findById(id).orElse(null));
    }

    @Override
    public CompanyDto createCompany(CompanyDto companyDto) {
        return CompanyMappers.INSTANCE.companyToCompanyDto(companyRepository.save(CompanyMappers.INSTANCE.companyDtoToCompany(companyDto)));
    }

    @Override
    public void deleteCompany(CompanyDto companyDto) {
        companyRepository.delete(CompanyMappers.INSTANCE.companyDtoToCompany(companyDto));
    }

    @Override
    public boolean existCompany(Integer id) {
        return companyRepository.existsById(id);
    }
}
