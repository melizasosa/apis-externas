package com.codigo.apis_externas.service.impl;

import com.codigo.apis_externas.aggregates.constants.Constants;
import com.codigo.apis_externas.aggregates.request.PersonaRequest;
import com.codigo.apis_externas.aggregates.response.BaseResponse;
import com.codigo.apis_externas.aggregates.response.ReniecResponse;
import com.codigo.apis_externas.client.ReniecClient;
import com.codigo.apis_externas.entity.PersonaEntity;
import com.codigo.apis_externas.repository.PersonaRepository;
import com.codigo.apis_externas.retrofit.ReniecService;
import com.codigo.apis_externas.retrofit.impl.ReniecClientImpl;
import com.codigo.apis_externas.service.PersonaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class PersonaServiceImpl  implements PersonaService {
    private final PersonaRepository personaRepository;
    private final ReniecClient reniecClient;
    private final RestTemplate restTemplate;
    ReniecService reniecServiceRetrofit = ReniecClientImpl.getClient().create(ReniecService.class);
    @Value("${token.api}")
    private String tokenapi;


    @Override
    public BaseResponse crearPersona(PersonaRequest request) {
        try {
            PersonaEntity persona = getEntityRestTemplate(request);
            if(Objects.nonNull(persona)){
                return new BaseResponse(Constants.OK_DNI_CODE,Constants.OK_DNI_MESS,
                        Optional.of(personaRepository.save(persona)));
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
        Optional<List<PersonaEntity>> personaEntityList = personaRepository.findByEstado(Constants.STATUS_ACTIVE);
        if(Objects.nonNull(personaEntityList)){
            return new BaseResponse(Constants.OK_DNI_CODE,Constants.OK_DNI_MESS,
                    personaEntityList);
        }else {
            BaseResponse response = new BaseResponse(Constants.ERROR_CODE_LIST_EMPTY
                    ,Constants.ERROR_MESS_LIST_EMPTY, Optional.empty());
            return response;
        }
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
        BaseResponse response = new BaseResponse();
        PersonaEntity personaRecuperada = personaRepository.findByNumDoc(dni).orElseThrow(
                ()->new RuntimeException("ERROR NO EXISTE LA PERSONA QUE QUEIRE ELIMINAR"));
        if(Objects.nonNull(personaRecuperada)){
            personaRecuperada.setEstado(Constants.STATUS_INACTIVE);
            //Datos de la auditoria de eliminación
            personaRecuperada.setUsuaDele(Constants.USU_CREA);
            personaRecuperada.setDateDele(new Timestamp(System.currentTimeMillis()));
            response.setCode(Constants.OK_DNI_CODE);
            response.setMessage(Constants.OK_DNI_MESS);
            response.setData(Optional.of(personaRepository.save(personaRecuperada)));
            return response;
        }else {
            response.setCode(Constants.ERROR_DNI_CODE);
            response.setMessage(Constants.ERROR_DNI_MESS);
            response.setData(Optional.of(personaRepository.save(personaRecuperada)));
            return response;
        }
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

    private PersonaEntity getEntityRetrofit(PersonaRequest personaRequest) throws IOException {
        PersonaEntity personaEntity = new PersonaEntity();

        //Preparación de cliente usando RETROFIT;
        Call<ReniecResponse> callReniec = reniecServiceRetrofit.getPersonaReniec("Bearer "+tokenapi,
                personaRequest.getNumdoc());
        //Ejecutando consulta:
        Response<ReniecResponse> execute = callReniec.execute();
        //validamos el estado de la ejecuíon y si tenemos datos en el body
        if (execute.isSuccessful() && Objects.nonNull(execute.body())){
            //extraemos los datos de la ejecución llevandolos a una clase en la cual los hemos recibido.
            ReniecResponse response = execute.body();

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

    private PersonaEntity getEntityRestTemplate(PersonaRequest personaRequest){
        PersonaEntity personaEntity = new PersonaEntity();
        String url = "https://api.apis.net.pe/v2/reniec/dni?numero="+personaRequest.getNumdoc();
        ResponseEntity<ReniecResponse> executeTemplate = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(createHeaders()),
                ReniecResponse.class
        );
        if(executeTemplate.getStatusCode().equals(HttpStatus.OK)){
            ReniecResponse response = executeTemplate.getBody();
            personaEntity.setNombres(response.getNombres());
            personaEntity.setApellidos(response.getApellidoPaterno()
                    +" "+response.getApellidoMaterno());
            personaEntity.setNumDoc(response.getNumeroDocumento());
            personaEntity.setTipoDoc(response.getTipoDocumento());
            personaEntity.setEstado(Constants.STATUS_ACTIVE);
            personaEntity.setUsuaCrea(Constants.USU_CREA);
            personaEntity.setDateCrea(new Timestamp(System.currentTimeMillis()));
            return personaEntity;
        }
        return null;
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","Bearer "+tokenapi);
        return headers;
    }
}
