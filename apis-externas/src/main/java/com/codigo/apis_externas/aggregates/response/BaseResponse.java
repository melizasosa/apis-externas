package com.codigo.apis_externas.aggregates.response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Optional;
@Getter
@Setter
@AllArgsConstructor
public class BaseResponse {
    private Integer code;
    private String message;
    private Optional data;
}
