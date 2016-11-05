#include <SoftwareSerial.h>
#include <Servo.h>

int servoPin = 9;
Servo servo;

// initialize bluetooth
int bluetoothTxPin = 10;
int bluetoothRxPin = 11;
SoftwareSerial bluetooth(bluetoothTxPin, bluetoothRxPin);

int serialBaudRate = 9600;

void setup()

{
  // attach to servo, bluetooth and serial
  servo.attach(servoPin);
  bluetooth.begin(serialBaudRate);

  // logging output, e.g. usb
  Serial.begin(serialBaudRate);
}

void loop()
{
  if (bluetooth.available() > 0 ) // if there is bytes to read in bluetooth serial buffer
  {
    int servopos = bluetooth.read();
    servo.write(servopos);

    // log position to serial output
    Serial.println(servopos);
  }
}

