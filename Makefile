.PHONY: run
run: obj_dir/VDecoupledGcdDriver
	@./obj_dir/VDecoupledGcdDriver
obj_dir/VDecoupledGcdDriver: DecoupledGcdDriver.v Simulator.cpp
	@verilator --build --cc --exe DecoupledGcdDriver.v Simulator.cpp
DecoupledGcdDriver.v:
	@sbt "runMain gcd.DecoupledGcdDriver"
test:
	sbt test
c: clean
clean:
	rm -rf DecoupledGcdDriver.v obj_dir DecoupledGCD.anno.json target test_run_dir
