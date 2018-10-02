# XertPlay
Experiments with the Xert analytics library that takes power data as input

This is currently a simple project based on the Xert-provided sample.  The Xert library works by initializing it once and then feeding it live power data every 1 second.  This is accumulated by the library to calculate a number of metrics using some baseline data for the user.  There is a REST API available to interact with the Xert website to pull data for the user as input to the library init.

In this sample, the REST API is not used.  Data can be set with sliders on the main screen and then a sample set of power data is pushed into the library (without pausing for a second).  As a result, this runs 1000 samples very quickly and shows the calculated result. 

## Building the App
using standard gradle commands, for example
```gradle assembleDebug```
```gradle installDebug```
