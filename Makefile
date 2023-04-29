all: build run

clean:
	rm -rf out/*

out/MandelbrotFractal.jar: out/parcs.jar src/MandelbrotFractal.java
	@javac -cp out/parcs.jar src/MandelbrotFractal.java
	@jar cfe out/MandelbrotFractal.jar MandelbrotFractal -C src .

build: out/MandelbrotFractal.jar

run: out/MandelbrotFractal.jar
	@cd out && java -cp 'parcs.jar:MandelbrotFractal.jar' MandelbrotFractal
