#### 拓展功能

### **View**

1. 手动开始游戏（房主）按钮

2. 房主界面以及非房主界面相同 非房主手动选择游戏方式（几局几胜） 会出现警告弹窗

   *<u>bool canStartGame 控制"开始游戏"按钮 由Client.getIsHost决定</u>*, 选择后自动改为false

3. 游戏中界面结果（对方选择）图片显示

4. 异常提示重连(手动/自动) 弹窗 按钮

5. *动画

6. *跨平台 (Android, Windows)

7. 最后一张页面显示比分结果 自动跳转（几局结束）？

### **Controller-frontend**

**前端** Controller 对接GUI和后端Controller

1. 页面切换
2. 控制按钮隐藏/显示
3. **animation timer刷新display内容**

**后端** Controller Communication

4. *异常重连
5.  倒计时结束后再显示选择 / 提示无法选择



### **Controller-backend** + **Model**

1. 清理数据
   - Socket
   - Choices
2. 处理异常重连
   - *断线 (On exit?)
     1. Socket 重复使用?
   - *Timeout (A timer) - 心跳
3. 线程管理
4. *DAO
5. *统计所有轮 (Round)的结果 展示此局 (Game)最终胜负
5.  *每轮开始结束都要确认



### **Game Logic**

1.  模式选择
    - 随机选择
    - *提示对手已经选择
2.  统计数据
