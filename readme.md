免费http代理池
=========

## 工程导入(maven)
* git clone https://github.com/wycm/http-proxy-pool 克隆项目到本地
* **eclipse**导入步骤(eclipse_kepler版本，自带maven)，File->Import->Maven->Existing Maven Projects->选择刚刚clone的http-proxy-pool目录->导入成功
* **idea**导入步骤,File->Open->选择刚刚clone的zhttp-proxy-pool目录->导入成功

## Quick Start
Run with [Main.java](https://github.com/wycm/http-proxy-pool/blob/master/proxy/src/main/java/com/github/wycm/hpp/proxy/Main.java) <br>

## 代理的使用
* 目前仅支持http请求方式
* 地址(url)：```http://localhost:8080/http-proxy-pool```
* 请求类型：GET
* **请求参数**

| 参数名 |类型 | 必填 | 值 | 说明|
| :------------ | :------------ | :------------ | :----- | :------------ |
| protocol | String | 否| http、https | 协议 |
| anonymous  | String  否  | 是| 透明、普匿、高匿 | 是否匿名 |

## TODO
* 增加代理