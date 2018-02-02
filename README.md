# DfaMin

Write a C or Java program to minimize the DFA using Hopcroft’s algorithm.
Objective: learner will be able to implement partitioning; set union operation in
turn minimizes the number of states in the given DFA.
Prerequisite: learner should be able to apply Hopcroft's algorithm
Pre-lab exercise: implement the set union operation on a set of sets.
Procedure – Hopcrofts Algorithm :
1. Initially start with two partitions of states: the set of all final, and the non-final
states.
2. Repeat
{ For each partition updated by the previous iteration,
 Examine the transitions for each state on each input symbol
 If any two states in a partition leading to different partitions on same symbol
Further divide and update the partitions
} until (no new partition is created)
