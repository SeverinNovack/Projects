#include "Book.h"
#include <iostream> 
#include <GL/freeglut.h> //lädt alles für OpenGL
#include <GL/GLU.h>

extern float bookR;

void Book() {
	glPushMatrix();
	glRotatef(3, 1, 0, 0);
	glPushMatrix();
	glTranslatef(0, -0.14, 0);
	glPushMatrix();
	glScalef(2, 0.2, 1.8);
	glColor3f(0.396, 0.263, 0.129);
	glutSolidCube(0.5);
	glPopMatrix();
	float s = 1.7;
	float t = 0.05;
	for (int i = 0; i < 10; i++) {
		glPushMatrix();
		glTranslatef(0, t, 0);
		glScalef(s, 0.05, 1.6);
		glColor3f(0.8667, 0.498, 0.1647);
		glutSolidCube(0.5);
		glPopMatrix();
		s = s - 0.05;
		t = t + 0.01;
	}

	/*glPushMatrix();
	glRotatef(90, 0, 0, 1);
	glTranslatef(0.2, 0, 1);
	glScalef(s, 0.05, 1.6);
	glColor3f(0.8667, 0.498, 0.1647);
	glutSolidCube(0.2);
	glPopMatrix();*/
	glPopMatrix();

	glPushMatrix();
	glRotatef(bookR, 0, 0, 1);
	glTranslatef(0.11, 0., 0);
	glScalef(1.6, 0.08, 3.8);
	glColor3f(0.8667, 0.498, 0.1647);
	glutSolidCube(0.18);
	glPopMatrix();
	glPopMatrix();
	


}