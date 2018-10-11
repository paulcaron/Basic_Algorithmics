#Computing the Edit Distance Between Strings

Given two strings A and B (lengths m and n) this program computes the optimal sequence of edit operations that can transform A to B.
It uses three operations:
- insert (k,c)
- delete (k)
- replace (k,c)

The time complexity of the ealgorith is O(s x min(m,n)), where s is the length (number of elements) of the list returned by method getMinimalEditSequence.