package com.chris.uniconnect.Repository;

import com.chris.uniconnect.Model.Entity.CompanyAdmin;
import com.chris.uniconnect.Model.Entity.CorporatePartnershipsUAEMex;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

//filtros de GET /partnerships y GET /companies; mismo criterio que CorporatePartnershipsUAEMex.isVigente()
public final class PartnershipSpecifications {

    private PartnershipSpecifications() {
    }

    //activo, ya iniciado y sin vencer
    private static Predicate vigentePredicate(Root<CorporatePartnershipsUAEMex> root, CriteriaBuilder cb) {
        LocalDate today = LocalDate.now();
        return cb.and(
                cb.isTrue(root.get("active")),
                cb.lessThanOrEqualTo(root.get("startDate"), today),
                cb.or(cb.isNull(root.get("endDate")), cb.greaterThanOrEqualTo(root.get("endDate"), today))
        );
    }

    //true: vigentes; false: el resto (inactivos, vencidos o por iniciar)
    public static Specification<CorporatePartnershipsUAEMex> vigente(Boolean vigente) {
        if (vigente == null) {
            return null;
        }
        return (root, query, cb) -> vigente ? vigentePredicate(root, cb) : cb.not(vigentePredicate(root, cb));
    }

    //vigentes cuya fecha de termino cae entre hoy y hoy + days
    public static Specification<CorporatePartnershipsUAEMex> expiringWithin(int days) {
        return (root, query, cb) -> {
            LocalDate today = LocalDate.now();
            return cb.and(vigentePredicate(root, cb), cb.between(root.get("endDate"), today, today.plusDays(days)));
        };
    }

    //nombre de la empresa, categoria o tipo de acuerdo, sin importar mayusculas
    public static Specification<CorporatePartnershipsUAEMex> text(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }
        String pattern = "%" + text.trim().toLowerCase() + "%";
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("companyName")), pattern),
                cb.like(cb.lower(cb.coalesce(root.get("category"), "")), pattern),
                cb.like(cb.lower(cb.coalesce(root.get("agreementType"), "")), pattern)
        );
    }

    public static Specification<CorporatePartnershipsUAEMex> active() {
        return (root, query, cb) -> cb.isTrue(root.get("active"));
    }

    //sin una cuenta de empresa habilitada (regla: a lo mas una activa por convenio)
    public static Specification<CorporatePartnershipsUAEMex> withoutActiveCompanyAdmin() {
        return (root, query, cb) -> {
            Subquery<Integer> subquery = query.subquery(Integer.class);
            Root<CompanyAdmin> admin = subquery.from(CompanyAdmin.class);
            subquery.select(admin.get("id")).where(
                    cb.equal(admin.get("idCorporatePartnership"), root.get("idCorporatePartnership")),
                    cb.isTrue(admin.get("userEntity").get("isEnabled"))
            );
            return cb.not(cb.exists(subquery));
        };
    }
}
