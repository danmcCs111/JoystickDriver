#!/bin/bash
mvnDir=$1
orgDir=`pwd`
cd "$(dirname "$0")"

$mvnDir clean install dependency:copy-dependencies

cd $orgDir
