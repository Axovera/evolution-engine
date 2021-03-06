/*
 * topPanel.java
 *
 * Created on 26 November 2008, 15:08
 */

package gui;


import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


import frames.IFramesTaker;

import java.awt.Color;
import java.awt.Stroke;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import javax.swing.SwingUtilities;
import util.IMarkTaker;

/**
 * Panel that holds a FrameCollection, shows them and allows multiple sellections
 * @author  Gil Costa
 */
public class InteractiveCollectionLinePanel extends CollectionLinePanel implements MouseMotionListener{
	/** default version id */
	private static final long serialVersionUID = 1L;

	//---------------------
	// ---- CONSTANTS ----
	//---------------------

    protected static final Color OVER_COLOR = Color.BLUE;
	protected static final Color OVER_BACK_COLOR = new Color(0,50,150);

    
    // controll vars
    
    int superFastDraw1 = -1;
    int superFastDraw2 = -1;
    
	// ----------------------------
	//  ----   CONSTRUCTORS   ----
	// ----------------------------
	/** Creates new form FramePanel */
	public InteractiveCollectionLinePanel(Scroller scroller, IFramesTaker taker, IMarkTaker markTaker) {
		super(scroller,taker, markTaker);
		this.addMouseMotionListener(this);
	}



    
    
    @Override
    public void mark(int i, boolean mark){
        if (i<0 || i>markedOnes.size()) return;
        markedOnes.set(i, mark);
        if (markTaker!=null)
            markTaker.takeMark(collection.get(i), i);
    }


	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 600, Short.MAX_VALUE)
		);
		layout.setVerticalGroup(
				layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 200, Short.MAX_VALUE)
		);
	}// </editor-fold>//GEN-END:initComponents



	// -----------------------------------
	//  ------   MOUSE LISTENERS   ------
	// -----------------------------------

    
    @Override public void mouseReleased(MouseEvent e) {
        for(int i=0; i< markedOnes.size(); i++){
            markedOnes.set(i, false);
        }
        superFastDraw1 = superFastDraw2 = -1;
        repaint();
    }
    
    @Override
	public void mousePressed(MouseEvent e) {
		if (collection!=null && !collection.isEmpty() && SwingUtilities.isLeftMouseButton(e)){
			int x = (int)(e.getX()/scaleFactor);
			int y = (int)(e.getY()/scaleFactor);
			// get line and collumn
			x /= dim.width;
			y /= dim.height;
			// get the index
			int dw = Math.max((int)(dim.width * scaleFactor),1);
			int columns = getWidth()/dw;
			int index = y*columns + x;
            fastSelect(index);
            if (index>=0 && index<markedOnes.size() && !markedOnes.get(index)){
                mark(index, true);
            }
		}
	}
	
    public void mouseDragged(MouseEvent e) {
        if (collection!=null && !collection.isEmpty() && SwingUtilities.isLeftMouseButton(e)){
			int x = (int)(e.getX()/scaleFactor);
			int y = (int)(e.getY()/scaleFactor);
			// get line and collumn
			x /= dim.width;
			y /= dim.height;
			// get the index
			int dw = Math.max((int)(dim.width * scaleFactor),1);
			int columns = getWidth()/dw;
			int index = y*columns + x;
            if (index>=0 && index<markedOnes.size()){
                if (!markedOnes.get(index))
                    mark(index, true);
                fastSelect(index);
            }
		}
    }

    public void mouseMoved(MouseEvent e) {
         if (collection!=null && !collection.isEmpty()){
			int x = (int)(e.getX()/scaleFactor);
			int y = (int)(e.getY()/scaleFactor);
			// get line and collumn
			x /= dim.width;
			y /= dim.height;
			// get the index
			int dw = Math.max((int)(dim.width * scaleFactor),1);
			int columns = getWidth()/dw;
			int index = y*columns + x;
            if (index>=0 && index<markedOnes.size()){
                fastSelect(index);
            }
         }
    }

    
    
    
	// -----------------------
	//  ----   REPAINT   ----
	// -----------------------
//	public void repaintCM(){
//	int x = frame.getCM().getX();
//	int y = frame.getCM().getY();
//	repaintCursor(x,y);
//	}
//	protected void repaintCursor(int x, int y){
//	repaint(new Rectangle(x-CM_RADIUS,y-CM_RADIUS,CM_SIZE,CM_SIZE));
//	}


    public void fastSelect(int index){
        if (index != -1){
            superFastDraw1 = selected;
            superFastDraw2 = index;
        }
        else superFastDraw1 = superFastDraw2 = -1;
        select(index);
        repaintCell(superFastDraw1);
    }

    @Override
    public void select(int id){
        if (collection == null || collection.isEmpty() || taker == null){
            repaint();
            return;
        }
		selected = id;
		if (selected ==-1) selected = collection.size()-1;
        else if (selected <-1) selected = 0;
        else if (selected == collection.size()) selected = 0;
        else if (selected > collection.size()) selected =collection.size()-1;
		taker.takeFrame(collection.get(selected));
        
        int rows = rows();
        int cols = columns();
        //double px = (selected%rows)/(cols*1.);
        //double py = ((selected/cols))/(rows*1.);
        int px = (selected%rows)*dim.width;
        int py = (selected/cols)*dim.height;
        //scroller.setPosition(px,py);
        //updateSize(px,py);
	}
    

    
    
    @Override
	public void componentResized(ComponentEvent e) {
        super.componentResized(e);
        // inactivate fastDraw
        superFastDraw1 = superFastDraw2 = -1;
        repaint();
	}
    
    @Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		super.mouseWheelMoved(e);
        // inactivate fastDraw
        superFastDraw1 = superFastDraw2 = -1;
        repaint();
	}
    
	@Override
	public void paintComponent(Graphics g) {
		clearScr(g);
		if (collection == null || collection.isEmpty() || dim == null) return;

		int dw = (int)(dim.width * scaleFactor);
		int dh = (int)(dim.height * scaleFactor);
		if (dw<=0) dw = 1;
		if (dh<=0) dh = 1;
		int columns = getWidth()/dw;
		if (columns == 0) columns =1;
        
        g.setColor(GRID_COLOR);
        int j = 0;
        int last = columns;
        //if (superFastDraw1!=-1){j = Math.max(0,(superFastDraw1-1)%columns); last = Math.min(columns,(superFastDraw1+1)%columns); }
        // draw grid columns
        for(; j<=last; j++){
            g.drawLine(j*dw, 0, j*dw, getHeight());
        }
        // draw grid rows
        int rows = getHeight()/dh;
        j = 0;
        last = rows;
        //if (superFastDraw1!=-1){j = Math.max(0,(superFastDraw1-1)/columns); last = Math.min(columns,(superFastDraw1+1)/columns); }
        for(; j<=last; j++){
            g.drawLine(0, j*dh, getWidth(), j*dh);
        }
        
        
        int i=0;
        int total = collection.size()-1;
        if (superFastDraw1!=-1){ i = total = superFastDraw1; }
		// draw each frame
		for(; i<=total; i++){
			BufferedImage img = collection.get(i).getImage();
			int imgX = (i%columns)*dw;
			int imgY = (i/columns)*dh;
            
            if (i == selected){
				// selected frame, draw background first
				g.setColor(OVER_BACK_COLOR);
				g.fillRect(imgX, imgY, dw, dh);
			}
            
            if (markedOnes.size()>i && markedOnes.get(i)){
                g.setColor(MARKED_BACK_COLOR);
				g.fillRect(imgX, imgY, dw, dh);
            }
            
            int imgW = (int)(img.getWidth()*scaleFactor);
			int imgH = (int)(img.getHeight()*scaleFactor);
            g.drawImage(img,imgX,imgY,imgW,imgH,this);
            
            if (markedOnes.size()>i && markedOnes.get(i)){
                g.setColor(MARKED_COLOR);
                Graphics2D g2d = (Graphics2D)g;
                Stroke old = g2d.getStroke();
                g2d.setStroke(SELECTED_STROKE);
                g.drawRect(imgX, imgY, dw, dh);
                g2d.setStroke(old);
            }
			
		}
        
        // draw a rectangle around the selected image
        g.setColor(OVER_COLOR);
        Graphics2D g2d = (Graphics2D)g;
        Stroke old = g2d.getStroke();
        g2d.setStroke(SELECTED_STROKE);
        int imgX = (selected%columns)*dw;
		int imgY = (selected/columns)*dh;
        g.drawRect(imgX, imgY, dw, dh);
        g2d.setStroke(old);
        if (superFastDraw2 != -1){
            superFastDraw1 = superFastDraw2;
            superFastDraw2 = -1;
            repaintCell(superFastDraw1);
        }
        else superFastDraw1 = -1;
	}

  








	// Variables declaration - do not modify//GEN-BEGIN:variables
	// End of variables declaration//GEN-END:variables

}
