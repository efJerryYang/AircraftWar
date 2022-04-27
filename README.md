# Aircraft War

## Content

- `lab-handout`

- `lab-submit`

  本目录涵盖了各次实验上传的文档，即除了工程代码以外的检查内容会包含在本目录下

- `out`

- `src`

- `test`

  本部分为测试代码，使用`JUnit5`

- `uml`

  本目录下存放有各次实验绘制的uml文件及导出png图片，是`lab-submit`部分的子集

## Description

- 完成通过难度来选择地图功能，提供音效设置选项
- 循环播放BGM，boss机出场播放boss机专有音乐，实现了停止播放功能
- 各要求的音效均已实现
- 火力道具生效，英雄机散射，子弹增加，可以叠加，随时间叠加层数下降
- 得分排行榜已实现，三个难度记录分别放在不同的json文件中，可以删除历史得分

## Note

* DAO模式，因为添加了gson作为数据操作的包，如果无法直接运行，需要添加lib/gson/目录下的jar包到Project Structure的依赖中
