# Aircraft War

## Content

- archive

  存档实验作业和课件，以及部分未完成`Todo`

  - `lab-handout`

  - `lab-submit`

    本目录涵盖了各次实验上传的文档，即除了工程代码以外的检查内容会包含在本目录下

- `handout`

  实践阶段（安卓开发）的课件

- `lib`

- `out`

- `src`

- `test`

  本部分为测试代码，使用`JUnit5`

- `uml`

  本目录下存放有各次实验绘制的uml文件及导出png图片，是`lab-submit`部分的子集

## Description

结果演示：

![image-20220506121833808](assets/image-20220506121833808.png)

难度控制：

| 进度 | 内容                 | 简单                                       | 普通                                                                        | 困难                                                          |
| ---- | -------------------- | ------------------------------------------ |---------------------------------------------------------------------------|-------------------------------------------------------------|
| √    | Boss敌机             | 无                                         | 有<br />血量固定                                                               | 有<br />血量逐次提升                                               |
| √    | 难度是否随时间增加   | 否                                         | 是                                                                         | 是                                                           |
| √    | 敌机最大数目         | 3(固定)                                    | 5                                                                         | 7                                                           |
| √    | 敌机初始速度         | Mob: 5<br />Elite: 3                       | Mob: 7<br />Elite: 4                                                      | Mob: 10<br />Elite: 6                                       |
| √    | 敌机初始血量         | Mob: 30<br />Elite: 60                     | Mob: 30<br />Elite: 84<br />Boss: 4000                                    | Mob: 30<br />Elite: 120<br />Boss: 8000                     |
| √    | 敌机产生概率         | Mob: 70%<br />Elite: 30%                   | Mob: 50%<br />Elite: 50%                                                  | Mob: 30%<br />Elite: 70%                                    |
| √    | Boss敌机产生         | 不产生                                     | 每获得500 \* level 分                                                         | 每获得500 * level / 3 分                                        |
| √    | 敌机越过本方防线扣分 | 不扣分                                     | Mob: 0.5倍hp<br />Elite: 0.75倍hp                                           | Mob: 0.75倍hp<br />Elite: 0.875倍hp                           |
| √    | 道具产生概率         | Blood: 33%<br />Bomb: 33%<br />Bullet: 33% | Blood: 30%<br />Bomb: 30%<br />Bullet: 30%                                | Blood: 20%<br />Bomb: 20%<br />Bullet: 30%                  |
| √    | 道具效果消退时间     | 333单位                                    | 285单位                                                                     | 222单位                                                       |
| √    | 加血道具的护盾效果   | 碰撞敌机不减少持续时间                     | 碰撞后道具有效时间减去敌机血量<br />Elite和Mob损毁，Boss减少与剩余时间相关的血量<br />碰撞后道具有效时间至少保持5时间单位 | 碰撞后道具有效时间减去敌机血量<br />敌机减少与剩余时间相关的血量<br />碰撞后道具有效时间至少保持5时间单位 |

## Note

* DAO模式，因为添加了gson作为数据操作的包，如果无法直接运行，需要添加lib/gson/目录下的jar包到Project Structure的依赖中
