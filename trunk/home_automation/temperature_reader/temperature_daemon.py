#!/usr/bin/env python

# XXX This does not work.

import daemon

from data_collector import collect_data

with daemon.DaemonContext():
    collect_data(2)
