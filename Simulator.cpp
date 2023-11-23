#include <bit>
#include <cstdint>
#include <iostream>
#include <fstream>
#include <verilated.h>
#include "obj_dir/VDiffuse.h"
#include "obj_dir/VDiffuse___024root.h"

using namespace std;

void check(bool condition, string_view message) {
	if (!condition) {
		cout << "Error: condition \"" << message << "\" not met!" << endl;
		exit(EXIT_FAILURE);
	}
}

int main(int argc, char** argv) {
        Verilated::commandArgs(argc, argv);
	VDiffuse diffuse;
        diffuse.reset = 0;
        auto rootp = diffuse.rootp;
        float a = 66.6f;
        rootp->io_a = bit_cast<uint32_t>(a);
	auto steps = 4;
        for (uint64_t main_time  = 0; main_time < steps; main_time++) {
                if (main_time == 0) {
                        diffuse.reset = 1;
                } else {
                        diffuse.reset = 0;
                }
                diffuse.clock = main_time;
                diffuse.eval();
        }
        float out = bit_cast<float>(rootp->io_out);
	check(out == a, "out == a");
        diffuse.final();
	cout << "Ok." << endl;
}


