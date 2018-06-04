## Widgets

一些自定义控件的合集。

-   HeartBeatView：模仿心跳，一般用于 LOADING 的 View
-   ArrowView：动画翻转箭头，一般用于收起、伸开的 View

### 引入方式

#### gradle

首先在项目的`build.gradle`文件中加入：

```groovy
allprojects {
    repositories {
        maven {url 'https://jitpack.io'}
    }
}
```

然后在需要使用当前库的模块的`build.gradle`中加入：

```groovy
dependencies {
    implementation 'com.github.ruoyewu:widgets:1.0.0'
}
```

