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
        this.Ndownloads=0;
    }

    public String getFileTitle() {
        return this.titulo;
    }
    public String getFileArtist() {
        return this.interprete;
    }

    public int getId() {
        return this.mediaId;
    }

    public int getNDownloads() {
        return this.Ndownloads;
    }

    public void addDownload() {
        this.Ndownloads=this.Ndownloads+1;
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

        builder.append("Id: "+Integer.toString(this.mediaId) + " ");
        builder.append("Titulo: "+this.titulo + " ");
        builder.append("Interprete: "+this.interprete + " ");
        builder.append("Ano: "+this.ano + " ");
        builder.append("Etiquetas: ");
        for (int i = 0; i < this.tags.length; i++) {
            builder.append(this.tags[i]+" ");
        }
        builder.append("Numero de downloads: "+this.Ndownloads);

        return builder.toString();
    }

    public void incrementDownloads() {
        this.Ndownloads = this.Ndownloads++;
    }

}