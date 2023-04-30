all: run

clean:
	rm -f out/Solver.jar out/Count.jar

out/Solver.jar: out/parcs.jar src/Solver.java
	@javac -cp out/parcs.jar src/Solver.java
	@jar cf out/Solver.jar -C src Solver.class -C src
	@rm -f src/Solver.class

out/Count.jar: out/parcs.jar src/Count.java
	@javac -cp out/parcs.jar src/Count.java
	@jar cf out/Count.jar -C src Count.class -C src
	@rm -f src/Count.class

build: out/Solver.jar out/Count.jar

run: out/Solver.jar out/Count.jar
	@cd out && java -cp 'parcs.jar:Solver.jar' Solver