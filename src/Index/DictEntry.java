package Index;

import java.util.ArrayList;

public class DictEntry {
    int doc_freq = 1; // number of documents that contain the term
    int term_freq = 1; //number of times the term is mentioned in the collection Posting pList = null;

    Double IDF; //doesn't really matter with single term queries
    Posting pList;


    public DictEntry(Posting head ){
        this.pList=head;
    }


    public void print(){
        Index.Posting temp = pList;

        while(temp!= null){
            System.out.print(temp.docId + " , --> positions: ( "+ temp.positions +" ) ,");
            temp=temp.next;
        }
        System.out.println(" )" );

    }

    public Posting getPostingID(String id){
        Posting temp = pList;
        while(temp!= null){
            if( String.valueOf(temp.docId ).equals(id)){
                return temp;
            }
            //System.out.print(temp.docId + " ,");
            temp=temp.next;
        }
        return null;

    }

    public ArrayList<Posting> getAllPosting(){
        Posting temp = pList;
        ArrayList<Posting> all = new ArrayList<>();
        while(temp!= null){
            all.add(temp);
            //System.out.print(temp.docId + " ,");
            temp=temp.next;
        }
        return all;
    }
}

