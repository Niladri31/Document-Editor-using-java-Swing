
package documentdesign;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.UndoManager;

//----------------------------------------------------------------------------------------------------------------------------------------------------
//CREATION OF BASE OF UI BY COMPOSITION
interface CompositeBase {
    
    JPanel addElement();
    
    JTextPane addLeafElement();
}

//To create base and small components 
class Composite implements CompositeBase {
    
    JFrame base;
    JPanel title, msg, tools, editarea, scroll;
    JLabel result, logo, caption;
    JTextPane editarea2;
    int flagSave = 0;
    File fileSave = null;
    
    Composite() {
        base = new JFrame("Document Editor");
        base.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        base.setVisible(true);
        base.setLayout(new BorderLayout());
        base.setSize(2000, 1080);
        addElement();
    }
    
    @Override
    public JPanel addElement() {
        
        title = new JPanel();
        msg = new JPanel();
        editarea = new JPanel();
        scroll = new JPanel();
        tools = new JPanel();
        
        base.add(title, BorderLayout.NORTH);
        base.add(editarea, BorderLayout.CENTER);
        base.add(msg, BorderLayout.SOUTH);
        base.add(scroll, BorderLayout.EAST);
        base.add(tools, BorderLayout.WEST);
        
        title.setPreferredSize(new Dimension(600, 50));
        msg.setPreferredSize(new Dimension(600, 150));
        editarea.setPreferredSize(new Dimension(600, 200));
        scroll.setPreferredSize(new Dimension(130, 100));
        tools.setPreferredSize(new Dimension(115, 100));
        
        title.setBackground(new Color(157, 240, 179));
        msg.setBackground(new Color(157, 240, 179));
        editarea.setBackground(new Color(157, 240, 179));
        scroll.setBackground(new Color(157, 240, 179));
        tools.setBackground(new Color(157, 240, 179));
        
        return tools;
    }
    
    @Override
    public JTextPane addLeafElement() {
        editarea2 = new JTextPane();
        editarea2.setPreferredSize(new Dimension(700, 1000));
        JScrollPane jp = new JScrollPane(editarea2);
        
        Font f = new Font("Courier", Font.PLAIN, 30);
        result = new JLabel();
        result.setFont(f);
        result.setPreferredSize(new Dimension(600, 200));
        result.setText("Editor using Design patterns.");
        msg.add(result);
        logo = new JLabel();
        title.setLayout(new BorderLayout());
        logo.setSize(new Dimension(150, 150));
        title.add(logo, BorderLayout.WEST);
//        logo.setIcon(new ImageIcon("C:\\Users\\HP\\Desktop\\logo.png"));
        caption = new JLabel();
        caption.setFont(f);
        caption.setPreferredSize(new Dimension(600, 200));
        caption.setText("Document Editor");
        title.add(caption, BorderLayout.CENTER);
        JMenuBar menubar = new JMenuBar();
        
        // to provide menu in text editor
        JMenu file = new JMenu("File");
        menubar.add(file);
        JMenuItem neww = new JMenuItem("New");// set text area to blank on click of new option
        neww.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (!editarea2.getText().equals("")) {
                    saveas();
                }
                editarea2.setText("");
                flagSave = 0;
                fileSave = null;
            }
        });
        file.add(neww);
        JMenuItem open = new JMenuItem("Open");
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
//                XWPFWordExtractor extractor;

//J file chooser allows user to select files from his/her machine
                JFileChooser fileChooser = new JFileChooser();
                int returnVal = fileChooser.showOpenDialog(base);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try {
                        FileInputStream fis = new FileInputStream(file.getAbsolutePath());
                        BufferedReader input = new BufferedReader(new InputStreamReader(
                                new FileInputStream(file)));
                        editarea2.read(input, "READING FILE :-)");
                        fis.close();
                        flagSave = 1;
                        fileSave = file;
                    } catch (Exception ex) {
                        System.out.println("problem accessing file" + file.getAbsolutePath());
                    }
                } else {
                    System.out.println("File access cancelled by user.");
                }
            }
        });
        file.add(open);
        JMenuItem save = new JMenuItem("Save");
        file.add(save);
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (flagSave == 1) {
                    BufferedWriter outFile = null;
                    try {
                        outFile = new BufferedWriter(new FileWriter(fileSave));
                    } catch (IOException ex) {
                        Logger.getLogger(Documentdesign.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        editarea2.write(outFile);
                    } catch (IOException ex) {
                        Logger.getLogger(Documentdesign.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        if (outFile != null) {
                            try {
                                outFile.close();
                            } catch (IOException e) {
                            }
                        }
                    }
                } else {
                    saveas();
                }
            }
        });
        
        JMenuItem saveas = new JMenuItem("Save As");
        saveas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                saveas();
            }
        });
        
        file.add(saveas);
        JMenuItem quit = new JMenuItem("Quit");
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                base.dispose();
            }
        });
        file.add(quit);
        JMenu edit = new JMenu("Edit");
        menubar.add(edit);
        
        JMenuItem cut = new JMenuItem(new DefaultEditorKit.CutAction());
        cut.setText("Cut");
        edit.add(cut);
        JMenuItem copy = new JMenuItem(new DefaultEditorKit.CopyAction());
        copy.setText("Copy");
        edit.add(copy);
        JMenuItem paste = new JMenuItem(new DefaultEditorKit.PasteAction());
        paste.setText("Paste");
        edit.add(paste);
        JRootPane root = editarea.getRootPane();
        root.setJMenuBar(menubar);
        
        root.getContentPane().add(jp);
        editarea2.setFont(new Font("Courier", Font.PLAIN, 18));
        editarea2.setVisible(true);
        editarea.setVisible(true);
        
        return editarea2;
    }
    
    void saveas() {

// File Name ExtensionFilter helps us to decide what would be extension of saved file
        FileNameExtensionFilter extensionFilter = new FileNameExtensionFilter("Text File", "txt");
        final JFileChooser saveAsFileChooser = new JFileChooser();
        saveAsFileChooser.setApproveButtonText("Save");
        saveAsFileChooser.setFileFilter(extensionFilter);
        int actionDialog = saveAsFileChooser.showOpenDialog(base);
        if (actionDialog != JFileChooser.APPROVE_OPTION) {
            return;
        }
        
        File file = saveAsFileChooser.getSelectedFile();
        if (!file.getName().endsWith(".txt")) {
            file = new File(file.getAbsolutePath() + ".txt");
        }
        
        BufferedWriter outFile = null;
        try {
            outFile = new BufferedWriter(new FileWriter(file));
        } catch (IOException ex) {
        }
        
        try {
            editarea2.write(outFile);
        } catch (IOException ex) {
        } finally {
            if (outFile != null) {
                try {
                    outFile.close();
                } catch (IOException e) {
                }
            }
            
        }
    }
}

//----------------------------------------------------------------------------------------------------------------------------------------------------
//CREATION OF BUTTONS USING COMMAND DP
interface Button {
    
    void execute();
//Fonts are already predefined in java can be used directly
    Font f = new Font("Courier", Font.BOLD, 16);
}

//classes implementing Button interface
class ButtonSize implements Button {
    
    JButton s10, s14, s16;
    JPanel jp;
    JTextPane s;
    JLabel label;
    
    ButtonSize(JPanel j, JTextPane tp) {
        jp = j;
        s = tp;
        label = new JLabel("Font Size");
        s10 = new JButton("Size 10");
        s14 = new JButton("Size 14");
        s16 = new JButton("Size 16");
        label.setFont(f);
        
    }
    
    @Override
    public void execute() {
        jp.add(label);
        s10.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                StyledDocument doc = (StyledDocument) s.getDocument();
                Style style = doc.addStyle("StyleName", null);
                StyleConstants.setFontSize(style, 10);
                try {
                    String text = s.getDocument().getText(0, s.getDocument().getLength());
                    s.setText("");
                    doc.insertString(0, text, style);
                } catch (BadLocationException ex) {
                }
            }
        });
        jp.add(s10);
        
        s14.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                StyledDocument doc = (StyledDocument) s.getDocument();
                Style style = doc.addStyle("StyleName", null);
                StyleConstants.setFontSize(style, 14);
                try {
                    String text = s.getDocument().getText(0, s.getDocument().getLength());
                    s.setText("");
                    doc.insertString(0, text, style);
                } catch (BadLocationException ex) {
                }
            }
        });
        jp.add(s14);
        
        s16.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                StyledDocument doc = (StyledDocument) s.getDocument();
                Style style = doc.addStyle("StyleName", null);
                StyleConstants.setFontSize(style, 16);
                try {
                    String text = s.getDocument().getText(0, s.getDocument().getLength());
                    s.setText("");
                    doc.insertString(0, text, style);
                } catch (BadLocationException ex) {
                }
            }
        });
        jp.add(s16);
    }
}

class ButtonStyle implements Button {
    
    JButton b, i, u;
    JPanel jp;
    JTextPane s;
    JLabel label;
    
    ButtonStyle(JPanel j, JTextPane tp) {
        jp = j;
        s = tp;
        label = new JLabel("Font Style");
        label.setFont(f);
        
        b = new JButton("Bold");
        i = new JButton("Italics");
        u = new JButton("Underline");
    }
    
    @Override// add styles like bold, italics
    public void execute() {
        jp.add(label);
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                StyledDocument doc = (StyledDocument) s.getDocument();
                Style style = doc.addStyle("StyleName", null);
                StyleConstants.setBold(style, true);
                try {
                    String text = s.getDocument().getText(0, s.getDocument().getLength());
                    s.setText("");
                    doc.insertString(0, text, style);
                } catch (BadLocationException ex) {
                }
            }
        });
        jp.add(b);
        
        i.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                StyledDocument doc = (StyledDocument) s.getDocument();
                Style style = doc.addStyle("StyleName", null);
                StyleConstants.setItalic(style, true);
                try {
                    String text = s.getDocument().getText(0, s.getDocument().getLength());
                    s.setText("");
                    doc.insertString(0, text, style);
                } catch (BadLocationException ex) {
                }
            }
        });
        jp.add(i);
        
        u.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                StyledDocument doc = (StyledDocument) s.getDocument();
                Style style = doc.addStyle("StyleName", null);
                StyleConstants.setUnderline(style, true);
                try {
                    String text = s.getDocument().getText(0, s.getDocument().getLength());
                    s.setText("");
                    doc.insertString(0, text, style);
                } catch (BadLocationException ex) {
                }
            }
        });
        jp.add(u);
    }
}

class ButtonFam implements Button {
    
    JButton f1, f2, f3;
    JPanel jp;
    JTextPane s;
    JLabel label;
    
    ButtonFam(JPanel j, JTextPane tp) {
        jp = j;
        s = tp;
        label = new JLabel("Font Family");
        label.setFont(f);
        // font menu
        f1 = new JButton("Monospaced");
        f2 = new JButton("Times New Roman");
        f3 = new JButton("Sans Serif");
    }
    
    @Override
    public void execute() {
        jp.add(label);
        f1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                StyledDocument doc = (StyledDocument) s.getDocument();
                Style style = doc.addStyle("StyleName", null);
                StyleConstants.setFontFamily(style, Font.MONOSPACED);
                try {
                    String text = s.getDocument().getText(0, s.getDocument().getLength());
                    s.setText("");
                    doc.insertString(0, text, style);
                } catch (BadLocationException ex) {
                }
            }
        });
        jp.add(f1);
        
        f2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                StyledDocument doc = (StyledDocument) s.getDocument();
                Style style = doc.addStyle("StyleName", null);
                StyleConstants.setFontFamily(style, "Times New Roman");
                try {
                    String text = s.getDocument().getText(0, s.getDocument().getLength());
                    s.setText("");
                    doc.insertString(0, text, style);
                } catch (BadLocationException ex) {
                }
            }
        });
        jp.add(f2);
        
        f3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                StyledDocument doc = (StyledDocument) s.getDocument();
                Style style = doc.addStyle("StyleName", null);
                StyleConstants.setFontFamily(style, Font.SANS_SERIF);
                try {
                    String text = s.getDocument().getText(0, s.getDocument().getLength());
                    s.setText("");
                    doc.insertString(0, text, style);
                } catch (BadLocationException ex) {
                }
            }
        });
        jp.add(f3);
    }
}

class ButtonColor implements Button {
    
    JPanel jp;
    JTextPane s;
    
    ButtonColor(JPanel j, JTextPane tp) {
        jp = j;
        s = tp;
    }
    
    @Override
    public void execute() {
        JLabel label = new JLabel("Font Color");
        label.setFont(f);
        jp.add(label);
        JButton f1 = new JButton("Red");
        f1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                StyledDocument doc = (StyledDocument) s.getDocument();
                Style style = doc.addStyle("StyleName", null);
                StyleConstants.setForeground(style, Color.RED);
                try {
                    String text = s.getDocument().getText(0, s.getDocument().getLength());
                    s.setText("");
                    doc.insertString(0, text, style);
                } catch (BadLocationException ex) {
                }
            }
        });
        jp.add(f1);
        JButton f2 = new JButton("Blue");
        f2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                StyledDocument doc = (StyledDocument) s.getDocument();
                Style style = doc.addStyle("StyleName", null);
                StyleConstants.setForeground(style, Color.BLUE);
                try {
                    String text = s.getDocument().getText(0, s.getDocument().getLength());
                    s.setText("");
                    doc.insertString(0, text, style);
                } catch (BadLocationException ex) {
                }
            }
        });
        jp.add(f2);
        JButton f3 = new JButton("Green");
        f3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                StyledDocument doc = (StyledDocument) s.getDocument();
                Style style = doc.addStyle("StyleName", null);
                StyleConstants.setForeground(style, new Color(34, 139, 34));
                try {
                    String text = s.getDocument().getText(0, s.getDocument().getLength());
                    s.setText("");
                    doc.insertString(0, text, style);
                } catch (BadLocationException ex) {
                }
            }
        });
        jp.add(f3);
    }
}

class ButtonDecor implements Button {
    
    JPanel jp;
    JTextPane s;
    int flagb = 0;
    
    ButtonDecor(JPanel j, JTextPane tp) {
        jp = j;
        s = tp;
    }
    
    @Override
    public void execute() {
        JLabel label = new JLabel("Decorate");
        label.setFont(f);
        jp.add(label);
        
        JButton s10 = new JButton("Border");
        s10.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Decor d = new DecorBorder(s);
                d.draw(flagb);
                flagb++;
            }
        });
        jp.add(s10);
    }
}

class ButtonOs implements Button {
    
    JPanel jp;
    JLabel kl;
    
    ButtonOs(JPanel j, JLabel k) {
        jp = j;
        kl = k;
    }
    
    @Override
    public void execute() {
        JLabel label = new JLabel("Change O.S");
        label.setFont(f);
        jp.add(label);
        JButton w = new JButton("Windows");
        w.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //bridge dp
                Software windows = new OS("Windows", "ARM", new Windows(), kl);
                windows.display();
            }
        });
        jp.add(w);
        JButton l = new JButton("Linux");
        l.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //bridge dp
                Software linux = new OS("Linux", "Alpha", new Linux(), kl);
                linux.display();
            }
        });
        jp.add(l);
        JButton m = new JButton("Mac OS");
        m.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //bridge dp
                Software mac = new OS("Mac OS", "x86-64", new macOS(), kl);
                mac.display();
            }
        });
        jp.add(m);
    }
}

//Invoker class
class Place {
    
    ArrayList<Button> list = new ArrayList();
    
    public void add(Button b) {
        list.add(b);
    }
    
    public void placeButtons() {
        for (Button cc : list) {
            cc.execute();
        }
    }
}

class ButtonTheme implements Button {
    
    JPanel p1, p2, p3, p4;
    JLabel operations;
    JTextPane editarea2;
    
    ButtonTheme(JPanel a, JPanel b, JPanel c, JPanel d, JTextPane t) {
        p1 = a;
        p2 = b;
        p3 = c;
        p4 = d;
        editarea2 = t;
    }
    protected UndoManager undoManager = new UndoManager();
    
    protected JButton undoButton = new JButton("Undo");
    
    protected JButton redoButton = new JButton("Redo");
    
    @Override
    public void execute() {
        JLabel label = new JLabel("Theme");
        operations = new JLabel("Operations");
        label.setFont(f);
        p4.add(label);
        
        JButton blue = new JButton("Aqua Blue");
        blue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                p1.setBackground(new Color(153, 255, 255));
                p2.setBackground(new Color(153, 255, 255));
                p3.setBackground(new Color(153, 255, 255));
                p4.setBackground(new Color(153, 255, 255));
            }
        });
        p4.add(blue);
        JButton yellow = new JButton("Lime Yellow");
        yellow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                p1.setBackground(new Color(255, 255, 102));
                p2.setBackground(new Color(255, 255, 102));
                p3.setBackground(new Color(255, 255, 102));
                p4.setBackground(new Color(255, 255, 102));
            }
        });
        p4.add(yellow);
        JButton pink = new JButton("Rose Pink");
        pink.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                p1.setBackground(new Color(255, 204, 204));
                p2.setBackground(new Color(255, 204, 204));
                p3.setBackground(new Color(255, 204, 204));
                p4.setBackground(new Color(255, 204, 204));
            }
        });
        p4.add(pink);
        JButton green = new JButton("Mint Green");
        green.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                p1.setBackground(new Color(157, 240, 179));
                p2.setBackground(new Color(157, 240, 179));
                p3.setBackground(new Color(157, 240, 179));
                p4.setBackground(new Color(157, 240, 179));
            }
        });
        p4.add(green);
        operations.setFont(f);
        p4.add(operations);
        
        undoButton.setEnabled(false);
        redoButton.setEnabled(false);
        
        p4.add(undoButton);
        p4.add(redoButton);
        // undo redo: add addUndoableEditListener to editing area. This event listener keeps track of the previous formatting done and helps to undo.  similarly redo
        editarea2.getDocument().addUndoableEditListener(
                new UndoableEditListener() {
            public void undoableEditHappened(UndoableEditEvent e) {
                undoManager.addEdit(e.getEdit());
                updateButtons();
            }
        });
        
        undoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    undoManager.undo();
                } catch (CannotRedoException cre) {
                    cre.printStackTrace();
                }
                updateButtons();
            }
        });
        
        redoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    undoManager.redo();
                } catch (CannotRedoException cre) {
                    cre.printStackTrace();
                }
                updateButtons();
            }
        });
         
    }
    
    public void updateButtons() {
        undoButton.setEnabled(undoManager.canUndo());
        redoButton.setEnabled(undoManager.canRedo());
    }
    
}

//----------------------------------------------------------------------------------------------------------------------------------------------------
//CODE OF BRIDGE DESIGN PATTERN
interface DrawOS {
    
    public void displayOS(String type, String Platform, JLabel jl);
}

class Windows implements DrawOS {
    
    public void displayOS(String type, String Platform, JLabel jl) {
        jl.setText(type + " is selected \t-" + "Platform : " + Platform);
    }
}

class Linux implements DrawOS {
    
    public void displayOS(String type, String Platform, JLabel jl) {
        jl.setText(type + " is selected \t-" + "Platform : " + Platform);
    }
}

class macOS implements DrawOS {
    
    public void displayOS(String type, String Platform, JLabel jl) {
        jl.setText(type + " is selected \t-" + "\tPlatform : " + Platform);
    }
}

abstract class Software {
    
    protected DrawOS drawos;
    
    protected Software(DrawOS drawos) {
        this.drawos = drawos;
    }
    
    public abstract void display();
}

class OS extends Software {
    
    String type, Platform;
    JLabel jl;
    
    public OS(String type, String Platform, DrawOS drawos, JLabel j) {
        super(drawos);
        this.type = type;
        this.Platform = Platform;
        jl = j;
    }
    
    @Override
    public void display() {
        drawos.displayOS(type, Platform, jl);
    }
}

//----------------------------------------------------------------------------------------------------------------------------------------------------
//CODE OF DECORATOR DESIGN PATTERN
abstract class Decor {
    
    JTextPane pane;
    
    Decor(JTextPane p) {
        pane = p;
    }
    
    abstract void draw(int flag);
}

class DecorBorder extends Decor {
    
    public DecorBorder(JTextPane p) {
        super(p);
    }
    
    @Override
    void draw(int flag) {
        if (flag % 2 == 0) {
            pane.setMargin(new Insets(100, 100, 100, 100));
            pane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK), BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        } else {
            pane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.white), BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        }
    }
}

//----------------------------------------------------------------------------------------------------------------------------------------------------
//MAIN CLASS 
public class Documentdesign {
    
    public static void main(String[] args) {

        //launcher page
        initsplash();
        //COMPOSITE DP
        //for creation of basic ui (panels)
        Composite cp = new Composite();

        //for creation of leaf components of ui (menubar, text pane)
        JPanel tools = cp.addElement();
        JTextPane f = cp.addLeafElement();

        //BRIDGE DP
        //for creation of different sets of buttons on ui (font size, font style, font family, o.s, deaoration, font color)
        ButtonSize b1 = new ButtonSize(tools, f);
        ButtonStyle b2 = new ButtonStyle(tools, f);
        ButtonFam b3 = new ButtonFam(tools, f);
        ButtonTheme b4 = new ButtonTheme(cp.title, cp.msg, cp.tools, cp.scroll, f);
        ButtonColor b5 = new ButtonColor(tools, f);
        ButtonOs b6 = new ButtonOs(tools, cp.result);
        ButtonDecor b7 = new ButtonDecor(tools, f);
        
        Place p = new Place();
        p.add(b1);
        p.add(b2);
        p.add(b3);
        p.add(b4);
        p.add(b5);
        p.add(b7);
        p.add(b6);
        
        p.placeButtons();
        
    }

//FOR LAUNCHER PAGE
    private static void initsplash() {
        JFrame frame = new JFrame();
        frame.add(new JLabel(new ImageIcon("launcher3.png")));
        frame.setVisible(true);
        Dimension dim = frame.getToolkit().getScreenSize();
        int screenWidth = dim.width;
        int screenHeight = dim.height;
        int frameWidth = screenWidth / 3;
        int frameHeight = screenHeight / 2;
        frame.setSize(frameWidth, frameHeight);
        frame.setLocation((screenWidth - frameWidth) / 2, (screenHeight - frameHeight) / 2);
        frame.getContentPane().setBackground(new Color(157, 240, 179));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        frame.dispose();
    }
}
