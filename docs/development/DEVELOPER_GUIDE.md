# FragmentRigger 开发者指南

## 🛠️ 开发环境配置

### 前置条件
- JDK 8+
- Android Studio Arctic Fox+
- Android SDK API 21+

### 项目配置
```bash
# 克隆项目
git clone https://github.com/JingYeoh/FragmentRigger.git

# 导入Android Studio
File -> New -> Import Project -> 选择项目根目录
```

## 📁 代码组织结构

### 核心模块结构
```
rigger/
├── src/main/java/com/jkb/fragmentrigger/
│   ├── internal/           # 内部实现（不对外暴露）
│   │   ├── proxy/         # 代理类实现
│   │   ├── processor/     # 注解处理器
│   │   └── utils/         # 工具类
│   ├── annotation/        # 注解定义
│   ├── support/          # 支持库
│   └── api/              # 公共API
├── src/test/java/        # 单元测试
└── src/androidTest/java/ # Android测试
```

## 🔨 开发规范

### 代码风格
- 遵循Google Java代码风格规范
- 使用4空格缩进
- 类名使用PascalCase，方法名使用camelCase

### 注释规范
```java
/**
 * Fragment代理管理器
 * 
 * @author JingYeoh
 * @since 1.0.0
 */
public class FragmentProxyManager {
    
    /**
     * 绑定Fragment到代理
     * 
     * @param fragment 目标Fragment实例
     * @param tag Fragment标识符
     * @return 绑定是否成功
     */
    public boolean bindFragment(Fragment fragment, String tag) {
        // 实现逻辑
    }
}
```

### 测试规范
- 单元测试覆盖率达到80%+
- 每个公共方法都需要有对应的测试用例
- 使用Given-When-Then测试模式

## 🔄 开发工作流

### 1. 功能开发流程
```
功能设计 → 编写测试 → 实现代码 → 代码审查 → 合并发布
```

### 2. 分支策略
- `master`: 主分支，稳定版本
- `develop`: 开发分支
- `feature/*`: 功能分支
- `hotfix/*`: 热修复分支

### 3. 提交规范
```
feat: 新增功能
fix: 修复bug
docs: 文档更新
style: 代码格式调整
refactor: 代码重构
test: 测试相关
chore: 构建工具或依赖更新
```

## 🔍 调试技巧

### 调试模式
启用调试模式查看详细日志：
```java
RiggerConfig.getInstance().setDebug(true);
```

### 常见问题排查
1. **注解不生效**: 检查是否配置了kapt插件
2. **代理类未生成**: 清理并重新构建项目
3. **依赖注入失败**: 检查RiggerProvider配置

## 📈 性能优化建议

### 编译时优化
- 使用增量编译加速构建
- 合理使用注解处理器缓存

### 运行时优化
- 避免频繁的Fragment切换
- 合理使用懒加载特性
- 监控内存使用情况
