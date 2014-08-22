function p = rfpredict(theta, X)

%Predicts 0 or 1 using Random Forest Approach
%vote goes to the majority among multiple decision trees

m = size(X, 1); % Number of training examples
p = zeros(m, 1);
sum_theta = zeros(size(X,1),1);
for j=1:size(theta,2)
	sum_theta = sum_theta + round(sigmoid(X*theta(:,j)));
end
p =  (sum_theta >= size(theta,2)/2);


end
