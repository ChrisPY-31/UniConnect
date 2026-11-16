package com.chris.uniconnect.Repository;

import com.chris.uniconnect.Enum.AplicationsStatus;
import com.chris.uniconnect.Model.Entity.JobApplication;
import org.springframework.data.jpa.domain.Specification;

//filtros de las postulaciones que ve el reclutador (por vacante) y la empresa (todas las de su convenio)
public final class JobApplicationSpecifications {

    private JobApplicationSpecifications() {
    }

    public static Specification<JobApplication> postJob(Long idPostJob) {
        return idPostJob == null ? null : (root, query, cb) -> cb.equal(root.get("idPostJob"), idPostJob);
    }

    public static Specification<JobApplication> partnership(Long idPartnership) {
        return idPartnership == null ? null : (root, query, cb) -> cb.equal(root.get("postJob").get("idCorporatePartnership"), idPartnership);
    }

    public static Specification<JobApplication> status(AplicationsStatus status) {
        return status == null ? null : (root, query, cb) -> cb.equal(root.get("aplicationStatus"), status);
    }

    //nombre o apellido del alumno, su carrera o el titulo de la vacante, sin importar mayusculas
    public static Specification<JobApplication> text(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }
        String pattern = "%" + text.trim().toLowerCase() + "%";
        return (root, query, cb) -> {
            var student = root.join("student");
            var career = student.join("career", jakarta.persistence.criteria.JoinType.LEFT);
            return cb.or(
                    cb.like(cb.lower(cb.coalesce(student.get("name"), "")), pattern),
                    cb.like(cb.lower(cb.coalesce(student.get("lastName"), "")), pattern),
                    cb.like(cb.lower(cb.coalesce(career.get("careerName"), "")), pattern),
                    cb.like(cb.lower(cb.coalesce(root.get("postJob").get("title"), "")), pattern)
            );
        };
    }
}
