package csu.csci325;

/**
 * 
 * @author  Geoff Clark
 * @e-mail  gclark82@gmail.com
 * @class   CSU.CSCI325
 * 
 */

class Remove {
    private String date;
    private String book;
    private String verse;
    private String note;
    
    public Remove(String date, String book, String verse, String note) {
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

    @Override
    public String toString() {
        return date + "," + book + "," + verse + "," + note;
    }
    
}
