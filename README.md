# FragmentRigger
:boom:一个强大的Fragment框架。
>这可能是使用成本最低的Fragment框架了，无需继承！！！！无需继承！！！！无需继承！！！！重要的话说三遍！！
在使用`FragmentRigger`的时候，使用成本只有一行注解，原理是把`Fragment`/`Activity`生命周期相关方法
定义为切点，通过ASpectJ绑定并使用代理类进行操作。

### 特性：
- [x] **超强大Api支持**
- [x] **Fragment基础跳转**
- [x] **Fragment多层嵌套**
- [x] **解决Fragment中可能遇到的bug**
- [x] **解决在`内存重启`时可能发生的异常**
- [x] **事物提交永不丢失**
- [ ] **Fragment转场动画**
- [ ] **Fragment懒加载**
