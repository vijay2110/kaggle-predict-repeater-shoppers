%==========================================================
% Load Data for random forest approach
%==========================================================

clear ; close all; clc;
fprintf('\nReading trnCustFeatures01.csv \n');
trndata = load('C:\\kaggle\\01\\trnCustFeatures01.csv');
fprintf('\nReading crvCustFeatures01.csv \n');
crvdata = load('C:\\kaggle\\01\\crvCustFeatures01.csv');
fprintf('\nReading tstCustFeatures01.csv \n');
tstdata = load('C:\\kaggle\\01\\tstCustFeatures01.csv');
fprintf('\nReading RSL CustFeatures01.csv \n');
rsldata = load('C:\\kaggle\\01\\rslCustFeatures01.csv');
fprintf('\nCombining data sets \n');

%combining all datasets as in Random Forest approach I am never 
%going to traing entire data together
%training samples are going to be picked randomly
dataSet = [trndata; crvdata; tstdata];

fprintf('\nLoaded all datasets\n');
clear trndata crvdata tstdata;
size(rsldata)
size(dataSet)
fflush(stdout);
