# Домашнее задание
## Байт код, class-loader, инструментация, asm.
Автоматическое логирование.

## Цель
Понять как реализуется AOP, какие для этого есть технические средства.

## Задание
Разработайте такой функционал:
* Метод класса можно пометить самодельной аннотацией @Log. Например, так:

   ``` java
      class TestLogging {
         @Log
         public void calculation(int param) {};
      }
  ```
* При вызове этого метода "автомагически" в консоль должны логироваться значения параметров.
Например так:

   ```java
       class Demo {
           public void action() {
               new TestLogging().calculation(6);
           }
       }
   ```
* В консоле дожно быть:
  
   ``` executed method: calculation, param: 6  ```

Обратите внимание: явного вызова логирования быть не должно.

Учтите, что аннотацию можно поставить, например, на такие методы:
```java
    public void calculation(int param1)
    public void calculation(int param1, int param2)
    public void calculation(int param1, int param2, String param3)
```
