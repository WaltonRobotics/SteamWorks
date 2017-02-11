import time
import picamera
import numpy as np
import cv2
from grip import GripPipeline
from networktables import NetworkTables

# As a client to connect to a robot
## Todo read from Actual robott
##NetworkTables.initialize(server='roborio-XXX-frc.local')

image = np.empty((240, 320, 3), dtype=np.uint8)
gripped = GripPipeline()

NetworkTables.initialize(server="roboRIO-2974-frc.local")
sd = NetworkTables.getTable("SmartDashboard")

#inches
KNOWN_WIDTH = 2.1

KNOWN_DISTANCE = 28.75
KNOWN_FOCAL_LENGTH = 232.73809523809524
#KNOWN_ASPECT_RATIO = 5/2

with picamera.PiCamera() as camera:
    camera.resolution = (320, 240)
    camera.framerate = 60
    camera.iso = 100
    camera.shutter_speed = 1000
    camera.awb_mode = "off"
    camera.awb_gains = (Fraction(135, 128), Fraction(343, 128))
    
    time.sleep(2)
    camera.exposure_mode = "off"
    
    while True:
        camera.capture(image, 'bgr')
        cv2.imshow('img', image)
        gripped.process(image)
        
        if len(gripped.filter_contours_output) >= 2:
            if len(gripped.filter_contours_output) == 2:
                sd.putString("status", "correct number of contours")

            else:
                sd.putString("status", "too many contours")
            #print(gripped.filter_contours_output)
            #print(gripped.filter_contours_output[0].shape)
            #print(type(cv2.minAreaRect(gripped.filter_contours_output[0])))
            #print(cv2.minAreaRect(gripped.filter_contours_output[0])[1][0] * KNOWN_DISTANCE/ KNOWN_WIDTH)
            distance = KNOWN_WIDTH * KNOWN_FOCAL_LENGTH / cv2.minAreaRect(gripped.filter_contours_output[0])[1][0]
            #print(distance)

            sd.putNumber("camera distance", distance)

            cY = 0
            cX = 0

            for c in gripped.filter_contours_output:
                    #countour.append(c)
                    
                    M = cv2.moments(c)
                    #print(M)
                    cX += int(M["m10"] / M["m00"])
                    cY += int(M["m01"] / M["m00"])
                    #cv2.drawContours(image, [c], -1, (255,0,0),2)

            length = len(gripped.filter_contours_output)

            cY /= length
            cX /= length
            print(cX)

            sd.putNumber("Center point X",cX)

            #sd.putNumberArray("image", gripped.filter_contours_output)
            #break
        else:
            sd.putString("status", "too few contours")

        if cv2.waitKey(1) & 0xFF == ord('q'):
            break
        NetworkTables.flush()

    cv2.destroyAllWindows()
