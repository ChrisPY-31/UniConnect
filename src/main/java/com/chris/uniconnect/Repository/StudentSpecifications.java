package com.chris.uniconnect.Repository;

import com.chris.uniconnect.Model.Entity.Student;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;

//filtros de GET /students; todos opcionales (null = no filtra)
public final class StudentSpecifications {

    //translate() de PostgreSQL: quita acentos sin necesitar la extension unaccent
    private static final String WITH_ACCENTS = "áéíóúàèìòùäëïöüâêîôûñ";
    private static final String WITHOUT_ACCENTS = "aeiouaeiouaeiouaeioun";

    private StudentSpecifications() {
    }

    private static String normalize(String text) {
        return Normalizer.normalize(text, Normalizer.Form.NFD).replaceAll("\\p{M}", "").toLowerCase().trim();
    }

    private static Expression<String> plain(CriteriaBuilder cb, Expression<String> field) {
        return cb.function("translate", String.class, cb.lower(cb.coalesce(field, "")), cb.literal(WITH_ACCENTS), cb.literal(WITHOUT_ACCENTS));
    }

    //cada palabra debe aparecer en el nombre o el apellido, sin importar mayusculas ni acentos ("pena chris" encuentra a "Christian Peña")
    public static Specification<Student> text(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }
        List<String> words = Arrays.stream(normalize(text).split("\\s+")).filter(w -> !w.isBlank()).toList();
        return (root, query, cb) -> cb.and(words.stream()
                .map(word -> {
                    String pattern = "%" + word + "%";
                    return cb.or(cb.like(plain(cb, root.get("name")), pattern), cb.like(plain(cb, root.get("lastName")), pattern));
                })
                .toArray(Predicate[]::new));
    }

    public static Specification<Student> career(Integer idCareer) {
        return idCareer == null ? null : (root, query, cb) -> cb.equal(root.get("idCareer"), idCareer);
    }

    //semester se guarda como texto ("1".."10")
    public static Specification<Student> semester(Integer semester) {
        return semester == null ? null : (root, query, cb) -> cb.equal(cb.trim(root.get("semester")), String.valueOf(semester));
    }

    public static Specification<Student> ubication(Integer idUbication) {
        return idUbication == null ? null : (root, query, cb) -> cb.equal(root.get("idUbication"), idUbication);
    }

    //el alumno debe tener TODAS las tecnologias; un EXISTS por tecnologia para no duplicar filas
    public static Specification<Student> allTechnologies(List<Integer> idTechnologies) {
        if (idTechnologies == null || idTechnologies.isEmpty()) {
            return null;
        }
        return (root, query, cb) -> cb.and(idTechnologies.stream().distinct()
                .map(idTechnology -> {
                    Subquery<Integer> subquery = query.subquery(Integer.class);
                    Root<Student> other = subquery.from(Student.class);
                    subquery.select(other.get("id")).where(
                            cb.equal(other.get("id"), root.get("id")),
                            cb.equal(other.join("technologies").get("idTechnology"), idTechnology)
                    );
                    return cb.exists(subquery);
                })
                .toArray(Predicate[]::new));
    }

    public static Specification<Student> withProjects(Boolean withProjects) {
        return !Boolean.TRUE.equals(withProjects) ? null : (root, query, cb) -> cb.isNotEmpty(root.get("projects"));
    }

    public static Specification<Student> recommended(Boolean recommended) {
        return !Boolean.TRUE.equals(recommended) ? null : (root, query, cb) -> cb.isNotEmpty(root.get("recomendations"));
    }

    //orden "recomendaciones": no se puede expresar con Sort, se pone en la consulta (Spring lo quita en el count)
    public static Specification<Student> orderByRecommendations() {
        return (root, query, cb) -> {
            query.orderBy(cb.desc(cb.size(root.get("recomendations"))), cb.desc(root.get("id")));
            return null;
        };
    }
}
