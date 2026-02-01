# FragmentRigger
:boom:A powerful library to manage Fragments.
一个强大的Fragment管理框架。（[中文版入口](README-CN.md)）

**This might be the library that at the least cost of use to manage fragments.**    
**No need to extend any class!!! No need to extend any class!!! No need to extend any class!!!** the most important thing must be said for three times!!!   
You can use this `FragmentRigger` with one line annotation.  
**Principle:** define the pointcuts for Fragment/Activity lifecycle methods and bind to the proxy class to execute.

## Features
- **Zero Inheritance**: No need to extend any base class
- **AOP-powered**: Uses AspectJ for method interception
- **Simple Annotation**: One-line annotation configuration
- **Lifecycle Management**: Comprehensive Fragment lifecycle handling
- **Stack Management**: Built-in fragment stack management
- **Lazy Loading**: Support for lazy loading fragments
- **Replacement Support**: Easy fragment replacement

## Architecture
```
FragmentRigger/
├── app/                 # Demo application module
├── rigger/              # Core library module
│   ├── core/           # Core functionality
│   ├── annotation/     # Annotation definitions
│   ├── aspect/         # AspectJ aspect definitions
│   └── support/        # Utility classes
├── docs/               # Documentation
└── tests/              # Test modules
```

## Quick Start

### Dependencies
```gradle
dependencies {
    implementation 'com.justkiddingbaby:fragment-rigger:1.4.4'
}
```

### Usage
```java
@Rigger("fragment_tag")
public class MyFragment extends Fragment {
    // Your fragment implementation
}
```

## Documentation

- [API Reference](docs/api/)
- [Architecture Guide](docs/architecture/)
- [Best Practices](docs/best-practices/)
- [Migration Guide](docs/migration/)

## Testing

This project includes comprehensive test coverage:
- Unit tests: `./gradlew test`
- Instrumentation tests: `./gradlew connectedCheck`
- Code quality: `./gradlew checkCodeQuality`

## Building

### Prerequisites
- Android SDK 27+
- Java 8+

### Build Commands
```bash
# Build library
./gradlew :rigger:assemble

# Run tests
./gradlew test

# Generate documentation
./gradlew :rigger:javadoc

# Publish to bintray
./gradlew :rigger:bintrayUpload
```

## Contributing
Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct and the process for submitting pull requests.

## Versioning
We use [SemVer](http://semver.org/) for versioning. For available versions, see the [tags on this repository](https://github.com/JustKiddingBaby/FragmentRigger/tags).

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Demo
>This library support usual fragment using scenes,if you found the scene that this library does not supported,you can post [Issues](https://github.com/JustKiddingBaby/FragmentRigger/issues) or [Email me](mailto:yangjing9611@foxmail.com)

|Stack manager|Show|Lazy loading|Replace|
|:-----------:|:-----:|:---------:|:------:|
|<img src="/images/start.gif" width = "200px"/>|<img src="/images/show.gif" width = "200px"/>|<img src="/images/lazyload.gif" width = "200px"/>|<img src="/images/replace.gif" width = "200px"/>|

## Goal
* Make Fragment use easier.
* At the least cost of use to manage fragments.

## Wiki
- [Project Wiki](https://github.com/JustKiddingBaby/FragmentRigger/wiki)