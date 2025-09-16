package com.chris.uniconnect.Model.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class JobFoundPk implements Serializable {
    @Column(name = "id_student")
    private Integer idStudent;

    @Column(name = "id_recruiter")
    private Integer idRecruiter;

    @Column(name = "id_company")
    private Integer idCompany;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JobFoundPk)) return false;
        JobFoundPk that = (JobFoundPk) o;
        return Objects.equals(idStudent, that.idStudent) &&
                Objects.equals(idRecruiter, that.idRecruiter) &&
                Objects.equals(idCompany, that.idCompany);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idStudent, idRecruiter, idCompany);
    }
}
