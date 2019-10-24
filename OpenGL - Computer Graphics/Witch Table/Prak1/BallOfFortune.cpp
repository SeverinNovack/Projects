#include "BallOfFortune.h"

#include <iostream> 
#include <GL/freeglut.h> //lädt alles für OpenGL
#include <GL/GLU.h>

void BallOfFortune() {
	//Zauberkugel

	glScalef(0.2, 0.2, 0.2);
	//Kugel
	glPushMatrix();
	glScalef(0.7, 0.7, 0.7);
	glTranslatef(0., -0.15, -1.5);
	glColor4f(1, 1, 1, 1);
	glutSolidSphere(0.4, 25, 100);
	glPopMatrix();

	//Halter
	glPushMatrix();
	//glScalef(0.5, 0.9, 0.9);
	glTranslatef(0., 0., -1.05);
	glRotatef(90, 1, 0, 0);
	glTranslatef(0., 0., 0.3);
	glColor4f(1, 0, 1, 1);
	GLUquadricObj * IDquadric;
	IDquadric = gluNewQuadric();      // Create A Pointer To The Quadric Object ( NEW )
	gluQuadricNormals(IDquadric, GLU_SMOOTH);  // Create Smooth Normals ( NEW )
	gluQuadricTexture(IDquadric, GL_TRUE);
	gluCylinder(IDquadric, 0.2f, 0.3f, 0.2f, 32, 32);	
	glPopMatrix();
}