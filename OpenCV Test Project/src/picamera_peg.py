import time
import picamera
import numpy as np
import cv2
from grip_peg import GripPipeline
from networktables import NetworkTables
from fractions import Fraction
import math 
import os

#Units are in feet
# As a client to connect to a robot
## Todo read from Actual robott
##NetworkTables.initialize(server='roborio-XXX-frc.local')

PID_FILE_NAME = "pid.pid"

#try:
#    os.remove(PID_FILE_NAME)
#except IOError:
#    pass

#with open(PID_FILE_NAME, "w") as fd:    
#    fd.write("{0:d}\n".format(os.getpid()))


image = np.empty((240, 320, 3), dtype=np.uint8)
gripped = GripPipeline()

NetworkTables.initialize(server="roboRIO-2974-frc.local")
sd = NetworkTables.getTable("SmartDashboard")

#inches
KNOWN_WIDTH = 2

KNOWN_DISTANCE = 52
KNOWN_FOCAL_LENGTH = 676.0
#KNOWN_ASPECT_RATIO = 5/2
KNOWN_HYPOTENEUSE = 8.375

def camera_setup(camera):
    camera.resolution = (320, 240)
    camera.framerate = 60
    camera.iso = 100
    camera.shutter_speed = 2000
    camera.awb_mode = "off"
    camera.awb_gains = (Fraction(135, 128), Fraction(343, 128))
    time.sleep(2)
    camera.exposure_mode = "off"

#def find_angle(distance1, distance2):
#    angle_valid = False
    
#    delta_d = distance2 - distance1    
    #print(delta_d)

#    angle = ""
    
#    if math.fabs(delta_d) < KNOWN_HYPOTENEUSE:
#        angle = math.asin(delta_d / KNOWN_HYPOTENEUSE) if math.asin(delta_d / KNOWN_HYPOTENEUSE) else ""
#        if not isinstance(angle, str):
#            angle_vaild = True

#    sd.putBoolean("Valid angle peg", angle_valid)


#    return angle, angle_valid

with picamera.PiCamera() as camera:
    camera_setup(camera)

    sd.putNumber("resolutionX", camera.resolution[0])
    sd.putNumber("resolutionY", camera.resolution[1])
    
    camera.start_preview(fullscreen=False, window=(640,640,320,240))
    #print(type(sd))
    print("Started preview")
    
    while True:
        camera.capture(image, 'bgr', True)
        #cv2.imshow('img2', image)
        gripped.process(image)

        #print(camera.digital_gain, camera.analog_gain)
        #cv2.imshow('img', image)
        if len(gripped.filter_contours_output) >= 2:
            #print(gripped.filter_contours_output)
            #print(gripped.filter_contours_output[0].shape)
            #print(type(cv2.minAreaRect(gripped.filter_contours_output[0])))
            #print(cv2.minAreaRect(gripped.filter_contours_output[0])[1][0] * KNOWN_DISTANCE/ KNOWN_WIDTH)
            #print(distance)

            cY = 0
            #distance = 0
            cX = 0
            
            #distance1 = 0
            #distance2 = 0
            
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

                         #distance1 = KNOWN_WIDTH * KNOWN_FOCAL_LENGTH / boundries[1][0]
                    elif (lcv == 2):
                         X2 = int(M["m10"] / M["m00"])
                         Y2 = int(M["m01"] / M["m00"])
                         width2 = boundries[1][0]
                         #distance2 = KNOWN_WIDTH * KNOWN_FOCAL_LENGTH / boundries[1][0]

                    #print(boundries[1][0])

                    cX += int(M["m10"] / M["m00"])
                    #distance += KNOWN_WIDTH * KNOWN_FOCAL_LENGTH / boundries[1][0]
                    cY += int(M["m01"] / M["m00"])
                    #cv2.drawContours(image, [c], -1, (255,0,0),2)
                  
            length = len(gripped.filter_contours_output)


            #print(distance1, distance2)

            #(tilt_angle, is_valid_angle) = find_angle(distance1, distance2) 

            #print(tilt_angle)

            cY /= length
            cX /= length
            #distance /= length

            if (abs(X1-X2) > abs(Y1-Y2)):
                sd.putNumber("Center point X peg", cX)
                sd.putNumber("Center point Y peg", cY)
                #sd.putNumber("Camera distance peg", distance)

                if is_valid_angle:
                    sd.putNumber("Target angle peg", tilt_angle)
            #sd.putNumberArray("image", gripped.filter_contours_output)
            #break
        
        sd.putNumber("status peg", len(gripped.filter_contours_output))

        if cv2.waitKey(1) & 0xFF == ord('q'):
            break
        NetworkTables.flush()
    camera.stop_preview()
