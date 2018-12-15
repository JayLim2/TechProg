package samara.university.common.entities;

public class Bid {
    private Player player;
    private boolean type; //FALSE - покупка ЕСМ, TRUE - продажа ЕГП
    private int count;
    private int price;

    private Bid(Player player, boolean type, int count, int price) {
        this.player = player;
        this.type = type;
        this.count = count;
        this.price = price;
    }

    public static Bid createBid(Player player, boolean type, int count, int price) {
        if (count != 0) {
            return new Bid(player, type, count, price);
        }
        return null;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        if (player != null) {
            this.player = player;
        }
    }

    public boolean type() {
        return type;
    }

    public int getCount() {
        return count;
    }

    public int getPrice() {
        return price;
    }
}