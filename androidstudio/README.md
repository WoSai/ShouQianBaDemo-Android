# ShouQianBaDemo-Android
收钱吧SDK(Android)演示程序
 
**androidstudio**

1.将shouqianba.aar 导入工程lib下

2.在build.gradle里加入

    

    repositories {
        flatDir {
            dirs 'libs'
          }
    }
    dependencies {
       compile(name:'shouqianba', ext:'aar')
       compile 'com.google.code.gson:gson:2.3.1'
    }