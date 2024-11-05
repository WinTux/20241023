package com.wintux._3.Controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.wintux._3.Excepciones.EstudianteNoEncontradoException;
import com.wintux._3.Models.Est;
import com.wintux._3.Models.Estudiante;
import com.wintux._3.Services.EstudianteService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class EstudianteController {
	private static Map<String,Estudiante> estudiantes = new HashMap<>();
	@Autowired
	ObjectMapper objectMapper;
	@Autowired
	EstudianteService estServ;
	
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
		//return new ResponseEntity<>(estudiantes.values(),HttpStatus.OK); // 200
		return new ResponseEntity<>(estServ.lista(),HttpStatus.OK); // 200
	}
	@GetMapping("/estudiante/{ap}")
	public ResponseEntity<Object> getEestudiantePorApellido(@PathVariable("ap") String apellido){
		List<Est> ests = estServ.listaPorApellidos(apellido);
		return new ResponseEntity<>(ests,HttpStatus.OK); // 200
	}
	
	@GetMapping("/estudiante/status") // http://localhost:7001/estudiante/status [GET]
	public ResponseEntity<String> customHeaders(){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Modo-Universidad", "CRISIS");
		headers.add("Permiso-cifrado", "true");
		headers.add("Cantidad-Activos", "254");
		return new ResponseEntity<>("Respuesta con encabezados.",headers, HttpStatus.OK); // 200
	}
	@GetMapping("/estudiante/edad") // http://localhost:7001/estudiante/edad?nacimiento=2000 [GET]
	public ResponseEntity<String> age(@RequestParam("nacimiento") int nacimiento){
		if(estaEnElFuturo(nacimiento)) {
			return ResponseEntity.badRequest().body(String.format("El año de nacimiento no puede estar en el futuro (%d)", nacimiento));
		}
		LocalDate fechaActual = LocalDate.now();
		DateTimeFormatter formateador = DateTimeFormatter.ofPattern("dd/MM/yyyy"); // 01/01/2024
		return ResponseEntity.status(HttpStatus.OK).header("Anyo-nacimiento", nacimiento+"").header("Fecha-de-servidor", fechaActual.format(formateador)).body(String.format("La edad es de %d años", calcularEdad(nacimiento)));
	}
	
	private Object calcularEdad(int nacimiento) {
		return java.time.Year.now().getValue() - nacimiento;
	}

	private boolean estaEnElFuturo(int nacimiento) {
		return nacimiento > java.time.Year.now().getValue();
	}
	
	@GetMapping("/estudiante/pruebacruda") // http://localhost:7001/estudiante/pruebacruda [GET]
	public void ejemploCrudo(HttpServletResponse response) throws IOException{
		response.setHeader("Codigo-depuracion", "XZ-400");
		response.setStatus(200);
		response.getWriter().println("Éxito");
	}
	
	@PostMapping("/estudiante") // http://localhost:7001/estudiante [POST]
	public ResponseEntity<Object> nuevoEstudiantes(@RequestBody Est est){
		//estudiantes.put(est.getId()+"", est);
		estServ.registrar(est);
		
		//return new ResponseEntity<>("Se creó un nuevo estudiante",HttpStatus.CREATED); // 201
		// <--- http://localhost:7001/estudiante/7 (location)
		URI ubicacionRecurso = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(est.getMatricula()).toUri();
		return ResponseEntity.created(ubicacionRecurso).build(); // 201
	}
	
	@PutMapping("/estudiante/{id}") // http://localhost:7001/estudiante/{id} [PUT]
	public ResponseEntity<Object> modificarEstudiante(@PathVariable("id") String id, @RequestBody Est est){
		Optional<Est> estudiante = estServ.hallarEstudiante(Integer.parseInt(id));
		if(!estudiante.isPresent())
			throw new EstudianteNoEncontradoException();
		Est estDDBB = estudiante.get();
		estDDBB.setNombre(est.getNombre());
		estDDBB.setApellido(est.getApellido());
		estDDBB.setFechanacimiento(est.getFechanacimiento());
		estDDBB.setCarreraid(est.getCarreraid());
		estDDBB.setPassword(est.getPassword());
		estDDBB.setEmail(est.getEmail());
		estDDBB.setEstado(est.getEstado());
		estServ.registrar(estDDBB);
		return new ResponseEntity<>("Se modificaron atributos del estudiante "+id,HttpStatus.OK); // 200 
		/*if(!estudiantes.containsKey(id))
			throw new EstudianteNoEncontradoException();
		estudiantes.remove(id);
		est.setId(Integer.parseInt(id));
		estudiantes.put(id, est);
		return new ResponseEntity<>("Se modificaron atributos del estudiante "+id,HttpStatus.OK); // 200 */
		
	}
	@DeleteMapping("/estudiante/{id}") // http://localhost:7001/estudiante/{id} [DELETE]
	public ResponseEntity<Object> eliminarEstudiante(@PathVariable("id") String id){
		Optional<Est> estudiante = estServ.hallarEstudiante(Integer.parseInt(id));
		if(!estudiante.isPresent())
			throw new EstudianteNoEncontradoException();
		Est estDDBB = estudiante.get();
		estServ.eliminar(estDDBB);
		return new ResponseEntity<>("Se eliminó al estudiante "+id,HttpStatus.OK); // 200
		/*
		estudiantes.remove(id);
		return new ResponseEntity<>("Se eliminó al estudiante "+id,HttpStatus.OK); // 200
		*/
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
	@ResponseBody
	@PostMapping(value="/estudiante/get", produces=MediaType.APPLICATION_JSON_VALUE) // http://localhost:7001/estudiante/get [GET]
	public Estudiante retornarUnEstudiante() {
		return estudiantes.get("1");
	}
	@PostMapping(value="/estudiante/subida", consumes=MediaType.MULTIPART_FORM_DATA_VALUE) // http://localhost:7001/estudiante/subida [POST]
	public ResponseEntity<String> archivoSubir(@RequestParam("archivito") MultipartFile arch) throws IOException{
		File elArchivo = new File("/home/rusok/Descargas/"+arch.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(elArchivo);
		fos.write(arch.getBytes());
		return ResponseEntity.ok("El archivo fue subido con éxito.");
	}
	
	@GetMapping(path="/estudiante/bajada") // http://localhost:7001/estudiante/bajada [GET]
	public ResponseEntity<Object> descargarArchivo() throws FileNotFoundException{
		String nombreArch = "/home/rusok/Documentos/Redes I.txt";
		File archivo = new File(nombreArch);
		InputStreamResource recurso = new InputStreamResource(
				new FileInputStream(archivo)
			);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Cache-Control", "no-cache,no-store,must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");
		ResponseEntity<Object> re = ResponseEntity.ok()
				.headers(headers)
				.contentLength(archivo.length())
				.contentType(MediaType.parseMediaType("application/txt"))
				.body(recurso);
		return re;
	}
}
