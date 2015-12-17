# 準備

##### 参考

- [leJOS公式サイト](http://www.lejos.org/)
- [leJOS EV3開発環境構築ガイド(Mac版)](http://sourceforge.net/p/etroboev3/wiki/lejosev3_mac_eclipse/)
- [leJOS + NetBeans 8.0 (Maven) + Java SE Embedded (7 or 8) で LEGO Mindstorms EV3のアプリケーション開発](http://yoshio3.com/2014/04/23/lejos-netbeans-maven-javase7embedded-lego-mindstorm/)
- [LeJOS EV3 実行/Netbeans開発環境構築メモ(Windows版)](http://www.triring.net/program/robocon/ev3/lejosev3_dev/NetBeans/index.html)

##### 前提条件

- JDKのインストールは完了済み
- Gradleのインストールは完了済み（gradle wrapperを使用する場合はインストール不要）
- IDEのインストールは完了済み

### 各種ファイルのダウンロードおよびインストール

- [leJOS](http://sourceforge.net/projects/ev3.lejos.p/files/)より `leJOS_EV3_<version>.tar.gz` をダウンロードする
- ダウンロードした `leJOS_EV3_<version>.tar.gz` を適当なディレクトリ(以下 `EV3_HOME` )に解凍する

- [Java for LEGO® Mindstorms® EV3](http://www.oracle.com/technetwork/java/embedded/downloads/javase/javaseemeddedev3-1982511.html)より `Oracle Java SE Embedded version 8` をダウンロードする
- ダウンロードしたファイルを適当なディレクトリ(以下 `EJDK_HOME` )に解凍する
- 以下のコマンドでEV3用のJREを生成する(出力ディレクトリを `OUTPUT_DIR` とする)

    ```bash:
    > cd $EJDK_HOME/bin
    > ./jrecreate.sh -vm all -d $OUTPUT_DIR/ejre1.8.0
    ...
    > cd $OUTPUT_DIR
    > tar cvf ejre1.8.0.tar ejre1.8.0
    > gzip -c ejre1.8.0.tar > ejre1.8.0.tar.gz
    ```

### SDカードの準備

- [SDカードの準備](http://sourceforge.net/p/etroboev3/wiki/lejosev3_mac_eclipse/#12-sd) を参考にしてSDカードをフォーマットする
- `$EV3_HOME/lejosimage.zip` を解凍してできたフォルダの中身をSDカードにコピーする
- 上記で作成した `ejre1.8.0.tar.gz` をSDカードにコピーする
- EV3にSDカードを挿して起動する

