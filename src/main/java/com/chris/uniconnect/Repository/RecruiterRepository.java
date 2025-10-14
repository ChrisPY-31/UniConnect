package com.chris.uniconnect.Repository;

import com.chris.uniconnect.Model.Entity.Recruiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruiterRepository extends JpaRepository<Recruiter , Integer> {
}
