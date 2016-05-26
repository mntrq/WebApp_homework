lines = LOAD '/user/PP/web-Google.txt' AS (start:chararray, end:chararray);
links1 = FOREACH lines GENERATE FLATTEN(TOKENIZE(start)) AS start1,
  FLATTEN(TOKENIZE(end)) AS end1;
links2 = FOREACH lines GENERATE FLATTEN(TOKENIZE(start)) AS start2,
  FLATTEN(TOKENIZE(end)) AS end2;

joinLink = JOIN links1 BY end1, links2 BY start2;
joinFilter = FOREACH joinLink GENERATE start1, end2;

unionLinks = UNION joinFilter, links1;
unionGroup = GROUP unionLinks BY $0;

unionFilter = FOREACH unionGroup GENERATE group, unionLinks.$1;

store unionFilter into 'my_SL_pig_out';
