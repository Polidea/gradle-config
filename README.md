# Gradle Config Plugin

This plugin helps you to easily handle variant specific settings with yaml format.

## Usage

```groovy
buildscript {
  repositories {
    maven {
      url "https://plugins.gradle.org/m2/"
    }
  }
  dependencies {
    classpath "com.polidea.android:config:1.0.0"
  }
}


apply plugin: 'com.android.application'
apply plugin: "com.polidea.android.config"
```

Build script snippet for new, incubating, plugin mechanism introduced in Gradle 2.1:
```
plugins {
  id "com.polidea.android.config" version "1.0.0"
}
```

## Settings class
The entries in config yaml will be available in ```Settings``` class as static members:

```
Settings.entry
```

Nested entries are supported:

```
Settings.entry.some_entry
```


## Config files

Config entries are generated from:

```
config/default.yml
config/default_secret.yml
config/${productFlavor}.yml
config/${productFlavor}_secret.yml
config/${buildType}.yml
config/${buildType}_secret.yml
```

The lower one overwrites upper one deeply. The purpose of loading settings files with suffix `_secret` is to avoid to commit the secret files to remote repository. If you want to use this feature, don't forget to include those files to your `.gitignore`:

```
*_secret.yml
```


## Accessing Configuration Settings

Consider the following config files.

config/default.yml

```yaml
size: 1
server: google.com
```

config/debug.yml

```yaml
size: 2
server: google.com
section:
  size: 3
  servers: [ {name: yahoo.com}, {name: amazon.com} ]
```

Notice that the variant specific config entries overwrite the common entries.

```java
Settings.size   // => 2
Settings.server // => google.com
```

Notice that object member notation is maintained even in nested entries.

```java
Settings.section.size // => 3
```

Notice array notation and object member notation is maintained.

```java
Settings.section.servers.get(0).name // => yahoo.com
Settings.section.servers.get(1).name // => amazon.com
```

## Type

Consider this config file:

```yaml
string_entry: string
int_entry: 1
double_entry: 1.0
date_entry: 2001-11-23 15:03:17
list_entry:
  - 1
  - 2
failed_list_entry:
  - 1
  - 'test'
object_entry:
  entry_a: 1
  entry_b: 2
list_of_map_entry:
  - a: 'a1'
    b: 'b1'
  - a: 'a2'
    b: 'b2'
```

The [snakeyaml](https://bitbucket.org/asomov/snakeyaml) library this plugin uses provides the following basic type conversions:

```java
Integer i = Settings.int_entry;
String s = Settings.string_entry;
Double d = Settings.double_entry;
Date da = Settings.date_entry;
List<Integer> list = Settings.list_entry;
```

The list with mixed type elements are not supported and throw an exception.

```
Error:Execution failed for task ':app:generateDevDebugSettings'.
> Not supported list with mixed type: [class java.lang.Integer, class java.lang.String]
```

The nested map entries are converted a generated class to achieve dot access.

```java
Settings.ObjectEntry oe = Settings.object_entry;
Integer entryA = object_entry.entry_a;
Integer entryB = object_entry.entry_b;
```

Also, the list of map are supported.

```java
List<Settings.ListOfMapEntryElement> entries = Settings.list_of_map_entry;
```

## Known issues

### Not found Settings class

Sometimes Android Studio does not detect generated Settings class. When you fact to this problem, `Refresh all Gradle projects` in the right pane of Android Studio may be helpful.

## License
```
Copyright 2017 Polidea
Copyright 2015 Takuya Miyamoto

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
