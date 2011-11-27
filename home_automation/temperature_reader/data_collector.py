#!/usr/bin/env python

import subprocess as sub
from time import sleep


import shared

def collect_data(interval):
	while True:


		p = sub.Popen('pcsensor -f', stdout=sub.PIPE, stderr=sub.PIPE, shell=True)
		output, errors = p.communicate()
		open(shared.COLLECTION_FILENAME, "a").write(output)

		sleep(interval)


if __name__=="__main__":

	collect_data(2)
