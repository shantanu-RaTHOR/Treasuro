package com.example.treasuro;

public class partiinfo
{
    String name,id;
    int score,qno, attmpt;
   public partiinfo(){}
   public partiinfo(String name,String id,int score,int qno,int attmpt)
    {
       this.name=name;
       this.id=id;
       this.score=score;
       this.qno=qno;
       this.attmpt=attmpt;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public int getScore() {
        return score;
    }

    public int getQno() {
        return qno;
    }

    public int getAttmpt() {
        return attmpt;
    }
}
