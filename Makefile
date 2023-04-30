all: run

clean:
	rm -f out/Solver.jar out/Count.jar

out/Solver.jar: out/parcs.jar src/Solver.java
	@javac -cp out/parcs.jar src/Solver.java -d out
	@jar cf out/Solver.jar -C out Solver.class
	@rm -f out/Solver.class

out/Count.jar: out/parcs.jar src/Count.java
	@javac -cp out/parcs.jar src/Count.java -d out
	@jar cf out/Count.jar -C out Count.class
	@rm -f out/Count.class

build: out/Solver.jar out/Count.jar

run: out/Solver.jar out/Count.jar
	@cd out && java -cp 'parcs.jar:Solver.jar' Solver
