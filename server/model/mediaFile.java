package model;

public class mediaFile {

    private int mediaId;
    private String titulo;
    private String interprete;
    private String ano;
    private String[] tags;
    private int Ndownloads;

    public mediaFile(int idIn, String tituloIn, String interpreteIn, String anoIn, String[] tagsIn) {
        this.mediaId = idIn;
        this.titulo = tituloIn;
        this.interprete = interpreteIn;
        this.ano = anoIn;
        this.tags = tagsIn;
    }

    public String getFileTitle() {
        return this.titulo;
    }

    public int getId() {
        return this.mediaId;
    }

    public int getNDownloads() {
        return this.Ndownloads;
    }

    public boolean containsTag(String tagIn) {
        for (int i = 0; i < this.tags.length; i++) {
            if (this.tags[i].equals(tagIn)) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(Integer.toString(this.mediaId) + " ");
        builder.append(this.titulo + " ");
        builder.append(this.interprete + " ");
        builder.append(this.ano + " ");

        for (int i = 0; i < this.tags.length; i++) {
            builder.append(this.tags[i]+" ");
        }

        return builder.toString();
    }

    public void incrementDownloads() {
        this.Ndownloads = this.Ndownloads++;
    }

}