#include <bit>
#include <cstdint>
#include <iostream>
#include <fstream>
#include <verilated.h>
#include "obj_dir/VGCDDriver.h"

uint64_t main_time = 0;

int main(int argc, char** argv) {
        using namespace std;

        Verilated::commandArgs(argc, argv);
	VGCDDriver gcdDriver;

	std::cout << "Starting simulation." << std::endl;

        //testbench.reset = 0;
        //auto rootp = testbench.rootp;
        
        //float f = 0.3f;
        //cout << "Float input: " << f << endl;
        //rootp->io_float_input_value = bit_cast<uint32_t>(f);
        //rootp->io_input = 41;

        //for (int i = 0; i < 10; i++) {
        //        if (main_time == 0) {
        //                //cout << "reset" << endl;
        //                testbench.reset = 1;
        //        } else {
        //                testbench.reset = 0;
        //        }
        //        testbench.clock = main_time;

        //        //if ((main_time % 2) == 0) {
        //                //cout << "clock: 0" << endl;
        //        //        testbench.clock = 1;
        //        //}
        //        //if ((main_time % 2) == 1) {
        //                //cout << "clock: 1" << endl;
        //        //        testbench.clock = 0;
        //        //}
        //        testbench.eval();

        //        cout << (int)rootp->io_output << endl;

        //        main_time++;
        //}

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


