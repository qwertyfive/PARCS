all: build run

clean:
	rm -f out/PiDigits.jar

out/PiDigits.jar: out/parcs.jar src/PiDigits.java
	@javac -cp out/parcs.jar src/PiDigits.java
	@jar cf out/PiDigits.jar -C src PiDigits.class -C src

build: out/PiDigits.jar

run: out/PiDigits.jar
	@cd out && java -cp 'parcs.jar:PiDigits.jar' PiDigits