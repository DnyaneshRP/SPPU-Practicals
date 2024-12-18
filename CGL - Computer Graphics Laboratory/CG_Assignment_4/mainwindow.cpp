#include "mainwindow.h"
#include "ui_mainwindow.h"
#include <math.h>
#include<cmath>
#include <iostream>
#include <cstdlib>
using namespace std;

QImage img(400,400,QImage::Format_RGB888);

MainWindow::MainWindow(QWidget *parent)
    : QMainWindow(parent)
    , ui(new Ui::MainWindow)
{
    ui->setupUi(this);
}

MainWindow::~MainWindow()
{
    delete ui;
}

void MainWindow::on_pushButton_clicked()
{
    DDA(200,0,200,400);
    DDA(0,200,400,200);
}


void MainWindow::on_pushButton_2_clicked()
{
    float x1, y1, x2, y2, x3, y3;
    x1 = ui->textEdit->toPlainText().toFloat();
    y1 = ui->textEdit_2->toPlainText().toFloat();
    x2 = ui->textEdit_3->toPlainText().toFloat();
    y2 = ui->textEdit_4->toPlainText().toFloat();
    x3 = ui->textEdit_5->toPlainText().toFloat();
    y3 = ui->textEdit_6->toPlainText().toFloat();
    DDA(200+x1, 200+y1, 200+x2, 200+y2);
    DDA(200+x1, 200+y1, 200+x3, 200+y3);
    DDA(200+x3, 200+y3, 200+x2, 200+y2);
    ui->label->setPixmap(QPixmap::fromImage(img));
}


void MainWindow::on_pushButton_3_clicked()
{
    float x1, y1, x2, y2, x3, y3, tx, ty;
    x1 = ui->textEdit->toPlainText().toFloat();
    y1 = ui->textEdit_2->toPlainText().toFloat();
    x2 = ui->textEdit_3->toPlainText().toFloat();
    y2 = ui->textEdit_4->toPlainText().toFloat();
    x3 = ui->textEdit_5->toPlainText().toFloat();
    y3 = ui->textEdit_6->toPlainText().toFloat();
    tx = ui->textEdit_7->toPlainText().toFloat();
    ty = ui->textEdit_8->toPlainText().toFloat();
    DDA(200+x1+tx, 200+y1+ty, 200+x2+tx, 200+y2+ty);
    DDA(200+x1+tx, 200+y1+ty, 200+x3+tx, 200+y3+ty);
    DDA(200+x3+tx, 200+y3+ty, 200+x2+tx, 200+y2+ty);
    ui->label->setPixmap(QPixmap::fromImage(img));
}


void MainWindow::on_pushButton_4_clicked()
{
    float x1, y1, x2, y2, x3, y3, sx, sy;
    x1 = ui->textEdit->toPlainText().toFloat();
    y1 = ui->textEdit_2->toPlainText().toFloat();
    x2 = ui->textEdit_3->toPlainText().toFloat();
    y2 = ui->textEdit_4->toPlainText().toFloat();
    x3 = ui->textEdit_5->toPlainText().toFloat();
    y3 = ui->textEdit_6->toPlainText().toFloat();
    sx = ui->textEdit_9->toPlainText().toFloat();
    sy = ui->textEdit_10->toPlainText().toFloat();
    DDA(200+(x1*sx), 200+(y1*sy), 200+(x2*sx), 200+(y2*sy));
    DDA(200+(x1*sx), 200+(y1*sy), 200+(x3*sx), 200+(y3*sy));
    DDA(200+(x3*sx), 200+(y3*sy), 200+(x2*sx), 200+(y2*sy));
    ui->label->setPixmap(QPixmap::fromImage(img));
}


void MainWindow::on_pushButton_5_clicked()
{
    float x1, y1, x2, y2, x3, y3;
    x1 = ui->textEdit->toPlainText().toFloat();
    y1 = ui->textEdit_2->toPlainText().toFloat();
    x2 = ui->textEdit_3->toPlainText().toFloat();
    y2 = ui->textEdit_4->toPlainText().toFloat();
    x3 = ui->textEdit_5->toPlainText().toFloat();
    y3 = ui->textEdit_6->toPlainText().toFloat();
    deg = ui->textEdit_11->toPlainText().toInt();
    rad = float(deg*(M_PI/180));
    float X1, Y1, X2, Y2, X3, Y3;

    X1 = x1 * cos(rad) - y1 * sin(rad);
    Y1 = x1 * sin(rad) + y1 * cos(rad);
    X2 = x2 * cos(rad) - y2 * sin(rad);
    Y2 = x2 * sin(rad) + y2 * cos(rad);
    X3 = x3 * cos(rad) - y3 * sin(rad);
    Y3 = x3 * sin(rad) + y3 * cos(rad);
    DDA(200+X1, 200+Y1, 200+X2, 200+Y2);
    DDA(200+X2, 200+Y2, 200+X3, 200+Y3);
    DDA(200+X3, 200+Y3, 200+X1, 200+Y1);
    ui->label->setPixmap(QPixmap::fromImage(img));
}

void MainWindow::DDA(float x1, float y1, float x2, float y2)
{
    float dx, dy, length, xinc, yinc, x, y, i;
    dx = x2 -x1;
    dy = y2 - y1;
    if(abs(dx) >= abs(dy))
    {
        length = abs(dx);
    }
    else
    {
        length = abs(dy);
    }
    xinc = dx/length;
    yinc = dy/length;

    x=x1;
    y=y1;
    i=0;
    while(i<length)
    {
        img.setPixel(x,y,qRgb(255,255,255));
        x = x + xinc;
        y = y + yinc;
        i++;
    }
    ui->label->setPixmap(QPixmap::fromImage(img));
}
