/*
CG Practical - 7
Write OpenGL program to show bouncing ball animation
g++ -o cgl7 cgl7.cpp -lGL -lGLU -lglut
./cgl7
*/

#include <GL/glut.h>

float y = 0.0f;
float dy = 0.001f; // Reduced velocity for slower movement

void display() {
    glClear(GL_COLOR_BUFFER_BIT);
    glLoadIdentity();

    // Draw the ball
    glColor3f(1.0f, 0.0f, 0.0f); // Red color
    glTranslatef(0.0f, y, 0.0f);
    glutSolidSphere(0.1f, 20, 20);

    // Update ball position
    y += dy;
    if (y >= 1.0f || y <= -1.0f) {
        dy = -dy; // Reverse direction on collision
    }

    glFlush();
}

int main(int argc, char** argv) {
    glutInit(&argc, argv);
    glutInitDisplayMode(GLUT_SINGLE | GLUT_RGB);
    glutInitWindowSize(500, 500);
    glutCreateWindow("Bouncing Ball");

    glutDisplayFunc(display);
    glutIdleFunc(display); // Continuously redraw

    glutMainLoop();
    return 0;
}