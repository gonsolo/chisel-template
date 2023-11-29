
BSDF = src/main/scala/bsdf

all: compile test simulate

.PHONY: c clean compile s simulate t test

s: simulate
simulate: obj_dir/VDiffuse
	@echo "** Simulating"
	@./obj_dir/VDiffuse
obj_dir/VDiffuse: Diffuse.v Simulator.cpp
	@verilator -CFLAGS -std=c++20 --build --cc --exe $^
Diffuse.v: $(BSDF)/Constants.scala $(BSDF)/Diffuse.scala $(BSDF)/Multiply.scala $(BSDF)/Writer.scala
	@sbt run
t: test
test:
	@echo "** Testing"
	@sbt test
compile:
	@echo "** Compiling"
	@sbt compile
c: clean
clean:
	@rm -rf Diffuse.v obj_dir *.json target test_run_dir
