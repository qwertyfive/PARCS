all: run

clean:
	rm -f out/MandelbrotParcs.jar

out/MandelbrotParcs.jar: src/MandelbrotParcs.java
	@javac -cp out/parcs.jar src/MandelbrotParcs.java
	@jar cf out/MandelbrotParcs.jar -C src .
	@rm -f src/MandelbrotParcs.class src/MandelbrotWorker.class


build: out/MandelbrotParcs.jar

run: out/MandelbrotParcs.jar
	@cd out && java -cp 'parcs.jar:MandelbrotParcs.jar' MandelbrotParcs


.PHONY: all build run clean
