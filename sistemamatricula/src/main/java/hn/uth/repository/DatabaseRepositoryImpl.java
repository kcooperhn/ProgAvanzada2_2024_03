package hn.uth.repository;

import java.io.IOException;

import hn.uth.data.Alumno;
import hn.uth.data.AlumnosResponse;
import retrofit2.Response;
import retrofit2.Call;
import okhttp3.ResponseBody;

public class DatabaseRepositoryImpl {

	private static DatabaseRepositoryImpl INSTANCE;
	private DatabaseClient client;
	
	//PATRON SINGLETON
	private DatabaseRepositoryImpl(String url, Long timeout) {
		client = new DatabaseClient(url, timeout);
	}
	
	//PATRON SINGLETON
	public static DatabaseRepositoryImpl getInstance(String url, Long timeout) {
		if(INSTANCE == null) {
			synchronized (DatabaseRepositoryImpl.class) {
				if(INSTANCE == null) {
					INSTANCE = new DatabaseRepositoryImpl(url, timeout);
				}
			}
		}
		return INSTANCE;
	}
	
	public AlumnosResponse consultarAlumnos() throws IOException {
		Call<AlumnosResponse> call = client.getDatabase().obtenerAlumnos();
		Response<AlumnosResponse> response = call.execute();//AQUI SE PRODUCE LA LLAMADA
		if(response.isSuccessful()) {
			return response.body();
		}else {
			return null;
		}
	}
	
	public boolean crearAlumno(Alumno nuevo) throws IOException {
		Call<ResponseBody> call = client.getDatabase().crearAlumno(nuevo);
		Response<ResponseBody> response = call.execute();//AQUI SE PRODUCE LA LLAMADA
		return response.isSuccessful();
	}
	
	public boolean actualizarAlumno(Alumno existente) throws IOException {
		Call<ResponseBody> call = client.getDatabase().actualizarAlumno(existente);
		Response<ResponseBody> response = call.execute();//AQUI SE PRODUCE LA LLAMADA
		return response.isSuccessful();
	}
}
