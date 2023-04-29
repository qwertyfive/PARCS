all: run

clean:
	rm -f out/MandelbrotSolver.jar out/Mandelbrot.jar

out/MandelbrotSolver.jar: out/parcs.jar src/MandelbrotSolver.java
	@javac -cp out/parcs.jar src/MandelbrotSolver.java
	@jar cf out/MandelbrotSolver.jar -C src MandelbrotSolver.class -C src
	@rm -f src/MandelbrotSolver.class

out/Mandelbrot.jar: out/parcs.jar src/Mandelbrot.java
	@javac -cp out/parcs.jar src/Mandelbrot.java
	@jar cf out/Mandelbrot.jar -C src Mandelbrot.class -C src
	@rm -f src/Mandelbrot.class

build: out/MandelbrotSolver.jar out/Mandelbrot.jar

run: out/MandelbrotSolver.jar out/Mandelbrot.jar
	@cd out && java -cp 'parcs.jar:MandelbrotSolver.jar:Mandelbrot.jar' MandelbrotSolver
