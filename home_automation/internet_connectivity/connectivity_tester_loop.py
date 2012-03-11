#!/usr/bin/env python

from datetime import datetime
from time import sleep
import os

TIMEOUT_SECONDS = 3
TARGET_WEBSITE = "google.com"

log_fh = open("connectivity.log", "a")
while True:
	return_code = os.system("ping -w %d -c 1 %s" % (TIMEOUT_SECONDS, TARGET_WEBSITE))
	
	log_fh.write( str(datetime.now()) + "\t" + str(return_code) + "\n")
	log_fh.flush()
	if not return_code:
		sleep(TIMEOUT_SECONDS)

log_fh.close()
