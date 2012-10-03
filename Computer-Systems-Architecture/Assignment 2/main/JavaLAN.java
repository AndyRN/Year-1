package main;

public class JavaLAN {

  private static String COM1 = "/dev/ttyS0";
  private static String COM2 = "/dev/ttyS1";
  private static final int PACKETSIZE = 16, PENDTABLE_SIZE = 26, DELAY = 1000;
  private static int pendindex = 0; // Index into Pending Table
  private static int rxCharCount;
  private static char myaddr = 0, kbdpacket[] = new char[PACKETSIZE],
          rxpacket[] = new char[PACKETSIZE], txpacket[] = new char[PACKETSIZE],
          inch, key;
  public static Term terminal;
  private static SerialPortHandler sio;
  private static PendtableRecord[] pendtable = new PendtableRecord[PENDTABLE_SIZE];
  //
  // Keyboard states
  private static final int LOGIN = 0, ADDRPENDING = 1, MENU = 2, GETADDR = 3,
          INPUTMESS = 4;
  private static int kindex; // keyboard user input state machine index
  //
  // Positions of special characters in a packet
  private static final int SOM = 0, DEST = 1, SRC = 2, TYPE = 3, CHKSM = 14,
          EOM = 15;
  //
  // Receiver states
  private static final int WAITING = 0, RECEIVING = 1, ARRIVED = 2,
          DECODING = 3;
  private static int rxindex = WAITING;
  //
  // Extra variables
  private static int keycnt, tout = 0, countNulls = 0; // Timeout for incorrect login.
  private static boolean debugMode = true, confirm = false, menu = true,
          canLogin = false, cantLogin = false, message = false, logout = false;
  private static char c;
  //
  // Debug Window
  private static DebugDisplay debugWindow;
 
  /**
   * @param args - Default. 
   * Function - Enables each window for the user, sets each port, and enables the main program loop.
   */
  public static void main(String[] args) {
    terminal = new Term(); // Creates terminal window.
    debugWindow = new DebugDisplay(); // Creates debug window.

    welcome("Matt and Andy"); // Displays welcome message

    setPorts(); // Sets up com ports according to operating system.

    terminal.println("What port would you like to use?");
    terminal.print("Type '1' for \"COM1\" or '2' for \"COM2\": ");

    boolean commFlag = true;
    while (commFlag == true) {
      if (terminal.getkbhit() == true) {
        key = terminal.getChar();
        terminal.putChar(key);
        terminal.println("");
        // Obtains the character input by the user.
        if (key == '1') {
          sio = new SerialPortHandler(COM1); // Sets up port
          commFlag = false;
        } else if (key == '2') {
          sio = new SerialPortHandler(COM2); // Sets up port
          commFlag = false;
        } else {
          terminal.print("\nInvalid port\n:");
        }
        // Uses the input character 'key' and tries to set the port requested.
      }
    }

    // Sets the correct index for the userIO function.
    kindex = LOGIN;
    terminal.println("Login - ");
    terminal.print("Enter a character (A-Z): ");

    // Instantiate the pendtable
    for (int i = 0; i < PENDTABLE_SIZE; i++) {
      pendtable[i] = new PendtableRecord();
    }
    
    // Super loop for program tasking.
    while (true) {
      userIO();
      receiver();
      transmitter();
    } // while(program running)
  } // main()

/**
* Function - Informs the user the type of OS they're using and customises the ports for that specific OS.
*/
  private static void setPorts() {
    String os = System.getProperty("os.name").toLowerCase();
    // Gets the name of the operating system in lowercase.
    if (os.contains("linux") || os.contains("unix")) {
      terminal.println("OS = A version of Linux/Unix\n");
      COM1 = "/dev/ttyS0";
      COM2 = "/dev/ttyS1";
    } else if (os.contains("windows")) {
      terminal.println("OS = A version of Windows\n");
      COM1 = "COM1";
      COM2 = "COM2";
    } // Changes the values in the COM1/COM2 variables based on the operating system.
  } // getOS()

  /**
   * Function - Allows the user to interact with the program via various methods inc. a menu.
   */
  private static void userIO() {
    switch (kindex) {
      case LOGIN:
        if (terminal.getkbhit() == true) {
          key = Character.toUpperCase(terminal.getChar());
          if (key >= 'A' && key <= 'Z') {
            terminal.putChar(key);
            terminal.putChar('\n');
            myaddr = key; // Save my login id to check against response from Lan
            kbdpacket = clearpacket(kbdpacket);
            kbdpacket[DEST] = myaddr;
            kbdpacket[SRC] = myaddr;
            kbdpacket[TYPE] = 'L'; // a login packet
            setchksum(kbdpacket);

            for (int i = 0; i < PENDTABLE_SIZE; i++) {
              pendtable[i].setLoggedin(0);
              pendtable[i].setPending(0);
              pendtable[i].setDelay(0);
            }// for

            // Temporary display the keyboard packet
            terminal.print("Packet: ");
            terminal.println(new String(kbdpacket));
            
            pendtable[myaddr - 'A'].setPacket(kbdpacket);
            // Try to login 5 times, with delay.
            pendtable[myaddr - 'A'].setPending(1);
            pendtable[myaddr - 'A'].setDelay(DELAY);
            // Move to next state
            kindex = ADDRPENDING;
          } // if (key is a valid address)
        } // if (key hit)
        break;
      case ADDRPENDING:
        if (!canLogin && !cantLogin) {
          tout++;
          if (tout > 1000000) {
            terminal.print("\n---That ID is already taken, or check the cable for breakages---\n:");
            kindex = LOGIN;
            pendtable[myaddr - 'A'].setPending(0);
            myaddr = 0;
            tout = 0;
          } // If(tout) - Stops attempting to login after an amount of time has passed.
        }
        if (canLogin == true) {
          terminal.println("\n---Your ID is now set to - '" + myaddr + "'---");
          kindex = MENU;
          canLogin = false;
          tout = 0;
        } // If(canLogin) - Enables user to continue to the menu state.
        if (cantLogin == true) {
          terminal.println("\n---That ID is already taken---\n");
          kindex = LOGIN;
          terminal.print("Enter a character (A-Z): ");
          pendtable[myaddr - 'A'].setPending(0);
          myaddr = 0;
          tout = 0;
          cantLogin = false;
        } // If(cantLogin) - Stops user continuing to the menu state.
        menu = true;
        break;
      case MENU:
        if (menu == true) {
          terminal.println("\n-------------------------------------------------");
          terminal.println("| = [MENU] Select an option = \t|");
          terminal.println("| : L(I)st everyone logged in\t|");
          terminal.println("| : (D)estination \t\t\t|");
          terminal.println("| : (S)end \t\t\t|");
          terminal.println("| : (C)ancel \t\t\t|");
          terminal.println("| : De(B)ug ~ " + debugMode + "\t\t\t|");
          terminal.println("| : (L)ogout \t\t\t|");
          terminal.println("-------------------------------------------------");
          terminal.print(">> ");
          menu = false;
        } // If(menu) - Menu is only displayed at certain points.
        c = Character.toUpperCase(terminal.getChar());

        switch (c) {
          case 'M':
            menu = true;
            break;
          case 'I':
            terminal.println("Everyone logged in: ");
            for (int i = 0; i <= 25; i++) {
              if (pendtable[i].getLoggedin() == 1 || pendtable[i].getLoggedin() == -1) {
                char login = (char) (i + 'A');
                terminal.println("- " + login);
              }
            } // For(i) - Cycles through the alphabet and displays any pendtable record logged in.
            terminal.println("");
            terminal.print(">> ");
            break;
          case 'D':
            terminal.print("What address?: ");
            kindex = GETADDR;
            break;
          case 'S':
            if (Character.isLetter(kbdpacket[DEST])) {
              pendtable[kbdpacket[DEST] - 'A'].setPacket(kbdpacket);
              pendtable[kbdpacket[DEST] - 'A'].setPending(1);
              pendtable[kbdpacket[DEST] - 'A'].setDelay(DELAY);
              terminal.println("---Sent---");
              terminal.println("");
              terminal.print(">> ");
            } // If(kbdpacket) - Sends message as a packet.
            break;
          case 'C':
            clearpacket(kbdpacket);
            terminal.println("---Packet cleared---");
            terminal.println("");
            terminal.print(">> ");
            break;
          case 'B':
            if (debugMode == false) {
              debugMode = true;
              terminal.println("---Debug Mode is now ON---");
              terminal.println("");
              terminal.print(">> ");
            } else {
              debugMode = false;
              terminal.println("---Debug Mode is now OFF---");
              terminal.println("");
              terminal.print(">> ");
            } // If(debugMode) - Alters printing incoming and outgoing packets to the debug window.
            break;
          case 'L':
            terminal.println("Are you sure you wish to logout? (Y/N)");
            confirm = true;
            break;
          case 'Y':
            if (confirm) {
              terminal.println("Logging out now...");
              confirm = false;
              clearpacket(kbdpacket);
              kbdpacket[DEST] = myaddr;
              kbdpacket[SRC] = myaddr;
              kbdpacket[TYPE] = 'X';
              setchksum(kbdpacket);
              // Sets up logout packet.
              
              sio.sendPacket(kbdpacket);
              if (debugMode == true) {
                debugWindow.printlnOut(new String(kbdpacket));
              }
              // Sends logout packet.
              
              logout = true; // This is set to stop being logged out by a broken packet.

              kindex = LOGIN;
              terminal.print("\n----------------------------------------------------------------------------------------------------------------\n");
              terminal.println("\nLogin - ");
              terminal.print("Enter a character (A-Z): ");
            }
            break;
          case 'N':
            terminal.println("---Logout cancelled---\n");
            confirm = false;
            break;
          default:
            break;
        } // switch (menu choice)
        break;
      case GETADDR:
        if (terminal.getkbhit() == true) {
          key = Character.toUpperCase(terminal.getChar());
          terminal.putChar(key);
          terminal.putChar('\n');
          // Displays inputted character to the terminal.
          if (key >= 'A' && key <= 'Z') {
            if (pendtable[key - 'A'].getLoggedin() == 0) {
              terminal.println("---They aren't logged in right now---\n");
              kindex = MENU;
            } else {
              kbdpacket = clearpacket(kbdpacket);
              kbdpacket[DEST] = key;
              kbdpacket[SRC] = myaddr;
              kbdpacket[TYPE] = 'D';
              keycnt = 4;
              kindex = INPUTMESS;
              message = true;
            }
          } // if valid address
          // Checks if the inputted key is valid, and if it is, sets up a packet to be sent.
        }
        break;
      case INPUTMESS:
        if (message == true) {
          terminal.print("\nEnter Message: ");
          message = false;
        }
        // Flag to only show the message once.
        if (terminal.getkbhit() == true) {
          if (keycnt < 14) {
            key = terminal.getChar();
            if (key == '\n') {
              while (keycnt != 14) {
                kbdpacket[keycnt] = ' ';
                keycnt++;
              }
              // If return is hit, it fills the rest of the packet with spaces.
            } else {
              terminal.putChar(key);
              kbdpacket[keycnt] = key;
              keycnt++;
            }
            // Places the inputted character into the packet.
            if (keycnt == 14) {
              terminal.println("\n---Message Complete---");
              setchksum(kbdpacket);
              kindex = MENU;
              terminal.println("");
              terminal.print(">> ");
            } // if(end of message)
            // Checks if the message is complete and returns to the menu.
          } // if (message character)
        } // if (key hit)
        break;
      default:
        System.out.println("\nError in keyboard user state machine");
        break;
    } // switch(kindex)
  } // userIO()

  /**
   * Function - Receives characters from the COM port specified and displays them if they are for the user.
   */
  private static void receiver() {

    switch (rxindex) {
      case WAITING:
        if ((inch = sio.getChar()) != (char) -1) {
          if (inch == '{') {
            rxpacket[0] = '{';
            rxindex = RECEIVING;
          }
        } // if(inch is a '{')
        break;
        // Gets the character from the COM port and checks if it's the start of a packet.

      case RECEIVING:
        for (rxCharCount = 1; rxCharCount <= 15; rxCharCount++) {
          inch = sio.getChar();
          if (inch != (char) -1) {
            rxpacket[rxCharCount] = inch;
            countNulls = 0;
          } else {
            rxCharCount--;
            countNulls++;
            if (countNulls >= 5000) {
              terminal.println("---Packet was not completely received---");
              rxCharCount = 16;
            }
          }
        }
        // Gets all the characters from the COM port and places into rxpacket.
        // Checks if not enough characters are sent, or a null character is sent.
        rxCharCount = EOM;
        rxindex = ARRIVED;
        break;

      case ARRIVED:
        if (rxpacket[DEST] >= 'A' && rxpacket[DEST] <= 'Z') {
          if (rxpacket[rxCharCount] == '}') {
            rxindex = DECODING;
          } else {
            rxindex = WAITING;
          }
        } else {
          terminal.println("---Bad packet received with an incorrect destination of : " + rxpacket[DEST] + "---\n");
          clearpacket(rxpacket);
        }
        break;
        // Checks to see if the destination is valid, and if it's at the end of the packet.

      case DECODING:
        rxindex = WAITING;

        if (debugMode == true) {
          debugWindow.printlnInc(nullClear(rxpacket));
        }
        // Prints the packet to the debug terminal, if debug mode is set.

        if (myaddr == 0) {
          pendtable[rxpacket[SRC] - 'A'].setPacket(rxpacket);
          pendtable[rxpacket[SRC] - 'A'].setDelay(DELAY);
          pendtable[rxpacket[SRC] - 'A'].setPending(1);
          // Bounces packets round the ring if not logged in.
        } else {
          if (rxpacket[DEST] == myaddr) { // if dest is me
            if (checkchksum(rxpacket)) {
              if (rxpacket[SRC] == myaddr) { // if src is me
                switch (rxpacket[TYPE]) { // to me from me
                  case 'L':
                    if ((pendtable[myaddr - 'A'].getLoggedin() == -1)) {
                      pendtable[myaddr - 'A'].setPending(0);
                      txpacket = clearpacket(txpacket);
                      txpacket[DEST] = myaddr;
                      txpacket[SRC] = myaddr;
                      txpacket[TYPE] = 'A';
                      setchksum(txpacket);
                      sio.sendPacket(txpacket);
                      // Sets up an ACK packet if someone is trying to log in as you.
                      if (debugMode == true) {
                        debugWindow.printlnOut(new String(txpacket));
                      }
                    } else {
                      pendtable[myaddr - 'A'].setLoggedin(-1);
                      pendtable[myaddr - 'A'].setPending(0);
                      clearpacket(rxpacket);
                      canLogin = true;
                      // Logs you in and clears the packet.
                    }
                    break;
                  case 'R':
                    pendtable[myaddr - 'A'].setPending(0);
                    rxpacket = clearpacket(rxpacket);
                    break;
                  case 'D':
                    pendtable[myaddr - 'A'].setPending(0);
                    terminal.println("" + rxpacket[SRC] + ": " + new String(rxpacket).substring(4, 14) + "\n");
                    txpacket = clearpacket(txpacket);
                    txpacket[SRC] = myaddr;
                    txpacket[DEST] = rxpacket[SRC];
                    txpacket[TYPE] = 'A';
                    setchksum(txpacket);
                    // Displays the payload in the terminal, and sets up an ACK packet.
                    
                    sio.sendPacket(txpacket);
                    if (debugMode == true) {
                      debugWindow.printlnOut(new String(txpacket));
                    }
                    // Sends the ACK packet back to the sender.
                    rxpacket = clearpacket(rxpacket);
                    break;
                  case 'A':
                  case 'Y':
                    if ((pendtable[myaddr - 'A'].getLoggedin() == 0)) {
                      cantLogin = true;
                      pendtable[myaddr - 'A'].setPending(0);
                    }
                    rxpacket = clearpacket(rxpacket);
                    break;
                    // Stops the user from taking someone elses login ID.
                  case 'N':
                    rxpacket = clearpacket(rxpacket);
                    break;
                  case 'X':
                    if (logout == true) {
                      for (int i = 0; i < 26; i++) {
                        pendtable[i].setLoggedin(0);
                      }
                      myaddr = 0;
                    } else {
                      terminal.println("\n---Someone has obviously attempted to log you out---\n");
                    }
                    rxpacket = clearpacket(rxpacket);
                    break;
                    // Logs everyone out on our pendtable if a legitimate packet.
                    // Detects if someone is sending bad packets to stop unexpected logouts.
                  default:
                    terminal.println("---Error in packet type: " + rxpacket[TYPE] + "\nFrom: " + rxpacket[SRC] + "---\n");
                    rxpacket = clearpacket(rxpacket);
                    break;
                    // Displays the packet to the screen if it's a bad type.
                } // switch
              } else { // if dest is me, but src is someone else
                switch (rxpacket[TYPE]) { // to me from you
                  case 'R':
                    pendtable[rxpacket[SRC] - 'A'].setLoggedin(1);
                    terminal.println("---" + rxpacket[SRC] + " is already logged in---\n");
                    rxpacket = clearpacket(rxpacket);
                    break;
                    // Detects users that are logged in on the ring.
                  case 'D':
                    pendtable[rxpacket[SRC] - 'A'].setLoggedin(1);
                    terminal.println("" + rxpacket[SRC] + ": " + new String(rxpacket).substring(4, 14) + "\n");
                    txpacket = clearpacket(txpacket);
                    txpacket[SRC] = myaddr;
                    txpacket[DEST] = rxpacket[SRC];
                    txpacket[TYPE] = 'A';
                    setchksum(txpacket);
                    // Displays payload to the terminal and sets up an ACK packet.
                    
                    sio.sendPacket(txpacket);
                    if (debugMode == true) {
                      debugWindow.printlnOut(new String(txpacket));
                    }

                    rxpacket = clearpacket(rxpacket);
                    break;
                    // Sends an ACK to the sender and clears the packet.
                  case 'A':
                  case 'Y':
                    pendtable[rxpacket[SRC] - 'A'].setPending(0);
                    rxpacket = clearpacket(rxpacket);
                    break;
                    // Stops sending packets to the sender.
                  case 'N':
                    pendtable[rxpacket[SRC] - 'A'].setPending(5);
                    pendtable[rxpacket[SRC] - 'A'].setDelay(DELAY);
                    rxpacket = clearpacket(rxpacket);
                    break;
                    // Resends packets from the pendtable.
                  case 'L':
                  case 'X':
                  default:
                    terminal.println("---Error in packet type: " + rxpacket[TYPE] + "\nFrom: " + rxpacket[SRC] + "---\n");
                    rxpacket = clearpacket(rxpacket);
                    break;
                    // Displays the packet to the screen if it's a bad type.
                } // switch
              }
            } else {
              terminal.println("---A packet was just received with an incorrect check sum---\n");
              txpacket = clearpacket(txpacket);
              txpacket[DEST] = rxpacket[SRC];
              txpacket[SRC] = myaddr;
              txpacket[TYPE] = 'N';
              setchksum(txpacket);

              sio.sendPacket(txpacket);
              if (debugMode == true) {
                debugWindow.printlnOut(new String(txpacket));
              }

              rxpacket = clearpacket(rxpacket);
              // Sends a NAK packet to the sender if a bad checksum occurs.
            }
          } else { // if dest is not me
            if (rxpacket[SRC] == myaddr) { // if src is me
              switch (rxpacket[TYPE]) { // to you from me
                case 'L':
                  terminal.println("---Error with a Login packet---\n");
                  rxpacket = clearpacket(rxpacket);
                  break;
                case 'R':
                  terminal.println("---Someone has closed down without logging out---\n");
                  pendtable[rxpacket[SRC] - 'A'].setLoggedin(0);
                  rxpacket = clearpacket(rxpacket);
                  break;
                  // Displays to the terminal if someone hasn't logged out properly.
                case 'D':
                  terminal.println("---Sending Failed---\n");
                  rxpacket = clearpacket(rxpacket);
                  break;
                case 'A':
                case 'Y':
                  rxpacket = clearpacket(rxpacket);
                  break;
                case 'N':
                  rxpacket = clearpacket(rxpacket);
                  break;
                case 'X':
                default:
                  terminal.println("---Error in packet type: " + rxpacket[TYPE] + "\nFrom: " + rxpacket[SRC] + "---\n");
                  rxpacket = clearpacket(rxpacket);
                  break;
                  // Displays the packet to the screen if it's a bad type.
              } // switch
            } else { // if dest and src is not me
              switch (rxpacket[TYPE]) { // to you from you
                case 'L':
                  sio.sendPacket(rxpacket);
                  if (debugMode == true) {
                    debugWindow.printlnOut(new String(rxpacket));
                  }
                  // Passes on the login packet.
                  
                  if (myaddr != 0 && pendtable[myaddr - 'A'].getLoggedin() == -1) {
                    terminal.println("---" + rxpacket[SRC] + " has logged in---\n");
                    clearpacket(txpacket);
                    txpacket[DEST] = rxpacket[SRC];
                    txpacket[SRC] = myaddr;
                    txpacket[TYPE] = 'R';
                    setchksum(txpacket);
                    pendtable[txpacket[SRC] - 'A'].setPacket(txpacket);
                    pendtable[txpacket[SRC] - 'A'].setDelay(DELAY);
                    pendtable[txpacket[DEST] - 'A'].setLoggedin(1);
                    pendtable[txpacket[SRC] - 'A'].setPending(1);
                    clearpacket(rxpacket);
                  }
                  break;
                  // Sets up an R packet, and transmits to the sender.
                case 'R':
                  sio.sendPacket(rxpacket);
                  if (debugMode == true) {
                    debugWindow.printlnOut(new String(rxpacket));
                  }
                  break;
                case 'D':
                  sio.sendPacket(rxpacket);
                  if (debugMode == true) {
                    debugWindow.printlnOut(new String(rxpacket));
                  }
                  break;
                case 'A':
                case 'Y':
                  pendtable[rxpacket[SRC] - 'A'].setLoggedin(1);
                  sio.sendPacket(rxpacket);
                  if (debugMode == true) {
                    debugWindow.printlnOut(new String(rxpacket));
                  }
                  break;
                case 'N':
                  sio.sendPacket(rxpacket);
                  if (debugMode == true) {
                    debugWindow.printlnOut(new String(rxpacket));
                  }
                  break;
                case 'X':
                  terminal.println("---" + rxpacket[SRC] + " has logged out---\n");
                  pendtable[rxpacket[SRC] - 'A'].setLoggedin(0);
                  sio.sendPacket(rxpacket);
                  if (debugMode == true) {
                    debugWindow.printlnOut(new String(rxpacket));
                  }
                  break;
                  // Sets the sender to logged out in the pendtable, and passes on the packet.
                default:
                  terminal.println("---Error in packet type: " + rxpacket[TYPE] + "\nFrom: " + rxpacket[SRC] + "---\n");
                  rxpacket = clearpacket(rxpacket);
                  break;
                  // Displays the packet to the screen if it's a bad type.
              } // switch
            }
          }
        }
      default:
        break;
    } // end switch(rxindex)

  } // receiver()

  /**
   * Function - Transmits anything in the pendtable that is waiting to be transmitted.
   */
  private static void transmitter() {
    if (++pendindex > ('Z' - 'A')) { // increment index and check limit
      pendindex = 0;
    }
    if (pendtable[pendindex].getPending() > 0) { // check if message to send
      if (pendtable[pendindex].getDelay() == DELAY) { // start of countdown?
        // actually send the packet on this attempt
        sio.sendPacket(pendtable[pendindex].getPacket());
        if (debugMode == true) {
          debugWindow.printlnOut(nullClear(pendtable[pendindex].getPacket()));
        }
      }
      pendtable[pendindex].decDelay();
      if (pendtable[pendindex].getDelay() == 0) { // countdown finished
        pendtable[pendindex].decPending(); // any attempts left?
        if (pendtable[pendindex].getPending() > 0) {
          pendtable[pendindex].setDelay(DELAY); // re-set countdown
        }
      } // if(end of a try)
    } // if(attempts to do)
  } // transmitter()

  /**
* @param name - The name(s) of the authors.
* Function - Displays a banner for the user at the start of the program.
*/
  private static void welcome(String name) {
    terminal.println("\t -------== Welcome to " + name + "'s Serial Ring LAN ==-------\n");
    terminal.println(" Authors: Matt McCouaig and Andrew Nutt.");
    terminal.println(" Date: 09/02/2012");
    terminal.println(" Current Revision: v1.9");
    terminal.println(" Revision Summary: Ironed out all the bugs. Errors respond properly.");
    terminal.println(" Revision Date: 26/03/2012\n");
    terminal.println(" Functional Description:\n\t"
            + "- List all users logged in\n\t"
            + "- Choose what user to send a message to\n\t"
            + "- Input your message\n\t"
            + "- Send the message as a packet\n\t"
            + "- Clear the packet\n\t"
            + "- Alter between displaying packets on the 'Debug Window'\n\t"
            + "- Logout\n");
    terminal.println(" User Advice: To understand the program as it runs, read all of the outputted");
    terminal.println(" information fully as it will inform you how the program works at each stage.\n");
    terminal.println("----------------------------------------------------------------------------------------------------------------\n");
  } // welcome()

  /**
* @param packet - Packet passed in to be cleared.
* @return p - Cleared packet.
* Function - Clears a specific packet that has been passed in by overwriting with a cleared packet.
*/
  public static char[] clearpacket(char[] packet) {
    char[] p = new char[PACKETSIZE];

    for (int i = 0; i < packet.length; i++) { // clear to spaces
      packet[i] = ' ';
      p[i] = ' ';
    } // for(clearing packets)

    // Set SOT EOT
    p[SOM] = packet[SOM] = '{';
    p[EOM] = packet[EOM] = '}';
    return p;
  } // clearpacket()

  /**
* @param packet - Initial packet passed in.
* Function - To place a correct checksum char in place [14] in the packet.
*/
  public static void setchksum(char[] packet) { // sets chksum on outgoing messages
    int chksum = 0;

    for (int i = 0; i < 16; i++) {
      if (i != CHKSM) {
        chksum += packet[i];
      }
    }

    packet[CHKSM] = (char) (byte) ~(chksum % 128);
  } // setchksum()

  /**
* @param packet - Packet passed in after being received.
* @return boolean - True/False based on checksum char check.
* Function - Checks the checksum char received with one worked out to see if corruption occurred or not.
*/
  public static boolean checkchksum(char[] packet) { // checks cksum on incoming messages
    int chksum = 0;
    byte rxChksumActual = (byte) packet[CHKSM];

    for (int i = 0; i < 16; i++) {
      if (i != CHKSM) {
        chksum += packet[i];
      }
    }

    byte rxChksumExpected = (byte) ~(chksum % 128);
    if (rxChksumActual == rxChksumExpected) {
      return true;
    } else {
      return false;
    }
  } // checkchksum()

  /**
* @param packet - Received packet is passed into this parameter.
* @return payload - Returns the received packet displaying it correctly.
* Function - Returns a packet with spaces in rather than null characters to display to the
* screen correctly to enable functionality with other programming languages.
*/
  private static String nullClear(char[] packet) {
    StringBuilder debugPacket = new StringBuilder();
    for (int i = 0; i < 16; i++) {
      if (i != 14) {
        char c = packet[i];
        if (c == 0) {
          debugPacket.append(' ');
        } else {
          debugPacket.append(c);
        }
      } else {
        debugPacket.append(packet[i]);
      }
    }
    return debugPacket.toString();
  } // nullClear()
} // JavaLan