#include <bit>
#include <cstdint>
#include <iostream>
#include <fstream>
#include <numeric>
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

uint32_t floatToUint32(float value) {
        return bit_cast<uint32_t>(value);
}

float uint32ToFloat(uint64_t value) {
        return bit_cast<float>((uint32_t)value);
}

void error() {
	cout << "Error!" << endl;
	exit(EXIT_FAILURE);
}

void enqueue(VDiffuse& diffuse, float a) {
        diffuse.rootp->input_bits_reflectance = floatToUint32(a);
	diffuse.rootp->input_valid = true;
	while (!diffuse.rootp->input_ready) {
		diffuse.clock += 1;
		diffuse.eval();
	}
	diffuse.clock += 1;
	diffuse.eval();
	diffuse.rootp->input_valid = false;
}

void expectDequeue(VDiffuse& diffuse, float a) {
	float invPi = numbers::inv_pi;
	float expected = a * invPi;
	diffuse.rootp->output_ready = true;
	while (!diffuse.rootp->output_valid) {
		diffuse.clock += 1;
		diffuse.eval();
	}
	if (diffuse.rootp->output_valid != true) {
		error();
	}
	//cout << "Input: " << a << ", expected (x / pi): " << uint32ToFloat(diffuse.rootp->output_bits_out) << endl;
	if (diffuse.rootp->output_bits_out != floatToUint32(expected)) {
		error();
	}
	diffuse.clock += 1;
	diffuse.eval();
	diffuse.rootp->output_ready = false;
}

void test_diffuse(VDiffuse& diffuse, float a) {
	enqueue(diffuse, a);
	expectDequeue(diffuse, a);
}

void simulateDiffuse() {
	VDiffuse diffuse;
        diffuse.reset = 0;
        auto rootp = diffuse.rootp;
	test_diffuse(diffuse, 1.0);
	test_diffuse(diffuse, 0.5);
        diffuse.final();
	cout << "Ok." << endl;

}

int main(int argc, char** argv) {
        Verilated::commandArgs(argc, argv);
	simulateDiffuse();
}

