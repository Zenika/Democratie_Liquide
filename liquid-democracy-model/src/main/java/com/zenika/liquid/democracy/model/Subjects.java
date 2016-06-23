package com.zenika.liquid.democracy.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kepennar on 23/06/16.
 */
public class Subjects {
    private List<Subject> opened;
    private List<Subject> closed;

    public Subjects() {
    }

    public Subjects(List<Subject> opened, List<Subject> closed) {
        this.opened = opened != null ? opened : new ArrayList<>();
        this.closed = closed != null ? closed : new ArrayList<>();
    }

    public List<Subject> getOpened() {
        return opened;
    }

    public void setOpened(List<Subject> opened) {
        this.opened = opened;
    }

    public List<Subject> getClosed() {
        return closed;
    }

    public void setClosed(List<Subject> closed) {
        this.closed = closed;
    }
}
