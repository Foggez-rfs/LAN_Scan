#!/bin/sh
export ANDROID_HOME=$ANDROID_SDK_ROOT
exec gradle "$@"
