/**
Copyright (C) 2014  Inversebit

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

/*
This file is a modification from
- The sensors ping example from arduino, created by David A. Mellis, modified by Tom Igoe
- The servo sweep example from arduino, created by BARRAGAN <http://barraganstudio.com>
*/
 
#include <Servo.h>

// this constant won't change.  It's the pin number
// of the sensor's output:
const int trigPin = 7;
const int echoPin = 8;

Servo myservo;
int pos = 0;

long duration, inches, cm;
unsigned long initialTime;
int val;


void setup() 
{
  // initialize serial communication:
  Serial.begin(9600);
  pinMode(trigPin, OUTPUT);
  pinMode(echoPin, INPUT);
  myservo.attach(9);
}

void loop()
{
  
      // establish variables for duration of the ping, 
      // and the distance result in inches and centimeters:
      
      if(Serial.available())     // if data is available to read
      {
        val = Serial.read(); 
      }
      
      if ((char)val == 'P')
      {
        for(pos = 0; pos < 180; pos += 1)  // goes from 0 degrees to 180 degrees 
        {                                  // in steps of 1 degree 
          myservo.write(pos);              // tell servo to go to position in variable 'pos' 
          delay(20);                       // waits 15ms for the servo to reach the position 
          ping();
          Serial.print(cm);
          Serial.print("A");
        } 
        for(pos = 180; pos>=1; pos-=1)     // goes from 180 degrees to 0 degrees 
        {                                
          myservo.write(pos);              // tell servo to go to position in variable 'pos' 
          delay(20);                       // waits 15ms for the servo to reach the position 
          ping();
          Serial.print(cm);
          Serial.print("A");
        }         
      }
      
      initialTime = millis();
      
      while((millis() - 50) < initialTime);
}

long microsecondsToCentimeters(long microseconds)
{
  // The speed of sound is 340 m/s or 29 microseconds per centimeter.
  // The ping travels out and back, so to find the distance of the
  // object we take half of the distance travelled.
  return microseconds / 29 / 2;
}

long ping()
{
    digitalWrite(trigPin, LOW);
    delayMicroseconds(2);
    digitalWrite(trigPin, HIGH);
    delayMicroseconds(5);
    digitalWrite(trigPin, LOW);
    
    duration = pulseIn(echoPin, HIGH);
      
    cm = microsecondsToCentimeters(duration);
}
