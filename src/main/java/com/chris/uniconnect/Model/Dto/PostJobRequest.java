package com.chris.uniconnect.Model.Dto;

import com.chris.uniconnect.Enum.TypeApplication;
import com.chris.uniconnect.Enum.WorkModality;
import com.chris.uniconnect.Enum.WorkingDay;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Set;

//un borrador solo exige titulo; para publicar se exigen descripcion, jornada, modalidad y tipo (se valida en el servicio)
public record PostJobRequest(
        @NotBlank(message = "El titulo es obligatorio") @Size(max = 150, message = "El titulo no puede pasar de 150 caracteres") String titulo,
        String descripcion,
        String ubicacion,
        WorkingDay jornada,
        WorkModality modalidad,
        TypeApplication tipo,
        @PositiveOrZero(message = "El salario minimo no puede ser negativo") Integer salarioMin,
        @PositiveOrZero(message = "El salario maximo no puede ser negativo") Integer salarioMax,
        @FutureOrPresent(message = "La fecha de cierre no puede ser en el pasado") LocalDate fechaCierre,
        Set<Integer> idsCarreras,
        Set<Integer> idsTecnologias
) {
}
