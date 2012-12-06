/***************************************************************************************
* Author: Andy Nutt
* Student ID: 11004967
* File: Main.c 
* Version: 1.1
***************************************************************************************/
 
#include "FileTransmit.h"					
#include "FileReceive.h"
#include <stdio.h>

int main() {
	char response;								
	char response2;							

	printf("FileTransferASM!\n");
	printf("What mode would you like to use?(t/r): ");
	scanf("%c", &response);						
	
	if (response == 't'){
		printf("Transmitting...\n");
		FileTransmit();				
		// Goes into transmitter mode.
	} else if (response == 'r'){
		printf("Receiving...\n");
		FileReceive();					
		// Goes into receiver mode.
	}

	getchar();	
	return 0;
	// Holds the program to see any messages, then finishes.
	
}

// References:
// MSDN for ASM commands and C commands.
// Microsoft for Visual Studio 2010's API.
// Rob's CSA book for initial ASM transfer code.