function g = sigmoid(z)
%compute sigmoid function

g = zeros(size(z));
g = -1.*z;
g = e.^g;
g = 1 .+ g;
g = 1./g;

end
