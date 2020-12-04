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
El programa cuenta con tests para las clases RGBDot y KMeans, para correrlo seguir laos siguientes pasos:
  - Acceder a la carpeta tests, dentro de la carpeta CloudCoverage.
     ```sh
        $ cd tests
      ```
  - Compilar el test.
    ```sh
        $ javac -cp lib/junit-4.13.1.jar:../src/main/java:. <nombreTest>.java
    ```
  - Correr el test.
    ```sh
        $ java -cp lib/junit-4.13.1.jar:lib/hamcrest-core-1.3.jar:. org.junit.runner.JUnitCore <nombreTest>
    ```
