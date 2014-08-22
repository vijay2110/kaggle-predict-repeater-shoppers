%==========================================================
% Initial model
%==========================================================


%==========================================================
% Load Data
%==========================================================
clear ; close all; clc;
fprintf('\nReading trnCustFeatures01.csv\n');
fflush(stdout);
trndata = load('C:\\kaggle\\01\\trnCustFeatures01.csv');
fprintf('\nReading crvCustFeatures01.csv\n');
fflush(stdout);
crvdata = load('C:\\kaggle\\01\\crvCustFeatures01.csv');
fprintf('\nReading tstCustFeatures01.csv\n');
fflush(stdout);
tstdata = load('C:\\kaggle\\01\\tstCustFeatures01.csv');
fprintf('\nReading RSL CustFeatures01.csv\n');
fflush(stdout);
rsldata = load('C:\\kaggle\\01\\rslCustFeatures01.csv');

%====================================================================
% Constructing X (feature matrix) and y (target/label column vector)
% Removing Noisy Data &
% Normalizing data
%====================================================================
fprintf('\nConstructing X and y\n');
fflush(stdout);
%column 6 is target value
%features are hand-picked after trying multiple combinations manually
X = trndata(:, [12 16 18 19 6]);
Xcrv = crvdata(:, [12 16 18 19 6]); 
Xtst = tstdata(:, [12 16 18 19 6]); 
Xrsl = rsldata(:, [12 16 18 19 6]); 

%Remove Noisy Data
%remove rows where column 1 vaules are 5 times more than column 1 mean
fprintf('\nRows: %g\n', size(X,1));
X = removePeaks(X,1,5);
fprintf('\nRows: %g\n', size(X,1));
%remove rows where column 2 vaules are 5 times more than column 2 mean
X = removePeaks(X,2,5);
fprintf('\nRows: %g\n', size(X,1));
%remove rows where column 3 vaules are 5 times more than column 3 mean
X = removePeaks(X,3,5);
fprintf('\nRows: %g\n', size(X,1));
%observe number of rows removed
fflush(stdout);

y = X(:, size(X,2));
X = X(:,1:(size(X,2)-1)); 
ycrv = Xcrv(:, size(Xcrv,2));
Xcrv = Xcrv(:,1:(size(Xcrv,2)-1)); 
ytst = Xtst(:, size(Xtst,2));
Xtst = Xtst(:,1:(size(Xtst,2)-1)); 
yrsl = Xrsl(:, size(Xrsl,2));  %ignore it (they are all zeros), we are predicting them
Xrsl = Xrsl(:,1:(size(Xrsl,2)-1)); 

%fprintf('\nNormalizing X \n');
[X, mu, sigma] = featureNormalize(X);

Xcrv = bsxfun(@minus, Xcrv, mu);
Xcrv = bsxfun(@rdivide, Xcrv, sigma);
Xtst = bsxfun(@minus, Xtst, mu);
Xtst = bsxfun(@rdivide, Xtst, sigma);
Xrsl = bsxfun(@minus, Xrsl, mu);
Xrsl = bsxfun(@rdivide, Xrsl, sigma);


%====================================================================
% Training Data by minimizing cost using Gradient Descent
%====================================================================

%Regularization parameter, setting it to zero as I have large data set, 
%so no chance to overfitting
lambda = 0;
%lambda_vec = [0 0.0001 0.0003 0.001 0.003 0.01 0.03 0.1 0.3 1 3 10 30 100 300 1000]';

% Initialize theta
initial_theta = zeros(size(X, 2), 1);

% Set options for fminunc
options = optimset('GradObj', 'on', 'MaxIter', 400);

fprintf('\nInitial Cost: %f', costFunctionReg(initial_theta, X, y, lambda));
fflush(stdout);

fprintf('\nStarting with fminunc\n');
fflush(stdout);
% Optimize
[theta, J, exit_flag] = ...
	fminunc(@(t)(costFunctionReg(t, X, y, lambda)), initial_theta, options);

fprintf('\nFinal Cost: %f', J);
fprintf('\nlambda: %g\n', lambda);
fprintf('\nTheta:\n %f', theta);
fflush(stdout);
	
	
%=======================================================================
% Compute F1 socres and accuracy over train data, crosss validation data
%=======================================================================
% Compute F1 and accuracy on training set
p = predict(theta, X);
fprintf('\nTrain Accuracy: %f\n', mean(double(p == y)) * 100);
[f1 acc] = calculateF1Score(p, y);
fprintf('\nTrain F1 score: %f\n', f1);
fprintf('\nTrain auc: %f\n', scoreAUC(y,p));
fflush(stdout);

% Compute F1 and accuracy on cross validation set
pcrv = predict(theta, Xcrv);
fprintf('\nCRV Train Accuracy: %f\n', mean(double(pcrv == ycrv)) * 100);
[f1 acc] = calculateF1Score(pcrv, ycrv);
fprintf('\nCRV F1 score: %f\n', f1);
fprintf('\nCRV auc: %f\n', scoreAUC(ycrv,pcrv));
fflush(stdout);

% Compute F1 and accuracy on test set
ptst = predict(theta, Xtst);
fprintf('\nTST Train Accuracy: %f\n', mean(double(ptst == ytst)) * 100);
[f1 acc] = calculateF1Score(ptst, ytst);
fprintf('\nTST F1 score: %f\n', f1);
fprintf('\nTST auc: %f\n', scoreAUC(ytst,ptst));
fflush(stdout);

%write predictions for final data set
prsl = predictFinal(theta, Xrsl);
XYans = [rsldata(:,1) prsl];
csvwrite('submission0101.csv', XYans);
fprintf('\nDone\n');
fflush(stdout);
