package com.codigo.apis_externas.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "personas")
@Getter
@Setter
public class PersonaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String numDoc;
    private String tipoDoc;
    private String nombres;
    private String apellidos;
    private Integer estado;
    private String usuaCrea;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp dateCrea;
    private String usuaUpda;
    private Timestamp dateUpda;
    private String usuaDele;
    private Timestamp dateDele;
}
