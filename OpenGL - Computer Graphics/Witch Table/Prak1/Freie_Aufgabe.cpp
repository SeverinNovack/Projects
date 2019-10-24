// GD-Praktikum:   teil_1.cpp  (Teil 1: Start-Programm)
// Hergenroether / Groch    Last Update: 05.07.2014

#include <iostream> 
#include <GL/freeglut.h> //lädt alles für OpenGL
#include <GL/GLU.h>
#include "Wuerfel.h"
#include "Table.h"
#include "BallOfFortune.h"
#include "Mouse.h"
#include "Candle.h"
#include "Book.h"

char oldkey;

float bookR = 0;
int bookCount = 0;

float c1R = -2;
int c1Count = 0;
bool c1 = false;
float c2R = -10;
int c2Count = 0;
bool c2 = false;

float mouseRotation = 0;
float mTx = -4.8;
float mTy = -1.5;
float mTz = -7.5;
int count = 0;

float tailR = 7;
float headR = 7;
int tailCount = 0;
bool f = false;

bool down = true;
bool up = false;
bool left = false;
bool right = false;

bool keypressed = false;
bool rotate = false;

void Init()
{
	glEnable(GL_DEPTH_TEST);
	glEnable(GL_COLOR_MATERIAL);
	glEnable(GL_LIGHTING);
	glEnable(GL_LIGHT0);
	glEnable(GL_LIGHT1);
	glEnable(GL_LIGHT2);
	glEnable(GL_LIGHT3);
	glEnable(GL_LIGHT4);
	glEnable(GL_LIGHT5); 
	glEnable(GL_LIGHT6);
	glEnable(GL_LIGHT7);
	glEnable(GL_NORMALIZE);
	GLfloat light_ambient[] = { 0.3, 0.2, 0.2, 1.0 };
	GLfloat light_diffuse[] = { .6, .4, 0.4, 1.0 };
	GLfloat light_diffuse3[] = { .05, .05, 0.05, .0 };
	GLfloat light_specular[] = { 1.0, 1.0, 1.0, 1.0 };

	GLfloat light_position1[] = {-.5, 0.44, -1.7, 1.0 };
	GLfloat light_position3[] = { -.28, 0.44, -1.7, 1.0 };
	GLfloat light_position4[] = { -.36, 0.44, -1.8, 1.0 };
	GLfloat light_position5[] = { -.36, 0.44, -1.6, 1.0 };

	GLfloat light_position2[] = { .28, .44, -1.7, 1.0 };
	GLfloat light_position6[] = { .5, .44, -1.7, 1.0 };
	GLfloat light_position7[] = { .36, .44, -1.8, 1.0 };
	GLfloat light_position8[] = { .36, .44, -1.6, 1.0 };
	

	//Light1
	glLightfv(GL_LIGHT0, GL_DIFFUSE, light_diffuse);
	glLightfv(GL_LIGHT0, GL_POSITION, light_position1);

	//Light3
	glLightfv(GL_LIGHT2, GL_DIFFUSE, light_diffuse);
	glLightfv(GL_LIGHT2, GL_POSITION, light_position3);
	
	//Light4
	glLightfv(GL_LIGHT3, GL_DIFFUSE, light_diffuse3);
	glLightfv(GL_LIGHT3, GL_POSITION, light_position4);

	//Light5
	glLightfv(GL_LIGHT4, GL_DIFFUSE, light_diffuse3);
	glLightfv(GL_LIGHT4, GL_POSITION, light_position5);

	
	//Light2
	glLightfv(GL_LIGHT1, GL_DIFFUSE, light_diffuse);
	glLightfv(GL_LIGHT1, GL_POSITION, light_position2);

	//Light6
	glLightfv(GL_LIGHT5, GL_DIFFUSE, light_diffuse);
	glLightfv(GL_LIGHT5, GL_POSITION, light_position6);

	//Light7
	glLightfv(GL_LIGHT6, GL_DIFFUSE, light_diffuse3);
	glLightfv(GL_LIGHT6, GL_POSITION, light_position7);

	//Light8
	glLightfv(GL_LIGHT7, GL_DIFFUSE, light_diffuse3);
	glLightfv(GL_LIGHT7, GL_POSITION, light_position8);
	



	glClearDepth(1.0);
	// Hier finden jene Aktionen statt, die zum Programmstart einmalig 
	// durchgeführt werden müssen
}

void RenderScene() //Zeichenfunktion
{
	// Hier befindet sich der Code der in jedem Frame ausgefuehrt werden muss
	glLoadIdentity();   // Aktuelle Model-/View-Transformations-Matrix zuruecksetzen
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	glClearColor(0, 0, 0.0, 0);
	//gluLookAt(0, 0., 1., 0., 0., 0., 0., 1., 0.); //Front
	//gluLookAt(0, 0., -1., 0., 0., 0., 0., 1., 0.); //Back
	//gluLookAt(0, 1., 0., 0., 0., 0., 0., 0., -1.); //Top
	//gluLookAt(0, -1., 0., 0., 0., 0., 0., 0., 1.); //Bottom
	//gluLookAt(-2, 0., 0., 0., 0., 0., 0., 1., 0.); //Left
	//gluLookAt(1, 0., 0., 0., 0., 0., 0., 1., 0.); //Right
	gluLookAt(0., 0.65, 1., 0, 0, 0, 0, 1., 0.);
	
	Table();

	BallOfFortune();

	//Maus
	glPushMatrix();
	glScalef(0.4, 0.4, 0.4);
	glRotatef(mouseRotation, 0, 1, 0);
	glTranslatef(mTx, mTy, mTz);
	Mouse();
	glPopMatrix();
	
	//Kerzen
	glPushMatrix();
	glTranslatef(-1.2, -0.7, -2.6);
	Candle();
	glPopMatrix();

	glPushMatrix();
	glTranslatef(1.2, -0.7, -2.6);
	Candle();
	glPopMatrix();


	//glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	glPushMatrix();
	glScalef(1.3, 1.3, 1.3);
	glTranslatef(0, -0.4, .3);
	Book();
	glPopMatrix();

	glutSwapBuffers();
}

void KeyboardFunc(unsigned char key, int x, int y){
	GLfloat xMousePos = float(x);
	GLfloat yMousePos = float(y);
	std::cout << "key = " << key << "  " << x << ", " << y << std::endl;
	if (key == 'a' || key == 's' || key == 'd' || key == 'w') {
		right = false;
		left = false;
		up = false;
		down = false;
		if (key != oldkey) {
			rotate = true;
		}
		else {
			rotate = false;
		}
	}
	if (key == 'a') {
		if (oldkey == 'w') {
			float xtmp = mTx;
			float ztmp = mTz;
			mTx = -ztmp;
			mTz = xtmp;
		}
		else if (oldkey == 's' || oldkey == NULL) {
			float xtmp = mTx;
			float ztmp = mTz;
			mTx = ztmp;
			mTz = -xtmp;
		}
		else if (oldkey == 'd') {
			float xtmp = mTx;
			float ztmp = mTz;
			mTx = -xtmp;
			mTz = -ztmp;
		}
		keypressed = true;
		mouseRotation = 270;
		mTz = mTz + 0.2;
	}
	else if (key == 's') {
		if (oldkey == 'w') {
			float xtmp = mTx;
			float ztmp = mTz;
			mTx = -xtmp;
			mTz = -ztmp;
		}
		else if (oldkey == 'a') {
			float xtmp = mTx;
			float ztmp = mTz;
			mTx = -ztmp;
			mTz = xtmp;
		}
		else if (oldkey == 'd') {
			float xtmp = mTx;
			float ztmp = mTz;
			mTx = ztmp;
			mTz = -xtmp;
		}
		keypressed = true;
		mouseRotation = 0;
		mTz = mTz + 0.2;
	}
	else if (key == 'w') {
		if (oldkey == 'a') {
			float xtmp = mTx;
			float ztmp = mTz;
			mTx = ztmp;
			mTz = -xtmp;
		}
		else if (oldkey == 's' || oldkey == NULL) {
			float xtmp = mTx;
			float ztmp = mTz;
			mTx = -xtmp;
			mTz = -ztmp;
		}
		else if (oldkey == 'd') {
			float xtmp = mTx;
			float ztmp = mTz;
			mTx = -ztmp;
			mTz = xtmp;
		}
		keypressed = true;
		mouseRotation = 180;
		mTz = mTz + 0.2;
	}
	else if (key == 'd') {
		if (oldkey == 'w') {
			float xtmp = mTx;
			float ztmp = mTz;
			mTx = ztmp;
			mTz = -xtmp;
		}
		else if (oldkey == 's' || oldkey == NULL) {
			float xtmp = mTx;
			float ztmp = mTz;
			mTx = -ztmp;
			mTz = xtmp;
		}
		else if (oldkey == 'a') {
			float xtmp = mTx;
			float ztmp = mTz;
			mTx = -xtmp;
			mTz = -ztmp;
		}
		keypressed = true;
		mouseRotation = 90;
		mTz = mTz + 0.2;
	}
	else if (key == 'r') {
		mouseRotation = 0;
		mTx = -4.8;
		mTy = -1.5;
		mTz = -7.5;
		count = 0;
		down = true;
	}
	if (key == 'r') oldkey == NULL;
	else oldkey = key;
	// RenderScene aufrufen.
	glutPostRedisplay();
}

void Reshape(int width, int height)
{
	// Hier finden die Reaktionen auf eine Veränderung der Größe des 
	// Graphikfensters statt
	// Matrix für Transformation: Frustum->viewport
	glMatrixMode(GL_PROJECTION);
	// Aktuelle Transformations-Matrix zuruecksetzen
	glLoadIdentity();
	// Viewport definieren
	glViewport(0, 0, width, height);
	// Frustum definieren (siehe unten)
	gluPerspective(70., 1., 0.1, 20.0);


	// Matrix für Modellierung/Viewing
	glMatrixMode(GL_MODELVIEW);
}

void Animate(int value)
{
	// Hier werden Berechnungen durchgeführt, die zu einer Animation der Szene  
	// erforderlich sind. Dieser Prozess läuft im Hintergrund und wird alle 
	// 1000 msec aufgerufen. Der Parameter "value" wird einfach nur um eins 
	// inkrementiert und dem Callback wieder uebergeben. 
	//std::cout << "value=" << value << std::endl;

	//Book Animation
	bookR = bookR + 0.5; // Rotationswinkel aendern
	bookCount++;
	if (bookCount == 360) {
		bookCount = 0;
		bookR = 0;
	}



	//Candle Animation
	if (c1 == false) {
		c1R = c1R + 0.025;
		c1Count++;
		if (c1Count == 160) {
			c1Count = 0;
			c1 = true;
		}
	}
	else {
		c1R = c1R - 0.025;
		c1Count++;
		if (c1Count == 160) {
			c1Count = 0;
			c1 = false;
		}
	}

	c2R = c2R - 1.0; // Rotationswinkel aendern
	if (c2R <= 0.0) {
		c2R = c2R + 360.0;
	}
	
	
	//Mouse Animation
	if (f == false) {
		tailR = tailR - 0.1;
		headR = headR - 0.1;
		tailCount++;
		if (tailCount == 140) {
			f = true;
			tailCount = 0;
		}
	}
	else {
		tailR = tailR + 0.1;
		headR = headR + 0.1;
		tailCount++;
		if (tailCount == 140) {
			f = false;
			tailCount = 0;
		}
	}

	if (right == true) {
		mTz = mTz + 0.01;
		count++;
		if (count > 225 && count < 450) {
			mTy = mTy + 0.01;
		}
		if (count >= 450 && count < 675) {
			mTy = mTy - 0.01;
		}
	}
	else if (down == true || up == true || left == true) {
		mTz = mTz + 0.01;
		count++;
	}
	if (count == 900) {
		float xtmp = mTx;
		float ztmp = mTz;
		mTx = 0;
		mTz = 0;
		if (down == true) {
			mouseRotation += 90;
			mTx = -2.0;
			mTz = -4.8;
			down = false;
			right = true;
		}
		else if (right == true) {
			mouseRotation += 90;
			mTx = -4.8;
			mTz = -2;
			right = false;
			up = true;
		}
		else if (up == true) {
			mouseRotation += 90;
			mTx = -7.5;
			mTz = -4.8;
			up = false;
			left = true;
		}
		else if (left == true) {
			mouseRotation = 00;
			mTx = -4.8;
			mTz = -7.5;
			left = false;
			down = true;
		}
		count = 0;

		//down = false;

	}

	/*fRotation = fRotation - 1.0; // Rotationswinkel aendern
	if (fRotation <= 0.0) {
		fRotation = fRotation + 360.0;
	}*/
	// RenderScene aufrufen
	glutPostRedisplay();
	// Timer wieder registrieren - Animate wird so nach 10 msec mit value+=1 aufgerufen.
	int wait_msec = 10;
	glutTimerFunc(wait_msec, Animate, ++value);
}

int main(int argc, char** argv)
{
	glutInit(&argc, argv);                // GLUT initialisieren
	glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGB | GLUT_DEPTH);
	glutInitWindowSize(600, 600);         // Fenster-Konfiguration
	glutCreateWindow("Severin Novack");   // Fenster-Erzeugung
	glutDisplayFunc(RenderScene);         // Zeichenfunktion bekannt machen
	glutReshapeFunc(Reshape);
	glutKeyboardFunc(KeyboardFunc);
	// TimerCallback registrieren; wird nach 10 msec aufgerufen mit Parameter 0  
	glutTimerFunc(10, Animate, 0);
	Init();
	glutMainLoop();
	return 0;
}