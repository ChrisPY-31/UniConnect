package com.chris.uniconnect.Model.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

//pagina publica de la institucion: una sola fila compartida por todos los ADMIN,
//que publican en el feed con este nombre y logo
@Entity
@Table(name = "institution_profile")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InstitutionProfile {

    public static final String DEFAULT_NAME = "UniConnect";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_institution_profile")
    private Integer idInstitutionProfile;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "banner_url")
    private String bannerUrl;

    private String website;

    private String location;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
