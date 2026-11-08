package com.chris.uniconnect.Repository;

import com.chris.uniconnect.Enum.Roles;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;


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

    @Query("select u from UserEntity u join u.roles r where r.roleEnum = :role and u.person is null")
    List<UserEntity> findWithoutPersonByRole(@Param("role") Roles role);


}
