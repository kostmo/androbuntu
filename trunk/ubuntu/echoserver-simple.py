#!/usr/bin/env python

"""
A simple echo server
"""

import socket
import os

from master import port


import signal


class MainServer:


#	def friendly_close(arg1, arg2):


	def __init__(self):

		hostname = ''

		backlog = 5
		size = 1024
		s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
		s.bind((hostname,port))
		s.listen(backlog)

#		signal.signal(signal.SIGTERM, x, s)

		while True:
			client, address = s.accept()
			data = client.recv(size)
			if data:

				print "Got a message:", data

				if data == "quit":

					client.close()
					break

				string = 'xte "key ' + data + '"'
				os.system( string )

				client.send(data+"\n")


			client.close()



if __name__ == "__main__":
	s = MainServer()

