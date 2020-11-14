package csu.csci325;

/**
 * 
 * @author  Geoff Clark
 * @e-mail  gclark82@gmail.com
 * @class   CSU.CSCI325
 * 
 */

class Note {
    private String date;
    private String book;
    private String verse;
    private String note;
    private String defaultnote;
    
    public Note(String defaultnote, String date, String book, String verse, 
            String note) {
        
        this.defaultnote = defaultnote;
        this.date = date;
        this.book = book;
        this.verse = verse;
        this.note = note;
    }
   
    public String getDate() {
        return date;
    }
    
    public void setDate(String date) {
        this.date = date;
    }
    
    public String getBook() {
        return book;
    }
    
    public void setBook(String book) {
        this.book = book;
    }
    
    public String getVerse() {
        return verse;
    }
    
    public void setVerse(String verse) {
        this.verse = verse;
    }
    
    public String getNote() {
        return note;
    }
    
    public void setNote(String date) {
        this.note = note;
    }
    
    public String getDefaultnote() {
        return defaultnote;
    }
    
    public void setDefaultnote(String defaultnote) {
        this.defaultnote = defaultnote;
    }

    @Override
    public String toString() {
        String importedNote = date + "," + book + "," + verse + "," + note;
        if(importedNote.equals(defaultnote)) {
            return "Create a new note below";
                    }
        return "Last Edit Date:  " + date + "\n" + "\n" + "Verse: " + book + 
                " " + verse + "\n" + "\n" + note;
    }
}
