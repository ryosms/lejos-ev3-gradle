# MindStorms EV3

LEGO MindStorms EV3用のJavaアプリケーション

[gradle](http://gradle.org/) でビルドするために
[jornfranke/lejos-ev3-example](https://github.com/jornfranke/lejos-ev3-example)
を参考に、マルチプロジェクト形式にして調整を行ったもの


### Usage

1. `Prepared.md` で事前準備を行う
2. `gradle.properties.template` をコピーして `gradle.properties` を作成する
3. `gradle.properties` の設定を各自の環境に合わせて書き換える

### Build & Deploy

※ 以下はすべて `template` プロジェクトでの例
※ 新規プロジェクトを作成している場合は `template` を読み替える
ex.) gradle your-project:build

- build

    gradle template:build

- clean

    gradle template:clean

- rebuild

    gradle template:clean template:build

- deploy to EV3

    gradle template:deployev3


### Add new project(s)

1. 新規プロジェクト用のディレクトリを作成する
2. `template` プロジェクトの `build.gradle` を参考にして新規プロジェクトの `build.gradle` を作成する
3. settings.gradleに作成したプロジェクトを追加する
4. Enjoy coding!


### 動作確認環境

* Mac OSX Yosemite 10.10.5
* Java SE 1.8.0u45
* gradle 2.8
* IntelliJ IDEA 15.0.2


### Projects description

- template: プロジェクトのサンプル
- yoshioterada: [LEGO Mindstorms(leJOS) Hands On Lab](http://www.slideshare.net/OracleMiddleJP/lego-mindstormslejos-hands-on-lab)
