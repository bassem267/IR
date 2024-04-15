package Index;

import java.util.ArrayList;

public class Posting {
    public Posting next = null;
    public int docId;
    public int dtf = 1; //collection frequency

    public Double TF;
    public int DSize ;
    public String term;

    ArrayList<Integer> positions=new ArrayList<>();
    ArrayList<Integer> positions2=new ArrayList<>();


    public Posting(int docId,int DSize){
        this.docId=docId;
        this.DSize= DSize;
    }

    public Posting(int docId,ArrayList<Integer> pp){
        this.docId=docId;
        this.positions2= pp;
    }

    public Double getTf(){
        TF=  ((double)dtf / (double)DSize);
        return TF;
    }
}


