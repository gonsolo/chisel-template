#include <bit>
#include <cstdint>
#include <iostream>
#include <fstream>
#include <verilated.h>
#include "obj_dir/VDiffuse.h"
#include "obj_dir/VDiffuse___024root.h"

int main(int argc, char** argv) {
        using namespace std;

        Verilated::commandArgs(argc, argv);
	VDiffuse diffuse;

	std::cout << "Starting simulation." << std::endl;

        diffuse.reset = 0;
        auto rootp = diffuse.rootp;
        
        //float f = 0.3f;
        //cout << "Float input: " << f << endl;
        //rootp->io_float_input_value = bit_cast<uint32_t>(f);
        //rootp->io_input = 41;

	cout << "Value before simulation: " << (int)rootp->output_bits_ratio_r_foo << endl;

        for (uint64_t main_time  = 0; main_time < 5; main_time++) {
                if (main_time == 0) {
                        diffuse.reset = 1;
                } else {
                        diffuse.reset = 0;
                }
                diffuse.clock = main_time;
                diffuse.eval();
		cout << "Time: " << main_time << " value: " << rootp->output_bits_ratio_r_foo << endl;
        }

        //float floatOutput = bit_cast<float>(rootp->io_float_output_value);
        //cout << "Float output: " << floatOutput << endl;
#if 0
        ofstream image("image.ppm");
        image << "P3" << endl;
        image << "2 1" << endl;
        image << "255" << endl;
        for (int address = 0; address < 2; ++address) {
                //int red = rootp->Testbench__DOT__colorMemory_red[address];
                //int green = rootp->Testbench__DOT__colorMemory_green[address];
                //int blue = rootp->Testbench__DOT__colorMemory_blue[address];
                //image << red << " " << green << " " << blue << endl;
        }
        image.close();
#endif    
        //testbench.final();
	std::cout << "Finished simulation." << std::endl;
}


