# Dobble Game Application
Este proyecto es una implementación del juego de mesa Dobble.

Es un fork de https://github.com/spring-projects/spring-petclinic 

Las claves por defecto de cada uno de los usuarios son las siguientes: 

- Para el usuario "admin" (es administrador) →  Usuario: admin / Contraseña: dobble_admin

- Para el usuario "dobble" (es usuario jugador) → Usuario: dobble / Contraseña: dobble

- Para el usuario "dobble2" (es usuario jugador) →  Usuario: dobble2 / Contraseña: dobble

(Podemos encontrar estos datos en el archivo data.sql)


## Cómo funciona el juego
El desarrollo normal de una partida del modo de juego “El Foso” comienza colocando una carta de la baraja boca arriba en el centro de la mesa y el resto de las cartas son repartidas boca abajo entre todos los jugadores. Una vez repartidas todas, les darán la vuelta y el juego comenzará, de tal forma que el primer jugador que identifique los símbolos que aparezcan tanto en su primera carta como en la del centro de la mesa, se descartará de su carta y esta pasará al centro de la mesa, continuando el juego empleando la misma dinámica. La partida concluirá cuando un jugador haya logrado deshacerse de todas las cartas de su montón, proclamándose, así como el ganador de la partida. 

## Running Dobble backend locally
Dobble is a [Spring Boot](https://spring.io/guides/gs/spring-boot) application built using [Maven](https://spring.io/guides/gs/maven/). You can build a jar file and run it from the command line:


```
git clone https://github.com/gii-is-DP1/DP1--2023-2024-l3-1.git
cd DP1--2023-2024-l3-1
./mvnw package
java -jar target/*.jar
```

You can access Dobble's backend here: [http://localhost:8080/](http://localhost:8080/swagger-ui/index.html)



Or you can run it from Maven directly using the Spring Boot Maven plugin. If you do this it will pick up changes that you make in the project immediately (changes to Java source files require a compile as well - most people use an IDE for this):

```
./mvnw spring-boot:run
```
## Database configuration

In its default configuration, Dobble uses an in-memory database (H2) which
gets populated at startup with data. The INSERTs are specified in the file data.sql.

## Working with React Dobble in your IDE

### Prerequisites
The following items should be installed in your system:
* Java 17 or newer.
* Node.js 18 or newer.
* git command line tool (https://help.github.com/articles/set-up-git)
* Your preferred IDE 
  * Eclipse with the m2e plugin. Note: when m2e is available, there is an m2 icon in `Help -> About` dialog. If m2e is
  not there, just follow the install process here: https://www.eclipse.org/m2e/
  * [Spring Tools Suite](https://spring.io/tools) (STS)
  * IntelliJ IDEA
  * [VS Code](https://code.visualstudio.com)

### Steps:

1) On the command line
```
git clone https://github.com/gii-is-DP1/DP1--2023-2024-l3-1.git
```
2) Inside Eclipse or STS
```
File -> Import -> Maven -> Existing Maven project
```

Then either build on the command line `./mvnw generate-resources` or using the Eclipse launcher (right click on project and `Run As -> Maven install`) to generate the css. Run the application main method by right clicking on it and choosing `Run As -> Java Application`.

3) Inside IntelliJ IDEA

In the main menu, choose `File -> Open` and select the Dobble [pom.xml](pom.xml). Click on the `Open` button.

CSS files are generated from the Maven build. You can either build them on the command line `./mvnw generate-resources`
or right click on the `DP1--2023-2024-l3-1` project then `Maven -> Generates sources and Update Folders`.

A run configuration named `DobbleApplication` should have been created for you if you're using a recent Ultimate
version. Otherwise, run the application by right clicking on the `DobbleApplication` main class and choosing
`Run 'DobbleApplication'`.

4) Navigate to Dobble
Visit [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) in your browser.


## Looking for something in particular?

|Spring Boot Configuration | Class or Java property files  |
|--------------------------|---|
|The Main Class | [DobbleApplication](https://github.com/gii-is-DP1/DP1--2023-2024-l3-1/blob/main/src/main/java/com/dobble/DobbleApplication.java) |
|Properties Files | [application.properties](https://github.com/gii-is-DP1/DP1--2023-2024-l3-1/blob/main/src/main/resources/application.properties) |


## Starting the frontend

Dobble is implemented with a React frontend in the folder named "frontend".
You can start the development server to see frontend using the command (maybe you should use the command npm insall prior to this):
```
npm start
```
You can then access the Dobble frontend at [http://localhost:3000](http://localhost:3000)
