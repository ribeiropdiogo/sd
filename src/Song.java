import java.util.List;

public class Song {
    private int id, year, downloads;
    private String name, artist;
    // private List<> etiquetas; <- não sei bem o que são as etiquetas (categorias?)


    public Song(int id, int year, int downloads, String name, String artist) {
        this.id = id;
        this.year = year;
        this.downloads = downloads;
        this.name = name;
        this.artist = artist;
    }
}
