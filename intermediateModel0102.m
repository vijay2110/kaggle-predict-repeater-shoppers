%==========================================================
% Train with Randomly initialized theta
%==========================================================

%==========================================================
% Load Data
%==========================================================
% Initialization
clear ; close all; clc;
fprintf('\nReading trnCustFeatures01.csv\n');
trndata = load('C:\\kaggle\\01\\trnCustFeatures01.csv');
fprintf('\nReading crvCustFeatures01.csv\n');
crvdata = load('C:\\kaggle\\01\\crvCustFeatures01.csv');
fprintf('\nReading tstCustFeatures01.csv\n');
tstdata = load('C:\\kaggle\\01\\tstCustFeatures01.csv');
fprintf('\nReading RSL CustFeatures01.csv\n');
rsldata = load('C:\\kaggle\\01\\rslCustFeatures01.csv');
%merging cros validation dataset into training dataset, as I already found 
%optimal value of lambda which is 0
trndata = [trndata; crvdata]; 

%====================================================================
% Constructing X (feature matrix) and y (target/label column vector)
% Removing Noisy Data &
% Add more features by taking squares and cubes of existing features
% Normalizing data
%====================================================================
fprintf('\nConstructing X and y\n');
fflush(stdout);
%few combinations of features which are mannualy picked based on their accuracy and F1 score
%12 30 40 16 18 19 41 43 44 46 48 49 59 60 61 62 63 64 6
%12 30 40 16 17 18 19 20 41 42 43 44 45 46 47 48 49 50 51 52 53 59 60 61 62 63 64 6
%12 30 40 16 18 19 41 43 44 46 48 49 59 60 61 62 63 64 51 52 53 6
X = trndata(:, [12 46 48 49 53 30 40 59 62 16 18 19 41 43 44 60 61 63 64 51 52 6]);
Xtst = tstdata(:, [12 46 48 49 53 30 40 59 62 16 18 19 41 43 44 60 61 63 64 51 52]); 
ytst = tstdata(:, 6);
Xrsl = rsldata(:, [12 46 48 49 53 30 40 59 62 16 18 19 41 43 44 60 61 63 64 51 52]); 
yrsl = rsldata(:, 6);

%remove noisy data
fprintf('\nRows: %g\n', size(X,1));
%remove rows where column 1 vaules are 3 times more than column 1 mean
X = removePeaks(X,1,3);
fprintf('\nRows: %g\n', size(X,1));
%remove rows where column 6 vaules are 5 times more than column 6 mean
X = removePeaks(X,6,5);
fprintf('\nRows: %g\n', size(X,1));
%remove rows where column 7 vaules are 5 times more than column 7 mean
X = removePeaks(X,7,5);
fprintf('\nRows: %g\n', size(X,1));
%observe number of rows removed

y = X(:, size(X,2));
X = X(:,1:(size(X,2)-1)); 

%add more features by taking squares and cubes of existing features
nCols=size(X,2);
X = [X(:,1:nCols).^2 X(:,8:nCols).^3 X];
Xtst = [Xtst(:,1:nCols).^2 Xtst(:,8:nCols).^3 Xtst];
Xrsl = [Xrsl(:,1:nCols).^2 Xrsl(:,8:nCols).^3 Xrsl];

%normalize datasets
[X, mu, sigma] = featureNormalize(X);
Xtst = bsxfun(@minus, Xtst, mu);
Xtst = bsxfun(@rdivide, Xtst, sigma);
Xrsl = bsxfun(@minus, Xrsl, mu);
Xrsl = bsxfun(@rdivide, Xrsl, sigma);


%====================================================================
% Finding out good initial theta by training subsets of data mutiple times
%====================================================================
%Regularization parameter, setting it to zero as I have large data set, 
%so no chance to overfitting
lambda = 0;

nRows = size(X,1);
%number of samples 
nSample = 10000;
initial_theta = zeros(size(X, 2), 1);
optimizedScore = 0;
%store optimal initial theta for which we got optimal auc score
optimizedTheta = initial_theta;

for i=1:100
	%randomly initiate initial theta, use idea epsilon value 0.12
	temp_initial_theta = rand(size(X, 2), 1)*2*0.12 .- 0.12;
	rndIDX = randperm(nRows); 
	Xtemp = X(rndIDX(1:nSample), :);
	ytemp = y(rndIDX(1:nSample), :);
	thetaTemp = trainLogisticReg(Xtemp,ytemp,lambda,temp_initial_theta);
	Xtemp = X(rndIDX(nSample+1:nSample*2), :);
	ytemp = y(rndIDX(nSample+1:nSample*2), :);
	temp_p = sigmoid(Xtemp*thetaTemp);
	tempAUC = scoreAUC(ytemp,temp_p);
	if (tempAUC>optimizedScore)
		optimizedScore=tempAUC;
		initial_theta=temp_initial_theta;
		optimizedTheta = thetaTemp;
	endif
end
fprintf('\nOptimized score : %f\n', optimizedScore);
fprintf('\noptimizedTheta :\n%f\n', optimizedTheta);
fflush(stdout);

%====================================================================
%Now train whole dataset again with good initial theta
%====================================================================

% Set Options
options = optimset('GradObj', 'on', 'MaxIter', 1000);

fprintf('\nInitial Cost: %f', costFunctionReg(initial_theta, X, y, lambda));

fprintf('\nStarting with fminunc\n');
% Optimize
[theta, J, exit_flag] = ...
	fminunc(@(t)(costFunctionReg(t, X, y, lambda)), initial_theta, options);

fprintf('\nFinal Cost: %f', J);
fprintf('\nlambda: %g\n', lambda);
fprintf('\ntheta:\n%f \n', theta);
fflush(stdout);

%=======================================================================
% Compute F1 socres and accuracy over train data, test data
%=======================================================================	
% Compute F1 score and accuracy on training data set
p = predict(theta, X);
fprintf('\nTrain Accuracy: %f\n', mean(double(p == y)) * 100);
[f1 acc] = calculateF1Score(p, y);
fprintf('\nTrain F1 score: %f\n', f1);
fprintf('\nTRN auc: %f\n', scoreAUC(y,p));
fflush(stdout);

% Compute F1 score and accuracy on test data set
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
fflush(stdout);
