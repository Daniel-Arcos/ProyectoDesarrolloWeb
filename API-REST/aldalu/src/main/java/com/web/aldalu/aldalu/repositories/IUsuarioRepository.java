package com.web.aldalu.aldalu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.aldalu.aldalu.models.entities.Usuario;

public interface IUsuarioRepository extends JpaRepository<Usuario, Long>{

}
