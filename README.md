# 收钱吧 SDK演示程序(for Android)

## 环境要求
java 7
android api 16+

## Demo使用
直接在相应的IDE中导入工程即可，SDK已经都集成好。

## Eclipse环境下集成SDK
直接将SDK目录中的```Shouqianbalib/```文件夹作为library项目导入工程

## Android Studio环境下集成SDK
1. 将SDK目录中的shouqianba.aar 放入工程用来存放jar包依赖的libs目录下

2. 在主应用的build.gradle里加入
```
    repositories {
        flatDir {
            dirs 'libs'
          }
    }
    dependencies {
       compile(name:'shouqianba', ext:'aar')
       compile 'com.google.code.gson:gson:2.3.1' // 收钱吧SDK需要用到
    }
```
