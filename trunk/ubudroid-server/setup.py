#!/usr/bin/env python

if __name__ == "__main__":

	from distutils.core import setup

	setup(name="ubudroid-server",
		description="server applet for AndroBuntu/UbuDroid Android application",
		long_description="""ubudroid is an application to allow an Android handset to interact with the Ubuntu desktop.
The client application provides interfaces for media control, X10 home automation, and custom user scripts.""",
		author="Karl Ostmo",
		author_email="kostmo@gmail.com",
		url="http://androbuntu.googlecode.com/",
		version="0.1.1",
		py_modules=["pycm19a"],
		scripts=["ubudroid-server"],
		data_files=[("share/ubudroid-server", ["ubudroid-server.png"])]
	)


