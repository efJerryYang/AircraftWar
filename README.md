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

本次实验任务完成如下：

* 使用策略模式重新封装shoot方法
  * boss机散射
  * 英雄机初始时为直射，在三个火力道具补充后英雄机散射且子弹加速伤害*2
  * 所有的操作由Context的实例决定，本例中为heroContext和enemyContext
* 使用数据访问对象模式操作成绩表数据
  * Record类为数据的一跳记录
  * RecordDAO为数据访问对象接口
  * RecordDAOImpl为数据访问对象的实现
  * 使用`gson`包用于读写`json`数据

## Note

* 主程序执行，因为添加了背景音乐和音效，如果无法直接运行，需要添加AircraftWar目录下的lib和lib下的jar_files到Project Structure的依赖中
* DAO模式，因为添加了gson作为数据操作的包，如果无法直接运行，需要添加lib/gson/目录下的jar包到Project Structure的依赖中
