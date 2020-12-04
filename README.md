# CloudCoverage

Programa Principal
javac -cp src/main/java/ src/main/java/app/App.java -d build/
java -cp build/ app.App <nombre Imagen> <bandera>
Test
javac -cp lib/junit-4.13.1.jar:. RGBDotTest.java
java -cp lib/junit-4.13.1.jar:lib/hamcrest-core-1.3.jar:. org.junit.runner.JUnitCore RGBDotTest
