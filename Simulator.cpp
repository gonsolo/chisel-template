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

void test_diffuse(VDiffuse& diffuse, float a) {
	float invPi = numbers::inv_pi;
	float expected = a * invPi;
        diffuse.rootp->io_reflectance = floatToUint32(a);
	auto steps = 4;
        for (uint64_t main_time = 0; main_time < steps; main_time++) {
                if (main_time == 0) {
                        diffuse.reset = 1;
                } else {
                        diffuse.reset = 0;
                }
                diffuse.clock = main_time;
                diffuse.eval();
        }
        float out = uint32ToFloat(diffuse.rootp->io_out);
	//cout << "a: " << a << ", b: " << b << ", expected: " << expected << ", out: " << out << endl;
	//cout << "a: " << a << ", expected: " << expected << ", out: " << out << endl;
	check(out == expected, "out == a * b");
}

void simulateMultiply() {
	// TODO
	//test_multiply(diffuse, 33.2, 2.7);
	//test_multiply(diffuse, 1.0, 2.0);
	//test_multiply(diffuse, 0.0, 3.3);
	//test_multiply(diffuse, -127.3, 13.0);
}

void simulateDiffuse() {
	VDiffuse diffuse;
        diffuse.reset = 0;
        auto rootp = diffuse.rootp;
	test_diffuse(diffuse, 1.0);
        diffuse.final();
	cout << "Ok." << endl;

}

int main(int argc, char** argv) {
        Verilated::commandArgs(argc, argv);
	simulateMultiply();
	simulateDiffuse();
}

