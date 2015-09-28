# Everpobre paso a paso Curso

This project's repository has tags for each milestone (steps). So you can quickly advance to a certain step just doing a checkout of step/nn

```
$ git tag -l
step/1
step/2
step/3
...
```


## Presentar el problema que queremos solucionar
	
* Qué es Everpobre
* mostrar capturas pantalla
* crear el proyecto Everpobre en Android Studio
	* crear una nueva app android
		* project name: Everpobre
	* io.keepcoding.everpobre: empresa keepcoding.io
	* API 14 - API 22
	* sin activity inicial
* Añadir .gitignore
	* Podemos bajar un `.gitignore` de [http://gitignore.io]()
	* crear git rama desarrollo (usando SourceTree o desde línea de comandos)
	
* Añadir clase Application (__STEP 1__)
	* necesaria para inicializar el modelo, responder a situaciones de memoria baja, etc.
	* Crear clase extiende de Application, EverpobreApp
	* en el paquete de la App: io.keepcoding.everpobre
	* sobreescribir onCreate
	* quedarnos con una ref weak estática al contexto de la App
	* definir en AndroidManifest `android:name="com.agbotraining.everpobre.EverpobreApp"`
	* crear clase util.Constants para clave Logs de la App

* Remove auto generated header comments in Java files
Preferences > Editor > File and Code Templates > Includes (tab) > File Header

* Añadir librerías básicas
	* usar gradleplease
	* añadir ButterKnife

* separar temas de estilos en dos ficheros
* establecer el estilo de la App en el AndroidManifest.xml: 		android:theme="@style/Everpobre"

__ STEP 2 __

* __androidviews.com: controles para nuestra App__ ⚡️


* crear la estructura básica de menús y actividades vacías
	* crear MainActivity como launcher activity
	* crear la Actividad para añadir/editar una libreta: EditNotebookActivity
		* en paquete activities (com.agbotraining.everpobre.activities)
	* crear la Actividad para mostrar las notas de una libreta: ShowNotebookActivity
	* crear la Actividad para añadir/editar notas EditNotesActivity
* lanzar EditNotebookActivity desde menú 
	* refactorizar el menú de la MainActivity para cargar EditNotebookActivity desde menu_main.xml
	* añadir el Intent

__STEP 3__

## Acceso a BB.DD. con Android: SQLite

* SQLite: una base de datos en un fichero
* Formas de acceder a BB.DD. en Android
### El modelo de Everpobre
   	* Crear el Modelo de datos a mano
   		* Crear clase Notebook en paquete Model
   			* añadir campos, generar getters y setters, constructor con name
			   
```			   
private long id;
private String name;
private Date creationDate;
private Date modificationDate;
```			   
			   
   			* id debe ser long ([http://sqlite.org/autoinc.html]())  ⚡️
 			
   		* Crear clase Note en paquete Model
   			* añadir campos, generar getters y setters, constructor   			
			   * id debe ser long ([http://sqlite.org/autoinc.html]())

```
private long id;
private String text;
private Date creationDate;
private Date modificationDate;
private String photoUrl;
private Notebook notebook;
private double latitude;
private double longitude;
private boolean hasCoordinates;
private String address;
```
			   
		* Añadir constructor note
```
    public Note(Notebook notebook, String text) {
		this.notebook = notebook;
		this.text = text;
		this.creationDate = new Date();
	}
```			   
   		* ¡Hemos olvidado las relaciones!
			* añadir relación en Notebook: allNotes
			* añadir relación en Notes: notebook
			* añadir método para insertar una nota en notebook

```			
    public List<Note> allNotes() {
		return notes;
	}
	
	public void addNote(Note note) {
		notes.add(note);
	}
	
	public void addNote(String noteText) {
		Note note = new Note(this, noteText);
		addNote(note);
	}
```			
	    * ¿Dónde construir la lista de notas? 
			- Definición
			- Constructor
			- lazy getter		
			
			
__STEP 4__

   		* Crear el DB Helper (esqueleto, vacío) dentro del paquete model.db
		   * ¿usar contexto de la EverpobreApp en DB? Esta clase es reusable: nos acoplaríamos innecesariamente
				* añadir métodos para convertir datos a DBHelper (convertir entre tipos)
				* añadir a DBHelper getInstance para construirlo de forma estática (factory estático, que no singleton)
			* añadir scripts de creación de la DB a NotebookDAO y a DBHelper
				* no olvidar añadir FOREINGN KEY a parte N de la relación, para delete on cascade
				* añadir a DBHelper método onOpen para establecer el PRAGMA (delete on cascade)
		* Crear DBConstants

## DAOs

   		* crear NotebookDAO vacía en paquete com.agbotraining.everpobre.model.dao;
		   (ir pasando código de los pedazos)
		   
   				* insert:
   					* getWritableDatabase()
   					* opciones del INSERT
   				* update
   				* delete, deleteAll
   					* ociones delete
   				* query, queryCursor
   					* getReadableDatabase
   					* diferencias de query frente a execDB
   					* rawQuery
   				* añadir id a Notebook: debe ser Long: leer id en clase NotebookDAO
   				* añadir id a Note: debe ser Long		
   					
				* crear interfaz DAOPersistable para generalizar los DAOs
				* interfaz genérico, explicar
				
```
public interface DAOPersistable<T> {
    long insert(@NonNull T data);
    void update(long id, @NonNull T data);
    void delete(long id);
    void deleteAll();
    @Nullable Cursor queryCursor();
    T query(long id);
}

```
				
				* explicar uso de @NonNull, @Nullable
				* hacer weak el contexto
   				
				   
__STEP 5__
				   
## Probando el modelo de datos 

   	* Qué son las pruebas de unidad
   	* Tipos de pruebas de unidad: JUnit y Android Test Case
   	* tests en src > androidTest
   		* crear configuración ejecución para lanzar los tests
   		* escribir los tests de unidad
			* clase de test de unidad Android: AndroidTestCase
			* crear NotebookTests
			* explicar signatura métodos de test
			* lanzar proyecto de test: ver tests pasados
			* crear tests desde clase, añadir métodos desde test

		* Esto no es TDD
		* qué debemos probar: modelo en memoria, red y acceso a BD al menos
		* cómo debemos pensar para escribir los tests de unidad
			* primero pensar en cómo queremos que se comporten nuestras clases, luego "ejercitarlas" y comprobar que se comportan como esperamos
	* crear NotebookDAOTests
		* test podemos insertar
		* contar registros
		* extraer fichero sqlite del simulador y explorar
			* abrir perspectiva DDMS
			* pull fichero
			* usar SQLite Studio
		* test delete
			* un test no depende del resto. Explicar
		* test deleteAll
    * crear NoteDAO
   		* paradigma de la programación orientada al copy / paste
   		* cambiar constantes
   		* revisar: insert, query, queryAll, update, delete, deleteAll
	* escribir NoteDAO tests
   		* test fallan: falta por crear la BD en DBHelper
   			* problema: ya existe la BD, no va a "funcionar el onCreate"
   			* problema2: execSQL sólo permite ejecutar 1 statement
   			* solución: desinstalar la App, usando el simulador
    	
		
__STEP 6__		
		
## Mostrando un GridView con todos los Notebooks
	
* El control GridView

	* crear fragment (Android > Fragment > BlankFragment) DataGridFragment. No incluir interfaces u otro código!!!
		* crear clase DataGridFragment en paquete fragments
	* añadir gridview al Layout del fragment (fragment_datagrid.xml)
	* incorporar fragment a layout main activity (activity_main.xml)
	
```
<fragment android:name="io.keepcoding.everpobre.fragments.DataGridFragment"
        android:id="@+id/gridFragment"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:layout="@layout/fragment_data_grid" />

```
	
	
	* añadir iconos a drawable
		* crear carpeta drawable
		* bajar iconos y renombrarlos:
			* [https://www.iconfinder.com/icons/33987/document_files_folder_note_papers_icon#size=128]()
			* [https://www.iconfinder.com/icons/33986/blank_book_catalog_note_notebook_icon#size=128]()
			* [https://www.iconfinder.com/icons/33993/contract_document_signature_icon#size=128]()
		* copiar iconos a drawable
	* crear diseño vista personalizado para cada GridView: view_notebook.xml
		* relative layout: los campos no deben estar "en orden"
		* tools: namespace
		* extraer los estilos al fichero de styles
```

<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    xmlns:tools="http://schemas.android.com/tools">

    <TextView
        android:padding="5dp"
        android:id="@+id/txt_notebook_name"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_alignLeft="@+id/icon_notebook"
        android:layout_below="@+id/icon_notebook"
        android:layout_marginTop="14dp"
        tools:text="My test Notebook" />

    <ImageView
        android:id="@+id/icon_notebook"
        android:layout_width="80dp"
        android:layout_height="80dp"
        android:layout_alignParentLeft="true"
        android:layout_alignParentTop="true"
        android:layout_marginLeft="14dp"
        android:layout_marginTop="17dp"
        android:src="@drawable/notebook" />

</RelativeLayout>
```


	* Crear un Custom Cursor Adapter
		* qué es un adapter
		* BaseAdapter <--> CursorAdapter
		* añadimos CursorAdapter de android.support.v4.widget.CursorAdapter;
		* métodos principales:
			* public View newView(Context context, Cursor cursor, ViewGroup parent) 
			* public void bindView(View view, Context context, Cursor cursor) {
			* public View getView(int position, View convertView, ViewGroup parent) {
		* el adapter necesita conocer "cosas": los datos a mostrar y los controles donde mostrarlos. En lugar de ponerlos en el código, definimos un interfaz e inyectamos en el constructor el responsable de pasarnos esta información
		* esta clase podría testearse :-D
	* Si lanzamos la App, se carga la MainActivity, carga el fragmento con el GridView, pero no vemos nada: el Fragmento no tiene adapter (aún)

__STEP 7__		
		* Creando un GridView con vistas personalizadas
			* añadir Layout vista personalizada al adapter
		* Convert Fragment into generic fragment
			* añadir Listener
		* añadirmos una variable adapter
			* en onCreate del fragment, establecer el adapter
			
		* debemos cargar algunos Notebooks de prueba al iniciar MainActivity
		* en MainActivity necesitamos:
			* recuperar el Fragmento
			* obtener un cursor con todos los Notebooks
			* crear un adapter
		* el fragment se carga "solo" al estar en el layout de mainActivity
			* se hace en Hilo Main!
    	* si tocamos cualquier Notebook, crash! El listener está a Null
	
	
__STEP 8__		
	
## Añadir Notebooks
    
* Añadir Notebooks
	* repasar: la Actividad EditNotebookActivity se lanzaba desde main
	* añadir EditText al layout
	* añadir opción al menú edit para grabar
	* leer el texto escrito para poner el título del Notebook
	* al grabar, insertar en BD, cerrar
	* al volver, hay que actualizar el adapter: movemos el código de onCreate a onResume (método refreshData)
	* problemas: ¡no funciona! Depuramos con tests
	
	
__STEP 9__			

---	
	
* Editar Notebooks
	* al hacer un long click deberíamos editar el Notebook
	* ahora mismo provoca un crash!
		* ver DataGridFragment: usamos el listener sin comprobar si es null :-(
		* añadir código defensivo
		* establecer el listener 
	* añadir el evento de longClickListener
	* pasar el id que obtenemos en el evento a la actividad
	* lanzar misma EditNotebookActivity, pasando en el Intent el id
	* en EditNotebookActivity, obtenemos el id y leemos el registro, para añadir / editar
	    * cuándo grabar los datos de la BD

__STEP 10__			


## Mandamiento: no accederás a DB en el hilo main

* ANR: Android Not Responding y bases de datos
	* El diálogo que debemos siempre evitar.
    * Lanzar tareas en segundo plano con AsyncTask _repaso_
    * podemos usar AsyncTask a mano, perfecto para bases de datos que no se se van a usar fuera de nuestra App
    * Ponerlo como ejercicio para el lector
    	* solución en ServerTracker

    	
### Content providers
    	
* Crear nuestro Content provider
	* qué es un Content Provider
	* ¿Por qué lo necesitamos si ya tenemos los DAO? --> para usar un Cursor Loader
	* registrar content provider en Manifest
	`<provider android:name=".provider.EverpobreProvider" android:authorities="com.agbotraining.everpobre.provider"/>` 
	* definir URI provider y principales (NOTEBOOKS y NOTES)
	* añadir UriMatcher
	* escribir onCreate
	* insert
	* query
	* delete
	* update
	* getType
	* Escribir nuevos tests de unidad del content provider
	* añadir métodos estáticos de conveniencia
	* añadir tests de estos métodos estáticos 

### Cursor Loaders
* Usar Cursor Loader
	* implementar el interfaz LoaderManager.LoaderCallbacks<Cursor>
	* escribir 3 métodos loader
	

    
* Listar todas las notas de un Notebook
	* añadir onItemClickListener a GridView 
	* lanzar actividad ShowNotebookActivity desde MainActivity
	* mostrar notas en un GridView	
	
## Añadir notas
 
* añadir opción "+" para crear una nueva nota
	* copiar opción de menú del menú principal al de shownotebookactivity
	* añadir codigo para cargar el menú. Cambiar el menú que se carga
	* copiar codigo de onOptionsItemSelected
* + nos lleva a EditNoteActivity
* para poder crear / editar una nota necesitamos el notebook en el que está. Si estamos añadiendo pasaremos el notebook, si estamos editando pasaremos directamente el id de la nota.

	* añadiendo pasamos id del notebook 


	
* crear layout EditNoteActivity (no es un fragment)
	* vamos a crear un fragment PhotoChooserFragment reusable con:
		* imageView para mostrar foto 
		* button para añadir de la librería / sacar foto
		* al volver de seleccionar la foto, asignamos o bien grabamos la foto
		* Uso de la cámara
			* Usando Intents implícitos para (repaso):
    			* hacer llamadas
    			* abrir un PDF
    			* abrir una página web
    	* Usar intents para obtener una imagen de la Biblioteca de imágenes
    	* Usar intents para capturar una foto con la cámara
    	* Guardar las fotos en almacenamiento externo SD
    	* Mostrar las fotos en las notas en ImageView
		* este fragmento debe tener un listener onPhotoSelectedFromLibrary, onPhotoTakenWithCamera que nos informe	de que la foto se ha tomado / seleccionado
		    * Guardar las URLs de las fotos en BD

	* Layout de la activity (xml)
		* campo de texto para text de la nota
		* fragmento
	* Añadir permisos a manifest para poder usar la cámara y grabar en SD
	* <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
<uses-permission android:name="android.permission.CAMERA"/>
		* en la EditNoteActivity, al recibir mensajes listener de foto tomada, quedarnos con los datos
		* distinguir al arrancar si estamos en modo añadir o en modo editar
			* grabar nueva nota (si estamos en modo añadir)
			* editar la actual (si no)
		* añadir a EditNoteActivity opción menú de grabar
	
	
---
	
* Añadiendo notas
	* Texto Editable y Teclado
	* EditText
	* Listeners del teclado
		* poner longitud de la nota a medida que se añade con addTextChangedListener
		* capturar enter cambiado a Siguiente para ocultar el teclado
	* cambiar layout
* reproducir efectos de sonido en Android
	* al borrar una nota hacer un sonido
	* añadir opción de menú borrar nota
		* si estamos añadiendo desabilitamos esta opción de menú
	* añadir código para borrar en EverpobreProvider deleteNote
	* añadir código borrar en nota
	* añadir sonido al proyecto (free sound org)
		* http://www.freesound.org/people/stomachache/sounds/44871/
		* creative commons 0
		* cambiar nombre a fichero (reglas de recursos)
		* crear carpeta raw
		* añadir código
* cambiar animaciones por defecto al cambiar de Activity
    * crear carpeta anim en res Eclipse
    * copiar animaciones a carpeta anim
    * overridePendingTransition(R.anim.push_up_in, R.anim.push_up_in); al añadir un notebook

---


## Actualizando nuestro proyecto


* Actualizando nuestro proyecto
	* añadir información de geolocalización
		* añadir lat y long a objetos notas (modelo, como doubles)
		* añadir address string
		* si no hay lat / long hay que indicarlo de alguna manera (usar un flag para no pintar los pins en medio del mar: has coordinates
		* en Dao, añadirlo a claves, allColumns, create table, getContentValues, noteFromCursor
	
		* añadir permisos ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION
		* obtener lat y long al crear las notas
		* obtener dirección a partir posic GPS para añadir a la nota
	* actualizar DAOs, actualizar content provider, actualizar DBHelper
	* Migración entre distintas versiones de la B.D.
		* usar switch con fall-through
		* cambiar DATABASE_VERSION
		* un statement por update!

# Uso de Mapas de Google en nuestra App
* ver las notas en forma de libreta o en un mapa
	* añadir opción de menú para lanzar el mapa en main
		
		
		
	* Crear MapActivity (vacía)
	* lanzar desde Main
* comprobar que tenemos Google Play Services bajado
* importar proyecto de google play services google-play-services-lib (Importar)
* añadir librería  google maps como referencia Android al proyecto 
	
    * Cómo registrar las API keys
		* registrar en Google las APIs, generando las firmas por consola
		* 4D:0B:CB:E7:7F:10:50:09:F0:B6:19:BA:D1:E1:79:DB:79:90:53:FD;com.agbotraining.everpobre
		* Añadir a manifest (dentro del tag <application>)
		
		// Añadir a AndroidManifest.xml dentro de <application>
 	<!-- Google Maps API Key -->
    <meta-data
        android:name="com.google.android.maps.v2.API_KEY"
        android:value="" />


	<meta-data
       android:name="com.google.android.gms.version"
       android:value="@integer/google_play_services_version" />
    

    * Añadir permisos a Manifest. Quitar los redudantes

    * Añadimos al layout de activity_map el fragment del mapa

    	* OJO: SupportMap


    * Poniendo pins en el mapa




# Evernote API

* Integración con Evernote
    * http://dev.evernote.com/doc/start/android.php

    
http://dev.evernote.com/doc/

- crear una cuenta de desarrollador en evernote Sandbox: https://sandbox.evernote.com/Login.action?targetUrl=%2FHome.action
- recibiremos un correo de confirmación de Evernote
- sandbox.evernote.com
- get an API key (nivel basic de acceso)
- - hay diferentes niveles de acceso: http://dev.evernote.com/doc/articles/permissions.php

Consumer Key: infos
Consumer Secret: 63146f1fb373923d

## Bajar el SDK de Evernote
- https://github.com/evernote/evernote-sdk-android/
- bajar como un zip / con git
- importar el proyecto library

## Modificar Android Manifest

<uses-permission android:name="android.permission.INTERNET" />

<activity android:name="com.evernote.client.android.EvernoteOAuthActivity" 
            android:configChanges="orientation|keyboardHidden" />
            
- Añadir a Application un evernote session
- añadir a main menu opción evernote
- lanzar activity evernote auth desde opción menú
- añadir activityForResult
- añadir opción para listar todos los notebooks de evernote
- listar los notebooks en Toast             
            
# Multihilo

* programación multihilo en Android con Hilos Java
	* bloquear hilo main bajando una imagen y bajarla con un hilo
	* handler + post message para cargar imágenes, Looper
		* mejorar la app de vinos
		* public final void runOnUiThread (Runnable action)
	* detectar cuándo estamos en el hilo main y usar asserts

---


# Servicio

* Crear un servicio que nos avise de si estamos en la zona GPS donde se creó una nota
* el servicio se arrancará con la App.
* al iniciarse, imprime una notificación
	* esta notificación contiene un pending intent para lanzar nuestra pantalla main
* acceso al servicio a través de la clase de la App
* parar el servicio desde una opción de menú

* cada 5 seg el servicio comprueba y si estamos dentro de un radio de 1000 m de una nota nos avisa.
	* posteando notificaciones
		* mensajes en el área de notificaciones
		* encendiendo los LEDs del dispositivo
    

    OLVIDADO: al actualizar un registro, cambiar fecha de actualización, ordenar por fecha de actualización


## ANEXO A: ADB DESDE CONSOLA    

* Usando ADB desde consola
	* configurar ADB para que esté en el PATH
		* Mac
		* Windows
	* instalar y desinstalar Apps con adb
	* acceder a logcat
	* iniciar sesión en el dispositivo o emulador con adb shell
	* push / pull ficheros desde el dispositivo
	* activar asserts: adb shell setprop debug.assert 1

    	 

## ANEXO B: Detectar si hay conexión a Internet


http://stackoverflow.com/questions/6493517/android-detect-if-device-has-internet-connection    	 


## Mejoras:

- usar el método de la clase application que devuelve el contexto de la app en DBHelper.getInstance() ?
- poner títulos a Actividades, la de su Notebook setTitle("Hello");

