# FragmentRigger
<figure class="rigger">
<img src="images/R.png" width="150px"/>
<img src="images/i.png" width="150px"/>
<img src="images/g.png" width="150px"/>
<img src="images/g.png" width="150px"/>
<img src="images/e.png" width="150px"/>
<img src="images/r.png" width="150px"/>
</figure>

:boom:一个强大的Fragment框架。

![Release](https://img.shields.io/badge/release-1.0.0-brightgreen.svg)
![SDK](https://img.shields.io/badge/SDK-12%2B-green.svg)
![Build](https://img.shields.io/badge/build-passing-brightgreen.svg)
[![License](https://img.shields.io/badge/license-MIT-yellowgreen.svg)](https://opensource.org/licenses/MIT)
[![JingYeoh](https://img.shields.io/badge/author-JustKiddingBaby-red.svg)](http://blog.justkiddingbaby.com/)

>这可能是使用成本最低的Fragment框架了，无需继承！！！无需继承！！！无需继承！！！重要的话说三遍！！
在使用`FragmentRigger`的时候，使用成本只有一行注解！！！
原理是把`Fragment`/`Activity`生命周期相关方法定义为切点，通过ASpectJ绑定并使用代理类进行操作。

### 特性
- [x] **超强大Api支持**
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

### 使用例子
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