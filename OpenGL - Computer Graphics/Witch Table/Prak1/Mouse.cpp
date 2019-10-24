#include "Mouse.h"

#include <iostream> 
#include <GL/freeglut.h> //lädt alles für OpenGL
#include <GL/GLU.h>

extern float tailR;
extern float headR;

void Mouse() {
	//Körper
	//glTranslatef(-1.7, 0.3, -1.7);
	//glTranslatef(-0.6, 0.5, -0.6);
	glPushMatrix();
	glScalef(1, 1, 2.4);
	//glRotatef(90, 0, 1, 0);
	glColor3f(0.2f, 0.2f, 0.2f);//Dark grey
	glutSolidSphere(0.18f, 25, 100);
	glPopMatrix();
	
	//Schwanz
	glPushMatrix();
	glRotatef(tailR, 0, 1, 0);
	glScalef(1, 1, 1.4);
	glTranslatef(0., 0., -0.6);
	glColor3f(0.737255, 0.560784, 0.560784);
	GLUquadricObj * tailQuad;
	tailQuad = gluNewQuadric();      // Create A Pointer To The Quadric Object ( NEW )
	gluQuadricNormals(tailQuad, GLU_SMOOTH);  // Create Smooth Normals ( NEW )
	gluQuadricTexture(tailQuad, GL_TRUE);
	gluCylinder(tailQuad, 0.01f, 0.02f, 0.3f, 32, 32);
	glPopMatrix();
	
	glRotatef(headR, 0, 1, 0);
	//Kopf
	glPushMatrix();
	glScalef(1, 1, 1.9);
	glTranslatef(0, 0, 0.3);
	glColor3f(0.2f, 0.2f, 0.2f);//Dark grey
	glutSolidSphere(0.1, 25, 100);
	glPopMatrix();
	//Nase
	glPushMatrix();
	//glRotatef(80, 1, 0, 0);
	glScalef(1, 1, 1.4);
	glTranslatef(0., 0., 0.4);
	glColor3f(0.737255, 0.560784, 0.560784);
	GLUquadricObj * noseQuad;
	noseQuad = gluNewQuadric();      // Create A Pointer To The Quadric Object ( NEW )
	gluQuadricNormals(noseQuad, GLU_SMOOTH);  // Create Smooth Normals ( NEW )
	gluQuadricTexture(noseQuad, GL_TRUE);
	gluCylinder(noseQuad, 0.05f, 0.01f, 0.3f, 32, 32);
	glPopMatrix();

}