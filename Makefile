all: build run

clean:
	rm -f out/MandelbrotFractal.jar

out/MandelbrotFractal.jar: out/parcs.jar src/MandelbrotFractal.java
	@javac -cp out/parcs.jar src/MandelbrotFractal.java
	@jar cf out/MandelbrotFractal.jar -C src MandelbrotFractal.class -C src

build: out/MandelbrotFractal.jar

run: out/MandelbrotFractal.jar
	@cd out && java -cp 'parcs.jar:MandelbrotFractal.jar' MandelbrotFractal
