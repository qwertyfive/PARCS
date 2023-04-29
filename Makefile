all: run

clean:
	rm -f out/MandelbrotParcs.jar out/MandelbrotWorker.jar

out/MandelbrotParcs.jar: out/parcs.jar src/MandelbrotParcs.java
	@javac -cp out/parcs.jar src/MandelbrotParcs.java
	@jar cf out/MandelbrotParcs.jar -C src MandelbrotParcs.class -C src
	@rm -f src/MandelbrotParcs.class

out/MandelbrotWorker.jar: out/parcs.jar src/MandelbrotWorker.java
	@javac -cp out/parcs.jar src/MandelbrotWorker.java
	@jar cf out/MandelbrotWorker.jar -C src MandelbrotWorker.class -C src
	@rm -f src/MandelbrotWorker.class

build: out/MandelbrotParcs.jar out/MandelbrotWorker.jar

run: out/MandelbrotParcs.jar out/MandelbrotWorker.jar
	@cd out && java -cp 'parcs.jar:MandelbrotParcs.jar' MandelbrotParcs

.PHONY: all build run clean
