all: run

clean:
	rm -f out/MandelbrotParcs.jar out/MandelbrotWorker.jar

out/MandelbrotParcs.jar: src/MandelbrotParcs.java
	@javac -cp out/parcs.jar src/MandelbrotParcs.java
	@jar cf out/MandelbrotParcs.jar -C src .
	@rm -f src/MandelbrotParcs.class

out/MandelbrotWorker.jar: src/MandelbrotWorker.java
	@javac -cp out/parcs.jar:out/MandelbrotParcs.jar src/MandelbrotWorker.java
	@jar cf out/MandelbrotWorker.jar -C src .
	@rm -f src/MandelbrotWorker.class

build: out/MandelbrotParcs.jar out/MandelbrotWorker.jar

run: build
	@cd out && java -cp 'parcs.jar:MandelbrotParcs.jar:MandelbrotWorker.jar' MandelbrotParcs

.PHONY: all build run clean
