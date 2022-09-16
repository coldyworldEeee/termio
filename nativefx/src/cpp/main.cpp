#include <QApplication>

#include "nativefxserver.hpp"
#include "terminalemulator.h"

namespace nfx = nativefx;

const char* APP_NAME = "terminal emulator";

int main(int argc, char* argv[]) {
  int qArgc = 3;
  //  char* qArgv[] = {
  //      APP_NAME,  // app name
  //      //      "--ignore-gpu-blacklist",  // ignore blacklist
  //  };

  QApplication app(qArgc, argv, false);
  QImage* image = NULL;
  QWidget* prevEvtTarget = NULL;
  QPoint prevP;

  TConsole::TerminalEmulator emulator;

  auto qtRedraw = [&image, &emulator](std::string const& name,
                                      uchar* bufferData, int w, int h) {
    if (image == NULL) {
      qDebug() << "Redraw width: " << w << ", height: " << h;
      image = new QImage(bufferData, w, h, w * 8,
                         QImage::Format_ARGB32_Premultiplied);
      emulator.resize(w, h);
      emulator.requestRedrawImage(image);
    }
  };

  auto qtResized = [&image, &emulator](std::string const& name,
                                       uchar* bufferData, int w, int h) {
    if (image == NULL || emulator.width() != w || emulator.height() != h) {
      qDebug() << "Resize width: " << w << ", height: " << h;
      delete image;
      image = new QImage(bufferData, w, h, w * 8,
                         QImage::Format_ARGB32_Premultiplied);
      emulator.resize(w, h);
      emulator.requestRedrawImage(image);
    }
  };

  auto evt = [&emulator, &prevEvtTarget, &prevP](std::string const& name,
                                                 nfx::event* evt) {};

  nfx::SharedCanvas* canvas = nfx::SharedCanvas::create("_emulator_mem");

  auto nativeRedrawCallback = [&canvas, &qtRedraw, &qtResized, &evt]() {
    canvas->processEvents(evt);
    canvas->draw(qtRedraw, qtResized);
  };
  emulator.setNativeRedrawCallback(nativeRedrawCallback);

  emulator.resize(1280, 800);
  emulator.initialize();
  emulator.setBackgroundColor(QColor(0x15, 0x15, 0x15));
  emulator.setForegroundColor(QColor(0xE1, 0xE1, 0xE1));
  emulator.setBlinkingCursor(true);
  emulator.installEventFilter(&emulator);
  emulator.show();

  if (argv[1]) {
    QString param = QString(argv[1]);
    QStringList li = param.split("=");
    if (li.size() == 2 && li[0] == "--with-text") {
      QString content = "";
      for (int i = 0; i < li[1].toInt(); i++) {
        content.append(
            "]0;root@joezane:~[root@joezane ~]#\r\n"
            "[0m[01;36mbin[0m   [01;34mdata[0m  [01;34metc[0m   "
            "[01;36mlib[0m      "
            "[01;34mlost+found[0m  [01;34mmnt[0m    [01;34mproc[0m     "
            "\r\n"
            "[01;34mroot[0m  [01;36msbin[0m  [01;34msys[0m   [01;34musr"
            "[0m[01;34mboot[0m  [01;34mdev[0m   [01;34mhome[0m  "
            "[01;36mlib64[0m  [01;34mmedia[0m\r\n");
      }
      emulator.sendText(content);
    }
  }
  return app.exec();
}
