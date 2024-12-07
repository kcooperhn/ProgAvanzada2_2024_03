package hn.uth.sistemamatricula.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static java.time.Duration.ofSeconds;
import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SistemaTest {
	
	//@Test
	public void testGuardarAlumno() throws InterruptedException {
		
		// Inicializa el WebDriver para Chrome
		WebDriver driver = new ChromeDriver();
		// Abre la página web de inicio de sesión
		driver.get("http://localhost:8080/");
		
		new WebDriverWait(driver, ofSeconds(30), ofSeconds(1)).until(titleIs("Alumnos"));
		
		// Localiza el campo de entrada de nombre de usuario
		WebElement campoNombre = driver.findElement(By.xpath("//vaadin-text-field[@id='txtNombre']/input"));
		WebElement campoApellido = driver.findElement(By.xpath("//vaadin-text-field[@id='txtApellido']/input"));
		WebElement campoCorreo = driver.findElement(By.xpath("//vaadin-text-field[@id='txtCorreo']/input"));
		WebElement campoFechaNac = driver.findElement(By.xpath("//vaadin-date-picker[@id='dpFechaNac']/input"));
		WebElement campoTelefono = driver.findElement(By.xpath("//vaadin-text-field[@id='txtTelefono']/input"));
		WebElement campoCarrera = driver.findElement(By.xpath("//vaadin-text-field[@id='txtCarrera']/input"));
		
		//LOCALIZANDO BOTONES
		WebElement btnGuardar = driver.findElement(By.xpath("//vaadin-button[@id='btnGuardar']"));

		
		// Ingresa el nombre de usuario
		campoNombre.sendKeys("Pedro Rolando");
		campoApellido.sendKeys("Perez Pereira");
		campoCorreo.sendKeys("prolando@gmail.com");
		campoTelefono.sendKeys("99775533");
		campoCarrera.sendKeys("Ingenieria en Producción Industrial");
		
		//campoFechaNac.click();
		
		//WebElement opcionDeseada = driver.findElement(By.xpath("//vaadin-date-picker-overlay[@id='overlay']/vaadin-date-picker-overlay-content/vaadin-date-picker-month-scroller/div[3]/vaadin-month-calendar")); 
		//opcionDeseada.click();
		
		btnGuardar.click();
		
		Thread.sleep(4000);
		try {
			WebElement notificacionExito = driver.findElement(By.xpath("//vaadin-notification-card[contains(.,'Alumno modificado exitosamente!')]"));
			
			String expected="Alumno modificado exitosamente!";
			String actual=notificacionExito.getText();
			//assertEquals(expected, actual);
		}catch(Exception e) {
			//assertTrue(e.getMessage() == null);
		}
		
	}
}
