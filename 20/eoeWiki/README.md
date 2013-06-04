EoeWiki
=======

Eoeandroid Wiki Android客户端

ref:
http://www.eoeandroid.com/thread-187739-1-1.html

整个应用程序编码为UTF-8,大家下载后，请一定要将项目的编码也设置成UTF-8.以保存中文的正常显示。

src
========
存放的是我们的代码

# 包介绍 #

## com.eoe.wiki ##

存放一些静态变量，或者是周期与整个应用程序一致的类与变量

## com.eoe.wiki.activity ##
存放我们的activity类，所有的activity都必需继承至BaseActivity（继承到TabActivity等的除外）。

这样做主要是为了方便我们在BaseActivity中初始化一些所有页面共有的一些内容

## com.eoe.wiki.db ##
存放的是我们的db 有关的东西。包括helper and provider，及所有数据库的表实体。所有的表实体（column）必需继承BaseClumn类.

## com.eoe.wiki.db.dao ##
存放的是我们的dao 有关的东西。所有的Dao必需继承BaseDao类.Dao的作用是提供一个操作数据库的中间层。

## com.eoe.wiki.utils ##
存放所有的工具类。里面的方法最好都是statis的。以便应用程序可以直接调用。

res
========
存放的是我们的资源文件

libs
========
存放的是我们的第三方库

build
========
存放的是我们关于build的信息。比如说打包的key、build的日志等等

assets
========
存放的是程序有关的额外的资源

AndroidManifest.xml
========
配置我们的整个应用程序
包名为 com.eoe.wiki

.gitignore
========
一些被git忽略的配置信息

bin
========