/*Creado por:
 * 20200112770 Rodolfo Javier Meneses Leal
 * */

import java.util.Date; // Importa la libreria util.date ya que una de las propiedades de clase hace uso de fecha

public class Activity {
	
	// Propiedades de la clase Activity
	String details; 	// Detalles de la actividad
	String course;		// Curso al que pertenece la actividad
	String category;	// Categoria a la que pertenece la actividad (Entregable o examen)
	Date dueDate;		// Fecha de entrega de la actividad
	
	// Retorna los Detalles de la actividad
	public String getDetails() {
		return details;
	}
	
	// Asgina los Detalles de la actividad
	public void setDetails(String details) {
		this.details = details;
	}
	
	// Retorna el curso al que pertenece la actividad
	public String getCourse() {
		return course;
	}
	
	// Asigna el curso al que pertenece la actividad
	public void setCourse(String course) {
		this.course = course;
	}
	
	// Retorna la fecha de entrega de la actividad
	public Date getDueDate() {
		return dueDate;
	}
	
	// Asigna la fecha de entrega de la actividad
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	
	// Retorna la categoria a la que pertenece la actividad (Entregable o examen)
	public String getCategory() {
		return category;
	}
	
	// Asigna la categoria a la que pertenece la actividad (Entregable o examen)
	public void setCategory(String category) {
		this.category = category;
	}
	
}
