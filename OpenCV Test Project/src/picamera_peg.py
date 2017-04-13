import time
from time import strftime
import picamera
import numpy as np
import cv2
from grip_peg import GripPipeline
from networktables import NetworkTables
from fractions import Fraction
import math 
import os
from datetime import datetime
import logging
import collections


#Units are in feet
# As a client to connect to a robot
## Todo read from Actual robott
##NetworkTables.initialize(server='roborio-XXX-frc.local')

#PID_FILE_NAME = "pid.pid"

#try:
#    os.remove(PID_FILE_NAME)
#except IOError:
#    pass

#with open(PID_FILE_NAME, "w") as fd:    
#    fd.write("{0:d}\n".format(os.getpid()))
#path = datetime.now().strftime("%Y-%m-%d-%H-%M")
#os.mkdir(path)
#os.chdir(path)

#values_file = open("SmartDashboardValues.txt", "w")

image = np.empty((240, 320, 3), dtype=np.uint8)
gripped = GripPipeline()

NetworkTables.initialize(server="roboRIO-2974-frc.local")
sd = NetworkTables.getTable("SmartDashboard")

Point = collections.namedtuple("Point", "x y")


def camera_setup(camera):
    camera.resolution = (320, 240)
    camera.framerate = 60
    camera.iso = 100
    camera.shutter_speed = 600
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

ANGLE_LIMIT = 10
MIN_WIDTH = 25
MAX_WIDTH = 35



def validate_contours(contours):
    valid_centers = []

    for contour in contours:
        boundries = cv2.minAreaRect(contour)
        width, height, angle = boundaries[1][0], boundaries[1][1], boundries[2]

        logging.info("Width={0:d}, height={1:d}, angle={2:d}".format(width, height, angle))

        moments = None
        if -ANGLE_LIMIT < angle < ANGLE_LIMIT:
            if MIN_WIDTH < width < MAX_WIDTH:
                moments = cv2.moments(contour)

        elif -90 - ANGLE_LIMIT < angle < -90 + ANGLE_LIMIT:
            if MIN_WIDTH < width < MAX_WIDTH:
                moments = cv2.moments(contour)

        if moments is not None:
            cx = moments["m10"] / moments["m00"]
            cy = moments["m01"] / moments["m00"]
            valid_centers.append(Point(cx, cy))

    return valid_centers


with picamera.PiCamera() as camera:
    camera_setup(camera)
    camera.start_preview(fullscreen=False, window=(640, 640, 320, 240))
    logging.info("Started preview")

    while True:
        camera.capture(image, 'bgr', True)
        gripped.process(image)
        valid_centers = validate_contours(gripped.filter_contours_output)

        if len(valid_centers) == 1:
            # Single rectangle process
            pegX = ...
        elif len(valid_centers) == 2:
            # Double rectangle process
            pegX = ...
        else:
            # INVALID!!!
            pegX = 0

        # Send to SmartDashboard

        cX = 0
        if len(gripped.filter_contours_output) == 1:
            M = cv2.moments(gripped.filter_contours_output[0])
            X1 = int(M["m10"] / M["m00"])

            if(X1 > 160):
                cX = X1 + 68
            else:
                cX = X1 - 68

            print (cX - 160)
            
        elif len(gripped.filter_contours_output) >= 2:
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
                sd.putNumber("Center point Y peg", cY)
                #sd.putNumber("Camera distance peg", distance)
                print ("cX: %d cY: %d  cX - lX: %d rX - cX: %d" % (cX -160, cY, X1 - cX , cX - X2))
                
         #       values_file.write("{0:f}, {1:f}, {2:f}\n".format(timeN - startT, cX, cY))
          #      values_file.flush()
#                if is_valid_angle:
#                    sd.putNumber("Target angle peg", tilt_angle)
            #sd.putNumberArray("image", gripped.filter_contours_output)
            #break

        cX = cX - 160
        cX = cX * 1.867637 
        
        sd.putNumber("status peg", len(gripped.filter_contours_output))
        sd.putNumber("mm peg centerX", cX)

        if cv2.waitKey(1) & 0xFF == ord('q'):
            break
        NetworkTables.flush()
                   
    camera.stop_preview()
