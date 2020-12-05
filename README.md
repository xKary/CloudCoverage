# Cloud Coverage

### Requerimientos previos

  - Tener java 8 o una versión superior

### Usar el programa
  - Descargar el la carpera CloudCoverage en su ordenador.
  - Abrir la terminal y acceder a dicha carpeta
    Linux
     ```sh
        $ cd CloudCoverage
      ```
   - Crear una carpeta con el nombre de build
        ```sh
            $ mkdir build
        ```
  - Para compilar el programa escriba el comando
    ```sh
        $ javac -cp src/main/java/ src/main/java/app/App.java -d build/
    ```
 - Para utilizar el programa utilice el siguiente comando
     ```sh
        $ java -cp build/ app.App <nombre Imagen> <bandera>
    ```
    Las imágenes de las cuales se desee conocer la cobertura nubosa debes estar guardadas en la carpeta src/main/resources.
    La bandera indica si se debe generar la imagen a blanco y negro, dicha bandera debe ser la letra S.
    Ejemplos:
    ```sh
        $ java -cp build/ app.App 11833.jpg
        $ java -cp build/ app.App 11833.jpg S
        $ java -cp build/ app.App 11833.jpg s
    ```  
 - El programa iniciará su ejecución y al finanlizar le indicará el índice de cobertura nubosa de la imagen y en caso de que haya generado la imagen en blanco y negro, esta se guardará en el la carpeta src/main/resources con el nombre de la imagen original con el sufijo "-seg" antes de la extensión.
### Tests
El programa cuenta con tests para la clase RGBDot, para correrlo seguir los siguientes pasos:
Dentro de la carpeta CloudCoverage.
  - Compilar el test.
    ```sh
        $ javac -cp tests/lib/junit-4.13.1.jar:src/main/java:. tests/RGBDotTest.java -d build
    ```
  - Correr el test.
    ```sh
        $ java -cp tests/lib/junit-4.13.1.jar:tests/lib/hamcrest-core-1.3.jar:build:. org.junit.runner.JUnitCore RGBDotTest
    ```
