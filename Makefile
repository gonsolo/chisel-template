all: compile

.PHONY: r run test compile c clean t test

r: run
run: obj_dir/VDiffuse
	@./obj_dir/VDiffuse
obj_dir/VDiffuse: Diffuse.v Simulator.cpp
	@verilator --build --cc --exe $^
Diffuse.v:
	@sbt "runMain bsdf.Diffuse"
t: test
test:
	sbt test
compile:
	sbt compile
c: clean
clean:
	rm -rf Diffuse.v obj_dir *.json target test_run_dir
