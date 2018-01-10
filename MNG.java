import java.io.*;
import java.util.*;

/*
 @author: Emad Bahrami
 @email: emadbahramirad@gmail.com

 Implementation of the Multi-Neural-Gas
    K: total number of neurons
    N: input dimension
    M: number of partner net(gas)
    P: number of input patterns
    t_max: total number of learning steps
    eta_init: initial learning rate
    eta_end:  final learning rate

    eta(t+1) = eta_init * (eta_end/eta_init)^(t/t_max)

 */

public class MNG {

    //total number of neurons
    int K;

    //neurons in each partner net = K/M
    int k_per_m;

    //Dimension of input
    int N;

    //number of patterns
    int P;

    //number of partner layer
    int M;

    //a list of partner nets
    ArrayList<Neuron []> multi_gas;

    int t_max;

    double eta_init;
    double eta_end;
    
    //size of neighborhood function, s
    double size;
    
    
    ArrayList<double []> patterns;

    /*
    **************************************************************************************************
    * Helper functions
     */
    public void printArray(double [] X,String msg){
        System.out.print(msg+"\t");
        for (int i=0;i<X.length;i++)
            System.out.print(X[i]+" ");
        System.out.println();

    }

    /* Computing the gaussian function
         * @input d: the computed distance between inputs and centers
         * , s: the width
         *
         * @output the r_k vector consist of output of each neuron
         * 	in RBF layer
         */
    public double  computeGaussian(double d,double width){
        double result = d/(2*Math.pow(width,2));
        return Math.exp(-result);
    }

    public void swap(double [] x,int i,int j){
        double temp = x[i];
        x[i]=x[j];
        x[j]=temp;
    }

    //**************************************************************************************************


    /*
     @input: input pattern X
     @output: the index of winner net
     */
    public int response(double [] X) {

        double distance=0;
        Neuron [] neurons;
        double min_dist=Double.MAX_VALUE;
        int min_Net=0;

        for(int m=0 ; m<M ; m++) {
            neurons = multi_gas.get(m);

            for (int k = 0; k < k_per_m; k++) {
                distance = 0;
                for (int n = 0; n < N; n++) {
                    //x_n - c_n
                    neurons[k].diff[n] = X[n] - neurons[k].center[n];
                    distance += Math.pow(neurons[k].diff[n], 2);
                }
                distance = Math.sqrt(distance);
                if(min_dist>distance){
                    min_dist = distance;
                    min_Net = m;
                }
                neurons[k].setResponse(distance);
            }
        }

        return min_Net;
    }


    public void updateCenters(int t,int winner_net){

        Neuron [] neurons=multi_gas.get(winner_net);
        Arrays.sort(neurons);
        double delta_c;

        // eta = eta*(eta_end/eta_init)^(t/t_max)
        double eta=eta_init*Math.pow(eta_end/eta_init,(double) t/t_max);

        int dist_l=1;
        int winner=0;

        

        for(int k=0;k<k_per_m;k++){
            dist_l = Math.abs(k-winner);
            delta_c = eta*computeGaussian(dist_l,size);

            neurons[k].updateCenter(delta_c);
        }
    }


    public void run(InputReader in,int pat) throws IOException {

	P=0;
        init();
	P = pat;
        readPatterns(in);

        /* Setting centers randomly from training data
	    * Input Data Driven: Subset of training data
	    * @input X: the input patterns(training points)
	    */
        Random rand = new Random();
        double center[];
        Neuron [] neurons;
        for (int m = 0; m < M; m++){
            neurons = new Neuron[k_per_m];
            for (int k = 0; k < k_per_m; k++) {
                center = patterns.get(rand.nextInt(patterns.size())).clone();
                neurons[k] = new Neuron(0, center);
            }
            multi_gas.add(neurons);
        }

        int winner_net;
        for (int t = 0; t < t_max; t++){
            for (int p = 0; p < P; p++){
                winner_net = response(patterns.get(p));
                updateCenters(t,winner_net);
            }
            Collections.shuffle(patterns,new Random());
        }



        File net = new File("PA-D.net");
        FileWriter fileWriter = new FileWriter(net,false);

        //writing the learnt centers to the file
        String out="";
        for(int m=0;m < M; m++) {
            neurons = multi_gas.get(m);
            for (int k = 0; k < k_per_m; k++) {
                out = "";
                for (int n = 0; n < N; n++) {
                    out += String.format("%1$,.5f\t", neurons[k].center[n]);
                }
                out += "\n";
                fileWriter.write(out);
                //System.out.print(out);
            }
        }
      fileWriter.close();

    }

    public void readPatterns(InputReader in){
        //P = in.nextInt();
        double [] input = new double[N];

        for(int i=0;i<P;i++){
            input = new double[N];
            for(int j=0;j<N;j++) {
                input[j] = in.nextDouble();
            }
            patterns.add(i,input);
        }
    }

    public void init(){

        //total number of patterns
        if(P==0)
            P = 300;

        //total number of neurons
        K = 12;

        //input dimension
        N = 2;

        //number of partner net
        M = 4;

        //number of learning steps
        t_max = 500;

        //final learning rate
        eta_end = 0.02;

        //initial learning rate
        eta_init = 0.5;
	
    	//size of the neighborhood function, s
	size = 0.4;
        
        //number of neurons per gas
        k_per_m = K/M;


	//list of input patterns
        patterns = new ArrayList<double[]>();

        //list of partner nets (M nets)
        multi_gas = new ArrayList<Neuron[]>();
    }


    public static void main(String[] args) throws IOException {
        MNG mng=new MNG();
	if(args.length<1 || args[0].isEmpty())
			throw new FileNotFoundException("File not found!\nPlease check the file name and the path to the input file");
	Scanner scan = new Scanner(System.in);
	System.out.println("Please enter the number of input patterns: ");
	int p = scan.nextInt();

        InputStream inputStream;
        try {
			inputStream = new FileInputStream(args[0]);
	}
	catch (IOException e) {
		throw new RuntimeException(e);
	}
	
	InputReader in = new InputReader(inputStream);
        mng.run(in,p);
    }

}

