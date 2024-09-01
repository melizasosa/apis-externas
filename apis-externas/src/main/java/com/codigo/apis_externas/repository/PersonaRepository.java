package com.codigo.apis_externas.repository;
import com.codigo.apis_externas.entity.PersonaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonaRepository extends JpaRepository<PersonaEntity, Long>{
}
