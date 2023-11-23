all: compile test run

.PHONY: r run test compile c clean t test

r: run
run: obj_dir/VDiffuse 
	@./obj_dir/VDiffuse
obj_dir/VDiffuse: Diffuse.v Simulator.cpp
	@verilator -CFLAGS -std=c++20 --build --cc --exe $^
Diffuse.v: src/main/scala/bsdf/Diffuse.scala
	sbt run
t: test
test:
	sbt test
compile:
	sbt compile
c: clean
clean:
	rm -rf Diffuse.v obj_dir *.json target test_run_dir
