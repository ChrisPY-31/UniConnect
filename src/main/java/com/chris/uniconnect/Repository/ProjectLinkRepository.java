package com.chris.uniconnect.Repository;

import com.chris.uniconnect.Model.Entity.ProjectLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectLinkRepository extends JpaRepository<ProjectLink, Integer> {

}
