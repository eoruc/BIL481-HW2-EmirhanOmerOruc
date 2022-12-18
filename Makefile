
run: clean
	javac *.java
	mkdir -p logs
	cat input/System.java | java -Djava.util.logging.config.file=logging.properties SuggestionEngine getProper 3

run2: clean
	javac *.java
	mkdir -p logs
	cat input/String.java | java -Djava.util.logging.config.file=logging.properties SuggestionEngine toUpper 3


run3: clean
	javac *.java
	mkdir -p logs
	cat input/Test1.java | java -Djava.util.logging.config.file=logging.properties SuggestionEngine test 3

run4: clean
	javac *.java
	mkdir -p logs
	cat input/Test1.java | java -Djava.util.logging.config.file=logging.properties SuggestionEngine test 4

run5: clean
	javac *.java
	mkdir -p logs
	cat input/Test1.java | java -Djava.util.logging.config.file=logging.properties SuggestionEngine test 5

run6: clean
	javac *.java
	mkdir -p logs
	cat input/Test1.java | java -Djava.util.logging.config.file=logging.properties SuggestionEngine get 3

run7: clean
	javac *.java
	mkdir -p logs
	cat input/Test1.java | java -Djava.util.logging.config.file=logging.properties SuggestionEngine data 3

run8: clean
	javac *.java
	mkdir -p logs
	cat input/Test1.java | java -Djava.util.logging.config.file=logging.properties SuggestionEngine getData 3

run9: clean
	javac *.java
	mkdir -p logs
	cat input/Test1.java | java -Djava.util.logging.config.file=logging.properties SuggestionEngine abbbbbbba 3

run10: clean
	javac *.java
	mkdir -p logs
	cat input/Test1.java | java -Djava.util.logging.config.file=logging.properties SuggestionEngine getFullData 3

run11: clean
	javac *.java
	mkdir -p logs
	cat input/Test2.java | java -Djava.util.logging.config.file=logging.properties SuggestionEngine only 1

run12: clean
	javac *.java
	mkdir -p logs
	cat input/Test2.java | java -Djava.util.logging.config.file=logging.properties SuggestionEngine only 2


run13: clean
	javac *.java
	mkdir -p logs
	cat input/Test2.java | java -Djava.util.logging.config.file=logging.properties SuggestionEngine only 999


clean:
	rm -f *.class
