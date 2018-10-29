#!/usr/bin/env python3
# Author: Benjamin T James
from gtts import gTTS
import time
import calendar
import os
import sys
import pyglet

# To get this to work, need 
# 1) avbin.dll (32-bit DLL from http://avbin.github.io/AVbin/Download.html)
# 2) pip install pyglet gtts
pyglet.lib.load_library('avbin')
pyglet.have_avbin=True

tts = gTTS(' '.join(sys.argv[1:]), lang="en")
filename = str(int(calendar.timegm(time.gmtime()))) + ".mp3"
tts.save(filename)

music = pyglet.media.load(filename, streaming=False)
music.play()

time.sleep(music.duration)
