package constraint.com.rxjava;

/**
 * Created by Innovify on 19/7/18.
 */
public class Notes {
    int id;
    private String note;

    public Notes(int id, String note) {
        this.id = id;
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
