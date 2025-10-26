package com.chris.uniconnect.Repository;

import com.chris.uniconnect.Model.Entity.RolesEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends CrudRepository<RolesEntity , Integer> {

    List<RolesEntity> findByRoleEnumIn(List<String> roleNames);

}
