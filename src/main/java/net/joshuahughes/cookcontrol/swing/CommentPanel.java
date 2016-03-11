package net.joshuahughes.cookcontrol.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Date;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer.UIResource;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.joshuahughes.cookcontrol.xml.Type.Comment;

public class CommentPanel extends JPanel
{
	private static final long serialVersionUID = -7308783169568700536L;
	BackedListModel<Comment> model;
	JList<Comment> list;
	JTextArea area = new JTextArea();
	public CommentPanel(List<Comment> commentList)
	{
		super(new BorderLayout());
		setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Comments"));
		list = new JList<>(model = new BackedListModel<>(commentList));
		ListCellRenderer<? super Comment> defaultRenderer = list.getCellRenderer();
		list.setCellRenderer(new ListCellRenderer<Comment>() {
			@Override
			public Component getListCellRendererComponent(JList<? extends Comment> list, Comment value, int index,
					boolean isSelected, boolean cellHasFocus) {
				UIResource component = (UIResource) defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				component.setText(new Date(value.getTime()).toString());
				return component;
			}
		});
		area.setEnabled(false);
		area.addFocusListener(new FocusAdapter() {
			Comment comment = null;
			@Override
			public void focusGained(FocusEvent e) {
				comment = list.getSelectedValue();
			}			
			public void focusLost(FocusEvent e) {
				if(comment != null)
					comment.setRemark(area.getText());
			}			
		});
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if(e.getValueIsAdjusting()) return;
				if(list.getSelectedIndex()<0)
				{
					area.setEnabled(false);
					return;
				}
				area.setEnabled(true);
				area.setText(list.getSelectedValue().getRemark());
			}
		});
		JButton add = new JButton(new AbstractAction("add"){
			private static final long serialVersionUID = -6488437817173769878L;
			@Override
			public void actionPerformed(ActionEvent e) {
				Comment comment = new Comment();
				comment.setTime(System.currentTimeMillis());
				comment.setRemark("remark:");
				model.addElement(comment);
				list.setSelectedIndex(model.getSize()-1);
			}});
		JButton delete = new JButton(new AbstractAction("delete"){
			private static final long serialVersionUID = -6488437817173769878L;
			@Override
			public void actionPerformed(ActionEvent e) {
				model.removeElement(list.getSelectedValue());
				if(model.getSize()<=0)
					area.setText("");
				else
					list.setSelectedIndex(model.getSize()-1);
			}});
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx=gbc.gridy=0;
		gbc.weightx=1;
		gbc.weighty=.9;
		gbc.gridwidth=2;
		gbc.fill = GridBagConstraints.BOTH;
		panel.add(new JScrollPane(list),gbc);
		gbc.weighty=.1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridwidth=1;
		gbc.gridy++;
		panel.add(add,gbc);
		gbc.gridx++;
		panel.add(delete,gbc);
		add(panel,BorderLayout.WEST);
		add(new JScrollPane(area),BorderLayout.CENTER);
	}
}
