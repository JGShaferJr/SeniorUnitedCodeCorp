#!/usr/bin/env python3
# Author: Benjamin T James
from gtts import gTTS
import time
import calendar
import os
import pyglet
from threading import Thread

pyglet.lib.load_library('avbin')
pyglet.have_avbin=True


def play_mp3(mp3file):
    music = pyglet.media.load(mp3file, streaming=False)
    music.play()
    time.sleep(music.duration)
    print("Done")

if __name__ == "__main__":
        print("PID:", os.getpid())
        while True:
                line = input().rstrip()
                if not line:
                        break
                tts = gTTS(line, lang="en")
                filename = str(int(calendar.timegm(time.gmtime()))) + ".mp3"
                tts.save(filename)
                t1 = Thread(target=play_mp3, args=(filename,))
                t1.start()
