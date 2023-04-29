all: build run

clean:
	rm -rf out/*

out/PiDigits.jar: out/parcs.jar src/PiDigits.java
	@javac -cp out/parcs.jar src/PiDigits.java
	@cd src && jar cvfe ../out/PiDigits.jar PiDigits ./*.class

build: out/PiDigits.jar

run: out/PiDigits.jar
	@cd out && java -cp 'parcs.jar:PiDigits.jar' PiDigits
