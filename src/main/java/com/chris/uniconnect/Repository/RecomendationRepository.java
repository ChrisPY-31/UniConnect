package com.chris.uniconnect.Repository;

import com.chris.uniconnect.Model.Entity.Recomendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecomendationRepository extends JpaRepository<Recomendation , Integer> {
}
