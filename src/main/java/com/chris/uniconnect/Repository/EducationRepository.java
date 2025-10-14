package com.chris.uniconnect.Repository;

import com.chris.uniconnect.Model.Entity.Education;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EducationRepository extends JpaRepository<Education , Integer> {
}
