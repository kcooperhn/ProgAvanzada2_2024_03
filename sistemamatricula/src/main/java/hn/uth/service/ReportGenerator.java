package hn.uth.service;

import java.io.File;
import java.util.Map;

import org.springframework.util.ResourceUtils;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.JasperReport;

public class ReportGenerator {

	private String ubicacionReporte;
	
	public boolean generarReportePDF(String nombreReporte, JRDataSource datasource, Map<String, Object> parameters) {
		boolean generado = false;
		try {
			//CARGAMOS EL ARCHIVO COMPILADO .JASPER
			//PARA LLENAR EL REPORTE A GENERAR
			JasperReport reporte = (JasperReport)JRLoader.loadObjectFromFile(obtenerUbicacionArchivo(nombreReporte+".jasper"));
			//LLENAR EL REPORTE MEDIANTE UNA IMPRESORA
			JasperPrint impresora = JasperFillManager.fillReport(reporte, parameters, datasource);
			//GENERAR LA UBICACION DEL ARCHIVO PDF CON EL CONTENIDO
			ubicacionReporte = generarUbicacionReporte();
			//SE EXPORTA EL PDF CON LOS DATOS DEL REPORTE
			JasperExportManager.exportReportToPdfFile(impresora, ubicacionReporte);
			generado = true;
		}catch(Exception error) {
			error.printStackTrace();
			generado = false;
		}
		return generado;
	}
	
	private String generarUbicacionReporte() {
		String ubicacion = null;
		try {
			ubicacion = File.createTempFile("temp", ".pdf").getAbsolutePath();
		}catch(Exception error) {
			error.printStackTrace();
		}
		return ubicacion;
	}
	
	private String obtenerUbicacionArchivo(String archivo) {
		String ubicacion = null;
		try {
			ubicacion = ResourceUtils.getFile("classpath:"+archivo).getAbsolutePath();
		}catch(Exception error) {
			error.printStackTrace();
		}
		return ubicacion;
	}

	public String getUbicacionReporte() {
		return ubicacionReporte;
	}
	
	
}
