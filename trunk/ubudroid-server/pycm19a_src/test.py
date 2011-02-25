#!/usr/bin/env python

from time import sleep

from pycm19a import start_session
monitor = start_session()
import sys
for i in range(3):
	if len(sys.argv) > 1:
		monitor.on('A', i + 1)
	else:
		monitor.off('A', i + 1)
	sleep(1)

monitor.Done = True
