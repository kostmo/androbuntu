#!/usr/bin/env python

"""***************************************************************************
**
** Copyright (C) 2005-2005 Trolltech AS. All rights reserved.
**
** This file is part of the example classes of the Qt Toolkit.
**
** This file may be used under the terms of the GNU General Public
** License version 2.0 as published by the Free Software Foundation
** and appearing in the file LICENSE.GPL included in the packaging of
** this file.  Please review the following information to ensure GNU
** General Public Licensing requirements will be met:
** http://www.trolltech.com/products/qt/opensource.html
**
** If you are unsure which license is appropriate for your use, please
** review the following information:
** http://www.trolltech.com/products/qt/licensing.html or contact the
** sales department at sales@trolltech.com.
**
** This file is provided AS IS with NO WARRANTY OF ANY KIND, INCLUDING THE
** WARRANTY OF DESIGN, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
**
***************************************************************************"""


from PyQt4 import QtCore, QtGui
import sys



class KarlList(QtGui.QListWidget):
	def __init__(self, parent=None):
		QtGui.QListWidget.__init__(self, parent)


#	self.connect(QtCore.SIGNAL("itemClicked()"), view.itemClicked)

	def mouseDoubleClickEvent(self, event):
		print "Fark!"

	def itemSelectionChanged(self):
		print "Crap!"


def durka():

	print "fart"



if __name__ == "__main__":
	app = QtGui.QApplication(sys.argv)

	xf86_list = [line.split()[0] for line in open("/usr/share/X11/XKeysymDB") if "XF86" in line]	# Is this ugly or what?

	view = KarlList()
	view.setWindowTitle("Simple List Model")
	view.addItems(xf86_list)


	QtCore.QObject.connect(view, QtCore.SIGNAL("itemClicked"), durka)



	view.show()
	sys.exit(app.exec_())
