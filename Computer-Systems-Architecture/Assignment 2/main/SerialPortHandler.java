package main;

import java.io.*;
import java.util.Enumeration;

import javax.comm.CommPortIdentifier;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.UnsupportedCommOperationException;

public class SerialPortHandler {

  public static final String COM1 = "COM1", COM2 = "COM2";
  private static Enumeration portList;
  private static CommPortIdentifier portID;
  private static SerialPort serialPort = null;
  private static OutputStream outputStream;
  private static InputStream inputStream;

  /**
   * @author Matt McCouaig + Andy Nutt
   * @param comStr - String holding the name of the COM port that is to be used.
   * Function - Finds and lists all ports, and allows use of them.
   */
  public SerialPortHandler(String comStr) {
    JavaLAN.terminal.println("\nChecking the availability of ports...");
    portList = CommPortIdentifier.getPortIdentifiers();
    // Obtains the ID's of the ports on the computer.

    while (portList.hasMoreElements()) {
      portID = (CommPortIdentifier) portList.nextElement();
      JavaLAN.terminal.print("\n" + portID.getName());
      // Displays all ports on the computer.
      if (portID.getPortType() == CommPortIdentifier.PORT_SERIAL) {
        if (portID.getName().equals(comStr)) {
          // Checks if the requested port exists.
          try {
            serialPort = (SerialPort) portID.open("JavaLAN", 1000);
            serialPort.setSerialPortParams(9600,
                    SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            serialPort.enableReceiveThreshold(0);
            serialPort.setRTS(true);
            serialPort.setDTR(true);
            // Tests to open the port requested, and set the properties for it. 

            outputStream = serialPort.getOutputStream();
            inputStream = serialPort.getInputStream();
            // Enables input and output from the port.

            JavaLAN.terminal.print(" <-- This port has been enabled");
          }// try
          catch (IOException e) {
          } catch (UnsupportedCommOperationException e) {
            JavaLAN.terminal.println("Unsupported use...\n");
            System.exit(0);
          } catch (PortInUseException e) {
            JavaLAN.terminal.println("Port in use!\n");
            System.exit(0);
          }
          // Deals with the exceptions to avoid the program crashing.
        } // if
      }// if
    }// while
    JavaLAN.terminal.println("\n\nFinished checking!\n");
    JavaLAN.terminal.println("----------------------------------------------------------------------------------------------------------------\n");
    if (serialPort == null) {
      JavaLAN.terminal.println("\nNo " + comStr + " port found.");
      System.exit(0);
    }
  }// constructor()

  /**
   * @return b - Character obtained from the COM port.
   * @exception IOException - Error caused by inability to read the character from the COM port.
   * Function - Reads a character from the COM port and returns it through the variable b.
   */
  public char getChar() {
    char b = (char) -1;
    try {
      b = (char) inputStream.read();
    }// try
    catch (IOException e) {
    }
    return b;
  }// getchar()

  /**
   * @param b - Character to be transmitted.
   * @exception IOException - Error caused by inability to transmit character down the COM port.
   * Function - Writes a character to the COM port to transmit it.
   */
  public void putChar(char b) {
    try {
      outputStream.write(b);
    } catch (IOException e) {
    }
  } // putChar()

  /**
   * @param packet - Packet passed in to be transmitted.
   * @exception IOException - Error caused by inability to send a packet without crashing the program.
   * Function - Sends the packet down the COM port each character at a time.
   */
  public int sendPacket(char[] packet) {
    try {
      for (int i = 0; i < packet.length; i++) {
        outputStream.write(packet[i]);
      }
    } catch (IOException e) {
    }
    return 0;
  }// sendPacket()
}// SerialPortHandler