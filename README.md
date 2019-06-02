# 之花
本App旨在方便诗词爱好者对诗词的快速搜索，同时提供了对对联等趣味内容。

## 主要功能：

- 对对联：利用seq2seq模型和海量诗词数据集进行训练，根据输入的诗句智能地匹配出下联。
- 诗词搜索：提供多种搜索模式进行检索。
- 每日推荐：自行开发WebAPI，每日更新一次推送内容。只需访问接口就可以获得数据，同时提供最近十天的推送。
- 古诗词典：对一首古诗的详细介绍，包含注释、创作背景、赏析等等。

## 未实现功能：

- 诗词分类：由于未熟悉CheckBox的使用，该功能暂时无法实现。

## 引用的开源项目

- [seq2seq-couplet）](https://github.com/wb14123/seq2seq-couplet）
- [MaterialViewPager](https://github.com/florent37/MaterialViewPager)
- [CardSearchView](https://github.com/limuyang2/CardSearchView)