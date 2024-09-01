package com.codigo.apis_externas.aggregates.response;
import lombok.*;

import java.util.Optional;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse {
    private Integer code;
    private String message;
    private Optional data;
}
