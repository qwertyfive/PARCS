JAVAC = javac
JFLAGS = -cp ".:./parcs.jar"
JAVA = java
MAIN_CLASS = MandelbrotParcs
WORKER_CLASS = MandelbrotWorker
PARCS_JAR = parcs.jar

all: compile

compile: $(MAIN_CLASS).class $(WORKER_CLASS).class

$(MAIN_CLASS).class: $(MAIN_CLASS).java
	$(JAVAC) $(JFLAGS) $(MAIN_CLASS).java

$(WORKER_CLASS).class: $(WORKER_CLASS).java
	$(JAVAC) $(JFLAGS) $(WORKER_CLASS).java

run: compile
	$(JAVA) -cp ".:./parcs.jar" $(MAIN_CLASS)

clean:
	rm -f *.class

parcs_jar:
	cp /out/parcs.jar .

prepare: parcs_jar

.PHONY: all compile run clean parcs_jar prepare
