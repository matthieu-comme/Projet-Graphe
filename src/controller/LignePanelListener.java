package controller;

import java.awt.*;
import java.util.ArrayList;

import model.Arc;
import model.Sommet;

public interface LignePanelListener {
    public ArrayList<Sommet> getGraphe();

    public void onClickAddSommet(Point p);

    public void onClickDelArc(Arc a);

    public void onClickShowArcInfo(Arc a);
}
