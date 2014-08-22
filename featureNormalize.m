function [X_norm, mu, sigma] = featureNormalize(X)
%nNormalizes the features 
mu = mean(X);
X_norm = bsxfun(@minus, X, mu);

sigma = std(X_norm);
X_norm = bsxfun(@rdivide, X_norm, sigma);

end
