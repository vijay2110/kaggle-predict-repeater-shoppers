function p = predict(theta, X)

%predicts 0 or 1 for given data and theta

m = size(X, 1); % Number of training examples
p = zeros(m, 1);
p = round(sigmoid(X*theta));

end
