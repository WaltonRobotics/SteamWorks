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
        
        logging.info(valid_centers)

        if len(valid_centers) == 1:
            # Single rectangle process
            pegX = valid_centers[0].x

            if pegX > 160:
                pegX += 68

            else:
                pegX -= 68

            pegX -= 160
  
        elif len(valid_centers) == 2:
            # Double rectangle process
            pegX = (valid_centers[0].x + valid_centers[1].x) / 2 - 160
        else:
            # INVALID!!!
            pegX = 0

        pegX *= 1.867637 

        # Send to SmartDashboard        
        sd.putNumber("mm peg centerX", pegX)

        if cv2.waitKey(1) & 0xFF == ord('q'):
            break
        NetworkTables.flush()
                   
    camera.stop_preview()
