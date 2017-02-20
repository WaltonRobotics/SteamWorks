import time
import picamera
import numpy as np
import cv2
from grip_goal import GripPipeline
from networktables import NetworkTables
from fractions import Fraction
import math import os

#Units are in feet
# As a client to connect to a robot
## Todo read from Actual robott
##NetworkTables.initialize(server='roborio-XXX-frc.local')

PID_FILE_NAME = "pid.pid"
WIDTH = 320
HEIGHT = 240
try:
    os.remove(PID_FILE_NAME)
except IOError:
    pass

with open(PID_FILE_NAME, "w") as fd:    
    fd.write("{0:d}\n".format(os.getpid()))


image = np.empty((HEIGHT, WIDTH, 3), dtype=np.uint8)
gripped = GripPipeline()

NetworkTables.initialize(server="roboRIO-2974-frc.local")
sd = NetworkTables.getTable("SmartDashboard")

#inches
KNOWN_WIDTH = 15

KNOWN_DISTANCE = 100.5
KNOWN_FOCAL_LENGTH = 387.94488525390625
#KNOWN_ASPECT_RATIO = 5/2
#KNOWN_HYPOTENEUSE = 8.375

def camera_setup(camera):
    camera.resolution = (WIDTH, HEIGHT)
    camera.framerate = 60
    camera.iso = 100
    camera.shutter_speed = 1000
    camera.awb_mode = "off"
    camera.awb_gains = (Fraction(135, 128), Fraction(343, 128))
    time.sleep(2)
    camera.exposure_mode = "off"

with picamera.PiCamera() as camera:
    camera_setup(camera)
    
    camera.start_preview(fullscreen=False, window=(0,0,WIDTH,HEIGHT))
    #print(type(sd))
    print("Started preview")
    
    while True:
        camera.capture(image, 'bgr', True)
        #cv2.imshow('img2', image)
        gripped.process(image)

        #print(camera.digital_gain, camera.analog_gain)
        #cv2.imshow('img', image)
        if len(gripped.filter_contours_output) >= 2:
            if len(gripped.filter_contours_output) == 2:
                sd.putString("status goal", "correct number of contours")

            else:
                sd.putString("status goal", "too many contours")
            #print(gripped.filter_contours_output)
            #print(gripped.filter_contours_output[0].shape)
            #print(type(cv2.minAreaRect(gripped.filter_contours_output[0])))
            #print(cv2.minAreaRect(gripped.filter_contours_output[0])[1][0] * KNOWN_DISTANCE/ KNOWN_WIDTH)
            #print(distance)

            cY = 0
            distance = 0
            cX = 0
            
            distance1 = 0
            distance2 = 0
            
            lcv = 0

            for c in gripped.filter_contours_output:
                    #countour.append(c)
                    lcv+=1 
                    M = cv2.moments(c)
                    boundries = cv2.minAreaRect(c)
    
                    #print(M)
                    if (lcv == 1):
                         X1 = int(M["m10"] / M["m00"])
                         Y1 = int(M["m01"] / M["m00"])
                         width1 = boundries[1][0]

                         distance1 = KNOWN_WIDTH * KNOWN_FOCAL_LENGTH / boundries[1][0]
                    elif (lcv == 2):
                         X2 = int(M["m10"] / M["m00"])
                         Y2 = int(M["m01"] / M["m00"])
                         width2 = boundries[1][0]
                         distance2 = KNOWN_WIDTH * KNOWN_FOCAL_LENGTH / boundries[1][0]

                    #print(boundries[1][0])

                    cX += int(M["m10"] / M["m00"])
                    distance += KNOWN_WIDTH * KNOWN_FOCAL_LENGTH / boundries[1][0]
                    cY += int(M["m01"] / M["m00"])
                    #cv2.drawContours(image, [c], -1, (255,0,0),2)
                  
            length = len(gripped.filter_contours_output)


            print(distance1, distance2)

            cY /= length
            cX /= length
            distance /= length

            if (abs(X1-X2) > abs(Y1-Y2)):
                sd.putNumber("Center point X goal", cX)
                sd.putNumber("Center point Y goal", cY)
                sd.putNumber("Camera distance goal", distance)
                sd.putNumber("To Flush goal", distance - 11)
                sd.putNumber("Camera angle goal", math.asin((cX - (WIDTH / 2)) / distance))

            #break
        else:
            sd.putString("status goal", "too few contours")

        if cv2.waitKey(1) & 0xFF == ord('q'):
            break
        NetworkTables.flush()
    camera.stop_preview()
