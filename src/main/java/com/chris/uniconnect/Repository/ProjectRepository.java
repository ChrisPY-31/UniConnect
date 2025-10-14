package com.chris.uniconnect.Repository;

import com.chris.uniconnect.Model.Entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project , Integer> {
}
