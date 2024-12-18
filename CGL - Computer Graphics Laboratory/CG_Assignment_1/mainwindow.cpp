#include "mainwindow.h"
#include "ui_mainwindow.h"
QImage img(500,500,QImage::Format_RGB888);
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
    int x1,y1,x2,y2;

    x1 = ui->textEdit->toPlainText().toInt();
    y1 = ui->textEdit_2->toPlainText().toInt();
    x2 = ui->textEdit_3->toPlainText().toInt();
    y2 = ui->textEdit_4->toPlainText().toInt();

    DDA(x1,y1,x2,y2);

    ui->label->setPixmap(QPixmap::fromImage(img));
}

void MainWindow::DDA(int x1, int y1, int x2, int y2)
{
    if(x1 == x2 and y1 == y2){
        img.setPixel(int(x1),int(y1),qRgb(255,255,255));
    }
    else{
        float dx,dy,length,xinc,yinc,x,y;
        dx = x2-x1;
        dy = y2-y1;
        if(abs(dx) >= abs(dy)){
            length = abs(dx);
        }else{
            length = abs(dy);
        }
        xinc = dx/length;
        yinc = dy/length;
        int i = 0;
        x = x1;
        y = y1;
        while(i<length){
            img.setPixel(int(x),int(y),qRgb(255,255,255));
            x = x + xinc;
            y = y + yinc;
            i++;
        }
    }
}


void MainWindow::on_pushButton_2_clicked()
{
    int xc,yc,r;
    xc = ui->textEdit_5->toPlainText().toInt();
    yc = ui->textEdit_6->toPlainText().toInt();
    r = ui->textEdit_7->toPlainText().toInt();
    BCircle(xc,yc,r);
}

void MainWindow::BCircle(int xc, int yc, int r)
{
    int x = 0;
    int y = r;
    int d = 3-(2*r);

    do{
        img.setPixel(xc+x,yc+y,qRgb(255,255,255));
        img.setPixel(xc+y,yc+x,qRgb(255,255,255));

        img.setPixel(xc-x,yc+y,qRgb(255,255,255));
        img.setPixel(xc-y,yc+x,qRgb(255,255,255));

        img.setPixel(xc-x,yc-y,qRgb(255,255,255));
        img.setPixel(xc-y,yc-x,qRgb(255,255,255));

        img.setPixel(xc+x,yc-y,qRgb(255,255,255));
        img.setPixel(xc+y,yc-x,qRgb(255,255,255));

        x = x+1;
        if(d<0){
            d = d + 4*(x)+6;
        }else{
            y = y-1;
            d = d + 4*(x-y)+10;
        }

        ui->label->setPixmap(QPixmap::fromImage(img));
    }while(x<=y);
}

