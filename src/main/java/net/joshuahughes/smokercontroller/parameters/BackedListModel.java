package net.joshuahughes.smokercontroller.parameters;

import java.util.List;

import javax.swing.AbstractListModel;

public class BackedListModel<E> extends AbstractListModel<E>
{
	List<E> delegate;
	public BackedListModel(List<E> list)
	{
		delegate = list;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 2676850578349675746L;

	@Override
	public int getSize() {
		return delegate.size();
	}

	@Override
	public E getElementAt(int index) {
		return delegate.get(index);
	}
    public void addElement(E element) {
        int index = delegate.size();
        delegate.add(element);
        fireIntervalAdded(this, index, index);
    }
    public boolean removeElement(Object obj) {
        int index = indexOf(obj);
        boolean rv = delegate.remove(obj);
        if (index >= 0) {
            fireIntervalRemoved(this, index, index);
        }
        return rv;
    }
    public int indexOf(Object elem) {
        return delegate.indexOf(elem);
    }

}
