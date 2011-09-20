package isnork.sim.ui;

import isnork.sim.Board;
import isnork.sim.BoardPanel;
import isnork.sim.GameConfig;
import isnork.sim.SeaLifePrototype;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

public final class BoardEditor extends JPanel implements ItemListener,
		ChangeListener, ActionListener {
	class BoardEditingPanel extends JPanel {
		ArrayList<SeaLifePrototype> b;
		JPanel sealifeList;
		JScrollPane s;
		int n = 0;

		public BoardEditingPanel(ArrayList<SeaLifePrototype> b) {
			this.b = b;
			s = new JScrollPane();
			sealifeList = new JPanel();
			this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			setBoard(b);
			s.setViewportView(sealifeList);
			this.add(s);
			sealifeList.setLayout(new BoxLayout(sealifeList, BoxLayout.Y_AXIS));

		}

		public void addNewSealifePrototype() {
			SeaLifePrototype z = new SeaLifePrototype();
			b.add(z);
			addSealife(z);
			repaint();
			revalidate();
		}

		private void addSealife(SeaLifePrototype t) {
			final SeaLifePrototype z = t;
			JPanel ret = new JPanel();
			ret.setLayout(new GridLayout(0, 2));
			ret.setMaximumSize(new Dimension(900, 200));

			n++;
			ret.add(new JLabel("#" + n));
			final JButton remove = new JButton("(Remove below)");
			remove.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					b.remove(z);
					setBoard(b);
				}
			});
			ret.add(remove);
			ret.add(new JLabel("Name: "));
			final JTextField nameField = new JTextField(t.getName());
			ret.add(nameField);
			ret.add(new JLabel("Moves (1 or 0): "));
			final JTextField speedField = new JTextField(""+t.getSpeed());
			ret.add(speedField);
			ret.add(new JLabel("Happiness Score: "));
			final JTextField happyField = new JTextField(""+t.getHappiness());
			ret.add(happyField);
			ret.add(new JLabel("Dangerous (Y/N): "));
			final JTextField dangerField = new JTextField(
					(t.isDangerous() ? "Y" : "N"));
			ret.add(dangerField);
			ret.add(new JLabel("Minimum Instances: "));
			final JTextField minField = new JTextField(""+t.getMinCount());
			ret.add(minField);
			ret.add(new JLabel("Maximum Instances: "));
			final JTextField maxField = new JTextField(""+t.getMaxCount());
			ret.add(maxField);
			ret.add(new JLabel("Icon Filename: "));
			final JTextField colorField = new JTextField(t.getFilename());
			ret.add(colorField);
			colorField.addKeyListener(new KeyListener() {
				
				@Override
				public void keyTyped(KeyEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void keyReleased(KeyEvent e) {
					z.setFilename(colorField.getText());
				}
				
				@Override
				public void keyPressed(KeyEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
			nameField.addKeyListener(new KeyListener() {

				@Override
				public void keyTyped(KeyEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void keyReleased(KeyEvent e) {
					z.setName(nameField.getText());
				}

				@Override
				public void keyPressed(KeyEvent e) {
					// TODO Auto-generated method stub

				}
			});

			speedField.addKeyListener(new KeyListener() {

				@Override
				public void keyTyped(KeyEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void keyReleased(KeyEvent e) {
					z.setSpeed(Integer.valueOf(speedField.getText()));
				}

				@Override
				public void keyPressed(KeyEvent e) {
					// TODO Auto-generated method stub

				}
			});
			happyField.addKeyListener(new KeyListener() {

				@Override
				public void keyTyped(KeyEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void keyReleased(KeyEvent e) {
					z.setHappiness(Integer.valueOf(happyField.getText()));
				}

				@Override
				public void keyPressed(KeyEvent e) {
					// TODO Auto-generated method stub

				}
			});
			minField.addKeyListener(new KeyListener() {

				@Override
				public void keyTyped(KeyEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void keyReleased(KeyEvent e) {
					z.setMinCount(Integer.valueOf(minField.getText()));
				}

				@Override
				public void keyPressed(KeyEvent e) {
					// TODO Auto-generated method stub

				}
			});
			maxField.addKeyListener(new KeyListener() {

				@Override
				public void keyTyped(KeyEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void keyReleased(KeyEvent e) {
					z.setMaxCount(Integer.valueOf(maxField.getText()));
				}

				@Override
				public void keyPressed(KeyEvent e) {
					// TODO Auto-generated method stub

				}
			});
			dangerField.addKeyListener(new KeyListener() {

				@Override
				public void keyTyped(KeyEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void keyReleased(KeyEvent e) {
					z.setDangerous((dangerField.getText().equals("Y")));
				}

				@Override
				public void keyPressed(KeyEvent e) {
					// TODO Auto-generated method stub

				}
			});

			ret.add(new JSeparator(JSeparator.HORIZONTAL));
			ret.add(new JSeparator(JSeparator.HORIZONTAL));

			sealifeList.add(ret);
		}

		public void setBoard(ArrayList<SeaLifePrototype> b) {
			this.b = b;
			n = 0;
			sealifeList.removeAll();
			for (SeaLifePrototype t : b) {
				addSealife(t);
			}
			s.setViewportView(sealifeList);
		}
	}

	private static final long serialVersionUID = 1L;
	JSpinner heightSpinner;
	JSpinner widthSpinner;
	JButton resetButton;
	JButton loadButton;
	JButton removeButton;
	JButton saveButton;
	JComboBox editBox;
	JSpinner nestsSpinner;
	JSpinner blockedSpinner;
	JButton randomButton;

	boolean loading = false;

	ArrayList<SeaLifePrototype> board;
	BoardEditingPanel boardPanel;
	JFileChooser chooser;

	public BoardEditor() {
		init();
	}

	private void init() {
		chooser = new JFileChooser();
		chooser.setFileFilter(new XMLFilter());
		setLayout(new BorderLayout());
		JPanel controlPanel = new JPanel();
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		controlPanel.setLayout(layout);
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		controlPanel.setPreferredSize(new Dimension(200, 300));

		controlPanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(), "Configuration"));

		JPanel panel = new JPanel(new FlowLayout());

		resetButton = new JButton("Reset");
		resetButton.addActionListener(this);
		resetButton.setActionCommand("RESET");
		panel = new JPanel();
		panel.add(resetButton);
		layout.setConstraints(panel, c);
		controlPanel.add(panel);

		JButton addButton = new JButton("Add New");
		addButton.addActionListener(this);
		addButton.setActionCommand("ADD");
		panel = new JPanel();
		panel.add(addButton);
		layout.setConstraints(panel, c);
		controlPanel.add(panel);

		loadButton = new JButton("Load");
		loadButton.addActionListener(this);
		loadButton.setActionCommand("LOAD");
		panel = new JPanel();
		panel.add(loadButton);
		layout.setConstraints(panel, c);
		controlPanel.add(panel);

		saveButton = new JButton("Save");
		saveButton.addActionListener(this);
		saveButton.setActionCommand("SAVE");
		panel = new JPanel();
		panel.add(saveButton);
		layout.setConstraints(panel, c);
		controlPanel.add(panel);

		loading = false;

		board = new ArrayList<SeaLifePrototype>();
		boardPanel = new BoardEditingPanel(board);

		scroller = new JScrollPane();
		scroller.getViewport().add(boardPanel);

		this.add(controlPanel, BorderLayout.EAST);
		this.add(scroller, BorderLayout.CENTER);
	}

	JScrollPane scroller;

	public void setBoard(ArrayList<SeaLifePrototype> b) {
		board = b;
		boardPanel.setBoard(b);

		scroller.revalidate();
		scroller.repaint();
	}

	// public final static void main(String [] args){
	// JFrame f = new JFrame();
	// f.setPreferredSize(new Dimension(600, 600));
	// f.getContentPane().add(new BoardEditor(engine));
	// f.pack();
	// f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	// f.setVisible(true);
	// }

	public void itemStateChanged(ItemEvent arg0) {

	}

	public void stateChanged(ChangeEvent arg0) {

	}

	private class XMLFilter extends FileFilter {

		@Override
		public boolean accept(File f) {
			return f.getName().toLowerCase().endsWith(".xml");
		}

		@Override
		public String getDescription() {
			return "XML Files";
		}

	}

	public void actionPerformed(ActionEvent arg0) {
		// find our super parent frame -- needed for dialogs
		Component c = this;
		while (null != c.getParent())
			c = c.getParent();

		if (arg0.getActionCommand().equalsIgnoreCase("SAVE")) {
			chooser.setCurrentDirectory(new File("boards"));
			int returnVal = chooser.showSaveDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				try {
					XMLEncoder d = new XMLEncoder(new FileOutputStream(
							chooser.getSelectedFile()));
					for(SeaLifePrototype t : board)
					{
						d.writeObject(t);
					}
					d.close();
					JOptionPane.showMessageDialog((Frame) c,
							"File saved successfully.", "Success",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception e) {
					JOptionPane.showMessageDialog((Frame) c, e, "Save Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		} else if (arg0.getActionCommand().equalsIgnoreCase("LOAD")) {
			chooser.setCurrentDirectory(new File("boards"));
			int returnVal = chooser.showOpenDialog(this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {

				loading = true;

				try {
					XMLDecoder d = new XMLDecoder(new FileInputStream(
							chooser.getSelectedFile()));
					board= new ArrayList<SeaLifePrototype>();
					SeaLifePrototype n = (SeaLifePrototype) d.readObject();
					while (n != null) {
						board.add(n);
						try {
							n = (SeaLifePrototype) d.readObject();
						} catch (ArrayIndexOutOfBoundsException e) {
							n = null;
						}
					}
				} catch (IOException e) {
					System.err.println("Error reading configuration file:"
							+ e.getMessage());
					e.printStackTrace();
					System.exit(-1);
				}

				setBoard(board);
				JOptionPane.showMessageDialog((Frame) c,
						"File loaded successfully.", "Success",
						JOptionPane.INFORMATION_MESSAGE);
				loading = false;
				// }catch(IOException e){
				// // e.printStackTrace();
				// JOptionPane.showMessageDialog( (Frame)c, e, "Load Error",
				// JOptionPane.ERROR_MESSAGE);
				// loading = false;
				// }
			}
		} else if (arg0.getActionCommand().equalsIgnoreCase("RESET")) {
			board = new ArrayList<SeaLifePrototype>();
			boardPanel.setBoard(board);
			this.repaint();
			boardPanel.repaint();
		} else if (arg0.getActionCommand().equalsIgnoreCase("ADD")) {
			boardPanel.addNewSealifePrototype();
		}
	}
}
