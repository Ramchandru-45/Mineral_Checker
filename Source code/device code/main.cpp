#include <Arduino.h>
#include <WiFi.h>

//WiFi Configuration
const char* ssid="ESP32-MC-DEVICE";
const char* password="ESP32-1234";


// WiFi client and Application client
WiFiClient app_client;

const int VoltPin=32; // Pin to read voltage

// Intialize WiFi
void Start_Wifi(){
  WiFi.mode(WIFI_AP);
  WiFi.softAP(ssid, password, 1 , 0, 1);
  Serial.println("WiFi AP started");
}

// Connect to the pplication
void AppConnect(){
  Serial.println("Waiting for application to connect...");
  while(app_client.connected() == false)
  {
    app_client.connect("192.168.4.2", 80);
  }

  if(app_client.connected())
  {
    Serial.println("Application conected");
  }
  else
  {
    Serial.println("Application not connected");
    app_client.stop();
  }
}

// Function to read volatage
int getVolt() {
  return analogRead(VoltPin);
}

// Setup (first function to run)
void setup(){
  Serial.begin(115200);
  pinMode(VoltPin,OUTPUT);
  Start_Wifi();
}

// Called repeatedly after setup
void loop(){
  AppConnect();
  while(app_client.connected()){
    if(app_client.available()){
      int message = app_client.read();
      if(message == 1){
        int i = 100;
        while(i-- > 0){
          int data = getVolt();
          Serial.println(data);
          app_client.print(data);
          app_client.print(',');
          delay(50);
        }
        app_client.write(-1);
        app_client.flush();
        Serial.println("Data sent");
      }
    }
  }
  app_client.stop();
}

// End of Program