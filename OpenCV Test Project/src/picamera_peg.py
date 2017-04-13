# Imports for project
import collections
from datetime import datetime
from fractions import Fraction
import logging
import math 
import os
from time import strftime
import time

# Import for the OpenCV classes
import cv2

from grip_peg import GripPipeline
from networktables import NetworkTables
import numpy as np
import picamera

# Creates an empty image 320x240
image = np.empty((240, 320, 3), dtype=np.uint8)

# Creates an instance of the GripPipeline object. This class is used to process the image received from the camera
gripped = GripPipeline()

# Starts the network connection to the SmartDashboard from the RoboRIO
NetworkTables.initialize(server="roboRIO-2974-frc.local")

# Gets the instance of the SmartDashboard to use
sd = NetworkTables.getTable("SmartDashboard")

# Creates a named tuple with x,y coordinates. A named tuple is like an object 
Point = collections.namedtuple("Point", "x y")

# Function used to initialise the camera settings. These settings are predefined and are used to make the camera more efficient.
def camera_setup(camera):
    camera.resolution = (320, 240)
    camera.framerate = 60
    camera.iso = 100
    camera.shutter_speed = 600
    camera.awb_mode = "off"
    camera.awb_gains = (Fraction(135, 128), Fraction(343, 128))
    
    # Lets the camera set the settings. Waits for 2 seconds.
    time.sleep(2)
    camera.exposure_mode = "off"

ANGLE_LIMIT = 10
MIN_WIDTH = 25
MAX_WIDTH = 35

# This function is used to validate the contours received from the image camera and processed by the GripPipline.
def validate_contours(contours):
    valid_centers = []

    # Loops through the given contours
    for contour in contours:
        # Creates a rectangle of the smallest size possible around the contour
        boundries = cv2.minAreaRect(contour)
        
        # When using cv2.minAreaRect(aContour) you must know that it returns a tuple:
        # ((x, y), (width, height), angle)
        
        width, height, angle = boundaries[1][0], boundaries[1][1], boundries[2]

        # Prints to the screen the width, height and angle
        logging.info("Width={0:d}, height={1:d}, angle={2:d}".format(width, height, angle))

        # Initialises moments with nothing
        moments = None
        
        # If the angle of the rectangle is between -ANGLE_LIMIT and ANGLE_LIMIT
        if -ANGLE_LIMIT < angle < ANGLE_LIMIT:
            # If the width is between MIN_WIDTH and MAX_WIDTH
            if MIN_WIDTH < width < MAX_WIDTH:
                # Set moments to the moment of the contour. This means that the contour is valid
                moments = cv2.moments(contour)

        # If the rectangle is reversed and so the rectangle angle is between -90 - ANGLE_LIMIT < angle < -90 + ANGLE_LIMIT
        elif -90 - ANGLE_LIMIT < angle < -90 + ANGLE_LIMIT:
            
            # Since the rectangle is reversed the height is the width is the height so we have to check the height
            if MIN_WIDTH < height < MAX_WIDTH:
                
                # Set moments to the moment of the contour. This means that the contour is valid
                moments = cv2.moments(contour)

        # If the contour is valid
        if moments is not None:
            
            # Find the x coordinate of the centre of the contour moment 
            cx = moments["m10"] / moments["m00"]
            
            # Find the y coordinate of the centre of the contour moment
            cy = moments["m01"] / moments["m00"]
            
            # Add the coordinate of the centre to the valid centres list
            valid_centers.append(Point(cx, cy))

    # Returns the computed valid centres of the contour list
    return valid_centers

# Gets the camera from the raspberry pi
with picamera.PiCamera() as camera:
    # Sets up the camera
    camera_setup(camera)
    
    # Starts the camera
    camera.start_preview(fullscreen=False, window=(640, 640, 320, 240))
    logging.info("Started preview")

    while True:
        # Gets the current image frame
        camera.capture(image, 'bgr', True)
        
        # Processes the image using the GripPipeline. Gets the contours and sets them in gripped.filter_contours_output
        gripped.process(image)
        
        # Validates the processed centres
        valid_centers = validate_contours(gripped.filter_contours_output)
        
        # Prints the valid contours to screen
        logging.info(valid_centers)

        # Checks if there is only one valid contour
        if len(valid_centers) == 1:
            # Gets the x coordinate of the single contour
            pegX = valid_centers[0].x

            # If the contour is more to the right adds 68 pixels
            if pegX > 160:
                pegX += 68

            # If the contour is more to the left removes 68 pixels
            else:
                pegX -= 68

            # Removes 160 from centre coordinate as to make 0 the middle of the screen as the resolution is 360x240
            pegX -= 160
  
        # Checks if there are 2 contours
        elif len(valid_centers) == 2:
            # Gets the average of the two x coordinates as to get the middle. Subtracts 160 to make 0 the middle 
            pegX = (valid_centers[0].x + valid_centers[1].x) / 2 - 160
        
        # Every other solution is invalid so makes pegX
        else:
            pegX = 0

        # Converts the x coordinate into millimetres
        pegX *= 1.867637 

        # Sends to SmartDashboard the value of the centre x coordinate in millimetres
        sd.putNumber("mm peg centerX", pegX)

        # If 'q' is pressed on the keyboard exit loop
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break
        
        # Send all values from SmartDashboard to the network so that the Driver Station can recieve it
        NetworkTables.flush()
    
    # Stops camera
    camera.stop_preview()