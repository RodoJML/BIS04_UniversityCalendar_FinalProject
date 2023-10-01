/*Creado por:
 * 20200112770 Rodolfo Javier Meneses Leal
 * */

import java.util.Date;

public class UniversityCalendar {
	
	int MAXACTIVITIES = 100;
	
	Student currentStudent;
	Activity[] activities;
	byte activitiesCounter;
	Date today;
	
	UniversityCalendar(){
		this.today = new Date();
		this.currentStudent = new Student();
		this.activitiesCounter = 0;
		
		this.activities = new Activity[MAXACTIVITIES];
				
		for(int i = 0; i != MAXACTIVITIES; i++) { // Para evitar en nullPointerException
			this.activities[i] = new Activity();
		}
		
	}
	
	long calculateWeek(Date day){
		// Utilizamos el metodo .getTime() para extraer la cantidad de milisegundos total a la fecha seleccionada desde 1970
		// Y le restamos la diferencia en milisegundos transcurridos a la fecha de inicio de lecciones desde 1970.
		long difference_In_Time = day.getTime() - this.currentStudent.getStartDate().getTime();
        
		// La cantidad de milisegundos lo convertimos a dias.
		long difference_In_Days = (difference_In_Time / (1000 * 60 * 60 * 24)) % 365;
		
		// La cantidad de dias se divide entre 7 para obtener la cantidad de semanas exacta.
		// Tambien se redondea para obtener un numero exacto y se agrega un 1 ya que el valor siempre nos daba 1 semana menos.
        long difference_In_Weeks = Math.floorDiv(difference_In_Days, 7) + 1;
		
        // retornamos el resultado
		return difference_In_Weeks;
	}

	public Date getToday() {
		return today;
	}

	public void setToday(Date today) {
		this.today = today;

	}

	public byte getActivitiesCounter() {
		return activitiesCounter;
	}

	public void setActivitiesCounter(byte activitiesCounter) {
		this.activitiesCounter = activitiesCounter;
	}

	
	
}
