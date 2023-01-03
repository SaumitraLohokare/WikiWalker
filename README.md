# WikiWalker
A naive solution to solve The Wiki Game in Java.

## How it works
- I used the Jsoup library to get and parse wikipedia links.
- I created a graph of links. For each link that I explored, I added a node with an edge that goes back to the parent link. (A parent link is the direct predecessor of the link we are exploring)
- While exploring links if I detect the target link I stop the graph generation and move on to backtracking the solution.
- The `solve()` method returns a stack of URLs that we can use to find the clicks needed from the start to the target.

I hope I was able to explain that good enough for you to understand :sweat_smile:

## Sample run
```
Enter the start link: https://en.wikipedia.org/wiki/Distillation
Enter the target link: https://en.wikipedia.org/wiki/Water

Generating a graph of wikipedia URLs...
Exploring https://en.wikipedia.org/wiki/Distillation
Exploring https://en.wikipedia.org/wiki/Distillation_(disambiguation)
...
Exploring https://en.wikipedia.org/wiki/Mixture
Found the target!
Backtracking to find the solution with least clicks...

========== Solution ==========

It should take 2 clicks!

Starting here: { Distillation | Link: https://en.wikipedia.org/wiki/Distillation }
Click on: { mixture | Link: https://en.wikipedia.org/wiki/Mixture }
Click on: { water | Link: https://en.wikipedia.org/wiki/Water }
```

## Suggestions and Contribution
I am always open to suggestions and constructive criticism! If you have any please open an issue on this repository or you can message me personally on [twitter](https://twitter.com/slohoka).
 
If you would like to improve the code feel free to open a pull request! I love collaboration!
