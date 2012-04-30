#!/usr/bin/env python

import os
import datetime


from pylab import figure
from matplotlib.backends.backend_pdf import PdfPages
from matplotlib.dates import DateFormatter

PLOT_MARKER_TYPES = ['s', '^', 'd', 'o', '+', '*', ',', '.', '1', '2', '3', '4', '<', '>', 'D', 'H', '_', 'h', 'p', 'v', 'x', '|']




def make_chart():

	# first element - dateestamp
	# second element - duration
	# third element - connectivity
	runlength_tuples = []
	previous_connectivity = False
	last_transition_date = None


	temp_tuples = []
	xvals = []
	yvals = []
	for line in open("connectivity.log"):
		splitted = line.split()

		date = splitted[0]
		time_24hr = splitted[1]
		time_24hr_integral_seconds = time_24hr[:time_24hr.rfind(".") - 1]




		fulltime = datetime.datetime.strptime(date + "-" + time_24hr_integral_seconds, "%Y-%m-%d-%H:%M:%S")
		if fulltime < (datetime.datetime.now() - datetime.timedelta(hours=5)):
			continue


		xvals.append(fulltime)

		ping_return_code = int(splitted[2])
		yvals.append(ping_return_code)


		current_connectivity = not bool(ping_return_code)
		if last_transition_date:
			if current_connectivity != previous_connectivity:
				delta = fulltime - last_transition_date
				runlength_tuples.append( (fulltime, delta.total_seconds(), int(previous_connectivity)))
				last_transition_date = fulltime
		else:
			runlength_tuples.append( (fulltime, 0, current_connectivity) )
			last_transition_date = fulltime

		previous_connectivity = current_connectivity



	off_times, on_times = 0, 0
	fh = open("transitions.log", "w")
	for transition_datetime, duration_seconds, connected in runlength_tuples:
		fh.write("%s\t%d\t%d\n" % (str(transition_datetime), duration_seconds, connected))
		if connected:
			on_times += duration_seconds
		else:
			off_times += duration_seconds

	fh.close()
	print len(runlength_tuples)
	avg_time_connected = on_times/float(len(runlength_tuples))
	avg_time_disconnected = off_times/float(len(runlength_tuples))
	print "Average time on: %f;\tAverage time off: %f" % (avg_time_connected, avg_time_disconnected)




	fig = figure()
	ax1 = fig.add_subplot(111)


	ax1.plot_date(xvals, yvals, linewidth=2.0, marker=".", linestyle="-")

	chart_title = 'Connectivity Lapses'

	ax1.grid(True)
	ax1.set_xlabel('Time')
	ax1.set_ylabel('Ping return code')
	ax1.set_title(chart_title)
	ax1.xaxis.set_major_formatter(DateFormatter('%a %I:%M %P'))
	fig.autofmt_xdate()
	ax1.autoscale_view()

	filename = "connectivity_plot" + ".pdf"
	pp = PdfPages(filename)
	pp.savefig(fig)
	pp.close()

	os.system("evince %s" % filename)

if __name__ == "__main__":
	make_chart()

