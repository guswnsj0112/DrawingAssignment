package org.graphicsEditor.frames;

import org.graphicsEditor.global.GConstants;
import org.graphicsEditor.shapes.GRectangle;
import org.graphicsEditor.shapes.GShape;
import org.graphicsEditor.shapes.GOval;
import org.graphicsEditor.transformer.GDrawer;
import org.graphicsEditor.transformer.GScaler;
import org.graphicsEditor.transformer.GTransformer;
import org.graphicsEditor.transformer.GTranslator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Vector;

public class GDrawingPanel extends JPanel {
	// declaration
	private enum EDrawingState {
		eIdle,
		eTransforming
	}

	// attributes
	private EDrawingState eDrawingState;
	// components
	private final Vector<GShape> shapes;
	private BufferedImage bufferImage;
	private GTransformer transformer;
	private GShape selectedShape;
	// associations
	private GShapeToolBar toolBar;

	// constructors
	public GDrawingPanel() {
		// attributes
		this.setBackground(Color.WHITE);
		this.eDrawingState = EDrawingState.eIdle;
		// components
		this.shapes = new Vector<GShape>();
		this.bufferImage = null;
		this.transformer = null;
		this.selectedShape = null;

		MouseHandler mouseHandler = new MouseHandler();
		this.addMouseListener(mouseHandler);
		this.addMouseMotionListener(mouseHandler);
	}
	// setters and getters
	public void associateWith(GShapeToolBar toolBar) {
		this.toolBar = toolBar;
	}

	// methods
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.prepareDrawing();
		if (g != null) {
			g.drawImage(this.bufferImage, 0, 0, null);
		}
	}

	private void prepareDrawing() {
		if (getWidth() <= 0 || getHeight() <= 0) {
			return;
		}
		if (bufferImage == null
				|| bufferImage.getWidth() != getWidth()
				|| bufferImage.getHeight() != getHeight()) {
			bufferImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
			this.redraw();
		}
	}

	private void redraw() {
		Graphics2D bufferGraphics = this.bufferImage.createGraphics();
		bufferGraphics.setColor(this.getBackground());
		bufferGraphics.fillRect(0, 0, this.getWidth(), this.getHeight());
		bufferGraphics.setColor(this.getForeground());

		for (GShape shape : this.shapes) {
			shape.draw(bufferGraphics);
		}
		if (this.selectedShape != null) {
			this.selectedShape.drawAnchors(bufferGraphics);
		}
		bufferGraphics.dispose();
	}

	private boolean startTransform(int x, int y) {
		if (toolBar.getShapeType() == GConstants.EShapeType.eSelect) { // context
			if (this.selectedShape != null) {
				GShape.EAnchor eAnchor = this.selectedShape.onShape(x, y);
				if (eAnchor == GShape.EAnchor.eResize) {
					this.transformer = new GScaler(this.selectedShape);
					this.transformer.start(x, y);
					this.prepareDrawing();
					return true;
				}
			}
			for (int i = shapes.size() - 1; i >= 0; i--) {
				GShape shape = shapes.get(i);
				GShape.EAnchor eAnchor = shape.onShape(x, y);
				if (eAnchor != null) {
					this.selectedShape = shape;
					if (eAnchor == GShape.EAnchor.eRotate) {
						this.transformer = new GDrawer(shape);
					} else if (eAnchor == GShape.EAnchor.eMove) {
						this.transformer = new GTranslator(shape);
					} else { // resize
						this.transformer = new GScaler(shape);
					}
					this.transformer.start(x, y);
					this.prepareDrawing();
					return true;
				}
			}
			this.selectedShape = null;
			this.redraw();
			repaint();
			return false;
		}
		else {
			GShape currentShape = toolBar.getShapeType().getShape();
			this.shapes.add(currentShape);
			this.selectedShape = null;
			this.transformer = new GDrawer(currentShape);

			this.transformer.start(x, y);
		}
		this.prepareDrawing();
		return true;
	}

	private void keepTransform(int x, int y) {
		this.transformer.keep(x, y);
		this.redraw();
		repaint();
	}

	private void continueDrawing(int x, int y) {
		this.transformer.cont(x, y);
		this.redraw();
		repaint();
	}

	private void finishTransform(int x, int y) {
		this.transformer.finish(x, y);
		this.transformer = null;
		this.redraw();
		repaint();
	}

	private class MouseHandler implements MouseListener, MouseMotionListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == 1) { // left button
				if (e.getClickCount() == 1) { // single click
					mouseLButton1Clocked(e);
				} else if (e.getClickCount() == 2) { // double click
					mouseLButton2Clocked(e);
				}
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			if (toolBar.getShapeType().getDrawingType() == GConstants.EDrawingType.eNPoint) { // context
				if (eDrawingState == EDrawingState.eTransforming) {
					keepTransform(e.getX(), e.getY());
				}
			}
		}
		private void mouseLButton1Clocked(MouseEvent e) {
			if (toolBar.getShapeType().getDrawingType() == GConstants.EDrawingType.eNPoint) { // context
				if (eDrawingState == EDrawingState.eIdle) {  // target state
					if (startTransform(e.getX(), e.getY())) {
						eDrawingState = EDrawingState.eTransforming;
					}
				} else {
					continueDrawing(e.getX(), e.getY());
				}
			}
		}
		private void mouseLButton2Clocked(MouseEvent e) {
			if (toolBar.getShapeType().getDrawingType() == GConstants.EDrawingType.eNPoint) { // context
				if (eDrawingState == EDrawingState.eTransforming) {
					finishTransform(e.getX(), e.getY());
					eDrawingState = EDrawingState.eIdle;
				}
			}
		}
		@Override
		public void mousePressed(MouseEvent e) {
			if (toolBar.getShapeType().getDrawingType() == GConstants.EDrawingType.e2Point) {
				if (eDrawingState == EDrawingState.eIdle) {  // target state
					if (startTransform(e.getX(), e.getY())) {
						eDrawingState = EDrawingState.eTransforming;
					}
				}
			}
		}
		@Override
		public void mouseDragged(MouseEvent e) {
			if (toolBar.getShapeType().getDrawingType() == GConstants.EDrawingType.e2Point) {
				if (eDrawingState == EDrawingState.eTransforming) {
					keepTransform(e.getX(), e.getY());
				}
			}
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			if (toolBar.getShapeType().getDrawingType() == GConstants.EDrawingType.e2Point) {
				if (eDrawingState == EDrawingState.eTransforming) {
					finishTransform(e.getX(), e.getY());
					eDrawingState = EDrawingState.eIdle;
				}
			}
		}


		@Override
		public void mouseEntered(MouseEvent e) {
		}
		@Override
		public void mouseExited(MouseEvent e) {
		}

	}
}
