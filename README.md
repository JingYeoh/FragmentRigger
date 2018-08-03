# FragmentRigger
![R](/images/rr.png)
![i](/images/i.png)
![g](/images/g.png)
![g](/images/g.png)
![e](/images/e.png)
![r](/images/r.png)

:boom:A powerful library to manage Fragments.
一个强大的Fragment管理框架。（[中文版入口](README-CN.md)）

![Platform](https://img.shields.io/badge/platform-Androd-green.svg)
![Download](https://api.bintray.com/packages/jkb/maven/fragment-rigger/images/download.svg)
![SDK](https://img.shields.io/badge/SDK-12%2B-green.svg)
![Build](https://img.shields.io/badge/Powered%20by-AsPectJ-blue.svg)
[![AsPectJ](https://img.shields.io/badge/license-MIT-yellowgreen.svg)](https://github.com/HujiangTechnology/gradle_plugin_android_aspectjx)
[![JingYeoh](https://img.shields.io/badge/author-JustKiddingBaby-red.svg)](http://blog.justkiddingbaby.com/)

**This might is the library that at the least cost of use to manage fragments.**    
**No need to extend any class!!! No need to extend any class!!! No need to extend any class!!!** the most important thing must be said for three times!!!   
You can use this `FragmentRigger` with one line annotation.  
**Principle:** define the pointcuts for Fragment/Activity lifecycle methods and bind to the proxy class to execute.

### Demo
>This library support usual fragment using scenes,if you found the scene that this library does not supported,you can post [Issues](https://github.com/JustKiddingBaby/FragmentRigger/issues) or [Email me](mailto:yangjing9611@foxmail.com)

|Stack manager|Show|Lazy loading|Replace|
|:-----------:|:-----:|:---------:|:------:|
|<img src="/images/start.gif" width = "200px"/>|<img src="/images/show.gif" width = "200px"/>|<img src="/images/lazyload.gif" width = "200px"/>|<img src="/images/replace.gif" width = "200px"/>|

### Goal
* Make Fragment use easier.
* At the least cost of use to manage fragments.

### Wiki
#### Getting Started
* [Installation](https://github.com/JustKiddingBaby/FragmentRigger/wiki)
* [Using start](https://github.com/JustKiddingBaby/FragmentRigger/wiki/Using-start)
* [Fragments usage](https://github.com/JustKiddingBaby/FragmentRigger/wiki/Fragment-usage)
* [Custom fragment tag](https://github.com/JustKiddingBaby/FragmentRigger/wiki/Custom-fragment-tag)
* [Lazy loading](https://github.com/JustKiddingBaby/FragmentRigger/wiki/Lazy-loading)
* [Transition animations](https://github.com/JustKiddingBaby/FragmentRigger/wiki/Transition-animations)
* [Intercept onBackPressed](https://github.com/JustKiddingBaby/FragmentRigger/wiki/Intercept-onBackPressed)
* [startFragmentForResult](https://github.com/JustKiddingBaby/FragmentRigger/wiki/startFragmentForResult)
* [Swipe edge to exit Activity/Fragment](https://github.com/JustKiddingBaby/FragmentRigger/wiki/Swipe-edge-to-exit)
* [How to use in library module](https://github.com/JustKiddingBaby/FragmentRigger/wiki/How-to-use-in-library-module)
#### Information
* [Errors in gradle](https://github.com/JustKiddingBaby/FragmentRigger/wiki/Errors-in-gradle)
* [ProGuard](https://github.com/JustKiddingBaby/FragmentRigger/wiki/ProGuard)
* [Change Log](https://github.com/JustKiddingBaby/FragmentRigger/wiki/Release-log)

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
- [x] **Option to configure fragment tag**
- [x] **Add `onBackPressed` method support for the fragment that is not added into stack**
- [x] **Swipe edge to exit Fragment/Activity**
- [ ] **Option to configure fragment launch mode**
- [ ] **Fragment shared elements transition animations**
- [ ] **Support DialogFragment**

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

### Usage example
>This library provides powerful api.

**1、Add support for your classes**
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

**2、Fragment usage**
>After add `@Puppet` annotation, use the proxy class `Rigger` to manage fragments.

```java
@Puppet(containerViewId = R.id.atyContent)
public class MainActivity extends AppCompatActivity{
  ...
  //add and show a fragment and add it to the stack,this fragment is placed in the container view.
   Rigger.getRigger(this).startFragment(TestFragment.newInstance());
}
```

**3、Activity/Fragment swipe back to exit**
>Add `Swiper` annotation for your `Activity/Fragment`.

```java
@Swiper
@Puppet
public class MainActivity extends AppCompatActivity{
    // swiper can only used with puppet.
}
```
```java
@Swiper
@Puppet
public class TestFragment extends Fragment{
    // swiper can only used with puppet.
}
```

### License
![](https://upload.wikimedia.org/wikipedia/commons/thumb/f/f8/License_icon-mit-88x31-2.svg/128px-License_icon-mit-88x31-2.svg.png)

This library is available under the MIT license. See the [LICENSE](https://opensource.org/licenses/MIT) file for more info.
