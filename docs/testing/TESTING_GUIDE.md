# FragmentRigger 测试指南

## 🧪 测试架构概览

FragmentRigger采用分层测试策略，确保框架的稳定性和可靠性。

### 测试金字塔
```
        /\\\
       /___\\\   UI测试 (少量)
      /_____\\\
     /_______\\\ 集成测试 (中等)
    /_________\\\
   /___________\\\ 单元测试 (大量)
```

## 📋 测试环境配置

### 依赖配置
在`rigger`模块的`build.gradle`中添加测试依赖：

```gradle
dependencies {
    // 单元测试
    testImplementation 'junit:junit:4.13.2'
    testImplementation 'org.mockito:mockito-core:4.6.1'
    testImplementation 'org.robolectric:robolectric:4.8.1'
    
    // Android测试
    androidTestImplementation 'androidx.test:runner:1.4.0'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.4.0'
}
```

## 🔬 单元测试

### 测试目录结构
```
rigger/src/test/java/com/jkb/fragmentrigger/
├── internal/proxy/          # 代理类测试
├── internal/processor/      # 注解处理器测试
├── internal/utils/          # 工具类测试
├── api/                     # 公共API测试
└── support/                 # 支持库测试
```

### 代理类测试示例

```java
public class FragmentProxyTest {
    
    @Test
    public void testProxyBinding() {
        // Given
        Fragment fragment = mock(Fragment.class);
        FragmentProxy proxy = new FragmentProxy("test_tag");
        
        // When
        boolean result = proxy.bind(fragment);
        
        // Then
        assertTrue(result);
        assertEquals("test_tag", proxy.getTag());
    }
    
    @Test
    public void testLifecycleCallback() {
        // 测试生命周期回调
        FragmentProxy proxy = new FragmentProxy("test");
        
        proxy.onAttach(mock(Context.class));
        proxy.onCreate(mock(Bundle.class));
        
        // 验证状态变更
        assertTrue(proxy.isAttached());
    }
}
```

### 注解处理器测试

```java
public class RiggerProcessorTest {
    
    @Test
    public void testAnnotationProcessing() {
        // 测试注解处理逻辑
        RiggerProcessor processor = new RiggerProcessor();
        
        ProcessingEnvironment env = mock(ProcessingEnvironment.class);
        processor.init(env);
        
        // 处理测试注解
        boolean success = processor.process(
            Collections.singleton(mock(TypeElement.class)), 
            mock(RoundEnvironment.class)
        );
        
        assertTrue(success);
    }
}
```

## 🔗 集成测试

### Fragment集成测试

```java
@RunWith(AndroidJUnit4.class)
public class FragmentIntegrationTest {
    
    @Test
    public void testFragmentShowAndHide() {
        // 测试Fragment显示和隐藏
        ActivityScenario<TestActivity> scenario = 
            ActivityScenario.launch(TestActivity.class);
        
        scenario.onActivity(activity -> {
            // 显示Fragment
            FragmentRigger.show("test", activity.getSupportFragmentManager());
            
            // 验证Fragment存在
            Fragment fragment = activity.getSupportFragmentManager()
                .findFragmentByTag("test");
            assertNotNull(fragment);
            
            // 隐藏Fragment
            FragmentRigger.hide("test", activity.getSupportFragmentManager());
            assertTrue(fragment.isHidden());
        });
    }
}
```

### 性能测试

```java
public class PerformanceTest {
    
    @Test
    public void testFragmentSwitchPerformance() {
        // 测试Fragment切换性能
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < 100; i++) {
            // 模拟Fragment切换
            performFragmentTransaction();
        }
        
        long duration = System.currentTimeMillis() - startTime;
        assertTrue("Fragment切换耗时应小于1秒", duration < 1000);
    }
}
```

## 🧩 Mock测试策略

### Mock配置

```java
@RunWith(MockitoJUnitRunner.class)
public class MockTest {
    
    @Mock
    FragmentManager fragmentManager;
    
    @Mock
    FragmentTransaction transaction;
    
    @Before
    public void setup() {
        when(fragmentManager.beginTransaction()).thenReturn(transaction);
        when(transaction.commit()).thenReturn(1);
    }
    
    @Test
    public void testWithMocks() {
        FragmentRigger.show("test", fragmentManager);
        
        verify(transaction).add(anyInt(), any(Fragment.class), eq("test"));
        verify(transaction).commit();
    }
}
```

## 📊 测试覆盖率目标

| 模块 | 覆盖率目标 | 当前状态 |
|------|-----------|----------|
| 代理类 | 90%+ | 待实现 |
| 注解处理器 | 85%+ | 待实现 |
| 工具类 | 95%+ | 待实现 |
| 公共API | 80%+ | 待实现 |

## 🔧 测试工具和技巧

### 调试测试
```bash
# 运行所有测试
./gradlew test

# 运行特定测试类
./gradlew test --tests "com.jkb.fragmentrigger.FragmentProxyTest"

# 生成测试报告
./gradlew testDebugUnitTestCoverage
```

### 测试代码示例模板
在`templates/`目录下提供测试代码模板，便于快速创建新测试。

---

通过这套测试体系，确保FragmentRigger框架的稳定性和可靠性！