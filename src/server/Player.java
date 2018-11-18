package server;

import java.util.Objects;

/**
 * Класс, описывающий игрока
 * <p>
 * Хранит его имя в игре, аватар и все счета (ЕСМ, ЕГП, деньги, фабрики).
 */
public class Player {
    private String name;
    private Avatar avatar;

    private int unitsOfProducts;     //ЕГП
    private int unitsOfResources;   //ЕСМ
    private int money;              //деньги
    private int workingFactories;                       //работающие обычные фабрики
    private int workingAutomatedFactories;              //работающие авто-фабрики
    private int underConstructionFactories;             //строящиеся обычные фабрики
    private int underConstructionAutomatedFactories;    //строящиеся авто-фабрики
    private int inAutomationNowFactories;               //автоматизируемые сейчас фабрики

    public Player(String name, Avatar avatar) {
        this.name = name;
        this.avatar = avatar;
    }

    public int getUnitsOfProducts() {
        return unitsOfProducts;
    }

    public void setUnitsOfProducts(int unitsOfProducts) {
        this.unitsOfProducts = unitsOfProducts;
    }

    public int getUnitsOfResources() {
        return unitsOfResources;
    }

    public void setUnitsOfResources(int unitsOfResources) {
        this.unitsOfResources = unitsOfResources;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getWorkingFactories() {
        return workingFactories;
    }

    public void setWorkingFactories(int workingFactories) {
        this.workingFactories = workingFactories;
    }

    public int getWorkingAutomatedFactories() {
        return workingAutomatedFactories;
    }

    public void setWorkingAutomatedFactories(int workingAutomatedFactories) {
        this.workingAutomatedFactories = workingAutomatedFactories;
    }

    public int getUnderConstructionFactories() {
        return underConstructionFactories;
    }

    public void setUnderConstructionFactories(int underConstructionFactories) {
        this.underConstructionFactories = underConstructionFactories;
    }

    public int getUnderConstructionAutomatedFactories() {
        return underConstructionAutomatedFactories;
    }

    public void setUnderConstructionAutomatedFactories(int underConstructionAutomatedFactories) {
        this.underConstructionAutomatedFactories = underConstructionAutomatedFactories;
    }

    public int getInAutomationNowFactories() {
        return inAutomationNowFactories;
    }

    public void setInAutomationNowFactories(int inAutomationNowFactories) {
        this.inAutomationNowFactories = inAutomationNowFactories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return unitsOfProducts == player.unitsOfProducts &&
                unitsOfResources == player.unitsOfResources &&
                money == player.money &&
                workingFactories == player.workingFactories &&
                workingAutomatedFactories == player.workingAutomatedFactories &&
                underConstructionFactories == player.underConstructionFactories &&
                underConstructionAutomatedFactories == player.underConstructionAutomatedFactories &&
                inAutomationNowFactories == player.inAutomationNowFactories &&
                Objects.equals(name, player.name) &&
                Objects.equals(avatar, player.avatar);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, avatar, unitsOfProducts, unitsOfResources, money, workingFactories, workingAutomatedFactories, underConstructionFactories, underConstructionAutomatedFactories, inAutomationNowFactories);
    }
}
