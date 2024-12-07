package hn.uth.data;

import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class AlumnosReport implements JRDataSource {
	
	private List<Alumno> items;
	private int contador = -1;
	private int maximo = 0;

	public List<Alumno> getItems() {
		return items;
	}

	public void setItems(List<Alumno> items) {
		this.items = items;
		this.maximo = this.items.size() -1;
	}

	public int getContador() {
		return contador;
	}

	public int getMaximo() {
		return maximo;
	}


	@Override
	public boolean next() throws JRException {
		if(contador < maximo) {
			contador++;
			return true; //AUN HAY DATOS QUE IMPRIMIR EN EL REPORTE
		}
		return false; //SE ENTIENDE QUE YA NO HAY MAS DATOS QUE IMPRIMIR EN EL REPORTE
	}

	@Override
	public Object getFieldValue(JRField jrField) throws JRException {
		//PERMITE LLENAR CADA CAMPO -FIELD- DEL REPORTE
		if("NombreCompleto".equals(jrField.getName())) {
			return items.get(contador).getNombre()+" "+items.get(contador).getApellido();
		}else if("Carrera".equals(jrField.getName())) {
			return items.get(contador).getCarrera();
		}else if("Correo".equals(jrField.getName())) {
			return items.get(contador).getCorreo();
		}else if("Telefono".equals(jrField.getName())) {
			return items.get(contador).getTelefono();
		}
		return "";
	}

}
