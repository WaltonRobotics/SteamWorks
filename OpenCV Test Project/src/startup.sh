sudo iwconfig wlan0 txpower off

export WORKON_HOME=$HOME/virtualenvs
source /usr/share/virtualenvwrapper/virtualenvwrapper.sh


workon cv

cd /home/pi/picamera/

python picamera_peg.py 

cd /home/pi/vl53l0x/VL53L0X_rasp_python/python

python laser_distance.py 