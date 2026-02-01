# FragmentRigger API 参考手册

## 📋 核心注解

### @Rigger
主要的Fragment管理注解，用于标记需要被FragmentRigger管理的Fragment。

```java
@Rigger(tag = "home_fragment", bundle = "key=value")
public class HomeFragment extends Fragment {
    // Fragment实现
}
```

**参数说明**:
- `tag`: Fragment的唯一标识符（必填）
- `bundle`: 传递给Fragment的参数（可选）
- `lazy`: 是否启用懒加载（默认false）

### @Puppet
用于标记依赖注入的目标字段或方法。

```java
@Puppet
private UserModel userModel;
```

## 🔧 核心API

### FragmentRigger
框架入口类，提供静态方法进行Fragment管理。

```java
// 显示Fragment
FragmentRigger.show("home_fragment", getSupportFragmentManager());

// 隐藏Fragment  
FragmentRigger.hide("home_fragment", getSupportFragmentManager());

// 替换Fragment
FragmentRigger.replace("new_fragment", R.id.container, getSupportFragmentManager());

// 回退栈管理
FragmentRigger.popBackStack(getSupportFragmentManager());
```

### RiggerProvider
服务提供接口，用于自定义依赖注入实现。

```java
public interface RiggerProvider {
    <T> T provide(Class<T> clazz);
}
```

## 🎛️ 配置API

### RiggerConfig
全局配置类，用于配置框架行为。

```java
RiggerConfig.getInstance()
    .setDebug(true)           // 调试模式
    .setProvider(provider)    // 设置依赖提供者
    .setInterceptor(interceptor); // 设置拦截器
```

## 🔄 事件监听

### RiggerListener
Fragment生命周期事件监听器。

```java
public interface RiggerListener {
    void onFragmentShown(String tag);
    void onFragmentHidden(String tag);
    void onFragmentAttached(String tag);
    void onFragmentDetached(String tag);
}
```

## 📊 性能监控

### RiggerMonitor
性能监控接口，用于收集框架性能数据。

```java
public interface RiggerMonitor {
    void recordTransaction(String fromTag, String toTag, long duration);
    void recordError(String tag, Throwable error);
}
```