package com.codigo.apis_externas.service;
import com.codigo.apis_externas.aggregates.request.PersonaRequest;
import com.codigo.apis_externas.aggregates.response.BaseResponse;
public interface PersonaService {
    BaseResponse crearPersona(PersonaRequest request);
    BaseResponse listarPersonas();
    BaseResponse buscarPersonaDni(String dni);
    BaseResponse actualizarPersona(Long id, PersonaRequest personaRequest);
    BaseResponse eliminarPersona(String dni);
}
