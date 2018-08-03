# FragmentRigger
![R](/images/rr.png)
![i](/images/i.png)
![g](/images/g.png)
![g](/images/g.png)
![e](/images/e.png)
![r](/images/r.png)

:boom:一个强大的Fragment框架。

![Platform](https://img.shields.io/badge/platform-Androd-green.svg)
![Download](https://api.bintray.com/packages/jkb/maven/fragment-rigger/images/download.svg)
![SDK](https://img.shields.io/badge/SDK-12%2B-green.svg)
![Build](https://img.shields.io/badge/Powered%20by-AsPectJ-blue.svg)
[![AsPectJ](https://img.shields.io/badge/license-MIT-yellowgreen.svg)](https://github.com/HujiangTechnology/gradle_plugin_android_aspectjx)
[![JingYeoh](https://img.shields.io/badge/author-JustKiddingBaby-red.svg)](http://blog.justkiddingbaby.com/)

**这可能是使用成本最低的Fragment框架。**  
**无需继承！！！无需继承！！！无需继承！！！** 重要的话说三遍！！   
在使用`FragmentRigger`的时候，使用成本只有一行注解！！！  
**原理：** 是把`Fragment`/`Activity`生命周期相关方法定义为切点，通过ASpectJ绑定并使用代理类进行操作。

### 演示
>本项目支持常见场景下的`Fragment`操纵方式，如有不支持的场景，欢迎提交[Issues](https://github.com/JustKiddingBaby/FragmentRigger/issues)或者[Email me](mailto:yangjing9611@foxmail.com)

|Stack manager|Show|Lazy loading|Replace|
|:-----------:|:-----:|:---------:|:------:|
|<img src="/images/start.gif" width = "200px"/>|<img src="/images/show.gif" width = "200px"/>|<img src="/images/lazyload.gif" width = "200px"/>|<img src="/images/replace.gif" width = "200px"/>|

### 目标
* 让Fragment的使用更简单.
* 以最低的成本去使用Fragment.

### Wiki
#### 开始
* [安装](https://github.com/JustKiddingBaby/FragmentRigger/wiki/首页)
* [开始使用](https://github.com/JustKiddingBaby/FragmentRigger/wiki/开始使用)
* [Fragment的操纵](https://github.com/JustKiddingBaby/FragmentRigger/wiki/Fragment的操纵)
* [自定义Fragment tag](https://github.com/JustKiddingBaby/FragmentRigger/wiki/%E8%87%AA%E5%AE%9A%E4%B9%89Fragment-tag)
* [懒加载](https://github.com/JustKiddingBaby/FragmentRigger/wiki/懒加载)
* [转场动画](https://github.com/JustKiddingBaby/FragmentRigger/wiki/转场动画)
* [onBackPressed拦截](https://github.com/JustKiddingBaby/FragmentRigger/wiki/onBackPressed拦截)
* [startFragmentForResult方法](https://github.com/JustKiddingBaby/FragmentRigger/wiki/startFragmentForResult方法)
* [滑动退出Activity/Fragment](https://github.com/JustKiddingBaby/FragmentRigger/wiki/滑动边缘退出)
* [如何在library module中使用](https://github.com/JustKiddingBaby/FragmentRigger/wiki/如何在library-module中使用)
#### 信息
* [gradle错误](https://github.com/JustKiddingBaby/FragmentRigger/wiki/gradle%E4%BE%9D%E8%B5%96%E9%97%AE%E9%A2%98)
* [代码混淆](https://github.com/JustKiddingBaby/FragmentRigger/wiki/代码混淆)
* [版本日志](https://github.com/JustKiddingBaby/FragmentRigger/wiki/版本日志)

### 特性
- [x] **超强大Api支持**
- [x] **足够多的英文注释**
- [x] **严格的异常抛出**
- [x] **解决Fragment中常见的异常及Bug**
- [x] **事务提交永不丢失**
- [x] **扩展原生方法，添加`onBackPressed`等常见的方法支持**
- [x] **当前栈成员树状图打印**
- [x] **Fragment懒加载**
- [x] **Fragment转场动画**
- [x] **自定义Fragment tag**
- [x] **为非栈内的Fragment添加onBackPressed支持**
- [x] **滑动边缘退出Activity/Fragment**
- [ ] **配置Fragment启动模式的选项**
- [ ] **Fragment间共享元素转场动画**
- [ ] **支持DialogFragment**

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
* ~~在ViewPager中的懒加载及其他场景下的懒加载~~
* ~~不同场景下转场动画不执行问题~~

### 使用样例
>本框架提供了强大的Api支持。  
和已有的大部分`Fragment`框架不同，**不需要继承任何父类**，只需要添加一行注解即可。  
在操纵`Fragment`的时候只需要通过代理类来操作，本框架完全采用一种**插入式** 的方式来降低使用成本。

**1、声明为框架的支持类**
>在需要使用本框架的`Activity/Fragment`上添加注解`@Puppet`即可。

```java
//MainActivity.java
@Puppet(containerViewId = R.id.atyContent)//containerViewId是你需要操纵的Fragment在add时候的container view
public class MainActivity extends AppCompatActivity
```
```java
//TestFragment.java
@Puppet
public class TestFragment extends Fragment
```

**2、使用框架操纵`Fragment`**
>添加`@Puppet`注解之后，在使用框架提供的`Fragment`操纵的方法的时候通过代理类`Rigger`来操纵。

```java
@Puppet(containerViewId = R.id.atyContent)
public class MainActivity extends AppCompatActivity{
  ...
  //add并show一个Fragment并添加至栈中，此时添加的Fragment是在@Puppet中的containerViewId中的
   Rigger.getRigger(this).startFragment(TestFragment.newInstance());
}
```


**3、Activity/Fragment 滑动退出**
>在需要滑动边缘退出的`Activity/Fragment`上添加`Swiper`注解.

```java
@Swiper
@Puppet
public class MainActivity extends AppCompatActivity{
    // swiper 只可以使用在有 puppet 注解的类上.
}
```
```java
@Swiper
@Puppet
public class TestFragment extends Fragment{
   // swiper 只可以使用在有 puppet 注解的类上.
}
```

### License
![](https://upload.wikimedia.org/wikipedia/commons/thumb/f/f8/License_icon-mit-88x31-2.svg/128px-License_icon-mit-88x31-2.svg.png)

本项目遵循MIT开源协议. 浏览[LICENSE](https://opensource.org/licenses/MIT)查看更多信息.