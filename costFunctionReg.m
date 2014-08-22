function [J, grad] = costFunctionReg(theta, X, y, lambda)
%Compute cost and gradient for logistic regression with regularization
%Cost is mean square error

m = length(y); % number of training examples

J = 0;
grad = zeros(size(theta));

h_temp = sigmoid(X*theta);
J = (sum((((-1).*y).*log(h_temp))-((1.-y).*log(1.-h_temp))))/m;
J = J + (lambda/(2*m)*(sum(theta.^2)-(theta(1,1)^2)));
temp_reg_term = ones(size(theta));
temp_reg_term(1,1) = 0;
temp_reg_term = temp_reg_term .* ((lambda/m).*theta);
grad = (X'*(h_temp.-y))./m;
grad = grad .+ temp_reg_term;
%fprintf('\nCost: %f', J);

end
