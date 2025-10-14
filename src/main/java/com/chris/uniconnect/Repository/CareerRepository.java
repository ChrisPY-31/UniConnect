package com.chris.uniconnect.Repository;

import com.chris.uniconnect.Model.Entity.Career;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CareerRepository extends ListCrudRepository<Career, Integer> {
}
