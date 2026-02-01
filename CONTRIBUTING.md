# 贡献指南

欢迎为 FragmentRigger 项目贡献代码！请阅读本指南了解贡献流程。

## 开发环境设置

### 前置条件
- Android Studio 3.0+
- JDK 8+
- Android SDK 27+

### 设置步骤
1. Fork 项目仓库
2. 克隆你的 Fork
3. 导入到 Android Studio
4. 运行 `./gradlew build` 验证设置

## 代码规范

### Java 代码风格
- 使用 4 空格缩进
- 遵循 Android 编码规范
- 使用有意义的命名
- 添加适当的 Javadoc 注释

### 提交信息规范
使用约定式提交格式：
```
<type>(<scope>): <subject>

<body>

<footer>
```

类型说明：
- feat: 新功能
- fix: 修复bug
- docs: 文档更新
- style: 代码格式调整
- refactor: 重构
- test: 测试相关
- chore: 构建过程或辅助工具变动

## 测试要求

所有代码变更必须包含相应的测试：
- 新功能：添加单元测试和集成测试
- Bug修复：添加回归测试
- 运行测试：`./gradlew test connectedCheck`

## Pull Request 流程

1. 从 master 分支创建功能分支
2. 实现功能并添加测试
3. 确保所有测试通过
4. 提交符合规范的提交信息
5. 创建 Pull Request

## 代码审查

所有 Pull Request 需要经过审查：
- 至少需要一名维护者批准
- 修复审查意见
- 确保代码质量

## 问题报告

发现 Bug 时请提交详细的问题报告：
- 描述重现步骤
- 提供环境信息
- 包含错误日志

谢谢您的贡献！