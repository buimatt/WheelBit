#include <CurieBLE.h>
#include <SoftwareSerial.h>

BLEPeripheral blePeripheral;
BLEService photogateService("19B10000-E8F2-537E-4F6C-D104768A1214");
BLEUnsignedCharCharacteristic revolution("19B10001-E8F2-537E-4F6C-D104768A1214", BLERead | BLEWrite);

const int photoTran = 13;
const int vcc = 12;
const int ledIndicator = 10;
SoftwareSerial serial(12, 13);
int reading = 0;

void setup(){  
  Serial.begin(9600);
  pinMode(photoTran,INPUT);
  blePeripheral.setLocalName("WheelBit");
  blePeripheral.setDeviceName("WheelBit");
  blePeripheral.setAdvertisedServiceUuid(photogateService.uuid());
  blePeripheral.addAttribute(photogateService);
  blePeripheral.addAttribute(revolution);
  blePeripheral.begin();
  //pinMode(ledIndicator, OUTPUT);
  digitalWrite(vcc,HIGH);

}

void loop(){
  BLECentral central = blePeripheral.central();
  reading = digitalRead(photoTran);
  if (central.connected()) {
    digitalWrite(ledIndicator, HIGH);
  } else {
    digitalWrite(ledIndicator, LOW);
  }
  revolution.setValue(digitalRead(13));
  Serial.println(reading);
  serial.println(reading);
}
