import 'package:flutter/material.dart';
import 'package:english_words/english_words.dart';

//main函数使用了(=>)符号, 这是Dart中单行函数或方法的简写。
void main() => runApp(new MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    //final wordPair = new WordPair.random();
    return new MaterialApp(
      title: 'Welcome to Flutter',
      theme: new ThemeData(
        primaryColor: Colors.white,
      ),
      home: new RandomWords(),
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
    );
  }
}

class RandomWords extends StatefulWidget {
  @override
  createState() => new RandomWordsState();
}

class RandomWordsState extends State<RandomWords> {
  final _suggestions = <WordPair>[];
  final _biggerFont = const TextStyle(fontSize: 18.0);
  final _saved = new Set<WordPair>();
  @override
  Widget build(BuildContext context) {
    //final wordPair = new WordPair.random();
    //return new Text(wordPair.asCamelCase);
    return new Scaffold(
      appBar: new AppBar(
        title: new Text("Startup Name Generator"),
        actions: <Widget>[
          new IconButton(
            icon: new Icon(Icons.list),
            onPressed: _pushSaved,
          )
        ],
      ),
      body: _buildSuggestions(),
    );
  }

  Widget _buildSuggestions() {
    return new ListView.builder(
        padding: const EdgeInsets.all(16.0),
        itemBuilder: (context, i) {
          if (i.isOdd) return new Divider();
          final index = i ~/ 2;
          if (index >= _suggestions.length) {
            _suggestions.addAll(generateWordPairs().take(10));
          }
          return _buildRow(_suggestions[index]);
        });
  }

  Widget _buildRow(WordPair pair) {
    final alreadSaved = _saved.contains(pair);
    return new ListTile(
      title: new Text(
        pair.asCamelCase,
        style: _biggerFont,
      ),
      trailing: new Icon(
        alreadSaved ? Icons.favorite : Icons.favorite_border,
        color: alreadSaved ? Colors.red : null,
      ),
      onTap: () {
        setState(() {
          if (alreadSaved) {
            _saved.remove(pair);
          } else {
            _saved.add(pair);
          }
        });
      },
    );
  }

  void _pushSaved() {
    Navigator.of(context).push(
      new MaterialPageRoute(
        builder: (context){
          final titles = _saved.map(
            (pair){
              return new ListTile(
                title: new Text(
                  pair.asCamelCase,
                  style: _biggerFont
                ),
              );
            },
          );
          final divided = ListTile.divideTiles(
            context: context,
            tiles: titles,
          ).toList();

          return new Scaffold(
            appBar: new AppBar(
              title: new Text("Saved Suggersions"),
            ),
            body: new ListView(children: divided),
          );
        },
      ),
    );
  }
}
