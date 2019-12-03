package model;

public class mediaFile{

    public int mediaId;
    public String titulo;
    public String interprete;
    public String ano;
    public String[] tags;

    public mediaFile(int idIn, String tituloIn, String interpreteIn, String anoIn,String[] tagsIn){
        this.mediaId=idIn;
        this.titulo=tituloIn;
        this.interprete=interpreteIn;
        this.ano=anoIn;
        this.tags=tagsIn;
    }
}