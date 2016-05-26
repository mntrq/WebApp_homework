links = LOAD '/user/PP/web-Google.txt' AS (start:chararray, end:chararray);
grouped = GROUP links BY end;
linkcount = FOREACH grouped GENERATE group, COUNT(links);
store linkcount into 'my_CL_pig_out';
