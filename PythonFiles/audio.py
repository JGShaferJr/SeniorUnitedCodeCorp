#!/usr/bin/env python3
import speech_recognition as sr
import time
from threading import Thread
import copy
import os
import calendar
 
def recog_audio(x_sr, x_audio):
        #print("Recognizing audio")
        try:
                print(x_sr.recognize_google(x_audio), flush=True)
        except sr.UnknownValueError:
                pass
 
def write_file(x_audio):
        logFile = str(int(calendar.timegm(time.gmtime()))) + ".wav"
        with open(logFile, "wb") as f:
                f.write(x_audio.get_wav_data())
 
 
if __name__ == "__main__":
       # print("PID:", os.getpid(), flush=True)
        d_index = 2
        r = sr.Recognizer()
        with sr.Microphone(device_index=d_index) as source:
                r.adjust_for_ambient_noise(source)
                while True:
                        audio = r.listen(source)
                        t1 = Thread(target=recog_audio, args=(copy.deepcopy(r), audio,))
                        t2 = Thread(target=write_file, args=(audio,))
                        t1.start()
                        t2.start()
