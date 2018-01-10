import java.io.*;
import java.util.StringTokenizer;

/**
 * Created by emad on 11.12.16.
 */
class InputReader{
    StringTokenizer tokenizer;
    BufferedReader reader;
    String skipLineChar;

    public InputReader(InputStream stream,String skipChar){
        reader = new BufferedReader(new InputStreamReader(stream));
        tokenizer = null;
        skipLineChar = skipChar;
    }

    public InputReader(InputStream stream){
        reader = new BufferedReader(new InputStreamReader(stream));
        tokenizer = null;
    }

    public InputReader(){

        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public String readLine(){
        try{
            return reader.readLine();
        }
        catch (IOException error){
            throw new RuntimeException(error);
        }
    }


    String next(){
        String line;
        while(tokenizer == null || !tokenizer.hasMoreTokens()) {
            try{
                if(skipLineChar==null)
                    tokenizer = new StringTokenizer(reader.readLine());
                else{
                    line= reader.readLine();
                    while(line.startsWith(skipLineChar))
                        line=reader.readLine();
                    tokenizer = new StringTokenizer(line);
                }
            }
            catch (IOException error){
                throw new RuntimeException(error);
            }

        }
        return tokenizer.nextToken();
    }


    public int nextInt(){
        return Integer.parseInt(next());
    }
    public long nextLong() {
        return Long.parseLong(next());
    }
    public double nextDouble(){
        return Double.parseDouble(next());
    }
}


