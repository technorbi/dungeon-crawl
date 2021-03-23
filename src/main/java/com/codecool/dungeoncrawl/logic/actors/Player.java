package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.items.Item;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Player extends Actor {

    private List<Item> inventory = new ArrayList<>();
    private final String[] notWalkable = {"wall", "tree", "statue", "empty"};

    public Player(Cell cell) {
        super(cell);
    }

    public String getTileName() {
        return "player";
    }

    public List<Item> getInventory() {
        return inventory;
    }

    private boolean onItem;

    public void addItemToInventory() {
        inventory.add(this.getCell().getItem());
        System.out.println(inventory);
    }

    public boolean isOnItem() {
        return onItem;
    }

    @Override
    public void move(int dx, int dy) {
        Cell nextCell = cell.getNeighbor(dx, dy);
        if (nextCell.getItem() != null) {
            onItem = true;
        }
        else if (Arrays.asList(notWalkable).contains(nextCell.getTileName())) {
            return;
        }
        else if (nextCell.getTileName().equals("lvl1doorin")){
            //TODO hasKey();
            return;
        }
        cell.setActor(null);
        nextCell.setActor(this);
        cell = nextCell;
        System.out.println(nextCell.getItem());
    }
}
