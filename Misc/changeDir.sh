#!/bin/bash
DIR="/timeseries"
for D in *; do
    if [ -d "${D}" ]; then
        echo "${D}"
	if [ -d "${D}$DIR" ]; then
		mkdir "${D}"/time\ series/
		mv "${D}"/timeseries/* "${D}"/time\ series/   # your processing here
    		rm -r "${D}"/timeseries
    	fi
    fi
done
