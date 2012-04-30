#!/usr/bin/env python

'''Test cases:

curl localhost:8081 --data '{"operation": "keypress", "key": "XF86AudioMute"}'
curl localhost:8081 --data '{"operation": "system", "args": ["vlc", "vid.mkv"]}'
curl localhost:8081 --data '{"operation": "window", "action": "list"}'
curl localhost:8081 --data '{"operation": "window", "action": "activate", "window_id": "16778341"}
curl localhost:8081 --data '{"operation": "x10", "action": "on", "house_code": "A", "unit_code": "3"}
curl localhost:8081 --data '{"operation": "x10", "action": "off", "house_code": "A", "unit_code": "3"}
curl localhost:8081 --data '{"operation": "x10", "action": "disconnect"}
'''

import os
import json
#from http.server import HTTPServer, BaseHTTPRequestHandler
from BaseHTTPServer import HTTPServer, BaseHTTPRequestHandler
from subprocess import check_output, check_call, Popen

from pycm19a import start_session

LISTEN_PORT = 8081
WINDOW_MANAGER_CONTROL = "/usr/bin/wmctrl"

# =============================================================================
class MyRequestHandler(BaseHTTPRequestHandler):

	def do_GET(self):
		print("command:", self.command)
		print("Path:", self.path)

	def do_POST(self):
		print("command:", self.command)
		print("Path:", self.path)

		json_string = self.rfile.read(int(self.headers["Content-Length"])).decode("utf-8")
		self.rfile.close()

		json_data = json.loads(json_string)
		return_dict = self.process_operation(json_data)

		# Write response
		self.wfile.write( json.dumps(return_dict).encode("utf-8") )

	def process_operation(self, json_data):
		operation_type = json_data["operation"]

		# Return False by default
		return_dict = {"success": False}

		if operation_type == "keypress":
			key = json_data["key"]
			print("Requested keypress:", key)
	#		output_bytearray = check_output(["xte", '"key %s"' % (key)], shell=True)
	#		return_dict["output"] = output_bytearray.decode("utf-8")

			command_status_code = os.system('xte "key %s"' % (key))
			return_dict["success"] = not bool(command_status_code)
			return_dict["status"] = command_status_code

		elif operation_type == "system":
			args = json_data["args"]
			p = Popen(args)

			return_dict["success"] = True

		elif operation_type == "window":

			action = json_data["action"]
			if action == "list":
				output_bytearray = check_output([WINDOW_MANAGER_CONTROL, "-l"])
				windows_array = []

				import re
				for match in re.finditer(r"0x(.+?)\s+(.+?)\s+(.+?)\s+(.*)\n", output_bytearray.decode("utf-8")):
					window_dict = {}
					window_dict["window_id"] = int(match.group(1), 16)
					window_dict["desktop_id"] = int(match.group(2))
					window_dict["machine"] = match.group(3)
					window_dict["title"] = match.group(4)
					windows_array.append( window_dict )

					return_dict["success"] = True

				return_dict["windows"] = windows_array

			if action == "activate":
				window_id = int(json_data["window_id"])
				return_code = check_call([WINDOW_MANAGER_CONTROL, "-i", "-a %d" % (window_id)])
				return_dict["success"] = True

		elif operation_type == "x10":

			X10_COMMANDS = {"on": self.server.cm19a_monitor.on, "off": self.server.cm19a_monitor.off}

			action = json_data["action"]
			if action in X10_COMMANDS:
				X10_COMMANDS[action](json_data["house_code"], int(json_data["unit_code"]))

			if action == "disconnect":

				self.server.cm19a_monitor.Done = True
				try:
					# We know this is an invalid unit code, so catch the error.
					self.server.cm19a_monitor.off("A", 0)
				except ValueError, e:
#					print(e.message)
					pass
				print("Disconnecting X10 Server.")

		elif operation_type == "vlc":
			pass

		return return_dict

# =============================================================================
class UbuDroidServer(HTTPServer):
	def __init__(self, server_location):
		HTTPServer.__init__(self, server_location, MyRequestHandler)

		self.cm19a_monitor = start_session()

# =============================================================================
if __name__ == "__main__":

	httpd = UbuDroidServer(('localhost', LISTEN_PORT))
	httpd.serve_forever()

