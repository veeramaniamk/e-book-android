package com.saveetha.e_book;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLConnection;




public class Read_PDF_From_URL {

  public static void main(String[] args) throws IOException {

    URL url1 =
      new URL("http://www.gnostice.com/downloads/Gnostice_PathQuest.pdf");

    byte[] ba1 = new byte[1024];
    int baLength;
    FileOutputStream fos1 = new FileOutputStream("download.pdf");

    try {
      // Contacting the URL
      System.out.print("Connecting to " + url1.toString() + " ... ");
      URLConnection urlConn = url1.openConnection();

      // Checking whether the URL contains a PDF
      if (!urlConn.getContentType().equalsIgnoreCase("application/pdf")) {
          System.out.println("FAILED.\n[Sorry. This is not a PDF.]");
      } else {
        try {

          // Read the PDF from the URL and save to a local file
          InputStream is1 = url1.openStream();
          while ((baLength = is1.read(ba1)) != -1) {
              fos1.write(ba1, 0, baLength);
          }
          fos1.flush();
          fos1.close();
          is1.close();

          // Load the PDF document and display its page count


        } catch (ConnectException ce) {
          System.out.println("FAILED.\n[" + ce.getMessage() + "]\n");
        }
      }

    } catch (NullPointerException npe) {
      System.out.println("FAILED.\n[" + npe.getMessage() + "]\n");
    }
  }
}