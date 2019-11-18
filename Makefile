OUT_DIR := out/production/FEUP-AIAD
SRC_DIR := src
JADE_JAR := jade.jar
FILES := find $(SRC_DIR) -name "*.java"

all: mkdir
	javac -classpath $(JADE_JAR) -d $(OUT_DIR) `$(FILES)`

mkdir:
	@mkdir -p $(OUT_DIR)

run:
	java -classpath $(OUT_DIR):$(JADE_JAR) main.Main
