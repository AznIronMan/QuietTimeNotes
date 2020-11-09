/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csu.csci325;

/**
 *
 * @author admingec
 */
class Menu {
    private String book;
    private String verse;
    
    public Menu(String book, String verse) {
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
    
    @Override
    public String toString() {
        return book + " " + verse;
        
    }
    
}
