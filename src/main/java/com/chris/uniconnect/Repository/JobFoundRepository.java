package com.chris.uniconnect.Repository;

import com.chris.uniconnect.Model.Entity.JobFound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobFoundRepository extends JpaRepository<JobFound , Integer> {
}
