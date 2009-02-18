#!/usr/bin/env python

# example helloworld.py

import pygtk
pygtk.require('2.0')
import gtk

class HelloWorld(gtk.Window):

	# This is a callback function. The data arguments are ignored
	# in this example. More on callbacks below.
	def hello(self, widget, data=None):
		print "Hello World"


	def delete_event(self, widget, event, data=None):
		return False

	def destroy(self, widget, data=None):
		print "destroy signal occurred"
		gtk.main_quit()



		import socket
		from master import port

#		host = 'localhost'
		host = '192.168.0.9'

		s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
		s.connect( (host, port) )

		s.send( "quit" )
		s.close()
		print 'Received:', data




	def cb_row_activated(self, treeview, path, view_column):

		ts = treeview.get_model()
		command_to_send = ts.get_value(ts.get_iter(path), 0)


		import socket
		from master import port

		host = 'localhost'

		size = 1024
		s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
		s.connect( (host, port) )

		s.send( command_to_send )
		data = s.recv(size)
		s.close()
		print 'Received:', data,






	def __init__(self):
		gtk.Window.__init__(self)

		self.connect("delete_event", self.delete_event)
		self.connect("destroy", self.destroy)


		self.set_border_width(10)


		button = gtk.Button("Hello World")
		button.connect("clicked", self.hello, None)
		button.connect_object("clicked", gtk.Widget.destroy, self)



		xf86_list = [line.split()[0] for line in open("/usr/share/X11/XKeysymDB") if "XF86" in line and line[0] != "!"]	# Is this ugly or what?


		ls = gtk.ListStore(str)
		for item in xf86_list:

			if "Audio" in item:
				ls.append( [item] )


		tv = gtk.TreeView(ls)
		col = gtk.TreeViewColumn("foobar", gtk.CellRendererText(), text=0)
		tv.append_column(col)

		tv.connect("row-activated", self.cb_row_activated)

		vbox = gtk.VBox()
		vbox.pack_start(button, False, False)

		sw = gtk.ScrolledWindow()
		sw.set_policy(gtk.POLICY_NEVER, gtk.POLICY_AUTOMATIC)
		sw.add(tv)
		vbox.pack_start(sw)


		self.add(vbox)

		self.show_all()


if __name__ == "__main__":
	hello = HelloWorld()
	gtk.main()
