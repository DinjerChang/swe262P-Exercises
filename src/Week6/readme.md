# Week6 Exercise
 There are two folders, "Twenty" for 20.1 assignment and "TwentySix" for 26.1 assignmnet
## Twenty Execution
This is for Plugin Framework  
Execution:  
make sure current path is "/home/runner/262P-Exercises"  

`cd Week6/Twenty/deploy`  

There are four combinations of plugins in config properties file
1. extractNormal + countNormal
2. extractNormal + countFirstLetter
3. extractZ + countNormal
4. extractZ + countFirstLetter

replace the plugin with your will, then  
`java -jar framework.jar ../../../pride-and-prejudice.txt`  
to see the result in console  

## TwentySix Execution
This is for DB spreadsheets  
Execution:  
make sure current path is "/home/runner/262P-Exercises"   
`cd Week6/TwentySix`  
`javac TwentySix.java`  
` java -cp ".:sqlite-jdbc-3.40.1.0.jar" TwentySix ../../pride-and-prejudice.txt`  
to see the result in console




