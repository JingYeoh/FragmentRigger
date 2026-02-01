# FragmentRigger 架构概述

## 设计理念
FragmentRigger 采用面向切面编程(AOP)的设计理念，通过注解和AspectJ实现对Fragment生命周期的透明管理，避免传统的继承方式带来的复杂性。

## 架构层次

### 1. 注解层 (Annotation Layer)
- 定义业务注解：`@Rigger`
- 提供配置接口和默认值

### 2. 切面层 (Aspect Layer) 
- AspectJ 切面实现
- 生命周期方法拦截
- 代理逻辑执行

### 3. 核心层 (Core Layer)
- Fragment 栈管理
- 生命周期状态跟踪
- 回调机制实现

### 4. 支持层 (Support Layer)
- 工具类和辅助方法
- 异常处理
- 日志记录

## 技术栈
- **AOP框架**: AspectJ
- **构建工具**: Gradle
- **Android支持**: Support Library 27+
- **测试框架**: JUnit, Mockito, Espresso

## 包结构优化
```
com.justkiddingbaby.fragmentrigger/
├── annotation/     # 注解定义
├── aspect/         # 切面实现  
├── core/           # 核心功能
├── support/        # 支持工具
└── model/          # 数据模型
```