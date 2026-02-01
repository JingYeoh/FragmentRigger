# FragmentRigger 快速开始指南

## 🚀 5分钟上手FragmentRigger

### 第一步：添加依赖

在项目的`build.gradle`文件中添加依赖：

```gradle
dependencies {
    implementation 'com.jkb:fragmentrigger:1.0.0'
    kapt 'com.jkb:fragmentrigger-compiler:1.0.0'
}
```

### 第二步：配置注解处理器

确保在`build.gradle`中启用kapt：

```gradle
apply plugin: 'kotlin-kapt'
```

### 第三步：创建第一个Fragment

使用`@Rigger`注解标记你的Fragment：

```java
import com.jkb.fragmentrigger.annotation.Rigger;

@Rigger(tag = "home")
public class HomeFragment extends Fragment {
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}
```

### 第四步：在Activity中使用

使用FragmentRigger管理Fragment显示：

```java
public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // 显示HomeFragment
        FragmentRigger.show("home", getSupportFragmentManager());
    }
}
```

### 第五步：运行应用

构建并运行应用，你将看到FragmentRigger自动管理Fragment的生命周期！

## 📱 常用场景示例

### 场景1：Fragment切换

```java
// 显示新Fragment
FragmentRigger.show("detail", getSupportFragmentManager());

// 返回上一个Fragment
FragmentRigger.popBackStack(getSupportFragmentManager());
```

### 场景2：传递参数

```java
@Rigger(tag = "detail", bundle = "id=123&name=test")
public class DetailFragment extends Fragment {
    // Fragment实现
}
```

### 场景3：懒加载

```java
@Rigger(tag = "lazy", lazy = true)
public class LazyFragment extends Fragment {
    // 只有在可见时才会加载数据
}
```

## 🔧 进阶配置

### 全局配置

```java
// 在Application中配置
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        
        RiggerConfig.getInstance()
            .setDebug(true)
            .setInterceptor(new RiggerInterceptor() {
                @Override
                public boolean intercept(String tag) {
                    // 自定义拦截逻辑
                    return false;
                }
            });
    }
}
```

### 依赖注入

```java
@Rigger(tag = "user")
public class UserFragment extends Fragment {
    
    @Puppet
    private UserRepository userRepository;
    
    // userRepository会自动注入
}
```

## ❓ 常见问题

### Q: 为什么注解不生效？
A: 检查是否添加了kapt插件和依赖，清理项目重新构建。

### Q: 如何调试？
A: 启用调试模式：`RiggerConfig.getInstance().setDebug(true)`

### Q: 支持Kotlin吗？
A: 完全支持Kotlin，使用方式与Java相同。

---

恭喜！你已经成功掌握了FragmentRigger的基本使用。接下来可以探索更多高级特性！