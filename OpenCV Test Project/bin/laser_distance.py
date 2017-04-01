#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Sun Mar  5 20:45:03 2017

@author: pi
"""

import time
import VL53L0X
from networktables import NetworkTables

NetworkTables.initialize(server="roboRIO-2974-frc.local")
sd = NetworkTables.getTable("SmartDashboard")

# Create a VL53L0X object
tof = VL53L0X.VL53L0X()

# Start ranging
tof.start_ranging(VL53L0X.VL53L0X_BETTER_ACCURACY_MODE)

timing = tof.get_timing()
if (timing < 20000):
    timing = 20000
print ("Timing %d ms" % (timing/1000))

while (True):
    distance = tof.get_distance()
    if (distance > 0):
        #print("%d mm" % distance)
        sd.putNumber("Camera distance", distance)
    
    time.sleep(timing/1000000.00)

tof.stop_ranging()
