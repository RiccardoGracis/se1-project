# Project Estimation  
Authors: Rocco Luca Iamello, Massimo Di Natale, Paolo Trungadi, Riccardo Gracis

Date: 28/04/2021

Version: 1.10
# Contents
- [Estimate by product decomposition]
- [Estimate by activity decomposition ]
# Estimation approach
<Consider the EZGas  project as described in YOUR requirement document, assume that you are going to develop the project INDEPENDENT of the deadlines of the course>
# Estimate by product decomposition
### 
|             | Estimate                        |             
| ----------- | ------------------------------- |  
| NC =  Estimated number of classes to be developed   |        15                   |             
|  A = Estimated average size per class, in LOC       |         200 LOC                  | 
| S = Estimated size of project, in LOC (= NC * A) |  3000 LOC|
| E = Estimated effort, in person hours (here use productivity 10 LOC per person hour)  |       300 ph         |   
| C = Estimated cost, in euro (here use 1 person hour cost = 30 euro) | 9000€ | 
| Estimated calendar time, in calendar weeks (Assume team of 4 people, 8 hours per day, 5 days per week ) |  ~2 calendar weeks (1,87 rounded up)      |               
# Estimate by activity decomposition
### 
|         Activity name    | Estimated effort (person hours)   |             
| ----------- | ------------------------------- | 
|  **Requirements**  | |
| Perform work analysis | 16 |
| Model Process | 6 |
| Identify user requirements  | 32 |
| Identify performance requirements | 6 |
| _Sub-total Requirements_ | _60_ |
| **Design** |  |
| Architecture | 8 |
| High level design | 8 |
| Low level design | 24 |
| Verification | 8 |
| _Sub-total Design_ | _48_ |
| **Coding** |  |
| General coding | 96 |
| Exceptions handling coding | 32 |
| _Sub-total Coding_ | _128_ |
| **Testing** |  |
| Unit Testing | 24 |
| Integration Testing | 40 |
| _Sub-total Testing_ | _64_ |
|  |  |
| **TOTAL** | **300** |

###
Insert here Gantt chart with above activities
```plantuml
@startgantt
title Gantt chart
hide footbox
project starts the 2021/04/05
saturday are closed
sunday are closed

[Requirements] lasts 8 days
[Perform work analysis] lasts 2 days and starts at [Requirements]'s start with blue link
then [Model Process] lasts 1 days
then [Identify user requirements] lasts 4 days
then [Identify performance requirements] lasts 1 days

-- Requirements completed --

[Design] lasts 6 days and starts at [Requirements]'s end with green link
[Architecture] lasts 1 day and starts at [Design]'s start with blue link
then [High Level] lasts 1 days
then [Low Level] lasts 3 days
then [Verification] lasts 1 days
-- Design completed --

[Coding] lasts 16 days and starts at [Design]'s end with green link
[General coding] lasts 12 day and starts at [Coding]'s start with blue link
then [Exceptions handling coding] lasts 4 days

-- Coding completed --

[Testing] lasts 8 days and starts at [Coding]'s end with green link
[Unit testing] lasts 3 day and starts at [Testing]'s start with blue link
then [Integration Testing] lasts 5 days
-- Testing completed --

[Requirements] is colored in Red
[Design] is colored in Orange
[Coding] is colored in DarkViolet
[Testing] is colored in Brown


caption We are considering working days of 8ph
@endgantt

```
