package com.chris.uniconnect.Repository;

import com.chris.uniconnect.Model.Entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepostory extends JpaRepository<Teacher , Integer> {

    Teacher findByUserEntityUsername(String username);
}
