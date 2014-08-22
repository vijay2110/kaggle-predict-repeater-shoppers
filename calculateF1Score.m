function [f1 acc] = calculateF1Score(predictions, yval)
%precision = true positives / (true positives + false positives)
%recall = true positives / (true positives + false negatives)
	tp = sum((predictions == 1) & (yval == 1));
	tn = sum((predictions == 0) & (yval == 0));
    fp = sum((predictions == 1) & (yval == 0));
    fn = sum((predictions == 0) & (yval == 1));
    precision = tp/(tp + fp);
    recall = tp/(tp + fn);
	
	%compute F1 score and accuracy
    f1 = (2 * precision * recall) /(precision + recall);
	acc = (tp + tn)/(tp + tn + fp + fn);

end
