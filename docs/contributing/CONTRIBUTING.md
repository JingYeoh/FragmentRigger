# FragmentRigger 贡献指南

感谢您对FragmentRigger项目的关注！我们欢迎任何形式的贡献。

## 🎯 贡献方式

### 1. 报告问题
- 使用[GitHub Issues](https://github.com/JingYeoh/FragmentRigger/issues)报告bug或提出建议
- 提供详细的问题描述、复现步骤和期望结果

### 2. 功能开发
- 查阅[开发者指南](development/DEVELOPER_GUIDE.md)了解开发规范
- 从`develop`分支创建功能分支进行开发
- 确保代码质量和测试覆盖率

### 3. 文档改进
- 完善API文档和使用示例
- 翻译文档到其他语言
- 优化文档结构和可读性

### 4. 代码审查
- 帮助审查Pull Request
- 提供建设性反馈
- 确保代码符合项目标准

## 📋 贡献流程

### 步骤1: Fork项目

1. 在GitHub上Fork项目到自己的账户
2. 克隆Fork后的仓库到本地

```bash
git clone https://github.com/your-username/FragmentRigger.git
cd FragmentRigger
```

### 步骤2: 创建功能分支

从`develop`分支创建新的功能分支：

```bash
git checkout -b feature/your-feature-name develop
```

**分支命名规范**:
- `feature/*`: 新功能开发
- `bugfix/*`: Bug修复
- `docs/*`: 文档更新
- `refactor/*`: 代码重构

### 步骤3: 开发实现

1. 编写代码，遵循项目编码规范
2. 添加对应的单元测试
3. 更新相关文档
4. 确保所有测试通过

```bash
# 运行测试
./gradlew test

# 构建项目
./gradlew build
```

### 步骤4: 提交代码

使用规范的提交消息格式：

```bash
git add .
git commit -m "feat: 添加Fragment懒加载功能"
git commit -m "fix: 修复内存泄漏问题"
git commit -m "docs: 更新API参考文档"
```

### 步骤5: 推送和提交PR

```bash
git push origin feature/your-feature-name
```

然后在GitHub上创建Pull Request到原项目的`develop`分支。

## 🔍 代码审查标准

### 代码质量要求
- 遵循Google Java代码风格
- 适当的代码注释和文档
- 单元测试覆盖率达到要求
- 无编译器警告和静态分析问题

### PR审查清单
- [ ] 代码功能正确实现
- [ ] 测试用例完整
- [ ] 文档同步更新
- [ ] 无破坏性变更
- [ ] 性能影响评估

## 🧪 测试要求

### 新增功能测试
- 必须包含单元测试
- 测试覆盖主要业务逻辑
- 边界情况和异常处理

### 回归测试
- 确保现有功能不受影响
- 运行完整的测试套件
- 性能基准测试

## 📝 文档要求

### API文档
- 所有公共API必须有Javadoc注释
- 示例代码可运行且正确
- 参数和返回值说明清晰

### 使用文档
- 提供完整的使用示例
- 常见问题解答
- 最佳实践指南

## 🏆 贡献者权益

### 贡献者名单
所有贡献者将在项目README中列出。

### 核心贡献者
长期活跃的贡献者可能成为项目核心维护者。

## ⚖️ 行为准则

我们遵循[贡献者公约](https://www.contributor-covenant.org/)，请保持专业和尊重的沟通态度。

---

感谢您的贡献！让我们一起打造更好的FragmentRigger！