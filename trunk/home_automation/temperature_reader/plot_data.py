#!/usr/bin/env python

import datetime

from pylab import figure
from matplotlib.backends.backend_pdf import PdfPages
from matplotlib.dates import DateFormatter

import shared

PLOT_MARKER_TYPES = ['s', '^', 'd', 'o', '+', '*', ',', '.', '1', '2', '3', '4', '<', '>', 'D', 'H', '_', 'h', 'p', 'v', 'x', '|']


def make_chart():

	temp_tuples = []
	xvals = []
	yvals = []
	for line in open(shared.COLLECTION_FILENAME):
		splitted = line.split()


		date = splitted[0]
		time_24hr = splitted[1]

		fulltime = datetime.datetime.strptime(date + "-" + time_24hr, "%Y/%m/%d-%H:%M:%S")
		xvals.append(fulltime)

		temp = float(splitted[3].rstrip("F"))
		yvals.append(temp)


	fig = figure()
	ax1 = fig.add_subplot(111)

#	i=0
#	ax1.plot_date(xvals, yvals, linewidth=2.0, marker=PLOT_MARKER_TYPES[i % len(PLOT_MARKER_TYPES)])
#	leg = ax1.legend([("%d RPM" % rpm_tuple[0]) for rpm_tuple in sorted_rpm_data_tuples], 'lower right', shadow=True)

	ax1.plot_date(xvals, yvals, linewidth=2.0, marker="s", linestyle="-")

	chart_title = 'Local temperature'

	ax1.grid(True)
	ax1.set_xlabel('Time')
	ax1.set_ylabel('Temperature (deg F)')
	ax1.set_title(chart_title)
	ax1.xaxis.set_major_formatter(DateFormatter('%I:%M %P'))
	fig.autofmt_xdate()
	ax1.autoscale_view()

	filename = "temperature_plot" + ".pdf"
	pp = PdfPages(filename)
	pp.savefig(fig)
	pp.close()


if __name__ == "__main__":
	make_chart()

