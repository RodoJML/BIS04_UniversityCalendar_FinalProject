/*Creado por:
 * 20200112770 Rodolfo Javier Meneses Leal
 * */

import java.util.Date;

public class Student {
	
	// Propiedades de la clase Student
	String name; 		// Nombre del estudiante
	Date startDate;		// Inicio de lecciones
	String courses[];	// Cursos 
	
	// Metodos de la clase Student
	
	// Retorna el nombre del estudiante
	public String getName() {
		return name;
	}
	
	// Asgina el nombre al estudiante
	public void setName(String name) {
		this.name = name;
	}
	
	// Retorna la fecha de inicio de lecciones del estudiante
	public Date getStartDate() {
		return startDate;
	}
	
	// Asigna la fecha de inicio de lecciones del estudiante
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	// Retorna los cursos del estudiante
	public String[] getCourses() {
		return courses;
	}
	
	// Asgina los cursos del estudiante
	public void setCourses(String[] courses) {
		this.courses = courses;
	}
}
