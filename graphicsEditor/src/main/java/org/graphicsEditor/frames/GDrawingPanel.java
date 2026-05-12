package org.graphicsEditor.frames;

import org.graphicsEditor.global.GConstants;
import org.graphicsEditor.shapes.GRectangle;
import org.graphicsEditor.shapes.GShape;
import org.graphicsEditor.shapes.GOval;
import org.graphicsEditor.shapes.GShape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Vector;

public class GDrawingPanel extends JPanel {

	private GShapeToolBar toolBar;
	public void associateWith(GShapeToolBar toolBar) {
		this.toolBar = toolBar;
	}

	private enum EDrawingState {
		eIdle,
		eDrawing,
		eMoving,
		eResizing,
		eRotating,
		eShearing
	}
	private EDrawingState eDrawingState;

	private BufferedImage bufferImage;
	private Vector<GShape> shapes;
	private GShape currentShape;

	// constructors
	public GDrawingPanel() {
		// attributes
		this.setBackground(Color.WHITE);
		this.eDrawingState = EDrawingState.eIdle;
		// components list
		this.shapes = new Vector<GShape>();

		MouseHandler mouseHandler = new MouseHandler();
		this.addMouseListener(mouseHandler);
		this.addMouseMotionListener(mouseHandler);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		if (g != null) {
			g.drawImage(this.bufferImage, 0, 0, null);
		}
	}

	private void startDrawing(int x, int y) {
		if (getWidth() <= 0 || getHeight() <= 0) {
			return;
		}
		if (bufferImage == null
				|| bufferImage.getWidth() != getWidth()
				|| bufferImage.getHeight() != getHeight()) {
			bufferImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D bufferGraphics = bufferImage.createGraphics();
			bufferGraphics.setColor(getBackground());
			bufferGraphics.fillRect(0, 0, getWidth(), getHeight());
			bufferGraphics.dispose();
		}
	}

	private void startNewShape(int x, int y) {
		currentShape = toolBar.getShapeType().getShape();
		currentShape.setLocation0(x, y);
		currentShape.setLocation1(x, y);
	}

	private void startTransform(int x, int y) {
		for (GShape shape : shapes) {
			GShape.EAnchor eAnchor = shape.onShape(x, y);
			if (eAnchor != null) {
				if (eAnchor == GShape.EAnchor.eRotate) {
					eDrawingState = EDrawingState.eRotating;
				} else if (eAnchor == GShape.EAnchor.eMove) {
					eDrawingState = EDrawingState.eMoving;
				} else { // resize
					eDrawingState = EDrawingState.eResizing;
				}
				currentShape = shape;
				break;
			}
		}
	}

	private void keepTransform(int x, int y) {
		Graphics2D bufferGraphics = this.bufferImage.createGraphics();
		bufferGraphics.setColor(this.getBackground());
		bufferGraphics.fillRect(0, 0, this.getWidth(), this.getHeight());
		bufferGraphics.setColor(this.getForeground());

		if (this.eDrawingState == EDrawingState.eDrawing) {
			this.currentShape.setLocation1(x, y);
			this.currentShape.draw(bufferGraphics);
		} else if (this.eDrawingState == EDrawingState.eMoving) {
			this.currentShape.move(x, y);
		} else if (this.eDrawingState == EDrawingState.eResizing) {
			this.currentShape.resize(x, y);
		} else if (this.eDrawingState == EDrawingState.eRotating) {
			this.currentShape.rotate(x, y);
		}
		for (GShape shape : this.shapes) {
			shape.draw(bufferGraphics);
		}
		bufferGraphics.dispose();
		repaint();
	}
	private void continueDrawing(int x, int y) {

	}
	private void finishTransform(int x, int y) {
		if (this.eDrawingState == EDrawingState.eDrawing) {
			this.shapes.add(this.currentShape);
		}
		this.currentShape = null;
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
			if (eDrawingState != EDrawingState.eIdle) {
				if (toolBar.getShapeType().getDrawingType() == GConstants.EDrawingType.eNPoint) { // context
					keepTransform(e.getX(), e.getY());
				}
			}
		}
		private void mouseLButton1Clocked(MouseEvent e) {
			if (eDrawingState == EDrawingState.eIdle) {  // target state
				if (toolBar.getShapeType().getDrawingType() == GConstants.EDrawingType.eNPoint) { // context
					startNewShape(e.getX(), e.getY());
					startDrawing(e.getX(), e.getY());  // prepare for double buffering
				}
			} else {
				if (toolBar.getShapeType().getDrawingType() == GConstants.EDrawingType.eNPoint) { // context
					continueDrawing(e.getX(), e.getY());
				}
			}
		}
		private void mouseLButton2Clocked(MouseEvent e) {
			if (eDrawingState != EDrawingState.eIdle) {
				if (toolBar.getShapeType().getDrawingType() == GConstants.EDrawingType.eNPoint) { // context
					finishTransform(e.getX(), e.getY());
					eDrawingState = EDrawingState.eIdle;
				}
			}
		}
		@Override
		public void mousePressed(MouseEvent e) {
			if (eDrawingState == EDrawingState.eIdle) {  // target state
				if (toolBar.getShapeType().getDrawingType() == GConstants.EDrawingType.e2Point)
					if (toolBar.getShapeType() == GConstants.EShapeType.eSelect) { // context
						startTransform(e.getX(), e.getY());
					} else { // shapes
						startNewShape(e.getX(), e.getY());
						eDrawingState = EDrawingState.eDrawing;
					}
				startDrawing(e.getX(), e.getY());  // prepare for double buffering
			}
		}
		@Override
		public void mouseDragged(MouseEvent e) {
			if (eDrawingState != EDrawingState.eIdle) {
				if (toolBar.getShapeType().getDrawingType() == GConstants.EDrawingType.e2Point) {
					keepTransform(e.getX(), e.getY());
				}
			}
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			if (eDrawingState != EDrawingState.eIdle) {
				if (toolBar.getShapeType().getDrawingType() == GConstants.EDrawingType.e2Point) {
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
