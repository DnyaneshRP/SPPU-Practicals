#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>

QT_BEGIN_NAMESPACE
namespace Ui {
class MainWindow;
}
QT_END_NAMESPACE

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    MainWindow(QWidget *parent = nullptr);
    ~MainWindow();

private slots:
    // QT creator function definitions
    void on_pushButton_clicked();
    void on_pushButton_2_clicked();

    // Our function definitions
    void DDA(float, float, float, float);
    void mousePressEvent(QMouseEvent *event);

private:
    Ui::MainWindow *ui;
    bool start;
    int count, ver, temp, i, j , k, m, n;
    int a[100], b[100], xi[20];
    float dy, dx, slope[20];
};
#endif // MAINWINDOW_H
