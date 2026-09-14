package com.chris.uniconnect.Repository;

import com.chris.uniconnect.Model.Entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Integer> {

    List<Language> findByIdPerson(Integer idPerson);
}
