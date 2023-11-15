.PHONY: run
run: obj_dir/VGCDDriver
	@./obj_dir/VGCDDriver
obj_dir/VGCDDriver: GCDDriver.v Simulator.cpp
	@verilator --build --cc --exe GCDDriver.v Simulator.cpp
#obj_dir/VGCDDriver.cpp: GCDDriver.v
#	verilator -cc GCDDriver.v
GCDDriver.v:
	@sbt "runMain gcd.GCDDriver"
test:
	sbt test
c: clean
clean:
	rm -rf GCDDriver.v obj_dir GCD.anno.json target test_run_dir
