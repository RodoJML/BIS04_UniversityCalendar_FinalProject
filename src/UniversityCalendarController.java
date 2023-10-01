/*Creado por:
 * 20200112770 Rodolfo Javier Meneses Leal
 * */

import java.text.ParseException;

public class UniversityCalendarController {
	
	// El controlador es la clase donde se instancia el objeto modelo y la vista.
	UniversityCalendar model = new UniversityCalendar();
	UniversityCalendarView view = new UniversityCalendarView(model);

	// Creamos la funcion start() para inicializar el programa.
	void start() throws ParseException {
		
		// Si la funcion WelcomeScreen retorna verdadero se ejecuta la pantalla de menu principal
		// Si retorna falso WelcomScreen tiene recursividad dentro del metodo y vuelve a iniciar.
		if(view.WelcomeScreen()) {
			
			// Ejecuta la pantalla principal del programa y todas sus funciones.
			view.MainScreen();
		}
		return;
	}
}
