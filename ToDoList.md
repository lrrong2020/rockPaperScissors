#### 拓展功能

### **View**

1.  输入服务器IP地址并开始游戏 界面
2.  手动开始游戏（房主）按钮
3.  防止误触退出 弹窗
4.  异常提示重连(手动/自动) 弹窗 按钮
5.  动画
6.  *跨平台 (Android, Windows)

### **Controller-frontend**

**前端** Controller 对接GUI和后端Controller

1. 页面切换
2. 控制按钮隐藏/显示
3. UI显示 (独立thread)


**后端** Controller Communication
4. 异常重连
5. timeout 独立thread



### **Controller-backend** + **Model**

1. 清理数据接口
2. 处理异常重连
   - 不一致
   - *断线
     1. 异常类
     2.  argument显示
3.  线程管理
4. *DAO
5. *统计所有轮 (Round)的结果 展示此局 (Game)最终胜负

