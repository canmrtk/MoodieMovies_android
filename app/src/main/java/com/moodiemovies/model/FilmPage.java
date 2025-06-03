// com.moodiemovies.model.FilmPage.java
package com.moodiemovies.model;

import java.util.List;

public class FilmPage {
    private List<Film> content; // Film listesi (FilmSummaryDTO'lar)
    private int totalPages;
    private long totalElements;
    private int number;       // Mevcut sayfa numarası (0'dan başlar)
    private int size;         // Sayfa başına eleman sayısı
    private boolean first;
    private boolean last;
    private boolean empty;

    // Getters and Setters for all fields
    public List<Film> getContent() { return content; }
    public void setContent(List<Film> content) { this.content = content; }
    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
    public long getTotalElements() { return totalElements; }
    public void setTotalElements(long totalElements) { this.totalElements = totalElements; }
    public int getNumber() { return number; }
    public void setNumber(int number) { this.number = number; }
    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
    public boolean isFirst() { return first; }
    public void setFirst(boolean first) { this.first = first; }
    public boolean isLast() { return last; }
    public void setLast(boolean last) { this.last = last; }
    public boolean isEmpty() { return empty; }
    public void setEmpty(boolean empty) { this.empty = empty; }
}