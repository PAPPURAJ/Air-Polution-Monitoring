
#include "FirebaseESP8266.h"
#include <ESP8266WiFi.h>



#define FIREBASE_HOST "airpol-55410-default-rtdb.firebaseio.com"  
#define FIREBASE_AUTH "G9Y0cJoPQr8x9aeZgpJExnGbw4UC3A4fvom0hJHt"

#define WIFI_SSID "AirPollution"     
#define WIFI_PASSWORD "12345678" 


FirebaseData firebaseData,loadData;
FirebaseJson json;



String readData(String field){
  
if (Firebase.getString(loadData, "/Data/"+field)){
    return loadData.stringData();
  }
  
}



void writeDB(String field,String val){
 Firebase.setString(firebaseData, "/Data/"+field,val );
  
}



void initFire(){
  pinMode(D4,OUTPUT);
  Serial.begin(9600);

  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to Wi-Fi");
  
  while (WiFi.status() != WL_CONNECTED)
  {
    digitalWrite(D4,0);
    Serial.print(".");
    delay(200);
    digitalWrite(D4,1);
    Serial.print(".");
    delay(200);
    
  }
  
  Serial.println();
  Serial.print("Connected with IP: ");
  Serial.println(WiFi.localIP());
  Serial.println();

  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  Firebase.reconnectWiFi(true);
}














void setup() {
  
  Serial.begin(9600);
  pinSet();
  initFire();

}



void pinSet(){
  pinMode(A0,INPUT);
  pinMode(D0,INPUT);
  pinMode(D5,INPUT);
  pinMode(D6,OUTPUT); //FAN
  
}


int in1, in2,in3;
String button,moni="";

void loop() {
  moni="";
  in();

  button=readData("Button");

  Serial.print("In1: "+String(in1));
  Serial.print("  In2: "+String(in2));
  Serial.print("  In3: "+String(in3));
  Serial.println("  Button: "+button);
  
  digitalWrite(D6,in1>500 || in2 || in3 || button=="1");
  delay(200);
  writeDB("CO",String(in1));
  delay(200);
  writeDB("CO2",String(in2));
  delay(200);
  writeDB("Fan",String(in1>500 || in2 || in3 || button=="1"));
  delay(200);
  writeDB("Fire",String(in3));
  delay(300);

  

  moni=in1>500?"CO ":"";
  moni+=in2?"CO2 ":"";
  moni+=in3?"FIRE":"";

  
  writeDB("Monitor",moni);
  delay(300);

   
  
}


void in(){
  in1=analogRead(A0);
  in2=digitalRead(D0);
  in3=!digitalRead(D5);
  
  }
  
