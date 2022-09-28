package bridge.modules;

import java.util.UUID;

public abstract class Currency {

    private final String currencyName;

    private Currency () {
        this.currencyName = null;
    }
    public Currency (final String name) {
        this.currencyName = name;
    }

    /**
     * Get amount of money from player.
     *
     * @param uuid the player's {@link UUID}
     * @return amount of money
     */
    public abstract int getCurrencyAmount(final UUID uuid);

    /**
     * Set player's amount of money.
     *
     * @param uuid the player's {@link UUID}
     * @param amount amount to set
     */
    public abstract void setCurrency(final UUID uuid, final int amount);

    /**
     * Add to player's "bank" some more money.
     *
     * @param uuid the player's {@link UUID}
     * @param add amount to add (Note that amount <b>can</b> be negative)
     */
    public void addCurrency(final UUID uuid, final int add) {
        setCurrency(uuid, getCurrencyAmount(uuid) + add);
    }

    /**
     * Check if player have enough money.
     *
     * @param uuid the player's {@link UUID}
     * @param amount the amount to check
     * @return true if he has
     */
    public boolean hasEnough(final UUID uuid, final int amount){
        return amount >= getCurrencyAmount(uuid);
    }

    /**
     * Get currency name.
     * @return the name of currency
     */
    public String getCurrencyName() {
        return currencyName;
    }
}
