package com.chris.uniconnect.payload;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MensajeResponse {
    String mensaje;
    Object object;
}
