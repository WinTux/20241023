package com.wintux._3.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.wintux._3.Models.Est;
@Repository
public interface EstudianteRepository extends JpaRepository<Est,Integer>{
	List<Est> findByApellido(String apellido); // select * from estudiante where apellido = '?';
	List<Est> findByApellidoAndNombre(String a, String n); // select * from estudiante where apellido = '?' and nombre = '?';
}
