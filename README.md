# api-compliance-framework
Repository containing my project for Global Alliance for Genomics and Health as part of GSoC 2019


| Travis CI Status | Code Coverage |
| ------ | ------|
| [![Build Status](https://travis-ci.org/rkaushik15/api-compliance-framework.svg?branch=master)](https://travis-ci.org/rkaushik15/api-compliance-framework) | [![codecov](https://codecov.io/gh/rkaushik15/api-compliance-framework/branch/master/graph/badge.svg)](https://codecov.io/gh/rkaushik15/api-compliance-framework) |


# API Compliane Framework

The repository contains the source code of a generic framework which can be used to create API Compliance Tools and Test Suites to run on any implementation of servers which support the APIs. This can ensure that developers and server implementors follow the API specifications. 
The framework can also be used to generate a Compliance Report ([example]()), which conform to a specific [JSON Schema](). 


# Refget Compliance Suite

The repository also contains a compliance suite for refget api, developed using the framework. The suite can be run on any server implementing refget API to generate a compliance report. Also added are scripts to host these JSON reports in a human readable format as static webpages.

Before running the test suite, maven needs to be installed.

Check if maven is present

```
mvn -version
```

If maven info is displayed, maven is already present. Else install maven:

```
sudo apt install maven
```

To run the refget compliance suite and generate a compliance report, follow these steps:

 ```
 git clone https://github.com/rkaushik15/api-compliance-framework.git
 
 cd api-compliance-framework
 
 mvn clean test -Prefget
 ```
 
The report is generated and output to the `report` directory. To host the report on a local port, use the following command (python installation required):

 ```
 python report_hosting_script.py
 ```
 
To view the JSON report on terminal, follow these steps: 

 ```
 cd report
 
 cat results.json
 ```
 
# Important URLs (Refget)

[Refget Specification](http://samtools.github.io/hts-specs/refget.html)

[Compliance Document](https://compliancedoc.readthedocs.io/en/latest/)


# Framework Test Suite

The repository also includes a test suite to run unit tests for the compliance framework. To run this test suite, follow these steps:

```
 git clone https://github.com/rkaushik15/api-compliance-framework.git
 
 cd api-compliance-framework
 
 mvn clean test -Pframework
 ```
