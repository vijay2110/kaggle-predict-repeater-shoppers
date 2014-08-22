function p = predictFinal(theta, X)

m = size(X, 1); % Number of training examples
p = zeros(m, 1);
p = sigmoid(X*theta); %not rounded, in order to get little better AUC score

end
