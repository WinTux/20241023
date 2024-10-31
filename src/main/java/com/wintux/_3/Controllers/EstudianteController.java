package com.wintux._3.Controllers;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.wintux._3.Models.Estudiante;

@Controller
public class EstudianteController {
	private static Map<String,Estudiante> estudiantes = new HashMap<>();
	@Autowired
	ObjectMapper objectMapper;
	
	static {
		Estudiante e1 = new Estudiante(1, "Pepe","Perales");
		Estudiante e2 = new Estudiante(2, "Ana","Sosa");
		Estudiante e3 = new Estudiante(3, "Sofía","Rocha");
		estudiantes.put("1",e1);
		estudiantes.put("2",e2);
		estudiantes.put("3",e3);
	}
	
	@GetMapping("/estudiante") // http://localhost:7001/estudiante [GET]
	public ResponseEntity<Object> getEstudiantes(){
		return new ResponseEntity<>(estudiantes.values(),HttpStatus.OK); // 200
	}
	@PostMapping("/estudiante") // http://localhost:7001/estudiante [POST]
	public ResponseEntity<Object> nuevoEstudiantes(@RequestBody Estudiante est){
		estudiantes.put(est.getId()+"", est);
		//return new ResponseEntity<>("Se creó un nuevo estudiante",HttpStatus.CREATED); // 201
		// <--- http://localhost:7001/estudiante/7 (location)
		URI ubicacionRecurso = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(est.getId()).toUri();
		return ResponseEntity.created(ubicacionRecurso).build(); // 201
	}
	
	@PutMapping("/estudiante/{id}") // http://localhost:7001/estudiante/{id} [PUT]
	public ResponseEntity<Object> modificarEstudiante(@PathVariable("id") String id, @RequestBody Estudiante est){
		estudiantes.remove(id);
		est.setId(Integer.parseInt(id));
		estudiantes.put(id, est);
		return new ResponseEntity<>("Se modificaron atributos del estudiante "+id,HttpStatus.OK); // 200
	}
	@DeleteMapping("/estudiante/{id}") // http://localhost:7001/estudiante/{id} [DELETE]
	public ResponseEntity<Object> eliminarEstudiante(@PathVariable("id") String id){
		estudiantes.remove(id);
		return new ResponseEntity<>("Se eliminó al estudiante "+id,HttpStatus.OK); // 200
	}
	
	@PatchMapping("/estudiante/{id}")
	public ResponseEntity<String> editarPorPatch(@PathVariable("id") String id, @RequestBody Map<String,Object> atributosModificados){
		Estudiante estOriginal = estudiantes.get(id);
		atributosModificados.forEach((atributo,valorNuevo)->{
			Field campo = ReflectionUtils.findField(Estudiante.class,atributo);
			if(campo != null) {
				campo.setAccessible(true);
				ReflectionUtils.setField(campo, estOriginal, valorNuevo);
			}
		});
		estudiantes.remove(id);
		estudiantes.put(id, estOriginal);
		return new ResponseEntity<>("[PATCH] Se modificaron atributos del estudiante "+id,HttpStatus.OK); // 200
	}
	
	@PatchMapping(path="/estudiante/{id}", consumes="application/json-patch+json")
	public ResponseEntity<String> editarConJsonPatch(@PathVariable("id") String id,@RequestBody JsonPatch atributosModificados){
		try {
			Estudiante estOriginal = estudiantes.get(id);
			JsonNode patcheado = atributosModificados.apply(objectMapper.convertValue(estOriginal, JsonNode.class));
			Estudiante estudianteActualizado = objectMapper.treeToValue(patcheado, Estudiante.class);
			estudiantes.remove(id);
			estudiantes.put(id, estudianteActualizado);
			return new ResponseEntity<>("[JSON PATCH] Se modificaron atributos del estudiante "+id,HttpStatus.OK); // 200
		}catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("[JSON PATCH] ocurrió un error al intentar modificar al estudiante "+id,HttpStatus.INTERNAL_SERVER_ERROR); // 500
		}
	}
}
