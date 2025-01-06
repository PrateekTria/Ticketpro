package com.ticketpro.util;

import android.view.View;

public interface TableAdapter<T> {
    
	int getRowCount();

    int getColumnWeight(int column);

    int getColumnCount();

    T getItem(int row, int column);

    View getView(int row, int column);
    
}