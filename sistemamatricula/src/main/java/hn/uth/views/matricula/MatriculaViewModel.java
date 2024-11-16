package hn.uth.views.matricula;

import java.util.List;

import hn.uth.data.Alumno;

public interface MatriculaViewModel {
	void mostrarAlumnosEnComboBox(List<Alumno> items);
	void mostrarMensajeError(String mensaje);
}
