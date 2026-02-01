# FragmentRigger 架构设计文档

## 🏗️ 架构概述

FragmentRigger 是一个基于AOP（面向切面编程）的Android Fragment管理框架，通过注解和代理模式实现无侵入的Fragment生命周期管理。

### 核心设计原则
1. **无侵入设计** - 无需继承特定基类，零学习成本
2. **切面编程** - 通过生命周期方法切入点实现代理
3. **模块化** - 清晰的模块边界和职责分离

## 📦 模块架构

```
fragmentrigger/
├── internal/           # 内部实现模块（隐藏API）
│   ├── proxy/         # 代理类实现
│   ├── processor/     # 注解处理器
│   └── utils/         # 工具类
├── annotation/        # 注解定义
├── support/          # 支持库（兼容性处理）
└── api/              # 公共API接口
```

## 🔄 核心工作流程

```mermaid
graph TD
    A[用户定义Fragment] --> B[添加@Rigger注解]
    B --> C[编译时注解处理]
    C --> D[生成代理类]
    D --> E[运行时代理绑定]
    E --> F[生命周期管理]
```

## 🎯 技术选型

- **编译时处理**: Android Annotation Processor
- **AOP实现**: 基于ASM字节码操作
- **依赖注入**: 轻量级Dagger风格实现
- **测试框架**: JUnit + Mockito + Robolectric

## 🔮 架构演进路线

### 短期目标
- 完善测试覆盖
- 优化性能监控
- 增强错误处理

### 长期目标
- 支持Kotlin协程
- 适配Compose
- 多模块架构支持
