
/**
 * Created by emad on 11.12.16.
 */
public class Neuron implements Comparable<Neuron> {
        private double response;
        int id;
        double [] center;

        //the vector X-C
        double [] diff;

        Neuron(double resp, double [] cent){
            response = resp;
            center = cent;
            diff = new double[cent.length];
        }

        public void updateCenter(double delta){
            for(int i=0;i<center.length;i++) {
                center[i] += delta * (diff[i]);
                //System.out.print(center[i]+" ");
            }
            //System.out.println();
        }

        public void setResponse(double resp){
            response = resp;
        }

        public double getResponse(){return response;}

        @Override
        public int compareTo(Neuron other) {
            if(this.response > other.response)
                return 1;

            if(this.response < other.response)
                return -1;
            return 0;
        }
    }
