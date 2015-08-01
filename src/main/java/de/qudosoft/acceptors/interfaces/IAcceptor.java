package de.qudosoft.acceptors.interfaces;

import java.util.List;

/**
 * Either accepts or declines a given {@link List} of items
 * @param <T> The type of the items
 */
public interface IAcceptor<T> {

    /**
     * Returns true if the {@link List} is accepted and false otherwise.
     * @param items The {@link List} of items
     * @return true, if the {@link List} is accepted, false otherwise.
     */
    public boolean accepts(List<T> items);

}
