package com.chris.uniconnect.Model.Entity;

import com.chris.uniconnect.Enum.CareerType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(name = "id_student")
    private Integer idStudent;

    @Enumerated(EnumType.STRING)
    private CareerType career;

    @OneToOne
    @JoinColumn(name = "id_student" , referencedColumnName = "id" , insertable = false, updatable = false)
    private Student student;

}
