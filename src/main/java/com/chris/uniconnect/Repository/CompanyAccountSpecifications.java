package com.chris.uniconnect.Repository;

import org.springframework.data.jpa.domain.Specification;

//filtros comunes de las cuentas ligadas a un convenio (CompanyAdmin y Recruiter: idCorporatePartnership + userEntity)
public final class CompanyAccountSpecifications {

    private CompanyAccountSpecifications() {
    }

    public static <T> Specification<T> partnership(Long idPartnership) {
        return idPartnership == null ? null : (root, query, cb) -> cb.equal(root.get("idCorporatePartnership"), idPartnership);
    }

    //cuenta habilitada (isEnabled); null = todas
    public static <T> Specification<T> active(Boolean active) {
        return active == null ? null : (root, query, cb) -> cb.equal(root.get("userEntity").get("isEnabled"), active);
    }

    //nombre, apellido, usuario o correo, sin importar mayusculas
    public static <T> Specification<T> text(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }
        String pattern = "%" + text.trim().toLowerCase() + "%";
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(cb.coalesce(root.get("name"), "")), pattern),
                cb.like(cb.lower(cb.coalesce(root.get("lastName"), "")), pattern),
                cb.like(cb.lower(root.get("userEntity").get("username")), pattern),
                cb.like(cb.lower(root.get("userEntity").get("email")), pattern)
        );
    }
}
