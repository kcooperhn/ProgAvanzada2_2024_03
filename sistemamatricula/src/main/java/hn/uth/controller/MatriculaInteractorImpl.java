package hn.uth.controller;

import hn.uth.data.AlumnosResponse;
import hn.uth.repository.DatabaseRepositoryImpl;
import hn.uth.views.matricula.MatriculaViewModel;

public class MatriculaInteractorImpl implements MatriculaInteractor {

	private DatabaseRepositoryImpl modelo;
	private MatriculaViewModel vista;
	
	public MatriculaInteractorImpl(MatriculaViewModel vista) {
		super();
		this.vista = vista;
		this.modelo = DatabaseRepositoryImpl.getInstance("https://apex.oracle.com", 10000L);
	}

	@Override
	public void consultarAlumnos() {
		try {
			AlumnosResponse respuesta = this.modelo.consultarAlumnos();
			if(respuesta == null || respuesta.getCount() == 0 || respuesta.getItems() == null) {
				this.vista.mostrarMensajeError("No hay alumnos disponibles");
			}else {
				this.vista.mostrarAlumnosEnComboBox(respuesta.getItems());
			}
		}catch(Exception error) {
			error.printStackTrace();
		}
	}

}
