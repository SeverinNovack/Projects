#include "Table.h"

#include <iostream> 
#include <GL/freeglut.h> //lädt alles für OpenGL
#include <GL/GLU.h>

void Table() {

	glScalef(1.5, 1.5, 1.5);
	//Tischbein1
	glPushMatrix();
	glTranslatef(0, -0.2, 0);
	glColor4f(0.7f, 0.3f, 0.0f, 0.0f);//orange/brown
	glScalef(0.4, 2, 0.4);
	//glRotatef(-90, 1, 0, 0);
	glTranslatef(1., -0.2, 0.);
	glutSolidCube(0.4);
	glPopMatrix();
	
	//Tischbein2
	glPushMatrix();
	glTranslatef(0, -0.2, 0);
	glColor4f(0.7f, 0.3f, 0.0f, 0.0f);//orange/brown
	glScalef(0.4, 2, 0.4);
	//glRotatef(-90, 1, 0, 0);
	glTranslatef(-1., -0.2, 0.);
	glutSolidCube(0.4);
	glPopMatrix();

	//Tischbein3
	glPushMatrix();
	glTranslatef(0, -0.2, 0);
	glColor4f(0.7f, 0.3f, 0.0f, 0.0f);//orange/brown
	glScalef(0.4, 2, 0.4);
	//glRotatef(-90, 1, 0, 0);
	glTranslatef(1., -0.2, -1.);
	glutSolidCube(0.4);
	glPopMatrix();

	//Tischbein4
	glPushMatrix();
	glTranslatef(0, -0.2, 0);
	glColor4f(0.7f, 0.3f, 0.0f, 0.0f);//orange/brown
	glScalef(0.4, 2, 0.4);
	//glRotatef(-90, 1, 0, 0);
	glTranslatef(-1., -0.2, -1.);
	glutSolidCube(0.4);
	glPopMatrix();

	//Tischplatte
	glPushMatrix();
	glTranslatef(0, -0.2, 0);
	glColor4f(0.7f, 0.3f, 0.0f, 0.0f);//orange/brown
	glScalef(2.4, 0.3, 2.4);
	//glRotatef(-90, 1, 0, 0);
	glTranslatef(0., 0., -0.1);
	glutSolidCube(0.4);
	glPopMatrix();
}
