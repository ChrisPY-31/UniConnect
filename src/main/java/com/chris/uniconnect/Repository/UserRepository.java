package com.chris.uniconnect.Repository;

import com.chris.uniconnect.Model.Entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Integer> {

    Optional<UserEntity> findUserByUsername(String username);
}
