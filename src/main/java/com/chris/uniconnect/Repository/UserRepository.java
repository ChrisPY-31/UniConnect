package com.chris.uniconnect.Repository;


import com.chris.uniconnect.Model.Entity.UserEntity;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends ListCrudRepository<UserEntity, Integer> {

    Optional<UserEntity> findUserByUsername(String username);

    boolean existsByEmail(String email);

    Optional<UserEntity> findByPersonId(Integer personId);


}
