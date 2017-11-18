#!/bin/sh

# Download latest NUR API libraries from GitHub.

GETTER=wget
SDK_REPO=nur_sdk
REMOTE_BASE="https://github.com/NordicID/$SDK_REPO/raw/master"
LOCAL_IMPORTDIR=import

mkdir -p import

$GETTER -q $REMOTE_BASE/java/ReleaseNotes.txt -O $LOCAL_IMPORTDIR/ReleaseNotes.txt
$GETTER -q $REMOTE_BASE/java/NurApi.jar -O $LOCAL_IMPORTDIR/NurApi.jar
