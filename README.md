# FragmentRigger
![R](/images/R.png)
![i](/images/i.png)
![g](/images/g.png)
![g](/images/g.png)
![e](/images/e.png)
![r](/images/r.png)

:boom:A powerful library to manage Fragments.([Wiki](https://github.com/JustKiddingBaby/FragmentRigger/wiki))    
一个强大的Fragment管理框架。（[中文版README](README-CN.md)）

![Platform](https://img.shields.io/badge/platform-Androd-green.svg)
![Release](https://img.shields.io/badge/release-1.0.0-brightgreen.svg)
![Download](https://api.bintray.com/packages/jkb/maven/fragment-rigger/images/download.svg)
![SDK](https://img.shields.io/badge/SDK-12%2B-green.svg)
![Build](https://img.shields.io/badge/build-passing-brightgreen.svg)
![Build](https://img.shields.io/badge/Powered%20by-AsPectJ-blue.svg)
[![AsPectJ](https://img.shields.io/badge/license-MIT-yellowgreen.svg)](https://github.com/HujiangTechnology/gradle_plugin_android_aspectjx)
[![JingYeoh](https://img.shields.io/badge/author-JustKiddingBaby-red.svg)](http://blog.justkiddingbaby.com/)

>This might is the library to manage fragments at the least cost of use.  
**Do not need extend any class!!!Do not need extend any class!!!Do not need extend any class!!!** the most thing must be said for three times!!!  
you just only need cost one line annotation code when you are using `FragmentRigger`.   
*Principle of library is define the pointcuts for Fragment/Activity lifecycle methods and bind to the proxy class to execute.*

### Feature
- [x] **Powerful api**
- [x] **Enough English notes**
- [x] **Strictest exceptions**
- [x] **Resolve usual exceptions and bugs in fragments**
- [x] **Never lost any fragment transaction commit**
- [x] **Extend the android native fragment methods,add some usual methods such as `onBackPressed()`**
- [x] **Print tree for the fragment stack**
- [x] **Fragment lazy load**
- [x] **Fragment transition animations**
- [ ] **Fragment shared elements transition animations**

### Problem solved
* ~~Fragment view overlapping~~
* ~~Fragment Multi-level showing~~
* ~~Fragment stack manager~~
* ~~Fragment transaction commit failed~~
* ~~Commit the transaction when the host activity is not resumed~~
* ~~Multiple commits are interconnected but the fragment transaction commit does not happen immediately~~
* ~~A series of exceptions when memory restarting~~
* ~~Data saved and restored when the screen is flipped~~
* ~~Can not perform this action after onSaveInstanceState~~
* ~~Lazy loading in ViewPager and other scenarios~~
* ~~The animation does not perform in different scenarios~~

### Using example
>**At the least cost of use** is this library's target,and this library provides powerful api.   
this library is differ from the existed fragment library.do not need to extend any class,you just only need add one line annotation code.   
you can manage fragments by proxy class,This library uses a plug-in approach to reduce the cost of use.

**1、Make your class support the library**
>Add `@Puppet` annotation for your `Activity/Fragment` that need to use this library.

```java
//MainActivity.java
@Puppet(containerViewId = R.id.atyContent)//containerViewId is the fragment to be placed in.
public class MainActivity extends AppCompatActivity
```
```java
//TestFragment.java
@Puppet
public class TestFragment extends Fragment
```

**2、Using this library to manage `Fragment`**
>Do not need extend any class,add `@Puppet` annotation,use the proxy class `Rigger` to manage fragments.

```java
@Puppet(containerViewId = R.id.atyContent)
public class MainActivity extends AppCompatActivity{
  ...
  //add and show a fragment and add it to the stack,this fragment is placed in the container view.
   Rigger.getRigger(this).startFragment(TestFragment.newInstance());
}
```

### Demo
>This library support usual fragment using scenes,if you found the scene that this library does not supported,you can post [Issues](https://github.com/JustKiddingBaby/FragmentRigger/issues) or [Email me](mailto:yangjing9611@foxmail.com)

|Stack manager|Replace|Lazy loading|
|:-----------:|:-----:|:---------:|
|<img src="/images/start.gif" width = "200px"/>|<img src="/images/replace.gif" width = "200px"/>|<img src="/images/lazyload.gif" width = "200px"/>|
|Support fragment level/multi-layer nesting,and show the top fragment when fragment in the stack is closed|One fragment will be showed in one `container`,this library provides powerful api to let you use fragment easier|Support the lazy loading scene such as using with `ViewPager`,it's easy to use,you just need add one line annotation code|
|[StartFragment.java](/app/src/main/java/com/yj/app/test/start/StartFragment.java)|[ReplaceFragment.java](/app/src/main/java/com/yj/app/test/replace/ReplaceFragment.java)|[LazyLoadFragment.java](/app/src/main/java/com/yj/app/test/lazyload/LazyLoadFragment.java)

|Show|Print stack tree|
|:--:|:--------------:|
|<img src="/images/show.gif" width = "200px"/>|<img src="/images/tree.png" width = "300px"/>|
|show fragment by `showFragment` method,support pre loading,lazy loading|The fragment stack can be printed in logcat with `Fragment tag`|
|[ShowFragment.java](/app/src/main/java/com/yj/app/test/show/ShowFragment.java)|[StartFragment.java](/app/src/main/java/com/yj/app/test/start/StartFragment.java)|

>The demos are only showing some usual scenes,the main purpose is to protrude the strong api support of this library,
some functions about `Fragment` can be showed in the demos,such as：`Fragment transition animations`、`Extend the android native fragment methods`.   
**if you want to know more about this library,please see the [Wiki](https://github.com/JustKiddingBaby/FragmentRigger/wiki).**

### How to config
>This library is powered by `AspectJ`,you must config the `AspectJ` library if you wanna to use this library.

**1、Add in the root project `build.gradle`**
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
**2、Add in the `application` `build.gradle`**
```gralde
apply plugin: 'android-aspectjx'
android{
  ...
}
```
**3、Add in the `library` `build.gradle`**
```gradle
compile 'com.justkiddingbaby:fragment-rigger:1.0.0'
```

### Release log
##### V1.0.0[2017/12/15]  
1、Finish base action.

### License
![](https://upload.wikimedia.org/wikipedia/commons/thumb/f/f8/License_icon-mit-88x31-2.svg/128px-License_icon-mit-88x31-2.svg.png)

This library is available under the MIT license. See the [LICENSE](https://opensource.org/licenses/MIT) file for more info.