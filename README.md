# Modified Dirichlet Distribution

This program implements the modified Dirichlet distribution and its mode finding algorithm as described in:

Kewei Tu, "[Modified Dirichlet Distribution: Allowing Negative Parameters to Induce Stronger Sparsity](http://sist.shanghaitech.edu.cn/faculty/tukw/emnlp16mdir.pdf)". In the Conference on Empirical Methods in Natural Language Processing (EMNLP 2016). 

Modified Dirichlet distribution is an extension of the Dirichlet distribution that allows the parameters to be negative. It not only induces much stronger sparsity, but also simultaneously performs smoothing. It is still conjugate to the multinomial distribution.

If you use this code in your scientific work, please cite my paper.
```
@inproceedings{tu2016modified,
  title={Modified Dirichlet Distribution: Allowing Negative Parameters to Induce Stronger Sparsity},
  author={Tu, Kewei},
  booktitle={Conference on Empirical Methods in Natural Language Processing (EMNLP 2016), Austin, Texas, USA},
  year={2016}
}
```
