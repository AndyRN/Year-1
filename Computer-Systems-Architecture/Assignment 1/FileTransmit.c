/***************************************************************************************
Author: Andy Nutt
Student ID: 11004967
File: FileTransmit.c 
Version: 1.4
***************************************************************************************/

#include <stdio.h> 
#include <conio.h>
#define FEND 0x40

void FileTransmit() {

char wtmode[] = "w";							
char rdmode[] = "r";							
char fname[] = "H:\\Send.txt";					
char pname[] = "COM2";							
char error1[] = "com port failed to open";		
char error2[] = "data file failed to open";		
char error3[] = "pfile failed to open";
char passwordfile[]= "H:\\Password.txt";
char NL[] = "\n";

FILE * fileptr;
FILE * pfileptr;
FILE * portptr;

__asm {
		lea eax,wtmode		 				
		push eax			 			
		lea eax,pname		 
		push eax			 
		call DWORD PTR (fopen) 				
		mov portptr,eax   					
		add esp,4		
		cmp portptr,0						
		// This opens the port ready for writing, also checks if it has been properly initiated.

		jne portOK							

		lea eax,error1
		push eax
		call DWORD PTR (printf)
		add esp,4
		jmp endit
		// Otherwise this error message will be displayed and the program will end.

	portOK:									
		lea eax,rdmode
		push eax
		lea eax,fname
		push eax
		call DWORD PTR (fopen)
		mov fileptr,eax
		add esp,4							
		cmp fileptr,0     			
		// This will open the file ready for reading, also checks if it has been properly opened.

		jne fileOK          				

		lea eax,error2
		push eax
		call DWORD PTR (printf)				
		add esp,4
		jmp endit							
		// Otherwise this error message will be displayed and the program will end.

	fileOK:
		lea eax,rdmode
		push eax
		lea eax,passwordfile
		push eax
		call DWORD PTR (fopen)
		mov pfileptr,eax
		add esp,4
		cmp pfileptr,0     					
		// This will open the password file ready for reading, also checks if it has been properly opened.

		jne pfileOK          				

		lea eax,error3
		push eax
		call DWORD PTR (printf)				
		add esp,4
		jmp endit							
		// Otherwise this error message will be displayed and the program will end.

	pfileOK:
		mov eax,fileptr				      	
		push eax
		call DWORD PTR (fgetc) 				
		add esp,4
		// Gets the next character that is in the file.

		mov ebx,eax
		cmp eax,EOF		
		je close
		// Moves the character into the ebx and checks to see if it is the end of the file.

		mov eax,pfileptr
		push eax
		call DWORD PTR (fgetc)
		add esp,4
		// Gets the next character within the password file.

		cmp eax,EOF
		jne dont_reset
		// Checks if it is the end of the file.

		push pfileptr
		call DWORD PTR (rewind)
		add esp,4
		// Moves the pointer back to the start of the password file if it has hit the end of the file.

		mov eax,pfileptr
		push eax
		call DWORD PTR (fgetc)
		add esp,4
		// Then gets the next character again.

	dont_reset:
		xor ebx,eax							
		// Encrypts the character from the file being sent with the character from the password.

		push portptr
		push ebx
		call DWORD PTR (fputc)
		add esp,8
		// Sends the encrypted character down the port.

		jmp pfileOK			 				
		// Loops.

	close:
		mov eax,portptr
		push eax							
		mov eax,FEND						
		push eax
		call DWORD PTR (fputc)
		add esp,8

		mov eax,portptr					
		push eax
		call DWORD PTR (fclose)				
		mov eax,fileptr
		push eax
		call DWORD PTR (fclose)				
		mov eax,pfileptr
		push eax
		call DWORD PTR (fclose)
		add esp,12							
		// Closes both the files and the port.

	endit:	
	
} //asm

printf("File sent.");
getchar();

} //main

// References:
// MSDN for ASM commands and C commands.
// Microsoft for Visual Studio 2010's API.
// Rob's CSA book for initial ASM transfer code.