package hn.uth.repository;

import hn.uth.data.Alumno;
import hn.uth.data.AlumnosResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface DatabaseRepository {

	@Headers({
	    "Accept: application/vnd.github.v3.full+json",
	    "User-Agent: sistema-matricula"
	})
	@GET("/pls/apex/ingenieria_uth/appmatricula/alumnos")
	Call<AlumnosResponse> obtenerAlumnos();
	
	@Headers({
	    "Accept: application/vnd.github.v3.full+json",
	    "User-Agent: sistema-matricula"
	})
	@POST("/pls/apex/ingenieria_uth/appmatricula/alumnos")
	Call<ResponseBody> crearAlumno(@Body Alumno nuevo);
	
	@Headers({
	    "Accept: application/vnd.github.v3.full+json",
	    "User-Agent: sistema-matricula"
	})
	@PUT("/pls/apex/ingenieria_uth/appmatricula/alumnos")
	Call<ResponseBody> actualizarAlumno(@Body Alumno nuevo);
	
	@Headers({
	    "Accept: application/vnd.github.v3.full+json",
	    "User-Agent: sistema-matricula"
	})
	@DELETE("/pls/apex/ingenieria_uth/appmatricula/alumnos")
	Call<ResponseBody> eliminarAlumno(@Query("id") Integer id);
}
