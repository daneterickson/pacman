## Description

Currently, the game score is not updating properly to its initial value as defined by the data file.

## Expected Behavior

The score should change as the data file used changes. For example, if the data file says player
score should be 5, then the actual score should also start at 5.

## Current Behavior

Score automatically starts at zero.

## Steps to Reproduce

1. Run the program with any file that has a nonzero initial score (i.e. high_score.json)
2. Click play and see that the score still starts at 0.

## Failure Logs

Failing test scoreInputTest in GameBoardTest, as well as failing test .

## Hypothesis for Fixing the Bug

This issue should be fixed by adding the setupPlayerScore() method into the parseJSON() method
inside JsonParser, as well as setting myPacScore in GameBoard to the score value from data.