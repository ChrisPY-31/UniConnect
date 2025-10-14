package com.chris.uniconnect.Repository;

import com.chris.uniconnect.Model.Entity.Aptitude;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AptitudeRepository extends ListCrudRepository<Aptitude , Integer> {
}
