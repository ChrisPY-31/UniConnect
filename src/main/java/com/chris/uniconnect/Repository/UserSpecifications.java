package com.chris.uniconnect.Repository;

import com.chris.uniconnect.Enum.Roles;
import com.chris.uniconnect.Enum.UserStatus;
import com.chris.uniconnect.Model.Entity.UserEntity;
import org.springframework.data.jpa.domain.Specification;

public final class UserSpecifications {

    private UserSpecifications() {
    }

    //username o correo, sin importar mayusculas
    public static Specification<UserEntity> text(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }
        String pattern = "%" + text.trim().toLowerCase() + "%";
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("username")), pattern),
                cb.like(cb.lower(root.get("email")), pattern)
        );
    }

    public static Specification<UserEntity> role(Roles role) {
        if (role == null) {
            return null;
        }
        return (root, query, cb) -> {
            //el join a roles puede duplicar filas; distinct mantiene bien el totalElements
            query.distinct(true);
            return cb.equal(root.join("roles").get("roleEnum"), role);
        };
    }

    public static Specification<UserEntity> status(UserStatus status) {
        if (status == null) {
            return null;
        }
        return (root, query, cb) -> switch (status) {
            case ACTIVA -> cb.and(cb.isTrue(root.get("isEnabled")), cb.isTrue(root.get("accountNonLocked")));
            case BLOQUEADA -> cb.and(cb.isTrue(root.get("isEnabled")), cb.isFalse(root.get("accountNonLocked")));
            case DESHABILITADA -> cb.isFalse(root.get("isEnabled"));
        };
    }
}
