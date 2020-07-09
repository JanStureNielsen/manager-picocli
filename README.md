The manager application has three commands which can be launched individually,
or launched from an @-file script, or launched from a shell:

```
manager -u <user> -p <pass> [list|create|delete] # run and exit
manager -u <user> -p <pass> @script              # run and exit
manager -u <user> -p <pass>                      # run shell
```

The picocli help looks like this:

```
> java -jar target/*.jar --help
Usage: manager [-hV] [-p=<password>] [-u=<username>] [@<filename>...] [COMMAND]
Manager CLI and shell for managing stuff with minimal fuss...
      [@<filename>...]   One or more argument files containing options.
  -h, --help             Show this help message and exit.
  -p, --password=<password>
                         password...
  -u, --username=<username>
                         username...
  -V, --version          Print version information and exit.
Commands:
  list    List some stuff with minimal fuss...
  create  Create some stuff with minimal fuss...
  delete  Deletes some stuff with minimal fuss...
  help    Create some stuff with minimal fuss...
```
but the JLine3 shell help looks like this:
```
> java -jar target/*.jar -u bob -p pass
manager> help
  System:
    exit   exit from app/script
    help   Create some stuff with minimal fuss...
  Builtins:
  ShellCommandRegistry:
    create Create some stuff with minimal fuss...
    delete Deletes some stuff with minimal fuss...
    help   Create some stuff with minimal fuss...
    list   List some stuff with minimal fuss...
manager> 
```
It would be nicer if the JLine3 was more like:
```
> java -jar target/*.jar -u bob -p pass
manager> help
  Commands:
    exit   exit from app/script
    help   Create some stuff with minimal fuss...
    create Create some stuff with minimal fuss...
    delete Deletes some stuff with minimal fuss...
    help   Create some stuff with minimal fuss...
    list   List some stuff with minimal fuss...
manager> 
```
but, unfortunately, that's not possible, yet.

# Building

    mvn clean install

# Running

```
java -jar target/*.jar -u bob -p 123 list        # run list and exit
java -jar target/*.jar -u bob -p 123 @commands   # run every command in `commands` and exit
java -jar target/*.jar -u bob -p 123             # run shell

```

# Discussions

I posted a [question on Stackoverflow](https://stackoverflow.com/questions/62334604/parsing-multiple-picocli-sub-commands-and-a-shell) 
and on [picocli's Google Group](https://groups.google.com/forum/?utm_medium=email&utm_source=footer#!msg/picocli/KEeSxrX7y2Q/-hijfys5BAAJ).
