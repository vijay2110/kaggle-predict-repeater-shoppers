function Xr = removePeaks(X,id,mfactor)

%Removes noisy data
%removes rows where column id values are mfactor times more than column id mean

Xr = X;
mu = mean(X(:,id));
idx = (X(:,id)<(mu*mfactor));
Xr = X(idx, :);
