# Todo

## Tutorial

- [ ] Java小游戏开发逻辑 https://zhuanlan.zhihu.com/p/59600990 
- [ ] 控制台窗口设计(beanshell)

## Game Setting

![image-20220321120554609](assets/image-20220321120554609.png)

## Commit

### lab 1

- [x] 每隔一定周期随机产生一架普通敌机或精英敌机
- [x] 精英敌机按设定周期发射子弹
- [x] 精英敌机坠毁后随机产生某种道具（或不产生道具）
- [x] 英雄机碰撞道具后，道具自动生效，其中加血道具生效时可使英雄机恢复一
  定血量，火力道具和炸弹道具无需具体实现，生效时只需打印“FireSupply
  active!”“BombSupply active!”语句即可。
- [x] 代码重构
- [x] 精英机左右移动，只有精英机产生道具，添加boss机
- [x] 使用源码解析器生成PlantUML代码，用于生成puml图
- [x] 绘制uml图，补注释，lab1完成后，branch，提交

---

- [x] 火力道具和炸弹道具生效
- [x] 设置所需要属性的setter和getter，方便调试，预留控制台窗口触发事件位置
- [x] 敌机通过屏幕下方扣除分数，分数扣除取决于敌军血量和类别
- [x] boss机子弹个数

### lab 2

- [x] 实验前修改代码
- [x] 英雄机单例模式
- [x] 敌机工厂模式
- [x] 道具工厂模式
- [x] 将属性隐藏在工厂中

### lab 3

- [x] 单元测试

- [x] 插件扫描代码并修复大部分

  > 剩余了一个线程池`CRITICAL`，和代码块过长的`MAJOR`

---

- [x] 修改英雄机和BOSS机弹道
- [x] 添加音频

### lab 4

- [x] 使用策略模式改写火力道具触发代码
- [x] 使用数据访问模式写成绩表

---

- [x] rename所有的`difficulty`为`level`
- [x] 将代码逻辑改为只有读取的时候才需要进行排序，添加有序标记
- [ ] 添加敌机击毁的爆炸图像
- [ ] 添加护盾逻辑（加满血后再回血就套上护盾）

lab5

- [x] 控制道具作用时长，效果逐级消退~~/直接初始化~~

lab6

- [x] 将三种敌机的关系从继承改为独立的，修改抽象敌机类
- [ ] 修改策略模式的实现方式
  - [ ] 英雄机射击和敌机射击本来应该分开
  - [ ] 原来的shoot方法的用处还没有想清楚
- [x] 观察者模式重写炸弹道具
- [x] 模板模式重写game代码
  - [x] 封装gameTask
  - [x] 封装timeCounter
  - [x] 添加难度控制
    - 时间间隔10000个单位提升难度一次，提升的幅度就从level入手
    - `level = baseLevel * (time/1000000.0 + levelScalar) `
- [x] 添加敌机血条
- [x] 添加Boss机血量呈现
- [ ] 绘制道具持续时间条
  - [x] 一个是子弹阶段道具，绘制不同的色层，加上角标说明是第几阶段，初始为0，灰色
    - [x] 0阶段：GRAY
    - [x] 1阶段：BLUE
    - [x] 2阶段：CYAN
    - [x] 3阶段：MAGENTA

  - [ ] 另一个是保护盾道具，同样绘制不同的色层，加上角标说明是第几阶段
    - [x] 0阶段：红色默认血条
    - [ ] 1阶段：绿色血条，先加血100，持续一段时间的缓慢回血
    - [x] 2阶段：黄色血条，持有护盾，碰撞敌机要加分
    - [ ] 3阶段：xx血条，回血护盾，碰撞扣除敌机的血量一半用于回复自己
- [x] 将原来用type的改成instanceof，或者调用class的equals
- [x] 带随机性的道具位置生成，主要是Boss类的道具掉落

### bug:

- [x] 鼠标点击可以导致瞬移，似乎是直接使用拖拽的必然bug，需要重新考虑拖拽的实现方式

  > 用move和drag同时作用于鼠标可以解决这一问题

- [x] ~~炸弹道具使用时没有触发道具生成~~（其实也合理，炸没了哪里来的道具）

- [x] 刷新频率与子弹、敌机产生速度绑定，慢速模式下存在卡顿

  > 不使用刷新频率来控制难度，而使用飞机本身的参数

- [x] 运行一段时间后（通常出现在击毁几架boss机后）再无敌机生成

  > 原因在于达到maxEnemyNum之后产生了boss机的问题，已修改`<`为`<=`
  > 

- [ ] 线程池`CRITICAL`问题修复

- [x] 多线程修复音频不同步的问题

- [x] 生成的敌机如果出现在界面边缘会有闪烁现象，导致无法左右移动

  > 原因在于生成的敌机超出左右边界过多时，原有的ObstractFlyingObject.forward()方法没有合理的处理locationX，只处理了speed故会导致反复的左右移动闪烁出现。

- [ ] 部分生成的敌机不能被炸弹清除

- [ ] 有时候突然在空白处出现散射敌机子弹现象，可能是boss机的问题

- [ ] 护盾道具的效果还有不明确的地方，对于道具生成的部分还是比较模糊
