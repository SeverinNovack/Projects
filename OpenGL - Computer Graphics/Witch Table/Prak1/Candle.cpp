#include "Candle.h"
#include <iostream> 
#include <GL/freeglut.h> //lädt alles für OpenGL
#include <GL/GLU.h>

extern float c1R;
extern float c2R;

void Candle() {

	//Halter
	glPushMatrix();
	glScalef(1, 0.1, 1);
	glColor3f(0.2f, 0.2f, 0.2f);//Dark grey
	glutSolidSphere(0.2, 32, 32);
	glPopMatrix();

	glPushMatrix();
	glRotatef(-90, 1, 0, 0);
	glColor4f(1.0f, 1.0f, 0.0f, 0.0f);//yellow
	GLUquadricObj* candleQuad;
	candleQuad = gluNewQuadric();      // Create A Pointer To The Quadric Object ( NEW )
	gluQuadricNormals(candleQuad, GLU_SMOOTH);  // Create Smooth Normals ( NEW )
	gluQuadricTexture(candleQuad, GL_TRUE);
	gluCylinder(candleQuad, 0.1f, 0.1f, 0.6f, 32, 32);
	glPopMatrix();

	glPushMatrix();
	glScalef(1, 0.1, 1);
	glTranslatef(0, 0.6, 0);
	glutSolidSphere(0.1, 32, 32);
	glPopMatrix();

	glPushMatrix();
	glRotatef(-90, 1, 0, 0);
	glTranslatef(0., 0, 0.6);
	glColor3f(1, 1, 1);
	GLUquadricObj* wickQuad;
	wickQuad = gluNewQuadric();      // Create A Pointer To The Quadric Object ( NEW )
	gluQuadricNormals(wickQuad, GLU_SMOOTH);  // Create Smooth Normals ( NEW )
	gluQuadricTexture(wickQuad, GL_TRUE);
	gluCylinder(wickQuad, 0.01f, 0.01f, 0.1f, 32, 32);
	glPopMatrix();

	glPushMatrix();
	glRotatef(c1R, 0, 0, 1);
	glRotatef(c2R, 0, 1, 0);
	glTranslatef(0, 0.75, 0);
	glScalef(1, 1.5, 1);
	glColor3f(0.9, 0, 0);
	glutSolidSphere(0.07, 32, 32);
	glPopMatrix();

	
}