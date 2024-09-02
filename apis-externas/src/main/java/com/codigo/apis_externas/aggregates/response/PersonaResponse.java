package com.codigo.apis_externas.aggregates.response;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Optional;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonaResponse {
    @Schema(description = "Codigo de Respuesta")
    private Integer code;
    @Schema(description = "Mensaje de Respuesta")
    private String message;
    @Schema(description = "Objeto Final de Respuesta")
    private Optional data;
}
