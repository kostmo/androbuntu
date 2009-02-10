#!/usr/bin/env python

"""
A simple echo server
"""

import socket
import os

from master import port

host = ''

backlog = 5
size = 1024
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.bind((host,port))
s.listen(backlog)


while True:
	client, address = s.accept()
	data = client.recv(size)
	if data:

		string = 'xte "key ' + data + '"'
		os.system( string )

		client.send(data)


	client.close()
