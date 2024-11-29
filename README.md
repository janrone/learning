### React 

注意，直接使用 class 在 React.js 的元素上添加类名如 <div class=“xxx”> 这种方式是不合法的。因为 class 是 JavaScript 的关键字，所以 React.js 中定义了一种新的方式：className 来帮助我们给元素添加类名。

还有一个特例就是 for 属性，例如 <label for='male'>Male</label>，因为 for 也是 JavaScript 的关键字，所以在 JSX 用 htmlFor 替代，即 <label htmlFor='male'>Male</label>。而其他的 HTML 属性例如 style 、data-* 等就可以像普通的 HTML 属性那样直接添加上去。

如果你在表达式插入里面返回 null ，那么 React.js 会什么都不显示，相当于忽略了该表达式插入。结合条件返回的话，我们就做到显示或者隐藏某些元素。


为 React 的组件添加事件监听是很简单的事情，你只需要使用 React.js 提供了一系列的 on* 方法即可。

React.js 会给每个事件监听传入一个 event 对象，这个对象提供的功能和浏览器提供的功能一致，而且它是兼容所有浏览器的。

React.js 的事件监听方法需要手动 bind 到当前实例，这种模式在 React.js 中非常常用。


http://huziketang.mangojuice.top/books/react/lesson9

### NodeJS 
教程和结果
《一起学 Node.js》
一直写APP，学习其他的尝尝鲜 。

[最终结果] (https://tpoetry.herokuapp.com/posts)

NodeJS
局部安装

    npm i sha1 --save
    npm i mongolass -save
    npm i express-formidable --save

启动MongoDB数据：
Windows 下：数据库文件的存放位置

在你安装MongoDB的bin 目录下打开cmd, 输入：mongod 回车启动服务。会看到：

Hotfix KB2731284 or later update is not installed.  以及 C:\data\db not found 的字样。
1
MongoDB默认数据库文件夹路径为C:/data/db（注：虽然是默认，但是需要你自己创建）。但也可以自己设置默认路径，比如d:/test/data/db。启动mongodb服务之前必须创建数据库文件的存放文件夹，否则不能启动成功。使用系统默认文件夹路径时，启动服务无需加 –dbpath 参数说明。如果不是默认路径，则启动服务格式有如下两种：

    （1）mongod --dbpath 存放的路径。如：mongod --dbpath d:\test\data 【注：路径不能包含空格，否则使用第2种】
    （2）mongod --dbpath "存放的路径" 。如 mongod --dbpath "d:\my text\data"

在浏览器中输入网址：http://localhost:27017/ 。如果服务启动成功会看到以下一段话：
It looks like you are trying to access MongoDB over HTTP on the native driver port.

来源：http://blog.csdn.net/victor_cindy1/article/details/52151439

Heroku 部署 Nodejs
在工程的根目录下新建一个 Procfile 文件，添加如下内容：

web: node index.js

Procfile 文件告诉了服务器该使用什么命令启动一个 web 服务，这里我们通过 node index.js 执行 Node 脚本。为什么这里声明了一个 web 类型呢？官方解释为：
The name “web” is important here. It declares that this process type will be attached to the HTTP routing stack of Heroku, and receive web traffic when deployed.

一些配置
start": "NODE_ENV=production pm2 start index.js --node-args='--harmony' --name 'poetry'" 

git push heroku master
————————————————
版权声明：本文为CSDN博主「janronehoo」的原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/janronehoo/article/details/53765604

https://tpoetry.herokuapp.com/posts

### Vue 
vue

### Flutter 
startup_namer

//main函数使用了(=>)符号, 这是Dart中单行函数或方法的简写。
void main() => runApp(new MyApp());

//Scaffold 是 Material library 中提供的一个widget, 它提供了默认的导航栏、标题和包含主屏幕widget树的body属性。widget树可以很复杂。
      //   home: new Scaffold(
      //     appBar: new AppBar(
      //       title: new Text('Welcome to Flutter'),
      //     ),
      //     body: new Center(
      //       //child: new Text('Hello World'),
      //       //child: new Text(wordPair.asPascalCase),
      //       child: new RandomWords(),
      //     ),
      //   ),


### Kotlin Jetpack

