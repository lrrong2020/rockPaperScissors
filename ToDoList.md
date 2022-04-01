#### 拓展功能

### **View**

1.  输入服务器IP地址并开始游戏 界面
2.  手动开始游戏（房主）按钮
3.  房主界面以及非房主界面相同 非房主手动选择游戏方式（几局几胜） 会出现警告弹窗
4.  游戏中界面结果（对方选择）显示
5.  防止误触退出 弹窗
6.  异常提示重连(手动/自动) 弹窗 按钮
7.  动画
8.  *跨平台 (Android, Windows)


### **Controller**

**前端** Controller 对接GUI和后端Controller

1. 页面切换
2. 控制按钮隐藏/显示
3. UI显示 (独立thread)


**后端** Controller Communication
4. 异常重连
5. timeout 独立thread



### **后端** + **Model**

1. 严格检查roundNo Access Control
2. 清理数据接口
3. 处理异常重连
   - 不一致
   - *断线
4. *DAO
5. *统计所有轮 (Round)的结果 展示此局 (Game)最终胜负

