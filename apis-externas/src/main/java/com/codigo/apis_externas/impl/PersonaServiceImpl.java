package com.codigo.apis_externas.impl;

import com.codigo.apis_externas.aggregates.constants.Constants;
import com.codigo.apis_externas.aggregates.request.PersonaRequest;
import com.codigo.apis_externas.aggregates.response.BaseResponse;
import com.codigo.apis_externas.aggregates.response.ReniecResponse;
import com.codigo.apis_externas.client.ReniecClient;
import com.codigo.apis_externas.entity.PersonaEntity;
import com.codigo.apis_externas.repository.PersonaRepository;
import com.codigo.apis_externas.service.PersonaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.Optional;

@Service
public class PersonaServiceImpl implements PersonaService{
    private final PersonaRepository personaRepository;
    private final ReniecClient reniecClient;

    @Value("${token.api}")
    private String tokenapi;

    public PersonaServiceImpl(PersonaRepository personaRepository, ReniecClient reniecClient) {
        this.personaRepository = personaRepository;
        this.reniecClient = reniecClient;
    }

    @Override
    public BaseResponse crearPersona(PersonaRequest request) {
        try {
            PersonaEntity persona = getEntity(request);
            if(Objects.nonNull(persona)){
                return new BaseResponse(Constants.OK_DNI_CODE,Constants.OK_DNI_MESS, Optional.of(personaRepository.save(persona)));
            }
        }catch (Exception e){
            BaseResponse response = new BaseResponse(Constants.ERROR_DNI_CODE
                    ,Constants.ERROR_DNI_MESS + e.getMessage(), Optional.empty());
            return response;
        }

        return null;
    }

    @Override
    public BaseResponse listarPersonas() {
        return null;
    }

    @Override
    public BaseResponse buscarPersonaDni(String dni) {
        return null;
    }

    @Override
    public BaseResponse actualizarPersona(Long id, PersonaRequest personaRequest) {
        return null;
    }

    @Override
    public BaseResponse eliminarPersona(String dni) {
        return null;
    }

    private ReniecResponse executionReniec(String documento){
        String auth = "Bearer "+tokenapi;
        ReniecResponse response = reniecClient.getPersonaReniec(documento,auth);
        return response;
    }

    private PersonaEntity getEntity(PersonaRequest personaRequest){
        PersonaEntity personaEntity = new PersonaEntity();

        //Ejecutar la consulta;
        ReniecResponse response = executionReniec(personaRequest.getNumdoc());
        if (Objects.nonNull(response)){
            personaEntity.setNombres(response.getNombres());
            personaEntity.setApellidos(response.getApellidoPaterno()
                    +" "+response.getApellidoMaterno());
            personaEntity.setNumDoc(response.getNumeroDocumento());
            personaEntity.setTipoDoc(response.getTipoDocumento());
            personaEntity.setUsuaCrea(Constants.USU_CREA);
            personaEntity.setDateCrea(new Timestamp(System.currentTimeMillis()));
            return personaEntity;
        }
        return null;
    }
}
