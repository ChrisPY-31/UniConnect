package com.chris.uniconnect.Mappers;

import com.chris.uniconnect.Enum.SkillType;
import com.chris.uniconnect.Model.Dto.Response.SkillResponse;
import com.chris.uniconnect.Model.Entity.Aptitude;
import com.chris.uniconnect.Model.Entity.Person;
import com.chris.uniconnect.Model.Entity.Technology;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Mapper
public interface SkillMappers {

    SkillMappers INSTANCE = Mappers.getMapper(SkillMappers.class);

    @Mapping(source = "idTechnology", target = "id")
    @Mapping(source = "name", target = "nombre")
    @Mapping(target = "tipo", constant = "TECNOLOGIA")
    SkillResponse technologyToSkill(Technology technology);

    @Mapping(source = "idAptitude", target = "id")
    @Mapping(source = "name", target = "nombre")
    @Mapping(target = "tipo", constant = "APTITUD")
    SkillResponse aptitudeToSkill(Aptitude aptitude);

    // Habilidades = tecnologias + aptitudes, tecnologias primero y cada grupo en orden alfabetico.
    default List<SkillResponse> combine(Collection<Technology> technologies, Collection<Aptitude> aptitudes) {
        Comparator<SkillResponse> byName = Comparator.comparing(SkillResponse::getNombre, String.CASE_INSENSITIVE_ORDER);
        List<SkillResponse> habilidades = new ArrayList<>();
        if (technologies != null) {
            technologies.stream().map(this::technologyToSkill).sorted(byName).forEach(habilidades::add);
        }
        if (aptitudes != null) {
            aptitudes.stream().map(this::aptitudeToSkill).sorted(byName).forEach(habilidades::add);
        }
        return habilidades;
    }

    default List<SkillResponse> personToSkills(Person person) {
        if (person == null) {
            return null;
        }
        return combine(person.getTechnologies(), person.getAptitudes());
    }
}
