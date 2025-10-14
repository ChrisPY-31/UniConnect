package com.chris.uniconnect.Exeptions;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Mensaje {
    String mensaje;
    Object object;
}
