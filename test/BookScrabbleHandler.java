package test;

import java.io.PrintWriter;
import java.util.Scanner;
import java.io.InputStream;
import java.io.OutputStream;

public class BookScrabbleHandler implements ClientHandler{
   private DictionaryManager dm;
    private PrintWriter out;
    private Scanner in;

    @Override
    public void handleClient(InputStream inFromClient, OutputStream outToClient)
    {
        dm = DictionaryManager.get();
        out = new PrintWriter(outToClient);
        in = new Scanner(inFromClient);

        boolean flag = false;
        String[] input = in.next().split(",");

        String copyArray [] = new String[input.length-1];
        for(int i =0, s=1; s<input.length; i++ , s++){
            copyArray[i] = input[s];
        }
        if(input.equals('Q'))
        {
            flag = dm.query(copyArray);
        }
        else
        {
             flag = dm.challenge(copyArray);
        }

        out.println(flag);
        out.flush();
    }

    @Override
    public void close()
    {
        out.close();
        in.close();
    }
}

