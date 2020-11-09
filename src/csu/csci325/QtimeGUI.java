package csu.csci325;

import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.text.Position;

/**
 * @author Geoff Clark
 * @e-mail gclark82@gmail.com
 * @class CSU.CSCI325
 */
public final class QtimeGUI extends javax.swing.JFrame {

    String loadPath = "notes.txt";
    String bookPath = "books.txt";
    String tempPath = "temp.txt";
         
    public QtimeGUI() throws IOException {
        File filename = new File(loadPath);
        File bookFile = new File(bookPath);
        buildBooks(bookFile);
        fileCheck(filename);
        initComponents();
        fillbookDrop();
        loadMenu();
    }
    
    public void loadNotes(int index) throws IOException {
        File filename = new File(loadPath);
        boolean exists = filename.exists();
        if (exists == false) {
            fileCheck(filename);
            brandNew();
        } else {
            List<Note> notes = readfromNotes(loadPath);
            notes.clear();
            notes = readfromNotes(loadPath);
            String o1 = (notes.get(index).toString());
            String o2 = o1.replaceAll("\\*", "\n");
            String o3 = o2.replaceAll("\\^", ",");
            String outputText = o3;
            existNote.setText(outputText);
            }
    }
   
    public void loadMenu() throws IOException {
        File filename = new File(loadPath);
        boolean exists = filename.exists();
        if (exists == false) {
            fileCheck(filename);
            brandNew();
        } else {
        List<Menu> menus = readfromMenus(loadPath);
        menus.clear();
        menus = readfromMenus(loadPath);
        DefaultListModel dlm = new DefaultListModel();
        dlm.removeAllElements();
        dlm.clear();
        for (Menu m : menus) {
            dlm.addElement(m);
            existingList.setModel(dlm);
        }
        }        
    }
    
    private List<Note> readfromNotes(String filename) {
        List<Note> notes = new ArrayList<>();
        Path fileLoad = Paths.get(filename);
        try (BufferedReader brLoad = Files.newBufferedReader(fileLoad,
                StandardCharsets.US_ASCII)){
            String line = brLoad.readLine();
            while (line != null) {
                String[] attributes = line.split(",");
                Note noteLoad = createNote(attributes);
                notes.add(noteLoad);
                line = brLoad.readLine();
            }
        } catch (IOException ioe) {
            errorBox.setText(ioe.toString());
        }
        return notes;
    }

    private static Note createNote(String[] metadata) {
        String date = metadata[0];
        String book = metadata[1];
        String verse = metadata[2];
        String note = metadata[3];
        return new Note(date, book, verse, note);
    }    
    
    private List<Remove> readfromRemoves(String filename) {
        List<Remove> removes = new ArrayList<>();
        Path fileLoad = Paths.get(filename);
        try (BufferedReader brLoad = Files.newBufferedReader(fileLoad,
                StandardCharsets.US_ASCII)){
            String line = brLoad.readLine();
            while (line != null) {
                String[] attributes = line.split(",");
                Remove removeLoad = createRemove(attributes);
                removes.add(removeLoad);
                line = brLoad.readLine();
            }
        } catch (IOException ioe) {
            errorBox.setText(ioe.toString());
        }
        return removes;
    }    
    
    private static Remove createRemove(String[] metadata) {
        String date = metadata[0];
        String book = metadata[1];
        String verse = metadata[2];
        String note = metadata[3];
        return new Remove(date, book, verse, note);
    }
    
    private List<Menu> readfromMenus(String filename) {
        
        List<Menu> menus = new ArrayList<>();
        Path fileLoad = Paths.get(filename);
        try (BufferedReader br2Load = Files.newBufferedReader(fileLoad,
                StandardCharsets.US_ASCII)){
            String line = br2Load.readLine();
            while (line != null) {
                String[] attributes = line.split(",");
                Menu menuLoad = createMenu(attributes);
                menus.add(menuLoad);
                line = br2Load.readLine();
            }
        } catch (IOException ioe) {
            errorBox.setText(ioe.toString());
        }
        return menus;
    }
    
    private static Menu createMenu(String[] metadata) {
        String book = metadata[1];
        String verse = metadata[2];
        return new Menu(book, verse);
    }
    
    public void checkVerse() throws IOException {
        File filePath = new File(loadPath);
        if(filePath.length() > 0) {        
        String newBook = (String) bookDrop.getSelectedItem();
        String newVerse = (chapInput.getText() + ":" + verseInput.getText());
        String toCheck = (newBook + " " + newVerse);
        int index = existingList.getNextMatch(toCheck, 0, Position.Bias.Forward);
        if (index != -1) {
            addtoVerse(index);
        } else {
            collectionTime();
        }
        } else {
            collectionTime();
        }
    }
    
    public void addtoVerse(int index) throws IOException {
        List<Remove> verse = readfromRemoves(loadPath);
        verse.clear();
        verse = readfromRemoves(loadPath);
        String verseFound = (verse.get(index).toString());
        System.out.print(verseFound);
        List<Remove> check = readfromRemoves(loadPath);
        check.clear();
        check = readfromRemoves(loadPath);
        String linetoCheck = (verseFound);
	String linetoSave = (check.get(index).toString());
        try {
            File inputFile = new File(loadPath);
            File outputFile = new File(tempPath);
            try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                    BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    if (!line.equals(linetoCheck)) {
                        System.out.println("N");
                        writer.write(line);
                        writer.newLine();
                    }
                }
            }
            if (inputFile.delete()) {
                if(!outputFile.renameTo(inputFile)) {
                    throw new IOException("");
                }
            }
        } catch (IOException ex) {
            errorBox.setText(ex.toString());
        }
        String tobeParsed = linetoSave;
        String[] parsed = tobeParsed.split(",");
        String oldBook = parsed[1];
        String oldVerse = parsed[2];
        String oldNote = parsed[3];
        String p = "MM-dd-yyyy HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(p);
        String d = sdf.format(new Date());
        String n1 = (newNote.getText()).replaceAll("\\r\\n|\\r|\\n", "*");
        String n2 = n1.replaceAll(",", "^");
        String n = n2;
        String collection = (d + "," + oldBook + "," + oldVerse + "," + oldNote 
                + "*" + n + "\n");
        check.clear();
        writeNote(collection);
    }
   
    public void fillbookDrop() {
        File file = new File(bookPath);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            Object[] lines = br.lines().toArray();
            for (Object line1 : lines) {
                String line = line1.toString();
                bookDrop.addItem(line);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(QtimeGUI.class.getName()).log(Level.SEVERE, 
                    null, ex);
            errorBox.setText(ex.toString());
        }
    }
    
    public void turnItOn() {
        bookDrop.setEnabled(true);
        chapInput.setEnabled(true);
        verseInput.setEnabled(true);
        resetButton.setEnabled(true);
        newNote.setEnabled(true);
        newNote.setEditable(true);
        submitButton.setEnabled(true);
        clearButton.setEnabled(true);
    }
    
    public void turnItOff() {
        bookDrop.setEnabled(false);
        chapInput.setEnabled(false);
        verseInput.setEnabled(false);
        resetButton.setEnabled(false);
        newNote.setEnabled(false);
        newNote.setEditable(false);
        submitButton.setEnabled(false);
        clearButton.setEnabled(false);
        existNote.setText("");
        existNote.setEnabled(false);
    }
    
    public void brandNew() {
        bookDrop.setEnabled(true);
        chapInput.setEnabled(true);
        verseInput.setEnabled(true);
        resetButton.setEnabled(true);
        existNote.setText("");
        existNote.setEnabled(false);
        existingList.setEnabled(false);
        deleteButton.setEnabled(false);
    }
    
    public void clearBooks() {
        bookDrop.setSelectedIndex(0);
        chapInput.setText("");
        verseInput.setText("");
    }
    
    public void clearNotes() {
        newNote.setText("");
    }
  
    public void collectionTime() throws IOException {
        String b = (String) bookDrop.getSelectedItem();
        String v = (chapInput.getText() + ":" + verseInput.getText());
        String n1 = (newNote.getText()).replaceAll("\\r\\n|\\r|\\n", "*");
        String n2 = n1.replaceAll(",", "^");
        String n = n2;
        String p = "MM-dd-yyyy HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(p);
        String d = sdf.format(new Date());
        String collection = (d + "," + b + "," + v + "," + n);
        writeNote(collection);
        System.out.println(collection);
     }
    
    public void writeNote(String write) throws IOException {
        Writer output;
        output = new BufferedWriter(new FileWriter(loadPath, true));
        output.append(write);
        output.close();
    }
    
    public void removalTime() throws Exception {
       int removeIndex = existingList.getSelectedIndex();
       List<Remove> removes = readfromRemoves(loadPath);
         removes.clear();
         removes = readfromRemoves(loadPath);
        String linetoRemove = (removes.get(removeIndex).toString());
       try {
           File inputFile = new File(loadPath);
           File outputFile = new File(tempPath);
           try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
               String line = null;
               while ((line = reader.readLine()) != null) {
                   if (!line.equals(linetoRemove)) {
                       writer.write(line);
                       writer.newLine();
                   }
               }
           }
           if (inputFile.delete()) {
               if(!outputFile.renameTo(inputFile)) {
                   throw new IOException("");
               }
           }
           } catch (IOException ex) {
           errorBox.setText(ex.toString());
           }
       removes.clear();
    }

    public void restartGUI() {
        dispose();
        java.awt.EventQueue.invokeLater(() -> {
            try {
                new QtimeGUI().setVisible(true);
            } catch (IOException ex) {
                Logger.getLogger(QtimeGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void fileCheck(File filename) throws IOException {
        boolean exists = filename.exists();
        if (exists == false) {
            filename.createNewFile();
            try (FileWriter writer = new FileWriter(filename)) {
                writer.flush();
            }
        }
    }
    
    public static void buildBooks(File bookFile) throws IOException {
        boolean exists = bookFile.exists();
        if (exists == false) {
            bookFile.createNewFile();
            try (FileWriter writer = new FileWriter(bookFile)) {
                writer.write("Genesis" + "\n" + "Exodus" + "\n" + "Leviticus" +
                        "\n" + "Numbers" + "\n" + "Deuteronomy" + "\n" + 
                        "Joshua" + "\n" + "Judges" + "\n" + "Ruth" + "\n" + 
                        "1 Samuel" + "\n" + "2 Samuel" + "\n" + "1 Kings" + 
                        "\n" + "2 Kings" + "\n" + "1 Chronicles" + "\n" + 
                        "2 Chronicles" + "\n" + "Ezra" + "\n" + "Nehemiah" + 
                        "\n" + "Esther" + "\n" + "Job" + "\n" + "Psalms" + 
                        "\n" + "Proverbs" + "\n" + "Ecclesiastes" + "\n" + 
                        "Song of Songs" + "\n" + "Isaiah" + "\n" + "Jeremiah" + 
                        "\n" + "Lamentations" + "\n" + "Ezekiel" + "\n" + 
                        "Daniel" + "\n" + "Hosea" + "\n" + "Joel" + "\n" + 
                        "Amos" + "\n" + "Obadiah" + "\n" + "Jonah" + "\n" + 
                        "Micah" + "\n" + "Nahum" + "\n" + "Habakkuk" + "\n" + 
                        "Zephaniah" + "\n" + "Haggai" + "\n" + "Zechariah" + 
                        "\n" + "Malachi" + "\n" + "Matthew" + "\n" + "Mark" + 
                        "\n" + "Luke" + "\n" + "John" + "\n" + "Acts" + "\n" + 
                        "Romans" + "\n" + "1 Corinthians" + "\n" + 
                        "2 Corinthians" + "\n" + "Galatians" + "\n" + 
                        "Ephesians" + "\n" + "Philippians" + "\n" + "Colossians"
                        + "\n" + "1 Thessalonians" + "\n" + "2 Thessalonians" + 
                        "\n" + "1 Timothy" + "\n" + "2 Timothy" + "\n" + "Titus"
                        + "\n" + "Philemon" + "\n" + "Hebrews" + "\n" + "James" 
                        + "\n" + "1 Peter" + "\n" + "2 Peter" + "\n" + "1 John" 
                        + "\n" + "2 John" + "\n" + "3 John" + "\n" + "Jude" + 
                        "\n" + "Revelation" + "\n");
                writer.flush();
                writer.close();
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        existingList = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        existNote = new javax.swing.JTextArea();
        newButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        newNote = new javax.swing.JTextArea();
        bookLabel = new javax.swing.JLabel();
        bookDrop = new javax.swing.JComboBox<>();
        chapLabel = new javax.swing.JLabel();
        chapInput = new javax.swing.JTextField();
        verseLabel = new javax.swing.JLabel();
        verseInput = new javax.swing.JTextField();
        resetButton = new javax.swing.JButton();
        submitButton = new javax.swing.JButton();
        clearButton = new javax.swing.JButton();
        exitButton = new javax.swing.JButton();
        errorBox = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("[CSU.CSCI325]  Quiet Time Notes by Geoff Clark");

        existingList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        existingList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                existingListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(existingList);

        existNote.setEditable(false);
        existNote.setColumns(20);
        existNote.setLineWrap(true);
        existNote.setRows(5);
        jScrollPane2.setViewportView(existNote);

        newButton.setText("Create New Note");
        newButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newButtonActionPerformed(evt);
            }
        });

        deleteButton.setText("Delete Selected Note(s)");
        deleteButton.setEnabled(false);
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        newNote.setEditable(false);
        newNote.setColumns(20);
        newNote.setLineWrap(true);
        newNote.setRows(5);
        newNote.setEnabled(false);
        newNote.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                newNoteKeyTyped(evt);
            }
        });
        jScrollPane3.setViewportView(newNote);

        bookLabel.setText("Book");

        bookDrop.setEnabled(false);

        chapLabel.setText("Chapter");

        chapInput.setEnabled(false);
        chapInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                chapInputKeyTyped(evt);
            }
        });

        verseLabel.setText("Verse");

        verseInput.setEnabled(false);
        verseInput.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                verseInputKeyTyped(evt);
            }
        });

        resetButton.setText("Reset Selection");
        resetButton.setEnabled(false);
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });

        submitButton.setText("Submit New Note");
        submitButton.setEnabled(false);
        submitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitButtonActionPerformed(evt);
            }
        });

        clearButton.setText("Clear New Note");
        clearButton.setEnabled(false);
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });

        exitButton.setText("Exit");
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });

        errorBox.setForeground(new java.awt.Color(255, 0, 0));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 624, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(submitButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(clearButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(exitButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(errorBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(newButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(deleteButton))
                            .addComponent(jScrollPane1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(bookLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bookDrop, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(chapLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chapInput, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(verseLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(verseInput, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                                .addComponent(resetButton))
                            .addComponent(jScrollPane2))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newButton)
                    .addComponent(bookDrop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(verseLabel)
                    .addComponent(verseInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chapLabel)
                    .addComponent(bookLabel)
                    .addComponent(deleteButton)
                    .addComponent(resetButton)
                    .addComponent(chapInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(submitButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clearButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(errorBox, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                        .addComponent(exitButton)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitButtonActionPerformed

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        clearNotes();
    }//GEN-LAST:event_clearButtonActionPerformed

    private void verseInputKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_verseInputKeyTyped
        char c2 = evt.getKeyChar();
        if(!(Character.isDigit(c2) || c2 == KeyEvent.VK_BACK_SPACE || c2 == KeyEvent.VK_DELETE)){
            evt.consume();
        }
    }//GEN-LAST:event_verseInputKeyTyped

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        clearBooks();
    }//GEN-LAST:event_resetButtonActionPerformed

    private void newButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newButtonActionPerformed
        clearBooks();
        clearNotes();
        turnItOn();
    }//GEN-LAST:event_newButtonActionPerformed

    private void submitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitButtonActionPerformed
        
        if((chapInput.getText()) == null || chapInput.getText().equals("") || 
                (verseInput.getText() == null) || 
                verseInput.getText().equals("") || newNote.getText().equals("") 
                || (newNote.getText() == null)) {
            errorBox.setText("Missing Field.  Please check inputs.");
        } else {
        
        try {
            // TODO submit code here
            checkVerse();
        } catch (IOException ex) {
            Logger.getLogger(QtimeGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        restartGUI();
        
        }
    }//GEN-LAST:event_submitButtonActionPerformed

    private void existingListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_existingListValueChanged
        int selectedIndex = existingList.getSelectedIndex();
        try {
            loadNotes(selectedIndex);
        } catch (IOException ex) {
            Logger.getLogger(QtimeGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        deleteButton.setEnabled(true);        
    }//GEN-LAST:event_existingListValueChanged

    private void chapInputKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chapInputKeyTyped
        char c4 = evt.getKeyChar();
        if(!(Character.isDigit(c4) || c4 == KeyEvent.VK_BACK_SPACE || c4 == KeyEvent.VK_DELETE)){
            evt.consume();
        }
    }//GEN-LAST:event_chapInputKeyTyped

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        try {
            removalTime();
        } catch (Exception ex) {
            Logger.getLogger(QtimeGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        restartGUI();
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void newNoteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_newNoteKeyTyped
        if(evt.getKeyChar()=='*'){
            errorBox.setText("No asterisks allowed.");
            evt.consume();
        } else {
            if(evt.getKeyChar()=='^') {
                errorBox.setText("No carets allowed.");
                evt.consume();
            } else {
            if(evt.getKeyChar()=='"' || evt.getKeyChar()=='\'') {
                errorBox.setText("No quotes allowed.");
                evt.consume();
            }
            else {
                errorBox.setText("");
            }
            }
        }       
    }//GEN-LAST:event_newNoteKeyTyped

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            try {
                new QtimeGUI().setVisible(true);
            } catch (IOException ex) {
                Logger.getLogger(QtimeGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> bookDrop;
    private javax.swing.JLabel bookLabel;
    private javax.swing.JTextField chapInput;
    private javax.swing.JLabel chapLabel;
    private javax.swing.JButton clearButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JLabel errorBox;
    private javax.swing.JTextArea existNote;
    private javax.swing.JList<String> existingList;
    private javax.swing.JButton exitButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JButton newButton;
    private javax.swing.JTextArea newNote;
    private javax.swing.JButton resetButton;
    private javax.swing.JButton submitButton;
    private javax.swing.JTextField verseInput;
    private javax.swing.JLabel verseLabel;
    // End of variables declaration//GEN-END:variables
}