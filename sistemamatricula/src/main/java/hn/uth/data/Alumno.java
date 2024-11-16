package hn.uth.data;

import java.sql.Date;
import java.time.LocalDate;

public class Alumno extends AbstractEntity {

    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
    //private LocalDate fechaNacimiento;
    private Date fecha_nacimiento;
    private String carrera;

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getApellido() {
        return apellido;
    }
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    public String getCorreo() {
        return correo;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    /*public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }*/
    public String getCarrera() {
        return carrera;
    }
    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }
	public Date getFecha_nacimiento() {
		return fecha_nacimiento;
	}
	public void setFecha_nacimiento(LocalDate fecha_nacimiento) {
		this.fecha_nacimiento = convertirFecha(fecha_nacimiento);
	}

	private Date convertirFecha(LocalDate fecha) {
		return Date.valueOf(fecha);
	}
}
