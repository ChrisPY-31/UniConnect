package com.chris.uniconnect.Repository;

import com.chris.uniconnect.Enum.TypeApplication;
import com.chris.uniconnect.Enum.VacanteStatus;
import com.chris.uniconnect.Enum.WorkModality;
import com.chris.uniconnect.Enum.WorkingDay;
import com.chris.uniconnect.Model.Entity.CorporatePartnershipsUAEMex;
import com.chris.uniconnect.Model.Entity.PostJob;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

//filtros de la busqueda publica de vacantes
public final class PostJobSpecifications {

    private PostJobSpecifications() {
    }

    //lo que puede ver cualquiera: publicada, no vencida y de un convenio vigente
    public static Specification<PostJob> visible() {
        return (root, query, cb) -> {
            LocalDate today = LocalDate.now();
            Join<PostJob, CorporatePartnershipsUAEMex> partnership = root.join("corporatePartnershipsUAEMex");
            return cb.and(
                    cb.equal(root.get("status"), VacanteStatus.PUBLICADA),
                    cb.or(cb.isNull(root.get("closingDate")), cb.greaterThanOrEqualTo(root.get("closingDate"), today)),
                    cb.isTrue(partnership.get("active")),
                    cb.lessThanOrEqualTo(partnership.get("startDate"), today),
                    cb.or(cb.isNull(partnership.get("endDate")), cb.greaterThanOrEqualTo(partnership.get("endDate"), today))
            );
        };
    }

    public static Specification<PostJob> hasId(Long idPostJob) {
        return (root, query, cb) -> cb.equal(root.get("idPostJob"), idPostJob);
    }

    public static Specification<PostJob> text(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }
        String pattern = "%" + text.trim().toLowerCase() + "%";
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("title")), pattern),
                cb.like(cb.lower(root.get("description")), pattern)
        );
    }

    public static Specification<PostJob> typeApplication(TypeApplication type) {
        return type == null ? null : (root, query, cb) -> cb.equal(root.get("typeApplication"), type);
    }

    public static Specification<PostJob> workDay(WorkingDay workDay) {
        return workDay == null ? null : (root, query, cb) -> cb.equal(root.get("workDay"), workDay);
    }

    public static Specification<PostJob> modality(WorkModality modality) {
        return modality == null ? null : (root, query, cb) -> cb.equal(root.get("modality"), modality);
    }

    public static Specification<PostJob> partnership(Long idPartnership) {
        return idPartnership == null ? null : (root, query, cb) -> cb.equal(root.get("idCorporatePartnership"), idPartnership);
    }

    public static Specification<PostJob> career(Integer idCareer) {
        if (idCareer == null) {
            return null;
        }
        return (root, query, cb) -> {
            query.distinct(true);
            return cb.equal(root.join("careers").get("idCareer"), idCareer);
        };
    }

    public static Specification<PostJob> technology(Integer idTechnology) {
        if (idTechnology == null) {
            return null;
        }
        return (root, query, cb) -> {
            query.distinct(true);
            return cb.equal(root.join("technologies").get("idTechnology"), idTechnology);
        };
    }

    //listas del reclutador y de la empresa: titulo, descripcion o ubicacion, sin importar mayusculas
    public static Specification<PostJob> ownText(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }
        String pattern = "%" + text.trim().toLowerCase() + "%";
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("title")), pattern),
                cb.like(cb.lower(root.get("description")), pattern),
                cb.like(cb.lower(root.get("ubication")), pattern)
        );
    }

    //un borrador nunca tiene postulaciones: se excluye de los selectores de vacante
    public static Specification<PostJob> notDraft() {
        return (root, query, cb) -> cb.notEqual(root.get("status"), VacanteStatus.BORRADOR);
    }

    public static Specification<PostJob> recruiter(Integer idRecruiter) {
        return idRecruiter == null ? null : (root, query, cb) -> cb.equal(root.get("idRecruiter"), idRecruiter);
    }

    //mismo criterio que PostJob.getEffectiveStatus(): una PUBLICADA con fecha de cierre pasada cuenta como VENCIDA
    public static Specification<PostJob> effectiveStatus(VacanteStatus status) {
        if (status == null) {
            return null;
        }
        return (root, query, cb) -> {
            LocalDate today = LocalDate.now();
            var expired = cb.and(
                    cb.equal(root.get("status"), VacanteStatus.PUBLICADA),
                    cb.isNotNull(root.get("closingDate")),
                    cb.lessThan(root.get("closingDate"), today)
            );
            return switch (status) {
                case VENCIDA -> cb.or(cb.equal(root.get("status"), VacanteStatus.VENCIDA), expired);
                case PUBLICADA -> cb.and(cb.equal(root.get("status"), VacanteStatus.PUBLICADA), cb.not(expired));
                default -> cb.equal(root.get("status"), status);
            };
        };
    }
}
