package com.chris.uniconnect.Repository;

import com.chris.uniconnect.Model.Entity.Person;
import com.chris.uniconnect.Model.Entity.RolesEntity;
import com.chris.uniconnect.Model.Entity.UserEntity;
import org.apache.catalina.User;
import org.mapstruct.control.MappingControl;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Integer> {

    Optional<UserEntity> findUserByUsername(String username);

    boolean existsByEmail(String email);


}
