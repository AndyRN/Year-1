/***************************************************************************************
* Author: Andy Nutt
* Student ID: 11004967
* File: FileReceive.c 
* Version: 1.7
***************************************************************************************/

#include <stdio.h>
#include <conio.h>
#define FEND 0x40

void FileReceive() {				

char wtmode[] = "w";							
char rdmode[] = "r";							
char fname[] = "H:\\Received.txt";					
char pname[] = "COM2";							
char error1[] = "com port failed to open";		
char error2[] = "data file failed to open";		
char error3[] = "pfile failed to open";
char passwordfile[]= "H:\\Password.txt";

FILE *fileptr;
FILE *pfileptr;
FILE *portptr;

printf("\"");
__asm { 
		lea eax,rdmode		 			
		push eax			 			
		lea eax,pname		 
		push eax			 
		call DWORD PTR (fopen) 				
		mov portptr,eax   				
		add esp,4
		cmp portptr,0					
		// This will open the port ready for reading, also checks if it has been properly opened.

		jne portOK						

		lea eax,error1
		push eax
		call DWORD PTR (printf)
		add esp,4
		jmp endit
		// Otherwise this error message will be displayed and the program will end.

	portOK:									
		lea eax,wtmode
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
		mov eax,portptr			        
		push eax
		call DWORD PTR (fgetc) 				
		add esp,4			 			
		// Gets the next character that is being sent down the port.

		mov ebx,eax
		cmp ebx,FEND						
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
		// Decrypts the character from the port with the character from the password.

		push portptr
		push ebx
		call DWORD PTR (putch)
		add esp,8
		// Displays the decrypted character on the screen.

		push fileptr
		push ebx
		call DWORD PTR (fputc)
		add esp,8
		// Adds the character to the designated file.

		jmp pfileOK			 				
		// Loops.

	close:
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

printf("\"");
printf("\nFile received. Stored in the file 'Received.txt'.");
getchar();			// Holds the program to see any messages.
 
} //main

// References:
// MSDN for ASM commands and C commands.
// Microsoft for Visual Studio 2010's API.
// Rob's CSA book for initial ASM transfer code.