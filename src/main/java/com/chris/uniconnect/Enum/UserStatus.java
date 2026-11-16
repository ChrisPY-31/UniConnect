package com.chris.uniconnect.Enum;

//estado visible de una cuenta; son excluyentes (deshabilitada tiene prioridad sobre bloqueada)
public enum UserStatus {
    ACTIVA,         //isEnabled = true  y accountNonLocked = true
    BLOQUEADA,      //isEnabled = true  y accountNonLocked = false
    DESHABILITADA   //isEnabled = false (sin importar accountNonLocked)
}
