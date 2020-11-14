package com.clarktribe.qtn;

/**
 * 
 * @author  Geoff Clark
 * @e-mail  gclark82@gmail.com
 * 
 */

class Menu {
    private String book;
    private String verse;
    private String defaultmenu;
    
    public Menu(String defaultmenu, String book, String verse) {
        this.defaultmenu = defaultmenu;
        this.book = book;
        this.verse = verse;
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
    
    public String getDefaultmenu() {
        return defaultmenu;
    }
    
    public void setDefaultmenu(String defaultmenu) {
        this.defaultmenu = defaultmenu;
    }
    
    @Override
    public String toString() {
        String finalMenu = (book + " " + verse);
        if(finalMenu.equals(defaultmenu)) {
            return "[Create a new note]";
        } else {
            return finalMenu;
        }
    }
    
}
