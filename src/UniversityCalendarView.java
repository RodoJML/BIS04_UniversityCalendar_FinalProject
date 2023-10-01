/*Creado por:
 * 20200112770 Rodolfo Javier Meneses Leal
 * */

import java.util.Date;
import java.util.Properties;
import java.util.Scanner;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.jdatepicker.impl.DateComponentFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

public class UniversityCalendarView implements ActionListener{
	
	// Declaracion de un objeto tipo University Calendar, este se utilizara luego para hacer referencia al modelo y no crear nuevos objetos.
	UniversityCalendar model; 
	
	// Declaracion de 2 objetos Scanner para posteriormente leer informacion, este sera utilizado para leer desde archivos txt.
	Scanner readFile;
	Scanner readFile1;
	
	// Declaracion de 1 objecto tipo "FileWriter", este permitira la escritura en archivos de texto.	
	FileWriter save;
	
	// Declaracion de 2 objectos "File" , estos se encargaran de crear los 2 archivos txt que utilizara el programa,
	File data;
	File data1;
	
	// Declaracion de 2 variables "String" ambas contendran en una sola linea de texto toda la informacion tanto del estudiante
	// como de las actividades, separado por comas, posteriormente estos "String" seran leidos y convertidos en arrays.
	// Ver sus usos en la linea 155 y linea 396. Dichos datos se almacenan al presionar el boton "Submit" de la pantalla de inicio
	// asi como tambien el botton de "agregar" actividades.
	String studentInformation; 
	String activityInformation; 
	
	// Declaracion del array que contendra en cada uno de sus slots una linea de texto con toda la informacion de 1 actividad
	// en una sola linea separada por comas fecha, curso, detalle de la actividad, etc.
	// En la linea 473 podemos ver como por cada punto y coma se popula un slot de informacion
	// En la linea 482 podemos ver como se lee cada slot y se separa en un nuevo array con los datos de 1 actividad. 
	String[] activitiesInformationArray;
	
	// Se declara e inicializa una variable booleana que inidica si el programa ha sido utilizado anteriormente.
	// Si un estudiante anteriormente ha ingresado informacion (Linea 467) este se convierte en true, por defecto sera false.
	boolean infoSubmitted = false;

	
	// Constructor de la clase "UniversityCalendarView" recibe un solo parametro de tipo "UniversityCalendar"
	UniversityCalendarView(UniversityCalendar parameter0){ 
		
		// Hacemos referencia al modelo sin necesidad de crear un nuevo objeto.
		this.model = parameter0; 
		
		// Instanciamos los objetos declarados, creamos ambos archivos de texto que seran utilizados por el programa
		// para leer y almacenar toda la informacion y poder cargar los datos aun despues de cerrada la aplicacion.
		this.data = new File("StudentsInformation.txt");
		this.data1 = new File("ActivitiesInformation.txt"); 
		
		// Instanciamos los objetos "Scanner" llamados "readFile" y "readFile1" 
		// indicamos que estaran leyendo informacion cada uno desde "data" y "data1" respectivamente
		// data es equivalente al archivo de texto "StudentsInformation.txt"
		// data1 es equivalente al archivo de texto "ActivitiesInformation.txt"
		try {
			this.readFile = new Scanner(data);
		} catch (FileNotFoundException e) { // Si al instanciar el objeto Scanner no existe un archivo previamente arroja lo siguiente: 
			JOptionPane.showMessageDialog(null, "¡Bienvenido a su calendario universitario!\n\n"
					+ "Si puede leer este mensaje entonces es su primera vez utilizando \n"
					+ "la aplicacion, o la informacion de usuario ha sido eliminada.", "Aviso", 2);
			e.printStackTrace();
		}
	}

	
	// La pantalla de bienvenidad arrojara TRUE or FALSE dependiendo de la existencia de un usuario previamente.
	boolean WelcomeScreen() throws ParseException {

		// JFrame es el contenedor principal y dentro de este colocaremos el o los grupos de componentes, ya sea botones, etiquetas, etc.
		// El objeto "Frame" crea una ventana en blanco con el titulo "Bienvenido".
		JFrame frame = new JFrame("Bienvenido");

		// JPanel Se encargara de agrupar los componentes graficos por tipo, para un mejor orden.
		// El objeto "Fields" contendra todos los textos o labels, y los campos para introducir texto. 
		JPanel fields = new JPanel();

		// Procedemos a crear los objetos inidividuales para campos de texto y sus respectivas etiquetas.
		
		// Etiquetas
		JLabel welcomeMessage = new JLabel("Calendario Universitario"); 
		JLabel userNameLabel = new JLabel("Ingrese su nombre completo: "); 
		JLabel startDateLabel = new JLabel("Inicio del cuatrimestre: ");
		JLabel subjectLabel = new JLabel("Ingrese las materias matriculadas: ");
		
		// Campos para introducir texto 
		JTextField textFieldUserName = new JTextField();
		JTextField subjedct0textField = new JTextField();
		JTextField subjedct1textField = new JTextField();
		JTextField subjedct2textField = new JTextField();
		JTextField subjedct3textField = new JTextField();
		JTextField subjedct4textField = new JTextField();
		JTextField subjedct5textField = new JTextField();
		JTextField subjedct6textField = new JTextField();

		
		// --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
		// Creacion del JDatePicker, boton que permite seleccionar fechas.
		// Segun lo investigado en internet debemos crear lo de la siguiente manera:
		
		// (1) Primero creamos un modelo, objeto de tipo "UtilDateModel" eso permitira que las fechas
		// que utilizara el JDatePicker sean las nativas de java de la libreria "java.util.date"
		UtilDateModel model = new UtilDateModel(); 
		
		// (2) Segundo se crea un objeto de tipo "Properties" para asignar propiedades al objeto
		// en cuanto al texto que se despliega para hoy, mes y año.
		Properties p = new Properties(); 
		p.put("text.today", "Hoy");
		p.put("text.month", "Mes");
		p.put("text.year", "Año");
		
		// (3) Tercero se crea un objeto de tipo "JDatePanelImpl" pero su constructor requiere de 2 parametros 
		// estos 2 parametros son los previamente creados, UtilDateModel (model) y Properties (p). 
		JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
		
		// (4) Por ultimo creamos el objeto mas importante que nos permitira desplegar el calendario 
		// y mostrar fechas, este es un objeto de tipo "JDatePickerImpl" que requiere 2 parametros
		// estos son 1 "JDatePanelImpl" y "DateComponentFormatter()" este ultimo permite que la fecha seleccionada
		// pueda aparecer como texto en la ventana activa. 
		JDatePickerImpl datePicker = new JDatePickerImpl(datePanel,new DateComponentFormatter());
		//  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
		
		
		// Indicamos al contenedor de textos y etiquetas que las ordene de manera "BoxLayout" sobre el eje Y
		// Esto es para poder visualizar sus objetos de manera vertical.
		fields.setLayout(new BoxLayout(fields, BoxLayout.Y_AXIS));
		

		// Hacemos uso del contenedor "fields" para empezar a llenarlo de todos las etiquetas y campos de texto
		fields.add(welcomeMessage);
		fields.add(userNameLabel);
		fields.add(textFieldUserName);
		fields.add(startDateLabel);
		fields.add(datePicker);
		fields.add(subjectLabel);
		fields.add(subjedct0textField);
		fields.add(subjedct1textField);
		fields.add(subjedct2textField);
		fields.add(subjedct3textField);
		fields.add(subjedct4textField);
		fields.add(subjedct5textField);
		fields.add(subjedct6textField);

		// Se procede a crear los botones que se utilizaran en esta ventana
		JButton submit = new JButton("SUBMIT");

		// Agregamos a nuestro frame en blanco el conjunto de fields, etiquetas, etc.
		frame.add(fields, BorderLayout.PAGE_START);
		frame.add(submit, BorderLayout.PAGE_END);

		// Damos unos parametros finales a nuestro frame o contenedor principal.
		frame.setLocationRelativeTo(null); 	// Pone la ventana en el centro de la pantalla.
		frame.setVisible(true); 			// Hace que la ventana sea visible.
		frame.pack();						// Ajusta la ventana principal al tamano de los elementos que contiene.
		
		// Establece lo que sucede si el usuario selecciona la X para cerrar ventana, en este caso termina la app.
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		// Aqui establecemos lo que sucede una vez que el usuario presiona el boton "SUBMIT"
		submit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				// Convertimos de formato fecha a String o texto
				// Extraemos la fecha seleccionada por el usuario desde el JDate Picker usando datePicker.getModel().getValue()
				Date selectedDate = (Date) datePicker.getModel().getValue();
				// Creamos un objeto de tipo DateFormat con formato "MM/dd/yyyy" para despues aplicarselo a la fecha extraida.
				// La fecha por defecto es extensa como Agosto 22 del 2022 + hora, etc. Entonces ocupamos reducirla para un facil uso.
				DateFormat dateFormatting = new SimpleDateFormat("dd/MM/yyyy");
				// Convertiremos el formato la fecha extensa a un String secillo en formato "MM/dd/yyyy"
				String selectedDatebyUser = dateFormatting.format(selectedDate);

				// Procedemos a leer los datos ingresados por el usuario para almacenarlos en un solo String.
				// La idea de esto es poder, posteriormente, almancenar el String al archivo de texto.
				studentInformation = (textFieldUserName.getText() 
						+ "," + selectedDatebyUser
						+ "," + subjedct0textField.getText() 
						+ "," + subjedct1textField.getText()
						+ "," + subjedct2textField.getText()
						+ "," + subjedct3textField.getText()
						+ "," + subjedct4textField.getText()
						+ "," + subjedct5textField.getText()
						+ "," + subjedct6textField.getText()
						+ "," + "TRUE"); // Ademas de guardar toda la info, este "TRUE" va funcionar para saber si ya se presiono submit en algun punto
										 // Se extrae y se convierte a Boolean para saber si alguien anteriormente ya uso la app.
				
				
				// Se procede a guardar todo el texto anterior en el archivo .txt existente "StudentsInformation.txt"
				// Recordar que el proposito de este archivo de texto es poder posteriormente cargar los datos, aun despues de cerrada la app.
				try {
					save = new FileWriter("StudentsInformation.txt"); 
					save.write(studentInformation); 			      
					save.close(); // Es importante hacer "close" para decirle a la funcion que hemos terminado de escribir.
					
					save = new FileWriter("ActivitiesInformation.txt"); 
					save.write(""); 			      
					save.close();
				}
				// De no encotrar los archivos de texto o presentarse alguna excepcion al escribir en ellos entonces imprimimos en consola,
				// utilizando el "printStackTrace" el error en cuestion.
				catch (IOException c) {
					c.printStackTrace();
				}
				
				// Ocultamos la ventana de bienvenida.
				frame.setVisible(false);

				// Lanzamos la ventana o pantalla principal de la aplicacion.
				try {
					MainScreen();
				}
				catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
		}); // Fin del ActionListener
		
		
		// La pantalla de bienvenida siempre se ejecutara, pero sera visible solo si no hay usuario existentes o...
		// si el usuario desea borrar y comenzar un nuevo calendario. Para detalles en lo que realiza esta funcion ver la linea 492
		// Al siempre ejecutar la pantalla de bienvenida nos aseguramos de siempre leer tambien la informacion de usuario.
		read_studentInfo();

		// Si hay informacion existente de un previo usuario entonces.
		if(infoSubmitted) { 
			// 	El frame principal se oculta
			frame.setVisible(false); 
			// Y muestra el siguiente mensaje:
			int OptionSelected = JOptionPane.showInternalOptionDialog(null, "Se ha detectado un calendario existente, desea crear uno nuevo?"
					+ "\nNO - Carga el usuario existente y su calendario \nSI - Crea un nuevo usuario y calendario", 
					"User Check", 0, 3, null, null, data);
			
			// Si selecciona que desea crear un nuevo usuario.
			if(OptionSelected == 0) {	
	
				// Borra el contenido dentro del archivo .txt que contiene la informacion de estudiante.
				try {
					save = new FileWriter("StudentsInformation.txt"); 
					save.write(""); 			      
					save.close(); // Es importante hacer "close" para decirle a la funcion que hemos terminado de escribir.
				} 
				catch (IOException c) { // Ataja cualquier excepcion si al intentar escribir el archivo existe algun error o exception.
					c.printStackTrace();
				}
				
				// Borra el contenido dentro del archivo .txt que contiene la informacion de las actividades.
				try {
					save = new FileWriter("ActivitiesInformation.txt"); 
					save.write(""); 			      
					save.close(); // Es importante hacer "close" para decirle a la funcion que hemos terminado de escribir.
				} 
				catch (IOException c) { // Ataja cualquier excepcion si al intentar escribir el archivo existe algun error o exception.
					c.printStackTrace();
				}
				
				// La variable "infoSubmitted" pasa a ser false, para no quedarnos atrapados en un loop. 
				this.infoSubmitted = false;
				
				// Se genera un nuevo archivo txt donde almacenaremos la nueva info de usuario.
				this.data = new File("StudentsInformation.txt");
				
				// Se ejecuta la pantalla de bienvenida nuevamente para pedir toda la info de un usuario nuevo
				WelcomeScreen(); 
			}

			if(OptionSelected == 1) { 	// De seleccionar que no desea crear un nuevo usuario entonces simplemente finaliza la funcion
				return true; 			// Finaliza la funcion devolviendo true para indicar lo que haya elegido el usuario
			}	
		}
		return false; // Finaliza la funcion de WelcomeScreen retornando un valor "false" si el usuario decide crear un nuevo usuario.
	}

	
	void MainScreen() throws ParseException {

		// Antes de iniciar la pantalla de principal extraemos toda la informacion de usuario necesaria 
		// del archivo de texto "StudentsInformation.txt" y "ActivitesInformation.txt" 
		read_studentInfo();
		read_write_activites();
		
		// Creamos un tipo de dato "Date" o fecha, con la fecha de hoy = Date()
		Date today0 = new Date();
		
		// JFrame es el contenedor principal y dentro de este colocaremos el o los grupos de componentes, ya sea botones, etiquetas, etc.
		// El objeto "Frame" crea una ventana en blanco con el titulo "Calendario Universitario".
		JFrame frame = new JFrame("Calendario Universitario");

		// JPanel Se encargara de agrupar los componentes graficos por tipo, para un mejor orden.
		// El objeto "Fields" contendra todos los textos o labels, y los campos para introducir texto.
		// El objeto "buttons" contendra todos los botones.
		JPanel fields = new JPanel();
		JPanel buttons = new JPanel();

		// Creacion de etiquetas 
		JLabel universityInfo = new JLabel("Universidad Latina de Costa Rica");
		JLabel blank = new JLabel(" ");
		JLabel greeting = new JLabel("¡Hola! " + model.currentStudent.getName() + "."); 				// Saludo al estudiante
		JLabel currentWeek = new JLabel("Cursando semana: " + model.calculateWeek(model.getToday())); 	// Semana actual
		JLabel today1 = new JLabel("" + today0);													// Fecha de hoy
		JLabel blank1 = new JLabel(" ");
		JLabel recentActivitiesLabel = new JLabel("Actividades agregadas durante la ultima sesion: ");
		
		// Creacion de los botones
		JButton queryWeek = new JButton("Consultar Semana");											
		JButton addActivity = new JButton("Ingresar Actividad");
		JButton seeAllActivities = new JButton("Ver todas las actividades");
		JButton settings = new JButton("Ajustes");
                JLabel credits = new JLabel(" Desarrollador: Rodolfo Javier Meneses Leal - 2021");
		
		// Se crea un array String de dos dimensionas de 5 filas y 3 columans, para almacenar las 5 actividades recientes ingresadas por el usuario.
		String[][] data0 = new String[5][3];
		
		// Creamos un array String con los titulos que tendran las columnas.
		String[] columNames0 = {"Tipo", "Curso", "Detalles"};
		
		// Creamos un contador, este se utilizara para evitar que el bucle for a continuacion exceda el tamano del array,
		// cuando este alcanze 5 entonces no se escribe mas en el array.
		int counter = 0;

		// Creacion de un bucle for, en vez de incrementar este va en reduciendo su tamano, obteniendo la cantidad de actividades actuales en el programa
		for(int i = model.getActivitiesCounter() - 1; i >= 0; i--) {
			for(int j = 0; j < 4; j++) {

				// SOLO si el contador es menor a 5 entonces se escribe en el array
				if(counter < 5) {
					switch(j) {
					case 0:
						// Extraemos del modelo las propiedas de la actividad, "tipo"
						data0[counter][j] = model.activities[i].getCategory();
						break;
					case 1:
						// Extraemos del modelo las propiedas de la actividad, "curso"
						data0[counter][j] = model.activities[i].getCourse();
						break;
					case 2:
						// Extraemos del modelo las propiedas de la actividad, "detalles"
						data0[counter][j] = model.activities[i].getDetails();
						break;
					}
				}
			}
			// Se incrementa el contador
			counter++;
		}

		// Se crea una tabla, en esta se incluyen los array creado hace unas cuantas lineas atras (data0) y (columnNames0)
		JTable recentActivities = new JTable(data0, columNames0);
		
		// Se crea un panel para poder hacer "scroll" con el mouse, y dentro de este panel se coloca la tabla.
		// Esto es para que la tabla no sea fija en pantalla y se pueda subir y bajar con el mouse.
		JScrollPane scrollPane = new JScrollPane(recentActivities);
		
		//recentActivities.setFillsViewportHeight(false);
		// Establecemos el tamaño de la tabla
		scrollPane.setPreferredSize(new Dimension(100, 104));

		// Indicamos al contenedor de etiquetas y botones que las ordene de manera "BoxLayout" sobre el eje "Y"
		// esto es para poder visualizar sus objetos de manera vertical. 
		fields.setLayout(new BoxLayout(fields, BoxLayout.Y_AXIS));
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));

		// Hacemos uso del contenedor "fields" para empezar a llenarlo de todos las etiquetas
		fields.add(universityInfo);
		fields.add(today1);
		fields.add(blank);
		fields.add(greeting);
		fields.add(currentWeek);
		fields.add(blank1);
		fields.add(recentActivitiesLabel);
		fields.add(scrollPane);
		
		// Hacemos uso del contenedor "buttons" para empezar a llenarlo de todos los botones
		buttons.add(queryWeek);
		buttons.add(addActivity);
		buttons.add(seeAllActivities);
		buttons.add(settings);
                buttons.add(credits);
		
		// Agregamos a nuestro frame contenedor principal el conjunto de etiquetas, botones, etc.
		frame.add(fields, BorderLayout.PAGE_START); // BorderLayout para colocar todas las etiquetas o texto en la parte superior.
		frame.add(buttons, BorderLayout.PAGE_END);	// BorderLayout para colocar todos los botones en la parte inferior del programa.

		// Damos unos parametros finales a nuestro frame o contenedor principal.
		frame.setLocationRelativeTo(null); 						// Pone la ventana en el centro de la pantalla.
		frame.setVisible(true);			   						// Hace que la ventana sea visible.
		frame.setSize(400,380);					   				// Ajusta la ventana al tamano 400 x 380 pixeles
		
		// Establece lo que sucede si el usuario selecciona la X para cerrar ventana, en este caso termina la app.
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		// Al presionar el boton "queryWeek" esto sucede
		queryWeek.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				queryWeek(); // LLama la funcion queryWeek
			}
		}); 

		// Al presionar el boton "addActivity" esto sucede
		addActivity.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
			addActivity(); // LLama la funcion addActivity
			} 
		});

		// Al presionar el boton "seeAllActivities" esto sucede
		seeAllActivities.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				seeAllActivities();
			}

		});

		// Al presionar el boton "settings" esto sucede
		settings.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				settings();
			}

		});
		
		System.runFinalization(); // Termina cualquier objeto que haya quedado activo
	}

	void queryWeek() {
		
		// JFrame es el contenedor principal y dentro de este colocaremos el o los grupos de componentes, ya sea botones, etiquetas, etc.
		// El objeto "Frame" crea una ventana en blanco con el titulo "Consultar Semana".
		JFrame frame = new JFrame("Consultar Semana");
		
		// JPanel Se encargara de agrupar los componentes graficos por tipo, para un mejor orden.
		// El objeto "Fields" contendra todos los textos o labels, y los campos para introducir texto.
		// El objeto "buttons" contendra todos los botones.
		JPanel fields = new JPanel();
		JPanel buttons = new JPanel();
		
		// Nueva referencia al modelo, para poder accesar desde dentro del "AddActionListener" a todas las funciones del modelo.
		UniversityCalendar referenceToModel = this.model;

		// --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
		// =========== Creacion del JDatePicker, boton que permite seleccionar fechas ==================
		// Segun lo investigado en internet debemos crear lo de la siguiente manera:
		
		// (1) Primero creamos un modelo, objeto de tipo "UtilDateModel" eso permitira que las fechas
		// que utilizara el JDatePicker sean las nativas de java de la libreria "java.util.date"
		UtilDateModel model = new UtilDateModel();
		
		// (2) Segundo se crea un objeto de tipo "Properties" para asignar propiedades al objeto
		// en cuanto al texto que se despliega para hoy, mes y año.
		Properties p = new Properties(); 					
		p.put("text.today", "Hoy");
		p.put("text.month", "Mes");
		p.put("text.year", "Año");
		
		// (3) Tercero se crea un objeto de tipo "JDatePanelImpl" pero su constructor requiere de 2 parametros 
		// estos 2 parametros son los previamente creados, UtilDateModel (model) y Properties (p). 
		JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
		
		// (4) Por ultimo creamos el objeto mas importante que nos permitira desplegar el calendario 
		// y mostrar fechas, este es un objeto de tipo "JDatePickerImpl" que requiere 2 parametros
		// estos son 1 "JDatePanelImpl" y "DateComponentFormatter()" este ultimo permite que la fecha seleccionada
		// pueda aparecer como texto en la ventana activa.
		JDatePickerImpl datePicker = new JDatePickerImpl(datePanel,new DateComponentFormatter());
		// --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
		
		
		// Se crean las etiquetas
		JLabel instructions = new JLabel("Seleccione la fecha a consultar: ");
		JLabel space = new JLabel(" ");
		
		// Se crean los botones
		JButton query = new JButton("Consultar"); 

		// Hacemos uso del contenedor "fields" para empezar a llenarlo de todos las etiquetas
		fields.add(instructions);
		fields.add(datePicker);
		fields.add(space);
		
		// Hacemos uso del contenedor "buttons" para empezar a llenarlo de todos los botones
		buttons.add(query);
		
		// setLayout nos permite definir la manera en que se despliegan los botones, en este caso
		// al utilizar FlowLayout estos se mostraran en fila y a la izquierda, si hubiesen varios.
		buttons.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		// Indicamos al contenedor de etiquetas que las ordene de manera "BoxLayout" sobre el eje "Y"
		// esto es para poder visualizar sus objetos de manera vertical.
		fields.setLayout(new BoxLayout(fields, BoxLayout.Y_AXIS));

		// Agregamos a nuestro frame contenedor principal el conjunto de etiquetas, botones, etc.
		// BorderLayout.PAGE_START para indicar que las etiquetas se despliegan al inicio de la ventana
		frame.add(fields, BorderLayout.PAGE_START);
		frame.add(buttons);
		
		// Damos unos parametros finales a nuestro frame o contenedor principal.
		frame.setLocationRelativeTo(null); // Pone la ventana en el centro de la pantalla.
		frame.setVisible(true);			   // Hace que la ventana sea visible.
		frame.pack();					   // Ajusta la ventana al tamano de los elementos
		
		// Establece lo que sucede si el usuario selecciona la X para cerrar ventana
		// en este caso NO termina la app sino que oculta la ventana hasta una nueva solicitud.
		frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

		// Cuando el usuario presione el boton "query" o "consultar" sucede lo siguiente:
		query.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				
				// Muestra una ventana JOptionPane
				// (1) showMessageDialog se despliega sobre del frame principal
				// (2) Muestras el texto "Semana" + el numero que se calcula usando la logica en el "modelo" haciendo uso de la funcion "calculateWeek"
				// (3) El titulo de la ventana es "Corresponde a"
				// (4) Muestra el icono generico de Java (1)
				JOptionPane.showMessageDialog(frame, "Semana " + referenceToModel.calculateWeek((Date) datePicker.getModel().getValue()), "Corresponde a: ", 1);
			}

		}); // Fin del ActionListener

	}

	void addActivity() {
		
		// Crea un array con dos valores nada mas, para que luego el usuario pueda seleccionar el tipo de categoria de la actividad.
		String category[] = {"Entregable","Examen"};
		
		// JFrame es el contenedor principal y dentro de este colocaremos el o los grupos de componentes, ya sea botones, etiquetas, etc.
		// El objeto "Frame" crea una ventana en blanco con el titulo "Ingresar Actividad".
		JFrame frame = new JFrame("Ingresar Actividad");
		
		// JPanel Se encargara de agrupar los componentes graficos por tipo, para un mejor orden.
		// El objeto "Fields" contendra todos los textos o labels, y los campos para introducir texto.
		// El objeto "buttons" contendra todos los botones
		JPanel fields = new JPanel();
		JPanel buttons = new JPanel();

		// --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
		// =========== Creacion del JDatePicker, boton que permite seleccionar fechas ==================
		// Segun lo investigado en internet debemos crear lo de la siguiente manera:
		
		// (1) Primero creamos un modelo, objeto de tipo "UtilDateModel" eso permitira que las fechas
		// que utilizara el JDatePicker sean las nativas de java de la libreria "java.util.date"
		UtilDateModel model = new UtilDateModel(); 
		
		// (2) Segundo se crea un objeto de tipo "Properties" para asignar propiedades al objeto
		// en cuanto al texto que se despliega para hoy, mes y año.
		Properties p = new Properties(); 
		p.put("text.today", "Hoy");
		p.put("text.month", "Mes");
		p.put("text.year", "Año");
		
		// (3) Tercero se crea un objeto de tipo "JDatePanelImpl" pero su constructor requiere de 2 parametros 
		// estos 2 parametros son los previamente creados, UtilDateModel (model) y Properties (p). 
		JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
		
		// (4) Por ultimo creamos el objeto mas importante que nos permitira desplegar el calendario 
		// y mostrar fechas, este es un objeto de tipo "JDatePickerImpl" que requiere 2 parametros
		// estos son 1 "JDatePanelImpl" y "DateComponentFormatter()" este ultimo permite que la fecha seleccionada
		// pueda aparecer como texto en la ventana activa.
		JDatePickerImpl datePicker = new JDatePickerImpl(datePanel,new DateComponentFormatter());
		// --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
		
		// Creacion de etiquetas
		JLabel instruccionFecha = new JLabel("Fecha: "); 			// Texto indicando al usuario seleccione fecha	
		JLabel instruccionMateria = new JLabel("Curso: "); 			// Texto indicando al usuario seleccione curso
		JLabel categoryLabel = new JLabel("Tipo: ");				// Texto indicando al usuario seleccione el tipo de actividad 
		JLabel instruccionActividad = new JLabel("Descripcion: "); 	// Texto indicando al usuario escriba los detalles de la actividad
		JLabel space = new JLabel(" ");
		
		// Creacion de campo de texto para que el usuario ingrese informacion.
		JTextField actividad = new JTextField();
		
		// Creacion del botton agregar
		JButton addButton = new JButton("Agregar");

		// Creacion de objetos tipo JComboBox para desplegar menus con informacion para seleccionar.
		// activa un "drop down menu" usando los cursos previamente ingresados por el usuario
		JComboBox<String> coursesMenu = new JComboBox<String>(this.model.currentStudent.getCourses()); // Lee desde el modelo los cursos del usuario
		JComboBox<String> activityCategory = new JComboBox<String>(category); 						   // Usa el array creado al inicio de esta funcion

		// Agrega al contenedor "field" todas las etiquetas, campos para digitar texto y menus de opciones.
		fields.add(instruccionFecha);
		fields.add(datePicker);
		fields.add(instruccionMateria);
		fields.add(coursesMenu);
		fields.add(categoryLabel);
		fields.add(activityCategory);
		fields.add(instruccionActividad);
		fields.add(actividad);
		fields.add(space);	
		
		// Agrega al contenedor de botones, el unico boton en esta ventana "Agregar" 
		buttons.add(addButton);

		// Usando "setLayou" establecemos la manera en como se despliegan las etiquetas y botones
		buttons.setLayout(new FlowLayout(FlowLayout.LEFT));			// FlowLayout.LEFT muestra los botones de manera vertical, y a la izquierda
		fields.setLayout(new BoxLayout(fields, BoxLayout.Y_AXIS));	// BoxLayout.Y_AXIS coloca todas las etioquetas y menus sobre el eje "Y" osea vertical

		frame.add(fields, BorderLayout.PAGE_START); // Todas las etiquetas y menus en la parte superior de la ventana
		frame.add(buttons, BorderLayout.PAGE_END);  // Todos los botones en la parte inferior de la ventana
		
		// Damos unos parametros finales a nuestro frame o contenedor principal.
		frame.setLocationRelativeTo(null); 						// Pone la ventana en el centro de la pantalla.
		frame.setVisible(true);			   						// Hace que la ventana sea visible.
		frame.pack();					   						// Ajusta la ventana al tamano de los elementos
		
		// Establece lo que sucede si el usuario selecciona la X para cerrar ventana
		// en este caso NO termina la app sino que oculta la ventana hasta una nueva solicitud.
		frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

		// Accion a realizar una vez presionado el boton "Agregar" 
		addButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				
				// Convertimos de formato fecha a String o texto
				// Extraemos la fecha seleccionada por el usuario desde el JDate Picker usando datePicker.getModel().getValue()
				Date selectedDate = (Date) datePicker.getModel().getValue();
				// Creamos un objeto de tipo DateFormat con formato "MM/dd/yyyy" para despues aplicarselo a la fecha extraida.
				// La fecha por defecto es extensa como Agosto 22 del 2022 + hora, etc. Entonces ocupamos reducirla para un facil uso.
				DateFormat dateFormatting = new SimpleDateFormat("dd/MM/yyyy");
				// Convertiremos el formato la fecha extensa a un String secillo en formato "MM/dd/yyyy"
				// Observe como dentro de los parentesis colocamos la variable que contiene la fecha extraida	
				String selectedDatebyUser = dateFormatting.format(selectedDate);
			
				// En la variable "activityInformation" de tipo String almacenamos en una sola linea
				// toda la informacion de la actividad ingresada por el usuario.
				activityInformation = (selectedDatebyUser 
						+ "," + String.valueOf(coursesMenu.getSelectedItem())
						+ "," + String.valueOf(activityCategory.getSelectedItem())
						+ "," + actividad.getText()
						+ ";"); //Detalle importante, cada actividad inidivual termina con punto y coma, esto sera util despues.
				
				try {
					// Guarda en el archivo "ActivitiesInformation.txt" los valores proporciionados por el usuario
					// La idea de este archivo .txt es poder guardar la informacion de todas las actividades aun despues de cerrada la app.
					
					// La palabra "true" en esta linea permite concatenar la info que ingrese el usuario, para sumar a la lista y no sobrescribor. 
					save = new FileWriter("ActivitiesInformation.txt", true);
					
					// Escribe en el archivo "ActivitiesInformation.txt" el texto contenido en el string "activityInformation"
					save.write(activityInformation);
					
					// Es importante hacer "close" para decirle a la funcion que hemos terminado de escribir.
					save.close();
				} 

				catch (IOException c) {
					c.printStackTrace();
				}
				
				// Ocultamos la ventana de añadir actividades
				frame.setVisible(false);
				
				try {
					read_write_activites();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}
	
	void seeAllActivities() {
		
		// JFrame es el contenedor principal y dentro de este colocaremos el o los grupos de componentes, ya sea botones, etiquetas, etc.
		// El objeto "Frame" crea una ventana en blanco con el titulo "Ver todas las actividades".
		JFrame frame = new JFrame("Ver todas las actividades");
		
		// Se crea un array de 2 dimensiones con la capacidad maxima de actividades en el programa que es 100
		String[][] data = new String[model.MAXACTIVITIES][4];
		
		// Se crea un array de texto con los nombres que tendran las columnas de la tabla.
		String[] columNames = {"Fecha", "Tipo", "Curso", "Detalles"};
		
		// Se crea un bucle for que incrementa mientras este sea menor a la cantidad de actividades que haya ingresado el usuario.
		for(int i = 0; i < model.getActivitiesCounter(); i++) {
			// Se crea un for lopp anidado para establecer lo que se colocara en cada una de las 4 columnas 
			// (0) Fecha, (1) Tipo, (2) Curso, (3) Detalles
			for(int j = 0; j < 4; j++) {
				
				switch(j) {
				case 0: // Fecha
					Date activityDate = model.activities[i].getDueDate();
					DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
					String activityDueDate = dateFormat.format(activityDate);
					data[i][j] = activityDueDate;
					break; 
				
				case 1: // Tipo (Entregable o Examen)
					data[i][j] = model.activities[i].getCategory(); // Se extrae la informacion del modelo, de las propiedades del objeto de actividades.
					break;
				case 2: // Curso
					data[i][j] = model.activities[i].getCourse(); // Se extrae la informacion del modelo, de las propiedades del objeto de actividades.
					break;
				case 3: // Detalles
					data[i][j] = model.activities[i].getDetails(); // Se extrae la informacion del modelo, de las propiedades del objeto de actividades.
					break;
				}
			}
		}

		// Se procede a crear la tabla JTable con toda la informacion previamente desde los bucles for
		JTable recentActivities = new JTable(data, columNames);
		
		// Se crea un objeto scrollPane y se agrega la tabla dentro de el para poder navegar usando el mouse
		JScrollPane scrollPane = new JScrollPane(recentActivities);
		//recentActivities.setFillsViewportHeight(true);
		
		// Se agrega al frame principal el scrollpane con la tabla
		frame.add(scrollPane);

		// Damos unos parametros finales a nuestro frame o contenedor principal.
		frame.setLocationRelativeTo(null); 						// Pone la ventana en el centro de la pantalla.
		frame.setVisible(true);			   						// Hace que la ventana sea visible.
		frame.setSize(400,380);					   			// Ajusta la ventana al tamano 

		// Establece lo que sucede si el usuario selecciona la X para cerrar ventana
		// en este caso NO termina la app sino que oculta la ventana hasta una nueva solicitud.
		frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
	}
	
	void settings() {

		// Creamos un objeto DateFormat este objeto se usa para dar formato a fechas
		// pasarlas de un formato extenso, a un formato deseaodo en este caso "dd/MM/yyyy" 
		// Se usara mas abajo 
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		
		// Creacion del frame o ventana principal que contendra los campos y botones
		JFrame frame = new JFrame("Ajustes");
		
		// Creacion del panel que contendra todas las etiquetas, campos de texto, seleccionadores de fecha.
		JPanel fields = new JPanel();
		
		// Creacion del panel que contendra todos los botones
		JPanel buttons = new JPanel();
		
		// --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
		// =========== Creacion del JDatePicker, boton que permite seleccionar fechas ==================
		// Segun lo investigado en internet debemos crear lo de la siguiente manera:
		//
		// (1) Primero creamos un modelo, objeto de tipo "UtilDateModel" eso permitira que las fechas
		// que utilizara el JDatePicker sean las nativas de java de la libreria "java.util.date"
		UtilDateModel model = new UtilDateModel();
		model.setValue(this.model.currentStudent.getStartDate());
		model.setSelected(true);
		//
		// (2) Segundo se crea un objeto de tipo "Properties" para asignar propiedades al objeto
		// en cuanto al texto que se despliega para hoy, mes y año.
		Properties p = new Properties(); 
		p.put("text.today", "Hoy");
		p.put("text.month", "Mes");
		p.put("text.year", "Año");
		// (3) Tercero se crea un objeto de tipo "JDatePanelImpl" pero su constructor requiere de 2 parametros 
		// estos 2 parametros son los previamente creados, UtilDateModel (model) y Properties (p). 
		JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
		//
		// (4) Por ultimo creamos el objeto mas importante que nos permitira desplegar el calendario 
		// y mostrar fechas, este es un objeto de tipo "JDatePickerImpl" que requiere 2 parametros
		// estos son 1 "JDatePanelImpl" y "DateComponentFormatter()" este ultimo permite que la fecha seleccionada
		// pueda aparecer como texto en la ventana activa.
		JDatePickerImpl datePicker = new JDatePickerImpl(datePanel,new DateComponentFormatter());
		// --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --  --
		
		// Creacion de las etiquetas
		JLabel startDateLabel0 = new JLabel("Inicio de Lecciones");
		JLabel blank0 = new JLabel(" ");
		JLabel blank1 = new JLabel(" ");
		JLabel changeNameLabel = new JLabel("Cambiar nombre del estudiante: ");
		// Creacion campo de texto donde el usuario puede escribir, pero por defaul tendra el nombre actual del estudianteç
		// podemos ver que dentro del constructor JTextField leemos desde el modelo el objeto de "currentStudent" y leemos una prpiedad, el nombre.
		JTextField enterStudentName = new JTextField(this.model.currentStudent.getName());
		
		// Creacion de los botones
		JButton save0 = new JButton("Guardar");
		JButton eraseAllActivities = new JButton("Borrar todas las actividades");
		
		// Se extraen desde el modelo, accesando al objeto "currentStudent" sus propiedades de cursos y se almacena en una nueva variable String.
		// Se hace de esta manera porque luego ocuparemos estas variables coruse0, course1... dentro del "actionListener" cuando se presiona el boton
		// de guardar los cambios, ya que desde dentro del "actionListener" no podemos llamar al modelo. 
		String course0 = this.model.currentStudent.courses[0];
		String course1 = this.model.currentStudent.courses[1];
		String course2 = this.model.currentStudent.courses[2];
		String course3 = this.model.currentStudent.courses[3];
		String course4 = this.model.currentStudent.courses[4];
		String course5 = this.model.currentStudent.courses[5];
		String course6 = this.model.currentStudent.courses[6];
		
		// Al presionar el boto "Guardar" sucede lo siguiente...
		save0.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {

				// Hacemos uso del objeto dateFormat que se creo al puro principio de este metodo
				// pasamos la fecha seleccionada por el usuario en el "datePicker" y lo pasamos a un String texto "dd/MM/yyyy"
				String changeStartDate = dateFormat.format((Date) datePicker.getModel().getValue());
				
				// Creamos un nuevo String de texto que contendra toda la nueva informacion de estudiante que haya cambiado el usuario.
				// enterStudentName.getText() lee el nuevo nombre escrito por el usuario
				// changeStartDate escribe en el array la nueva fecha seleccionada por el usuario
				String newStudentInfo = (enterStudentName.getText() + "," + changeStartDate + "," 
				+ course0 + "," + course1 + "," + course2 + "," + course3 + "," 
				+ course4 + "," + course5 + "," + course6 + "," + "TRUE");

				// Ahora en nuestro archivo de texto escribimos todo el nuevo array con la nueva information
				// Se sobrescribe lo que sea que tuviera el archivo.
				try {
					save = new FileWriter("StudentsInformation.txt"); 
					save.write(newStudentInfo); 			      
					save.close(); // Es importante hacer "close" para decirle a la funcion que hemos terminado de escribir.
				} 
				catch (IOException c) {
					c.printStackTrace();
				}
				
				// Mostramos un mensaje de informacion para indicarle al usuario que debe reiniciar el programa para ver los cambios.
				JOptionPane.showMessageDialog(frame, "Reinicie el programa para aplicar los cambios", "Aviso", 2);
				
				// Ocultamos la ventana de preferencias
				frame.setVisible(false);
			}
		});	// Fin del action listener...
		
		
		// Al presionar el boton "Borrar todos los ajustes" sucede lo siguiente...
		eraseAllActivities.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				
				// Se despliega un cuadro de dialogo de confirmacion, para confirmar si el usuario desea borrar las actividades.
				int OptionSelected = JOptionPane.showConfirmDialog(frame, "Esta seguro que desea borrar todas las actividades?", "Confirmacion", 0, 0, null);
				
				// Si el usuario selecciona que si... osea 0 
				if(OptionSelected == 0) {
					try {
						// Sobreescribimos el archivo de texto que contiene todas las actividades
						save = new FileWriter("ActivitiesInformation.txt");
						// Se sobreescribe con ningun texto o su equivalente en codigo ""
						save.write(""); 			      
						save.close(); // Es importante hacer "close" para decirle a la funcion que hemos terminado de escribir.
						
						// Mostramos un mensaje de informacion para indicarle al usuario que debe reiniciar el programa para ver los cambios.
						JOptionPane.showMessageDialog(frame, "Reinicie el programa para aplicar los cambios", "Aviso", 2);
					} 
					catch (IOException c) {
						c.printStackTrace();
					}
				}
			}
		});
		
		// Agregamos a fields todas las etiquetas, cuadros de texto y seleccionadores de fecha
		fields.add(startDateLabel0);
		fields.add(datePicker);
		fields.add(blank0);
		fields.add(changeNameLabel);
		fields.add(enterStudentName);
		fields.add(blank1);
		
		// Agregamos a botones, todos los botones.
		buttons.add(save0);
		buttons.add(eraseAllActivities);
		
		// Para visualizar los botones en fila y comenzando a la izquiera de la pantalla.
		buttons.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		// Para visualizar todas las etiquetas, campos de texto y demas de manera vertical o sobre el eje Y
		fields.setLayout(new BoxLayout(fields, BoxLayout.Y_AXIS));
		
		// Agregamos al frame principal fields al inicio de la pagina.
		frame.add(fields, BorderLayout.PAGE_START);
		// Agregamos al frame principal los botones por default quedaran al final de la pagina.
		frame.add(buttons);
		
		// Damos unos parametros finales a nuestro frame o contenedor principal.
		frame.setLocationRelativeTo(null); 						// Pone la ventana en el centro de la pantalla.
		frame.setVisible(true);			   						// Hace que la ventana sea visible.
		frame.pack();					   			// Ajusta la ventana al tamano 

		// Establece lo que sucede si el usuario selecciona la X para cerrar ventana
		// en este caso NO termina la app sino que oculta la ventana hasta una nueva solicitud.
		frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
	}
	
	
	
	void read_studentInfo() throws ParseException { // Ver en el cuerpo de esta funcion porque se usa "ParseException" en Date date1
		
		try {
			// Creamos un nuevo objeto de tipo Scanner que utilizara el archivo en data osea "StudentsInformation.txt"
			// cada vez que usemos "readFile" dentro de esta funcion estaremos leyendo desde ese archivo .txt
			this.readFile = new Scanner(data);
		} catch (FileNotFoundException e) {
			
		}
		
		while(readFile.hasNextLine()) { // Mientras el archivo tenga linea de texto entonces...
			
			// Creamos una variable de tipo String y le asignamos la informacion que tiene el archivo de texto.
			String readingData = readFile.nextLine();

			// Toda la informacion en el archivo .txt se convierte en un array, delimitado por las comas.
			String[] studentInformationArray = readingData.split(",");

			// Convertir la fecha que es un String ubicado en el archivo .txt a tipo de dato Date
			// *** Esta linea necesita que la funcion principal tenga un throws ParseException
			// Si la informacion que se le pasa es invalida la funciona ataja la excepcion imprimiendola en consola
			Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(studentInformationArray[1]); // Sabemos que el Slot 1 siempre sera la fecha.

			// Pasarle al objeto student ya previamente creado desde el modelo su informacion, nombre, fecha de inicio, cursos.
			this.model.currentStudent.setName(studentInformationArray[0]);
			this.model.currentStudent.setStartDate(date1);

			// Se crea un nuevo array para separar unicamente las materias
			String studentSubjects[] = {studentInformationArray[2], studentInformationArray[3], 
										studentInformationArray[4], studentInformationArray[5], 
										studentInformationArray[6], studentInformationArray[7],
										studentInformationArray[8]};
			
			// Asignamos al estudiante sus cursos, usando el array previamente creado.
			this.model.currentStudent.setCourses(studentSubjects);

			// De haberse presionado "Submit" en la ultima linea de texto se almacenara un 1, lo sacamos de String y lo pasamos a boolean
			// para indicar que ya alguien anteriormente ingreso informacion, que ya existe un usuario.
			this.infoSubmitted = Boolean.parseBoolean(studentInformationArray[9]);
		}
		
	}
	
	void read_write_activites() throws ParseException {

		try {
			// Creamos un nuevo objeto de tipo Scanner que utilizara el archivo en data osea "ActivitiesInformation.txt"
			// cada vez que usemos "readFile1" dentro de esta funcion estaremos leyendo desde ese archivo .txt
			this.readFile1 = new Scanner(data1);
		} catch (FileNotFoundException e) {
		}
		
		// Creamos un String para posteriormente almacenar todo lo que contenga el archivo "ActivitiesInformation.txt"
		String readingData1;
		
		// Mientras el archivo tenga una nueva linea de texto entonces...
		while(readFile1.hasNextLine()) {
			// Se almacenar todo lo que contiene "ActivitiesInformation.txt" en esta variable
			readingData1 = readFile1.nextLine();  
			
			// Se crea un array con la misma cantidad de slots que punto y comas en la variable "readingData1"
			// Por cada ; se almacenara en un slot del array la info por ejemplo:
			// 18/08/2021,Redes I,Entregable,Prueba;20/10/2021,Progra II,Examen,Prueba;
			// activitiesInformationArray[0] = 18/08/2021,Redes I,Entregable,Prueba
			// activitiesInformationArray[1] = 20/10/2021,Progra II,Examen,Prueba
			// etc...
			activitiesInformationArray = readingData1.split(";");
		}
		
		try {
			// Una vez creado el array anterior, este siempre contendra tantos slots como actividades
			// entonces procedemos a asignar la cantidad actividades al modelo usando ".lenght" sobre el array.
			this.model.setActivitiesCounter((byte) activitiesInformationArray.length);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "¡No tiene actividades pendientes!", "Aviso", 3);
		}
		
		// Bucle for que se incrementara siempre que sea menor que la cantidad de actividades
		for(int i = 0; i < this.model.getActivitiesCounter(); i++) {
			
			// Una vez mas creamos un array esta vez usando como referencia las comas
			// Por ejemplo:
			// activitiesInformationArray[i] = 18/08/2021,Redes I,Entregable,Prueba
			
			// activitiesInformationArray[0] = 18/08/2021
			// activitiesInformationArray[1] = Redes I
			// activitiesInformationArray[2] = Entregable
			// activitiesInformationArray[3] = Detalles escritos por el usuario 
			String[] activityInformationArray = activitiesInformationArray[i].split(",");
			
			// Extraemos del slot 0 la fecha y la convertimos a String con formato "dd/MM/yyyy"
			Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(activityInformationArray[0]);
			
			// Procedemos a dar todas las variables al programa para que pueda utilizarlas
			// Recordar que Todo esto se extrajo del archivo de texto 
			
			// Siguiendo con el ejemplo
			this.model.activities[i].setDueDate(date1); 						   // 18/08/2021
			this.model.activities[i].setCourse(activityInformationArray[1]); 	   // Redes I
			this.model.activities[i].setCategory(activityInformationArray[2]); 	   // Entregable
			
			try {
				this.model.activities[i].setDetails(activityInformationArray[3]);  // Detalles escritos por el usuario
			} catch(Exception e) {
				this.model.activities[i].setDetails(""); 						   // Si el usuario no coloca ningun detalle en la actividad
			}																	   // esta se auto popula con ""
		}
	}


	@Override // Si la clase tuviera un exception se ejectura este metodo.
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	}

		
}
