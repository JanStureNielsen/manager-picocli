The manager application has three commands which can be launched individually,
or launched from an @-file script, or launched from a shell:

```
manager -u <user> -p <pass> [list|create|delete] # run and exit
manager -u <user> -p <pass> @script              # run and exit
manager -u <user> -p <pass>                      # run shell
```

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
