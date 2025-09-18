package com.chris.uniconnect.Model.Entity;

import com.chris.uniconnect.Enum.CareerType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "career")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Career {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_career")
    private Integer idCareer;

    @Column(name = "career_name")
    private String careerName;

    @OneToMany(mappedBy = "career")
    private List<Student> students;

}
