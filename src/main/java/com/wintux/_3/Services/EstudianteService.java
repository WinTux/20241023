package com.wintux._3.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wintux._3.Models.Est;
import com.wintux._3.Repositories.EstudianteRepository;
@Service
public class EstudianteService {
	@Autowired
	EstudianteRepository estudianteRepo;
	public List<Est> lista(){
		return estudianteRepo.findAll();// select * from estudiante;
	}
	public List<Est> listaPorApellidos(String apellido){
		return estudianteRepo.findByApellido(apellido);
	}
	public List<Est> listaPorApellidosYnombres(String apellido, String nombre){
		return estudianteRepo.findByApellidoAndNombre(apellido,nombre);
	}
	public void registrar(Est est) {
		System.out.println("ÇSe registrará al estudiante "+ est.getApellido());
		estudianteRepo.save(est);
	}
	public Optional<Est> hallarEstudiante(int matri){
		return estudianteRepo.findById(matri);
	}
	public void eliminar(Est est) {
		estudianteRepo.delete(est);
	}
}
