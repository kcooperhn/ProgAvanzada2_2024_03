package hn.uth.controller;

import hn.uth.data.Alumno;

public interface AlumnosInteractor {

	void consultarAlumnos();
	void crearAlumno(Alumno nuevo);
	void actualizarAlumno(Alumno existente);
}
