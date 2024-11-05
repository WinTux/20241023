package com.wintux._3.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="estudiante",schema="pregrado")
public class Est {
	@Id
	private int matricula;
	private String nombre, apellido;
}
