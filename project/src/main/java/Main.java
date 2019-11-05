import org.lwjgl.opengl.GPU_DEVICE;
import sun.reflect.annotation.ExceptionProxy;

import java.io.*;
import java.util.Scanner;

public class Main {

        Scanner in;
        PrintWriter out;

        public void run(){

            try{
                in = new Scanner(new BufferedReader((new FileReader("input.txt"))));
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));

            }catch(Exception e){
                out.close();
                e.printStackTrace();
                System.exit(1)
            }
        }




}
