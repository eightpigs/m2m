# m2m (mysql2Model)

m2m is an Intellij Idea plugin that generates entity classes from tables based on custom configuration files.

## Features

1. Support for automatically creating different packages based on table names
2. Support to generate the specified table in the library
3. Support reading comments from the table
4. Support for adding custom annotations to fields
5. Support for adding custom "import"

## Usage

Sample configuration file: **.m2m.yaml**.

```yaml
database:
  type: mysql
  host: "127.0.0.1"
  port: 3306
  user: root
  password: root
  name: shadow
  params: useInformationSchema=true&serverTimezone=UTC&useUnicode=true&characterEncoding=utf8&autoReconnect=true

# Code style.
style:
  indent: 1
  indentStyle: tab

# automatically creating different packages based on table names.
package:
  base: io.eightpigs.test.model
  group:
    user: user
    order: order

# Table name to be processed.
tables:
  - users
  - items

# java doc
info:
  author: eightpigs <eightpigs@outlook.com>
  date: ${datetime}

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
          - '@DBField(name="${fieldName}")'
        imports:
          - io.eightpigs.tools.mybatis.annotation.DBField
```

## Configurations

#### Database

Currently only supports MySQL database, can support more databases, see: [IDatabase](https://github.com/eightpigs/m2m/blob/master/src/main/java/io/eightpigs/m2m/database/IDatabase.java).

#### Code Style

Support Tab and Space styles.

**Use a tab to indent**
```yaml
style:
  indent: 1
  indentStyle: tab
```

**Use 4 space to indent**
```yaml
style:
  indent: 4
  indentStyle: space
```

#### Automatically create packages based on table names

`package.base`: Default package name.
`package.groups`: If the table name has the prefix, a new package is created on the default package and the class is placed in the current package.

eg: The table `user_salts` will be put into the `io.eightpigs.test.model.user` package.

```yaml
package:
  base: io.eightpigs.test.model
  group:
    user: user
```

#### Specify the table to generate

The configuration item `tables` are used to specify the table to be generated.

#### configuration at build time

Configuration item "classes" is the detailed configuration at build time.

This configuration item must be included in the configuration file, and if there is no one-to-one configuration (talbe - class), there must be a wildcard configuration: *.

The simplest configuration:

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

`classes.annotations`: Allow adding custom class annotations.
`classes.imports`: Allow adding custom class imports.
`classes.commet`: Whether to use the comments of the database table.

`properties.annotations`: Allow adding custom annotations.
`properties.imports`: This property may need to import a package (such as a custom annotation).
`properties.commet`: Whether to use the comments of the database column.
