package com.chris.uniconnect.Enum;

//ENVIADA -> EN_REVISION -> ENTREVISTA -> ACEPTADA; RECHAZADA (reclutador) y RETIRADA (estudiante) desde cualquier estado abierto
public enum AplicationsStatus {
    ENVIADA,
    EN_REVISION,
    ENTREVISTA,
    ACEPTADA,
    RECHAZADA,
    RETIRADA;

    public boolean isFinal() {
        return this == ACEPTADA || this == RECHAZADA || this == RETIRADA;
    }
}
