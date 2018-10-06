#!/usr/bin/env python2
from flask import Flask, Response
import cv2

class Camera:
	def __init__(self):
		self.video = cv2.VideoCapture(0)

	def __del__(self):
		self.video.release()

	def get_frame(self):
		_, image = self.video.read()
		_, jpg = cv2.imencode('.jpg', image)
		return jpg.tobytes()

app = Flask(__name__)

def get_frame(camera):
	while True:
		frame = camera.get_frame()
		yield (b'--frame\r\n'
			   b'Content-Type: image/jpeg\r\n\r\n' + frame + b'\r\n\r\n')

@app.route('/')
def serve():
	return Response(get_frame(Camera()),
			mimetype='multipart/x-mixed-replace; boundary=frame')

if __name__ == '__main__':
	app.run(host='0.0.0.0', debug=True)
