#!/usr/bin/env python3
import speech_recognition as sr
import calendar, time
import sys

if __name__ == "__main__":
	"""usage: audio.py audioDeviceNumber logFile.wav"""
	
	# default arguments
	logFile = str(int(calendar.timegm(time.gmtime()))) + ".wav"
	d_index = 2
	
	# set arguments if applicable
	if len(sys.argv) > 1:
		d_index = int(sys.argv[1])
		if len(sys.argv) > 2:
			logFile = sys.argv[2]
			
	r = sr.Recognizer()
	with sr.Microphone(device_index=d_index) as source:
		r.adjust_for_ambient_noise(source)
		audio = r.listen(source)
		
		# Log the audio data
		with open(logFile, "wb") as f:
			f.write(audio.get_wav_data())
		
		# Print the text
		print(r.recognize_google(audio))
	
