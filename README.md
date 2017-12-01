# FragmentRigger
:boom:一个强大的Fragment框架。
>这可能是使用成本最低的Fragment框架了，无需继承！！！无需继承！！！无需继承！！！重要的话说三遍！！
在使用`FragmentRigger`的时候，使用成本只有一行注解！！！
原理是把`Fragment`/`Activity`生命周期相关方法定义为切点，通过ASpectJ绑定并使用代理类进行操作。

### 特性
- [x] **超强大Api支持**
- [x] **解决Fragment中常见的异常及Bug**
- [x] **事务提交永不丢失**
- [x] **扩展原生方法，添加`onBackPressed`等常见的方法支持**
- [x] **当前栈成员树状图打印**
- [ ] **Fragment转场动画**
- [ ] **Fragment懒加载**

### 解决的问题
* ~~Fragment界面重叠~~
* ~~Fragment多级嵌套~~
* ~~Fragment栈的管理问题~~
* ~~Fragment事务提交失败~~
* ~~Activity在非onResume状态下提交事务~~
* ~~Fragment事务提交不能立即执行导致两次提交事件冲突~~
* ~~`内存重启`时的一系列异常~~
* ~~屏幕翻转时的数据保存及恢复~~
* ~~Can not perform this action after onSaveInstanceState~~
* 转场动画