package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.logic.items.Key;
import com.codecool.dungeoncrawl.logic.items.KeyType;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Player extends Actor {

    private List<Item> inventory = new ArrayList<>();
    private final String[] notWalkable = {"wall", "wall2", "wall3", "tree", "statue", "statue2", "empty", "grave", };
    private boolean onItem;
    private int health = 1000;
    private int strength = 100;
    private boolean onDoorDown = false;
    private boolean onDoorUp = false;


    public Player(Cell cell) {
        super(cell);
    }

    public String getTileName() {
        return "player";
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public void addItemToInventory() {
        inventory.add(this.getCell().getItem());
    }

    public boolean isOnItem() {
        return onItem;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    @Override
    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public boolean isOnDoorDown() {
        return onDoorDown;
    }

    public boolean isOnDoorUp() {
        return onDoorUp;
    }

    @Override
    public void move(int dx, int dy) {
        onItem = false;
        onDoorDown = false;
        onDoorUp = false;
        Cell nextCell = cell.getNeighbor(dx, dy);
        if (nextCell.getTileName().equals("doorlvl1out") || nextCell.getTileName().equals("doorlvl2out")){
            if(!hasKey(nextCell.getTileName())) {return;}
            onDoorDown = true;
            cell.setActor(null);
            return;
        }
        if (nextCell.getTileName().equals("doorlvl2in") || nextCell.getTileName().equals("doorlvl3in")){
            onDoorUp = true;
            cell.setActor(null);
            return;
        }
        if (nextCell.getItem() != null) {
            onItem = true;
        }
        else if (Arrays.asList(notWalkable).contains(nextCell.getTileName())) {
            return;
        } else if (nextCell.getActor() != null) {
            fight(cell.getActor(), nextCell.getActor());
        }
        cell.setActor(null);
        nextCell.setActor(this);
        cell.setItem(null);
        cell = nextCell;
    }

    public void fight(Actor player, Actor enemy) {
        System.out.println(player);
        System.out.println(enemy);
        Actor attacker = player;
        Actor defender = enemy;
        Actor temp;
        while (getHealth() > 0 && enemy.getHealth() > 0) {
            attack(attacker, defender);
            temp = attacker;
            attacker = defender;
            defender = temp;
        }
        if (0 >= player.getHealth()){
            System.exit(0);
        }
        if (attacker instanceof Player) {
            player.setHealth(attacker.getHealth());
            player.setStrength(attacker.getStrength()+10);
        } else {
            player.setHealth(defender.getHealth());
            player.setStrength(defender.getStrength()+10);
        }
    }

    public void attack(Actor attacker, Actor defender) {
        int attStrength = attacker.getStrength();
        int defHealth = defender.getHealth();
        defender.setHealth(defHealth-attStrength);
    }

    public boolean hasKey(String door){
        List<String> keyNames = new ArrayList<>();
        for (Item item : inventory) {
            if (item instanceof Key){
                keyNames.add(item.getTileName());
            }
        }
        System.out.println(keyNames);
        if(door.equals("doorlvl1out") && keyNames.contains("bronze")){return true;}
        else if(door.equals("doorlvl2out") && keyNames.contains("silver")){return true;}
        return false;
    }
}
