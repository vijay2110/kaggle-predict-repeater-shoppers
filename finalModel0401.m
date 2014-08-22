%====================================================================
%  Random Forest approach
%====================================================================


%====================================================================
% Constructing X (feature matrix) and y (target/label column vector)
% Removing Noisy Data &
% Add more features by taking squares and cubes of existing features
% Normalizing data
%====================================================================
fprintf('\nConstructing X and y\n');
fflush(stdout);

%few combinations of features which are mannualy picked based on their accuracy and F1 score
X = dataSet(:, [12 46 48 49 53 30 40 59 62 16 18 19 41 43 44 60 61 63 64 51 52 6]);
Xrsl = rsldata(:, [12 46 48 49 53 30 40 59 62 16 18 19 41 43 44 60 61 63 64 51 52 6]);

%remove noisy data
%remove rows where column 1 vaules are 5 times more than column 1 mean
X = removePeaks(X,1,5);
fprintf('\nRows: %g\n', size(X,1));
%remove rows where column 6 vaules are 7 times more than column 6 mean
X = removePeaks(X,6,7);
fprintf('\nRows: %g\n', size(X,1));
%remove rows where column 7 vaules are 7 times more than column 7 mean
X = removePeaks(X,7,7);
fprintf('\nRows: %g\n', size(X,1));
fflush(stdout);

nCols=size(X,2);

y = X(:, nCols);
X = X(:,1:nCols-1); 
yrsl = Xrsl(:, 6); 
Xrsl = Xrsl(:,1:nCols-1); 

%add more features by taking squares and cubes of existing features
nCols = size(X,2);
X = [X(:,1:nCols).^2 X(:,2:nCols).^3 X];
Xrsl = [Xrsl(:,1:nCols).^2 Xrsl(:,2:nCols).^3 Xrsl];

no_of_decision_trees=200;
random_data_set_size=10000;

%normalize datasets
fprintf('\nNormalizing X \n');
[X, mu, sigma] = featureNormalize(X);


%====================================================================
% Finding out good initial theta by training subsets of data mutiple times
%====================================================================
nRows = size(X,1);
nSample = 10000;
initial_theta = zeros(size(X, 2), 1);
%set it to 0 initially
lambda = 0;
optimizedScore = 0;
%Calculate good initial theta
for i=1:100
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
	endif
end
fprintf('\nOptimized score : %f\n', optimizedScore);
fprintf('\nOptimized initial theta : \n%f\n', initial_theta);
fflush(stdout);


%====================================================================
% Now find out optimal lambda
%====================================================================
lambda_vec = [0 0.0001 0.0003 0.001 0.003 0.01 0.03 0.1 0.3 1 3 10 30 100 300 1000]';
optimizedLambda = 0;
optimizedScore = 0;
for i=1:size(lambda_vec,1)
	rndIDX = randperm(nRows); 
	Xtemp = X(rndIDX(1:nSample), :);
	ytemp = y(rndIDX(1:nSample), :);
	thetaTemp = trainLogisticReg(Xtemp,ytemp,lambda_vec(i,1), initial_theta);
	Xtemp = X(rndIDX(nSample+1:nSample*2), :);
	ytemp = y(rndIDX(nSample+1:nSample*2), :);
	temp_p = sigmoid(Xtemp*thetaTemp);
	tempAUC = scoreAUC(ytemp,temp_p);
	if (tempAUC>optimizedScore)
		optimizedScore=tempAUC;
		optimizedLambda=lambda_vec(i,1);
	endif
end
lambda = optimizedLambda;
fprintf('\nOptimized lambda : %f\n', optimizedLambda);
fflush(stdout);


%====================================================================
% Now build random forest decision trees
%====================================================================
theta = zeros(size(X, 2), no_of_decision_trees);

nSample = 10000;
for i=1:no_of_decision_trees
	rndIDX = randperm(nRows); 
	Xtemp = X(rndIDX(1:nSample), :);
	ytemp = y(rndIDX(1:nSample), :);
	theta(:,i) = trainLogisticReg(Xtemp,ytemp,lambda,initial_theta);
end

%=======================================================================
% Compute accuracy over train data
%=======================================================================
% Compute accuracy on training data set	
p = predict(theta, X);
fprintf('\nTrain Accuracy: %f ', mean(double(p == y)) * 100);
fprintf('\nTRN auc: %f\n', scoreAUC(y,p));
fflush(stdout);

%write predictions for final data set
Xrsl = bsxfun(@minus, Xrsl, mu);
Xrsl = bsxfun(@rdivide, Xrsl, sigma);
prsl = predict(theta, Xrsl);
XYans = [rsldata(:,1) prsl];
csvwrite('submission0101.csv', XYans);
fflush(stdout);
