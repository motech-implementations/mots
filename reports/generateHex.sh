#!/bin/bash

for f in *.jasper ; do cat $f | xxd -pxxd -c 999999999 > $f.hex; done
