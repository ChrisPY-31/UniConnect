package com.chris.uniconnect.Repository;

import com.chris.uniconnect.Model.Entity.Ubication;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UbicationRepository extends ListCrudRepository<Ubication , Integer> {
}
