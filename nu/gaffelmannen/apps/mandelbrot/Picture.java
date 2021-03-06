package nu.gaffelmannen.apps.mandelbrot;

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.JScrollPane;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.MouseInfo;
import javax.swing.InputMap;
import javax.swing.JComponent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Observable;

public final class Picture extends Observable implements MouseWheelListener {
	static final String NEWLINE = System.getProperty("line.separator");
	
	private JScrollPane scrollPane;
    private BufferedImage image;
    private JFrame frame;
    private String filename;
    private boolean isOriginUpperLeft = true;
    private int width, height;
    private boolean output = false;
    private int unitsScrolled = 0;
    private double relativeCursorPosX = 0;
    private double relativeCursorPosY = 0;
    private boolean reset = false;
    private boolean panUp = false;
    private boolean panDown = false;
    private boolean panRight = false;
    private boolean panLeft = false;
    private boolean zoom = false;

    public int getUnitsScrolled() {
    	return unitsScrolled;
    }
    public double getRelativeCursorPosX() {
    	return relativeCursorPosX;
    }
    public double getRelativeCursorPosY() {
    	return relativeCursorPosY;
    }
    public boolean Reset() {
    	return reset;
    }
    public int height() {
        return height;
    }
    public int width() {
        return width;
    }
    public void clearResetFlag() {
    	this.reset = false;
    }
    public boolean getPanUpFlag() {
    	return this.panUp;
    }
    public void clearPanUpFlag() {
    	synchronized(this) {
			panUp = false;
			notify();
		}
    }
    public boolean getPanDownFlag() {
    	return this.panDown;
    }
    public void clearPanDownFlag() {
    	synchronized(this) {
			panDown = false;
			notify();
		}
    }
    public boolean getPanRightFlag() {
    	return this.panRight;
    }
    public void clearPanRightFlag() {
    	synchronized(this) {
			panRight = false;
			notify();
		}
    }
    public boolean getPanLeftFlag() {
    	return this.panLeft;
    }
    public void clearPanLeftFlag() {
    	synchronized(this) {
			panLeft = false;
			notify();
		}
    }
    public boolean getZoomFlag() {
    	return this.zoom;
    }
    public void clearZoomFlag() {
    	synchronized(this) {
			zoom = false;
			notify();
		}
    }
    
    public Picture(int w, int h) {
        width = w;
        height = h;
        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        // set to TYPE_INT_ARGB to support transparency
        filename = w + "-by-" + h;
    }

    public Picture(String filename) {
        this.filename = filename;
        try {
            // try to read from file in working directory
            File file = new File(filename);
            if (file.isFile()) {
                image = ImageIO.read(file);
            }
            else {
                URL url = getClass().getResource(filename);
                if (url == null) { url = new URL(filename); }
                image = ImageIO.read(url);
            }
            width  = image.getWidth(null);
            height = image.getHeight(null);
        }
        catch (IOException e) {
            // e.printStackTrace();
            throw new RuntimeException("Could not open file: " + filename);
        }
    }

    public Picture(File file) {
        try { image = ImageIO.read(file); }
        catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not open file: " + file);
        }
        if (image == null) {
            throw new RuntimeException("Invalid image file: " + file);
        }
    }

    public JLabel getJLabel() {
        if (image == null) { return null; }         // no image available
        ImageIcon icon = new ImageIcon(image);
        return new JLabel(icon);
    }

    public void setOriginUpperLeft() {
        isOriginUpperLeft = true;
    }

    public void setOriginLowerLeft() {
        isOriginUpperLeft = false;
    }
    
    public void show() {

        // create the GUI for viewing the image if needed
        if (frame == null) {
            frame = new JFrame();
            frame.addMouseWheelListener(this);

            scrollPane = new JScrollPane();
            
            JMenuBar menuBar = new JMenuBar();
            JMenu menu = new JMenu("File");
            menuBar.add(menu);
            
            JMenuItem menuItemSave = new JMenuItem(" Save...   ");
            menuItemSave.addActionListener(new ActionListener() {
            	public void actionPerformed(ActionEvent e) {
            		FileDialog chooser = new FileDialog(frame, "Use a .png or .jpg extension", FileDialog.SAVE);
            		chooser.setVisible(true);
					if (chooser.getFile() != null) {
						save(chooser.getDirectory() + File.separator + chooser.getFile());
					}
            	}
            });
            menuItemSave.setAccelerator(
        		KeyStroke.getKeyStroke(KeyEvent.VK_S,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
            menu.add(menuItemSave);
            
            JMenuItem menuItemReset = new JMenuItem("Reset");
            menuItemReset.addActionListener(new ActionListener() {
            	public void actionPerformed(ActionEvent e) {
            		synchronized (this) {
            			reset = true;
            			System.out.println("Reset.");
            			notify();
            		}
            	}
            });
            menu.add(menuItemReset);
            
            frame.addKeyListener(new KeyAdapter() {
            	public void keyPressed(KeyEvent evt) {
            		if (evt.getKeyCode() == KeyEvent.VK_UP) {
            			synchronized(this) {
                			panUp = true;
                			setChanged();
                			notifyObservers("up");
                			notify();
                		}
            		}
            		if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
            			synchronized(this) {
                			panDown = true;
                			setChanged();
                			notifyObservers("down");
                			notify();
                		}
            		}
            		if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
            			synchronized(this) {
                			panRight = true;
                			setChanged();
                			notifyObservers("right");
                			notify();
                		}
            		}
            		if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
            			synchronized(this) {
                			panLeft = true;
                			setChanged();
                			notifyObservers("left");
                			notify();
                		}
            		}
            	}
            });
            
            
            frame.setJMenuBar(menuBar);
            frame.setContentPane(getJLabel());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setTitle(filename);
            frame.setResizable(false);
            frame.pack();
            frame.setVisible(true);
        }

        // draw
        frame.repaint();
    }

    public Color get(int i, int j) {
        if (isOriginUpperLeft) return new Color(image.getRGB(i, j));
        else                   return new Color(image.getRGB(i, height - j - 1));
    }

    public void set(int i, int j, Color c) {
        if (c == null) { throw new RuntimeException("can't set Color to null"); }
        if (isOriginUpperLeft) image.setRGB(i, j, c.getRGB());
        else                   image.setRGB(i, height - j - 1, c.getRGB());
    }

    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;
        Picture that = (Picture) obj;
        if (this.width()  != that.width())  return false;
        if (this.height() != that.height()) return false;
        for (int x = 0; x < width(); x++)
            for (int y = 0; y < height(); y++)
                if (!this.get(x, y).equals(that.get(x, y))) return false;
        return true;
    }

    public void save(String name) {
        save(new File(name));
    }

    public void save(File file) {
        this.filename = file.getName();
        if (frame != null) { frame.setTitle(filename); }
        String suffix = filename.substring(filename.lastIndexOf('.') + 1);
        suffix = suffix.toLowerCase();
        if (suffix.equals("jpg") || suffix.equals("png")) {
            try { ImageIO.write(image, suffix, file); }
            catch (IOException e) { e.printStackTrace(); }
        }
        else {
            System.out.println("Error: filename must end in .jpg or .png");
        }
    }
    
    void eventOutput(String eventDescription, MouseWheelEvent e) {
    	System.out.println(eventDescription);
    }
    
    public void mouseWheelMoved(MouseWheelEvent e) {
    	synchronized (this) {
    		this.unitsScrolled = e.getUnitsToScroll();
    		this.zoom = true;
    		/*
    		relativeCursorPosY = (double)(MouseInfo.getPointerInfo().getLocation().x / (double)width());
    		relativeCursorPosY = (double)(MouseInfo.getPointerInfo().getLocation().y / (double)height());
    		
    		if(relativeCursorPosX < 0.5) {
    			relativeCursorPosX = -relativeCursorPosX;
    		}
    		
    		if(relativeCursorPosY < 0.5) {
    			relativeCursorPosY = -relativeCursorPosY;
    		}
    		*/
    		setChanged();
			notifyObservers("zoom");
    		notify();
		}
    	
    	if(output) {
	        String message;
	        int notches = e.getWheelRotation();
	        if (notches < 0) {
	            message = "Mouse wheel moved UP "
	                         + -notches + " notch(es)" + NEWLINE;
	        } else {
	            message = "Mouse wheel moved DOWN "
	                         + notches + " notch(es)" + NEWLINE;
	        }
	        if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
	            message += "    Scroll type: WHEEL_UNIT_SCROLL" + NEWLINE;
	            message += "    Scroll amount: " + e.getScrollAmount()
	                    + " unit increments per notch" + NEWLINE;
	            message += "    Units to scroll: " + e.getUnitsToScroll()
	                    + " unit increments" + NEWLINE;
	            message += "    Vertical unit increment: "
	                + scrollPane.getVerticalScrollBar().getUnitIncrement(1)
	                + " pixels" + NEWLINE;
	        } else { //scroll type == MouseWheelEvent.WHEEL_BLOCK_SCROLL
	            message += "    Scroll type: WHEEL_BLOCK_SCROLL" + NEWLINE;
	            message += "    Vertical block increment: "
	                + scrollPane.getVerticalScrollBar().getBlockIncrement(1)
	                + " pixels" + NEWLINE;
	        }
	        
	        eventOutput(message, e);
    	}
    }
    
    void repaint() {
        frame.repaint();
    }

    public static void main(String[] args) {
        Picture pic = new Picture(args[0]);
        System.out.printf("%d-by-%d\n", pic.width(), pic.height());
        pic.show();
    }

}
