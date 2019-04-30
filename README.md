# m2m

m2m 是一个可以通过配置文件定制化生成数据库表到具体实体的Intellij Idea插件。

## 功能

- 支持生成指定表
- 支持根据定义通过表前缀生成到不同包中
- 支持从数据获取注释生成到类、属性上
- 支持配置每个类、属性的自定义注解
- 支持配置每个类的自定义导包
- 支持自定义生成的作者信息
- 支持两种缩进风格（Tab、Space）

## TODO

- [ ] 修复使用空格做缩进导致缩进不一致的BUG
- [ ] 表名单复数转换
- [ ] 重写英文版README

## 使用

配置文件名为: **.m2m.yaml**，该配置文件存在放项目的根目录（与README.md处于同级）。

快捷键:  **Ctrl + Shift + Alt + Meta(Win) + 加号**

### 最简单的配置示例

- 指定数据库
- 指定默认的包
- 指定需要生成的表
- 指定代码生成的目标路径（见下文详细描述）

```yaml
database:
  type: mysql
  host: "127.0.0.1"
  port: 3306
  user: root
  password: root
  name: shadow
  params: useInformationSchema=true&serverTimezone=UTC&useUnicode=true&characterEncoding=utf8&autoReconnect=true

path: src

# 包的基本配置
package:
  base: io.eightpigs.test.model

# 需要生成的表
tables:
  - users
  - items
```

### 完整的配置文件

```yaml
database:
  type: mysql
  host: "127.0.0.1"
  port: 3306
  user: root
  password: root
  name: shadow
  params: useInformationSchema=true&serverTimezone=UTC&useUnicode=true&characterEncoding=utf8&autoReconnect=true

# 生成到项目的dao目录中
path: dao/src/main/java 

# 生成的代码风格
style:
  indent: 1
  indentStyle: tab

# 根据不同表的前缀将相关表生成到不同包下
package:
  base: io.eightpigs.test.model
  group:
    user: user
    order: order

# 需要生成的表
tables:
  - users
  - items

# 类上的java注释信息
info:
  author: eightpigs <eightpigs@outlook.com>
  date: ${datetime}

# 类的生成细节
# 支持指定一个通配符（*)代表所有类
# 也可以单独给每个类分别指定规则
classes:
  - tableName: "*"
    comment: true
    constructors:
      full: true
      empty: true
    annotations:
      - '@DBTable(name="${tableName}")'
    imports:
      - io.eightpigs.tools.mybatis.annotation.DBTable
    properties:
      - columnName: "*"
        getter: true
        setter: true
        comment: true
        annotations:
          - '@DBField(name="${columnName}")'
        imports:
          - io.eightpigs.tools.mybatis.annotation.DBField
```

## 配置详解

### database

`database`定义了目标数据库的信息。

- `database.type`: 数据库的类型，当前进支持mysql。如果想支持更多数据库，可以查看: [IDatabase](https://github.com/eightpigs/m2m/blob/master/src/main/java/io/eightpigs/m2m/database/IDatabase.java)接口的定义。
- `database.host`: 数据库的地址，默认：127.0.0.1
- `database.port`: 数据库的端口，默认：3306
- `database.user`: 数据库的用户
- `database.password`: 数据库用户的密码
- `database.name`: 数据库名
- `database.params`: 数据库连接的其他参数

### style

`style`配置项定义了生成时的代码风格。

- `style.indent`: 具体缩进的数值
- `style.indentStyle`: 缩进的风格，支持: **tab** 和 **space**

### package

`package`定义了类的基本包和分组规则。

- `package.base` : 基本的包名
- `package.group`: 根据表前缀分组的规则

例如以下配置将会将`user_salts`生成到`io.eightpigs.test.model.user`包下，类名为：UserSalts
```yaml
package:
  base: io.eightpigs.test.model
  group:
    user: user
```

### tables

具体需要生成的表名，如果不指定，则生成所有的表。

###  info

`info`定义了类生成时需要用到的相关信息。

- `info.author`: 类注释上的作者信息
- `info.date`: 类生成时的时间，支持使用变量

### classes

`classes`定义了类生成的所有规则信息，可以使用通配符（*）对所有类使用同一规则，也可以针对不同表（类）定义不同的生成规则。

当不配置`classes`时，默认的规则如下：

```yaml
classes:
  - tableName: "*"
    comment: true
    constructors:
      full: true
      empty: true
    properties:
      - columnName: "*"
        getter: true
        setter: true
        comment: true
```

- `classes.tableName`: 每一个tableName为一个类的配置，tableName如果为*，将会覆盖默认的通用配置。tableName可以写具体的表名，用于对指定表生成类时细节的定义。
- `classes.comment`: 类生成时是否需要使用数据库中表的注释，默认为：true
- `classes.constructors`: 类生成时是否需要全参、空构造，默认都生成。
- `classes.annotations`: 在类上加的自定义注解，数量无限制。并且支持变量。具体变量请看下文
- `classes.imports`: 类生成时自定的包引用，一般用于引入自定义注解需要的包。导包信息在生成时将会做去重处理，防止自定义包和属性类型重复。

- `classes.properties`: 定义了属性的生成规则，可以与类一样，使用一个通配符代表所有的生成规则或者单独对每个属性进行控制。
- `classes.properties.columnName`: 具体的列名
- `classes.properties.getter`: 生成该类对应的属性时，是否生成对应的getter方法，默认为：true
- `classes.properties.setter`: 生成该类对应的属性时，是否生成对应的setter方法，默认为：true
- `classes.properties.comment`: 属性生成时是否需要使用表中字段的注释，默认为：true
- `classes.properties.annotations`: 属性的自定义注解，同: `classes.annotations`
- `classes.properties.imports`: 属性需要自定义的导包，同：`classes.imports`

## 内置变量

- `${date}`: 当前日期，格式为: `yyyy-MM-dd`
- `${time}`: 当前时间，格式为: `HH:mm:ss`
- `${datetime}`: 当前完整日期+时间，格式为: `yyyy-MM-dd HH:mm:ss`
- `${tableName}`: 当前正在处理的表名
- `${columnName}`: 当前正在处理的列名
