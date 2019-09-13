# APIサーバー構築

## ドメイン
ビジネスロジック場所

* domain  
  * だたの商品のcrudです。(InMemory)

## REST
Restサーバーはswaggerで作成  

* rest-config
  * swaggerファイルの場所

* rest-lib
  * swaggerファイルからサーバー側のコード生成(spring)
  * swagger-generate-codeをgradleから実行
  * controllerすでに定義されていて、RequestMappingの重複で落ちるのでgradleで消した(FIXME!!) 

* rest-server
  * rest-libを参照して、実APIサーバーを構築
  * 内部でdomainのビジネスロジック呼び出し

## GRPC
GRPCサーバーはprotoで作成

* grpc-lib
  * protoファイルとコードのgenerate
  * swaggerのように参照できなかったので同一プロジェクト(FIXME!!)
  * curlかrest-clientとかで動かしてね

* grpc-server
  * grpc-libを参照して、実APIサーバーを構築
  * 内部でdomainのビジネスロジックを呼び出し
  * evansが便利です。https://github.com/ktr0731/evans
