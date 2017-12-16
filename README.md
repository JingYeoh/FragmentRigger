# FragmentRigger
![R](/images/R.png)
![i](/images/i.png)
![g](/images/g.png)
![g](/images/g.png)
![e](/images/e.png)
![r](/images/r.png)

:boom:一个强大的Fragment框架。

![Platform](https://img.shields.io/badge/platform-Androd-green.svg)
![Release](https://img.shields.io/badge/release-1.0.0-brightgreen.svg)
![Download](https://api.bintray.com/packages/jkb/maven/fragment-rigger/images/download.svg)
![SDK](https://img.shields.io/badge/SDK-12%2B-green.svg)
![Build](https://img.shields.io/badge/build-passing-brightgreen.svg)
![Build](https://img.shields.io/badge/Powered%20by-AsPectJ-blue.svg)
[![AsPectJ](https://img.shields.io/badge/license-MIT-yellowgreen.svg)](https://github.com/HujiangTechnology/gradle_plugin_android_aspectjx)
[![JingYeoh](https://img.shields.io/badge/author-JustKiddingBaby-red.svg)](http://blog.justkiddingbaby.com/)

>这可能是使用成本最低的Fragment框架了，无需继承！！！无需继承！！！无需继承！！！重要的话说三遍！！
在使用`FragmentRigger`的时候，使用成本只有一行注解！！！
原理是把`Fragment`/`Activity`生命周期相关方法定义为切点，通过ASpectJ绑定并使用代理类进行操作。

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
- [ ] **Fragment间共享元素转场动画**

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
>本框架以**使用成本最低的Fragment框架**为目标，提供了强大的Api支持。
和已有的大部分`Fragment`框架不同，不需要继承任何父类，只需要添加一行注解即可。
在操纵`Fragment`的时候只需要通过代理类来操作，本框架完全采用一种插入式的方式来降低使用成本。

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
>我们没有继承类，只需要声明一个注解就可以使用本框架，在使用框架提供的`Fragment`操纵的方法的时候通过代理类来使用。

```java
@Puppet(containerViewId = R.id.atyContent)
public class MainActivity extends AppCompatActivity{
  ...
  //add并show一个Fragment并添加至栈中，此时添加的Fragment是在@Puppet中的containerViewId中的
   Rigger.getRigger(this).startFragment(TestFragment.newInstance());
}
```

### 运行效果
>本项目支持常见场景下的`Fragment`操纵方式，如有不支持的场景，欢迎提交[Issues](https://github.com/JustKiddingBaby/FragmentRigger/issues)或者[Email me ](mailto:yangjing9611@foxmail.com)

|栈管理|同级替换|
|:---:|:-----:|
|<img src="/images/start.gif" width = "200px"/>|<img src="/images/replace.gif" width = "200px"/>
|支持Fragment同级\多层嵌套，并提供返回自动显示栈顶成员等一系列场景支持|在一个`container`中只显示一个Fragment，对比原生的使用，提供强大并简易的Api支持|
|[StartFragment.java](/app/src/main/java/com/yj/app/test/start/StartFragment.java)|[ReplaceFragment.java](/app/src/main/java/com/yj/app/test/replace/ReplaceFragment.java)|

|同级显示|懒加载|
|<img src="/images/show.gif" width = "200px"/>|<img src="/images/lazyload.gif" width = "200px"/>|
通过`show`方法显示`Fragment`，支持预加载，懒加载等场景|支持`ViewPager`等场景下的懒加载机制，使用简单，一行注解就可以支持|
|[ShowFragment.java](/app/src/main/java/com/yj/app/test/show/ShowFragment.java)|[LazyLoadFragment.java](/app/src/main/java/com/yj/app/test/lazyload/LazyLoadFragment.java)

|栈内成员树状图|
|:----------:|
|<img src="/images/tree.png" width = "300px"/>|
|可在Log中实时查看自己栈内的成员并以树状图打印出栈内`Fragment tag`|
|[StartFragment.java](/app/src/main/java/com/yj/app/test/start/StartFragment.java)|

>上面的demo只是展示了部分常用的场景，主要是为了突出本框架强大的Api支持，一些针对`Fragment`的其他功能在上面几个demo中也有体现，
如：`转场动画`、`原生方法的扩展`等，详细使用请看Wiki。

### 如何配置
>本项目`AOP`的实现是通过`AsPectJ`来实现的，所以在配置本项目的同时需要加入`AsPectJ`的支持。

**1、在项目根`build.gradle`中添加**
```gradle
buildscript {
    dependencies {
        ...
        classpath 'com.hujiang.aspectjx:gradle-android-plugin-aspectjx:1.0.10'
    }
}
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
**2、在`application`的`build.gradle`中添加**
```gralde
apply plugin: 'android-aspectjx'
android{
  ...
}
```
**3、在需要支持本库的`library`的`build.gradle`中添加**
```gradle
compile 'com.justkiddingbaby:fragment-rigger:1.0.0'
```

### 发布日志
##### V1.0.0[2017/12/15]  
1、完成基础功能

### License
![](https://upload.wikimedia.org/wikipedia/commons/thumb/f/f8/License_icon-mit-88x31-2.svg/128px-License_icon-mit-88x31-2.svg.png)

本项目遵循MIT开源协议. 浏览[LICENSE](https://opensource.org/licenses/MIT)查看更多信息.